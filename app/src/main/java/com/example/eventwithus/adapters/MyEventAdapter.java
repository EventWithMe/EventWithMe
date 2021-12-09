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

import com.example.eventwithus.ChatActivity;
import com.example.eventwithus.EventDetailActivity;
import com.example.eventwithus.MyEventDetailActivity;
import com.example.eventwithus.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyEventAdapter extends RecyclerView.Adapter<MyEventAdapter.ViewHolder>{

    final Context context;
    List<String> events;

    public MyEventAdapter(Context context, List<String> events){
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_rsvp, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String event = events.get(position);

        /*holder.btnChatRsvp.setOnClickListener(view -> {
                // TODO connect chat with chat

        });*/

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
        private  ImageView ivRsvpImage;
        private  TextView tvEventNameRSVP;
        private  TextView tvDateRSVP;
        private  TextView card_timeTV;
        private  TextView card_venueTV;
        private  TextView card_cityTV;
        private  Button btnChatRsvp;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ivRsvpImage = itemView.findViewById(R.id.image_view);
            tvEventNameRSVP = itemView.findViewById(R.id.text_view_name);
            tvDateRSVP = itemView.findViewById(R.id.text_view_desc);
//            card_timeTV = itemView.findViewById(R.id.card_timeTV);
//            card_venueTV = itemView.findViewById(R.id.card_venueTV);
//            card_cityTV = itemView.findViewById(R.id.card_cityTV);
            btnChatRsvp = itemView.findViewById(R.id.btnChatRsvp);

            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            //get position of movie clicked
            /*int position = getAdapterPosition();
            //get movie at the position
            String event = events.get(position);
            //create new intent
            Intent intent = new Intent(context, MyEventDetailActivity.class);
            //pass data
            intent.putExtra("ivPfp", event.getImageURL());
            intent.putExtra("nameTV", event.getEventName());
            intent.putExtra("cityTV", event.getCity());
            intent.putExtra("timeTV", event.getStartTime());
            intent.putExtra("venueTV", event.getVenueName());
            intent.putExtra("descriptionTV", event.getEventId());
            intent.putExtra("dateTV", event.getDate());
            intent.putExtra("eventLink", event.getEventLink());

            //show the activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);*/
        }

        public void bind(String event) {
            String[] formatted = event.split(";");
            tvEventNameRSVP.setText(formatted[3]);
            tvDateRSVP.setText(formatted[1]);
//            card_timeTV.setText("Time: " + event.getStartTime());
//            card_venueTV.setText("Venue: " + event.getVenueName());
//            card_cityTV.setText("City: " + event.getCity());
                Picasso.with(context).load(formatted[5]).fit().centerInside().into(ivRsvpImage);

                btnChatRsvp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ChatActivity.class);
                        context.startActivity(intent);
                    }
                });
        }
    }
}
