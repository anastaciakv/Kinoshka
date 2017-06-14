package de.proximity.kinoshka.movielist;


import java.util.ArrayList;
import java.util.List;

import de.proximity.kinoshka.data.MovieTask;
import de.proximity.kinoshka.entity.Movie;
import de.proximity.kinoshka.entity.MovieListResponse;

public class MovieListPresenter implements MovieListContract.Presenter {
    private static final String TAG = MovieListPresenter.class.getSimpleName();
    private final MovieListContract.View view;
    private final MovieTask movieTask;
    private final int PAGE_SIZE = 20;
    private MovieTask.MovieTaskCallback movieTaskCallback;
    private boolean isLoading;
    private int currentPage = 1;
    private int totalPages = 1;
    List<Movie> movies = new ArrayList<>();

    public MovieListPresenter(MovieListContract.View view, MovieTask movieTask) {
        this.view = view;
        this.movieTask = movieTask;
    }

    @Override
    public void start() {
        if (movies.isEmpty())
            fetchPopularMovies();
    }

    void fetchPopularMovies() {
        view.showProgress(true);
        isLoading = true;
        movieTask.fetchPopularMovies(currentPage, getMovieTaskCallback());
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
                    view.showList(false);
                    view.showEmptyView(true);

                }
            };
        }
        return movieTaskCallback;
    }

    @Override
    public void onMovieClicked(int clickedItemIndex) {
        view.navigateToMovieDetails(movies.get(clickedItemIndex));
    }

    @Override
    public void onListScrolled(int visibleItemCount, int totalItemCount, int firstVisibleItemPosition) {
        if (!isLoading && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition > 0) {
                currentPage++;
                fetchPopularMovies();
            }
        }
    }

    private boolean isLastPage() {
        return currentPage == totalPages;
    }
}
