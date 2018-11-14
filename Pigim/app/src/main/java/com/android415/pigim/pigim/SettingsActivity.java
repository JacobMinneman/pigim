package com.android415.pigim.pigim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Switch;

import static com.android415.pigim.pigim.Utils.THEME_KEY;

public class SettingsActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    private Switch mThemeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Utils.setTheme();

        mThemeSwitch = findViewById(R.id.theme_switch);
        mThemeSwitch.setChecked(
                Utils.mPreferences.getBoolean(THEME_KEY,
                        (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES))
        );

        // listener/handler for theme switch
        mThemeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mThemeSwitch.isChecked()) {
                //switching to dark theme
                Utils.mPreferences.edit().putBoolean(THEME_KEY, true).apply();
                finish();
                startActivity(getIntent());
            } else {
                // switching to light theme
                Utils.mPreferences.edit().putBoolean(THEME_KEY, false).apply();
                finish();
                startActivity(getIntent());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.mPreferences.edit().putBoolean(THEME_KEY, mThemeSwitch.isChecked()).apply();
    }
}
