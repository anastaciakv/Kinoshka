package de.proximity.kinoshka.moviedetails;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import de.proximity.kinoshka.R;
import de.proximity.kinoshka.databinding.ActivityMovieDetailsBinding;
import de.proximity.kinoshka.entity.Movie;

public class MovieDetailsActivity extends LifecycleActivity implements android.databinding.DataBindingComponent {
    ActivityMovieDetailsBinding binding;

    @BindingAdapter("imageUrl")
    public void bindImage(ImageView imageView, String url) {
        Picasso.with(this).load(url).error(ContextCompat.getDrawable(this, R.drawable.ic_image))
                .placeholder(ContextCompat.getDrawable(this, R.drawable.ic_image)).into(imageView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Movie movie = null;
        if (getIntent().getExtras() != null) {
            movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.ITEM_KEY));
        }
        if (movie == null) {
            Toast.makeText(this, R.string.msg_no_movie_to_display, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        MovieDetailsViewModel viewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);
        viewModel.setMovie(movie);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details, this);
        binding.setMovie(movie);
        binding.setVmodel(viewModel);
    }

    @Override
    public MovieDetailsActivity getMovieDetailsActivity() {
        return this;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}