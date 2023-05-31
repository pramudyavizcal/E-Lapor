package com.project.elapor.ui.pengaduan_terkirim.message;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.elapor.R;
import com.project.elapor.databinding.ActivityMessageBinding;
import com.project.elapor.databinding.ActivityMessageTerkirimBinding;
import com.project.elapor.ui.pengaduan_masuk.PengaduanModel;
import com.project.elapor.ui.pengaduan_masuk.message.MessageModel;
import com.project.elapor.ui.pengaduan_terkirim.PengaduanModelTerkirim;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class MessageActivityTerkirim extends AppCompatActivity {

    public static final String EXTRA_DATA = "data";
    public static final String ROLE = "role";
    private ActivityMessageTerkirimBinding binding;
    private PengaduanModelTerkirim model;
    private static final int REQUEST_IMAGE_FROM_CAMERA = 1001;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 1002;
    private String imageText;
    private MessageAdapterTerkirim adapter;
    private String role;
    private final ArrayList<MessageModelTerkirim> retrieveLastMessage = new ArrayList<>();
    private long responseTimeBefore = 0L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMessageTerkirimBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = getIntent().getParcelableExtra(EXTRA_DATA);
        role = getIntent().getStringExtra(ROLE);
        if (role.equals("user")) {
            if(model.getStatus().equals("finish")){
               binding.finish.setVisibility(View.GONE);
                binding.attach.setEnabled(false);
                binding.messageEt.setEnabled(false);
                binding.messageEt.setHint("LAPORAN TELAH DISELESAIKAN");
                binding.send.setEnabled(false);
            }else{
                binding.finish.setVisibility(View.VISIBLE);
            }
            if(model.getAdminImage().equals("null")){
                binding.imagex.setVisibility(View.VISIBLE);
                binding.image.setVisibility(View.INVISIBLE);
            }else {
                Glide.with(this)
                        .load(model.getAdminImage())
                        .into(binding.image);
                binding.image.setVisibility(View.VISIBLE);
                binding.imagex.setVisibility(View.INVISIBLE);
            }

            binding.name.setText(model.getAdminName());
        } else {
            if(model.getUserImage().equals("null")){
                binding.imagex.setVisibility(View.VISIBLE);
                binding.image.setVisibility(View.INVISIBLE);
            }else {
                Glide.with(this)
                        .load(model.getUserImage())
                        .into(binding.image);
                binding.image.setVisibility(View.VISIBLE);
                binding.imagex.setVisibility(View.INVISIBLE);
            }

            binding.name.setText(model.getUserName());
            binding.finish.setVisibility(View.VISIBLE);
        }
        // LOAD CHAT HISTORY
        initRecyclerView();
        initViewModel();

        // kirim pesan
        binding.send.setOnClickListener(view -> {
            String message = binding.messageEt.getText().toString().trim();
            if (message.isEmpty()) {
                Toast.makeText(MessageActivityTerkirim.this, "Pesan tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else {
                sendMessage(message, false);

            }
        });

        // KLIK BERKAS
        binding.attach.setOnClickListener(view -> {
            chooseCameraOrGallery();
        });

        binding.finish.setOnClickListener(view -> new AlertDialog.Builder(MessageActivityTerkirim.this)
                .setTitle("Konfirmasi Menyelesaikan Laporan")
                .setMessage("Apakah anda yakin ingin menyelesaikan laporan ini ?")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    finishReport();
                })
                .setNegativeButton("TIDAK", null)
                .show());
    }

    private void chooseCameraOrGallery() {
        String[] options = {"Unggah Gambar Melalui Kamera", "Unggah Gambar Melalui Galeri"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilihan");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // UNGGAH GAMBAR
                dialog.dismiss();
                ImagePicker.with(MessageActivityTerkirim.this)
                        .cameraOnly()
                        .compress(100)
                        .start(REQUEST_IMAGE_FROM_CAMERA);
            } else if (which == 1) {
                // UNGGAH GAMBAR
                dialog.dismiss();
                ImagePicker.with(MessageActivityTerkirim.this)
                        .galleryOnly()
                        .compress(100)
                        .start(REQUEST_IMAGE_FROM_GALLERY);
            }
        });
        builder.create().show();
    }

    private void finishReport() {
        FirebaseFirestore
                .getInstance()
                .collection("report")
                .document(model.getUid())
                .update("status", "finish")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            binding.finish.setVisibility(View.GONE);
                            Toast.makeText(MessageActivityTerkirim.this, "Laporan ini sukses diselesaikan!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void sendMessage(String message, boolean isText) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat getDate = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss");
        String format = getDate.format(new Date());
        long timeInMillis = System.currentTimeMillis();

        if(role.equals("user")) {
            MessageDatabaseTerkirim.sendChat(message, format, model.getUid(), model.getUserUid(), isText,model.getUserName(), timeInMillis);
        } else {
            MessageDatabaseTerkirim.sendChat(message, format, model.getUid(), model.getAdminUid(), isText,model.getAdminName(), timeInMillis);
            getLastResponseTime();
        }

        binding.messageEt.getText().clear();
        imageText = null;

        // LOAD CHAT HISTORY
        initRecyclerView();
        initViewModel();

        if(role.equals("admin")) {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(retrieveLastMessage.size() > 0) {
                        for(int i=0; i<1; i++) {
                            if(!Objects.equals(retrieveLastMessage.get(i).getUid(), retrieveLastMessage.get(i + 1).getUid())) {
                                long differentResponseTime = (retrieveLastMessage.get(i+1).getTimeInMillis() - retrieveLastMessage.get(i).getTimeInMillis()) / 1000;
                                long responseTime;
                                if(responseTimeBefore > 0) {
                                    responseTime = (differentResponseTime + responseTimeBefore) / 2;
                                } else {
                                    responseTime = differentResponseTime;
                                }
                                HashMap<String, Object> data = new HashMap<>();
                                data.put("responseTime", responseTime);
                                data.put("uid", model.getAdminUid());
                                FirebaseFirestore
                                        .getInstance()
                                        .collection("response_time")
                                        .document(model.getUid())
                                        .set(data)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    getLastResponseTime();
                                                }
                                            }
                                        });
                            }
                        }
                    }
                }
            }, 1000);
        }
    }

    private void getLastResponseTime() {
        FirebaseFirestore
                .getInstance()
                .collection("response_time")
                .document(model.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            responseTimeBefore = (long) documentSnapshot.get("responseTime");
                        }
                    }
                });
    }

    private void initRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.chatRv.setLayoutManager(linearLayoutManager);

        if (role.equals("user")) {
            adapter = new MessageAdapterTerkirim(model.getUserUid());
        } else {
            adapter = new MessageAdapterTerkirim(model.getAdminUid());
        }
        binding.chatRv.setAdapter(adapter);

    }

    private void initViewModel() {

        MessageViewModelTerkirim viewModel = new ViewModelProvider(this).get(MessageViewModelTerkirim.class);

        viewModel.setListMessage(model.getUid());
        viewModel.getMessage().observe(this, messageList -> {
            if (messageList.size() > 0) {
                if(messageList.size() >= 2) {
                    retrieveLastMessage.clear();
                    retrieveLastMessage.add(messageList.get(messageList.size() - 2));
                    retrieveLastMessage.add(messageList.get(messageList.size() - 1));
                }
                adapter.setData(messageList);
                viewModel.getNama().getValue();
            }
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_FROM_GALLERY || requestCode == REQUEST_IMAGE_FROM_CAMERA) {
                uploadPicture(data.getData());
            }
        }
    }

    private void uploadPicture(Uri data) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        String imageFileName = "message/data_" + System.currentTimeMillis() + ".png";

        mStorageRef.child(imageFileName).putFile(data)
                .addOnSuccessListener(taskSnapshot ->
                        mStorageRef.child(imageFileName).getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    mProgressDialog.dismiss();
                                    imageText = uri.toString();
                                    sendMessage(imageText, true);
                                })
                                .addOnFailureListener(e -> {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(MessageActivityTerkirim.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                                    Log.d("imageDp: ", e.toString());
                                }))
                .addOnFailureListener(e -> {
                    mProgressDialog.dismiss();
                    Toast.makeText(MessageActivityTerkirim.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                    Log.d("imageDp: ", e.toString());
                });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}