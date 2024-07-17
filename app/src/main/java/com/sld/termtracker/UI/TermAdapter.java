package com.sld.termtracker.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtracker.R;
import com.sld.termtracker.Entities.Term;

import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {
    private List<Term> terms;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Term term);
    }

    public TermAdapter(List<Term> terms, OnItemClickListener listener) {
        this.terms = terms;
        this.listener = listener;
    }

    public static class TermViewHolder extends RecyclerView.ViewHolder {
        public TextView title, startDate, endDate;

        public TermViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.term_title);
            startDate = itemView.findViewById(R.id.term_date_start_date);
            endDate = itemView.findViewById(R.id.term_date_end_date);
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
    }

    @Override
    public int getItemCount() {
        return terms.size();
    }
}
