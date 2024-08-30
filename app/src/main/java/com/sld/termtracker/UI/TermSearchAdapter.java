package com.sld.termtracker.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtracker.R;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Entities.Course;
import com.sld.termtracker.Entities.OfflineCourse;
import com.sld.termtracker.Entities.OnlineCourse;
import com.sld.termtracker.Entities.Test;

import java.util.List;

public class TermSearchAdapter extends RecyclerView.Adapter<TermSearchAdapter.TermSearchViewHolder>{

    private List<Course> courses;
    private Repository repository;

    public TermSearchAdapter(List<Course> courses, Repository repository) {
        this.courses = courses;
        this.repository = repository;
    }

    @NonNull
    @Override
    public TermSearchAdapter.TermSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_report_item, parent, false);
        return new TermSearchAdapter.TermSearchViewHolder(view);
    }


    public static class TermSearchViewHolder extends RecyclerView.ViewHolder {
        public TextView courseTitle, courseType, numberOfTests;

        public TermSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.course_title_item);
            courseType = itemView.findViewById(R.id.course_type_item);
            numberOfTests = itemView.findViewById(R.id.course_tests_number);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TermSearchAdapter.TermSearchViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.courseTitle.setText(course.getCourseTitle());
        holder.numberOfTests.setText(course.getNumberOfTests());
        if (course instanceof OfflineCourse) {
            holder.courseType.setText("Offline course");
        } else if (course instanceof OnlineCourse) {
            holder.courseType.setText("Online Course");
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
