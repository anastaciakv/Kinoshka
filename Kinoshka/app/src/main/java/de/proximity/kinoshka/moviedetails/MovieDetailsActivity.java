package de.proximity.kinoshka.moviedetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.proximity.kinoshka.R;
import de.proximity.kinoshka.entity.Movie;

import static dagger.internal.Preconditions.checkNotNull;

public class MovieDetailsActivity extends AppCompatActivity implements MovieDetailsContract.View {
    @Inject
    MovieDetailsContract.Presenter presenter;
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
        ButterKnife.bind(this);
        Movie movie = null;
        if (getIntent().getExtras() != null) {
            movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.ITEM_KEY));
        }
        if (movie == null) {
            Toast.makeText(this, R.string.msg_no_movie_to_display, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        DaggerMovieDetailscomponent.builder()
                .movieDetailsModule(new MovieDetailsModule(this, movie)).build()
                .inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @OnClick(R.id.ivPoster)
    public void onPosterClicked(View view) {
        presenter.onPosterClicked();
    }

    @OnClick(R.id.ivPosterBig)
    public void onBigPosterClicked(View view) {
        presenter.onBigPosterClicked();
    }

    @Override
    public void setPresenter(@NonNull MovieDetailsContract.Presenter presenter) {
        checkNotNull(presenter);
        this.presenter = presenter;
    }

    @Override
    public void setMovieTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void setMovieDescription(String overview) {
        tvDescription.append(overview);
    }

    @Override
    public void setMoviePoster(String imgUrl) {
        Picasso.with(this).load(imgUrl).error(ContextCompat.getDrawable(this, R.drawable.ic_image))
                .placeholder(ContextCompat.getDrawable(this, R.drawable.ic_image)).into(ivPoster);
    }

    @Override
    public void setMovieReleaseDate(String releaseDate) {
        tvDescription.append("\n\n");
        tvDescription.append(getString(R.string.lbl_release_date) + " " + releaseDate);
    }

    @Override
    public void setMovieRating(String rating) {
        tvRating.setText(String.valueOf(rating));
    }

    @Override
    public void displayBigPoster(String imgUrl) {
        Picasso.with(MovieDetailsActivity.this).load(imgUrl).error(ContextCompat.getDrawable(this, R.drawable.ic_image))
                .into(ivPosterBig);
        ivPosterBig.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBigPoster() {
        ivPosterBig.setVisibility(View.INVISIBLE);
    }
}