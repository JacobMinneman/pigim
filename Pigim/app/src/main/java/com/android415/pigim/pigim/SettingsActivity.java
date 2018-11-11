package com.android415.pigim.pigim;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private final String THEME_KEY = "theme";

    private SharedPreferences mPreferences;
    private String mSharedPrefFile = "com.android415.pigim.pigim";
    private Switch mThemeSwitch;
    private Boolean mIsDarkThemeOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mThemeSwitch = findViewById(R.id.theme_switch);

        // Getting the theme from shared preferences
        mPreferences = getSharedPreferences(mSharedPrefFile, MODE_PRIVATE);
        mIsDarkThemeOn = mPreferences.getBoolean(THEME_KEY, true);
        if (mIsDarkThemeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        mThemeSwitch.setChecked(mIsDarkThemeOn);

        // listener/handler for theme switch
        mThemeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mThemeSwitch.isChecked()) {
                //switching to dark theme
                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                mIsDarkThemeOn = true;
                finish();
                startActivity(getIntent());
            } else {
                // switching to light theme
                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                mIsDarkThemeOn = false;
                finish();
                startActivity(getIntent());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putBoolean(THEME_KEY, mIsDarkThemeOn);
        preferencesEditor.apply();
    }
}
