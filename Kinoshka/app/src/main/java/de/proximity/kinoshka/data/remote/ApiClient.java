package de.proximity.kinoshka.data.remote;


import de.proximity.kinoshka.entity.MovieListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiClient {
    String BASE_URL = "https://api.themoviedb.org/3/";
    String IMG_BASE_URL = "http://image.tmdb.org/t/p/w342";

    @GET("movie/popular")
    Call<MovieListResponse> getPopularMoviesList(@Query("page") int page);
}
