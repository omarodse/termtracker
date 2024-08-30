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
import com.sld.termtracker.Database.Repository;

public class EmptyStateFragment extends Fragment {

    private static final String TAG = "EmptyStateFragment";
    private static final String ARG_MESSAGE = "message";
    private static final String ARG_ITEM_TITLE = "frameTitle";
    private static final String ARG_ITEM_ID = "term_id";

    private static final String ARG_COURSE_ID = "courseId";

    private static final String ARG_COURSE_TYPE = "courseType";
    private String itemTitle;
    private String message;
    private Repository repository;
    private int itemId;

    private int courseId;

    private String courseType;

    MaterialToolbar toolbar;

    public static EmptyStateFragment newInstance(String message, String itemTitle, int itemId, int courseId, String courseType) {
        EmptyStateFragment fragment = new EmptyStateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        if (itemId != -1) {
            args.putInt(ARG_ITEM_ID, itemId);
            args.putString(ARG_ITEM_TITLE, itemTitle);
        }
        if (courseId != -1) {
            args.putInt(ARG_COURSE_ID, courseId);
            args.putString(ARG_COURSE_TYPE, courseType);
        }
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
            courseId = getArguments().getInt(ARG_COURSE_ID);
            courseType = getArguments().getString(ARG_COURSE_TYPE);
            emptyStateText.setText(message);
        }

        TextView termStartDate = view.findViewById(R.id.date_start);
        TextView termEndDate = view.findViewById(R.id.date_end);
        TextView startsLabel = view.findViewById(R.id.term_start);
        TextView endsLabel = view.findViewById(R.id.term_end);

        repository = new Repository(getActivity().getApplication());

        if(itemId != -1 || itemId != 0) {
            repository.getTermById(itemId, term -> {
                if(term != null) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            termStartDate.setText(term.getStartDate());
                            termEndDate.setText(term.getEndDate());
                        });
                    }
                } else {
                    termStartDate.setVisibility(View.GONE);
                    termEndDate.setVisibility(View.GONE);
                    startsLabel.setVisibility(View.GONE);
                    endsLabel.setVisibility(View.GONE);
                }
            });
        }

        if ((getActivity() instanceof MainActivity || getActivity() instanceof TestsActivity)) {
            fabAdd.setVisibility(View.GONE);
        }
        if(getActivity() instanceof CoursesActivity) {
            if(itemId == 0) {
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
                    fragment = AddCourseFragment.newInstance(itemId, itemTitle, courseId, courseType);
                } else if(message.contains("assessments")) {
                    fragment = AddTestFragment.newInstance(itemId, itemTitle, courseId, courseType);
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
