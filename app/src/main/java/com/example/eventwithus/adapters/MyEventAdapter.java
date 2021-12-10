package com.example.eventwithus.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventwithus.ChatActivity;
import com.example.eventwithus.R;
import com.example.eventwithus.models.EventHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
        View view = LayoutInflater.from(context).inflate(R.layout.card_favorites2, parent, false);
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
        private ImageView ivImage_f2;
        private TextView tvName_f2;
        private TextView tvDate_f2;
        private TextView tvTime_f2;
        private TextView tvVenue_f2;
        private TextView tvCity_f2;
        private CardView favorites_card2;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ivImage_f2 = itemView.findViewById(R.id.ivImage_f2);
            tvName_f2 = itemView.findViewById(R.id.tvName_f2);
            tvDate_f2 = itemView.findViewById(R.id.tvDate_f2);
            tvTime_f2 = itemView.findViewById(R.id.tvTime_f2);
            tvVenue_f2 = itemView.findViewById(R.id.tvVenue_f2);
            tvCity_f2 = itemView.findViewById(R.id.tvCity_f2);
            favorites_card2 = itemView.findViewById(R.id.favorites_card2);

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
            tvName_f2.setText(formatted[3]);
            tvDate_f2.setText(EventHelper.formatJsonDate(formatted[1]));
            tvTime_f2.setText(EventHelper.startTimeFormatter(formatted[6]));
            tvVenue_f2.setText(formatted[4]);
            tvCity_f2.setText(formatted[7]);
            Picasso.with(context).load(formatted[5]).fit().centerInside().into(ivImage_f2);

            favorites_card2.setOnClickListener(view -> {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("eventId", formatted[0]);
                context.startActivity(intent);
            });
        }
    }
}
