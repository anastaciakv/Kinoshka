package de.proximity.kinoshka.data.remote;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.proximity.kinoshka.data.MovieTask;
import de.proximity.kinoshka.db.MovieDao;
import de.proximity.kinoshka.entity.Movie;
import de.proximity.kinoshka.entity.Review;
import de.proximity.kinoshka.entity.Trailer;
import de.proximity.kinoshka.provider.KinoshkaContentProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static dagger.internal.Preconditions.checkNotNull;

@Singleton
public class MovieTaskImpl implements MovieTask {
    private final ApiClient apiClient;
    private final MovieDao movieDao;
    private final ContentResolver contentResolver;

    @Inject
    public MovieTaskImpl(ApiClient apiClient, MovieDao movieDao, ContentResolver contentResolver) {
        this.apiClient = apiClient;
        this.movieDao = movieDao;
        this.contentResolver = contentResolver;
    }

    @Override
    public void addToFavorites(Movie movie) {
        new AddToFavoritesTask(movie, contentResolver).execute();
    }

    static class AddToFavoritesTask extends AsyncTask<Void, Void, Void> {
        private final Movie movie;
        private final ContentResolver contentResolver;

        AddToFavoritesTask(Movie movie, ContentResolver contentResolver) {
            this.movie = movie;
            this.contentResolver = contentResolver;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            contentResolver.insert(KinoshkaContentProvider.CONTENT_URI, movie.toContentValues());
            return null;
        }
    }

    @Override
    public void removeFromFavorites(Movie movie) {
        // movieDao.deleteById(movie.id);
        Uri uri = KinoshkaContentProvider.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.id)).build();
        contentResolver.delete(uri, null, null);
    }

    @Override
    public boolean checkIsFavorite(Movie movie) {
        Uri uri = KinoshkaContentProvider.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.id)).build();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        boolean isFavorite = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        return isFavorite;
//        return movieDao.selectMovieById(movie.id) != null;
    }

    @Override
    public void fetchReviews(long movieId, @NonNull MovieTaskCallback callback) {
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

    @Override
    public void fetchTrailers(long movieId, @NonNull MovieTaskCallback callback) {
        checkNotNull(callback);
        Call<ServerResponse<Trailer>> call = apiClient.getMovieTrailers(movieId);
        call.enqueue(new Callback<ServerResponse<Trailer>>() {
            @Override
            public void onResponse(Call<ServerResponse<Trailer>> call, Response<ServerResponse<Trailer>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ServerResponse<Trailer>> call, Throwable t) {
                callback.onError();
            }
        });
    }

    @Override
    public Cursor fetchMoviesFavorite() {
        return contentResolver.query(KinoshkaContentProvider.CONTENT_URI, null, null, null, null);
    }

}

