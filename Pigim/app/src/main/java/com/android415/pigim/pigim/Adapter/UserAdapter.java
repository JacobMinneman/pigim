package com.android415.pigim.pigim.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android415.pigim.pigim.MainActivity;
import com.android415.pigim.pigim.R;
import com.android415.pigim.pigim.MessageActivity;
import com.android415.pigim.pigim.Model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> users;
    private LayoutInflater inflater;
    private Context context;

    public UserAdapter(MainActivity context, List<User> users){
        this.inflater = LayoutInflater.from(context);
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    // Updates each itemView to display user and that user's profile image
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        final User user = users.get(position);
        viewHolder.username.setText(user.getUsername());

        String profileImg = user.getImageURL();
        int profileImageID = context.getResources().getIdentifier(profileImg,"mipmap","com.android415.pigim.pigim");
        viewHolder.profile_image.setImageResource(profileImageID);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid",user.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;

        public ViewHolder(View itemView){
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }
}

