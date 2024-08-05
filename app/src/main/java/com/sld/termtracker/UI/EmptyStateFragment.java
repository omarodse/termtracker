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
    private static final String ARG_ITEM_TITLE = "frameTitle";
    private static final String ARG_ITEM_ID = "term_id";
    private String itemTitle;
    private String message;

    private int itemId;

    MaterialToolbar toolbar;

    public static EmptyStateFragment newInstance(String message, String itemTitle, int itemId) {
        EmptyStateFragment fragment = new EmptyStateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        args.putInt(ARG_ITEM_ID, itemId);
        args.putString(ARG_ITEM_TITLE, itemTitle);
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
            itemTitle = getArguments().getString(ARG_ITEM_TITLE);
            itemId = getArguments().getInt(ARG_ITEM_ID);
            emptyStateText.setText(message);

            // Hide FAB if termId is null
            if (!(getActivity() instanceof TermsActivity)) {
                if (itemId == 0) {
                    fabAdd.setVisibility(View.GONE);
                }
            } else if(getActivity() instanceof MainActivity) {
                fabAdd.setVisibility(View.GONE);
            }
        }

        fabAdd.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            Fragment fragment = null;

            if (message != null) {
                if (message.contains("terms")) {
                    fragment = new AddTermFragment();
                } else if (message.contains("courses")) {
                    fragment = AddCourseFragment.newInstance(itemId, itemTitle, -1);
                } else if(message.contains("assessments")) {
                    fragment = AddTestFragment.newInstance(itemId, itemTitle, -1);
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
