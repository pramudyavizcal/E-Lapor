package com.project.elapor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.elapor.databinding.ActivityRegisterBinding;
import com.project.elapor.databinding.ActivitySettingBinding;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;
    private String dp;
    private static final int REQUEST_FROM_GALLERY = 1001;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.submitPerubahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formValidation("user");
            }
        });


        // KLIK TAMBAH GAMBAR
        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(SettingActivity.this)
                        .galleryOnly()
                        .compress(100)
                        .start(REQUEST_FROM_GALLERY);
            }
        });
ambildata();
    }
    private void ambildata() {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String image = "" + documentSnapshot.get("image");
                        String name = "" + documentSnapshot.get("name");
                        String nip = "" + documentSnapshot.get("nip");
                        String unit = "" + documentSnapshot.get("unit");
                        String email = "" + documentSnapshot.get("email");
                        String password = "" + documentSnapshot.get("password");
                        binding.name.setText(name);
                        binding.nip.setText(nip);
                        binding.unit.setText(unit);
                        binding.email.setText(email);
                        binding.password.setText(password);
                        if (image.equals("null")){
                            binding.imageHint.setVisibility(View.VISIBLE);
                        }else {
                            Glide.with(SettingActivity.this)
                                    .load(image)
                                    .centerCrop()
                                    .into(binding.image);
                            binding.image.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void formValidation(String role) {
        String nip = binding.nip.getText().toString().trim();
        String email = binding.email.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        String unit = binding.unit.getText().toString().trim();
        String name = binding.name.getText().toString().trim();

        if(nip.isEmpty()) {
            Toast.makeText(this, "NIP tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        else if(email.isEmpty()) {
            Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
//        else if(password.isEmpty()) {
//            Toast.makeText(this, "Kata Sandi tidak boleh kosong", Toast.LENGTH_SHORT).show();
//        }
        else if(unit.isEmpty()) {
            Toast.makeText(this, "Unit Kerja tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        else if(name.isEmpty()) {
            Toast.makeText(this, "Nama Lengkap tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
//        else if(dp == null) {
//            Toast.makeText(this, "Foto Profil tidak boleh kosong", Toast.LENGTH_SHORT).show();
//        }
        else  {

            binding.progressCircular.setVisibility(View.VISIBLE);
            /// create account

                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                HashMap<String, Object> data = new HashMap<>();
                                //data.put("uid", uid);
                                data.put("name", name);
                                data.put("nip", nip);
                                data.put("email", email);
                                data.put("password", password);
                                data.put("unit", unit);
                                data.put("image", dp);
                                //data.put("role", role);

                                FirebaseFirestore.getInstance()
                                        .collection("users")
                                        .document(uid)
                                        .update(data)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    binding.progressCircular.setVisibility(View.GONE);
                                                    showSuccessDialog();

                                                } else {
                                                    binding.progressCircular.setVisibility(View.GONE);
                                                    showFailureDialog();
                                                }
                                            }
                                        });
                            }

        }


    /// munculkan dialog ketika gagal registrasi
    private void showFailureDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal update data")
                .setMessage("Silahkan coba lagi")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }

    /// munculkan dialog ketika sukses registrasi
    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil update data")
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onBackPressed();
                })
                .show();
    }

    /// fungsi untuk memvalidasi kode berdasarkan inisiasi variabel di atas tadi
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FROM_GALLERY) {
                uploadProfileDp(data.getData());
            }
        }
    }


    /// fungsi untuk mengupload foto kedalam cloud storage
    private void uploadProfileDp(Uri data) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        String imageFileName = "profile/data_" + System.currentTimeMillis() + ".png";

        mStorageRef.child(imageFileName).putFile(data)
                .addOnSuccessListener(taskSnapshot ->
                        mStorageRef.child(imageFileName).getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    mProgressDialog.dismiss();
                                    dp = uri.toString();
                                    Glide
                                            .with(this)
                                            .load(dp)
                                            .into(binding.image);
                                })
                                .addOnFailureListener(e -> {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(SettingActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                                    Log.d("imageDp: ", e.toString());
                                }))
                .addOnFailureListener(e -> {
                    mProgressDialog.dismiss();
                    Toast.makeText(SettingActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                    Log.d("imageDp: ", e.toString());
                });
    }

    //HAPUSKAN ACTIVITY KETIKA SUDAH TIDAK DIGUNAKAN, AGAR MENGURANGI RISIKO MEMORY LEAKS
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}