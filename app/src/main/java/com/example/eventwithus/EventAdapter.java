package com.example.eventwithus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventwithus.models.EventHelper;
import com.example.eventwithus.models.EventItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ExampleViewHolder> {
    private final Context mContext;
    private ArrayList<EventItem> eventItems;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }
    public EventAdapter(Context context, ArrayList<EventItem> exampleList) {
        mContext = context;
        eventItems = exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.example_item, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        EventItem currentItem = eventItems.get(position);

        String imageUrl = currentItem.getImageUrl();
        String evenName = currentItem.getCreator();
        String eventDate = EventHelper.formatJsonDate(currentItem.getDate());

        holder.mEventName.setText(evenName);
        holder.mEventDesc.setText(eventDate);
        Picasso.with(mContext).load(imageUrl).fit().centerInside().transform(new RoundedTransformation(50, 0)).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return eventItems.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mEventName;
        public TextView mEventDesc;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            mEventName = itemView.findViewById(R.id.text_view_name);
            mEventDesc = itemView.findViewById(R.id.text_view_desc);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);

                }

            });
        }
    }
}