package edu.neu.firebase.wecart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
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
    private MsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        msgRecyclerView = findViewById(R.id.msg_recycler_view);
        chat_user_content = findViewById(R.id.input_text);
        Button send_message = findViewById(R.id.send_message);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new MsgAdapter(chat_message_list = getData());

        msgRecyclerView.setLayoutManager(layoutManager);
        msgRecyclerView.setAdapter(adapter);
        createNotificationChannel();

        send_message.setOnClickListener(v -> {
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

                String channelId = "100";
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder builder = new Notification.Builder(ChatActivity.this, channelId);

                builder.setSmallIcon(R.drawable.logo);
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
                builder.setContentTitle("The Chat is Over!");                    //set title
                builder.setContentText("Click to jump to the other page.");                 //message content
                builder.setWhen(System.currentTimeMillis());
                builder.setAutoCancel(true);

                //jump to activity
                Intent intent = new Intent(ChatActivity.this, Jump.class);
                PendingIntent pi = PendingIntent.getActivities(ChatActivity.this, 0, new Intent[]{intent}, PendingIntent.FLAG_CANCEL_CURRENT);
                builder.setContentIntent(pi);
                //show content
                Notification notification = builder.build();
                manager.notify(1, notification);
            }
        });
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification name";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("100", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private List<Chat_message> getData(){
        List<Chat_message> list = new ArrayList<>();
        list.add(new Chat_message("Hi, what can I do for you?", Chat_message.TYPE_RECEIVED));
        return list;
    }
}
