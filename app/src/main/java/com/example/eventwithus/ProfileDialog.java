package com.example.eventwithus;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.ParseFile;

public class ProfileDialog extends Dialog {

    ImageView iv_dialogPfp;
    TextView tv_dialogName;
    TextView tv_dialogCity;

    Context context;
    String name;
    String city;
    ParseFile imageFile;

    public ProfileDialog(@NonNull Context context, String name, String city, ParseFile imageFile) {
        super(context);
        this.context = context;
        this.name = name;
        this.city = city;
        this.imageFile = imageFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profile_dialog);

        iv_dialogPfp = findViewById(R.id.iv_dialogPfp);
        tv_dialogName = findViewById(R.id.tv_dialogName);
        tv_dialogCity = findViewById(R.id.tv_dialogCity);

        if(imageFile != null) {
            Glide.with(context).load(imageFile.getUrl()).transform(new RoundedCorners(50)).into(iv_dialogPfp);
        } else {
            iv_dialogPfp.setImageResource(R.drawable.ic_baseline_person_24);
        }
        tv_dialogName.setText(name);
        tv_dialogCity.setText(city);
    }
}
