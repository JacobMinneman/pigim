package com.android415.pigim.pigim;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.android415.pigim.pigim.Adapter.MessageAdapter;
import com.android415.pigim.pigim.Model.Message;
import com.android415.pigim.pigim.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private CircleImageView profile_image;
    private TextView username;
    private FirebaseUser fuser;
    private DatabaseReference dbRef;
    private ImageButton buttonSend;
    private EditText textToSend;
    private MessageAdapter messageAdapter;
    private List<Message> messages;
    private RecyclerView recyclerView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        buttonSend = findViewById(R.id.button_send);
        textToSend = findViewById(R.id.text_to_send);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager((getApplicationContext()));
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        intent = getIntent();
        final String userid = intent.getStringExtra("userid");

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = textToSend.getText().toString();
                if(!message.equals("")){
                    sendMessage(fuser.getUid(),userid,message);
                }else{
                    Toast.makeText(MessageActivity.this,"Type a message",Toast.LENGTH_SHORT).show();
                }
                textToSend.setText("");
            }
        });

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.default_user_icon);
                } else{
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(profile_image);
                }

                getMessages(fuser.getUid(), userid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, String receiver, String message){

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        dbRef.child("Chats").push().setValue(hashMap);

    }

    private void getMessages(final String currentId, final String userId, final String profileUrl){
        messages = new ArrayList<>();

        dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Message msg = snapshot.getValue(Message.class);
                    if(msg.getReceiver().equals(currentId) && msg.getSender().equals(userId) || msg.getReceiver().equals(userId) && msg.getSender().equals(currentId)){
                        messages.add(msg);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this,messages,profileUrl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

