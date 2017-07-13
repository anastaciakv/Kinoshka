package de.proximity.kinoshka.provider;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import de.proximity.kinoshka.db.MovieDbHelper;
import de.proximity.kinoshka.entity.Movie;

import static de.proximity.kinoshka.entity.Movie.Table.TABLE_NAME;

public class KinoshkaContentProvider extends ContentProvider {
    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;
    public static final String AUTHORITY = "de.proximity.kinoshka";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = TABLE_NAME;
    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String WHERE_ID = Movie.Table.COLUMN_ID + "=?";

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH_MOVIES, MOVIES);
        uriMatcher.addURI(AUTHORITY, PATH_MOVIES + "/#", MOVIE_WITH_ID);
        return uriMatcher;
    }

    private MovieDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final int code = sUriMatcher.match(uri);

        if (code == MOVIES || code == MOVIE_WITH_ID) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }

            final Cursor cursor;
            if (code == MOVIES) {
                cursor = db.query(TABLE_NAME, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
            } else {
                String id = uri.getPathSegments().get(1);
                cursor = db.query(TABLE_NAME, projection,
                        WHERE_ID, new String[]{id},
                        null, null, sortOrder);
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
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                final long id = db.insert(TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(uri, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case MOVIE_WITH_ID:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(returnUri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case MOVIE_WITH_ID:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                String id = uri.getPathSegments().get(1);
                final int count = db.delete(TABLE_NAME, WHERE_ID, new String[]{id});
                if (count > 0) {
                    context.getContentResolver().notifyChange(uri, null);
                }
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
