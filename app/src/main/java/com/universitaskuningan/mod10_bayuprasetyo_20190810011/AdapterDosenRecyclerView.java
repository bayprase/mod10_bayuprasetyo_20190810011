package com.universitaskuningan.mod10_bayuprasetyo_20190810011;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterDosenRecyclerView extends RecyclerView.Adapter<AdapterDosenRecyclerView.ViewHolder> {

    private ArrayList<Dosen> daftarDosen;
    private Context context;

    public AdapterDosenRecyclerView(ArrayList<Dosen> dosens, Context ctx){
        daftarDosen = dosens;
        daftarDosen = dosens;
        context = ctx;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvTitleNama, tvTitleJA;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_nikdosen);
//            tvTitleNama = itemView.findViewById(R.id.tv_namadosen);
//            tvTitleJA = itemView.findViewById(R.id.tv_jadosen);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dosen, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String nik = daftarDosen.get(position).getNik();
//        final String nama = daftarDosen.get(position).getNama();
//        final String ja = daftarDosen.get(position).getJa();
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.tvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // Update
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_view);
                dialog.setTitle("Pilih Aksi");
                dialog.show();

                Button editButton = dialog.findViewById(R.id.bt_edit_data);
                Button deleteButton = dialog.findViewById(R.id.delete_data);

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        context.startActivity(DBCreateActivity.getActIntent((Activity)context).putExtra("data",daftarDosen.get(position)));
                    }
                });

                // Delete
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("dosen").child(daftarDosen.get(position).getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Data Dosen Berhasil dihapus", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                return true;
            }
        });

        holder.tvTitle.setText("NIK Dosen: "+nik);
    }

    @Override
    public int getItemCount() {
        return daftarDosen.size();
    }


}
