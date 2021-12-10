package com.example.eventwithus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventwithus.adapters.ChatAdapter;
import com.example.eventwithus.models.Message;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    EditText etMessage;
    ImageButton ibSend;
    RecyclerView rvChat;

    String eventId;
    static final String TAG = "ChatActivity";
    private ArrayList<Message> mMessages;
    private boolean mFirstLoad;
    private ChatAdapter mAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        String websocketUrl = "wss://eventwithme.b4a.io/";
        context = this.getApplicationContext();

        eventId = getIntent().getStringExtra("eventId");
        Log.i(TAG, String.format("Chat opened with eventId=%s", eventId));

        try {
            ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(websocketUrl));
            ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);
            parseQuery.whereNotEqualTo(Message.USER_ID_KEY, ParseUser.getCurrentUser().getObjectId());
            parseQuery.whereEqualTo(Message.EVENT_ID_KEY, eventId);

            // Connect to Parse server
            SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

            // Listen for CREATE events on the Message class
            subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, object) -> {
                mMessages.add(0, object);
                Log.i(TAG, String.format("New message: %s", object.getBody()));
                // RecyclerView updates need to be run on the UI thread
                runOnUiThread(() -> {
                    mAdapter.notifyItemInserted(0);
                    rvChat.scrollToPosition(0);
                });
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        etMessage = findViewById(R.id.etMessage);
        ibSend = findViewById(R.id.ibSend);
        rvChat = findViewById(R.id.rvChat);
        mMessages = new ArrayList<>();
        mFirstLoad = true;
        final String userId = ParseUser.getCurrentUser().getObjectId();
        mAdapter = new ChatAdapter(this, userId, mMessages);
        rvChat.setAdapter(mAdapter);

        // associate the LayoutManager with the RecyclerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setReverseLayout(true);
        rvChat.setLayoutManager(linearLayoutManager);

        // refresh to make currently posted messages appear
        refreshMessages();

        // When send button is clicked, create message object on Parse
        ibSend.setOnClickListener(v -> {
            String data = etMessage.getText().toString();
            if (data.isEmpty()) {
                Log.d(TAG, "Attempted to send empty message");
                return;
            }
            Message message = new Message();
            message.setUserId(ParseUser.getCurrentUser().getObjectId());
            message.setBody(data);
            message.setEventId(eventId);
            message.setTimestamp(new Date(System.currentTimeMillis()));
            message.saveInBackground(e -> {
                Log.i(TAG, "Successfully created message on Parse");
                refreshMessages();
            });
            etMessage.setText(null);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    void refreshMessages() {
        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        // Set query to only retrieve messages from current event
        query.whereEqualTo(Message.EVENT_ID_KEY, eventId);
        // Configure limit and sort order
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);

        // get the latest 50 messages, order will show up newest to oldest of this group
        query.orderByDescending("createdAt");
        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        query.findInBackground((messages, e) -> {
            if (e == null) {
                Log.i(TAG, String.format("Found %d messages", messages.size()));
                mMessages.clear();
                mMessages.addAll(messages);
                mAdapter.notifyDataSetChanged(); // update adapter
                // Scroll to the bottom of the list on initial load
                if (mFirstLoad) {
                    rvChat.scrollToPosition(0);
                    mFirstLoad = false;
                }
            } else {
                Log.e("message", "Error Loading Messages" + e);
            }
        });
    }
}