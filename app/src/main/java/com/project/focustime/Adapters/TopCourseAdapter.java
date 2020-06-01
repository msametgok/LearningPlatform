package com.project.focustime.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.focustime.Activities.CourseDetailsActivity;
import com.project.focustime.Models.Course;
import com.project.focustime.R;

import java.util.ArrayList;

public class TopCourseAdapter extends RecyclerView.Adapter<TopCourseAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Course> mTopCourses;

    public TopCourseAdapter(Context context, ArrayList<Course> topCourses) {
        mContext = context;
        mTopCourses = topCourses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_course_cell, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Course currentTopCourse = mTopCourses.get(holder.getAdapterPosition());
                switchToCourseDetailsView(currentTopCourse);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Course currentTopCourse = mTopCourses.get(position);
        Glide.with(mContext)
                .asBitmap()
                .load(currentTopCourse.getThumbnail())
                .into(holder.image);

        holder.name.setText(currentTopCourse.getTitle());

        if(!currentTopCourse.getPrice().equals("0")){
            holder.coursePrice.setText(currentTopCourse.getPrice()+"$");
        }else{
            holder.coursePrice.setText("FREE");
        }

        holder.topCourseRating.setRating((float)currentTopCourse.getRating());
    }

    @Override
    public int getItemCount() {
       try{
           return mTopCourses.size();
       }catch (Exception e){
           return 0;
       }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;
        TextView coursePrice;
        RatingBar topCourseRating;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
            coursePrice = itemView.findViewById(R.id.topCoursePrice);
            topCourseRating = itemView.findViewById(R.id.topCourseRating);
        }
    }

    private void switchToCourseDetailsView(Course currentTopCourse) {
        Intent intent = new Intent(mContext, CourseDetailsActivity.class);
        intent.putExtra("Course", currentTopCourse);
        mContext.startActivity(intent);

       System.out.println(currentTopCourse.getId()+ " " + currentTopCourse.getTitle());

    }
}
