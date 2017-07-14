package de.proximity.kinoshka.ui.moviedetails;


import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.proximity.kinoshka.R;
import de.proximity.kinoshka.binding.BindListAdapter;
import de.proximity.kinoshka.databinding.ReviewItemBinding;
import de.proximity.kinoshka.entity.Review;

class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> implements BindListAdapter<Review> {
    private List<Review> reviews = new ArrayList<>();

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ReviewItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.review_item, parent, false);

        return new ReviewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(reviews.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    @Override
    public void replaceItems(List<Review> items) {
        if (items == null) return;
        reviews.clear();
        reviews.addAll(items);
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final ReviewItemBinding binding;
        private ObservableBoolean showFull = new ObservableBoolean(false);

        public ReviewViewHolder(ReviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFull.set(!showFull.get());
                }
            });
        }

        public void bind(Review review) {
            binding.setReview(review);
            binding.setShowFull(showFull);

        }
    }
}