package de.proximity.kinoshka.ui.favorites;

import android.database.Cursor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.proximity.kinoshka.data.MovieTask;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class FavoritesViewModelTest {
    @Mock
    MovieTask movieTask;
    FavoritesViewModel viewModel;
    @Mock
    Cursor cursor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        viewModel = new FavoritesViewModel(movieTask);
    }

    @After
    public void tearUp() {
        viewModel.cursor = null;
    }

    @Test
    public void getMovies() throws Exception {
    }

    @Test
    public void onStartLoading() throws Exception {
        viewModel.isLoading.set(false);
        viewModel.onStartLoading();
        assertTrue(viewModel.isLoading.get());
    }

    @Test
    public void given_noCursor_when_onLoadFinished_thenHideList() throws Exception {
        viewModel.showList.set(false);
        viewModel.isLoading.set(true);
        viewModel.onLoadFinished();
        assertFalse(viewModel.isLoading.get());
        assertFalse(viewModel.showList.get());
    }

    @Test
    public void given_emptyCursor_when_onLoadFinished_thenHideList() throws Exception {
        viewModel.showList.set(false);
        viewModel.isLoading.set(true);
        viewModel.cursor = cursor;
        doReturn(0).when(cursor).getCount();

        viewModel.onLoadFinished();
        assertFalse(viewModel.isLoading.get());
        assertFalse(viewModel.showList.get());
    }

    @Test
    public void given_CursorWithData_when_onLoadFinished_thenShowList() throws Exception {
        viewModel.showList.set(false);
        viewModel.isLoading.set(true);
        viewModel.cursor = cursor;
        doReturn(10).when(cursor).getCount();

        viewModel.onLoadFinished();
        assertFalse(viewModel.isLoading.get());
        assertTrue(viewModel.showList.get());
    }

    @Test
    public void when_getMovies_thenFetch() throws Exception {
        doReturn(cursor).when(movieTask).fetchMoviesFavorite();
        Cursor actual = viewModel.getMovies();
        verify(movieTask).fetchMoviesFavorite();
        assertEquals(cursor, actual);
    }

}