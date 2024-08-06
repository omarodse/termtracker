package com.sld.termtracker.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtracker.R;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Entities.Course;

import java.text.ParseException;
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

    public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
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
            Course course = adapter.courses.get(position);
            Context context = itemView.getContext();
            if(item.getItemId() == R.id.action_delete) {
                if (position != RecyclerView.NO_POSITION) {
                    int termId = course.getTermId();
                    repository.delete(course);
                    adapter.deleteItem(position);
                    if(courses.isEmpty()) {
                        if (context instanceof TermsActivity) {
                            ((TermsActivity) context).getSupportFragmentManager().popBackStack();
                            ((TermsActivity) context).showEmptyStateFragment("No active courses", "", termId);
                        }
                    }
                    return true;
                }
            } else if (item.getItemId() == R.id.action_edit) {
                // Navigate to the AddTestFragment with the test ID
                context = itemView.getContext();
                course = adapter.courses.get(position);
                handleEditCourse(course, context);
                return true;
            } else if (item.getItemId() == R.id.action_set_alert) {
                showSetAlertDialog(course, context);
                return true;
            }
            return false;
        }
    }

    private static void handleEditCourse(Course course, Context context) {
        FragmentTransaction transaction = null;

        if (context instanceof TermsActivity) {
            TermsActivity activity = (TermsActivity) context;
            transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, AddCourseFragment.newInstance(0, "", course.getCourseId()));

        } else if (context instanceof CoursesActivity) {
            CoursesActivity activity = (CoursesActivity) context;
            transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, AddCourseFragment.newInstance(0, "", course.getCourseId()));

        }

        if (transaction != null) {
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void showSetAlertDialog(Course course, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Set Alerts");

        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_set_alert, null);
        builder.setView(dialogView);

        EditText startDateEditText = dialogView.findViewById(R.id.start_date_edit_text);
        EditText endDateEditText = dialogView.findViewById(R.id.end_date_edit_text);

        // Set up the buttons
        builder.setPositiveButton("Set", (dialog, which) -> {
            // Get the entered dates
            String startDate = startDateEditText.getText().toString();
            String endDate = endDateEditText.getText().toString();

            if (DateUtils.areDatesValid(startDate, endDate)) {
                // Context context = getContext();
                DateUtils.scheduleDateNotification(context, startDate, course.getCourseTitle() + " starts today");
                DateUtils.scheduleDateNotification(context, endDate, course.getCourseTitle() + " ends today");
            } else {
                Toast.makeText(context, "Invalid dates", Toast.LENGTH_SHORT).show();
                return;
            }

        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
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
