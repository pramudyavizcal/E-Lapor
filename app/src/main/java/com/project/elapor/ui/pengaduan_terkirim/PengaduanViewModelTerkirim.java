package com.project.elapor.ui.pengaduan_terkirim;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class PengaduanViewModelTerkirim extends ViewModel {

    private final MutableLiveData<ArrayList<PengaduanModelTerkirim>> listPengaduan = new MutableLiveData<>();
    final ArrayList<PengaduanModelTerkirim> pengaduanModelArrayList = new ArrayList<>();

    private static final String TAG = PengaduanViewModelTerkirim.class.getSimpleName();

    public void setListPengaduanUserByUid(String uid) {
        pengaduanModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("report")
                    .orderBy("date", Query.Direction.DESCENDING)
                    .orderBy("status", Query.Direction.DESCENDING)
                    .whereEqualTo("userUid", uid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                PengaduanModelTerkirim model = new PengaduanModelTerkirim();

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
                                PengaduanModelTerkirim model = new PengaduanModelTerkirim();

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
                                PengaduanModelTerkirim model = new PengaduanModelTerkirim();

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

    public void setListPengaduanAdminByUidCompleteAndQuery(String uid, String query) {
        pengaduanModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("report")
                    .whereEqualTo("adminUid", uid)
                    .whereEqualTo("status", "finish")
                    .whereGreaterThanOrEqualTo("userUnitTemp", query)
                    .whereLessThanOrEqualTo("userUnitTemp", query + '\uf8ff')
                    //.orderBy("date", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                PengaduanModelTerkirim model = new PengaduanModelTerkirim();

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


    public LiveData<ArrayList<PengaduanModelTerkirim>> getPengaduan() {
        return listPengaduan;
    }


}
