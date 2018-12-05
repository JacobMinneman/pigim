package com.android415.pigim.pigim;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private MaterialEditText username, email, password;
    private Button button_register;
    private FirebaseAuth authentication;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
       // getSupportActionBar().setTitle("Register");
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        button_register = findViewById(R.id.button_register);

        authentication = FirebaseAuth.getInstance();

        // When the register button is clicked, a string is pulled from each text field and then
        // provided that all three fields are full and the password is at least 6 characters,
        // we register a new user
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameText = username.getText().toString();
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                if(TextUtils.isEmpty(usernameText)||TextUtils.isEmpty(emailText)||TextUtils.isEmpty(passwordText)) {
                    Toast.makeText(RegisterActivity.this, "All fields must be completed", Toast.LENGTH_SHORT).show();
                }else if (passwordText.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Password must be AT LEAST 6 characters long", Toast.LENGTH_SHORT).show();
                }else{
                    registerNewUser(usernameText,emailText,passwordText);
                }
            }
        });
    }

    private void registerNewUser(final String username, String email, String password){

        authentication.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = authentication.getCurrentUser();
                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();

                    // Gets a reference to the current userID in the database
                    dbRef = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                    // Creates a "profile" to add to the database of the current user
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("id",userid);
                    hashMap.put("username",username);
                    hashMap.put("imageURL","default");

                    // Attempts to add that profile to the database
                    dbRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                } else{
                    Toast.makeText(RegisterActivity.this,"Error with registration. Try again!",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}
