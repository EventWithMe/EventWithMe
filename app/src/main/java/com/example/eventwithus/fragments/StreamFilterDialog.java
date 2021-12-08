package com.example.eventwithus.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.eventwithus.R;

import java.util.Calendar;

// where ever you want to show the fragment use this code:
    /*StreamFilterDialog streamFilterDialog = new StreamFilterDialog(context, other data...)
    streamFilterDialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_border);
    streamFilterDialog.show();*/


public class StreamFilterDialog extends Dialog  {

    Button btnGet;
    TextView tvw;
    EditText editTextStartDate;
    EditText editTextEndDate;
    DatePickerDialog picker;
    EditText editTextSearch;
    EditText editTextCityName;


    public StreamFilterDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_stream_filter);
        editTextEndDate = findViewById(R.id.editTextEnd);
       editTextStartDate = findViewById(R.id.editTextStart);
       editTextCityName = findViewById(R.id.editTextCityName);
       editTextSearch = findViewById(R.id.editTextSearch);
       btnGet = findViewById(R.id.button1);
       tvw = findViewById(R.id.textViewDisplayStart);


     //  editText1.setInputType(InputType.TYPE_NULL);

       editTextStartDate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final Calendar cldr = Calendar.getInstance();
               int day = cldr.get(Calendar.DAY_OF_MONTH);
               int month = cldr.get(Calendar.MONTH);
               int year = cldr.get(Calendar.YEAR);
               // date picker dialog
               picker = new DatePickerDialog(getContext(),
                       new DatePickerDialog.OnDateSetListener() {
                           @Override
                           public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                               editTextStartDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                           }
                       }, year, month, day);
               picker.show();
           }
       });
        editTextEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                editTextEndDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        btnGet=findViewById(R.id.button1);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvw.setText("Selected Date: "+ editTextStartDate.getText());
            }
        });


        // set on click listeners for handling filters
    }
}
