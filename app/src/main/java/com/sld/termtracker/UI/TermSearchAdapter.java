package com.sld.termtracker.UI;

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
import com.sld.termtracker.Entities.OfflineCourse;
import com.sld.termtracker.Entities.OnlineCourse;
import com.sld.termtracker.Entities.Test;

import java.util.List;

public class TermSearchAdapter extends RecyclerView.Adapter<TermSearchAdapter.TermSearchViewHolder>{

    private List<Course> courses;
    Repository repository;

    private static final String TAG = "SearchAdapter";

    public TermSearchAdapter(List<Course> courses, Repository repository) {
        this.courses = courses;
        this.repository = repository;
    }

    @NonNull
    @Override
    public TermSearchAdapter.TermSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_report_item, parent, false);
        return new TermSearchAdapter.TermSearchViewHolder(view, repository);
    }

    public static class TermSearchViewHolder extends RecyclerView.ViewHolder {
        public TextView courseTitle, courseType, numberOfTests;
        private Repository repository;

        public TermSearchViewHolder(@NonNull View itemView, Repository repository) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.course_title_item);
            courseType = itemView.findViewById(R.id.course_type_item);
            numberOfTests = itemView.findViewById(R.id.course_tests_number);
            this.repository = repository;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TermSearchAdapter.TermSearchViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.courseTitle.setText(course.getCourseTitle());
        Log.d(TAG, "Binding course at position: " + position + " with title: " + course.getCourseTitle());
        if (course instanceof OfflineCourse) {
            int testsTotal = ((OfflineCourse) course).getOfflineTestsNumber();
            holder.numberOfTests.setText(String.valueOf(testsTotal));
            holder.courseType.setText("Offline course");
        } else if (course instanceof OnlineCourse) {
            int testsTotal = ((OnlineCourse) course).getOnlineTestsNumber();
            holder.numberOfTests.setText(String.valueOf(testsTotal));
            holder.courseType.setText("Online Course");
        }
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }
}
