package de.proximity.kinoshka.ui.moviedetails;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.proximity.kinoshka.R;
import de.proximity.kinoshka.databinding.TrailerItemBinding;
import de.proximity.kinoshka.entity.Trailer;

class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private List<Trailer> reviews = new ArrayList<>();

    public void update(List<Trailer> trailerList) {
        reviews.clear();
        reviews.addAll(trailerList);
        notifyDataSetChanged();
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TrailerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.trailer_item, parent, false);
        binding.tvTitle.setOnClickListener(view -> {
            startYouTube(binding.getTrailer().key, parent.getContext());
        });
        binding.btnShare.setOnClickListener(view -> shareTrailer(binding.getTrailer().key, parent.getContext()));
        return new TrailerViewHolder(binding);
    }

    private void shareTrailer(String trailerKey, Context context) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.msg_share_trailer) + getYouTubeLink(trailerKey));
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getResources().getText(R.string.share_chooser_dialog)));
    }

    @NonNull
    private String getYouTubeLink(String videoKey) {
        return "http://www.youtube.com/watch?v=" + videoKey;
    }

    private void startYouTube(String trailerKey, Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getYouTubeLink(trailerKey))));
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(reviews.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        private final TrailerItemBinding binding;

        public TrailerViewHolder(TrailerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Trailer trailer) {
            binding.setTrailer(trailer);
        }
    }
}