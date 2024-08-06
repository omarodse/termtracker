package com.sld.termtracker.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Entities.Term;

import java.util.ArrayList;
import java.util.List;

public class TermFragment extends Fragment {

    private static final String TAG = "TermFragment";
    private RecyclerView termsRecyclerView;
    private TermAdapter termsAdapter;
    private Repository dataRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms, container, false);

        termsRecyclerView = view.findViewById(R.id.terms_recycler_view);

        // Initialize repository
        dataRepository = new Repository(getActivity().getApplication());

        // Setup RecyclerView for Terms
        termsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        termsAdapter = new TermAdapter(new ArrayList<>(), dataRepository, term -> {
            ((TermsActivity) getActivity()).showCoursesFragment(term.getTermId(), term.getTitle());
        });
        termsRecyclerView.setAdapter(termsAdapter);

        // Retrieve terms asynchronously
        loadTerms();


        FloatingActionButton fabAdd = view.findViewById(R.id.addFAB);
        fabAdd.setOnClickListener(v -> {
            if (getActivity() instanceof TermsActivity) {
                ((TermsActivity) getActivity()).showAddTermForm();
            }
        });

        return view;
    }

    public void loadTerms() {
        dataRepository.getAllTerms(terms -> {
            if (terms.isEmpty()) {
                if(getActivity() instanceof TermsActivity) {
                    ((TermsActivity) getActivity()).showEmptyStateFragment("No active terms", "", 0);
                }
            }
            getActivity().runOnUiThread(() -> {
                termsAdapter.updateTerms(terms);
            });
        });
    }
    public void addTerm(Term term) {
        dataRepository.insert(term);
        loadTerms();
    }

}
