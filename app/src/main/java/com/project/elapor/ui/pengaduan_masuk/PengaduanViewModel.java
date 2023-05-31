package com.project.elapor.ui.pengaduan_masuk;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.project.elapor.ui.pengaduan_masuk.PengaduanModel;

import java.util.ArrayList;

public class PengaduanViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<PengaduanModel>> listPengaduan = new MutableLiveData<>();
    final ArrayList<PengaduanModel> pengaduanModelArrayList = new ArrayList<>();

    private static final String TAG = PengaduanViewModel.class.getSimpleName();

    public void setListPengaduanUserByRuangBedah() {
        pengaduanModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("report")
                    .orderBy("date", Query.Direction.DESCENDING)
                    .whereEqualTo("status", "not finish")
                    .whereEqualTo("adminUnit", "Ruang Bedah")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                PengaduanModel model = new PengaduanModel();

                                model.setUid("" + document.get("uid"));
                                model.setAdminImage("" + document.get("adminImage"));
                                model.setAdminUid("" + document.get("adminUid"));
                                model.setAdminName("" + document.get("adminName"));
                                model.setAdminUnit("" + document.get("adminUnit"));
                                model.setDate("" + document.get("date"));
                                model.setMessage("" + document.get("message"));
                                model.setUserImage("" + document.get("userImage"));
                                model.setUserName("" + document.get("userName"));
                                model.setUserUid("" + document.get("userUid"));
                                model.setUserUnit("" + document.get("userUnit"));
                                model.setStatus("" + document.get("status"));


                                pengaduanModelArrayList.add(model);
                            }
                            listPengaduan.postValue(pengaduanModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void setListPengaduanUserByInstalasiSIMRS() {
        pengaduanModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("report")
                    .orderBy("date", Query.Direction.DESCENDING)
                    .whereEqualTo("status", "not finish")
                    .whereEqualTo("adminUnit", "Instalasi SIMRS")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                PengaduanModel model = new PengaduanModel();

                                model.setUid("" + document.get("uid"));
                                model.setAdminImage("" + document.get("adminImage"));
                                model.setAdminUid("" + document.get("adminUid"));
                                model.setAdminName("" + document.get("adminName"));
                                model.setAdminUnit("" + document.get("adminUnit"));
                                model.setDate("" + document.get("date"));
                                model.setMessage("" + document.get("message"));
                                model.setUserImage("" + document.get("userImage"));
                                model.setUserName("" + document.get("userName"));
                                model.setUserUid("" + document.get("userUid"));
                                model.setUserUnit("" + document.get("userUnit"));
                                model.setStatus("" + document.get("status"));


                                pengaduanModelArrayList.add(model);
                            }
                            listPengaduan.postValue(pengaduanModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }


    public void setListPengaduanAdminByUid(String uid) {
        pengaduanModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("report")
                    .whereEqualTo("adminUid", uid)
                    .whereEqualTo("status", "not finish")
                    .orderBy("date", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                PengaduanModel model = new PengaduanModel();

                                model.setUid("" + document.get("uid"));
                                model.setAdminImage("" + document.get("adminImage"));
                                model.setAdminUid("" + document.get("adminUid"));
                                model.setAdminName("" + document.get("adminName"));
                                model.setAdminUnit("" + document.get("adminUnit"));
                                model.setDate("" + document.get("date"));
                                model.setMessage("" + document.get("message"));
                                model.setUserImage("" + document.get("userImage"));
                                model.setUserName("" + document.get("userName"));
                                model.setUserUid("" + document.get("userUid"));
                                model.setUserUnit("" + document.get("userUnit"));
                                model.setStatus("" + document.get("status"));


                                pengaduanModelArrayList.add(model);
                            }
                            listPengaduan.postValue(pengaduanModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void setListPengaduanAdminByUidComplete(String uid) {
        pengaduanModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("report")
                    .whereEqualTo("adminUid", uid)
                    .whereEqualTo("status", "finish")
                    .orderBy("date", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                PengaduanModel model = new PengaduanModel();

                                model.setUid("" + document.get("uid"));
                                model.setAdminImage("" + document.get("adminImage"));
                                model.setAdminUid("" + document.get("adminUid"));
                                model.setAdminName("" + document.get("adminName"));
                                model.setAdminUnit("" + document.get("adminUnit"));
                                model.setDate("" + document.get("date"));
                                model.setMessage("" + document.get("message"));
                                model.setUserImage("" + document.get("userImage"));
                                model.setUserName("" + document.get("userName"));
                                model.setUserUid("" + document.get("userUid"));
                                model.setUserUnit("" + document.get("userUnit"));
                                model.setStatus("" + document.get("status"));


                                pengaduanModelArrayList.add(model);
                            }
                            listPengaduan.postValue(pengaduanModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }


    public LiveData<ArrayList<PengaduanModel>> getPengaduan() {
        return listPengaduan;
    }

}
