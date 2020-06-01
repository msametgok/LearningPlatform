package com.project.focustime.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.focustime.Activities.CoursesActivity;
import com.project.focustime.Activities.LauncherActivity;
import com.project.focustime.Adapters.CategoryAdapter;
import com.project.focustime.Adapters.TopCourseAdapter;
import com.project.focustime.DBOperations;
import com.project.focustime.Models.Category;
import com.project.focustime.Models.Course;
import com.project.focustime.R;
import com.project.focustime.Utils.Helpers;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.project.focustime.connectServer;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class CourseFragment extends Fragment {

    private static final String TAG = "Fragment";

    private ArrayList<Category> categories;

    private ArrayList<Course> courses;

    private RecyclerView recyclerViewForTopCourses = null;
    private RecyclerView recyclerViewForCategories = null;
    private ProgressBar progressBar;
    DBOperations db = new DBOperations();
    connectServer cs = new connectServer();
    Connection conn = cs.doInBackground();
    ArrayList<Course> ncourse = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        recyclerViewForTopCourses  = view.findViewById(R.id.recyclerViewForTopCourses);
        recyclerViewForCategories  = view.findViewById(R.id.recyclerViewForCategories);
        initProgressBar(view);
        Log.d("Done", "Start");
        getTopCourses();
        getCategories();
        return view;
    }


    private void initProgressBar(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        Sprite circularLoading = new Circle();
        progressBar.setIndeterminateDrawable(circularLoading);
    }

    private void initTopCourseRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewForTopCourses.setLayoutManager(layoutManager);
        TopCourseAdapter adapter = new TopCourseAdapter(getActivity(), courses);
        recyclerViewForTopCourses.setAdapter(adapter);
    }

    private void initCategoryListRecyclerView() {
        CategoryAdapter adapter = new CategoryAdapter(getActivity(), categories,courses);
        recyclerViewForCategories.setAdapter(adapter);
        recyclerViewForCategories.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setHeight(int numberOfCategories) {

        int pixels = Helpers.convertDpToPixel((numberOfCategories * 90) + 10);
        Log.d(TAG, "Lets change the height of recycler view");
        ViewGroup.LayoutParams params1 = recyclerViewForCategories.getLayoutParams();
        recyclerViewForCategories.setMinimumHeight(pixels);
        recyclerViewForCategories.requestLayout();
    }

    private  void getTopCourses() {
        progressBar.setVisibility(View.VISIBLE);

        courses = db.getCourses(conn);

        try {
                initTopCourseRecyclerView();
                Log.d("Course Done", "done");
                progressBar.setVisibility(View.INVISIBLE);

        }catch (Exception e){

            Log.e("HATA",e.toString());

        }

    }

    private void getCategories() {
        progressBar.setVisibility(View.VISIBLE);
        categories = db.getCategory(conn);

        try {
                initCategoryListRecyclerView();
                setHeight(categories.size());
                Log.d("Category Done", "done");
                progressBar.setVisibility(View.INVISIBLE);

        }catch (Exception e){

            Log.e("HATA",e.toString());

        }

    }

}