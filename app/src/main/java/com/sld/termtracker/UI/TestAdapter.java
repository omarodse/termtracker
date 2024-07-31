package com.sld.termtracker.UI;

import android.content.Context;
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
    public static class TestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

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
            if(item.getItemId() == R.id.action_delete) {
                if (position != RecyclerView.NO_POSITION) {
                    Test test = adapter.tests.get(position);
                    repository.delete(test);
                    adapter.deleteItem(position);
                    return true;
                }
            } else if (item.getItemId() == R.id.action_edit) {
                // Navigate to the AddTestFragment with the test ID
                Context context = itemView.getContext();
                if (context instanceof TermsActivity) {
                    Test test = adapter.tests.get(position);
                    TermsActivity activity = (TermsActivity) context;
                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, AddTestFragment.newInstance(0, "" ,test.getTestId()));
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                return true;
            }
            return false;
        }

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
