package com.example.eventwithus.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.load.engine.Initializable;
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


public class StreamFragment extends Fragment  implements  EventAdapter.OnItemClickListener , Initializable {
    private StreamFragment.FragmentStreamListener listener;

    public interface  FragmentStreamListener{
        void onInputStreamSent(CharSequence input);
    }
    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_EVENT_NAME = "eventName";
    public static final String EXTRA_EVENT_TYPE = "type";
    public static final String EXTRA_EVENT_DATE = "date";
    public static final String EXTRA_EVENT_ID = "id";
    public static final String EXTRA_EVENT_VENUE_NAME = "venueName";

    private Button searchBtn;
    private EditText inputET;
    private RecyclerView mRecyclerView;
    private EventAdapter eventAdapter;
    private ArrayList<EventItem> mEventList;
    private ArrayList<EventDetail> mDetailList;
    private RequestQueue mRequestQueue;
    private Toolbar toolbar;
    public static final String TAG = "StreamFragment";
    private RecyclerView rvEvents;
    protected EventsAdapter adapter;
    protected List<Event> allEvents;
    Spinner spinner2;
    String StreamText = "";
    final String keyword = "keyword=";
     String keyword2;

    final String apikey = "apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*";
    final String city = "&city=San%20Antonio";
    final String eventsurl = "https://app.ticketmaster.com/discovery/v2/events?";


    String url = "https://pixabay.com/api/?key=5303976-fd6581ad4ac165d1b75cc15b3&q=kitten&image_type=photo&pretty=true";
    String url2 = "https://app.ticketmaster.com/discovery/v2/events/k7vGFKzleBdwS/images.json?apikey=kdQ1Zu3hN6RX9";//images TICKETMASTER
    String url3 = "https://app.ticketmaster.com/discovery/v2/events/?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*";
    String url4 = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&city=San%20Antonio";
    //String url5 = "https://app.ticketmaster.com/discovery/v2/events?+"+keyword2+"&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&city=San%20Antonio&daterange=this-weekend";
    String url6 = "https://app.ticketmaster.com/discovery/v2/events?keyword=rock&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&city=San%20Antonio&daterange=this-weekend";

    String music = "https://app.ticketmaster.com/discovery/v2/events?keyword="+keyword2+"&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=music";
    String sports = "https://app.ticketmaster.com/discovery/v2/events?"+keyword2+"&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=sports";
    String family = "https://app.ticketmaster.com/discovery/v2/events?"+keyword2+"&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&keyword=family&locale=*";
    String film = "https://app.ticketmaster.com/discovery/v2/events?"+keyword2+"&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=Film";
    String misc = "https://app.ticketmaster.com/discovery/v2/events?"+keyword2+"&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&city=San%20Antonio";
    String artNThr = "https://app.ticketmaster.com/discovery/v2/events?"+keyword2+"&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&keyword=Arts%20&%20Theater&locale=*";



