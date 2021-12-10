package com.example.eventwithus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventwithus.R;
import com.example.eventwithus.adapters.MyEventAdapter;
import com.example.eventwithus.models.EventHelper;
import com.google.android.material.snackbar.Snackbar;
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

        // on below line we are creating a method to create item touch helper
        // method for adding swipe to delete functionality.
        // in this we are specifying drag direction and position to right
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // this method is called
                // when the item is moved.
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                String event = eventsList.get(viewHolder.getAdapterPosition());
                String[] formatted = event.split(";");

                // below line is to get the position
                // of the item at that position.
                int position = viewHolder.getAdapterPosition();

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                // TODO: 12/9/2021 eventhelper remove this event from DB
                eventsList.remove(viewHolder.getAdapterPosition());

                // below line is to notify our item is removed from adapter.
                myEventAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                // below line is to display our snackbar with action.
                Snackbar.make(recyclerView, formatted[3], Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        eventsList.add(position, event);

                        // below line is to notify item is
                        // added to our adapter class.
                        myEventAdapter.notifyItemInserted(position);
                    }
                }).show();
            }
            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(recyclerView);
    }
}