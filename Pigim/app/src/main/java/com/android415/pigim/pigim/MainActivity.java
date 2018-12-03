package com.android415.pigim.pigim;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.LinkedList;
import java.util.Scanner;

import static com.android415.pigim.pigim.Utils.MESSAGES_KEY;
import static com.android415.pigim.pigim.Utils.PROFILE_PIC;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = this.getClass().getSimpleName();

    private static FirebaseUser mUser;
    private String mConversation;

    private EditText mSendMsg;
    private Button mSendBtn;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;

    private RecyclerView mRecyclerView;
    private MessageListAdapter mAdapter;

    private LinkedList<String> mMessageList = new LinkedList<>();

    private String profilePic;

    private ImageView profilePicView;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.setTheme();

        // Getting the previous conversation from shared preferences
        mConversation = Utils.mPreferences.getString(MESSAGES_KEY, "");
        if (!mConversation.equals(""))
        {
            Scanner scanner = new Scanner(mConversation);
            String sender, message;

            while (scanner.hasNextLine())
            {
                sender = scanner.nextLine();

                // if sender is contains a letter (is not empty)
                if (sender.matches(".*[a-z].*"))
                {
                    message = scanner.nextLine();

                    mMessageList.addLast(sender + "\n" + message);
                }
            }
            scanner.close();
        }

        profilePic = Utils.mPreferences.getString(PROFILE_PIC, "default");
        setProfilePic();

        // setting up all of the view links
        mSendMsg = findViewById(R.id.sendMsgText);
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

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.msg_recycler);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new MessageListAdapter(this, mMessageList);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
            case R.id.nav_contacts: {
                break;
            }
            // For future use
//            case R.id.nav_chats: {
//                break;
//            }
//            case R.id.nav_export: {
//                break;
//            }
        }

        //mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // used for send button in keyboard and send button next to edit text view
    private void sendMessage() {
        if (!mSendMsg.getText().toString().equals("")) {
            String message = "Me:" + "\n" + mSendMsg.getText().toString();
            mConversation += message + "\n";
            mSendMsg.setText("");

            int messageListSize = mMessageList.size();
            mMessageList.addLast(message);
            mRecyclerView.getAdapter().notifyItemInserted(messageListSize);
            mRecyclerView.smoothScrollToPosition(messageListSize);

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

            // removing from recycler view
            mConversation = "";
            mMessageList.clear();
            mRecyclerView.getAdapter().notifyDataSetChanged();
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
    protected void onRestart() {
        super.onRestart();
        Utils.setTheme();
    }

    // if nav drawer is open then back button closes it, else regular function
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    public void setProfilePic()
    {
        navView = findViewById(R.id.nav_view);
        final View VIEW = navView.getHeaderView(0);
        profilePicView = VIEW.findViewById(R.id.menu_pigim_icon);

        switch (profilePic)
        {
            case "owl":
            {
                profilePicView.setImageResource(R.mipmap.owl_round);
                break;
            }

            case"eagle":
            {
                profilePicView.setImageResource(R.mipmap.eagle_round);
                break;
            }

            case "pigeon":
            {
                profilePicView.setImageResource(R.mipmap.pigeon_round);
                break;
            }

            case "bluejay":
            {
                profilePicView.setImageResource(R.mipmap.bluejay_round);
                break;
            }

            case "ostrich":
            {
                profilePicView.setImageResource(R.mipmap.ostrich_round);
                break;
            }

            case "peacock":
            {
                profilePicView.setImageResource(R.mipmap.peacock_round);
                break;
            }

            case "penguin":
            {
                profilePicView.setImageResource(R.mipmap.penguin_round);
                break;
            }

            case "phoenix":
            {
                profilePicView.setImageResource(R.mipmap.phoenix_round);
                break;
            }

            case "cardinal":
            {
                profilePicView.setImageResource(R.mipmap.cardinal_round);
                break;
            }

            default:
            {
                profilePicView.setImageResource(R.mipmap.ic_launcher_round);
                break;
            }

        }

    }
}
