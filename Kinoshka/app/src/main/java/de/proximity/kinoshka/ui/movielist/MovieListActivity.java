package de.proximity.kinoshka.ui.movielist;


import android.app.ActivityOptions;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.parceler.Parcels;

import javax.inject.Inject;

import de.proximity.kinoshka.R;
import de.proximity.kinoshka.binding.ContextDataBindingComponent;
import de.proximity.kinoshka.databinding.ActivityMovieListBinding;
import de.proximity.kinoshka.di.Injectable;
import de.proximity.kinoshka.entity.Movie;
import de.proximity.kinoshka.ui.moviedetails.MovieDetailsActivity;

public class MovieListActivity extends AppCompatActivity implements Injectable, LifecycleRegistryOwner {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private MovieListViewModel viewModel;
    ActivityMovieListBinding binding;
    MovieGridAdapter adapter;
    private android.databinding.DataBindingComponent dataBindingComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieListViewModel.class);
        dataBindingComponent = new ContextDataBindingComponent(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_list);
        binding.setViewmodel(viewModel);
        initGrid();
        viewModel.getMovies().observe(this, movies -> adapter.update(movies));
    }

    private void initGrid() {
        final GridLayoutManager layoutManager = new GridLayoutManager(this, getNumberOfColumns());
        binding.rvMovieList.setLayoutManager(layoutManager);
        binding.rvMovieList.setHasFixedSize(true);
        binding.rvMovieList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();

                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                viewModel.onListScrolled(visibleItemCount, totalItemCount, firstVisibleItemPosition);
            }
        });
        adapter = new MovieGridAdapter(dataBindingComponent, this::navigateToMovieDetails);
        binding.rvMovieList.setAdapter(adapter);
    }

    private void navigateToMovieDetails(Movie movie, View v) {
        Intent intent = new Intent(MovieListActivity.this, MovieDetailsActivity.class);

        Bundle extras = new Bundle();
        extras.putParcelable(Movie.ITEM_KEY, Parcels.wrap(movie));
        intent.putExtras(extras);

        Bundle bundle = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            bundle = ActivityOptions.makeSceneTransitionAnimation(this, v, v.getTransitionName()).toBundle();
        }
        startActivity(intent, bundle);
    }

    private int getNumberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_by_most_popular:
                viewModel.onSortByMostPopular();
                item.setChecked(true);
                return true;
            case R.id.menu_sort_by_top_rated:
                viewModel.onSortByTopRated();
                item.setChecked(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }
}
