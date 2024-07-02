package com.sld.termtracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.termtracker.R;
import com.google.android.material.appbar.MaterialToolbar;

public class TermsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

//        MaterialToolbar toolbar = findViewById(R.id.materialToolbar);
//
//        setSupportActionBar(toolbar);
//        // Check if the action bar is not null before setting the title
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle(R.string.app_name);
//        }
    }
}