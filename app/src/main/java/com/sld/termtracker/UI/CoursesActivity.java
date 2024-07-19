package com.sld.termtracker.UI;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.termtracker.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CoursesActivity extends AppCompatActivity {
    private static final String TAG = "CoursesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        MaterialToolbar toolbar = findViewById(R.id.materialToolbar);

        setSupportActionBar(toolbar);
        // Check if the action bar is not null before setting the title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

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
                    overridePendingTransition(0, 0);  // Smooth transitions
                }
            }
            return true;  // Handle the navigation item selection here
        });
        // Display if no courses active
        showEmptyStateFragment("No active courses", "Courses");
    }

    private void showEmptyStateFragment(String message, String frameTitle) {
        EmptyStateFragment emptyStateFragment = EmptyStateFragment.newInstance(message, frameTitle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, emptyStateFragment);
        transaction.commit();
    }
}
