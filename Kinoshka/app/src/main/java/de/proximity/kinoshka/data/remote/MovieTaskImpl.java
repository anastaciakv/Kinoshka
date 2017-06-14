package de.proximity.kinoshka.data.remote;


import android.support.annotation.NonNull;

import de.proximity.kinoshka.data.MovieTask;
import de.proximity.kinoshka.entity.MovieListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dagger.internal.Preconditions.checkNotNull;

public class MovieTaskImpl implements MovieTask {
    private final ApiClient apiClient;

    public MovieTaskImpl(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public void fetchPopularMovies(int page, @NonNull final MovieTaskCallback callback) {
        checkNotNull(callback);
        Call<MovieListResponse> call = apiClient.getPopularMoviesList(page);
        call.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                callback.onMovieListFetched(response.body());
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                callback.onMovieListFetchError();
            }
        });
    }
}
