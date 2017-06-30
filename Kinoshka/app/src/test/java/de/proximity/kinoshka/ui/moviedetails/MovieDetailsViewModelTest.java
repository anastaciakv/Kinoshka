package de.proximity.kinoshka.ui.moviedetails;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import de.proximity.kinoshka.data.MovieTask;
import de.proximity.kinoshka.data.remote.ServerResponse;
import de.proximity.kinoshka.entity.Movie;
import de.proximity.kinoshka.entity.Review;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;

public class MovieDetailsViewModelTest {
    private static final int MOVIE_ID = 23;
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Captor
    ArgumentCaptor<MovieTask.MovieTaskCallback> movieTaskCallbackArgumentCaptor;

    Movie movie;
    @Mock
    MovieTask movieTask;

    MovieDetailsViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        viewModel = new MovieDetailsViewModel(movieTask);
        movie = new Movie();
        movie.id = MOVIE_ID;
    }

    @Test
    public void setMovie() throws Exception {
        viewModel.setMovie(movie);
        assertEquals(movie, viewModel.movie);
    }

    @Test
    public void when_setMovie_then_fetchReviews() throws Exception {
        viewModel.setMovie(movie);
        verify(movieTask).fetchReviews(anyInt(), any(MovieTask.MovieTaskCallback.class));
    }

    @Test
    public void when_fetchReviewsSuccess_then_showReviews() throws Exception {
        viewModel.isReviewAvailable.set(false);
        viewModel.setMovie(movie);
        verify(movieTask).fetchReviews(anyInt(), movieTaskCallbackArgumentCaptor.capture());
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review());
        ServerResponse<Review> response = new ServerResponse<>();
        response.items = reviews;
        movieTaskCallbackArgumentCaptor.getValue().onSuccess(response);
        assertTrue(viewModel.isReviewAvailable.get());
    }

    @Test
    public void when_fetchReviewsSuccessButNoReviews_then_hideReviews() throws Exception {
        viewModel.isReviewAvailable.set(false);
        viewModel.setMovie(movie);
        verify(movieTask).fetchReviews(anyInt(), movieTaskCallbackArgumentCaptor.capture());
        List<Review> reviews = new ArrayList<>();
        ServerResponse<Review> response = new ServerResponse<>();
        response.items = reviews;
        movieTaskCallbackArgumentCaptor.getValue().onSuccess(response);
        assertFalse(viewModel.isReviewAvailable.get());
    }

    @Test
    public void when_fetchReviewsSuccessButReviewsNull_then_hideReviews() throws Exception {
        viewModel.isReviewAvailable.set(false);
        viewModel.setMovie(movie);
        verify(movieTask).fetchReviews(anyInt(), movieTaskCallbackArgumentCaptor.capture());

        ServerResponse<Review> response = new ServerResponse<>();
        response.items = null;
        movieTaskCallbackArgumentCaptor.getValue().onSuccess(response);
        assertFalse(viewModel.isReviewAvailable.get());
    }

    @Test
    public void when_fetchReviewsError_then_hideReviews() throws Exception {
        viewModel.isReviewAvailable.set(true);
        viewModel.setMovie(movie);
        verify(movieTask).fetchReviews(anyInt(), movieTaskCallbackArgumentCaptor.capture());

        movieTaskCallbackArgumentCaptor.getValue().onError();
        assertFalse(viewModel.isReviewAvailable.get());
    }

}