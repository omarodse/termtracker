package com.sld.termtracker.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtracker.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Entities.Course;
import com.sld.termtracker.Entities.CourseType;
import com.sld.termtracker.Entities.OfflineCourse;
import com.sld.termtracker.Entities.OnlineCourse;
import com.sld.termtracker.Entities.Term;

import java.util.ArrayList;

public class CourseFragment extends Fragment {
    private static final String TAG = "courseFragment";
    private static final String ARG_TERM_ID = "term_id";
    private static final String ARG_TERM_TITLE = "term_title";
    private String termTitle;
    private int termId;
    private ArrayList<Course> courseList;
    private CourseAdapter adapter;
    private Repository repository;

    public static CourseFragment newInstance(int termId, String termTitle) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TERM_ID, termId);
        args.putString(ARG_TERM_TITLE, termTitle);
        fragment.setArguments(args);
        return fragment;
    }

    // Method for displaying all courses
    public static CourseFragment newInstanceForAllCourses() {
        return new CourseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courses, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerviewCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        TextView termStarts = view.findViewById(R.id.term_start);
        TextView termEnds = view.findViewById(R.id.term_end);
        TextView startsLabel = view.findViewById(R.id.starts_label);
        TextView endsLabel = view.findViewById(R.id.ends_label);


        // Determine whether to load courses by term or all courses
        if (getArguments() != null && getArguments().containsKey(ARG_TERM_ID)) {
            termId = getArguments().getInt(ARG_TERM_ID);
            termTitle = getArguments().getString(ARG_TERM_TITLE);
            loadCourses(termId, termTitle);
        } else {
            loadAllCourses();
        }

        repository = new Repository(getActivity().getApplication());

        if(getActivity() instanceof TermsActivity) {
            if(termId != -1 || termId !=0) {
                repository.getTermById(termId, term ->{
                    if(term != null) {
                        if(getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                termStarts.setText(term.getStartDate());
                                termEnds.setText(term.getEndDate());
                            });
                        }
                    } else {
                        termStarts.setVisibility(View.GONE);
                        termEnds.setVisibility(View.GONE);
                        startsLabel.setVisibility(View.GONE);
                        endsLabel.setVisibility(View.GONE);
                    }
                });
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        courseList = new ArrayList<>();
        adapter = new CourseAdapter(courseList, repository, this::showCourseDetailFragment);
        recyclerView.setAdapter(adapter);

        FloatingActionButton addCourseForm = view.findViewById(R.id.course_add_FAB);

        if(getActivity() instanceof CoursesActivity) {
            addCourseForm.setVisibility(View.GONE);
        }

        addCourseForm.setOnClickListener(v -> {
            if (getActivity() instanceof TermsActivity) {
                ((TermsActivity) getActivity()).showAddCourseFragment(termId, termTitle, -1, "");
            }
        });

        return view;
    }

    private void loadCourses(int termId, String termTitle) {
        Repository repository = new Repository(getActivity().getApplication());
        repository.getCoursesByTermId(termId, courses -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (courses.isEmpty()) {

                        // Pop a stack to avoid going back to the same fragment
                        getParentFragmentManager().popBackStack();

                        if (getActivity() instanceof TermsActivity) {
                            ((TermsActivity) getActivity()).showEmptyStateFragment("No active courses", termTitle, termId, -1, "");
                        }
                    } else {
                        courseList.clear();
                        courseList.addAll(courses);
                        adapter.notifyDataSetChanged();
                        if(getActivity() instanceof TermsActivity) {
                            ((TermsActivity) getActivity()).setNoCourses(false);
                        }
                    }
                });
            } else {
                Log.e(TAG, "getActivity() returned null");
            }
        });
    }

    private void loadAllCourses() {
        Repository repository = new Repository(getActivity().getApplication());
        repository.getAllCourses(courses -> {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if(courses.isEmpty()) {
                        // Pop a stack to avoid going back to the same fragment
                        getParentFragmentManager().popBackStack();

                        if (getActivity() instanceof CoursesActivity) {
                            ((CoursesActivity) getActivity()).showEmptyStateFragment("No active courses", termTitle, termId, -1, "");
                        }
                    } else {
                        courseList.clear();
                        courseList.addAll(courses);
                        adapter.notifyDataSetChanged();
                    }
                });
            } else {
                Log.e(TAG, "getActivity() returned null");
            }

        });
    }

    private void showCourseDetailFragment(Course course) {
        CourseDetailsFragment fragment = new CourseDetailsFragment();
        if(course instanceof OfflineCourse) {
            fragment = CourseDetailsFragment.newInstance(course.getCourseId(), "Offline Course");
        } else if(course instanceof OnlineCourse) {
            fragment = CourseDetailsFragment.newInstance(course.getCourseId(), "Online Course");
        }
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public String getTermTitle() {
        return termTitle;
    }

}
