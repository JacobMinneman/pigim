package com.android415.pigim.pigim;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private final String THEME_KEY = "theme";

    private SharedPreferences preferences;
    private String sharedPrefFile = "com.android415.pigim.pigim";
    private Switch themeSwitch;
    private Boolean isDarkThemeOn = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        themeSwitch = (Switch) findViewById(R.id.theme_switch);

        // Getting the theme from shared preferences
        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        isDarkThemeOn = preferences.getBoolean(THEME_KEY, true);
        if (isDarkThemeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        themeSwitch.setChecked(isDarkThemeOn);

        // listener/handler for theme switch
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (themeSwitch.isChecked()) {
                //switching to dark theme
                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                isDarkThemeOn = true;
                finish();
                startActivity(getIntent());
            } else {
                // switching to light theme
                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                isDarkThemeOn = false;
                finish();
                startActivity(getIntent());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putBoolean(THEME_KEY, isDarkThemeOn);
        preferencesEditor.apply();
    }
}
