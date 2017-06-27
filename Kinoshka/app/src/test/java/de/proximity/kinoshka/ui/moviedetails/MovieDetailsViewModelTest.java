package de.proximity.kinoshka.ui.moviedetails;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.proximity.kinoshka.entity.Movie;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class MovieDetailsViewModelTest {
    @Mock
    Movie movie;
    MovieDetailsViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        viewModel = new MovieDetailsViewModel();
    }

    @Test
    public void setMovie() throws Exception {
        viewModel.setMovie(movie);
        assertEquals(movie, viewModel.movie);
    }

    @Test
    public void onPosterClicked() throws Exception {
        viewModel.isBigPosterVisible.set(false);
        viewModel.onPosterClicked();
        assertTrue(viewModel.isBigPosterVisible.get());
    }

    @Test
    public void onBigPosterClicked() throws Exception {
        viewModel.isBigPosterVisible.set(true);
        viewModel.onBigPosterClicked();
        assertFalse(viewModel.isBigPosterVisible.get());
    }

}