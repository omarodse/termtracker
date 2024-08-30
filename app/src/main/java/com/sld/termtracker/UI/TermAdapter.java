package com.sld.termtracker.UI;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtracker.R;
import com.sld.termtracker.Database.Repository;
import com.sld.termtracker.Entities.Term;

import java.util.ArrayList;
import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {
    private List<Term> terms;
    private OnItemClickListener listener;

    private static final String TAG = "termAdapter";
    private Repository repository;

    public interface OnItemClickListener {
        void onItemClick(Term term);
    }

    public TermAdapter(List<Term> terms, Repository repository, OnItemClickListener listener) {
        this.terms = terms;
        this.repository = repository;
        this.listener = listener;
    }

    public static class TermViewHolder extends RecyclerView.ViewHolder {
        public TextView title, startDate, endDate;
        public ImageView deleteIcon;

        public TermViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.term_title_search);
            startDate = itemView.findViewById(R.id.term_date_start_date);
            endDate = itemView.findViewById(R.id.term_date_end_date);
            deleteIcon = itemView.findViewById(R.id.delete_button);
        }

        public void bind(final Term term, final OnItemClickListener listener) {
            title.setText(term.getTitle());
            startDate.setText(term.getStartDate());
            endDate.setText(term.getEndDate());
            itemView.setOnClickListener(v -> listener.onItemClick(term));
        }
    }

    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_term_item, parent, false);
        return new TermViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder holder, int position) {
        Term term = terms.get(position);
        holder.bind(term, listener);

        holder.deleteIcon.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            checkAndDeleteTerm(term, position, context);
        });
    }

    @Override
    public int getItemCount() {
        return terms.size();
    }

    public void updateTerms(ArrayList<Term> newTerms) {
        terms.clear();
        terms.addAll(newTerms);
        notifyDataSetChanged();
    }

    private void checkAndDeleteTerm(Term term, int position, Context context) {
        repository.getCoursesByTermId(term.getTermId(), courses -> {
            // Ensure UI updates are run on the main thread
            ((Activity) context).runOnUiThread(() -> {
                if (courses.isEmpty()) {
                    repository.delete(term);
                    terms.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Term deleted", Toast.LENGTH_SHORT).show();
                    if(terms.isEmpty()) {
                        if(context instanceof TermsActivity) {
                            ((TermsActivity) context).showEmptyStateFragment("No active terms", "Terms", 0, -1, "");
                        }
                    }
                } else {
                    Toast.makeText(context, "Cannot delete term with associated courses", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
