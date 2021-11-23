package com.example.eventwithus.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventwithus.EventsAdapter;
import com.example.eventwithus.R;
import com.example.eventwithus.models.Event;

import java.util.ArrayList;
import java.util.List;


public class StreamFragment extends Fragment {

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

        rvEvents = view.findViewById(R.id.rvStreamList);
        allEvents = new ArrayList<>();
        adapter = new EventsAdapter(getContext(), allEvents);
        rvEvents.setAdapter(adapter);
        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    protected void populateEvents() {
        // TODO: 11/21/2021 method will get data from the ticketmaster API & store it in the events model
    }

}