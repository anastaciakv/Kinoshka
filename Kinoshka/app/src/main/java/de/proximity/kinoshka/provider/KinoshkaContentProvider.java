package de.proximity.kinoshka.provider;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import dagger.android.DaggerContentProvider;
import de.proximity.kinoshka.db.MovieDao;
import de.proximity.kinoshka.entity.Movie;

public class KinoshkaContentProvider extends DaggerContentProvider {
    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;
    public static final String AUTHORITY = "de.proximity.kinoshka";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = Movie.Table.TABLE_NAME;
    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();


    private static final UriMatcher sUriMatcher = buildUriMatcher();

    @Inject
    MovieDao movieDao;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH_MOVIES, MOVIES);
        uriMatcher.addURI(AUTHORITY, PATH_MOVIES + "/#", MOVIE_WITH_ID);
        return uriMatcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final int code = sUriMatcher.match(uri);
        if (code == MOVIES || code == MOVIE_WITH_ID) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }

            final Cursor cursor;
            if (code == MOVIES) {
                cursor = movieDao.selectAll();
            } else {
                cursor = movieDao.selectById(ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                final long id = movieDao.insert(Movie.fromContentValues(contentValues));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case MOVIE_WITH_ID:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case MOVIE_WITH_ID:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final int count = movieDao.deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case MOVIE_WITH_ID:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final Movie movie = Movie.fromContentValues(contentValues);
                final int count = movieDao.update(movie);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
