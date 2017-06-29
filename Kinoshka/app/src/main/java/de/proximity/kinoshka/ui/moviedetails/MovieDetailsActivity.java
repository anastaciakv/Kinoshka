package de.proximity.kinoshka.ui.moviedetails;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import org.parceler.Parcels;

import javax.inject.Inject;

import de.proximity.kinoshka.R;
import de.proximity.kinoshka.binding.ContextDataBindingComponent;
import de.proandroid.databinding.DataBindingComponenteDetailsBinding;
import de.proximity.kinoshka.di.Injectable;
import de.proximity.kinoshka.entity.Movie;

public class MovieDetailsActivity extends AppCompatActivity implements Injectable {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ActivityMovieDetailsBinding binding;

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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details, new ContextDataBindingComponent(this));
        binding.toolbar.setTitle(movie.originalTitle);
        binding.setMovie(movie);
        MovieDetailsViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieDetailsViewModel.class);
        binding.setViewModel(viewModel);
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
