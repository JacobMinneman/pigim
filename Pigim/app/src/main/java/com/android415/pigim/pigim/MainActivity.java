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
import android.widget.ImageView;

import com.android415.pigim.pigim.Adapter.UserAdapter;
import com.android415.pigim.pigim.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.android415.pigim.pigim.Utils.MESSAGES_KEY;
import static com.android415.pigim.pigim.Utils.PROFILE_PIC;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = this.getClass().getSimpleName();

    private static FirebaseUser mUser;
    private String mConversation;

    // Drawer
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;

    // Recycler View
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> listOfUsers = new ArrayList<>();
    private String profilePic;

    private ImageView profilePicView;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.setTheme();

        profilePic = Utils.mPreferences.getString(PROFILE_PIC, "default");
        setProfilePic();


        mDrawerLayout = findViewById(R.id.drawer_layout);

        // toggle for nav drawer open/close
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // used to connect to nav drawer listener
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        // recycler view of users
        readUsers();
        recyclerView = findViewById(R.id.recycler_view);
        userAdapter = new UserAdapter(this,listOfUsers);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    // Makes the call to the database to populate the list of users
    private void readUsers() {

        listOfUsers.clear();

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert currentUser != null;

                    if(!currentUser.getUid().equals(user.getId())) {
                        listOfUsers.add(user);
                        userAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        else if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, StartActivity.class));
            finish();
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

    @Override
    protected void onResume() {
        super.onResume();
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

    // Sets the profile pic in the drawer
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
                profilePicView.setImageResource(R.mipmap.default_user_icon);
                break;
            }

        }

    }
}
