package edu.neu.firebase.wecart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private List<Chat_message> chat_message_list = new ArrayList<>();
    private RecyclerView msgRecyclerView;
    private EditText chat_user_content;
    private Button send_message;
    private LinearLayoutManager layoutManager;
    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        msgRecyclerView = findViewById(R.id.msg_recycler_view);
        chat_user_content = findViewById(R.id.input_text);
        send_message = findViewById(R.id.send_message);
        layoutManager = new LinearLayoutManager(this);
        adapter = new MsgAdapter(chat_message_list = getData());

        msgRecyclerView.setLayoutManager(layoutManager);
        msgRecyclerView.setAdapter(adapter);

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = chat_user_content.getText().toString();
                //set response when input is empty
                if(!content.equals("")) {
                    chat_message_list.add(new Chat_message(content, Chat_message.TYPE_send_message));
                    adapter.notifyItemInserted(chat_message_list.size()-1);
                    msgRecyclerView.scrollToPosition(chat_message_list.size()-1);
                    chat_user_content.setText("");//Clear the input content
                }
//sample chat message list
                if(chat_message_list.size() == 2){
                    chat_message_list.add(new Chat_message("What's your name?", Chat_message.TYPE_RECEIVED));
                    adapter.notifyItemInserted(chat_message_list.size()-1);
                    msgRecyclerView.scrollToPosition(chat_message_list.size()-1);
                }
                if(chat_message_list.size() == 4){
                    chat_message_list.add(new Chat_message("Any other questions?", Chat_message.TYPE_RECEIVED));
                    adapter.notifyItemInserted(chat_message_list.size()-1);
                    msgRecyclerView.scrollToPosition(chat_message_list.size()-1);
                }
                if(chat_message_list.size() == 6){
                    chat_message_list.add(new Chat_message("Question recorded, bye!", Chat_message.TYPE_RECEIVED));
                    adapter.notifyItemInserted(chat_message_list.size()-1);
                    msgRecyclerView.scrollToPosition(chat_message_list.size()-1);
                }
            }
        });
    }

    private List<Chat_message> getData(){
        List<Chat_message> list = new ArrayList<>();
        list.add(new Chat_message("Hi, what can I do for you?", Chat_message.TYPE_RECEIVED));
        return list;
    }
}