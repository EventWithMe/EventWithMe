package com.example.eventwithus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

// picking an image from the phone's storage https://stackoverflow.com/questions/5309190/android-pick-images-from-gallery
// TODO: 12/2/2021 write changes to the DB below
// TODO: 12/2/2021 make a little popup window to allow the user to select using their camera or gallery for photo
// TODO: 12/2/2021 write the code to get the gallery photo or take a photo

public class EditProfileActivity extends AppCompatActivity {

    public static final String TAG = "EditProfileActivity"; // tag for logging
    public static final String EMAIL_KEY= "email";
    public static final String FIRSTNAME_KEY= "firstname";
    public static final String LASTNAME_KEY= "lastname";
    public static final String IMAGE_KEY= "image";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private static final int PICK_IMAGE = 39;

    ImageView ivPfpE;
    EditText etFirstNamePE;
    EditText etLastNamePE;
    EditText etEmailPE;
    Button btnSaveProfile;


    private ParseUser currentUser;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ivPfpE = findViewById(R.id.ivPfpE);
        etFirstNamePE = findViewById(R.id.etFirstNamePE);
        etLastNamePE = findViewById(R.id.etLastNamePE);
        etEmailPE = findViewById(R.id.etEmailPE);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        currentUser = ParseUser.getCurrentUser();
        context = getApplicationContext();

        etFirstNamePE.setText(currentUser.getString(FIRSTNAME_KEY));
        etLastNamePE.setText(currentUser.getString(LASTNAME_KEY));
        etEmailPE.setText(currentUser.getString(EMAIL_KEY));

        ivPfpE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);*/

                /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);*/

                showDialog();

            }
        });

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstName = etFirstNamePE.getText().toString();
                String lastName = etLastNamePE.getText().toString();
                String email = etEmailPE.getText().toString();

                // input validation on first name, last name, and email
                if(firstName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please input your first name it is required", Toast.LENGTH_SHORT).show();
                    return;
                } else if(lastName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please input your last name it is required", Toast.LENGTH_SHORT).show();
                    return;
                } else if(email.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please input your email it is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // TODO: 12/2/2021 write the changes to the DB
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "OnActivityResult Entered");

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }

    private void showDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.image_chooser_popup);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        Button btnClose = dialog.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}