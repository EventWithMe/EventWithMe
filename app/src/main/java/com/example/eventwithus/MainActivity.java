package com.example.eventwithus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

public class MainActivity extends AppCompatActivity implements Initializable {

    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_EVENT_NAME = "eventName";
    public static final String EXTRA_EVENT_TYPE = "type";


    public static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_stream:
                        refreshFragment();
                        fragment = new StreamFragment();
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
        refreshFragment();
    }
}