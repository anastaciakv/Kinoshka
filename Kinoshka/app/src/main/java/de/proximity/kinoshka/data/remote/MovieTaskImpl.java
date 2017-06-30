package de.proximity.kinoshka.data.remote;


import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.proximity.kinoshka.data.MovieTask;
import de.proximity.kinoshka.entity.Movie;
import de.proximity.kinoshka.entity.Review;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dagger.internal.Preconditions.checkNotNull;

@Singleton
public class MovieTaskImpl implements MovieTask {
    private final ApiClient apiClient;

    @Inject
    public MovieTaskImpl(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public void fetchReviews(int movieId, @NonNull MovieTaskCallback callback) {
        checkNotNull(callback);
        Call<ServerResponse<Review>> call = apiClient.getMovieReviews(movieId);
        call.enqueue(new Callback<ServerResponse<Review>>() {
            @Override
            public void onResponse(Call<ServerResponse<Review>> call, Response<ServerResponse<Review>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ServerResponse<Review>> call, Throwable t) {
                callback.onError();
            }
        });
    }

    @Override
    public void fetchMovies(String sortMode, int page, @NonNull final MovieTaskCallback callback) {
        checkNotNull(callback);
        Call<ServerResponse<Movie>> call = apiClient.getSortedMoviesList(sortMode, page);
        call.enqueue(new Callback<ServerResponse<Movie>>() {
            @Override
            public void onResponse(Call<ServerResponse<Movie>> call, Response<ServerResponse<Movie>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ServerResponse<Movie>> call, Throwable t) {
                callback.onError();
            }
        });
    }

}

