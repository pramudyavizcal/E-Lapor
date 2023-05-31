package com.project.elapor.ui.pengaduan_terkirim.message;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MessageDatabaseTerkirim {

    public static void sendChat(String message, String format, String uid, String myUid, boolean isText,String userName, long timeInMillis) {


        Map<String, Object> logChat   = new HashMap<>();
        logChat.put("message", message);
        logChat.put("date", format);
        logChat.put("uid", myUid);
        logChat.put("isText", isText);
        logChat.put("userName", userName);
        logChat.put("timeInMillis", timeInMillis);

        // UPDATE LOG CHAT
        FirebaseFirestore
                .getInstance()
                .collection("report")
                .document(uid)
                .collection("message")
                .document(String.valueOf(timeInMillis))
                .set(logChat)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d("SENDER MSG", "success");
                    }else {
                        Log.d("SENDER MSG", task.toString());
                    }
                });


        Map<String, Object> updateMessage   = new HashMap<>();
        updateMessage.put("message", message);
        //updateMessage.put("userName", userName );
        updateMessage.put("date", format);
        // UPDATE LAST MESSAGE
        FirebaseFirestore
                .getInstance()
                .collection("report")
                .document(uid)
                .update(updateMessage)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d("MSG", "success update last message");
                    } else {
                        Log.d("MSG", task.toString());
                    }
                });
    }

}
