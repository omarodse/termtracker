package com.sld.termtracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.termtracker.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.materialToolbar);
        setSupportActionBar(toolbar);

        // Check if the action bar is not null before setting the title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            TextView toolbarTitle = findViewById(R.id.toolbar_title);
            toolbarTitle.setText(getString(R.string.app_name));
        }

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
                    finish();  // Finish the current activity
                    overridePendingTransition(0, 0);  // Smooth transitions
                }
            }
            return true;  // Handle the navigation item selection here
            });

        showEmptyStateFragment();
    }

    private void showEmptyStateFragment() {
        EmptyStateFragmentHome emptyStateFragmentHome = new EmptyStateFragmentHome();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, emptyStateFragmentHome);
        transaction.commit();
    }

}