package com.sld.termtracker.UI;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.termtracker.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TestsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);

        MaterialToolbar toolbar = findViewById(R.id.materialToolbar);

        setSupportActionBar(toolbar);
        // Check if the action bar is not null before setting the title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

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
            return true;  // Handle the navigation item selection here
        });
        showEmptyStateFragment("No active tests", "Tests");
    }

    private void showEmptyStateFragment(String message, String frameTitle) {
        EmptyStateFragment emptyStateFragment = EmptyStateFragment.newInstance(message, frameTitle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.tests_fragment_container, emptyStateFragment);
        transaction.commit();
    }
}
