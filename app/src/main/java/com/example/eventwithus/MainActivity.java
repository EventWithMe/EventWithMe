package com.example.eventwithus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.load.engine.Initializable;
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
    boolean doubleBackToExitPressedOnce = false;
    //final Fragment fragment1 = new MapViewFragment();
    final Fragment fragment2 = new StreamFragment();
   // final Fragment fragment3 = new RsvpFragment();
    //final Fragment fragment4 = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();






    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_EVENT_NAME = "eventName";
    public static final String EXTRA_EVENT_TYPE = "type";







    private StreamFragment streamFragment;
    private MapViewFragment mapViewFragment;
    public static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    //final FragmentManager fm = getSupportFragmentManager();
    private static final String APP_ID = "9DA1B1F4-0BE6-4DA8-82C5-2E81DAB56F23"; // US-1 Demo
    public static final String VERSION = "3.0.40";
    private SwipeRefreshLayout swipeRefreshLayout;
    //Fragment active = fragment1;
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
            switch (item.getItemId()) {
                case R.id.get_location:
                    fragment = mapViewFragment;
                    fm.beginTransaction().replace(R.id.flContainer, fragment, "myFragmentTag").commit();
                    return true;
                case R.id.action_stream:
                    //fragment = new StreamFragment();
                    //setFragment(fragment2, "2", 1);
                    fragment = streamFragment;
                    fm.beginTransaction().replace(R.id.flContainer, fragment, "myFragmentTag").commit();
                    return true;
                case R.id.action_rsvp:
                    fragment = new RsvpFragment();
                    fm.beginTransaction().replace(R.id.flContainer, fragment, "myFragmentTag").commit();
                    return true;
                case R.id.action_profile:
                    fragment = new ProfileFragment();
                    fm.beginTransaction().replace(R.id.flContainer, fragment, "myFragmentTag").commit();
                    //setFragment(fragment, "4", 3);
                    return true;
            }











/**
            Fragment fragment;
            int id = item.getItemId();
            if (id == R.id.action_stream) {
              //  fragment = streamFragment;
                setFragment(streamFragment, "myFragmentTag", 0);
                // refreshFragment();
            } else if (id == R.id.action_rsvp) {
               // fragment = new RsvpFragment();
                setFragment(RsvpFragment, "1", 0);
            } else if (id == R.id.get_location) {
                fragment = new MapViewFragment();
            } else {
                fragment = new ProfileFragment();
            }

            fm.beginTransaction().replace(R.id.flContainer, fragment, "myFragmentTag").commit();
 **/

            return true;
        });



        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_stream);
    }
    /**
    public void setFragment(Fragment fragment, String tag, int position) {
        if (fragment.isAdded()) {
            fm.beginTransaction().hide(active).show(fragment).commit();
        } else {
            fm.beginTransaction().add(R.id.flContainer, fragment, tag).commit();
        }
        bottomNavigationView.getMenu().getItem(position).setChecked(true);
        active = fragment;
    }

**/




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

    /**
    @Override
    public void onBackPressed() {
        if (active == fragment1) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        } else {
            setFragment(fragment1, "1", 0);
        }
    }
**/


    @Override
    public void onRefresh() {

    }

    @Override
    public void onInputStreamSent(ArrayList<EventMarker> eventMarkers, ArrayList<RsvpTag> rsvpTags) {
        if(eventMarkers != null) {
            mapViewFragment.updateEventMarkers(eventMarkers, rsvpTags);

        }

    }
}