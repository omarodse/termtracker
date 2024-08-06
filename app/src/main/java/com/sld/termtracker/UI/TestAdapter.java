package com.sld.termtracker.UI;

import android.app.AlertDialog;
import android.content.Context;
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
import com.sld.termtracker.Entities.Test;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

    private List<Test> tests;

    private static final String TAG = "testAdapter";
    Repository repository;
    private TestClickListener testClickListener;

    // TestAdapter constructor
    public TestAdapter(List<Test> tests, Repository repository, TestClickListener testClickListener) {
        this.tests = tests;
        this.repository = repository;
        this.testClickListener = testClickListener;
    }

    // Inner class TestViewHolder for each item
    public class TestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        public TextView title, startDate, endDate, type;
        public View optionsMenu;
        private TestAdapter adapter;
        private Repository repository;
        private TestClickListener testClickListener;

        // TestViewHolder constructor
        public TestViewHolder(@NonNull View itemView, TestAdapter adapter, Repository repository, TestClickListener testClickListener) {

            super(itemView);
            title = itemView.findViewById(R.id.test_title);
            startDate = itemView.findViewById(R.id.test_date_start_date);
            endDate = itemView.findViewById(R.id.test_date_end_date);
            type = itemView.findViewById(R.id.test_type);
            optionsMenu = itemView.findViewById(R.id.course_options_menu);

            // Hide options menu
            if(itemView.getContext() instanceof MainActivity) {
                optionsMenu.setVisibility(View.GONE);
            }

            optionsMenu.setOnClickListener(this);
            this.adapter = adapter;
            this.repository = repository;
            this.testClickListener = testClickListener;
            itemView.setOnClickListener(v -> testClickListener.onTestClick(adapter.tests.get(getAdapterPosition())));
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
            Test test = adapter.tests.get(position);
            Context context = itemView.getContext();
            if(item.getItemId() == R.id.action_delete) {
                if (position != RecyclerView.NO_POSITION) {
                    repository.delete(test);
                    adapter.deleteItem(position);
                    if(tests.isEmpty()) {
                        if(context instanceof TermsActivity) {
                            ((TermsActivity) context).getSupportFragmentManager().popBackStack();
                            ((TermsActivity) context).showEmptyStateFragment("No active assessments", "", 0);
                        }
                    }
                    return true;
                }
            } else if (item.getItemId() == R.id.action_edit) {
                // Navigate to the AddTestFragment with the test ID
                context = itemView.getContext();
                test = adapter.tests.get(position);
                handleEditTest(test, context);
                return true;
            } else if (item.getItemId() == R.id.action_set_alert) {
                showSetAlertDialog(test, context);
                return true;
            }
            return false;
        }

    }
    private static void handleEditTest(Test test, Context context) {
        FragmentTransaction transaction = null;

        if (context instanceof TermsActivity) {
            TermsActivity activity = (TermsActivity) context;
            transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, AddTestFragment.newInstance(0, "", test.getTestId()));

        } else if (context instanceof CoursesActivity) {
            CoursesActivity activity = (CoursesActivity) context;
            transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, AddTestFragment.newInstance(0, "", test.getTestId()));

        } else if (context instanceof TestsActivity) {
            TestsActivity activity = (TestsActivity) context;
            transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, AddTestFragment.newInstance(0, "", test.getTestId()));

        }

        if (transaction != null) {
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void showSetAlertDialog(Test test, Context context) {
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
                DateUtils.scheduleDateNotification(context, startDate, test.getTitle() + " starts today");
                DateUtils.scheduleDateNotification(context, endDate, test.getTitle() + " ends today");
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
    @NonNull
    @Override
    public TestAdapter.TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_test_item, parent, false);
        return new TestViewHolder(view, this, repository, testClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.TestViewHolder holder, int position) {
        Test test = tests.get(position);
        holder.title.setText(test.getTitle());
        holder.startDate.setText(test.getStartDate());
        holder.endDate.setText(test.getEndDate());
        holder.type.setText(test.getType().toString());
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    public void deleteItem(int position) {
        tests.remove(position);
        notifyItemRemoved(position);
    }

    public interface TestClickListener {
        void onTestClick(Test test);
    }

}
