package de.proximity.kinoshka.provider;

import android.arch.persistence.room.Room;
import android.content.ComponentName;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.proximity.kinoshka.db.MovieDao;
import de.proximity.kinoshka.db.MoviesDb;
import de.proximity.kinoshka.entity.Movie;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class KinoshkaContentProviderTest extends AndroidTestCase {

    private final Context mContext = InstrumentationRegistry.getTargetContext();
    private MovieDao movieDao;
    private MoviesDb db;
    private KinoshkaContentProvider provider;


    @Before
    public void setUp() throws Exception {
        super.setUp();
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(),
                MoviesDb.class).build();
        movieDao = db.movieDao();
        provider = new KinoshkaContentProvider();
        provider.attachInfo(InstrumentationRegistry.getTargetContext(), null);
        provider.setMovieDao(movieDao);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        db.close();
    }

    @Test
    public void testProviderRegistry() {
        String packageName = mContext.getPackageName();
        String taskProviderClassName = KinoshkaContentProvider.class.getName();
        ComponentName componentName = new ComponentName(packageName, taskProviderClassName);
        try {
            PackageManager pm = mContext.getPackageManager();
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            String actualAuthority = providerInfo.authority;
            String expectedAuthority = packageName;
            String incorrectAuthority =
                    "Error: TaskContentProvider registered with authority: " + actualAuthority +
                            " instead of expected authority: " + expectedAuthority;
            assertEquals(incorrectAuthority,
                    actualAuthority,
                    expectedAuthority);

        } catch (PackageManager.NameNotFoundException e) {
            String providerNotRegisteredAtAll =
                    "Error: TaskContentProvider not registered at " + mContext.getPackageName();
            fail(providerNotRegisteredAtAll);
        }
    }

    private static final Uri TEST_MOVIES = KinoshkaContentProvider.CONTENT_URI;
    // Content URI for a single task with id = 1
    private static final Uri TEST_MOVIE_WITH_ID = TEST_MOVIES.buildUpon().appendPath("1").build();


    @Test
    public void uriMatcher() throws Exception {
           /* Create a URI matcher that the TaskContentProvider uses */
        UriMatcher testMatcher = KinoshkaContentProvider.buildUriMatcher();

        /* Test that the code returned from our matcher matches the expected TASKS int */
        String moviesUriDoesNotMatch = "Error: The MOVIES URI was matched incorrectly.";
        int actualMoviesMatchCode = testMatcher.match(TEST_MOVIES);
        int expectedMoviesMatchCode = KinoshkaContentProvider.MOVIES;
        assertEquals(moviesUriDoesNotMatch,
                actualMoviesMatchCode,
                expectedMoviesMatchCode);

        /* Test that the code returned from our matcher matches the expected TASK_WITH_ID */
        String movieWithIdDoesNotMatch =
                "Error: The MOVIE_WITH_ID URI was matched incorrectly.";
        int actualMovieWithIdCode = testMatcher.match(TEST_MOVIE_WITH_ID);
        int expectedMovieWithIdCode = KinoshkaContentProvider.MOVIE_WITH_ID;
        assertEquals(movieWithIdDoesNotMatch,
                actualMovieWithIdCode,
                expectedMovieWithIdCode);
    }

    @Test
    public void query_All() throws Exception {
        assertThat(movieDao.count(), is(0));
        Movie movie = new Movie();
        movie.id = 12345;
        Movie movie2 = new Movie();
        movie.id = 256;
        provider.insert(KinoshkaContentProvider.CONTENT_URI, movie.toContentValues());
        provider.insert(KinoshkaContentProvider.CONTENT_URI, movie2.toContentValues());
        Uri uri = KinoshkaContentProvider.CONTENT_URI;
        Cursor cursor = provider.query(uri, null, null, null, null);
        assertNotNull(cursor);
        assertThat(cursor.getCount(), is(2));
    }

    @Test
    public void query_ById() throws Exception {
        assertThat(movieDao.count(), is(0));
        Movie movie = new Movie();
        movie.id = 12345;
        Movie movie2 = new Movie();
        movie.id = 256;
        provider.insert(KinoshkaContentProvider.CONTENT_URI, movie.toContentValues());
        provider.insert(KinoshkaContentProvider.CONTENT_URI, movie2.toContentValues());
        Uri uri = KinoshkaContentProvider.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.id)).build();
        Cursor cursor = provider.query(uri, null, null, null, null);
        assertNotNull(cursor);
        assertThat(cursor.getCount(), is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void query_unknownUri() throws Exception {
        assertThat(movieDao.count(), is(0));
        Uri uri = Uri.parse("content://" + "unknownUri");
        provider.query(uri, null, null, null, null);
    }

    @Test
    public void getType() throws Exception {
    }

    @Test
    public void insert() throws Exception {
        assertThat(movieDao.count(), is(0));
        Movie movie = new Movie();
        movie.id = 12345;
        movie.originalTitle = "title";
        Uri result = provider.insert(KinoshkaContentProvider.CONTENT_URI, movie.toContentValues());
        assertNotNull(result);
        assertThat(movieDao.count(), is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void insert_wrongUri() throws Exception {
        assertThat(movieDao.count(), is(0));
        Movie movie = new Movie();
        Uri uri = KinoshkaContentProvider.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.id)).build();
        provider.insert(uri, movie.toContentValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void insert_unknownUri() throws Exception {
        assertThat(movieDao.count(), is(0));
        Movie movie = new Movie();
        Uri uri = Uri.parse("content://" + "unknownUri");
        provider.insert(uri, movie.toContentValues());
    }

    @Test
    public void delete() throws Exception {
        assertThat(movieDao.count(), is(0));
        Movie movie = new Movie();
        movie.id = 12345;
        provider.insert(KinoshkaContentProvider.CONTENT_URI, movie.toContentValues());
        assertThat(movieDao.count(), is(1));

        Uri uri = KinoshkaContentProvider.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.id)).build();
        int countDeleted = provider.delete(uri, null, null);
        assertEquals(1, countDeleted);
        assertThat(movieDao.count(), is(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_wrongUri() throws Exception {
        assertThat(movieDao.count(), is(0));
        Movie movie = new Movie();
        movie.id = 12345;
        provider.insert(KinoshkaContentProvider.CONTENT_URI, movie.toContentValues());
        assertThat(movieDao.count(), is(1));

        Uri uri = KinoshkaContentProvider.CONTENT_URI;
        provider.delete(uri, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_unknownUri() throws Exception {
        assertThat(movieDao.count(), is(0));
        Movie movie = new Movie();
        movie.id = 12345;
        provider.insert(KinoshkaContentProvider.CONTENT_URI, movie.toContentValues());
        assertThat(movieDao.count(), is(1));
        Uri uri = Uri.parse("content://" + "unknownUri");
        provider.delete(uri, null, null);
    }

    @Test
    public void update() throws Exception {
        assertThat(movieDao.count(), is(0));
        Movie movie = new Movie();
        movie.id = 12345;
        provider.insert(KinoshkaContentProvider.CONTENT_URI, movie.toContentValues());

        movie.originalTitle = "title";
        Uri uri = KinoshkaContentProvider.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.id)).build();

        int count = provider.update(uri, movie.toContentValues(), null, null);
        assertEquals(1, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_wrongUri() throws Exception {
        assertThat(movieDao.count(), is(0));
        Movie movie = new Movie();
        movie.id = 12345;
        provider.insert(KinoshkaContentProvider.CONTENT_URI, movie.toContentValues());

        movie.originalTitle = "title";
        Uri uri = KinoshkaContentProvider.CONTENT_URI;
        provider.update(uri, movie.toContentValues(), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_unknownUri() throws Exception {
        assertThat(movieDao.count(), is(0));
        Movie movie = new Movie();
        movie.id = 12345;
        provider.insert(KinoshkaContentProvider.CONTENT_URI, movie.toContentValues());

        movie.originalTitle = "title";
        Uri uri = Uri.parse("content://" + "unknownUri");
        provider.update(uri, movie.toContentValues(), null, null);
    }


}