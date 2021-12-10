package com.example.eventwithus;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.eventwithus.fragments.ProfileFragment;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class EditProfileActivity extends AppCompatActivity {

    public static final String TAG = "EditProfileActivity"; // tag for logging
    public static final String FIRSTNAME_KEY= "firstname";
    public static final String LASTNAME_KEY= "lastname";
    public static final String EMAIL_KEY= "email";
    public static final String CITY_KEY = "city";
    public static final String IMAGE_KEY= "image";
    public static final String BIO_KEY= "bio";
    public static final String PHOTO= "photo";
    public static final String GALLERY= "gallery";
    private static final int MAX_PROFILE_WIDTH = 180;
    private static final int MAX_PROFILE_HEIGHT = 180;

    ImageView imageViewEditProfilePicture;
    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextEmail;
    EditText editTextCity;
    EditText editTextBio;
    Button btnSaveProfile;

    private ParseUser currentUser;
    private File profilePictureFile;
    private static final String photoFileName = "photo%d.jpg";
    private static int photoFileIndex = 0;
    private boolean pfpChange;
    Context context;

    final ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            this::cameraResultCallback
    );
    final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            this::galleryResultCallback
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        imageViewEditProfilePicture = findViewById(R.id.imageViewEditProfilePicture);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextCity = findViewById(R.id.editTextCity);
        editTextBio = findViewById(R.id.editTextBio);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        currentUser = ParseUser.getCurrentUser();
        context = getApplicationContext();
        pfpChange = false;

        editTextFirstName.setText(currentUser.getString(FIRSTNAME_KEY));
        editTextLastName.setText(currentUser.getString(LASTNAME_KEY));
        editTextEmail.setText(currentUser.getString(EMAIL_KEY));
        editTextCity.setText(currentUser.getString(CITY_KEY));
        editTextBio.setText(currentUser.getString(BIO_KEY));

        currentUser.fetchInBackground((object, e) -> loadProfilePic());

        imageViewEditProfilePicture.setOnClickListener(view -> handleImageChoice(PHOTO));

        btnSaveProfile.setOnClickListener(view -> {

            String firstName = editTextFirstName.getText().toString();
            String lastName = editTextLastName.getText().toString();
            String email = editTextEmail.getText().toString();
            String city = editTextCity.getText().toString();
            String bio = editTextBio.getText().toString();

            // input validation on first name, last name, and email
            if(firstName.isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.edit_profile_activity_toast_first_name_required),
                        Toast.LENGTH_SHORT).show();
                return;
            } else if(lastName.isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.edit_profile_activity_toast_last_name_required),
                        Toast.LENGTH_SHORT).show();
                return;
            } else if(email.isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.edit_profile_activity_toast_email_required),
                        Toast.LENGTH_SHORT).show();
                return;
            } else if(city.isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.edit_profile_activity_toast_city_required),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if(pfpChange) {
                savePhotoFile(this.profilePictureFile);
            }

            currentUser.put(FIRSTNAME_KEY, firstName);
            currentUser.put(LASTNAME_KEY, lastName);
            currentUser.put(EMAIL_KEY, email);
            currentUser.put(CITY_KEY, city);
            currentUser.put(BIO_KEY, bio);
            currentUser.saveInBackground(e -> {
                Log.i(TAG, "Profile changes saved");
                Intent intent = new Intent(context, ProfileFragment.class);
                intent.putExtra(FIRSTNAME_KEY, firstName);
                intent.putExtra(LASTNAME_KEY, lastName);
                intent.putExtra(EMAIL_KEY, email);
                intent.putExtra(CITY_KEY, city);
                intent.putExtra(BIO_KEY, bio);
                setResult(RESULT_OK, intent);
                finish();
            });
        });
    }

    private void loadProfilePic() {
        ParseFile file = currentUser.getParseFile(IMAGE_KEY);
        if(file == null) {
            Log.i(TAG, "User has no pfp");
            return;
        }
        Glide.with(context)
                .load(file.getUrl())
                .circleCrop()
                .into(imageViewEditProfilePicture);
    }

    private void handleImageChoice(String choice) {
        Log.i(TAG, "User chose " + choice);

        if(choice.equals(PHOTO)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            profilePictureFile = getPhotoFileUri(String.format(photoFileName, photoFileIndex));
            photoFileIndex++;
            Uri fileProvider = FileProvider.getUriForFile(context, "com.codepath.fileprovider", profilePictureFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
            cameraActivityResultLauncher.launch(intent);
        }
        else if(choice.equals(GALLERY)) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            profilePictureFile = getPhotoFileUri(String.format(photoFileName, photoFileIndex));
            photoFileIndex++;
            Uri fileProvider = FileProvider.getUriForFile(context, "com.codepath.fileprovider", profilePictureFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
            intent.setType("image/*");
            galleryActivityResultLauncher.launch(intent);
        }
    }

    public void cameraResultCallback(ActivityResult result) {
        Log.i(TAG, String.format("Camera Result Callback resultCode: %d", result.getResultCode()));
        if (result.getResultCode() == Activity.RESULT_CANCELED)
            return;
        // resize bitmap
        // Load the taken image into a preview
        Glide.with(context)
                .load(profilePictureFile)
                .circleCrop()
                .into(imageViewEditProfilePicture);
        pfpChange = true;
        savePhotoFile(profilePictureFile);
    }

    public void galleryResultCallback(ActivityResult result) {
        Log.i(TAG, "Gallery Result Received");
        Intent data = result.getData();
        Uri selectedImageUri = null;
        if (data != null) {
            selectedImageUri = data.getData();
        }
        Glide.with(context)
                .load(selectedImageUri)
                .circleCrop()
                .into(imageViewEditProfilePicture);
        savePhotoFile(profilePictureFile);
    }

    private void showDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.image_chooser_popup);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        Button btnCamera = dialog.findViewById(R.id.btnCamera);
        Button btnGallery = dialog.findViewById(R.id.btnGallery);

        btnCamera.setOnClickListener(view -> {
            handleImageChoice(PHOTO);
            dialog.dismiss();

        });

        btnGallery.setOnClickListener(view -> {
            handleImageChoice(GALLERY);
            dialog.dismiss();
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

    private void savePhotoFile(File photoFile) {
        ParseFile file = new ParseFile(photoFile);

        currentUser.put(IMAGE_KEY, file);
        currentUser.saveInBackground(e -> {
            if (e == null) {
                Log.i(TAG, "Profile picture saved successfully");
            } else {
                Log.e(TAG, "Error saving profile picture: " + e.getMessage());
            }
        });
    }

    private void savePhotoBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        currentUser.put(IMAGE_KEY, new ParseFile("profile_picture", imageBytes));
        currentUser.saveInBackground();
    }

    private Bitmap scaleBitmap(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Log.d(TAG, String.format("Scaling bitmap: Starting dimen W=%d H=%d", width, height));
        float ratioBitmap = (float) width / (float) height;
        float ratioMax = (float) MAX_PROFILE_WIDTH / (float) MAX_PROFILE_HEIGHT;

        int finalWidth = MAX_PROFILE_WIDTH;
        int finalHeight = MAX_PROFILE_HEIGHT;
        if (ratioMax > ratioBitmap) {
            finalWidth = (int) ((float)MAX_PROFILE_HEIGHT * ratioBitmap);
        } else {
            finalHeight = (int) ((float)MAX_PROFILE_WIDTH / ratioBitmap);
        }
        image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
        Log.d(TAG, String.format("Done scaling bitmap: Finished dimen W=%d H=%d",
                finalWidth, finalHeight));

        return image;
    }

}