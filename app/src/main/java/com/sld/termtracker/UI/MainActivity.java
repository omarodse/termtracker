package com.sld.termtracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.termtracker.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sld.termtracker.Database.Repository;

public class MainActivity extends AppCompatActivity implements CourseDetailsFragment.OnCourseTitleUpdatedListener, TestDetailsFragment.OnTestTitleUpdatedListener {

    private static final String TAG = "MainActivity";
    Repository repository;
    private MaterialToolbar toolbar;
    private ImageView backArrow;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotificationUtils.createNotificationChannel(this);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.main_toolbar_title);
        backArrow = findViewById(R.id.back_arrow);

        setSupportActionBar(toolbar);
        // Check if the action bar is not null before setting the title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            // updateToolbarTitle("Terms");
        }

        backArrow.setOnClickListener(v -> onBackPressed());
        // Set up back stack listener
        getSupportFragmentManager().addOnBackStackChangedListener(this::updateToolbar);

        // Set navigation state
        BottomNavigationView navView = findViewById(R.id.bottom_menu);
        navView.setSelectedItemId(R.id.navigation_home); // Set based on the activity

        navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            int currentItemId = navView.getSelectedItemId();

            if (itemId != currentItemId) {
                Intent intent = null;

                if (itemId == R.id.navigation_terms) {
                    intent = new Intent(this, TermsActivity.class);
                } else if (itemId == R.id.navigation_courses) {
                    intent = new Intent(this, CoursesActivity.class);
                } else if (itemId == R.id.navigation_tests) {
                    intent = new Intent(this, TestsActivity.class);
                }

                if (intent != null) {
                    startActivity(intent);
                    finish();
                }
            }
            return true;
            });

        repository = new Repository(getApplication());

        loadCourses();

    }

    private void updateToolbar() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(currentFragment instanceof CourseDetailsFragment) {
            String courseTitle = ((CourseDetailsFragment) currentFragment).getCourseTitle();
            updateToolbarTitle(courseTitle);
            showBackButton(true);
        } else if(currentFragment instanceof HomeFragment) {
            updateToolbarTitle("TermTracker");
            showBackButton(false);
        } else if(currentFragment instanceof TestDetailsFragment) {
            String testTitle = ((TestDetailsFragment) currentFragment).getTestTitle();
            updateToolbarTitle(testTitle);
            showBackButton(true);
        }
    }
    public void updateToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public void onCourseTitleUpdated(String courseTitle) {
        updateToolbarTitle(courseTitle);
    }

    public void showBackButton(boolean show) {
        if (show) {
            backArrow.setVisibility(View.VISIBLE);
        } else {
            backArrow.setVisibility(View.GONE);
        }
    }
    private void loadCourses() {
        repository.getAllCourses(courses -> {
            runOnUiThread(() -> {
                if (courses.isEmpty()) {
                    showEmptyStateFragment();
                } else {
                    showHomeFragment();
                }
            });
        });
    }

    private void showHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, homeFragment);
        transaction.commit();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void showEmptyStateFragment() {
        EmptyStateFragmentHome emptyStateFragmentHome = new EmptyStateFragmentHome();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, emptyStateFragmentHome);
        transaction.commit();
    }

    public void mainShowTestsFragment(String courseTitle, int courseId) {
        TestFragment testFragment = TestFragment.newInstance(courseTitle, courseId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, testFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showEmptyStateFragment(String message, String frameTitle, int termId) {
        EmptyStateFragment emptyStateFragment = EmptyStateFragment.newInstance(message, frameTitle, termId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, emptyStateFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onTestTitleUpdated(String testTitle) {
        updateToolbarTitle(testTitle);
    }
}