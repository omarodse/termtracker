package com.sld.termtracker.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtracker.R;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Database.TermtrackerDatabaseBuilder;
import com.sld.termtracker.Entities.Course;
import com.sld.termtracker.Entities.CourseType;
import com.sld.termtracker.Entities.OfflineCourse;
import com.sld.termtracker.Entities.OnlineCourse;
import com.sld.termtracker.Entities.Term;
import com.sld.termtracker.Entities.Test;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView coursesRecyclerView, testsRecyclerView, reportRecyclerView;
    private CourseAdapter courseAdapter;
    private TestAdapter testAdapter;
    private TermSearchAdapter termSearchAdapter;
    private Repository repository;
    private TextView toolbarTitle;
    private ArrayList<Course> coursesList = new ArrayList<>();
    private ArrayList<Test> testsList = new ArrayList<>();
    private ArrayList<Term> termList = new ArrayList<>();
    private EditText searchTerm;

    private Button searchButton;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // The RecyclerViews
        coursesRecyclerView = view.findViewById(R.id.activeCoursesRecyclerView);
        testsRecyclerView = view.findViewById(R.id.upcomingTestsRecyclerView);
        reportRecyclerView = view.findViewById(R.id.recyclerViewReport);

        // Here's the search field and search button
        searchTerm = view.findViewById(R.id.search_term_edit_text);
        searchButton = view.findViewById(R.id.search_button);

        // TextViews
        TextView termTitle = view.findViewById(R.id.term_title_search);
        TextView termStartDate = view.findViewById(R.id.term_start_search);
        TextView termEndDate = view.findViewById(R.id.term_end_search);
        TextView reportLabel = view.findViewById(R.id.report_label);
        TextView coursesLabel = view.findViewById(R.id.activeCoursesTitle);
        TextView testsLabel = view.findViewById(R.id.upcomingTestsTitle);
        TextView dateDash = view.findViewById(R.id.date_dash_search);

        // Table columns and dividers
        LinearLayout tableColumns= view.findViewById(R.id.report_columns);
        View divider2 = view.findViewById(R.id.divider2);
        View divider3 = view.findViewById(R.id.divider3);

        // Initialize repository
        repository = new Repository(getActivity().getApplication());

        // Check if there are terms
        repository.getAllTerms(terms -> {
            if (!terms.isEmpty() && isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    searchTerm.setVisibility(View.VISIBLE);
                    termList.clear();
                    termList.addAll(terms);
                });
            }
        });

        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        testsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reportRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        courseAdapter = new CourseAdapter(coursesList, repository, this::showCourseDetailFragment);
        testAdapter = new TestAdapter(testsList, repository, this::showTestDetailFragment);
        termSearchAdapter = new TermSearchAdapter(coursesList, repository);

        coursesRecyclerView.setAdapter(courseAdapter);
        testsRecyclerView.setAdapter(testAdapter);
        reportRecyclerView.setAdapter(termSearchAdapter);

        Drawable arrowDrawable = ContextCompat.getDrawable(getContext(), R.drawable.chevron_backward_20px);
        searchTerm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // When EditText is focused, set the views to GONE
                    coursesRecyclerView.setVisibility(View.GONE);
                    testsRecyclerView.setVisibility(View.GONE);
                    searchButton.setVisibility(View.VISIBLE);
                    coursesLabel.setVisibility(View.GONE);
                    testsLabel.setVisibility(View.GONE);
                    if (arrowDrawable != null) {
                        arrowDrawable.setTint(ContextCompat.getColor(requireContext(), R.color.colorAccent));
                        searchTerm.setCompoundDrawablesWithIntrinsicBounds(arrowDrawable, null, null, null);
                    }
                } else {
                    searchTerm.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            }
        });

        searchTerm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Get the drawable at the start (left) of the EditText
                    Drawable drawableStart = searchTerm.getCompoundDrawables()[0];

                    if (drawableStart != null) {
                        // Calculate the start of the drawable bounds
                        int drawableWidth = drawableStart.getBounds().width();
                        int touchArea = searchTerm.getPaddingStart() + drawableWidth;

                        // Check if the touch is within the drawable's area
                        if (event.getX() <= touchArea) {
                            // Hide the keyboard
                            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                            // Perform actions when the drawable is clicked
                            searchTerm.clearFocus();
                            searchTerm.setText("");
                            coursesRecyclerView.setVisibility(View.VISIBLE);
                            testsRecyclerView.setVisibility(View.VISIBLE);
                            searchButton.setVisibility(View.GONE);
                            coursesLabel.setVisibility(View.VISIBLE);
                            testsLabel.setVisibility(View.VISIBLE);
                            termTitle.setVisibility(View.GONE);
                            termStartDate.setVisibility(View.GONE);
                            termEndDate.setVisibility(View.GONE);
                            reportLabel.setVisibility(View.GONE);
                            tableColumns.setVisibility(View.GONE);
                            divider2.setVisibility(View.GONE);
                            divider3.setVisibility(View.GONE);
                            dateDash.setVisibility(View.GONE);
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        searchButton.setOnClickListener(v -> {
            String searchTitle = searchTerm.getText().toString();
            Term termFound = filter(searchTitle);
            if (termFound != null) {
                termTitle.setText(termFound.getTitle());
                termStartDate.setText(termFound.getStartDate());
                termEndDate.setText(termFound.getEndDate());
                termTitle.setVisibility(View.VISIBLE);
                termStartDate.setVisibility(View.VISIBLE);
                termEndDate.setVisibility(View.VISIBLE);
                reportLabel.setVisibility(View.VISIBLE);
                tableColumns.setVisibility(View.VISIBLE);
                divider2.setVisibility(View.VISIBLE);
                divider3.setVisibility(View.VISIBLE);
                dateDash.setVisibility(View.VISIBLE);

                // Load the table
                int termId = termFound.getTermId();
                loadTable(termId);

                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        loadCourses();
        loadTests();

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadCourses() {
        repository.getAllCourses(courses -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    coursesList.clear();

                    // Limit to two courses if more are available
                    if (courses.size() > 2) {
                        coursesList.addAll(courses.subList(0, 2));
                    } else {
                        coursesList.addAll(courses);
                    }

                    courseAdapter.notifyDataSetChanged();

                    if (coursesList.isEmpty()) {
                        if(getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).showEmptyStateFragment();
                        }
                    }
                });
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadTests() {
        repository.getAllTests(tests -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    testsList.clear();

                    // Limit to two courses if more are available
                    if (tests.size() > 2) {
                        testsList.addAll(tests.subList(0, 2));
                    } else {
                        testsList.addAll(tests);
                    }

                    testAdapter.notifyDataSetChanged();

//                    if (testsList.isEmpty()) {
//                        findViewById(R.id.emptyStateContainer).setVisibility(View.GONE);
//                    }
                });
            }
        });
    }

    public Term filter (String termTitle) {
        for (Term term: termList) {
            if (term.getTitle().toLowerCase().equals(termTitle.toLowerCase())) {
                return term;
            }
        }
        return null;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void loadTable(int termId) {
        repository.getCoursesByTermId(termId, courses -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    coursesList.clear();
                    coursesList.addAll(courses);
                    termSearchAdapter.notifyDataSetChanged();
                });
            }
        });
    }

    private void showCourseDetailFragment(Course course) {
        Fragment fragment = new CourseDetailsFragment();
        if (course instanceof OfflineCourse) {
            fragment = CourseDetailsFragment.newInstance(course.getCourseId(), "Offline Course");
        } else if (course instanceof OnlineCourse) {
            fragment = CourseDetailsFragment.newInstance(course.getCourseId(), "Online Course");
        }
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showTestDetailFragment(Test test) {
        Fragment fragment = new TestDetailsFragment();
        if (test.getCourseType().equals(CourseType.OFFLINE_COURSE)) {
            fragment = TestDetailsFragment.newInstance(test.getTestId(), test.getTitle(), "Offline Course");
        } else if (test.getCourseType().equals(CourseType.ONLINE_COURSE)) {
            fragment = TestDetailsFragment.newInstance(test.getTestId(), test.getTitle(), "Online Course");
        }
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void updateToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }
}
