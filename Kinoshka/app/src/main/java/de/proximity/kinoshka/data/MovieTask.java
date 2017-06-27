package de.proximity.kinoshka.data;


import android.support.annotation.NonNull;

import de.proximity.kinoshka.entity.MovieListResponse;

public interface MovieTask {
    interface MovieTaskCallback {

        void onMovieListFetched(MovieListResponse movieListResponse);

        void onMovieListFetchError();
    }

    void fetchMovies(int sortMode, int page, @NonNull MovieTaskCallback callback);
}
