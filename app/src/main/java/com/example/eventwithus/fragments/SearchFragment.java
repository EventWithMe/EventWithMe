package com.example.eventwithus.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eventwithus.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment  {
    private FragmentSearchListener listener;

    public interface  FragmentSearchListener{
        void onInputSearchSent(CharSequence input, CharSequence keyword, CharSequence city);
    }

    //TODO implement Dates for Search Filter
    private BottomNavigationView bottomNavigationView;
    private static final String TEXT = "text";
    private TextSwitcher textSwitcher, textSwitcher2;
    private int stringIndex = 0;
    private String[] row = { "Concerts", "Sports", "Arts & Theater", "Family", "Film", "Misc"};
    private TextView textView;
    private static final String key = "UserInput";
    Button leftBTN;
    ImageButton searchBTN;
    Spinner spinner;
    EditText keywordET, cityET;
    SearchView searchView;
    String music = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=music";
    String sports = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=sports";
    String family = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&keyword=family&locale=*";
    String film = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*&segmentName=Film";
    String misc = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&locale=*";
    String artNThr = "https://app.ticketmaster.com/discovery/v2/events?apikey=kdQ1Zu3hN6RX9HbrUlAlMIGppB2faLMB&keyword=Arts%20&%20Theater&locale=*";





    private String mParam1;


    public SearchFragment() {
        // Required empty public constructor
    }



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
    //                          0           1           2               3       4       5
//private String[] row = { "Concerts", "Sports", "Arts & Theater", "Family", "Film", "Misc"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CharSequence input;

        View v = inflater.inflate(R.layout.fragment_search, container, false);
       // searchView = v.findViewById(R.id.searchView);
        leftBTN = v.findViewById(R.id.leftBTN);
        searchBTN = v.findViewById(R.id.searchBTN);
        keywordET = v.findViewById(R.id.keywordET);
        cityET = v.findViewById(R.id.cityET);
        textSwitcher = v.findViewById(R.id.textSwitcher);
        textSwitcher2 = v.findViewById(R.id.textSwitcher2);
        spinner = v.findViewById(R.id.spinner);


        ArrayAdapter aa = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,row);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(aa);

        bottomNavigationView = v.findViewById(R.id.bottom_navigation);
        searchBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                TextView tv = (TextView) textSwitcher.getCurrentView();
                CharSequence keyword = keywordET.getText().toString();
                CharSequence input = tv.getText().toString();
                CharSequence city = cityET.getText().toString();
                listener.onInputSearchSent(input, keyword, city);
            }

        });


        leftBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stringIndex == row.length-1){
                    stringIndex = 0;
                    textSwitcher.setText(row[stringIndex]);
                }else{
                    textSwitcher.setText(row[++stringIndex]);
                }
               // TextView tv = (TextView) textSwitcher.getCurrentView();

                   // CharSequence input = tv.getText().toString();
                   // listener.onInputSearchSent(input, keyword);




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


        textSwitcher2.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                textView = new TextView(getContext());
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(30);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                return textView;
            }
        });
        textSwitcher2.setText(row[stringIndex]);



        class OnSwipeTouchListener implements OnTouchListener {

            private final GestureDetector gestureDetector;

            public OnSwipeTouchListener (Context ctx){
                gestureDetector = new GestureDetector(ctx, new GestureListener());
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }

           final class GestureListener extends SimpleOnGestureListener {

                private static final int SWIPE_THRESHOLD = 100;
                private static final int SWIPE_VELOCITY_THRESHOLD = 100;

                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    boolean result = false;
                    try {
                        float diffY = e2.getY() - e1.getY();
                        float diffX = e2.getX() - e1.getX();
                        if (Math.abs(diffX) > Math.abs(diffY)) {
                            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                                if (diffX > 0) {
                                    onSwipeRight();
                                } else {
                                    onSwipeLeft();
                                }
                                result = true;
                            }
                        }
                        else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffY > 0) {
                                onSwipeBottom();
                            } else {
                                onSwipeTop();
                            }
                            result = true;
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    return result;
                }
            }

            public void onSwipeRight() {
            }

            public void onSwipeLeft() {
            }

            public void onSwipeTop() {
            }

            public void onSwipeBottom() {
            }
        }




       textSwitcher2.setOnTouchListener(new OnSwipeTouchListener(getContext()){
           public void onSwipeTop() {
               Toast.makeText(getContext(), "top", Toast.LENGTH_SHORT).show();
           }
           public void onSwipeRight() {
               textSwitcher2.setInAnimation(AnimationUtils.loadAnimation(getContext(),
                       R.anim.slide_in_left));
               textSwitcher2.setOutAnimation(AnimationUtils.loadAnimation(getContext(),
                       R.anim.slide_out_right));
               Toast.makeText(getContext(), "right", Toast.LENGTH_SHORT).show();
               if(stringIndex == row.length-1){
                   stringIndex = 0;
                   textSwitcher2.setText(row[stringIndex]);
               }else{
                   textSwitcher2.setText(row[++stringIndex]);
               }
           }
           public void onSwipeLeft() {
               textSwitcher2.setInAnimation(AnimationUtils.loadAnimation(getContext(),
                       R.anim.slide_in_right));
               textSwitcher2.setOutAnimation(AnimationUtils.loadAnimation(getContext(),
                       R.anim.slide_out_left));
               Toast.makeText(getContext(), "left", Toast.LENGTH_SHORT).show();
               if(stringIndex == 0){
                   stringIndex = row.length-1;
                   textSwitcher2.setText(row[stringIndex]);
               }else{
                   textSwitcher2.setText(row[--stringIndex]);
               }
               TextView tv = (TextView) textSwitcher2.getCurrentView();

           }
           public void onSwipeBottom() {
               Toast.makeText(getContext(), "bottom", Toast.LENGTH_SHORT).show();
           }


       });




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



    }




















    }


