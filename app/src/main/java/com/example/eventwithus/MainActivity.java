package com.example.eventwithus;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.load.engine.Initializable;
import com.example.eventwithus.fragments.ChatFragment;
import com.example.eventwithus.fragments.MapViewFragment;
import com.example.eventwithus.fragments.ProfileFragment;
import com.example.eventwithus.fragments.RsvpFragment;
import com.example.eventwithus.fragments.SearchFragment;
import com.example.eventwithus.fragments.StreamFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.ArrayList;


@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity implements Initializable, SearchFragment.FragmentSearchListener, StreamFragment.FragmentStreamListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_EVENT_NAME = "eventName";
    public static final String EXTRA_EVENT_TYPE = "type";

    private StreamFragment streamFragment;
    private MapViewFragment mapViewFragment;
    public static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private static final String APP_ID = "9DA1B1F4-0BE6-4DA8-82C5-2E81DAB56F23"; // US-1 Demo
    public static final String VERSION = "3.0.40";
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        streamFragment = new StreamFragment();
        mapViewFragment = new MapViewFragment();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // PreferenceUtils.init(getApplicationContext());

        // SendBird.init(APP_ID, getApplicationContext());

        bottomNavigationView.setOnItemSelectedListener(item -> {

            Fragment fragment;
            int id = item.getItemId();
            if (id == R.id.action_stream) {
                fragment = streamFragment;
                // refreshFragment();
            } else if (id == R.id.action_rsvp) {
                fragment = new RsvpFragment();
            } else if (id == R.id.get_location) {
                fragment = new MapViewFragment();
            } else {
                fragment = new ProfileFragment();
            }

            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment, "myFragmentTag").commit();
            return true;
        });



        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_stream);
    }

    /*
    public void refreshFragment(){
        Fragment currentFragment = null;
        Toast.makeText(this, "RefreshFragment ", Toast.LENGTH_LONG).show();
        if (currentFragment instanceof StreamFragment) {
            FragmentTransaction transaction =  fragmentManager.beginTransaction();
            transaction.detach(currentFragment);
            transaction.attach(currentFragment);
            transaction.commit();
        }
    }
    */

    @Override
    public void initialize() {
      //  refreshFragment();
    }

    @Override
    public void onInputSearchSent(CharSequence input, CharSequence keyword, CharSequence city) {
       streamFragment.updateEditText(input, keyword, city);
    }

    @Override
    public void onCityDateKeywordSearchSent(CharSequence city, LocalDate Date, CharSequence KeyWord) {
        streamFragment.updateCityDateKeywordText(city, Date, KeyWord );
    }



    @Override
    public void onRefresh() {

    }

    @Override
    public void onInputStreamSent(ArrayList<EventMarker> eventMarkers) {
        mapViewFragment.updateEventMarkers(eventMarkers);
    }
}