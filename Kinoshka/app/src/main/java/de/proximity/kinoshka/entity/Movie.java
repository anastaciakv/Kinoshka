package de.proximity.kinoshka.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import org.parceler.Parcel;

import static de.proximity.kinoshka.entity.Movie.Table.COLUMN_BACKDROP_PATH;
import static de.proximity.kinoshka.entity.Movie.Table.COLUMN_ID;
import static de.proximity.kinoshka.entity.Movie.Table.COLUMN_ORIGINAL_TITLE;
import static de.proximity.kinoshka.entity.Movie.Table.COLUMN_OVERVIEW;
import static de.proximity.kinoshka.entity.Movie.Table.COLUMN_POSTER_PATH;
import static de.proximity.kinoshka.entity.Movie.Table.COLUMN_RELEASE_DATE;
import static de.proximity.kinoshka.entity.Movie.Table.COLUMN_VOTE_AVERAGE;

@Entity(tableName = Movie.Table.TABLE_NAME)
@Parcel
public class Movie {
    public interface Table {
        String TABLE_NAME = "movies";
        String COLUMN_ID = BaseColumns._ID;
        String COLUMN_ORIGINAL_TITLE = "originalTitle";
        String COLUMN_POSTER_PATH = "posterPath";
        String COLUMN_BACKDROP_PATH = "backdropPath";
        String COLUMN_RELEASE_DATE = "releaseDate";
        String COLUMN_OVERVIEW = "overview";
        String COLUMN_VOTE_AVERAGE = "voteAverage";
    }

    public static final String ITEM_KEY = "MOVIE_ITEM";
    public static final String TRANSITION_NAME_KEY = "TRANSITION_NAME";

    @PrimaryKey
    @ColumnInfo(index = true, name = COLUMN_ID)
    public long id;
    public int voteCount;
    public boolean video;
    public double voteAverage;
    public String title;
    public double popularity;
    public String posterPath;
    public String originalLanguage;
    // @ColumnInfo(name = COLUMN_ORIGINAL_TITLE)
    public String originalTitle;
    public String backdropPath;
    public boolean adult;
    public String overview;
    public String releaseDate;

    public Movie() {
    }

    public Movie(Cursor cursor) {
        id = cursor.getLong(cursor.getColumnIndex(Table.COLUMN_ID));
        originalTitle = cursor.getString(cursor.getColumnIndex(Table.COLUMN_ORIGINAL_TITLE));
        voteAverage = cursor.getDouble(cursor.getColumnIndex(Table.COLUMN_VOTE_AVERAGE));
        posterPath = cursor.getString(cursor.getColumnIndex(Table.COLUMN_POSTER_PATH));
        backdropPath = cursor.getString(cursor.getColumnIndex(Table.COLUMN_BACKDROP_PATH));
        releaseDate = cursor.getString(cursor.getColumnIndex(Table.COLUMN_RELEASE_DATE));
        overview = cursor.getString(cursor.getColumnIndex(Table.COLUMN_OVERVIEW));
    }

    /**
     * Create a new {@link Movie} from the specified {@link ContentValues}.
     *
     * @param values A {@link ContentValues} of a movie.
     * @return A newly created {@link Movie} instance.
     */
    public static Movie fromContentValues(ContentValues values) {
        final Movie movie = new Movie();
        if (values.containsKey(COLUMN_ID)) {
            movie.id = values.getAsLong(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_VOTE_AVERAGE)) {
            movie.voteAverage = values.getAsDouble(COLUMN_VOTE_AVERAGE);
        }
        if (values.containsKey(COLUMN_ORIGINAL_TITLE)) {
            movie.originalTitle = values.getAsString(COLUMN_ORIGINAL_TITLE);
        }
        if (values.containsKey(COLUMN_BACKDROP_PATH)) {
            movie.backdropPath = values.getAsString(COLUMN_BACKDROP_PATH);
        }
        if (values.containsKey(COLUMN_POSTER_PATH)) {
            movie.posterPath = values.getAsString(COLUMN_POSTER_PATH);
        }
        if (values.containsKey(COLUMN_OVERVIEW)) {
            movie.overview = values.getAsString(COLUMN_OVERVIEW);
        }
        if (values.containsKey(COLUMN_RELEASE_DATE)) {
            movie.releaseDate = values.getAsString(COLUMN_RELEASE_DATE);
        }
        return movie;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_ORIGINAL_TITLE, originalTitle);
        values.put(COLUMN_BACKDROP_PATH, backdropPath);
        values.put(COLUMN_POSTER_PATH, posterPath);
        values.put(COLUMN_OVERVIEW, overview);
        values.put(COLUMN_VOTE_AVERAGE, voteAverage);
        values.put(COLUMN_RELEASE_DATE, releaseDate);
        return values;
    }

    public String getVoteAverageStr() {
        return String.valueOf(voteAverage);
    }

    public interface SortMode {
        String mostPopular = "popular";
        String topRated = "top_rated";
    }

    public static String IMG_BASE_URL = "http://image.tmdb.org/t/p/";

    public String getImageUrl(String size, String path) {
        return IMG_BASE_URL.concat(size).concat(path);
    }

    public String getBigPosterUrl() {
        return getImageUrl(SupportedImageSize.w780, backdropPath == null ? posterPath : backdropPath);
    }

    public String getPosterUrlW342() {
        return getImageUrl(SupportedImageSize.w342, posterPath);
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