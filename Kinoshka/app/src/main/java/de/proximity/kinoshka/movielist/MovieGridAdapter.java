package de.proximity.kinoshka.movielist;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.proximity.kinoshka.R;
import de.proximity.kinoshka.data.remote.ApiClient;
import de.proximity.kinoshka.entity.Movie;

class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.MovieViewHolder> {
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    final private ListItemClickListener onClickListener;
    List<Movie> movies;

    public MovieGridAdapter(ListItemClickListener itemClickListener) {
        this.onClickListener = itemClickListener;
        movies = new ArrayList<>();

    }

    public void update(List<Movie> movieList) {
        this.movies.addAll(movieList);
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ivPoster)
        ImageView ivPoster;
        @BindView(R.id.tvLabel)
        TextView label;
        private Context context;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            Picasso.with(context).load(ApiClient.IMG_BASE_URL.concat(movies.get(position).posterPath)).into(ivPoster);
//            label.setText(String.valueOf(position));
        }

        @Override
        public void onClick(View v) {
            onClickListener.onListItemClick(getAdapterPosition());
        }
    }
}
