package com.sld.termtracker.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.termtracker.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EmptyStateFragment extends Fragment {

    private static final String TAG = "EmptyStateFragment";
    private static final String ARG_MESSAGE = "message";
    private static final String ARG_FRAME_TITLE = "frameTitle";
    private static final String ARG_TERM_ID = "term_id";
    private String termTitle;
    private String message;

    private int termId;

    MaterialToolbar toolbar;

    public static EmptyStateFragment newInstance(String message, String frameTitle, int termId) {
        EmptyStateFragment fragment = new EmptyStateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        args.putInt(ARG_TERM_ID, termId);
        args.putString(ARG_FRAME_TITLE, frameTitle);
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
            message = getArguments().getString(ARG_MESSAGE);
            termTitle = getArguments().getString(ARG_FRAME_TITLE);
            termId = getArguments().getInt(ARG_TERM_ID);
            emptyStateText.setText(message);

            // Hide FAB if termId is null
            if (!(getActivity() instanceof TermsActivity)) {
                if (termId == 0) {
                    fabAdd.setVisibility(View.GONE);
                }
            }
        }

        fabAdd.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            Fragment fragment = null;

            if (message != null) {
                if (message.contains("terms")) {
                    fragment = new AddTermFragment();
                } else if (message.contains("courses")) {
                    fragment = AddCourseFragment.newInstance(termId, termTitle);
                }
            }

            if (fragment != null) {
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return view;
    }

    public String getCenterText() {
        return message;
    }

}
