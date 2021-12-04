package com.example.eventwithus.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.eventwithus.R;
import com.example.eventwithus.RequestQueueSingleton;
import com.example.eventwithus.adapters.MyEventAdapter;
import com.example.eventwithus.models.EventHelper;
import com.example.eventwithus.models.MyEvents;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RsvpFragment extends Fragment {

    public static final String TAG = "RsvpFragment";

    private RecyclerView recyclerView;
    private MyEventAdapter myEventAdapter;
    private ArrayList<MyEvents> eventsList;
    private ParseUser currentUser;

    public RsvpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rsvp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rvMyEventsList);
        recyclerView.setHasFixedSize(true);
        EventHelper.refreshUserData();
        currentUser = ParseUser.getCurrentUser();

        eventsList = new ArrayList<>();
        myEventAdapter = new MyEventAdapter(getContext(), eventsList);


        recyclerView.setAdapter(myEventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        parseJSON();
    }

    private void parseJSON() {
        String url = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&city=San%20Antonio";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {

                        JSONArray jsonArray = response.getJSONObject("_embedded").getJSONArray("events");

                        Log.d(TAG,"onResponseSuccess");
                        String imageURL = "";

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject hit = jsonArray.getJSONObject(i);

                            String rawDate = hit.getJSONObject("dates").getJSONObject("start").getString("localDate");
                            String formattedDate = "123_" + EventHelper.formatJsonDate(rawDate);
                            String[] rsvdEvents = Objects.requireNonNull(currentUser.getString("eventsinfo")).split(",");

                            // checks if user Rsvp'd for the event
                            if (Arrays.asList(rsvdEvents).contains(formattedDate)) {
                                String eventName = hit.getString("name");
                                String city = hit.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("city").getString("name");
                                String date = hit.getJSONObject("dates").getJSONObject("start").getString("localDate");
                                String startTime = hit.getJSONObject("dates").getJSONObject("start").getString("localTime");
                                String venueName = hit.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                                String eventType = hit.getString("type");
                                String eventId = hit.getString("id");
                                String eventLink = hit.getString("url");

                                JSONArray imagesArray = hit.getJSONArray("images");

                                for (int j = 0; j < imagesArray.length(); j++) {
                                    JSONObject elem = imagesArray.getJSONObject(j);
                                    imageURL = elem.getString("url");//gets the image url
                                }
                                eventsList.add(new MyEvents(eventName, city, date, startTime, venueName, eventType, eventId, imageURL, eventLink));
                            }

                            if(isAdded())
                            myEventAdapter = new MyEventAdapter(getActivity().getBaseContext(), eventsList);
                            recyclerView.setAdapter(myEventAdapter);
                            //myEventAdapter.setOnItemClickListener(StreamFragment.this );
                        }
                    } catch (JSONException e) {
                        Log.e(TAG,"onResponse Failure :"+e);
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        RequestQueueSingleton.getInstance(getActivity().getBaseContext()).addToRequestQueue(request);
    }
}