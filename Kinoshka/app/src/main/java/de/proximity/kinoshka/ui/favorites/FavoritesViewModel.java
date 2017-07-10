package de.proximity.kinoshka.ui.favorites;


import android.arch.lifecycle.ViewModel;
import android.database.Cursor;
import android.databinding.ObservableBoolean;

import javax.inject.Inject;

import de.proximity.kinoshka.data.MovieTask;

public class FavoritesViewModel extends ViewModel {
    private final MovieTask movieTask;
    Cursor cursor;
    public ObservableBoolean isLoading = new ObservableBoolean(false);
    public ObservableBoolean showList = new ObservableBoolean(true);

    public Cursor getMovies() {
        cursor = movieTask.fetchMoviesFavorite();
        return cursor;
    }

    @Inject
    public FavoritesViewModel(MovieTask task) {
        this.movieTask = task;
    }

    public void onStartLoading() {
        isLoading.set(true);
    }

    public void onLoadFinished() {
        isLoading.set(false);
        showList.set(cursor != null && cursor.getCount() > 0);
    }
}
