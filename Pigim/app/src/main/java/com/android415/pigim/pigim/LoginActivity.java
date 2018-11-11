package com.android415.pigim.pigim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.PrivateKey;
import java.util.PriorityQueue;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private final String THEME_KEY = "theme";

    private SharedPreferences preferences;
    private String sharedPrefFile = "com.android415.pigim.pigim";

    private Boolean isDarkThemeOn = true;

    private final String USERNAME = "me@pigim.com";
    private final String PASSWORD = "pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Getting the theme from shared preferences
        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        isDarkThemeOn = preferences.getBoolean(THEME_KEY, true);
        if (isDarkThemeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        final Button loginButton = (Button) findViewById(R.id.button_login);
        final EditText username = (EditText) findViewById(R.id.text_username);
        final EditText password = (EditText) findViewById(R.id.text_password);
        final CheckBox showPasswordCheck = (CheckBox) findViewById(R.id.checkBox_show_password);

        loginButton.setOnClickListener(v -> {
            // pattern for email format (https://www.journaldev.com/638/java-email-validation-regex)
            Pattern emailFormat = Pattern.compile("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$");

            // first checking if email format
            Boolean isEmailFormat = username.getText().toString().matches(emailFormat.toString());

            if (username.getText().toString().toLowerCase().equals(USERNAME) &&
                    password.getText().toString().equals(PASSWORD) &&
                    isEmailFormat) {
                password.setText("");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                password.setText("");
                if (!isEmailFormat) {
                    Toast.makeText(LoginActivity.this,
                            "Username is an invalid email address.\nPlease try again.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Username/password is incorrect.\nPlease try again.", Toast.LENGTH_LONG).show();
                }
            }
        });

        showPasswordCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // showing password if show password checkbox is checked and setting cursor to the end
            if (isChecked) {
                password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                password.setSelection(password.getText().length());
            } else {
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                password.setSelection(password.getText().length());
            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        // Getting the theme from shared preferences
        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        isDarkThemeOn = preferences.getBoolean(THEME_KEY, false);
        if (isDarkThemeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        recreate();
    }
}
