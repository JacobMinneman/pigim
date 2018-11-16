package com.android415.pigim.pigim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private static final int RC_SIGN_IN = 1;

    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        Utils.setTheme();

        if (!isUserLoggedIn()) {
            createSignInIntent();
        }
    }

    private void createSignInIntent() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .setLogo(R.mipmap.ic_launcher_round)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "Result code: " + requestCode);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                Log.i(TAG, "Logged in as " + mUser.getEmail());
                finish();
                startActivity(new Intent(this, MainActivity.class));
            } else {
                if (response != null) {
                    Log.e(TAG, response.getError().getStackTrace().toString());
                } else {
                    finish();
                }
            }
        }
    }

    private boolean isUserLoggedIn() {
        return (mUser != null);
    }
}
