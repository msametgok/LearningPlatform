package com.project.focustime.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.focustime.R;

public class CourseDescriptionAdapter extends RecyclerView.Adapter<CourseDescriptionAdapter.ViewHolder> {
    private static final String TAG = "CourseDescriptionAdapter";
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mCourseIncludesTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mCourseIncludesTextView = itemView.findViewById(R.id.courseIncludeCell);
        }
    }
    private String mCourseIncludesList;
    private Context mContext;
    public CourseDescriptionAdapter(Context context, String courseIncludes) {
        this.mCourseIncludesList = courseIncludes;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_include_cell, parent, false);
        final CourseDescriptionAdapter.ViewHolder holder = new CourseDescriptionAdapter.ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String currentCourseInclude = mCourseIncludesList; //.get(position) sonradan ekle
        holder.mCourseIncludesTextView.setText(currentCourseInclude);
    }

    @Override
    public int getItemCount() {
        //return mCourseIncludesList.size();
        return 1;
    }
}

