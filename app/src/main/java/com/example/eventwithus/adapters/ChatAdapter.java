package com.example.eventwithus.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventwithus.ProfileDialog;
import com.example.eventwithus.R;
import com.example.eventwithus.models.Message;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private static final String TAG = "ChatAdapter";
    private final List<Message> mMessages;
    private final Context mContext;
    private final String mUserId;

    private static final int MESSAGE_OUTGOING = 123;
    private static final int MESSAGE_INCOMING = 321;

    public ChatAdapter(Context context, String userId, List<Message> messages) {
        mMessages = messages;
        this.mUserId = userId;
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isMe(position)) {
            return MESSAGE_OUTGOING;
        } else {
            return MESSAGE_INCOMING;
        }
    }

    private boolean isMe(int position) {
        Message message = mMessages.get(position);
        return message.getUserId() != null && message.getUserId().equals(mUserId);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == MESSAGE_INCOMING) {
            View contactView = inflater.inflate(R.layout.message_incoming, parent, false);
            return new IncomingMessageViewHolder(contactView);
        } else if (viewType == MESSAGE_OUTGOING) {
            View contactView = inflater.inflate(R.layout.message_outgoing, parent, false);
            return new OutgoingMessageViewHolder(contactView);
        } else {
            throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = mMessages.get(position);
        holder.bindMessage(message);
    }

    public abstract static class MessageViewHolder extends RecyclerView.ViewHolder {

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bindMessage(Message message);
    }

    public class IncomingMessageViewHolder extends MessageViewHolder {
        final ImageView imageOther;
        final TextView body;
        final TextView name;

        public IncomingMessageViewHolder(View itemView) {
            super(itemView);
            imageOther = itemView.findViewById(R.id.ivProfileOther);
            body = itemView.findViewById(R.id.tvBody);
            name = itemView.findViewById(R.id.tvName);
        }

        @Override
        public void bindMessage(Message message) {

            // Get user's name and pfp from database
            ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
            query.whereEqualTo("objectId", message.getUserId());
            query.findInBackground((objects, e) -> {
                if (e == null) {
                    String firstname = objects.get(0).getString("firstname");
                    String lastname = objects.get(0).getString("lastname");
                    String city = objects.get(0).getString("city");
                    name.setText(String.format("%s %s", firstname, lastname));

                    ParseFile image = objects.get(0).getParseFile("image");
                    if (image != null) {
                        Glide.with(mContext)
                                .load(image.getUrl())
                                .circleCrop()
                                .into(imageOther);
                    }
                    else {
                        Log.i(TAG, String.format("User %s seems to have no profile picture", message.getUserId()));
                    }

                    imageOther.setOnClickListener(view -> {
                        Toast.makeText(mContext, "clicked on user: " + firstname, Toast.LENGTH_SHORT).show();
                        profileDialog(firstname, lastname, city, image);
                    });
                }
                else {
                    Log.e(TAG, String.format("Error retrieving profile url for user %s", message.getUserId()), e);
                }

            });
            body.setText(message.getBody());
        }
    }

    private void profileDialog(String firstname, String lastname, String city, ParseFile parseFile) {
        ProfileDialog profileDialog = new ProfileDialog(mContext, firstname + " " + lastname, city, parseFile);
        profileDialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_border);
        profileDialog.show();
    }

    public class OutgoingMessageViewHolder extends MessageViewHolder {
        final ImageView imageMe;
        final TextView body;
        final TextView name;

        public OutgoingMessageViewHolder(View itemView) {
            super(itemView);
            imageMe = itemView.findViewById(R.id.ivProfileMe);
            body = itemView.findViewById(R.id.tvBody);
            name = itemView.findViewById(R.id.tvName);
        }

        @Override
        public void bindMessage(Message message) {
            String firstname = ParseUser.getCurrentUser().getString("firstname");
            String lastname = ParseUser.getCurrentUser().getString("lastname");
            ParseFile profile = ParseUser.getCurrentUser().getParseFile("image");

            if (profile != null) {
                Glide.with(mContext)
                        .load(profile.getUrl())
                        .circleCrop()
                        .into(imageMe);
            }
            else {
                Log.i(TAG, String.format("User %s seems to have no profile picture", message.getUserId()));
            }
            body.setText(message.getBody());
            name.setText(String.format("%s %s", firstname, lastname));
        }
    }
}