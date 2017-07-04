package de.proximity.kinoshka.data.remote;


import de.proximity.kinoshka.entity.Movie;
import de.proximity.kinoshka.entity.Review;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiClient {
    String BASE_URL = "https://api.themoviedb.org/3/";

    @GET("movie/{sort}")
    Call<ServerResponse<Movie>> getSortedMoviesList(@Path("sort") String sortMode, @Query("page") int page);

    @GET("movie/{id}/reviews")
    Call<ServerResponse<Review>> getMovieReviews(@Path("id") long movieId);
}
