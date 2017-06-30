package de.proximity.kinoshka.data;


import android.support.annotation.NonNull;

import de.proximity.kinoshka.data.remote.ServerResponse;

public interface MovieTask {
    interface MovieTaskCallback<T> {

        void onSuccess(ServerResponse<T> serverResponse);

        void onError();

    }

    void fetchReviews(int movieId, @NonNull MovieTaskCallback callback);

    void fetchMovies(String sortMode, int page, @NonNull MovieTaskCallback callback);
}
