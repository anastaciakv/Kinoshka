package de.proximity.kinoshka.ui.movielist;


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
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import de.proximity.kinoshka.R;
import de.proximity.kinoshka.databinding.ActivityMovieListBinding;
import de.proximity.kinoshka.di.Injectable;
import de.proximity.kinoshka.ui.NavigationController;
import de.proximity.kinoshka.ui.favorites.FavoritesActivity;
import de.proximity.kinoshka.utils.Helper;

public class MovieListActivity extends AppCompatActivity implements Injectable, LifecycleRegistryOwner {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private MovieListViewModel viewModel;
    ActivityMovieListBinding binding;
    MovieGridAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieListViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_list);
        binding.setViewmodel(viewModel);
        initGrid();
        viewModel.getMovies().observe(this, movies -> adapter.update(movies));
    }

    private void initGrid() {
        final GridLayoutManager layoutManager = new GridLayoutManager(this, Helper.getNumberOfColumns(this));
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
        adapter = new MovieGridAdapter(
                (movie, view) -> NavigationController.navigateToMovieDetails(MovieListActivity.this, movie, view));
        binding.rvMovieList.setAdapter(adapter);
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
            case R.id.menu_favorites:
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }
}
