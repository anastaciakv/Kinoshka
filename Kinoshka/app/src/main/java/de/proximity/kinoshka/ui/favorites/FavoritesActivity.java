package de.proximity.kinoshka.ui.favorites;


import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import javax.inject.Inject;

import de.proximity.kinoshka.R;
import de.proximity.kinoshka.databinding.ActivityFavoritesBinding;
import de.proximity.kinoshka.di.Injectable;
import de.proximity.kinoshka.ui.NavigationController;
import de.proximity.kinoshka.utils.Helper;

public class FavoritesActivity extends AppCompatActivity implements Injectable, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int MOVIE_LOADER_ID = 1991;
    @Inject
    FavoritesViewModel viewModel;
    ActivityFavoritesBinding binding;
    private FavMovieGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorites);
        initGrid();
        binding.setViewmodel(viewModel);
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
    }

    private void initGrid() {
        final GridLayoutManager layoutManager = new GridLayoutManager(this, Helper.getNumberOfColumns(this));
        binding.rvMovieList.setLayoutManager(layoutManager);
        binding.rvMovieList.setHasFixedSize(true);
        adapter = new FavMovieGridAdapter(
                (movie, view) -> NavigationController.navigateToMovieDetailsFromFavorites(FavoritesActivity.this, movie, view));
        binding.rvMovieList.setAdapter(adapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyLoader(this, viewModel);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        viewModel.onLoadFinished();
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }

    private static class MyLoader extends AsyncTaskLoader<Cursor> {
        private FavoritesViewModel viewModel;

        public MyLoader(Context context, FavoritesViewModel viewModel) {
            super(context);
            this.viewModel = viewModel;
        }

        @Override
        protected void onStartLoading() {
            viewModel.onStartLoading();
            forceLoad();
        }

        @Override
        public Cursor loadInBackground() {
            return viewModel.getMovies();
        }
    }
}
