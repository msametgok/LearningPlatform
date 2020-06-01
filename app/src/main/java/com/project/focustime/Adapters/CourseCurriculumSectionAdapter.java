package com.project.focustime.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.focustime.Models.Lesson;
import com.project.focustime.Models.Section;
import com.project.focustime.R;
import com.project.focustime.Utils.Helpers;

import java.util.ArrayList;
import java.util.List;

public class CourseCurriculumSectionAdapter extends RecyclerView.Adapter<CourseCurriculumSectionAdapter.ViewHolder>{
    private static final String TAG = "CourseCurriculumSectionAdapter";
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mSectionTItle;
        private TextView msectionDuration;
        private RecyclerView mLessonRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.mSectionTItle = itemView.findViewById(R.id.sectionTitle);
            this.msectionDuration = itemView.findViewById(R.id.sectionDuration);
            this.mLessonRecyclerView = itemView.findViewById(R.id.sectionWiseLessonsRecyclerView);
        }
    }

    private ArrayList<Section> mSection;
    private Context mContext;
    public CourseCurriculumSectionAdapter(Context context, ArrayList<Section> section) {
        mContext = context;
        mSection = section;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_curriculum_section_cell, parent, false);
        final CourseCurriculumSectionAdapter.ViewHolder holder = new CourseCurriculumSectionAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Section currentSection = mSection.get(position);
        holder.mSectionTItle.setText("● "+currentSection.getTitle());
        holder.msectionDuration.setText(currentSection.getTotalDuration());
        List<Lesson> mLessons = currentSection.getmLesson();
        CourseCurriculumLessonAdapter adapter = new CourseCurriculumLessonAdapter(mContext, mLessons);
        holder.mLessonRecyclerView.setAdapter(adapter);
        holder.mLessonRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        setHeight(mLessons.size(), holder.mLessonRecyclerView);
    }

    private void setHeight(int numberOfItems, RecyclerView mRecyclerView) {
        int pixels = Helpers.convertDpToPixel((numberOfItems) + 10);
        ViewGroup.LayoutParams params1 = mRecyclerView.getLayoutParams();
        mRecyclerView.setMinimumHeight(249);
        mRecyclerView.requestLayout();
    }

    @Override
    public int getItemCount() {
        return mSection.size();
    }
}
