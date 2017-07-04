package de.proximity.kinoshka.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import de.proximity.kinoshka.entity.Movie;

/**
 * Interface for database access for Movie related operations.
 */
@Dao
public interface MovieDao {
    /**
     * Inserts a movie into the table.
     *
     * @param movie A movie to insert
     * @return The row ID of the newly inserted movie.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Movie movie);

    /**
     * Select all movieDao.
     *
     * @return A {@link Cursor} of all the movieDao in the table.
     */
    @Query("SELECT * FROM " + Movie.Table.TABLE_NAME)
    Cursor selectAll();

    /**
     * Delete a movie by the ID.
     *
     * @param id The movie ID.
     * @return A number of movieDao deleted. This should always be {@code 1}.
     */
    @Query("DELETE FROM " + Movie.Table.TABLE_NAME + " WHERE " + Movie.Table.COLUMN_ID + " = :id")
    int deleteById(long id);

    /**
     * Select a movie by the ID.
     *
     * @param id The movie ID.
     * @return A {@link Cursor} of the selected movie.
     */
    @Query("SELECT * FROM " + Movie.Table.TABLE_NAME + " WHERE " + Movie.Table.COLUMN_ID + " = :id")
    public abstract Cursor selectById(long id);

    /**
     * Select a movie by the ID.
     *
     * @param id The movie ID.
     * @return The selected {@link Movie}.
     */
    @Query("SELECT * FROM " + Movie.Table.TABLE_NAME + " WHERE " + Movie.Table.COLUMN_ID + " = :id")
    public abstract Movie selectMovieById(long id);

    /**
     * Update the movie. The movie is identified by the row ID.
     *
     * @param movie The movie to update.
     * @return A number of movies updated. This should always be {@code 1}.
     */
    @Update()
    int update(Movie movie);
}
