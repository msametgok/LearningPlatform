package com.project.focustime.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.focustime.Adapters.CourseDescriptionAdapter;
import com.project.focustime.Models.CourseDetails;
import com.project.focustime.R;


public class CourseDescriptionFragment extends Fragment {
    private CourseDetails mCourseDetails;
    private RecyclerView mCourseIncludeRecyclerView;
    public CourseDescriptionFragment(CourseDetails courseDetails) {
        mCourseDetails = courseDetails;
    }

    private void init(View view) {
        mCourseIncludeRecyclerView = view.findViewById(R.id.courseIncludesRecyclerView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_course_include, container, false);
        init(view);
        getIncludesData();
        return view;
    }

    private void getIncludesData() {
        CourseDescriptionAdapter adapter = new CourseDescriptionAdapter(getActivity(), mCourseDetails.getCourseIncludes());
        mCourseIncludeRecyclerView.setAdapter(adapter);
        mCourseIncludeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
