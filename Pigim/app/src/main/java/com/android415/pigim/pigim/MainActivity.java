package com.android415.pigim.pigim;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.android415.pigim.pigim.Utils.MESSAGES_KEY;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = this.getClass().getSimpleName();

    private static FirebaseUser mUser;
    private String mConversation;

    private EditText mSendMsg;
    private TextView mMessages;
    private ScrollView mReceiveScroll;
    private Button mSendBtn;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.setTheme();

        // Getting the previous conversation from shared preferences
        // TODO move to JSON file for storage
        mConversation = getSharedPreferences(Utils.mSharedPrefFile, MODE_PRIVATE)
                .getString(MESSAGES_KEY, "");

        // setting up all of the view links
        mSendMsg = findViewById(R.id.sendMsgText);
        mMessages = findViewById(R.id.messageText);
        mReceiveScroll = findViewById(R.id.messageScroll);
        mSendBtn = findViewById(R.id.button_send);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        // toggle for nav drawer open/close
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // used to connect to nav drawer listener
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mMessages.setText(mConversation);

        // listener/handler for sending a message
        mSendMsg.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendMessage();
                return true;
            }
            return false;
        });

        // listener for when send button is clicked instead of keyboard send button
        mSendBtn.setOnClickListener(v -> sendMessage());

        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    // onclick listener for drawer items
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_settings: {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.nav_chats: {
                break;
            }
            case R.id.nav_contacts: {
                break;
            }
            case R.id.nav_export: {
                break;
            }
        }

        //mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // used for send button in keyboard and send button next to edit text view
    private void sendMessage() {
        if (!mSendMsg.getText().toString().equals("")) {
            mConversation = mMessages.getText().toString();
            mConversation += "\n" + "" + "Me:" + "\n" + mSendMsg.getText().toString() + "\n";
            mSendMsg.setText("");
            mMessages.setText(mConversation);
            mReceiveScroll.fullScroll(View.FOCUS_DOWN);
        }
    }

    // inflates the overflow menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // used to choose selection of menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // nav drawer chosen
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // overflow menu chosen
        else if (item.getItemId() == R.id.delete_history) {

            // removing from preferences
            getSharedPreferences(Utils.mSharedPrefFile, MODE_PRIVATE).edit()
                    .remove(MESSAGES_KEY).apply();

            // removing from text view
            final TextView messages = findViewById(R.id.messageText);
            messages.setText("");
            mConversation = "";
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // updates the shared preferences
    @Override
    protected void onPause() {
        super.onPause();

        getSharedPreferences(Utils.mSharedPrefFile, MODE_PRIVATE).edit()
                .putString(MESSAGES_KEY, mConversation).apply();
    }

    // if nav drawer is open then back button closes it, else regular function
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
