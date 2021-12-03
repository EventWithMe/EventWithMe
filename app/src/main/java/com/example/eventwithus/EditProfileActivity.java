package com.example.eventwithus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.eventwithus.models.EventHelper;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

// TODO: 12/2/2021 write changes to the DB below
// TODO: 12/2/2021 make a little popup window to allow the user to select using their camera or gallery for photo
// TODO: 12/2/2021 write the code to get the gallery photo or take a photo

public class EditProfileActivity extends AppCompatActivity {

    public static final String TAG = "EditProfileActivity"; // tag for logging
    public static final String EMAIL_KEY= "email";
    public static final String FIRSTNAME_KEY= "firstname";
    public static final String LASTNAME_KEY= "lastname";
    public static final String IMAGE_KEY= "image";
    public static final String PHOTO= "photo";
    public static final String GALLERY= "gallery";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private static final int PICK_IMAGE = 39;
    private static final int TAKE_PHOTO = 41;

    ImageView ivPfpE;
    EditText etFirstNamePE;
    EditText etLastNamePE;
    EditText etEmailPE;
    Button btnSaveProfile;


    private ParseUser currentUser;
    private File photoFile;
    private String selectedImagePath;
    private String filemanagerstring;
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

        loadProfilePic();

        ivPfpE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                // TODO: 12/2/2021 write the code to save this data
            }
        });
    }

    private void loadProfilePic() {
        ParseFile file = currentUser.getParseFile(IMAGE_KEY);
        if(file == null) {
            Log.i(TAG, "User has no pfp");
            return;
        }
        Glide.with(context).load(file.getUrl()).transform(new RoundedCorners(50)).into(ivPfpE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "OnActivityResult Entered");

        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();
                Picasso.with(context).load(selectedImageUri).into(ivPfpE);
            } else { // Result was a failure
                Toast.makeText(context, "System image could not be retrieved", Toast.LENGTH_SHORT).show();
            }
        } else if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                savePhoto(this.photoFile);
                currentUser.fetchInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        Glide.with(context).load(currentUser.getParseFile(IMAGE_KEY).getUrl()).into(ivPfpE);
                    }
                });
            } else { // Result was a failure
                Toast.makeText(context, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleImageChoice(String choice) {
        Log.i(TAG, "User chose " + choice);

        if(choice.equals(PHOTO)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            photoFile = getPhotoFileUri(photoFileName);
            Log.i(TAG, "getPhotoFileUri complete");
            Uri fileProvider = FileProvider.getUriForFile(context, "com.codepath.fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
            Log.i(TAG, "Before checking if user can handle intent");
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } else if(choice.equals(GALLERY)) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);
        }
    }

    private void showDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.image_chooser_popup);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        Button btnCamera = dialog.findViewById(R.id.btnCamera);
        Button btnGallery = dialog.findViewById(R.id.btnGallery);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleImageChoice(PHOTO);
                dialog.dismiss();

            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleImageChoice(GALLERY);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        Log.i(TAG, "getPhotoFileURI entered");
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void savePhoto(File photoFile) {

        ParseFile file = new ParseFile(photoFile);

        currentUser.put(IMAGE_KEY, file);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(context, "New profile pic saved", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });
    }
}