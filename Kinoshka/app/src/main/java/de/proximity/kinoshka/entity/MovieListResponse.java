package de.proximity.kinoshka.entity;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieListResponse {
    public int page;
    public int totalResults;
    public int totalPages;
    @SerializedName("results")
    public List<Movie> movies;
}
