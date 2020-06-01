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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.focustime.Activities.SignInActivity;
import com.project.focustime.Adapters.WishlistAdapter;
import com.project.focustime.DBOperations;
import com.project.focustime.Models.Course;
import com.project.focustime.R;
import com.project.focustime.Utils.Helpers;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.project.focustime.connectServer;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class WishlistFragment extends Fragment implements WishlistAdapter.RemoveItemFromWishList {
    private static final String TAG = "WishlistFragment";
    GridView myWishlistGridLayout;
    private ProgressBar progressBar;
    Button signInButton;
    TextView myCoursesLabel;
    RelativeLayout myWishlistView, signInPlaceholder;
    ArrayList<Course> mWishlist = new ArrayList<>();
    DBOperations db = new DBOperations();
    connectServer cs = new connectServer();
    Connection conn = cs.doInBackground();


    WishlistAdapter.RemoveItemFromWishList mRemoveFromWishlist;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.wishlist_fragment, container, false);
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

    private void init(View view) {
        myWishlistGridLayout = view.findViewById(R.id.myCoursesGridLayout);
        myCoursesLabel = view.findViewById(R.id.myCoursesLabel);
        myWishlistView = view.findViewById(R.id.myWishlistView);
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
        getMyWishlist();
        signInPlaceholder.setVisibility(View.GONE);
        myWishlistView.setVisibility(View.VISIBLE);
    }

    private void loggedOutView() {
        signInPlaceholder.setVisibility(View.VISIBLE);
        myWishlistView.setVisibility(View.GONE);
    }

    private void initProgressBar(View view) {
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        Sprite circularLoading = new Circle();
        progressBar.setIndeterminateDrawable(circularLoading);
    }

    private void initMyWishlistGridView(ArrayList<Course> mWishlist){
        WishlistAdapter adapter = new WishlistAdapter(getActivity(), mWishlist, this);
        myWishlistGridLayout.invalidateViews();
        myWishlistGridLayout.setAdapter(adapter);
    }

    public void getMyWishlist() {
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences preferences = getContext().getSharedPreferences(Helpers.SHARED_PREF, 0);
        final int userID = preferences.getInt("userId", 0);

        mWishlist.clear();

        try{

            mWishlist = db.getWishlist(conn,userID);

            initMyWishlistGridView(mWishlist);
            progressBar.setVisibility(View.INVISIBLE);

        }catch (Exception e){

        }

    }

    @Override
    public void removeFromWishList(int courseId) {

        SharedPreferences preferences = getContext().getSharedPreferences(Helpers.SHARED_PREF, 0);
        String authToken = preferences.getString("userToken", "loggedOut");
        final int userID = preferences.getInt("userId", 0);
        db.deleteWishlist(conn,courseId,userID);
        getMyWishlist();

    }
}
