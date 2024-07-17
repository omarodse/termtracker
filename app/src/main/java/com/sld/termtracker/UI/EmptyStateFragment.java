package com.sld.termtracker.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.termtracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EmptyStateFragment extends Fragment {
    private static final String ARG_MESSAGE = "message";

    public static EmptyStateFragment newInstance(String message) {
        EmptyStateFragment fragment = new EmptyStateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empty_state, container, false);
        TextView emptyStateText = view.findViewById(R.id.empty_state_text);
        FloatingActionButton fabAdd = view.findViewById(R.id.addFAB);

        if (getArguments() != null) {
            String message = getArguments().getString(ARG_MESSAGE);
            emptyStateText.setText(message);

            fabAdd.setOnClickListener(v -> {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                Fragment fragment = null;

                if (message != null) {
                    if (message.contains("terms")) {
                        fragment = new AddTermFragment();
                    }
                }

                if (fragment != null) {
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.addToBackStack(null); // Add to back stack to allow back navigation
                    transaction.commit();
                }
            });
        }

        return view;
    }
}
