package com.example.aakashb.howardchat;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by aakashb on 8/7/17.
 */

public class Message {
    private String mUserName;
    private String mUserId;

    private String mUserMessage;


    public Message(String userName, String userId, String message) {
        mUserName = userName;
        mUserId = userId;
        mUserMessage = message;
        // Note we do not generate a timestamp...that's only generated by the server.
    }

    public Message(DataSnapshot pingSnapshot) {
        mUserName = pingSnapshot.child("fromUserName").getValue(String.class);
        mUserId = pingSnapshot.child("fromUserId").getValue(String.class);
        mUserMessage = pingSnapshot.child("content").getValue(String.class);
    }

    public String getUserName() {
        return mUserName;
    }

    public String getMessage() {
        return mUserMessage;
    }

    public String getUserId() {
        return mUserId;
    }


}
