package de.proximity.kinoshka.ui.moviedetails;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.view.MenuItem;
import android.widget.Toast;

import org.parceler.Parcels;

import javax.inject.Inject;

import de.proximity.kinoshka.R;
import de.proximity.kinoshka.databinding.ActivityMovieDetailsBinding;
import de.proximity.kinoshka.di.Injectable;
import de.proximity.kinoshka.entity.Movie;

public class MovieDetailsActivity extends AppCompatActivity implements Injectable, LifecycleRegistryOwner {
    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ActivityMovieDetailsBinding binding;
    MovieDetailsViewModel viewModel;
    private ReviewAdapter reviewAdapter;
    private String transitionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Movie movie = null;
        if (getIntent().getExtras() != null) {
            movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.ITEM_KEY));
            transitionName = getIntent().getStringExtra(Movie.TRANSITION_NAME_KEY);
        }
        if (movie == null) {
            Toast.makeText(this, R.string.msg_no_movie_to_display, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        reviewAdapter = new ReviewAdapter();
        initModel(movie);
        initView(movie);
        observe();
    }

    private void observe() {
        viewModel.getReviews().observe(this, reviews -> reviewAdapter.update(reviews));
    }

    private void initModel(Movie movie) {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieDetailsViewModel.class);
        viewModel.setMovie(movie);
    }

    private void initView(Movie movie) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        if (transitionName != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.ivPosterBig.setTransitionName(transitionName);
        }
        binding.toolbar.setTitle(movie.originalTitle);
        binding.setMovie(movie);
        binding.setViewModel(viewModel);

        binding.rvReviewList.setAdapter(reviewAdapter);
        binding.rvReviewList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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

    @Override
    public void onBackPressed() {
        //fixes crash when using shared element transition and pressing the hardware back button
        finish();
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }
}
