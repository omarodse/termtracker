package com.sld.termtracker.UI;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.termtracker.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sld.termtracker.Database.Repository;

public class TestsActivity extends AppCompatActivity implements TestDetailsFragment.OnTestTitleUpdatedListener {

    private MaterialToolbar toolbar;
    private ImageView backArrow;
    private TextView toolbarTitle;
    Repository repository;

    private static final String TAG = "TestsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.main_toolbar_title);
        backArrow = findViewById(R.id.back_arrow);

        setSupportActionBar(toolbar);
        // Check if the action bar is not null before setting the title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            updateToolbarTitle("Assessments");
        }

        backArrow.setOnClickListener(v -> onBackPressed());
        getSupportFragmentManager().addOnBackStackChangedListener(this::updateToolbar);

        // Set navigation state
        BottomNavigationView navView = findViewById(R.id.bottom_menu);
        navView.setSelectedItemId(R.id.navigation_tests); // Set based on the activity

        navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            int currentItemId = navView.getSelectedItemId();

            if (itemId != currentItemId) {
                Intent intent = null;

                if (itemId == R.id.navigation_home) {
                    intent = new Intent(this, MainActivity.class);
                } else if (itemId == R.id.navigation_terms) {
                    intent = new Intent(this, TermsActivity.class);
                } else if (itemId == R.id.navigation_courses) {
                    intent = new Intent(this, CoursesActivity.class);
                }

                if (intent != null) {
                    startActivity(intent);
                    finish();  // Finish the current activity
                    overridePendingTransition(0, 0);  // Smooth transitions
                }
            }
            return true;
        });

        // Initialize repository
        repository = new Repository(getApplication());

        // Load tests
        loadTests();
    }

    private void updateToolbar() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(currentFragment instanceof TestDetailsFragment) {
            showBackButton(true);
        } else if(currentFragment instanceof TestFragment) {
            updateToolbarTitle("Assessments");
            showBackButton(false);
        } else if(currentFragment instanceof AddTestFragment) {
            updateToolbarTitle("Edit Assessment");
            showBackButton(true);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void loadTests() {
        repository.getAllTests(tests -> {
            runOnUiThread(() -> {
                if (tests.isEmpty()) {
                    showEmptyStateFragment("No active assessments", "Assessments", 0);
                } else {
                    showTestsFragment();
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

    private void showTestsFragment() {
        TestFragment testFragment = TestFragment.newInstanceForAllTests();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, testFragment);
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
    public void onTestTitleUpdated(String testTitle) {
        updateToolbarTitle(testTitle);
    }
}
