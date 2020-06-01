package com.project.focustime.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.project.focustime.Activities.ChangePasswordActivity;
import com.project.focustime.Activities.EditProfileActivity;
import com.project.focustime.Activities.ChangePasswordActivity;
import com.project.focustime.Activities.EditProfileActivity;
import com.project.focustime.Activities.MainActivity;
import com.project.focustime.Activities.SignInActivity;
import com.project.focustime.Models.User;
import com.project.focustime.R;
import com.project.focustime.Utils.Helpers;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;

import de.hdodenhof.circleimageview.CircleImageView;


public class AccountFragment extends Fragment {
    Button signInButton;
    RelativeLayout signInPlaceholder, accountView;
    CircleImageView displayImageView;
    TextView userName;
    RelativeLayout editProfileRelativeLayout, changePasswordRelativeLayout, logOutRelativeLayout;
    private ProgressBar progressBar;
    View view;
    private Context mContext;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        init(view);
        initProgressBar(view);
        if (isLoggedIn()){
            this.loggedInView();
        }else{
            this.loggedOutView();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLoggedIn()){
            this.loggedInView();
        }else{
            this.loggedOutView();
        }
    }

    private boolean isLoggedIn() {
        SharedPreferences preferences = getContext().getSharedPreferences(Helpers.SHARED_PREF, 0);
        int userValidity = preferences.getInt("userValidity", 0);
        if (userValidity == 1) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    void init(View view) {
        signInPlaceholder = view.findViewById(R.id.signInPlaceHolder);
        accountView = view.findViewById(R.id.accountView);
        displayImageView = view.findViewById(R.id.displayImageView);
        userName = view.findViewById(R.id.userName);
        editProfileRelativeLayout = view.findViewById(R.id.editProfileRelativeLayout);
        changePasswordRelativeLayout = view.findViewById(R.id.changePasswordRelativeLayout);
        logOutRelativeLayout = view.findViewById(R.id.logOutRelativeLayout);

        logOutRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllTheSharedPreferencesValues();
            }
        });

        editProfileRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        changePasswordRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        signInButton = view.findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SignInActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initProgressBar(View view) {
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        Sprite circularLoading = new Circle();
        progressBar.setIndeterminateDrawable(circularLoading);
    }


    private void loggedInView() {
        getUserDetails();
        signInPlaceholder.setVisibility(View.GONE);
        accountView.setVisibility(View.VISIBLE);
    }

    private void loggedOutView() {
        signInPlaceholder.setVisibility(View.VISIBLE);
        accountView.setVisibility(View.GONE);
    }

    private void getUserDetails() {
        if (isLoggedIn()){
            getUserData();
        }else{
            Toast.makeText(getContext(), "Please Login First", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserData() {
        final SharedPreferences preferences = getContext().getSharedPreferences(Helpers.SHARED_PREF, 0);
        int userID = preferences.getInt("userId", 0);
        String name = preferences.getString("userName","");
        String photo = preferences.getString("userPhoto","");

        initViewElementsWithUserInfo(name,photo);

    }

    private void initViewElementsWithUserInfo(String name, String photo) {
        progressBar.setVisibility(View.VISIBLE);
        /*Glide.with(mContext)
                .asBitmap()
                .load(photo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(displayImageView);*/
        Bitmap bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.userfoto);
        displayImageView.setImageBitmap(bImage);
        userName.setText(name);
        progressBar.setVisibility(View.GONE);
    }
    private void clearAllTheSharedPreferencesValues() {
        final SharedPreferences preferences = getContext().getSharedPreferences(Helpers.SHARED_PREF, 0);
        preferences.edit().remove("userId").commit();
        preferences.edit().remove("userValidity").commit();
        preferences.edit().remove("userFirstName").commit();
        preferences.edit().remove("userEmail").commit();
        preferences.edit().remove("userPassword").commit();
        preferences.edit().remove("userRole").commit();
        preferences.edit().remove("userToken").commit();
        preferences.edit().remove("userPhoto").commit();
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }
}
