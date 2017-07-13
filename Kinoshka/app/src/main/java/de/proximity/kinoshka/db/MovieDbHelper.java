package de.proximity.kinoshka.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import de.proximity.kinoshka.entity.Movie;

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, Movie.Table.TABLE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + Movie.Table.TABLE_NAME + " (" +
                Movie.Table.COLUMN_ID + " LONG PRIMARY KEY, " +
                Movie.Table.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, "
                + Movie.Table.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, "
                + Movie.Table.COLUMN_OVERVIEW + " TEXT NOT NULL, "
                + Movie.Table.COLUMN_POSTER_PATH + " TEXT NOT NULL, "
                + Movie.Table.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                + Movie.Table.COLUMN_VOTE_AVERAGE + " DOUBLE NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Movie.Table.TABLE_NAME);
        onCreate(db);
    }
}