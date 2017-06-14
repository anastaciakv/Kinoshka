package de.proximity.kinoshka.movielist;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import de.proximity.kinoshka.data.MovieTask;
import de.proximity.kinoshka.entity.Movie;
import de.proximity.kinoshka.entity.MovieListResponse;
import de.proximity.kinoshka.movielist.testutils.TestUtils;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class MovieListPresenterTest {
    @Mock
    MovieListContract.View view;
    private MovieListPresenter presenter;
    @Mock
    MovieTask movieTask;
    @Captor
    ArgumentCaptor<MovieTask.MovieTaskCallback> movieTaskCallbackArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new MovieListPresenter(view, movieTask);
    }

    @Test
    public void when_start_then_fetchMovies() throws Exception {
        presenter.movies = new ArrayList<>();
        presenter.start();
        verify(movieTask).fetchPopularMovies(anyInt(), any(MovieTask.MovieTaskCallback.class));
    }

    @Test
    public void given_moviesAlreadyFetched_when_start_then_doNothing() throws Exception {
        presenter.movies = getPopularMovies().movies;
        presenter.start();
        verifyZeroInteractions(movieTask);
    }

    @Test
    public void getMovieTaskCallbackNotNull() throws Exception {
        assertNotNull(presenter.getMovieTaskCallback());
    }

    @Test
    public void when_fetchMovies_then_showProgress() throws Exception {
        presenter.fetchPopularMovies();

        verify(view).showProgress(true);
    }

    @Test
    public void when_fetchMoviesSuccess_then_hideProgressAndHideEmptyView() throws Exception {
        presenter.fetchPopularMovies();
        verify(movieTask).fetchPopularMovies(anyInt(), movieTaskCallbackArgumentCaptor.capture());
        movieTaskCallbackArgumentCaptor.getValue().onMovieListFetched(getPopularMovies());

        verify(view).showProgress(false);
        verify(view).showEmptyView(false);
        verify(view).showList(true);
    }

    @Test
    public void when_fetchMoviesFail_then_hideProgressAndShowEmptyView() throws Exception {
        presenter.fetchPopularMovies();
        verify(movieTask).fetchPopularMovies(anyInt(), movieTaskCallbackArgumentCaptor.capture());
        movieTaskCallbackArgumentCaptor.getValue().onMovieListFetchError();

        verify(view).showProgress(false);
        verify(view).showEmptyView(true);
        verify(view).showList(false);
    }

    @Test
    public void when_fetchMoviesSuccess_then_updateMoviewList() throws Exception {
        presenter.fetchPopularMovies();
        verify(movieTask).fetchPopularMovies(anyInt(), movieTaskCallbackArgumentCaptor.capture());
        MovieListResponse response = getPopularMovies();
        movieTaskCallbackArgumentCaptor.getValue().onMovieListFetched(response);

        verify(view).updateMovieList(response.movies);
    }

    @Test
    public void when_itemClicked_then_navigateToMovieDetails() throws Exception {
        List<Movie> movieList = getPopularMovies().movies;
        presenter.movies = movieList;
        presenter.onMovieClicked(2, v);
        verify(view).navigateToMovieDetails(movieList.get(2), v);
    }


    private MovieListResponse getPopularMovies() {
        String responseJson = TestUtils.loadJSONFromFile(this, "popular-movie-list.json");
        return new Gson().fromJson(responseJson, MovieListResponse.class);
    }
}