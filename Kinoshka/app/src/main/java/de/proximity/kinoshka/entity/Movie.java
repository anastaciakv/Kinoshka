package de.proximity.kinoshka.entity;

import org.parceler.Parcel;

@Parcel
public class Movie {
    public static final String ITEM_KEY = "MOVIE_ITEM";
    public int voteCount;
    public int id;
    public boolean video;
    public double voteAverage;
    public String title;
    public double popularity;
    public String posterPath;
    public String originalLanguage;
    public String originalTitle;
    public String backdropPath;
    public boolean adult;
    public String overview;
    public String releaseDate;

    public Movie() {
    }

    public interface SortMode {
        int mostPopular = 1;
        int topRated = 2;
    }
}
