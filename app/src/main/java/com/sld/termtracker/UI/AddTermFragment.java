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

public class AddTermFragment extends Fragment {
    private TextInputEditText termTitleEditText;
    private TextInputEditText termStartDateEditText;
    private TextInputEditText termEndDateEditText;
    private Repository repository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_term, container, false);

        termTitleEditText = view.findViewById(R.id.term_title);
        termStartDateEditText = view.findViewById(R.id.term_start_date);
        termEndDateEditText = view.findViewById(R.id.term_end_date);
        Button saveButton = view.findViewById(R.id.save_button);

        repository = new Repository(getActivity().getApplication());

        saveButton.setOnClickListener(v -> saveTerm());

        return view;
    }

    private void saveTerm() {
        String title = termTitleEditText.getText().toString().trim();
        String startDate = termStartDateEditText.getText().toString().trim();
        String endDate = termEndDateEditText.getText().toString().trim();

        if (title.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        Term term = new Term(title, startDate, endDate);
        repository.insert(term);

        Toast.makeText(getContext(), "Term saved", Toast.LENGTH_SHORT).show();

        clearForm();
    }

    private void clearForm() {
        termTitleEditText.setText("");
        termStartDateEditText.setText("");
        termEndDateEditText.setText("");
    }
}
