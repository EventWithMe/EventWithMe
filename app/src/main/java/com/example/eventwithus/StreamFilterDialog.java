package com.example.eventwithus;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

// where ever you want to show the fragment use this code:
    /*StreamFilterDialog streamFilterDialog = new StreamFilterDialog(context, other data...)
    streamFilterDialog.getWindow().setBackgroundDrawableResource(R.drawable.layout_border);
    streamFilterDialog.show();*/


public class StreamFilterDialog extends Dialog {

    // put xml variables here

    public StreamFilterDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_profileview);

        // initialize xml variables


        // set on click listeners for handling filters
    }
}
