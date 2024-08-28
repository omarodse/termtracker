package com.sld.termtracker.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.termtracker.R;
import com.google.android.material.textfield.TextInputEditText;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Entities.Term;

import java.text.ParseException;

public class AddTermFragment extends Fragment {

    private static final String TAG = "AddTerm";
    private TextInputEditText termTitleEditText;
    private TextInputEditText termStartDateEditText;
    private TextInputEditText termEndDateEditText;
    private Repository repository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_term, container, false);

        if(getActivity() instanceof TermsActivity) {
            ((TermsActivity) getActivity()).updateToolbarTitle("Add term");
            ((TermsActivity) getActivity()).showBackButton(true);
        }

        termTitleEditText = view.findViewById(R.id.term_title_search);
        termStartDateEditText = view.findViewById(R.id.term_start_date);
        termEndDateEditText = view.findViewById(R.id.term_end_date);
        Button saveButton = view.findViewById(R.id.save_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);

        cancelButton.setOnClickListener(v -> {
            if(getActivity() instanceof TermsActivity) {
                ((TermsActivity) getActivity()).updateToolbarTitle("Terms");
                ((TermsActivity) getActivity()).showBackButton(false);
            }
            getParentFragmentManager().popBackStack();
        });

        repository = new Repository(getActivity().getApplication());

        saveButton.setOnClickListener(v -> {
            try {
                saveTerm();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        });

        return view;
    }

    private void saveTerm() throws ParseException {
        String title = termTitleEditText.getText().toString().trim();
        String startDate = termStartDateEditText.getText().toString().trim();
        String endDate = termEndDateEditText.getText().toString().trim();

        if (title.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (DateUtils.areDatesValid(startDate, endDate)) {
            // Dates are valid, proceed with saving the data
        } else {
            // Error message to the user
            Toast.makeText(getContext(), "Dates are not valid", Toast.LENGTH_SHORT).show();
            return;
        }

        Term term = new Term(title, startDate, endDate);
        repository.insert(term);

        Toast.makeText(getContext(), "Term saved", Toast.LENGTH_SHORT).show();
        getParentFragmentManager().popBackStack();
        clearForm();

        reloadTermFragment();

    }

    private void clearForm() {
        termTitleEditText.setText("");
        termStartDateEditText.setText("");
        termEndDateEditText.setText("");
    }

    private void reloadTermFragment() {
        if (getActivity() instanceof TermsActivity) {
            ((TermsActivity) getActivity()).showTerms();
        }
    }

}
