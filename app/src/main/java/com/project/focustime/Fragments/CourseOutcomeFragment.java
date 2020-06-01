package com.project.focustime.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.focustime.Adapters.CourseOutcomesAdapter;
import com.project.focustime.Models.CourseDetails;
import com.project.focustime.R;


public class CourseOutcomeFragment extends Fragment {

    private CourseDetails mCourseDetails;
    private RecyclerView mCourseOutcomesRecyclerView;
    public CourseOutcomeFragment(CourseDetails courseDetails) {
        this.mCourseDetails = courseDetails;
    }

    private void init(View view) {
        mCourseOutcomesRecyclerView = view.findViewById(R.id.courseOutcomeRecyclerView);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_course_outcome, container, false);
        init(view);
        getOutcomesData();
        return view;
    }

    private void getOutcomesData() {
        CourseOutcomesAdapter adapter = new CourseOutcomesAdapter(getActivity(), mCourseDetails.getCourseOutcomes());
        mCourseOutcomesRecyclerView.setAdapter(adapter);
        mCourseOutcomesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
