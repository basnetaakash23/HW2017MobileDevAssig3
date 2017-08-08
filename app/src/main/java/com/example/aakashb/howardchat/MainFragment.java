package com.example.aakashb.howardchat;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MessageSource.get(getContext()).getMessages(new MessageSource.MessageListener() {
                @Override
                public void onMessageReceived(List<? extends Message> messageList) {
                    @SuppressLint("NewApi") MessageArrayAdapter adapter = new MessageArrayAdapter(getContext(), messageList);
                    messageListView.setAdapter(adapter);
                    messageListView.setSelection(adapter.getCount()-1);

                }
            });
        }

        EditText mEditText = (EditText) v.findViewById(R.id.message_input);
        final String text_message = mEditText.getText().toString();


        Button button = v.findViewById(R.id.send_message_button);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null) {
                    //Toast.makeText(getContext(), "can't send message, not logged in", Toast.LENGTH_SHORT);
                }

                Message message = new Message(user.getDisplayName(),user.getUid(), text_message);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    MessageSource.get(getContext()).sendMessage(message);
                }

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

            //TextView subtitle = rowView.findViewById(R.id.timestamp_text_view);
            // get a Long timestamp (of millis since epoch) into a human-readable string.
            //Date date = new Date(message.getTimestamp());


            return rowView;
        }
    }
}


