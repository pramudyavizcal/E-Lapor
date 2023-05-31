package com.project.elapor.ui.pengaduan_terkirim;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.elapor.R;
import com.project.elapor.ui.pengaduan_terkirim.message.MessageActivityTerkirim;


import java.util.ArrayList;

public class PengaduanAdapterTerkirim extends RecyclerView.Adapter<PengaduanAdapterTerkirim.ViewHolder> {



    private final ArrayList<PengaduanModelTerkirim> listPengaduan = new ArrayList<>();

    String role, page;
    public PengaduanAdapterTerkirim(String role, String page) {
        this.role = role;
        this.page = page;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<PengaduanModelTerkirim> items) {
        listPengaduan.clear();
        listPengaduan.addAll(items);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public PengaduanAdapterTerkirim.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pengaduan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PengaduanAdapterTerkirim.ViewHolder holder, int position) {
        holder.bind(listPengaduan.get(position), role, page, listPengaduan);
    }

    @Override
    public int getItemCount() {
        return listPengaduan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, unit, lastMessage, date;
        ImageView image, check, delete,imagex;
        ConstraintLayout cv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            unit = itemView.findViewById(R.id.unit);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            date = itemView.findViewById(R.id.date);
            image = itemView.findViewById(R.id.image);
            imagex = itemView.findViewById(R.id.imagex);
            check = itemView.findViewById(R.id.imageView);
            cv = itemView.findViewById(R.id.cv);
            delete = itemView.findViewById(R.id.delete);
        }

        @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
        public void bind(PengaduanModelTerkirim model, String role, String page, ArrayList<PengaduanModelTerkirim> listPengaduan) {
            if(page.equals("dashboard")) {
                check.setVisibility(View.VISIBLE);
            } else {
                delete.setVisibility(View.GONE);
            }

            if(role.equals("user")){
                if(model.getAdminImage().equals("null")){
                    imagex.setVisibility(View.VISIBLE);
                    image.setVisibility(View.INVISIBLE);
                }else {
                    Glide.with(itemView.getContext())
                            .load(model.getAdminImage())
                            .into(image);
                    image.setVisibility(View.VISIBLE);
                }
                name.setText(model.getAdminName());
                unit.setText("Unit: " +model.getAdminUnit());
            } else {
                if(model.getUserImage().equals("null")){
                    imagex.setVisibility(View.VISIBLE);
                    image.setVisibility(View.INVISIBLE);
                }else {
                    Glide.with(itemView.getContext())
                            .load(model.getUserImage())
                            .into(image);
                    image.setVisibility(View.VISIBLE);
                }
                name.setText(model.getUserName());
                unit.setText("Unit: " + model.getUserUnit());
            }
            lastMessage.setText("Pesan: " + model.getMessage());
            date.setText(model.getDate());
            cv.setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), MessageActivityTerkirim.class);
                intent.putExtra(MessageActivityTerkirim.EXTRA_DATA, model);
                intent.putExtra(MessageActivityTerkirim.ROLE, role);
                itemView.getContext().startActivity(intent);
            });

            delete.setOnClickListener(view -> {
                new AlertDialog.Builder(itemView.getContext())
                        .setTitle("Konfirmasi Laporan Selesai")
                        .setMessage("Anda yakin ingin menyelesaikan laporan ini?")
                        .setIcon(R.drawable.ic_baseline_warning_24)
                        .setPositiveButton("SETUJU", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            ProgressDialog mProgressDialog = new ProgressDialog(itemView.getContext());

                            mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
                            mProgressDialog.setCanceledOnTouchOutside(false);
                            mProgressDialog.show();

                            FirebaseFirestore
                                    .getInstance()
                                    .collection("report")
                                    .document(model.getUid())
                                    .update("status", "finish")
                                    .addOnCompleteListener(task -> {
                                        if(task.isSuccessful()) {
                                            //listPengaduan.remove(listPengaduan.get(getLayoutPosition()));
                                            delete.setColorFilter(Color.argb(255, 0, 255, 0));
                                            notifyDataSetChanged();
                                            mProgressDialog.dismiss();
                                            Toast.makeText(itemView.getContext(), "Berhasil menyelesaikan laporan.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            mProgressDialog.dismiss();
                                            Toast.makeText(itemView.getContext(), "Gagal menyelesaikan laporan, mohon periksa internet anda dan coba lagi nanti.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        })
                        .setNegativeButton("TIDAK", null)
                        .show();
            });

        }
    }
}
