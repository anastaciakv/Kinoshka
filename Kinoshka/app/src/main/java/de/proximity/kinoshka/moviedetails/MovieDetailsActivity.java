package de.proximity.kinoshka.moviedetails;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.proximity.kinoshka.R;
import de.proximity.kinoshka.entity.Movie;

public class MovieDetailsActivity extends AppCompatActivity {
    private Movie movie;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            movie = getIntent().getExtras().getParcelable(Movie.ITEM_KEY);
        }
        if (movie == null) {
            Toast.makeText(this, "Error!No movie to display!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        tvTitle.setText(movie.title);
    }
}
