package de.proximity.kinoshka.ui.movielist;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.proximity.kinoshka.data.MovieTask;
import de.proximity.kinoshka.data.remote.ServerResponse;
import de.proximity.kinoshka.entity.Movie;
import timber.log.Timber;

public class MovieListViewModel extends BaseObservable {
    private final MovieTask movieTask;
    public ObservableField<List<Movie>> movies = new ObservableField<>();
    private MovieTask.MovieTaskCallback<Movie> movieTaskCallback;
    public ObservableBoolean isLoading = new ObservableBoolean(false);
    public ObservableBoolean showList = new ObservableBoolean(true);
    int currentPage = 1;
    int totalPages = 1;
    String currentSortMode;

    @Inject
    public MovieListViewModel(MovieTask task) {
        this.movieTask = task;
        currentSortMode = Movie.SortMode.mostPopular;
        fetchMovies();
    }

    void fetchMovies() {
        isLoading.set(true);
        movieTask.fetchMovies(currentSortMode, currentPage, getMovieTaskCallback());
        Timber.d("movieTask.fetchMovies");
    }

    public MovieTask.MovieTaskCallback getMovieTaskCallback() {
        if (movieTaskCallback == null) {
            movieTaskCallback = new MovieTask.MovieTaskCallback<Movie>() {
                @Override
                public void onSuccess(ServerResponse<Movie> serverResponse) {
                    isLoading.set(false);
                    List<Movie> prev = movies.get();
                    if (prev == null) prev = new ArrayList<>();
                    prev.addAll(serverResponse.items);
                    movies.set(prev);
                    movies.notifyChange();
                    totalPages = serverResponse.totalPages;
                    showList.set(true);
                }

                @Override
                public void onError() {
                    isLoading.set(false);
                    showList.set(movies.get() != null && !movies.get().isEmpty());
                }
            };
        }
        return movieTaskCallback;
    }

    public void onSortByMostPopular() {
        if (Movie.SortMode.mostPopular.equals(currentSortMode)) return;
        changeSortModeAndUpdate(Movie.SortMode.mostPopular);
    }


    public void onSortByTopRated() {
        if (Movie.SortMode.topRated.equals(currentSortMode)) return;
        changeSortModeAndUpdate(Movie.SortMode.topRated);
    }

    private void changeSortModeAndUpdate(String sortMode) {
        currentSortMode = sortMode;
        currentPage = 1;
        movies.set(new ArrayList<Movie>());
        fetchMovies();
    }


    public void onListScrolled(int visibleItemCount, int totalItemCount, int firstVisibleItemPosition) {
        if (!isLoading.get() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition > 0) {
                currentPage++;
                fetchMovies();
            }
        }
    }

    boolean isLastPage() {
        return currentPage == totalPages;
    }
}