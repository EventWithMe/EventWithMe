package com.example.eventwithus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.SurfaceControl;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Initializable;
import com.example.eventwithus.fragments.ChatFragment;
import com.example.eventwithus.fragments.ProfileFragment;
import com.example.eventwithus.fragments.RsvpFragment;
import com.example.eventwithus.fragments.SearchFragment;
import com.example.eventwithus.fragments.StreamFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.Parse;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements Initializable, SearchFragment.FragmentSearchListener, StreamFragment.FragmentStreamListener {

    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_EVENT_NAME = "eventName";
    public static final String EXTRA_EVENT_TYPE = "type";

    private StreamFragment streamFragment;
    public static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private static final String APP_ID = "9DA1B1F4-0BE6-4DA8-82C5-2E81DAB56F23"; // US-1 Demo
    public static final String VERSION = "3.0.40";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // StreamFragment streamFragment;
        streamFragment = new StreamFragment();
       // streamFragment = new StreamFragment();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
      //  PreferenceUtils.init(getApplicationContext());

       // SendBird.init(APP_ID, getApplicationContext());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment;

                switch (item.getItemId()) {
                    case R.id.action_stream:
                        fragment = streamFragment;
                       // refreshFragment();
                        break;
                    case R.id.action_rsvp:
                        fragment = new RsvpFragment();
                        break;
                    case R.id.get_location:
                        fragment = new SearchFragment();
                        break;
                    case R.id.action_chat:
                        fragment = new ChatFragment();
                        break;
                    case R.id.action_profile:
                    default:
                        fragment = new ProfileFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment, "myFragmentTag").commit();
                return true;
            }
        });



        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_stream);
    }
    public void refreshFragment(){
        Fragment currentFragment = null;
        Toast.makeText(this, "RefreshFragment ", Toast.LENGTH_LONG).show();
        if (currentFragment instanceof StreamFragment) {
           FragmentTransaction trans =  fragmentManager.beginTransaction();
            trans.detach(currentFragment);
            trans.attach(currentFragment);
            trans.commit();
        }

    }

    @Override
    public void initialize() {
      //  refreshFragment();
    }

    @Override
    public void onInputSearchSent(CharSequence input, CharSequence keyword) {
       streamFragment.updateEditText(input, keyword);
    }

    @Override
    public void onInputStreamSent(CharSequence input) {

    }
}