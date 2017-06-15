package de.proximity.kinoshka.data;


import android.support.annotation.NonNull;

import de.proximity.kinoshka.entity.MovieListResponse;

public interface MovieTask {
    interface MovieTaskCallback {

        void onMovieListFetched(MovieListResponse movieListResponse);

        void onMovieListFetchError();
    }

    //  void fetchPopularMovies(int page, @NonNull MovieTaskCallback callback);

    void fetchMovies(int sortMode, int page, @NonNull MovieTaskCallback callback);
}
