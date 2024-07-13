package com.sld.termtracker.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.termtracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoTermsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_terms, container, false);

        FloatingActionButton fabAddTerm = view.findViewById(R.id.termsFAB);
        fabAddTerm.setOnClickListener(v -> showAddTermFragment());

        return view;
    }

    private void showAddTermFragment() {
        Fragment addTermFragment = new AddTermFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, addTermFragment);
        transaction.addToBackStack(null);  // Add this transaction to the back stack
        transaction.commit();
    }
}
