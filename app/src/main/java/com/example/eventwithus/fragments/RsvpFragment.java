package com.example.eventwithus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventwithus.R;
import com.example.eventwithus.adapters.MyEventAdapter;
import com.example.eventwithus.models.EventHelper;
import com.parse.ParseUser;

import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class RsvpFragment extends Fragment {

    public static final String TAG = "RsvpFragment";
    public static final String PARSE_RSVP_KEY= "eventsinfo"; // key to get and update the eventinfo column in the User object in Parse DB

    private RecyclerView recyclerView;
    private MyEventAdapter myEventAdapter;
    private List<String> eventsList;
    private ParseUser currentUser;

    public RsvpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        recyclerView = view.findViewById(R.id.rvMyEventsList);
        recyclerView.setHasFixedSize(true);
        EventHelper.refreshUserData();
        currentUser = ParseUser.getCurrentUser();

        eventsList = EventHelper.getLoggedInUserEvents(PARSE_RSVP_KEY);
        eventsList.remove(0);
        myEventAdapter = new MyEventAdapter(getContext(), eventsList);

        recyclerView.setAdapter(myEventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}