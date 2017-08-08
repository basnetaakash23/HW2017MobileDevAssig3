package com.example.aakashb.howardchat;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aakashb on 8/7/17.
 */

public class MessageSource {

    public interface MessageListener{
        void onMessageReceived(List<? extends Message> messageList) ;

    }

    private static MessageSource sMessageSource;

    private Context mContext;

    public static MessageSource get(Context context) {
        if (sMessageSource == null) {
            sMessageSource = new MessageSource(context);
        }
        return sMessageSource;
    }

    private MessageSource(Context context) {
        mContext = context;
    }

    public void getMessages(final MessageListener messageListener){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference messageRef = databaseRef.child("messages");
        Query last50messageQuery = messageRef.limitToLast(50);
        //DatabaseReference messageRef1 = databaseRef.child("fromUserId");
        //DatabaseReference messageRef2 = databaseRef.child("fromUserName");

        last50messageQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Message> allMessages = new ArrayList<>();
                Iterable<DataSnapshot> iter = dataSnapshot.getChildren();
                for (DataSnapshot messageSnapshot : iter) {
                    Message allMessage = new Message(messageSnapshot);
                    allMessages.add(allMessage);
                }
                messageListener.onMessageReceived(allMessages);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendMessage(Message message){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference messageReference = databaseReference.child("messages");
        DatabaseReference newMessageReference = messageReference.push();

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("content",message.getMessage());
        messageMap.put("fromUserId",message.getUserId());
        messageMap.put("fromUserName",message.getUserName());

        newMessageReference.setValue(messageMap);

    }
}
