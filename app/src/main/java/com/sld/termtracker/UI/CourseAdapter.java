package com.sld.termtracker.UI;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtracker.R;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Entities.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courses;
    private static final String TAG = "courseAdapter";
    Repository repository;
    private OnItemClickListener listener;

    private CourseClickedListener courseClickedListener;

    public interface OnItemClickListener {
        void onItemClick(Course course);
    }
    public CourseAdapter(List<Course> courses, Repository repository, CourseClickedListener courseClickedListener) {
        this.courses = courses;
        this.repository = repository;
        this.courseClickedListener = courseClickedListener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_course_item, parent, false);
        return new CourseViewHolder(view, this, repository, courseClickedListener);

    }
    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.title.setText(course.getCourseTitle());
        holder.startDate.setText(course.getStartDate());
        holder.endDate.setText(course.getEndDate());
        holder.status.setText(course.getStatus().toString());
        Log.d(TAG, "From course adapter working");
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        public TextView title, startDate, endDate, status;

        public View optionsMenu;
        private CourseAdapter adapter;
        private Repository repository;
        private CourseAdapter.CourseClickedListener courseClickedListener;
        public CourseViewHolder(@NonNull View itemView, CourseAdapter adapter, Repository repository, CourseAdapter.CourseClickedListener courseClickedListener) {

            super(itemView);
            title = itemView.findViewById(R.id.course_title);
            startDate = itemView.findViewById(R.id.date_start_date);
            endDate = itemView.findViewById(R.id.date_end_date);
            status = itemView.findViewById(R.id.course_status);
            optionsMenu = itemView.findViewById(R.id.course_options_menu);

            // Hide options menu
            if(itemView.getContext() instanceof MainActivity) {
                optionsMenu.setVisibility(View.GONE);
            }

            optionsMenu.setOnClickListener(this);
            this.adapter = adapter;
            this.repository = repository;
            this.courseClickedListener = courseClickedListener;
            itemView.setOnClickListener(v -> courseClickedListener.onCourseClick(adapter.courses.get(getAdapterPosition())));
        }

        @Override
        public void onClick(View v) {
            showPopupMenu(v);
        }

        private void showPopupMenu(View v) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.menu_item_options, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int position = getAdapterPosition();
            if(item.getItemId() == R.id.action_delete) {
                if (position != RecyclerView.NO_POSITION) {
                    Course course = adapter.courses.get(position);
                    repository.delete(course);
                    adapter.deleteItem(position);
                    return true;
                }
            } else if (item.getItemId() == R.id.action_edit) {
                // Navigate to the AddTestFragment with the test ID
                Context context = itemView.getContext();
                if (context instanceof TermsActivity) {
                    Course course = adapter.courses.get(position);
                    TermsActivity activity = (TermsActivity) context;
                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, AddCourseFragment.newInstance(0, "" , course.getCourseId()));
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                return true;
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void deleteItem(int position) {
        courses.remove(position);
        notifyItemRemoved(position);
    }

    public interface CourseClickedListener {
        void onCourseClick(Course course);
    }
}
