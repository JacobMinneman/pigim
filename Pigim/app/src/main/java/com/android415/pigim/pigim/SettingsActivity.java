package com.android415.pigim.pigim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.widget.ImageButton;
import android.widget.Switch;

import static com.android415.pigim.pigim.Utils.PROFILE_PIC;
import static com.android415.pigim.pigim.Utils.THEME_KEY;

public class SettingsActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private final Float SELECTED_ALPHA = 0.2f;
    private final Float UNSELECTED_ALPHA = 1.0f;

    private Switch mThemeSwitch;
    private ImageButton bluejay, cardinal, eagle, ostrich, owl, peacock, phoenix, penguin, pigeon;

    enum profilePic {bluejay, cardinal, eagle, ostrich, owl, peacock, phoenix, penguin, pigeon}
    private String picChosen = "default";

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

        picChosen = Utils.mPreferences.getString(PROFILE_PIC, "default");

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

        bluejay = findViewById(R.id.image_bluejay);
        cardinal = findViewById(R.id.image_cardinal);
        eagle = findViewById(R.id.image_eagle);
        ostrich = findViewById(R.id.image_ostrich);
        owl = findViewById(R.id.image_owl);
        peacock = findViewById(R.id.image_peacock);
        penguin = findViewById(R.id.image_penguin);
        phoenix = findViewById(R.id.image_phoenix);
        pigeon = findViewById(R.id.image_pigeon);

        setAllEnabled();

        switch (picChosen)
        {
            case "owl":
            {
                owl.setEnabled(false);
                owl.setAlpha(SELECTED_ALPHA);
                break;
            }

            case"eagle":
            {
                eagle.setEnabled(false);
                eagle.setAlpha(SELECTED_ALPHA);
                break;
            }

            case "pigeon":
            {
                pigeon.setEnabled(false);
                pigeon.setAlpha(SELECTED_ALPHA);
                break;
            }

            case "bluejay":
            {
                bluejay.setEnabled(false);
                bluejay.setAlpha(SELECTED_ALPHA);
                break;
            }

            case "ostrich":
            {
                ostrich.setEnabled(false);
                ostrich.setAlpha(SELECTED_ALPHA);
                break;
            }

            case "peacock":
            {
                peacock.setEnabled(false);
                peacock.setAlpha(SELECTED_ALPHA);
                break;
            }

            case "penguin":
            {
                penguin.setEnabled(false);
                penguin.setAlpha(SELECTED_ALPHA);
                break;
            }

            case "phoenix":
            {
                phoenix.setEnabled(false);
                phoenix.setAlpha(SELECTED_ALPHA);
                break;
            }

            case "cardinal":
            {
                cardinal.setEnabled(false);
                cardinal.setAlpha(SELECTED_ALPHA);
                break;
            }

            default:
                break;

        }

        bluejay.setOnClickListener(v -> setProfilePic(profilePic.bluejay));
        cardinal.setOnClickListener(v -> setProfilePic(profilePic.cardinal));
        eagle.setOnClickListener(v -> setProfilePic(profilePic.eagle));
        ostrich.setOnClickListener(v -> setProfilePic(profilePic.ostrich));
        owl.setOnClickListener(v -> setProfilePic(profilePic.owl));
        peacock.setOnClickListener(v -> setProfilePic(profilePic.peacock));
        penguin.setOnClickListener(v -> setProfilePic(profilePic.penguin));
        phoenix.setOnClickListener(v -> setProfilePic(profilePic.phoenix));
        pigeon.setOnClickListener(v -> setProfilePic(profilePic.pigeon));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.mPreferences.edit().putBoolean(THEME_KEY, mThemeSwitch.isChecked()).apply();
        getSharedPreferences(Utils.mSharedPrefFile, MODE_PRIVATE).edit()
                .putString(PROFILE_PIC, picChosen).apply();
    }

    private void setProfilePic(profilePic selectedPic)
    {
        setAllEnabled();

        switch(selectedPic)
        {
            case owl:
            {
                picChosen = "owl";
                owl.setEnabled(false);
                owl.setAlpha(SELECTED_ALPHA);
                break;
            }
            case eagle:
            {
                picChosen = "eagle";
                eagle.setEnabled(false);
                eagle.setAlpha(SELECTED_ALPHA);
                break;
            }
            case pigeon:
            {
                picChosen = "pigeon";
                pigeon.setEnabled(false);
                pigeon.setAlpha(SELECTED_ALPHA);
                break;
            }
            case bluejay:
            {
                picChosen = "bluejay";
                bluejay.setEnabled(false);
                bluejay.setAlpha(SELECTED_ALPHA);
                break;
            }
            case ostrich:
            {
                picChosen = "ostrich";
                ostrich.setEnabled(false);
                ostrich.setAlpha(SELECTED_ALPHA);
                break;
            }
            case peacock:
            {
                picChosen = "peacock";
                peacock.setEnabled(false);
                peacock.setAlpha(SELECTED_ALPHA);
                break;
            }
            case penguin:
            {
                picChosen = "penguin";
                penguin.setEnabled(false);
                penguin.setAlpha(SELECTED_ALPHA);
                break;
            }
            case phoenix:
            {
                picChosen = "phoenix";
                phoenix.setEnabled(false);
                phoenix.setAlpha(SELECTED_ALPHA);
                break;
            }
            case cardinal:
            {
                picChosen = "cardinal";
                cardinal.setEnabled(false);
                cardinal.setAlpha(SELECTED_ALPHA);
                break;
            }
            default:
                picChosen = "default";
        }
    }

    private void setAllEnabled()
    {
        bluejay.setEnabled(true);
        cardinal.setEnabled(true);
        eagle.setEnabled(true);
        ostrich.setEnabled(true);
        owl.setEnabled(true);
        peacock.setEnabled(true);
        phoenix.setEnabled(true);
        penguin.setEnabled(true);
        pigeon.setEnabled(true);

        bluejay.setAlpha(UNSELECTED_ALPHA);
        cardinal.setAlpha(UNSELECTED_ALPHA);
        eagle.setAlpha(UNSELECTED_ALPHA);
        ostrich.setAlpha(UNSELECTED_ALPHA);
        owl.setAlpha(UNSELECTED_ALPHA);
        peacock.setAlpha(UNSELECTED_ALPHA);
        phoenix.setAlpha(UNSELECTED_ALPHA);
        penguin.setAlpha(UNSELECTED_ALPHA);
        pigeon.setAlpha(UNSELECTED_ALPHA);
    }
}
