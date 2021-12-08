package edu.neu.firebase.wecart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>{

    private List<Chat_message> list;
    public MsgAdapter(List<Chat_message> list){
        this.list = list;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        TextView left_msg;

        LinearLayout rightLayout;
        TextView right_msg;

        public ViewHolder(View view){
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            left_msg = view.findViewById(R.id.left_msg);

            rightLayout = view.findViewById(R.id.right_layout);
            right_msg = view.findViewById(R.id.right_msg);
        }
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat_message chatmessage = list.get(position);
        if(chatmessage.getType() == Chat_message.TYPE_RECEIVED){
            //Show the receive message
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.left_msg.setText(chatmessage.getContent());

            holder.rightLayout.setVisibility(View.GONE);
        }else if(chatmessage.getType() == Chat_message.TYPE_send_message){
            //Show the send_message message
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.right_msg.setText(chatmessage.getContent());

            holder.leftLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}