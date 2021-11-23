package com.example.eventwithus.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventwithus.R;
import com.example.eventwithus.models.Event;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class MyEventAdapter extends RecyclerView.Adapter<MyEventAdapter.ViewHolder> {

    Context context;
    List<Event> events;

    public MyEventAdapter(Context context, List<Event> events){
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_card_with_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear(){
        events.clear();
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addAll(List<Event> eventList){
        events.addAll(eventList);
        notifyDataSetChanged();
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        ImageView eventIV;
        TextView card_NameTV;
        TextView card_dateTV;
        TextView card_timeTV;
        TextView card_venueTV;
        TextView card_descTV;
        ImageButton chat_button;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            eventIV = itemView.findViewById(R.id.eventIV);
            card_NameTV = itemView.findViewById(R.id.card_NameTV);
            card_dateTV = itemView.findViewById(R.id.card_dateTV);
            card_timeTV = itemView.findViewById(R.id.card_timeTV);
            card_venueTV = itemView.findViewById(R.id.card_venueTV);
            card_descTV = itemView.findViewById(R.id.card_descTV);
            chat_button = itemView.findViewById(R.id.chat_button);

        }

        @SuppressLint("SetTextI18n")
        public void bind(Event event) {

            card_NameTV.setText(event.getEventName());
            card_dateTV.setText(String.valueOf(event.getDate()));
            card_timeTV.setText(event.getStartTime());
            card_venueTV.setText(event.getVenueName());
            card_descTV.setText(event.getDescription());
            // TODO get event picture
            //Glide.with(context)

            // TODO connect chat button
            chat_button.setOnClickListener(v -> {

            });
        }
    }
}
