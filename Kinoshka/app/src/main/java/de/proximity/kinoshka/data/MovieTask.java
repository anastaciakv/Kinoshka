package de.proximity.kinoshka.data;


import android.support.annotation.NonNull;

import de.proximity.kinoshka.data.remote.ServerResponse;
import de.proximity.kinoshka.entity.Movie;

public interface MovieTask {
    void addToFavorites(Movie movie);

    void removeFromFavorites(Movie movie);

    boolean checkIsFavorite(Movie movie);

    interface MovieTaskCallback<T> {

        void onSuccess(ServerResponse<T> serverResponse);

        void onError();

    }

    void fetchReviews(long movieId, @NonNull MovieTaskCallback callback);

    void fetchMovies(String sortMode, int page, @NonNull MovieTaskCallback callback);
}
