package com.project.focustime.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.focustime.Activities.CoursesActivity;
import com.project.focustime.DBOperations;
import com.project.focustime.Models.Category;
import com.project.focustime.Models.Course;
import com.project.focustime.R;
import com.google.android.material.card.MaterialCardView;
import com.project.focustime.connectServer;
import java.sql.Connection;
import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>  {

    private static final String TAG = "CategoryAdapter";

    private ArrayList<Category> mCategory;
    private ArrayList<Course> mCourses = new ArrayList<>();
    private Context mContext;

    private MaterialCardView categoryCardViewList;
    DBOperations db = new DBOperations();
    connectServer cs = new connectServer();
    Connection conn = cs.doInBackground();


    public CategoryAdapter(Context context, ArrayList<Category> category, ArrayList<Course> course) {
        mCategory = category;
        mContext = context;
        mCourses=course;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_cell, parent, false);
        categoryCardViewList = view.findViewById(R.id.categoryCardView);

        final ViewHolder holder = new ViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Category currentCategory = mCategory.get(holder.getAdapterPosition());
                getCoursesByCategoryId(currentCategory.getTitle());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Category currentCategory = mCategory.get(position);
        Glide.with(mContext)
                .asBitmap()
                .load(currentCategory.getThumbnail())
                .into(holder.image);

        applyCategoryCardViewBackgroundColor(position);
        holder.name.setText(currentCategory.getTitle());
        holder.numberOfCourses.setText(currentCategory.getNumberOfCourses() + " Courses");

    }

    private void getCoursesByCategoryId(String catName) {

        ArrayList<Course> ncourse = new ArrayList<>();

        for(Course c: mCourses){
            if(c.getCategory().equals(catName)){
                ncourse.add(c);
            }
        }

        Intent intent = new Intent(mContext, CoursesActivity.class);
        intent.putExtra("Course", ncourse);
        mContext.startActivity(intent);

    }

    //arkaplan rengi
    private void applyCategoryCardViewBackgroundColor(int position) {
        int incrementalPosition = position + 1;
        if (incrementalPosition % 3 == 0) {
            categoryCardViewList.setCardBackgroundColor(Color.parseColor("#29D0A8"));
        } else if ((incrementalPosition - 2) % 3 == 0) {
            categoryCardViewList.setCardBackgroundColor(Color.parseColor("#F65053"));
        } else {
            categoryCardViewList.setCardBackgroundColor(Color.parseColor("#594CF5"));
        }
    }

    @Override
    public int getItemCount() {
       try{
           return mCategory.size();
       }catch (Exception e){
           return 0;
       }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView numberOfCourses;
        ImageView goToCategoryWiseCourseList;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.categoryImageView);
            name = itemView.findViewById(R.id.categoryName);
            numberOfCourses = itemView.findViewById(R.id.numberOfCourses);
            goToCategoryWiseCourseList = itemView.findViewById(R.id.goToCategoryWiseCourseList);
        }

    }
}
