package com.sld.termtracker.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentUris;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.termtracker.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Entities.CourseType;
import com.sld.termtracker.Entities.Term;

import java.util.List;

public class TermsActivity extends AppCompatActivity implements
        CourseDetailsFragment.OnCourseTitleUpdatedListener, TestDetailsFragment.OnTestTitleUpdatedListener {

    private static final String TAG = "termActivity";
    private Repository repository;
    private MaterialToolbar toolbar;
    private ImageView backArrow;
    private TextView toolbarTitle;
    private static boolean noTerms = true;
    private boolean noCourses = true;
    private String termTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.main_toolbar_title);
        backArrow = findViewById(R.id.back_arrow);

        setSupportActionBar(toolbar);
        // Check if the action bar is not null before setting the title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            updateToolbarTitle("Terms");
        }

        backArrow.setOnClickListener(v -> onBackPressed());
        // Set up back stack listener
        getSupportFragmentManager().addOnBackStackChangedListener(this::updateToolbar);

        // Set navigation state
        BottomNavigationView navView = findViewById(R.id.bottom_menu);
        navView.setSelectedItemId(R.id.navigation_terms); // Set based on the activity

        navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            int currentItemId = navView.getSelectedItemId();

            Intent intent = null;

            if (itemId == R.id.navigation_home) {
                intent = new Intent(this, MainActivity.class);
            } else if (itemId == R.id.navigation_courses) {
                intent = new Intent(this, CoursesActivity.class);
            } else if (itemId == R.id.navigation_tests) {
                intent = new Intent(this, TestsActivity.class);
            } else if (itemId == R.id.navigation_terms) {
                showTerms();
                updateToolbarTitle("Terms");
                showBackButton(false);
                return true;
            }

            if (intent != null) {
                startActivity(intent);
                finish();
            }

            return true;
        });

        repository = new Repository(getApplication());

        // Retrieve terms asynchronously
        repository.getAllTerms(terms -> {
            runOnUiThread(() -> {
                if (terms.isEmpty()) {
                    showEmptyStateFragment("No active terms", "Terms", -1, -1, "");
                } else {
                    showTerms();
                    noTerms = false;
                }
            });
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void updateToolbar() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof AddCourseFragment) {
            showBackButton(true);
            int courseId = ((AddCourseFragment) currentFragment).getCourseId();
            if(courseId == -1) {
                updateToolbarTitle("Add course");
            } else {
                updateToolbarTitle("Edit Course");
            }
        } else if (currentFragment instanceof CourseFragment) {
            termTitle = ((CourseFragment) currentFragment).getTermTitle();
            updateToolbarTitle(termTitle);
            showBackButton(true);
        } else if (currentFragment instanceof TermFragment) {
            updateToolbarTitle("Terms");
            showBackButton(false);
        } else if(currentFragment instanceof EmptyStateFragment) {
            String emptyStateText = ((EmptyStateFragment) currentFragment).getCenterText();
            if(emptyStateText.contains("courses")) {
                updateToolbarTitle(termTitle);
                showBackButton(true);
            } else if(emptyStateText.contains("terms")) {
                updateToolbarTitle("Terms");
                showBackButton(false);
            }
        } else if(currentFragment instanceof AddTestFragment) {
            showBackButton(true);
            int testId = ((AddTestFragment) currentFragment).getTestId();
            if(testId == -1) {
                updateToolbarTitle("Add assessment");
            } else {
                updateToolbarTitle("Edit assessment");
            }
        } else if(currentFragment instanceof TestDetailsFragment) {
            String testTitle = ((TestDetailsFragment) currentFragment).getTestTitle();
            updateToolbarTitle(testTitle);
            showBackButton(true);
        }
    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    public void updateToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }
    @Override
    public void onCourseTitleUpdated(String courseTitle) {
        updateToolbarTitle(courseTitle);
    }

    public void onTestTitleUpdated(String testTitle) {
        updateToolbarTitle(testTitle);
    }

    public void showBackButton(boolean show) {
        if (show) {
            backArrow.setVisibility(View.VISIBLE);
        } else {
            backArrow.setVisibility(View.GONE);
        }
    }

    public boolean isNoTerms() {
        return noTerms;
    }

    public boolean isNoCourses() {
        return noCourses;
    }

    public void setNoCourses (boolean change) {
        noCourses = change;
    }

    public void showEmptyStateFragment(String message, String frameTitle, int termId, int courseId, String courseType) {
        EmptyStateFragment emptyStateFragment = EmptyStateFragment.newInstance(message, frameTitle, termId, courseId, courseType);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, emptyStateFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showTerms() {
        TermFragment termFragment = new TermFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, termFragment);
        transaction.commit();
    }

    public void showAddTermForm() {
        AddTermFragment addTermFragment = new AddTermFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, addTermFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showAddCourseFragment(int termId, String termTitle, int courseId, String courseType) {
        AddCourseFragment addCourse = AddCourseFragment.newInstance(termId, termTitle, courseId, courseType);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, addCourse);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showAddTestFragment(int itemId, String itemTitle, String courseType) {
        AddTestFragment addTest = AddTestFragment.newInstance(itemId, itemTitle, -1, courseType);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, addTest);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showCoursesFragment(int termId, String termTitle) {
        CourseFragment courseFragment = CourseFragment.newInstance(termId, termTitle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, courseFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showTestsFragment(String itemTitle, int itemId, String courseType) {
        TestFragment testFragment = TestFragment.newInstance(itemTitle, itemId, courseType);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, testFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean hasNoTerms() {
        return noTerms;
    }
}