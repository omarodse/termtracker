package com.sld.termtracker.UI;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.termtracker.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sld.termtracker.Database.Repository;

public class CoursesActivity extends AppCompatActivity implements CourseDetailsFragment.OnCourseTitleUpdatedListener, TestDetailsFragment.OnTestTitleUpdatedListener {
    private static final String TAG = "CoursesActivity";

    private MaterialToolbar toolbar;
    private ImageView backArrow;
    private TextView toolbarTitle;
    private Repository repository;

    private boolean hasTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.main_toolbar_title);
        backArrow = findViewById(R.id.back_arrow);

        setSupportActionBar(toolbar);
        // Check if the action bar is not null before setting the title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            updateToolbarTitle("Courses");
        }

        backArrow.setOnClickListener(v -> onBackPressed());
        getSupportFragmentManager().addOnBackStackChangedListener(this::updateToolbar);

        // Set navigation state
        BottomNavigationView navView = findViewById(R.id.bottom_menu);
        navView.setSelectedItemId(R.id.navigation_courses); // Set based on the activity

        navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            int currentItemId = navView.getSelectedItemId();

            if (itemId != currentItemId) {
                Intent intent = null;

                if (itemId == R.id.navigation_home) {
                    intent = new Intent(this, MainActivity.class);
                } else if (itemId == R.id.navigation_terms) {
                    intent = new Intent(this, TermsActivity.class);
                } else if (itemId == R.id.navigation_tests) {
                    intent = new Intent(this, TestsActivity.class);
                }

                if (intent != null) {
                    startActivity(intent);
                    finish();  // Finish the current activity
                    //overridePendingTransition(0, 0);  // Smooth transitions
                }
            }
            return true;
        });


        // Initialize repository
        repository = new Repository(getApplication());

        // Load courses
        loadCourses();
    }

    private void updateToolbar() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(currentFragment instanceof TestDetailsFragment || currentFragment instanceof CourseDetailsFragment) {
            showBackButton(true);
        } else if(currentFragment instanceof CourseFragment) {
            updateToolbarTitle("Courses");
            showBackButton(false);
        } else if(currentFragment instanceof TestFragment) {
            String courseTitle = ((TestFragment) currentFragment).getCourseTitle();
            updateToolbarTitle(courseTitle + "\nAssessments");
            showBackButton(true);
        } else if(currentFragment instanceof AddCourseFragment) {
            updateToolbarTitle("Edit course");
            showBackButton(true);
        } else if(currentFragment instanceof AddTestFragment) {
            int testId = ((AddTestFragment) currentFragment).getTestId();
            if(testId != -1) {
                updateToolbarTitle("Edit assessment");
            }
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void loadCourses() {
        repository.getAllCourses(courses -> {
            runOnUiThread(() -> {
                if (courses.isEmpty()) {
                    showEmptyStateFragment("No active courses", "Courses", 0);
                } else {
                    showCoursesFragment();
                }
            });
        });
    }

    public void showEmptyStateFragment(String message, String frameTitle, int termId) {
        EmptyStateFragment emptyStateFragment = EmptyStateFragment.newInstance(message, frameTitle, termId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, emptyStateFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showCoursesFragment() {
        CourseFragment coursesFragment = CourseFragment.newInstanceForAllCourses();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, coursesFragment);
        transaction.commit();
    }
    public void showTestsFragment(String courseTitle, int courseId) {
        TestFragment testFragment = TestFragment.newInstance(courseTitle, courseId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, testFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showAddTestFragment(int itemId, String itemTitle) {
        AddTestFragment addTest = AddTestFragment.newInstance(itemId, itemTitle, -1);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, addTest);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void updateToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    public void showBackButton(boolean show) {
        if (show) {
            backArrow.setVisibility(View.VISIBLE);
        } else {
            backArrow.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCourseTitleUpdated(String courseTitle) {
        updateToolbarTitle(courseTitle);
    }

    @Override
    public void onTestTitleUpdated(String testTitle) {
        updateToolbarTitle(testTitle);
    }
}
