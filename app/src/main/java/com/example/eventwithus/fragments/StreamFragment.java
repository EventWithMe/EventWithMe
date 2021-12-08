package com.example.eventwithus.fragments;

import android.content.Intent;
import android.content.res.Configuration;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StreamFragment extends Fragment implements EventAdapter.OnItemClickListener, Initializable , StreamDialogFragment.OnInputSelected{
    private StreamFragment.FragmentStreamListener listener;




    public interface FragmentStreamListener {
        void onInputStreamSent(CharSequence input);
    }

    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_EVENT_NAME = "eventName";
    public static final String EXTRA_EVENT_TYPE = "type";
    public static final String EXTRA_EVENT_DATE = "date";
    public static final String EXTRA_EVENT_ID = "id";
    public static final String EXTRA_EVENT_VENUE_NAME = "venueName";
    public static final String EXTRA_EVENT_START_TIME = "startTime";
    public static final String EXTRA_VENUE_CITY = "venueCity";

    private Button searchBtn, backBTN;
    private EditText inputET;
    private RecyclerView mRecyclerView;
    private EventAdapter eventAdapter;
    private ArrayList < EventItem > mEventList;
    private ArrayList < EventDetail > mDetailList;
    private RequestQueue mRequestQueue;
    public static final String TAG = "StreamFragment";
    private RecyclerView rvEvents;
    protected EventsAdapter adapter;
    protected List < Event > allEvents;
    Spinner spinner2;
    String CategoryText = "";
    final String keyword = "keyword=";
    String keyword2;
    String Genreid = "";
    final String apikey = "apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*";
     String cityName = "";
    final String eventsurl = "https://app.ticketmaster.com/discovery/v2/events?";
    String[] MusicGenres;
    String[] SportGenres;

    // String url2 = "https://app.ticketmaster.com/discovery/v2/events/k7vGFKzleBdwS/images.json?apikey=kdQ1Zu3hN6RX9";//images TICKETMASTER
    //String url3 = "https://app.ticketmaster.com/discovery/v2/events/?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*";
    // String url4 = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&city=San%20Antonio";
    //String url5 = "https://app.ticketmaster.com/discovery/v2/events?+"+keyword2+"&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&city=San%20Antonio&daterange=this-weekend";
    // String url6 = "https://app.ticketmaster.com/discovery/v2/events?keyword=rock&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&city=San%20Antonio&daterange=this-weekend";

    //to get all genres by category
    String SportsGenreURL = "https://app.ticketmaster.com/discovery/v2/classifications/segments/KZFzniwnSyZfZ7v7nE?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*";
    String MusicGenreURL = "https://app.ticketmaster.com/discovery/v2/classifications/segments/KZFzniwnSyZfZ7v7nJ?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*";

    String music = "https://app.ticketmaster.com/discovery/v2/events?&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=music";
    String sports = "https://app.ticketmaster.com/discovery/v2/events?&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=sports";
    String family = "https://app.ticketmaster.com/discovery/v2/events?&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&keyword=family&locale=*";
    String film = "https://app.ticketmaster.com/discovery/v2/events?&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=Film";
    String misc = "https://app.ticketmaster.com/discovery/v2/events?&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&city=San%20Antonio";
    String artNThr = "https://app.ticketmaster.com/discovery/v2/events?&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&keyword=Arts%20&%20Theater&locale=*";

    String sportsGenre = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=sports&genreId=" + Genreid;
    String musicGenre = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=music&genreId=" + Genreid;
    String artsGenre = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentId=KZFzniwnSyZfZ7v7na&genreId=" + Genreid;
    String filmGenre = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=film&genreId=" + Genreid;

    @Override
    public void sendInput(String searchInput, String startDate, String endDate, String city) {
        Log.i(TAG,"sent info :"+searchInput+" "+startDate+" "+endDate+" "+city);
        MySearchQuery(searchInput, startDate, endDate, city);
    }

    String searchFilerURL = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&keyword=Rock&locale=*&startDateTime=2021-12-08T16:27:00Z&endDateTime=2021-12-30T16:27:00Z&city="+cityName;

    String CurrCat = "";

    Map < String, String > Genresid = new HashMap < > ();
    List < String > GenresList = new ArrayList < String > ();

    public StreamFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            String keyword = getArguments().getString("UserInput");
            Toast.makeText(getContext(), "keyword :" + keyword, Toast.LENGTH_LONG).show();
        }
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
        String[] Categories = {
                "All Events",
                "My Search",
                "Concerts",
                "Sports",
                "Arts & Theater",
                "Family",
                "Film",
                "Misc"
        };
        String[] Dates = {};
        List < String > allSports = Arrays.asList(GetGenres(SportsGenreURL));
        List < String > allMusic = Arrays.asList(GetGenres(MusicGenreURL));
        Log.i(TAG, String.valueOf(allSports));
        Log.i(TAG, String.valueOf(allMusic));

        String[] MusicGenre = {
                "Rock",
                "Dance/Electronic",
                "Country",
                "Hip-Hop/Rap",
                "Jazz",
                "Pop",
                "Classical"
        };
        String[] SportsGenre = {
                "Boxing",
                "Tennis",
                "Swimming",
                "Surfing",
                "Volleyball",
                "Aquatics",
                "Fitness",
                "Football",
                "Soccer",
                "Softball",
                "Baseball",
                "Basketball"
        };
        String[] ArtsNThtre = {
                "Classical",
                "Comedy",
                "Cultural",
                "Magic & Illusion",
                "Miscellaneous",
                "Miscellaneous Theatre",
                "Music",
                "Opera",
                "Theatre"
        };
        String[] Film = {
                "Action/Adventure",
                "Animation",
                "Arthouse",
                "Comedy",
                "Documentary",
                "Drama",
                "Family",
                "Foreign",
                "Horror",
                "Miscellaneous",
                "Music",
                "Science Fiction",
                "Urban"
        };

        Map < String, String > Genresid = new HashMap < > ();
        Genresid.put("Rock", "KnvZfZ7vAvt");
        Genresid.put("Dance/Electronic", "KnvZfZ7vAvF");
        Genresid.put("Country", "KnvZfZ7vAv6");
        Genresid.put("Hip-Hop/Rap", "KnvZfZ7vAv1");
        Genresid.put("Classical", "KnvZfZ7vAeA");
        Genresid.put("Pop", "KnvZfZ7vAev");
        Genresid.put("Jazz", "KnvZfZ7vAvE");
        //sports
        Genresid.put("Boxing", "KnvZfZ7vAdA");
        Genresid.put("Volleyball", "KnvZfZ7vAA7");
        Genresid.put("Aquatics", "KnvZfZ7vAeI");
        Genresid.put("Fitness", "KnvZfZ7vAJ7");
        Genresid.put("Football", "KnvZfZ7vAdE");
        Genresid.put("Soccer", "KnvZfZ7vA7E");
        Genresid.put("Swimming", "KnvZfZ7vA7n");
        Genresid.put("Baseball", "KnvZfZ7vAdv");
        Genresid.put("Basketball", "KnvZfZ7vAde");
        Genresid.put("Surfing", "KnvZfZ7vA7t");
        Genresid.put("Tennis", "KnvZfZ7vAAv");
        //Arts & Theater
        Genresid.put("Classical", "KnvZfZ7v7nJ");
        Genresid.put("Comedy", "KnvZfZ7vAe1");
        Genresid.put("Cultural", "KnvZfZ7v7nE");
        Genresid.put("Dance", "KnvZfZ7v7nI");
        Genresid.put("Magic & Illusion", "KnvZfZ7v7lv");
        Genresid.put("Miscellaneous", "KnvZfZ7v7le");
        Genresid.put("Miscellaneous Theatre", "KnvZfZ7v7nJ");
        Genresid.put("Music", "KnvZfZ7v7lA");
        Genresid.put("Opera", "KnvZfZ7v7lA");
        Genresid.put("Theatre", "KnvZfZ7v7l1");
        //Family
        Genresid.put("Action/Adventure", "KnvZfZ7vAke");
        Genresid.put("Animation", "KnvZfZ7vAkd");
        Genresid.put("Arthouse", "KnvZfZ7vAk7");
        Genresid.put("Comedy", "KnvZfZ7vAkA");
        Genresid.put("Documentary", "KnvZfZ7vAkk");
        Genresid.put("Drama", "KnvZfZ7vAk6");
        Genresid.put("Family", "KnvZfZ7vAkF");
        Genresid.put("Foreign", "KnvZfZ7vAk1");
        Genresid.put("Horror", "KnvZfZ7vAJk");
        Genresid.put("Miscellaneous", "KnvZfZ7vAka");
        Genresid.put("Science Fiction", "KnvZfZ7vAJa");

        Configuration configuration = new Configuration();
        int currentNightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                Log.i(TAG, "NIGHT NO");
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                Log.i(TAG, "NIGHT YES");
                break;
        }

        //TODO implement Dates for Search Filter
        mEventList = new ArrayList < > ();
        mDetailList = new ArrayList < > ();
        backBTN = view.findViewById(R.id.backBTN);
        searchBtn = view.findViewById(R.id.searchBtn);
        spinner2 = view.findViewById(R.id.spinner2);

        updateSpinner2(Categories);

        mRecyclerView = view.findViewById(R.id.rvStreamList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEventList.clear();
                parseJSON2(misc);
                updateSpinner2(Categories);
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView < ? > adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    if (spinner2.getSelectedItem() == "My Search") {




                        StreamDialogFragment dialog = new StreamDialogFragment();
                        dialog.setTargetFragment(StreamFragment.this, 1);
                        dialog.show(getFragmentManager(), "MyCustomDialog");

                        Log.i(TAG, "RETURNED FROM DIALOG");
                        /**
                       StreamFilterDialog d = new StreamFilterDialog(getContext());
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(d.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                        d.show();
                        d.getWindow().setAttributes(lp);
                      // Dialog.show();
                        **/

                    }
                    if (spinner2.getSelectedItem() == "All Events") {
                        mEventList.clear();
                        parseJSON2(misc);

                    }
                    if (Arrays.asList(MusicGenre).contains(spinner2.getSelectedItem().toString())) {
                        Genreid = Genresid.get(spinner2.getSelectedItem().toString());
                        musicGenre = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=music&genreId=" + Genreid;
                        mEventList.clear();
                        parseJSON2(musicGenre);
                    }
                    if (Arrays.asList(SportsGenre).contains(spinner2.getSelectedItem().toString())) {
                        Genreid = Genresid.get(spinner2.getSelectedItem().toString());
                        sportsGenre = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=sports&genreId=" + Genreid;
                        mEventList.clear();
                        parseJSON2(sportsGenre);
                    }
                    if (Arrays.asList(ArtsNThtre).contains(spinner2.getSelectedItem().toString())) {
                        Genreid = Genresid.get(spinner2.getSelectedItem().toString());
                        artsGenre = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentId=KZFzniwnSyZfZ7v7na&genreId=" + Genreid;
                        mEventList.clear();
                        parseJSON2(artsGenre);
                    }
                    if (Arrays.asList(Film).contains(spinner2.getSelectedItem().toString())) {
                        Genreid = Genresid.get(spinner2.getSelectedItem().toString());
                        filmGenre = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=film&genreId=" + Genreid;
                        mEventList.clear();
                        parseJSON2(filmGenre);
                    }

                    Toast.makeText(getContext(), item.toString(),
                            Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getContext(), "Selected",
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView < ? > adapterView) {

                // TODO Auto-generated method stub
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(), "searchBtn Clicked ", Toast.LENGTH_LONG).show();
                // clear all old events displayed before displaying new ones
                mEventList.clear();
                String cityName = "";

                switch (spinner2.getSelectedItem().toString()) {
                    case "All Events":
                        System.out.println("All Events");
                        parseJSON2(misc);
                        break;
                    case "Concerts":
                        CurrCat = "rock";
                        System.out.println("Concerts");
                        parseJSON2(music);
                        updateSpinner2(MusicGenre);
                        break;
                    case "Sports":
                        CurrCat = "sportz";

                        System.out.println("Sports");
                        parseJSON2(sports);
                        updateSpinner2(SportsGenre);
                        break;
                    case "Family":
                        CurrCat = "family";
                        System.out.println("Family");
                        parseJSON2(family);
                        break;
                    case "Film":
                        System.out.println("Film");
                        parseJSON2(film);
                        updateSpinner2(Film);
                        break;
                    case "Misc":
                        System.out.println("Misc");
                        parseJSON2(misc);
                        break;
                    case "Arts & Theater":
                        System.out.println("Saturday");
                        parseJSON2(artNThr);
                        updateSpinner2(ArtsNThtre);

                        break;
                    default:
                        parseJSON2(misc);

                }
                GenresList.clear();
            }
        });
    }

    public void updateSpinner2(String[] options) {
        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, options);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(aa);
        aa.notifyDataSetChanged();

    }

    //update edit text acts as Intent get EXTRA but for fragments, Here we are getting the results from filters passed from MainActivity.
    public void updateEditText(CharSequence category, CharSequence KeyWord, CharSequence cityName) {
        // CategoryText= (String) category;
        keyword2 = (String) KeyWord;
        music = "https://app.ticketmaster.com/discovery/v2/events?keyword=&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=music&city=" + cityName;
        sports = "https://app.ticketmaster.com/discovery/v2/events?&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=sports&city=" + cityName;
        family = "https://app.ticketmaster.com/discovery/v2/events?" + keyword2 + "&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&city=" + cityName;
        film = "https://app.ticketmaster.com/discovery/v2/events?" + keyword2 + "&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=Film&city=" + cityName;
        misc = "https://app.ticketmaster.com/discovery/v2/events?" + keyword2 + "&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&city=" + cityName;
        artNThr = "https://app.ticketmaster.com/discovery/v2/events?" + keyword2 + "&apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&keyword=Arts%20&%20Theater&locale=*&city=" + cityName;
    }

    public void updateCityDateKeywordText(CharSequence City, LocalDate date, CharSequence Keyword) {

    }



    public void MySearchQuery(String SEARCH_KEY_WORD, String START_DATE, String END_DATE, String CITYNAME){
        searchFilerURL = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&keyword="+SEARCH_KEY_WORD+"&locale=*&startDateTime="+START_DATE+"T16:27:00Z&endDateTime="+END_DATE+"T16:27:00Z&city="+CITYNAME;
        mEventList.clear();
        parseJSON2(searchFilerURL);
    }




    private String[] GetGenres(String url) {

        Toast.makeText(getContext(), url, Toast.LENGTH_LONG).show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener < JSONObject > () {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            GenresList.clear();
                            JSONArray jsonArray = response.getJSONObject("_embedded").getJSONArray("genres");

                            Log.d("Main Activity", "onResponseSuccess");
                            String eventImage = "";

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                String id = hit.getString("id");
                                String GenreName = hit.getString("name");
                                GenresList.add(GenreName);
                                Genresid.put(GenreName, id);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "onResponse Failure :" + e);
                            e.printStackTrace();
                        }
                        Log.i(TAG, "Total Retrieved GenresList :" + GenresList);
                        Log.i(TAG, "Total Retrieved Genresid HashMap :" + Genresid);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueueSingleton.getInstance(getActivity().getBaseContext()).addToRequestQueue(request);
        String[] strarray = GenresList.toArray(new String[0]);
        return strarray;
    }

    private void parseJSON2(String url) {
        Log.i(TAG, url);
        //Toast.makeText(getContext(), url, Toast.LENGTH_LONG).show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener < JSONObject > () {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONObject("_embedded").getJSONArray("events");

                            Log.d("Main Activity", "onResponseSuccess");
                            String eventImage = "";
                            String venueName = "";
                            String GenreName = "";
                            String id = "";
                            String venueCity="";
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);
                                String info = jsonArray.getJSONObject(i).isNull("info") ? null : jsonArray.getJSONObject(i).getString("info");
                                String eventid = hit.getString("id");
                                String eventName = hit.getString("name");
                                String date = hit.getJSONObject("dates").getJSONObject("start").getString("localDate");
                                String time = hit.getJSONObject("dates").getJSONObject("start").getString("localTime");
                                JSONObject _embedded2 = jsonArray.getJSONObject(i).isNull("_embedded") ? null : jsonArray.getJSONObject(i).getJSONObject("_embedded");
                                //JSONObject _embedded2 = hit.getJSONObject("_embedded");

                                JSONArray venuesArray = _embedded2.getJSONArray("venues"); //GET VENUES
                                for (int k = 0; k < venuesArray.length(); k++) {
                                    JSONObject elem = venuesArray.getJSONObject(k);

                                    venueName = elem.getString("name"); //gets the image url
                                    venueCity = elem.getString("city");
                                }
                                
                                JSONArray imagesArray = hit.getJSONArray("images"); //GET IMAGES

                                for (int j = 0; j < imagesArray.length(); j++) {
                                    JSONObject elem = imagesArray.getJSONObject(j);

                                    eventImage = elem.getString("url"); //gets the image url
                                }

                                // Genresid.put(GenreName,id);
                                mEventList.add(new EventItem(eventImage, eventName, date));
                                mDetailList.add(new EventDetail(info, eventid, venueName, time, venueCity));

                            }
                            eventAdapter = new EventAdapter(getActivity().getBaseContext(), mEventList);
                            mRecyclerView.setAdapter(eventAdapter);
                            eventAdapter.setOnItemClickListener(StreamFragment.this);

                        } catch (JSONException e) {
                            Log.e(TAG, "onResponse Failure :" + e);
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

        Intent detailIntent = new Intent(getActivity().getBaseContext(), EventDetailActivity.class);
        EventItem clickedItem = mEventList.get(position);
        EventDetail clickedItem1 = mDetailList.get(position);
        // JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, ur)
        detailIntent.putExtra(EXTRA_VENUE_CITY,clickedItem1.getVenueCity());
        detailIntent.putExtra(EXTRA_EVENT_START_TIME, clickedItem1.getStartTime());
        detailIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
        detailIntent.putExtra(EXTRA_EVENT_NAME, clickedItem.getCreator());
        detailIntent.putExtra(EXTRA_EVENT_DATE, clickedItem.getDate());
        detailIntent.putExtra(EXTRA_EVENT_ID, clickedItem1.getId());
        detailIntent.putExtra(EXTRA_EVENT_TYPE, clickedItem1.getInfo());
        detailIntent.putExtra(EXTRA_EVENT_VENUE_NAME, clickedItem1.getVenueName());

        getActivity().startActivity(detailIntent);
    }

    @Override
    public void initialize() {
        Log.i(TAG, "INITIALIZE CALLED");

    }
}