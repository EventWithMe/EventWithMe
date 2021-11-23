package com.example.eventwithus.fragments;

import android.content.Intent;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.eventwithus.EventAdapter;
import com.example.eventwithus.EventDetailActivity;
import com.example.eventwithus.EventsAdapter;
import com.example.eventwithus.R;
import com.example.eventwithus.RequestQueueSingleton;
import com.example.eventwithus.models.Event;
import com.example.eventwithus.models.EventDetail;
import com.example.eventwithus.models.EventItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class StreamFragment extends Fragment  implements  EventAdapter.OnItemClickListener{
    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_EVENT_NAME = "eventName";
    public static final String EXTRA_EVENT_TYPE = "type";

    private RecyclerView mRecyclerView;
    private EventAdapter eventAdapter;
    private ArrayList<EventItem> mEventList;
    private ArrayList<EventDetail> mDetailList;
    private RequestQueue mRequestQueue;




    public static final String TAG = "StreamFragment";
    private RecyclerView rvEvents;
    protected EventsAdapter adapter;
    protected List<Event> allEvents;

    public StreamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stream, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        mEventList = new ArrayList<>();
        mDetailList = new ArrayList<>();
        //mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();

        mRecyclerView = view.findViewById(R.id.rvStreamList);
        mRecyclerView.setHasFixedSize(true);
        //allEvents = new ArrayList<>();
        //adapter = new EventsAdapter(getContext(), allEvents);
       // mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    protected void populateEvents() {
        // TODO: 11/21/2021 method will get data from the ticketmaster API & store it in the events model
    }
    private void parseJSON() {
        String url = "https://pixabay.com/api/?key=5303976-fd6581ad4ac165d1b75cc15b3&q=kitten&image_type=photo&pretty=true";
        String url2 = "https://app.ticketmaster.com/discovery/v2/events/k7vGFKzleBdwS/images.json?apikey=kdQ1Zu3hN6RX9";//images TICKETMASTER
        String url3 = "https://app.ticketmaster.com/discovery/v2/events/?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*";
        String url4 = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&city=San%20Antonio";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url4, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONObject("_embedded").getJSONArray("events");

                            Log.d("Main Activity","onResponseSuccess");
                            String eventImage = "";

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String eventName = hit.getString("name");
                                String date = hit.getJSONObject("dates").getJSONObject("start").getString("localDate");
                                // String type = hit.getString("type");
                                JSONArray imagesArray = hit.getJSONArray("images");
                                String info = hit.getString("info");
                                for (int j = 0; j < imagesArray.length(); j++) {
                                    JSONObject elem = imagesArray.getJSONObject(j);

                                    eventImage = elem.getString("url");//gets the image url

                                }


                                mEventList.add(new EventItem(eventImage, eventName, date));
                                mDetailList.add(new EventDetail(info));
                            }

                            eventAdapter = new EventAdapter(getActivity().getBaseContext(), mEventList);
                            mRecyclerView.setAdapter(eventAdapter);
                            eventAdapter.setOnItemClickListener(StreamFragment.this );

                        } catch (JSONException e) {
                            Log.e(TAG,"onResponse Failure :"+e);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueueSingleton.getInstance(getActivity().getBaseContext()).addToRequestQueue(request);

    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(getActivity().getBaseContext(), EventDetailActivity.class );
        EventItem clickedItem = mEventList.get(position);
        EventDetail clickedItem1 = mDetailList.get(position);
        // JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, ur)


        detailIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
        detailIntent.putExtra(EXTRA_EVENT_NAME, clickedItem.getCreator());


        detailIntent.putExtra(EXTRA_EVENT_TYPE, clickedItem1.getInfo());

        getActivity().startActivity(detailIntent);
    }

}