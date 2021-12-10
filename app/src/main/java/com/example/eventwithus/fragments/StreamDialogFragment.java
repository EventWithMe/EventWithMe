package com.example.eventwithus.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.eventwithus.R;

import java.text.DecimalFormat;
import java.util.Calendar;

public class StreamDialogFragment extends DialogFragment {

    private static final String TAG = "MyCustomDialog";

    public interface OnInputSelected{
        void sendInput(String input, String start, String end, String city);
    }
    public OnInputSelected mOnInputSelected;

    //widgets
    private EditText mInput;
    private TextView  mActionCancel;
    Button btnGet;
    TextView tvw;
    EditText editTextStartDate;
    EditText editTextEndDate;
    DatePickerDialog picker;
    EditText editTextSearch;
    EditText editTextCityName;
    @Override
    public void onStart() {
        super.onStart();
        //Reference to dimens.xml
        //<dimen name="dialog_with">200dp</dimen>
        //<dimen name="dialog_height">200dp</dimen>
        int dialogWidth = getResources().getDimensionPixelSize(R.dimen.dialog_width);
        int dialogHeight = getResources().getDimensionPixelSize(R.dimen.dialog_height);
        if (getDialog().getWindow() == null) return;
        getDialog().getWindow().setDimAmount(0.7f);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_stream_filter, container, false);
       // mActionOk = view.findViewById(R.id.action_ok);
        //mActionCancel = view.findViewById(R.id.action_cancel);
       // mInput = view.findViewById(R.id.input);

        editTextEndDate = view.findViewById(R.id.editTextEnd);
        editTextStartDate = view.findViewById(R.id.editTextStart);
        editTextCityName = view.findViewById(R.id.editTextCityName);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        btnGet = view.findViewById(R.id.button1);
        tvw = view.findViewById(R.id.textViewDisplayStart);
/**
        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
            }
        });


**/
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: capturing input.");
                String searchInput = editTextSearch.getText().toString();
                String startDateInput = editTextStartDate.getText().toString();
                String endDateInput = editTextEndDate.getText().toString();
                String cityName = editTextCityName.getText().toString();
                //String input = mInput.getText().toString();
                if(!searchInput.equals("")){
//
//                    //Easiest way: just set the value.
//                    MainFragment fragment = (MainFragment) getActivity().getFragmentManager().findFragmentByTag("MainFragment");
//                    fragment.mInputDisplay.setText(input);

                    mOnInputSelected.sendInput(searchInput, startDateInput, endDateInput, cityName);
                }


                getDialog().dismiss();
            }
        });



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
                                DecimalFormat mFormat= new DecimalFormat("00");
                                editTextStartDate.setText(year + "-" + (mFormat.format(Double.valueOf(monthOfYear + 1))) + "-" + mFormat.format(Double.valueOf(dayOfMonth)));
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
                                DecimalFormat mFormat= new DecimalFormat("00");
                                editTextEndDate.setText(year + "-" + (mFormat.format(Double.valueOf(monthOfYear + 1))) + "-" + mFormat.format(Double.valueOf(dayOfMonth)));
                            }
                        }, year, month, day);
                picker.show();
            }
        });




        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputSelected = (OnInputSelected) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage() );
        }
    }


}
