package de.proximity.kinoshka.moviedetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.proximity.kinoshka.R;
import de.proximity.kinoshka.entity.Movie;

public class ScrollingActivity extends AppCompatActivity {
    private Movie movie;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
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

        tvTitle.setText(movie.title);
    }
}
