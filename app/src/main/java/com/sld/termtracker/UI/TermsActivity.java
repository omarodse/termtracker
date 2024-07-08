package com.sld.termtracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.termtracker.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TermsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        MaterialToolbar toolbar = findViewById(R.id.materialToolbar);

        setSupportActionBar(toolbar);
        // Check if the action bar is not null before setting the title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Set navigation state
        BottomNavigationView navView = findViewById(R.id.bottom_menu);
        navView.setSelectedItemId(R.id.navigation_terms); // Set based on the activity

        navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            int currentItemId = navView.getSelectedItemId();

            if (itemId != currentItemId) {
                Intent intent = null;

                if (itemId == R.id.navigation_home) {
                    intent = new Intent(this, MainActivity.class);
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
        showNoTermsFragment();
    }
    private void showNoTermsFragment() {
        Fragment noTermsFragment = new NoTermsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.terms_fragment_container, noTermsFragment);
        transaction.commit();
    }
}