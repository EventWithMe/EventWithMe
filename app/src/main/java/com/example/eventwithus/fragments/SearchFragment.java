package com.example.eventwithus.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventwithus.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment  {
    private FragmentSearchListener listener;

    public interface  FragmentSearchListener{
        void onInputSearchSent(CharSequence input);
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TEXT = "text";
    private TextSwitcher textSwitcher;
    private int stringIndex = 0;
    private String[] row = { "Concerts", "Sports", "Arts & Theater", "Family", "Film", "Misc"};
    private TextView textView;
    private static final String key = "UserInput";
    Button leftBTN;
    SearchView searchView;
    String music = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=music";
    String sports = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=sports";
    String family = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&keyword=family&locale=*";
    String film = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=Film";
    String misc = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*";
    String artNThr = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&keyword=Arts%20&%20Theater&locale=*";


    //

    // TODO: Rename and change types of parameters
    private String mParam1;


    public SearchFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String text) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(TEXT, text);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(TEXT);

        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
       // searchView = v.findViewById(R.id.searchView);
        leftBTN = v.findViewById(R.id.leftBTN);
        textSwitcher = v.findViewById(R.id.textSwitcher);
        leftBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stringIndex == row.length-1){
                    stringIndex = 0;
                    textSwitcher.setText(row[stringIndex]);
                }else{
                    textSwitcher.setText(row[++stringIndex]);
                }
               CharSequence input = textView.getText();
                listener.onInputSearchSent(input);

            }
        });
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                textView = new TextView(getContext());
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(30);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                return textView;
            }
        });
        textSwitcher.setText(row[stringIndex]);

        // Inflate the layout for this fragment
        return v;


    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentSearchListener) {
            listener = (FragmentSearchListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] Categories = { "Concerts", "Sports", "Arts & Theater", "Family", "Film", "Misc"};
        String[] Dates = { "All Dates", "This Weekend"};
        String[] Distance = { "10 mi", "25 mi", "50 mi","75 mi","All (mi)"};
        //Bundle bundle = new Bundle();
       // SteamFragment fragment = new StreamFragment();

        // Intent intent = new Intent(getActivity(), LocationService.class);
     //   startActivity(intent);
        Toast.makeText(getContext(), "toast", Toast.LENGTH_LONG).show();

       // GestureDetector gestureDetector;
       /** gestureDetector = new GestureDetector(new MyGestureDetector());
        View.OnTouchListener gestureListener = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };
        textView.setOnTouchListener(gestureListener);



**/
    }
    public void displaySearchView(){

    }









/**


    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        final String TAG = MyGestureDetector.class.getSimpleName();

        // for touch left or touch right events
        private static final int SWIPE_MIN_DISTANCE = 80;   //default is 120
        private static final int SWIPE_MAX_OFF_PATH = 400;
        private static final int SWIPE_THRESHOLD_VELOCITY = 70;

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(TAG, " on filing event, first velocityX :" + velocityX +
                    " second velocityY" + velocityY);
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                if(e1.getX() - e2.getX()
                        > SWIPE_MIN_DISTANCE && Math.abs(velocityX)
                        > SWIPE_THRESHOLD_VELOCITY) {
                    onHorizonTouch(true);  // left
                }  else if (e2.getX() - e1.getX()
                        > SWIPE_MIN_DISTANCE && Math.abs(velocityX)
                        > SWIPE_THRESHOLD_VELOCITY) {
                    onHorizonTouch(false); // right
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        void onHorizonTouch(Boolean toLeft) {

            if(!toLeft ) {
                stringIndex = 0;
                textSwitcher.setInAnimation(AnimationUtils.loadAnimation(
                        getContext(), android.R.anim.fade_in));
                textSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
                        getContext(), android.R.anim.fade_out));

                textSwitcher.setText(row[++stringIndex]);
               //textView.setText("Text1");
            }
            if(toLeft) {
                textSwitcher.setInAnimation(AnimationUtils.loadAnimation(
                        getContext(), android.R.anim.fade_in));
                textSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
                        getContext(), android.R.anim.fade_out));

                textSwitcher.setText(row[--stringIndex]);
            }
        }
    }
**/





    }


