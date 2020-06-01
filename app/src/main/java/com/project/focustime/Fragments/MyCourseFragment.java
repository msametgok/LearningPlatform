package com.project.focustime.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.focustime.Activities.*;
import com.project.focustime.Adapters.MyCourseAdapter;


import com.project.focustime.DBOperations;
import com.project.focustime.Models.Course;
import com.project.focustime.Models.MyCourse;

import com.project.focustime.R;
import com.project.focustime.Utils.Helpers;
import com.project.focustime.Adapters.MyCourseAdapter;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.project.focustime.connectServer;

import java.sql.Connection;
import java.util.ArrayList;

public class MyCourseFragment extends Fragment {
    private static final String TAG = "MyCourseFragment";
    GridView myCoursesGridLayout;
    private ProgressBar progressBar;
    Button signInButton;
    RelativeLayout myCourseView;
    RelativeLayout signInPlaceholder;
    DBOperations db = new DBOperations();
    connectServer cs = new connectServer();
    Connection conn = cs.doInBackground();
    ArrayList<MyCourse> mMyCourses = new ArrayList<>();
    ArrayList<Course> courses = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_mycourse, container, false);
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
        Log.d(TAG, isLoggedIn()+"");
        if (isLoggedIn()){
            this.loggedInView();
        }else{
            this.loggedOutView();
        }
    }

    private void init(View view) {
        myCoursesGridLayout = view.findViewById(R.id.myCoursesGridLayout);
        myCourseView = view.findViewById(R.id.myCourseView);
        signInPlaceholder = view.findViewById(R.id.signInPlaceHolder);
        signInButton = view.findViewById(R.id.signInButton);
       signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
            }
        });
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

    private void loggedInView() {
        // fetching all of the my courses
        getMyCourses();
        signInPlaceholder.setVisibility(View.GONE);
        myCourseView.setVisibility(View.VISIBLE);
    }

    private void loggedOutView() {
        signInPlaceholder.setVisibility(View.VISIBLE);
        myCourseView.setVisibility(View.GONE);
    }
    private void initProgressBar(View view) {
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        Sprite circularLoading = new Circle();
        progressBar.setIndeterminateDrawable(circularLoading);
    }

    private  void getMyCourses() {
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences preferences = getContext().getSharedPreferences(Helpers.SHARED_PREF, 0);
        int userID = preferences.getInt("userId",0);
        mMyCourses.clear();

        try{

            mMyCourses = db.getMyCourses(conn,userID);

            initMyCourseGridView(mMyCourses);
            progressBar.setVisibility(View.INVISIBLE);

        }catch (Exception e){

        }

    }

    private void initMyCourseGridView(ArrayList<MyCourse> mMyCourse){
        MyCourseAdapter adapter = new MyCourseAdapter(getActivity(), mMyCourse);
        myCoursesGridLayout.setAdapter(adapter);
    }
}
