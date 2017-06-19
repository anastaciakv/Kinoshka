package de.proximity.kinoshka.movielist;


import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.proximity.kinoshka.data.MovieTask;
import de.proximity.kinoshka.entity.Movie;
import de.proximity.kinoshka.entity.MovieListResponse;

public class MovieListPresenter implements MovieListContract.Presenter {
    private static final String TAG = MovieListPresenter.class.getSimpleName();
    private final MovieListContract.View view;
    private final MovieTask movieTask;
    private MovieTask.MovieTaskCallback movieTaskCallback;
    private boolean isLoading;
    int currentPage = 1;
    private int totalPages = 1;
    List<Movie> movies = new ArrayList<>();
    int currentSortMode;

    public MovieListPresenter(MovieListContract.View view, MovieTask movieTask) {
        this.view = view;
        this.movieTask = movieTask;
        currentSortMode = Movie.SortMode.mostPopular;
    }

    @Override
    public void start() {
        if (movies.isEmpty())
            fetchMovies();
    }

    void fetchMovies() {
        view.showProgress(true);
        isLoading = true;
        movieTask.fetchMovies(currentSortMode, currentPage, getMovieTaskCallback());
    }

    public MovieTask.MovieTaskCallback getMovieTaskCallback() {
        if (movieTaskCallback == null) {
            movieTaskCallback = new MovieTask.MovieTaskCallback() {

                @Override
                public void onMovieListFetched(MovieListResponse movieListResponse) {
                    isLoading = false;
                    view.showProgress(false);
                    view.showEmptyView(false);
                    view.showList(true);
                    movies.addAll(movieListResponse.movies);
                    view.updateMovieList(movieListResponse.movies);
                    totalPages = movieListResponse.totalPages;
                }

                @Override
                public void onMovieListFetchError() {
                    isLoading = false;
                    view.showProgress(false);
                    view.showList(!movies.isEmpty());
                    view.showEmptyView(movies.isEmpty());

                }
            };
        }
        return movieTaskCallback;
    }

    @Override
    public void onMovieClicked(int clickedItemIndex, View v) {
        view.navigateToMovieDetails(movies.get(clickedItemIndex), v);
    }

    @Override
    public void onListScrolled(int visibleItemCount, int totalItemCount, int firstVisibleItemPosition) {
        if (!isLoading && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition > 0) {
                currentPage++;
                fetchMovies();
            }
        }
    }

    @Override
    public void onSortByMostPopular() {
        if (currentSortMode == Movie.SortMode.mostPopular) return;
        changeSortModeAndUpdate(Movie.SortMode.mostPopular);
    }

    @Override
    public void onSortByTopRated() {
        if (currentSortMode == Movie.SortMode.topRated) return;
        changeSortModeAndUpdate(Movie.SortMode.topRated);
    }

    private void changeSortModeAndUpdate(int sortMode) {
        currentSortMode = sortMode;
        currentPage = 1;
        movies.clear();
        view.clearMovieList();
        fetchMovies();
    }

    private boolean isLastPage() {
        return currentPage == totalPages;
    }
}