package de.proximity.kinoshka.data;


import android.support.annotation.NonNull;

import de.proximity.kinoshka.data.remote.ServerResponse;
import de.proximity.kinoshka.entity.Movie;

public interface MovieTask {
    interface MovieTaskCallback {

        void onMovieListFetched(ServerResponse<Movie> movieListResponse);

        void onMovieListFetchError();
    }

    void fetchMovies(String sortMode, int page, @NonNull MovieTaskCallback callback);
}
