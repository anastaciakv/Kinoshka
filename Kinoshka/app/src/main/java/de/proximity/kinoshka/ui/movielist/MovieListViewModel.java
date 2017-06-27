package de.proximity.kinoshka.ui.movielist;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.proximity.kinoshka.data.MovieTask;
import de.proximity.kinoshka.data.remote.MovieTaskImpl;
import de.proximity.kinoshka.entity.Movie;
import de.proximity.kinoshka.entity.MovieListResponse;

public class MovieListViewModel extends ViewModel {
    private final MovieTaskImpl movieTask;
    MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private MovieTask.MovieTaskCallback movieTaskCallback;
    public ObservableBoolean isLoading = new ObservableBoolean(false);
    public ObservableBoolean showList = new ObservableBoolean(true);
    int currentPage = 1;
    private int totalPages = 1;
    int currentSortMode;

    @Inject
    public MovieListViewModel(MovieTaskImpl task) {
        this.movieTask = task;
        currentSortMode = Movie.SortMode.mostPopular;
        fetchMovies();
    }

    private void fetchMovies() {
        isLoading.set(true);
        movieTask.fetchMovies(currentSortMode, currentPage, getMovieTaskCallback());
    }

    public MovieTask.MovieTaskCallback getMovieTaskCallback() {
        if (movieTaskCallback == null) {
            movieTaskCallback = new MovieTask.MovieTaskCallback() {

                @Override
                public void onMovieListFetched(MovieListResponse movieListResponse) {
                    isLoading.set(false);
                    List<Movie> oldList = movies.getValue();
                    if (oldList == null) oldList = new ArrayList<>();
                    oldList.addAll(movieListResponse.movies);
                    movies.setValue(oldList);
                    totalPages = movieListResponse.totalPages;
                    showList.set(true);
                }

                @Override
                public void onMovieListFetchError() {
                    isLoading.set(false);
                    showList.set(movies.getValue() != null && !movies.getValue().isEmpty());
                }
            };
        }
        return movieTaskCallback;
    }

    public void onSortByMostPopular() {
        if (currentSortMode == Movie.SortMode.mostPopular) return;
        changeSortModeAndUpdate(Movie.SortMode.mostPopular);
    }


    public void onSortByTopRated() {
        if (currentSortMode == Movie.SortMode.topRated) return;
        changeSortModeAndUpdate(Movie.SortMode.topRated);
    }

    private void changeSortModeAndUpdate(int sortMode) {
        currentSortMode = sortMode;
        currentPage = 1;
        movies.setValue(new ArrayList<>());
        fetchMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
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

    private boolean isLastPage() {
        return currentPage == totalPages;
    }
}