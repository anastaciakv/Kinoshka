package de.proximity.kinoshka.ui.moviedetails;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.proximity.kinoshka.R;
import de.proximity.kinoshka.binding.BindListAdapter;
import de.proximity.kinoshka.databinding.TrailerItemBinding;
import de.proximity.kinoshka.entity.Trailer;

class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> implements BindListAdapter<Trailer> {
    private List<Trailer> trailers = new ArrayList<>();


    @Override
    public TrailerViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final TrailerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.trailer_item, parent, false);
        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startYouTube(binding.getTrailer().key, parent.getContext());
            }
        });
        binding.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTrailer(binding.getTrailer().key, parent.getContext());
            }
        });
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
        holder.bind(trailers.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    @Override
    public void replaceItems(List<Trailer> items) {
        if (items == null) return;
        trailers.clear();
        trailers.addAll(items);
        notifyDataSetChanged();
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