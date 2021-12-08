package com.example.eventwithus.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eventwithus.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment  {
    private FragmentSearchListener listener;

    public interface  FragmentSearchListener{
        void onInputSearchSent(CharSequence category, CharSequence Genre, CharSequence city);
        void onCityDateKeywordSearchSent(CharSequence city, LocalDate Date, CharSequence KeyWord );
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
    private RadioGroup radioGroup;
    private TextView themeTV;
    DatePickerDialog picker;
    EditText eText;
    Button btnGet;
    TextView tvw;




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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        radioGroup = v.findViewById(R.id.idRGgroup);
        themeTV = v.findViewById(R.id.idtvTheme);

        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // on radio button check change
                switch (checkedId) {
                    case R.id.idRBLight:
                        // on below line we are checking the radio button with id.
                        // on below line we are setting the text to text view as light mode.
                        themeTV.setText("Light Theme");
                        // on below line we are changing the theme to light mode.
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        break;
                    case R.id.idRBDark:
                        // this method is called when dark radio button is selected
                        // on below line we are setting dark theme text to our text view.
                        themeTV.setText("Dark Theme");
                        // on below line we are changing the theme to dark mode.
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        break;
                }
            }
        });
    }




















    }


