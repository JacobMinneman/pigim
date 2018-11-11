package com.android415.pigim.pigim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final String THEME_KEY = "theme";
    private final String MESSAGES_KEY = "messages";

    private SharedPreferences mPreferences;
    private String mSharedPrefFile = "com.android415.pigim.pigim";

    private Boolean mIsDarkThemeOn = true;

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

        // Getting the theme from shared preferences
        mPreferences = getSharedPreferences(mSharedPrefFile, MODE_PRIVATE);
        mIsDarkThemeOn = mPreferences.getBoolean(THEME_KEY, true);
        if (mIsDarkThemeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Getting the previous conversation from shared preferences
        // (later moving to json file for storage)
        mConversation = mPreferences.getString(MESSAGES_KEY, "");

        // setting up all of the view links
        mSendMsg = (EditText) findViewById(R.id.sendMsgText);
        mMessages = (TextView) findViewById(R.id.messageText);
        mReceiveScroll = (ScrollView) findViewById(R.id.messageScroll);
        mSendBtn = (Button) findViewById(R.id.button_send);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // toggle for nav drawer open/close
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // used to connect to nav drawer listener
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
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
        mConversation = mMessages.getText().toString();
        mConversation += "\n" + "----Me:----" + "\n" + mSendMsg.getText().toString();
        mSendMsg.setText("");
        mMessages.setText(mConversation);
        mReceiveScroll.fullScroll(View.FOCUS_DOWN);
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
            SharedPreferences.Editor preferencesEditor = mPreferences.edit();
            preferencesEditor.remove(MESSAGES_KEY);
            preferencesEditor.apply();

            // removing from text view
            final TextView messages = (TextView) findViewById(R.id.messageText);
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

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(MESSAGES_KEY, mConversation);
        preferencesEditor.apply();
    }

    // makes sure shared preferences theme is displayed
    @Override
    protected void onRestart() {
        super.onRestart();

        // Getting the theme from shared preferences
        mPreferences = getSharedPreferences(mSharedPrefFile, MODE_PRIVATE);
        mIsDarkThemeOn = mPreferences.getBoolean(THEME_KEY, false);
        if (mIsDarkThemeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        recreate();
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
