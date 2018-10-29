package com.android415.pigim.pigim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    private final String THEME_KEY = "theme";
    private final String MESSAGES_KEY = "messages";

    private SharedPreferences preferences;
    private String sharedPrefFile = "com.android415.pigim.pigim";

    private Boolean isDarkThemeOn = true;

    private String conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting the theme from shared preferences
        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        isDarkThemeOn = preferences.getBoolean(THEME_KEY, true);
        if (isDarkThemeOn)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Getting the previous conversation from shared preferences
        // (later moving to json file for storage)
        conversation = preferences.getString(MESSAGES_KEY, "");

        // setting up all of the view links
        final EditText sendMsg = (EditText) findViewById(R.id.sendMsgText);
        final TextView messages = (TextView) findViewById(R.id.messageText);
        final ScrollView receiveScroll = (ScrollView) findViewById(R.id.messageScroll);

        messages.setText(conversation);

        // listener/handler for sending a message
        sendMsg.setOnEditorActionListener(new EditText.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {

                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    conversation = messages.getText().toString();
                    conversation += "\n" + "----Me:----" + "\n" + sendMsg.getText().toString();
                    sendMsg.setText("");
                    messages.setText(conversation);
                    receiveScroll.fullScroll(View.FOCUS_DOWN);
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Check if the correct item was clicked
        if(item.getItemId()==R.id.settings)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId()==R.id.delete_history)
        {
            // removing from preferences
            SharedPreferences.Editor preferencesEditor = preferences.edit();
            preferencesEditor.remove(MESSAGES_KEY);
            preferencesEditor.apply();

            // removing from text view
            final TextView messages = (TextView) findViewById(R.id.messageText);
            messages.setText("");
            conversation = "";
        }
        return true;
    }

    @Override
    protected void onPause(){
        super.onPause();

        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString(MESSAGES_KEY, conversation);
        preferencesEditor.apply();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();

        // Getting the theme from shared preferences
        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        isDarkThemeOn = preferences.getBoolean(THEME_KEY, false);
        if (isDarkThemeOn)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        recreate();
    }

}
