package edu.neu.firebase.wecart.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import edu.neu.firebase.wecart.Chat_message;
import edu.neu.firebase.wecart.MsgAdapter;
import edu.neu.firebase.wecart.R;

public class SellerChatFragment extends Fragment {

    private List<Chat_message> chat_message_list = new ArrayList<>();
    private RecyclerView msgRecyclerView;
    private EditText chat_seller_content;
    private MsgAdapter adapter;

    public SellerChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_chat, container, false);

        msgRecyclerView = view.findViewById(R.id.msg_recycler_view);
        chat_seller_content = view.findViewById(R.id.input_text);
        Button send_message = view.findViewById(R.id.send_message);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new MsgAdapter(chat_message_list = getData());

        msgRecyclerView.setLayoutManager(layoutManager);
        msgRecyclerView.setAdapter(adapter);

        send_message.setOnClickListener(v -> {
            String content = chat_seller_content.getText().toString();
            if(!content.equals("")) {
                chat_message_list.add(new Chat_message(content, Chat_message.TYPE_send_message));
                adapter.notifyItemInserted(chat_message_list.size()-1);
                msgRecyclerView.scrollToPosition(chat_message_list.size()-1);
                chat_seller_content.setText("");//Clear the input content
            }
            //sample chat
            if(chat_message_list.size() == 2){
                chat_message_list.add(new Chat_message("I donno how to cancel order", Chat_message.TYPE_RECEIVED));
                adapter.notifyItemInserted(chat_message_list.size()-1);
                msgRecyclerView.scrollToPosition(chat_message_list.size()-1);
            }
            if(chat_message_list.size() == 4){
                chat_message_list.add(new Chat_message("Can you help me to cancel it?", Chat_message.TYPE_RECEIVED));
                adapter.notifyItemInserted(chat_message_list.size()-1);
                msgRecyclerView.scrollToPosition(chat_message_list.size()-1);
            }
            if(chat_message_list.size() == 6){
                chat_message_list.add(new Chat_message("I am ok now, thank you!", Chat_message.TYPE_RECEIVED));
                adapter.notifyItemInserted(chat_message_list.size()-1);
                msgRecyclerView.scrollToPosition(chat_message_list.size()-1);
            }
        });

        return view;
    }


    private List<Chat_message> getData(){
        List<Chat_message> list = new ArrayList<>();
        list.add(new Chat_message("Hi, I need help.", Chat_message.TYPE_RECEIVED));
        return list;
    }
}