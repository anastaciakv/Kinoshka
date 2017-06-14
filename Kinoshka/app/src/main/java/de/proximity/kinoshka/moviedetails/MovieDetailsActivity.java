package de.proximity.kinoshka.moviedetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.proximity.kinoshka.R;
import de.proximity.kinoshka.data.remote.NetworkModule;
import de.proximity.kinoshka.entity.Movie;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private Movie movie;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.tvRating)
    TextView tvRating;
    @BindView(R.id.ivPoster)
    ImageView ivPoster;
    @BindView(R.id.ivPosterBig)
    ImageView ivPosterBig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            movie = getIntent().getExtras().getParcelable(Movie.ITEM_KEY);
        }
        if (movie == null) {
            Toast.makeText(this, "Error!No movie to display!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setMovieData();
    }

    private void setMovieData() {
        //        String imgUrl = NetworkModule.getImageUrl(NetworkModule.SupportedImageSize.w500, movie.backdropPath);
        String imgUrl = NetworkModule.getImageUrl(NetworkModule.SupportedImageSize.w342, movie.posterPath);
        Log.i(TAG, "setMovieData: " + imgUrl);
        Picasso.with(this).load(imgUrl).into(ivPoster);

        tvTitle.setText(movie.title);
        tvDescription.append(movie.overview);
        tvDescription.append("\n\n");
        tvDescription.append("Release Date: " + movie.releaseDate);

        tvRating.setText(String.valueOf(movie.voteAverage));
        ivPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgUrl = NetworkModule.getImageUrl(NetworkModule.SupportedImageSize.w780, movie.posterPath);
                Picasso.with(MovieDetailsActivity.this).load(imgUrl).into(ivPosterBig);
                ivPosterBig.setVisibility(View.VISIBLE);
            }
        });
        ivPosterBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivPosterBig.setVisibility(View.INVISIBLE);
            }
        });
    }
}
