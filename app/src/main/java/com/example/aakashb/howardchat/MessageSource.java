package com.example.aakashb.howardchat;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aakashb on 8/7/17.
 */

public class MessageSource {

    public static MessageSource get(Context context) {
        return null;
    }

    public interface MessageListener{
        void onMessageReceived(List<? extends Message> messageList);
    }

    public void getMessages(MessageListener messageListener){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference messageRef = databaseRef.child("content");
        DatabaseReference messageRef1 = databaseRef.child("fromUserId");
        DatabaseReference messageRef2 = databaseRef.child("fromUserName");

        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Message> allMessages = new ArrayList<>();
                Iterable<DataSnapshot> iter = dataSnapshot.getChildren();
                for (DataSnapshot messageSnapshot : iter) {
                    Message allMessage = new Message(messageSnapshot);
                    allMessages.add(allMessage);
                }
                MessageListener.onMessageReceived(allMessages);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void sendMessage(Message message){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference messageReference = databaseReference.child("content");
        DatabaseReference newMessageReference = messageReference.push();

        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("content",message.getMessage());
        messageMap.put("fromUserId",message.getUserId());
        messageMap.put("fromUserName",message.getUserName());

        newMessageReference.setValue(messageMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError== null){
                    //Toast.makeText(Context context,)
                }else{

                }
            }
        });
    }
}
