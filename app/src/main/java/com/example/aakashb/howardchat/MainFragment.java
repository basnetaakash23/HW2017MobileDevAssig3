package com.example.aakashb.howardchat;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Created by aakashb on 8/7/17.
 */



public class MainFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){


        View v = inflater.inflate(R.layout.fragment_main, container, false);
        //Get references
        final ListView messageListView = v.findViewById(R.id.all_messages_list_view);


            MessageSource.get(getContext()).getMessages(new MessageSource.MessageListener() {
                @Override
                public void onMessageReceived(List<? extends Message> messageList) {
                    MessageArrayAdapter adapter = new MessageArrayAdapter(getContext(), messageList);
                    messageListView.setAdapter(adapter);
                    messageListView.setSelection(adapter.getCount() - 1);

                }
            });

        final EditText mEditText = (EditText) v.findViewById(R.id.message_input);



        Button button = v.findViewById(R.id.send_message_button);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null) {
                    //Toast.makeText(getContext(), "can't send message, not logged in", Toast.LENGTH_SHORT);
                }
                String text_message = mEditText.getText().toString();
                Log.i("AB" , "Edit Text is: " + text_message);
                Message message = new Message(user.getDisplayName(),user.getUid(), text_message);
                MessageSource.get(getContext()).sendMessage(message);
                mEditText.setText("");


            }
        });
        return v;
    }



    private class MessageArrayAdapter extends BaseAdapter {
        protected Context mContext;
        protected List<Message> mMessageList;
        protected LayoutInflater mLayoutInflater;
        public MessageArrayAdapter(Context context, List<? extends Message> messagesList) {
            mContext = context;
            mMessageList = (List<Message>) messagesList;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mMessageList.size();
        }

        @Override
        public Object getItem(int position) {
            return mMessageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Message message = mMessageList.get(position);
            View rowView = mLayoutInflater.inflate(R.layout.list_item_message, parent, false);

            TextView title = rowView.findViewById(R.id.user_text_view);
            title.setText(message.getUserName());

            TextView subtitle = rowView.findViewById(R.id.message_text_view);
            //get a Long timestamp (of millis since epoch) into a human-readable string.
            subtitle.setText(message.getMessage());


            return rowView;
        }
    }
}