    public StreamFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true); // for overflow menu

        if (getArguments() != null) {
          String  keyword = getArguments().getString("UserInput");
            Toast.makeText(getContext(), "keyword :"+keyword, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        //menu.clear(); / / this sentence is useless. You don't need to add it
        inflater.inflate(R.menu.menu_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_preferences) {
            Toast.makeText(getContext(), "Preferences", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
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
        Toast.makeText(getContext(), "onViewCreated ", Toast.LENGTH_LONG).show();
        String[] Categories = { "Concerts", "Sports", "Arts & Theater", "Family", "Film", "Misc"};
        String[] Dates = {};
        toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        //TODO implement Dates for Search Filter

        mEventList = new ArrayList<>();
        mDetailList = new ArrayList<>();
        // mRequestQueue = Volley.newRequestQueue(this);
        // parseJSON();


        searchBtn = view.findViewById(R.id.searchBtn);
        //inputET = view.findViewById(R.id.inputET);
        spinner2 = view.findViewById(R.id.spinner2);


        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> aa = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Categories);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2.setAdapter(aa);
        mRecyclerView = view.findViewById(R.id.rvStreamList);
        mRecyclerView.setHasFixedSize(true);
        // allEvents = new ArrayList<>();
        // adapter = new EventsAdapter(getContext(), allEvents);
        // mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        populateEvents();

        // keep just in case we want to implement this functionality in the stream fragment
/*
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(), "RefreshFragment ", Toast.LENGTH_LONG).show();
                Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentByTag("myFragmentTag");

                if (currentFragment instanceof StreamFragment) {
                    FragmentTransaction fragTransaction =   getFragmentManager().beginTransaction();
                    fragTransaction.detach(currentFragment);
                    fragTransaction.attach(currentFragment);
                    fragTransaction.commit();
                }
                switch (StreamText) {
                    case "Concerts":
                        System.out.println("Concerts");
                        parseJSON2(music);
                        break;
                    case "Sports":
                        System.out.println("Sports");
                        parseJSON2(sports);
                        break;
                    case "Family":
                        System.out.println("Family");
                        parseJSON2(family);
                        break;
                    case "Film":
                        System.out.println("Film");
                        parseJSON2(film);
                        break;
                    case "Misc":
                        System.out.println("Misc");
                        parseJSON2(misc);
                        break;
                    case "Arts & Theater":
                        System.out.println("Saturday");
                        parseJSON2(artNThr);
                        break;
                    default:
                        parseJSON2(misc);

                }
            }
        });
    }

 */

    }

    //choose which url to go with based on Search Filter Category
    public void populateEvents(){

        switch (StreamText) {
            case "Concerts":
                System.out.println("Concerts");
                parseJSON2(music);
                break;
            case "Sports":
                System.out.println("Sports");
                parseJSON2(sports);
                break;
            case "Family":
                System.out.println("Family");
                parseJSON2(family);
                break;
            case "Film":
                System.out.println("Film");
                parseJSON2(film);
                break;
            case "Misc":
                System.out.println("Misc");
                parseJSON2(misc);
                break;
            case "Arts & Theater":
                System.out.println("Saturday");
                parseJSON2(artNThr);
                break;
            case "":
                parseJSON2(misc);

        }
    }

    //update edit text acts as Intent get EXTRA but for fragments, Here we are getting the results from filters passed from MainActivity.
    public void updateEditText(CharSequence newText, CharSequence newText2, CharSequence cityName) {
        StreamText= (String) newText;
        keyword2 = (String) newText2;
        music = "https://app.ticketmaster.com/discovery/v2/events?keyword="+keyword2+"&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=music&city="+cityName;
       sports = "https://app.ticketmaster.com/discovery/v2/events?"+keyword2+"&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=sports&city="+cityName;
       family = "https://app.ticketmaster.com/discovery/v2/events?"+keyword2+"&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&keyword=family&locale=*&city="+cityName;
        film = "https://app.ticketmaster.com/discovery/v2/events?"+keyword2+"&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=Film&city="+cityName;
        misc = "https://app.ticketmaster.com/discovery/v2/events?"+keyword2+"&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&city="+cityName;
        artNThr = "https://app.ticketmaster.com/discovery/v2/events?"+keyword2+"&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&keyword=Arts%20&%20Theater&locale=*&city="+cityName;
    }

/*
    private void parseJSON(String url) {

        Toast.makeText(getContext(), url, Toast.LENGTH_LONG).show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
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
                                // TODO: 12/1/2021 get the unique id for an event from the Json
                                // String id = hit.getString("");

                                // String type = hit.getString("type");
                                JSONArray imagesArray = hit.getJSONArray("images");
                                String info = hit.getString("info");
                                for (int j = 0; j < imagesArray.length(); j++) {
                                    JSONObject elem = imagesArray.getJSONObject(j);

                                    eventImage = elem.getString("url");//gets the image url

                                }

                                // TODO: 12/1/2021 add the id to this new call
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
*/

    private void parseJSON2(String url) {
        // Toast.makeText(getContext(),StreamText, Toast.LENGTH_LONG).show();
        // Toast.makeText(getContext(),keyword2, Toast.LENGTH_LONG).show();
        // Toast.makeText(getContext(),url, Toast.LENGTH_LONG).show();
        // Toast.makeText(getContext(), url, Toast.LENGTH_LONG).show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {

                        JSONArray jsonArray = response.getJSONObject("_embedded").getJSONArray("events");

                        Log.d("Main Activity","onResponseSuccess");
                        String eventImage = "";
                        String venueName = "";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject hit = jsonArray.getJSONObject(i);
                            String info = jsonArray.getJSONObject(i).isNull("info") ? null : jsonArray.getJSONObject(i).getString("info");
                            String eventid = hit.getString("id");
                            String eventName = hit.getString("name");
                            String date = hit.getJSONObject("dates").getJSONObject("start").getString("localDate");
                            JSONObject _embedded2 = hit.getJSONObject("_embedded");
                            JSONArray venuesArray = _embedded2.getJSONArray("venues");
                            for (int k = 0; k < venuesArray.length(); k++) {
                                JSONObject elem = venuesArray.getJSONObject(k);

                                 venueName = elem.getString("name");//gets the image url

                            }
                            // TODO: 12/1/2021 get the unique id for an event from the Json
                            // String id = hit.getString("");
                            // String type = hit.getString("type");
                            JSONArray imagesArray = hit.getJSONArray("images");

                            for (int j = 0; j < imagesArray.length(); j++) {
                                JSONObject elem = imagesArray.getJSONObject(j);

                                eventImage = elem.getString("url");//gets the image url

                            }

                            // TODO: 12/1/2021 add the id to this new call
                            mEventList.add(new EventItem(eventImage, eventName, date));
                            mDetailList.add(new EventDetail(info,eventid, venueName));
                        }
                        FragmentActivity activity = getActivity();
                        if (activity != null) {
                            eventAdapter = new EventAdapter(activity.getBaseContext(), mEventList);
                        }
                        mRecyclerView.setAdapter(eventAdapter);
                        eventAdapter.setOnItemClickListener(StreamFragment.this );

                    } catch (JSONException e) {
                        Log.e(TAG,"onResponse Failure :"+e);
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);

        FragmentActivity activity = getActivity();
        if (activity != null) {
            RequestQueueSingleton.getInstance(activity.getBaseContext()).addToRequestQueue(request);
        }

    }

    @Override
    public void onItemClick(int position) {

        FragmentActivity fragmentActivity = new FragmentActivity();
        FragmentManager manager = fragmentActivity.getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = manager.beginTransaction();

        Fragment fragment = manager.findFragmentByTag("myFragmentTag");
        if (fragment != null) {
            fragmentTransaction.detach(fragment);
            fragmentTransaction.attach(fragment);
            fragmentTransaction.commit();
        }

        FragmentActivity activity = getActivity();
        if (activity != null) {
            Intent detailIntent = new Intent(activity.getBaseContext(), EventDetailActivity.class);

            EventItem clickedItem = mEventList.get(position);
            EventDetail clickedItem1 = mDetailList.get(position);
            // JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, ur)

            detailIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
            detailIntent.putExtra(EXTRA_EVENT_NAME, clickedItem.getCreator());
            detailIntent.putExtra(EXTRA_EVENT_DATE, clickedItem.getDate());
            detailIntent.putExtra(EXTRA_EVENT_ID, clickedItem1.getId());
            detailIntent.putExtra(EXTRA_EVENT_TYPE, clickedItem1.getInfo());
            detailIntent.putExtra(EXTRA_EVENT_VENUE_NAME, clickedItem1.getVenueName());

            getActivity().startActivity(detailIntent);
        }
    }




    @Override
    public void initialize() {


    }
}