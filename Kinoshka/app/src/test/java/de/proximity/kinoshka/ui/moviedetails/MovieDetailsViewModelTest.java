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
import de.proximity.kinoshka.entity.Trailer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
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
        verify(movieTask).fetchReviews(anyLong(), any(MovieTask.MovieTaskCallback.class));
    }

    @Test
    public void when_fetchReviewsSuccess_then_showReviews() throws Exception {
        viewModel.isReviewAvailable.set(false);
        viewModel.setMovie(movie);
        verify(movieTask).fetchReviews(anyLong(), movieTaskCallbackArgumentCaptor.capture());
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
        verify(movieTask).fetchReviews(anyLong(), movieTaskCallbackArgumentCaptor.capture());
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
        verify(movieTask).fetchReviews(anyLong(), movieTaskCallbackArgumentCaptor.capture());

        ServerResponse<Review> response = new ServerResponse<>();
        response.items = null;
        movieTaskCallbackArgumentCaptor.getValue().onSuccess(response);
        assertFalse(viewModel.isReviewAvailable.get());
    }

    @Test
    public void when_fetchReviewsError_then_hideReviews() throws Exception {
        viewModel.isReviewAvailable.set(true);
        viewModel.setMovie(movie);
        verify(movieTask).fetchReviews(anyLong(), movieTaskCallbackArgumentCaptor.capture());

        movieTaskCallbackArgumentCaptor.getValue().onError();
        assertFalse(viewModel.isReviewAvailable.get());
    }

    @Test
    public void given_hasVideos_when_setMovie_then_fetchVideos() {
        movie.video = true;
        viewModel.setMovie(movie);
        verify(movieTask).fetchTrailers(anyLong(), any(MovieTask.MovieTaskCallback.class));
    }

    @Test
    public void given_hasNoVideos_when_setMovie_then_doNotFetchVideos() {
        movie.video = false;
        viewModel.setMovie(movie);
        verify(movieTask, times(0)).fetchTrailers(anyLong(), any(MovieTask.MovieTaskCallback.class));
    }

    @Test
    public void when_fetchTrailersSuccess_then_showTrailers() throws Exception {
        viewModel.isTrailerAvailable.set(false);
        movie.video = true;
        viewModel.setMovie(movie);
        verify(movieTask).fetchTrailers(anyLong(), movieTaskCallbackArgumentCaptor.capture());
        List<Trailer> trailers = new ArrayList<>();
        trailers.add(new Trailer());
        ServerResponse<Trailer> response = new ServerResponse<>();
        response.items = trailers;
        movieTaskCallbackArgumentCaptor.getValue().onSuccess(response);
        assertTrue(viewModel.isTrailerAvailable.get());
        assertEquals(trailers, viewModel.getTrailers().getValue());
    }

    @Test
    public void when_fetchTrailersSuccessButNoTrailers_then_hideTrailers() throws Exception {
        viewModel.isTrailerAvailable.set(false);
        movie.video = true;
        viewModel.setMovie(movie);
        verify(movieTask).fetchTrailers(anyLong(), movieTaskCallbackArgumentCaptor.capture());
        List<Trailer> trailers = new ArrayList<>();
        ServerResponse<Trailer> response = new ServerResponse<>();
        response.items = trailers;
        movieTaskCallbackArgumentCaptor.getValue().onSuccess(response);
        assertFalse(viewModel.isTrailerAvailable.get());
    }

    @Test
    public void when_fetchTrailersSuccessButTrailersNull_then_hideTrailers() throws Exception {
        viewModel.isTrailerAvailable.set(false);
        movie.video = true;
        viewModel.setMovie(movie);
        verify(movieTask).fetchTrailers(anyLong(), movieTaskCallbackArgumentCaptor.capture());

        ServerResponse<Trailer> response = new ServerResponse<>();
        response.items = null;
        movieTaskCallbackArgumentCaptor.getValue().onSuccess(response);
        assertFalse(viewModel.isTrailerAvailable.get());
    }

    @Test
    public void when_fetchTrailersError_then_hideTrailers() throws Exception {
        viewModel.isTrailerAvailable.set(true);
        movie.video = true;
        viewModel.setMovie(movie);
        verify(movieTask).fetchTrailers(anyLong(), movieTaskCallbackArgumentCaptor.capture());

        movieTaskCallbackArgumentCaptor.getValue().onError();
        assertFalse(viewModel.isTrailerAvailable.get());
    }

    @Test
    public void given_notFavorite_when_onFavoritesClicked_then_isFavoriteTrue() {
        viewModel.isFavorite.set(false);
        viewModel.onFavoritesClicked(null);
        assertTrue(viewModel.isFavorite.get());
    }

    @Test
    public void given_isFavorite_when_onFavoritesClicked_then_isFavoriteFalse() {
        viewModel.isFavorite.set(true);
        viewModel.onFavoritesClicked(null);
        assertFalse(viewModel.isFavorite.get());
    }

    @Test
    public void given_notFavorite_when_onFavoritesClicked_then_saveToFavorites() {
        viewModel.setMovie(movie);
        viewModel.isFavorite.set(false);
        viewModel.onFavoritesClicked(null);
        verify(movieTask).addToFavorites(movie);
    }

    @Test
    public void given_isFavorite_when_onFavoritesClicked_then_removeFromFavorites() {
        viewModel.setMovie(movie);
        viewModel.isFavorite.set(true);
        viewModel.onFavoritesClicked(null);
        verify(movieTask).removeFromFavorites(movie);
    }

    @Test
    public void when_setMovie_then_checkFavorites() {
        viewModel.setMovie(movie);
        verify(movieTask).checkIsFavorite(movie);
    }

    @Test
    public void given_isFavorite_when_setMovie_then_markFavorite() {
        viewModel.isFavorite.set(false);
        doReturn(true).when(movieTask).checkIsFavorite(movie);
        viewModel.setMovie(movie);
        assertTrue(viewModel.isFavorite.get());
    }

    @Test
    public void given_notFavorite_when_setMovie_then_markNotFavorite() {
        viewModel.isFavorite.set(false);
        doReturn(false).when(movieTask).checkIsFavorite(movie);
        viewModel.setMovie(movie);
        assertFalse(viewModel.isFavorite.get());
    }
}