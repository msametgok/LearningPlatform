package com.project.focustime.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.project.focustime.DBOperations;
import com.project.focustime.R;
import com.project.focustime.Utils.Helpers;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.material.textfield.TextInputEditText;
import com.project.focustime.connectServer;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import de.hdodenhof.circleimageview.CircleImageView;

;

public class EditProfileActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 2342;
    private static final int PICK_IMAGE_REQUEST = 22;
    Button chooseDisplayImage, uploadDisplayImage, submitButton;
    TextInputEditText firstNameEditText, lastNameEditText, emailEditText, biographyEditText, websiteEditText, githubEditText, linkedInLinkEditText;
    CircleImageView displayImageView;
    Uri filePath;
    private Bitmap bitmap;
    private ProgressBar progressBar;
    DBOperations db = new DBOperations();
    connectServer cs = new connectServer();
    Connection conn = cs.doInBackground();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init();
        initProgressBar();
        getLoggedInUserData();
    }

    private void getLoggedInUserData() {
        if (isLoggedIn()){
            getUserData();
        }else{
            Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void getUserData() {
        SharedPreferences preferences = this.getSharedPreferences(Helpers.SHARED_PREF, 0);
        int userID = preferences.getInt("userId",0);
        String name = preferences.getString("userName", "");
        String email = preferences.getString("userEmail", "");
        String bio = preferences.getString("userBio", "");
        String website = preferences.getString("userWebsite", "");
        String github = preferences.getString("userGithub", "");
        String linkedin = preferences.getString("userLinkedIn", "");

        firstNameEditText.setText(name);
        emailEditText.setText(email);
        biographyEditText.setText(bio);
        websiteEditText.setText(website);
        githubEditText.setText(github);
        linkedInLinkEditText.setText(linkedin);

    }


    private void init() {
        firstNameEditText = findViewById(R.id.firstNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        biographyEditText = findViewById(R.id.biographyTextView);
        websiteEditText = findViewById(R.id.websiteEditText);
        githubEditText = findViewById(R.id.githubEditText);
        linkedInLinkEditText = findViewById(R.id.linkedInEditText);
        chooseDisplayImage = findViewById(R.id.chooseDisplayImage);
        displayImageView = findViewById(R.id.displayImageView);
        uploadDisplayImage = findViewById(R.id.uploadDisplayImage);
        submitButton = findViewById(R.id.submitButton);
        requestStoragePermission();

        chooseDisplayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChoose();
            }
        });

        uploadDisplayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadUserImageToTheServer();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitEditProfileForm();
            }
        });
    }

    private void initProgressBar() {
        progressBar = findViewById(R.id.progressBar);
        Sprite circularLoading = new Circle();
        progressBar.setIndeterminateDrawable(circularLoading);
    }


    private boolean isLoggedIn() {
        SharedPreferences preferences = this.getSharedPreferences(Helpers.SHARED_PREF, 0);
        int userValidity = preferences.getInt("userValidity", 0);
        if (userValidity == 1) {
            return true;
        }else{
            return false;
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granter", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Permission Not Granter", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                displayImageView.setImageBitmap(bitmap);
                uploadDisplayImage.setVisibility(View.VISIBLE);

            }catch (IOException e){

            }
        }
    }

    private void showFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select A Display Image"), PICK_IMAGE_REQUEST);
    }

    private String getPath(Uri uri) {
        Cursor cursor  = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void uploadUserImageToTheServer() {
        Toast.makeText(this, "Uploading....", Toast.LENGTH_SHORT).show();
        String mediaPath = getPath(filePath);
        SharedPreferences preferences = this.getSharedPreferences(Helpers.SHARED_PREF, 0);
        String authToken = preferences.getString("userToken", "loggedOut");
        File file = new File(mediaPath);

    }

    private void submitEditProfileForm() {
        SharedPreferences preferences = this.getSharedPreferences(Helpers.SHARED_PREF, 0);
        int userID = preferences.getInt("userId", 0);

        String name = firstNameEditText.getText().toString();
        String bio = biographyEditText.getText().toString();
        String web = websiteEditText.getText().toString();
        String github = githubEditText.getText().toString();
        String linkedin = linkedInLinkEditText.getText().toString();

        if(!name.equals("")){

            db.editProfile(conn,userID,name,bio,web,github,linkedin);
            SharedPreferences.Editor prefsEditor = preferences.edit();
            prefsEditor.putString("userName", name);
            prefsEditor.putString("userBio", bio);
            prefsEditor.putString("userWebsite", web);
            prefsEditor.putString("userGithub", github);
            prefsEditor.putString("userLinkedIn", linkedin);
            prefsEditor.commit();
            Toast.makeText(getApplicationContext(),"Profile Updated Successfully",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"Name cannot be empty",Toast.LENGTH_SHORT).show();
        }

    }

    public void handleBackButton(View view) {
        EditProfileActivity.super.onBackPressed();
    }
}
