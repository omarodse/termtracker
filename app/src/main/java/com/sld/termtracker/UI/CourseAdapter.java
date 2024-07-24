package com.sld.termtracker.UI;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtracker.R;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Entities.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courses;
    private static final String TAG = "courseAdapter";
    Repository repository;

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        public TextView title, starDate, endDate, status;
        public CourseViewHolder(@NonNull View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.course_title);
            starDate = itemView.findViewById(R.id.date_start_date);
            endDate = itemView.findViewById(R.id.date_end_date);
            status = itemView.findViewById(R.id.course_status);
        }
    }

    public CourseAdapter(ArrayList<Course> courses, Context context) {
        this.courses = courses;
        this.repository = new Repository((Application) context.getApplicationContext());
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_course_item, parent, false);
        return new CourseViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.title.setText(course.getCourseTitle());
        holder.starDate.setText(course.getStartDate());
        holder.endDate.setText(course.getEndDate());
        holder.status.setText(course.getStatus().toString());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }
}
