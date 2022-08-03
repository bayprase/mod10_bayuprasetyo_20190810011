package com.universitaskuningan.mod10_bayuprasetyo_20190810011;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBCreateActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    private Button btSubmit;
    private EditText etNik, etNama;
    private Spinner etJa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbcreate);

        etNik = findViewById(R.id.nik);
        etNama = findViewById(R.id.nama);
        etJa = findViewById(R.id.spinnerJA);

        btSubmit = findViewById(R.id.bt_submit);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        final Dosen dosen = (Dosen)getIntent().getSerializableExtra("data");
        if (dosen != null){
            etNik.setText(dosen.getNik());
            etNama.setText(dosen.getNama());
            etJa.getSelectedItem().toString();
            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dosen.setNik(etNik.getText().toString());
                    dosen.setNama(etNama.getText().toString());
                    dosen.setJa(etJa.getSelectedItem().toString());
                    updateDosen(dosen);
                }
            });
        }else {

            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isEmpty(etNik.getText().toString()) && !isEmpty(etNama.getText().toString())) {
                        submitDosen(new Dosen(etNik.getText().toString(), etNama.getText().toString(), etJa.getSelectedItem().toString()));
                    } else {
                        Snackbar.make(findViewById(R.id.bt_submit), "Data Dosen tidak boleh kosong", Snackbar.LENGTH_LONG).show();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(etNama.getWindowToken(), 0);
                    }

                }
            });
        }
    }

    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s);
    }

    private void updateDosen(Dosen dosen){
        databaseReference.child("dosen").child(dosen.getKey()).setValue(dosen).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar.make(findViewById(R.id.bt_submit), "Data barhasil ditambahkan", Snackbar.LENGTH_LONG).setAction("OKE", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        });
    }

    private void submitDosen(Dosen dosen){
        databaseReference.child("dosen").push().setValue(dosen).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                etNik.setText("");
                etNama.setText("");
                etJa.getSelectedItem().toString();
                Snackbar.make(findViewById(R.id.bt_submit), "Data barhasil ditambahkan", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public static Intent getActIntent(Activity activity){
        return new Intent(activity, DBCreateActivity.class);
    }
}