package de.proximity.kinoshka.ui.moviedetails;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.proximity.kinoshka.entity.Movie;

import static junit.framework.Assert.assertEquals;

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

}