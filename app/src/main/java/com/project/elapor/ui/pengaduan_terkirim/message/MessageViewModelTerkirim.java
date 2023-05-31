package com.project.elapor.ui.pengaduan_terkirim.message;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.project.elapor.ui.pengaduan_masuk.message.MessageModel;

import java.util.ArrayList;

public class MessageViewModelTerkirim extends ViewModel {

    private final MutableLiveData<ArrayList<MessageModelTerkirim>> listMessage = new MutableLiveData<>();
    final ArrayList<MessageModelTerkirim> messageModelArrayList = new ArrayList<>();

    private static final String TAG = MessageViewModelTerkirim.class.getSimpleName();

    public void setListMessage(String uid) {
        messageModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("report")
                    .document(uid)
                    .collection("message")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                MessageModelTerkirim model = new MessageModelTerkirim();

                                model.setDate("" + document.get("date"));
                                model.setMessage("" + document.get("message"));
                                model.setText(document.getBoolean("isText"));
                                model.setUid("" + document.get("uid"));
                                model.setTimeInMillis(document.getLong("timeInMillis"));
                                model.setNama("" + document.get("userName"));

                                messageModelArrayList.add(model);
                            }
                            listMessage.postValue(messageModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public LiveData<ArrayList<MessageModelTerkirim>> getMessage() {
        return listMessage;
    }
    public LiveData<ArrayList<MessageModelTerkirim>> getNama() {
        return listMessage;
    }

}
