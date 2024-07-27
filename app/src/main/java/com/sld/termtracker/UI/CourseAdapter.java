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
import com.sld.termtracker.Entities.Term;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courses;
    private static final String TAG = "courseAdapter";
    Repository repository;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Course course);
    }
    public CourseAdapter(List<Course> courses, OnItemClickListener listener) {
        this.courses = courses;
        this.listener = listener;
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
        holder.startDate.setText(course.getStartDate());
        holder.endDate.setText(course.getEndDate());
        holder.status.setText(course.getStatus().toString());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(course));
        Log.d(TAG, "From course adapter working");
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        public TextView title, startDate, endDate, status;
        public CourseViewHolder(@NonNull View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.course_title);
            startDate = itemView.findViewById(R.id.date_start_date);
            endDate = itemView.findViewById(R.id.date_end_date);
            status = itemView.findViewById(R.id.course_status);
        }
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }
}
