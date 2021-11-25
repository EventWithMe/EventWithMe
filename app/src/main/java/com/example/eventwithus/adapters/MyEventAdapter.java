package com.example.eventwithus.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventwithus.MyEventDetailActivity;
import com.example.eventwithus.R;
import com.example.eventwithus.models.MyEvents;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyEventAdapter extends RecyclerView.Adapter<MyEventAdapter.ViewHolder>{

    Context context;
    List<MyEvents> events;

    public MyEventAdapter(Context context, List<MyEvents> events){
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
        MyEvents event = events.get(position);

        holder.chat_button.setOnClickListener(view -> {
                // TODO connect chat with chat fragment
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.layout.fragment_rsvp,  new ChatFragment()); // give your fragment container id in first parameter
//                transaction.addToBackStack(null);
//                transaction.commit();
        });

        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private final ImageView eventIV;
        private final TextView card_NameTV;
        private final TextView card_dateTV;
        private final TextView card_timeTV;
        private final TextView card_venueTV;
        private final TextView card_cityTV;
        private final Button chat_button;

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
            card_cityTV = itemView.findViewById(R.id.card_cityTV);
            chat_button = itemView.findViewById(R.id.chat_button);

            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            //get position of movie clicked
            int position = getAdapterPosition();
            //get movie at the position
            MyEvents event = events.get(position);
            //create new intent
            Intent intent = new Intent(context, MyEventDetailActivity.class);
            //pass data
            intent.putExtra("imageView", events.get(position).getImageURL());
            intent.putExtra("nameTV", events.get(position).getEventName());
            intent.putExtra("cityTV", events.get(position).getCity());
            intent.putExtra("timeTV", events.get(position).getStartTime());
            intent.putExtra("venueTV", events.get(position).getVenueName());
            intent.putExtra("descriptionTV", events.get(position).getEventId());
            intent.putExtra("dateTV", events.get(position).getDate());
            intent.putExtra("eventLink", events.get(position).getEventLink());

            //show the activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        @SuppressLint("SetTextI18n")
        public void bind(MyEvents event) {
            card_NameTV.setText(event.getEventName());
            card_dateTV.setText("Date: " + event.getDate());
            card_timeTV.setText("Time: " + event.getStartTime());
            card_venueTV.setText("Venue: " + event.getVenueName());
            card_cityTV.setText("City: " + event.getCity());
            if(event.getImageURL() != null){
                Picasso.with(context).load(event.getImageURL()).fit().centerInside().into(eventIV);
            }
        }
    }
}
