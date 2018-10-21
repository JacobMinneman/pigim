package com.android415.pigim.pigim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.PrivateKey;
import java.util.PriorityQueue;

public class LoginActivity extends AppCompatActivity
{
    private final String THEME_KEY = "theme";

    private SharedPreferences preferences;
    private String sharedPrefFile = "com.android415.pigim.pigim";

    private Boolean isDarkThemeOn = true;

    private final String USERNAME = "pigim";
    private final String PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        final Button loginButton = (Button) findViewById(R.id.button_login);
        final TextView username = (TextView) findViewById(R.id.text_username);
        final TextView password = (TextView) findViewById(R.id.text_password);

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (username.getText().toString().toLowerCase().equals(USERNAME) &&
                        password.getText().toString().equals(PASSWORD))
                {
                    password.setText("");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    password.setText("");
                    Toast.makeText(LoginActivity.this,
                            "Username/password is incorrect.\nPlease try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
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
