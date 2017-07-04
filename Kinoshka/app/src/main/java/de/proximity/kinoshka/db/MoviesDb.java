package de.proximity.kinoshka.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import de.proximity.kinoshka.entity.Movie;

/**
 * Main database description.
 */
@Database(entities = {Movie.class}, version = 1)
public abstract class MoviesDb extends RoomDatabase {

    public abstract MovieDao movieDao();
}
