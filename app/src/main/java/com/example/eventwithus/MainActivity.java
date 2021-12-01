package com.example.eventwithus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.eventwithus.fragments.ChatFragment;
import com.example.eventwithus.fragments.ProfileFragment;
import com.example.eventwithus.fragments.RsvpFragment;
import com.example.eventwithus.fragments.StreamFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

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
                        fragment = new StreamFragment();
                        break;
                    case R.id.action_rsvp:
                        fragment = new RsvpFragment();
                        break;
                    case R.id.action_chat:
                        fragment = new ChatFragment();
                        break;
                    case R.id.action_profile:
                    default:
                        fragment = new ProfileFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_stream);
    }
}