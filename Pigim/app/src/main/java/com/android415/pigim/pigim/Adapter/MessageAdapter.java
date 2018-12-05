package com.android415.pigim.pigim.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.android415.pigim.pigim.Model.Message;
import com.android415.pigim.pigim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final int MSG_LEFT = 0;
    private static final int MSG_RIGHT = 1;
    private Context context;
    private List<Message> conversation;
    private String imageurl;
    private FirebaseUser user;

    public MessageAdapter(Context context, List<Message> conversation, String imageurl){
        this.conversation = conversation;
        this.context = context;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    // Updates each itemView to display user and that user's profile image
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int position) {

        Message msg = conversation.get(position);
        viewHolder.showMessage.setText(msg.getMessage());

        int profileImageID = context.getResources().getIdentifier(imageurl,"mipmap","com.android415.pigim.pigim");
        viewHolder.profile_image.setImageResource(profileImageID);
    }

    @Override
    public int getItemCount() {
        return conversation.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView showMessage;
        public ImageView profile_image;

        public ViewHolder(View itemView){
            super(itemView);
            showMessage = itemView.findViewById(R.id.message_box);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(conversation.get(position).getSender().equals(user.getUid()))
            return MSG_RIGHT;
        else
            return MSG_LEFT;

    }
}

