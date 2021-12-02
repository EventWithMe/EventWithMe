package com.example.eventwithus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseUser;

// TODO: 12/2/2021 write changes to the DB below

public class EditProfileActivity extends AppCompatActivity {

    public static final String EMAIL_KEY= "email";
    public static final String FIRSTNAME_KEY= "firstname";
    public static final String LASTNAME_KEY= "lastname";
    public static final String IMAGE_KEY= "image";

    ImageView ivPfpE;
    EditText etFirstNamePE;
    EditText etLastNamePE;
    EditText etEmailPE;
    Button btnSaveProfile;


    private ParseUser currentUser;

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

        etFirstNamePE.setText(currentUser.getString(FIRSTNAME_KEY));
        etLastNamePE.setText(currentUser.getString(LASTNAME_KEY));
        etEmailPE.setText(currentUser.getString(EMAIL_KEY));

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
}