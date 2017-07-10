package de.proximity.kinoshka.db;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.proximity.kinoshka.entity.Movie;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class MoviesDbTest {
    private MoviesDb db;

    @Before
    public void setUp() throws Exception {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(),
                MoviesDb.class).build();
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    @Test
    public void insertAndCount() throws Exception {
        assertThat(db.movieDao().count(), is(0));
        Movie movie = new Movie();
        movie.id = 12345;
        movie.originalTitle = "title";
        db.movieDao().insert(movie);
        assertThat(db.movieDao().count(), is(1));
    }

}