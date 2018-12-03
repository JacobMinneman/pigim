package com.android415.pigim.pigim;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;

import com.google.firebase.FirebaseApp;

import static android.content.Context.MODE_PRIVATE;

class Utils {
    static final String THEME_KEY = "theme";
    static final String MESSAGES_KEY = "messages";
    static final String PROFILE_PIC = "profilePic";
    static String mSharedPrefFile = "com.android415.pigim.pigim";
    static SharedPreferences mPreferences;

    static void setTheme() {
        // Getting the theme from shared preferences
        mPreferences = FirebaseApp.getInstance().getApplicationContext()
                .getSharedPreferences(mSharedPrefFile, MODE_PRIVATE);
        boolean isDarkThemeOn = mPreferences.getBoolean(THEME_KEY,
                (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES));
        if (isDarkThemeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
