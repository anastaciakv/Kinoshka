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
import de.proximity.kinoshka.binding.ImageReadyCallback;
import de.proximity.kinoshka.databinding.ActivityMovieDetailsBinding;
import de.proximity.kinoshka.di.Injectable;
import de.proximity.kinoshka.entity.Movie;

public class MovieDetailsActivity extends AppCompatActivity implements Injectable, LifecycleRegistryOwner, ImageReadyCallback {
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        supportPostponeEnterTransition();
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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieDetailsViewModel.class);
        reviewAdapter = new ReviewAdapter();

        initView(movie);
        observe(movie);
    }

    private void observe(Movie movie) {
        viewModel.setMovie(movie);
        viewModel.getReviews().observe(this, reviews -> reviewAdapter.update(reviews));
    }

    private void initView(Movie movie) {
        binding.toolbar.setTitle(movie.originalTitle);
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (transitionName != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.ivPosterBig.setTransitionName(transitionName);
        }
        binding.setCallback(this);
        binding.setMovie(movie);
        binding.setViewModel(viewModel);
        binding.rvReviewList.setHasFixedSize(true);
        binding.rvReviewList.setAdapter(reviewAdapter);
        binding.rvReviewList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            closeScreen();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void closeScreen() {
        supportFinishAfterTransition();
    }

    @Override
    public void onBackPressed() {
        closeScreen();
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public void onImageReady() {
        supportStartPostponedEnterTransition();
    }
}
