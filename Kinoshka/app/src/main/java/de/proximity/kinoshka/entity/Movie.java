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

    public String getPosterUrlW342() {
        return getImageUrl(SupportedImageSize.w342, posterPath);
    }

    public String getPosterUrlW780() {
        return getImageUrl(SupportedImageSize.w780, posterPath);
    }

    public String getVoteAverageStr() {
        return String.valueOf(voteAverage);
    }

    public interface SortMode {
        int mostPopular = 1;
        int topRated = 2;
    }

    public static String IMG_BASE_URL = "http://image.tmdb.org/t/p/";

    public String getImageUrl(String size, String path) {
        return IMG_BASE_URL.concat(size).concat(path);
    }

    public interface SupportedImageSize {
        String w92 = "w92";
        String w154 = "w154";
        String w185 = "w185";
        String w342 = "w342";
        String w500 = "w500";
        String w780 = "w780";
    }
}
