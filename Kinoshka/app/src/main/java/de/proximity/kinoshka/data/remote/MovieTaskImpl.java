package de.proximity.kinoshka.data.remote;


import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.proximity.kinoshka.data.MovieTask;
import de.proximity.kinoshka.entity.Movie;
import de.proximity.kinoshka.entity.MovieListResponse;
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
    public void fetchMovies(int sortMode, int page, @NonNull final MovieTaskCallback callback) {
        checkNotNull(callback);
        Call<MovieListResponse> call = null;
        switch (sortMode) {
            case Movie.SortMode.mostPopular:
                call = apiClient.getPopularMoviesList(page);
                break;
            case Movie.SortMode.topRated:
                call = apiClient.getTopRatedMoviesList(page);
                break;
        }
        if (call == null) {
            callback.onMovieListFetchError();
        } else {
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
}
