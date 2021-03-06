package de.proximity.kinoshka.ui.movielist;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import de.proximity.kinoshka.data.MovieTask;
import de.proximity.kinoshka.data.remote.ServerResponse;
import de.proximity.kinoshka.entity.Movie;
import de.proximity.kinoshka.movielist.testutils.TestUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(JUnit4.class)
public class MovieListViewModelTest {

    @Mock
    MovieTask movieTask;
    @Captor
    ArgumentCaptor<MovieTask.MovieTaskCallback> movieTaskCallbackArgumentCaptor;
    MovieListViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        viewModel = new MovieListViewModel(movieTask);
    }

    @Test
    public void when_VMcreated_then_fetchMovies() throws Exception {
        verify(movieTask).fetchMovies(anyString(), anyInt(), any(MovieTask.MovieTaskCallback.class));
    }

    @Test
    public void when_VMcreated_then_defaultSortModeIsPopular() throws Exception {
        assertEquals(Movie.SortMode.mostPopular, viewModel.currentSortMode);
    }

    @Test
    public void getMovieTaskCallbackNotNull() throws Exception {
        assertNotNull(viewModel.getMovieTaskCallback());
    }

    @Test
    public void when_fetchMovies_then_isLoadingTrue() throws Exception {
        viewModel.fetchMovies();
        assertTrue(viewModel.isLoading.get());
    }

    @Test
    public void when_fetchMoviesSuccess_then_LoadingFalseShowListTrue() throws Exception {
        verify(movieTask).fetchMovies(anyString(), anyInt(), movieTaskCallbackArgumentCaptor.capture());
        movieTaskCallbackArgumentCaptor.getValue().onSuccess(getPopularMovies());

        assertFalse(viewModel.isLoading.get());
        assertTrue(viewModel.showList.get());
    }

    @Test
    public void when_fetchMoviesFail_then_IsLoadingFalseShowListFalse() throws Exception {
        verify(movieTask).fetchMovies(anyString(), anyInt(), movieTaskCallbackArgumentCaptor.capture());
        movieTaskCallbackArgumentCaptor.getValue().onError();

        assertFalse(viewModel.isLoading.get());
        assertFalse(viewModel.showList.get());
    }

    @Test
    public void given_someMoviesAlreadyFetched_when_fetchMoviesFail_then_isLoadingFalseShowListTrue() throws Exception {
        viewModel.movies.set(getPopularMovies().items);
        viewModel.fetchMovies();
        verify(movieTask, times(2)).fetchMovies(anyString(), anyInt(), movieTaskCallbackArgumentCaptor.capture());
        movieTaskCallbackArgumentCaptor.getValue().onError();

        assertFalse(viewModel.isLoading.get());
        assertTrue(viewModel.showList.get());
    }

    @Test
    public void given_currentSortByPopular_when_sortByMostPopular_then_doNothing() throws Exception {
        verify(movieTask).fetchMovies(anyString(), anyInt(), any(MovieTask.MovieTaskCallback.class));
        viewModel.currentSortMode = Movie.SortMode.mostPopular;
        viewModel.onSortByMostPopular();
        verifyZeroInteractions(movieTask);
    }

    @Test
    public void given_currentSortByTopRated_when_sortByTopRated_then_doNothing() throws Exception {
        verify(movieTask).fetchMovies(anyString(), anyInt(), any(MovieTask.MovieTaskCallback.class));
        viewModel.currentSortMode = Movie.SortMode.topRated;
        viewModel.onSortByTopRated();
        verifyZeroInteractions(movieTask);
    }

    @Test
    public void given_currentSortByPopular_when_sortByTopRated_then_UpdateMovieList() throws Exception {
        viewModel.currentSortMode = Movie.SortMode.mostPopular;
        viewModel.movies.set(getPopularMovies().items);
        viewModel.onSortByTopRated();

        assertTrue(viewModel.movies.get().isEmpty());
        verify(movieTask).fetchMovies(Movie.SortMode.topRated, 1, viewModel.getMovieTaskCallback());
    }

    @Test
    public void given_currentSortByTopRated_when_sortByPopular_then_UpdateMovieList() throws Exception {
        viewModel.movies.set(getPopularMovies().items);
        viewModel.currentSortMode = Movie.SortMode.topRated;
        viewModel.onSortByMostPopular();

        assertTrue(viewModel.movies.get().isEmpty());
        verify(movieTask, times(2)).fetchMovies(Movie.SortMode.mostPopular, 1, viewModel.getMovieTaskCallback());
    }

    @Test
    public void given_currentPage5_when_sortChangedToTopRated_then_CurrentPage1() throws Exception {
        viewModel.currentSortMode = Movie.SortMode.mostPopular;
        viewModel.currentPage = 5;
        viewModel.onSortByTopRated();
        assertEquals(1, viewModel.currentPage);
    }

    @Test
    public void given_currentPage5_when_sortChangedToMostPopular_then_CurrentPage1() throws Exception {
        viewModel.currentSortMode = Movie.SortMode.topRated;
        viewModel.currentPage = 5;
        viewModel.onSortByMostPopular();
        assertEquals(1, viewModel.currentPage);
    }

    @Test
    public void given_currentPage5_sortPopular_when_sortNotChanged_then_PageNotChanged() throws Exception {
        viewModel.currentSortMode = Movie.SortMode.mostPopular;
        viewModel.currentPage = 5;
        viewModel.onSortByMostPopular();
        assertEquals(5, viewModel.currentPage);
    }

    @Test
    public void given_currentPage5_sortTopRated_when_sortNotChanged_then_PageNotChanged() throws Exception {
        viewModel.currentSortMode = Movie.SortMode.topRated;
        viewModel.currentPage = 5;
        viewModel.onSortByTopRated();
        assertEquals(5, viewModel.currentPage);
    }

    @Test
    public void islastPage() throws Exception {
        viewModel.totalPages = 12;
        viewModel.currentPage = 2;
        assertFalse(viewModel.isLastPage());
        viewModel.currentPage = 11;
        assertFalse(viewModel.isLastPage());
        viewModel.currentPage = 12;
        assertTrue(viewModel.isLastPage());
    }

    @Test
    public void returnsMovies() {
        List<Movie> movies = getPopularMovies().items;
        viewModel.movies.set(movies);
        assertEquals(movies, viewModel.movies.get());
    }

    @Test
    public void given_needMoreItems_when_scrolled_then_increasePage_andFetch() {
        viewModel.currentPage = 1;
        viewModel.totalPages = 4;
        viewModel.currentSortMode = Movie.SortMode.topRated;
        viewModel.isLoading.set(false);

        viewModel.onListScrolled(4, 20, 16);
        assertEquals(2, viewModel.currentPage);
        verify(movieTask).fetchMovies(Movie.SortMode.topRated, 2, viewModel.getMovieTaskCallback());
    }

    @Test
    public void given_atFirstItem_when_scrolled_then_doNothing() {
        verify(movieTask, times(1)).fetchMovies(Movie.SortMode.mostPopular, 1, viewModel.getMovieTaskCallback());
        viewModel.currentPage = 1;
        viewModel.totalPages = 4;
        viewModel.currentSortMode = Movie.SortMode.topRated;
        viewModel.isLoading.set(false);

        viewModel.onListScrolled(4, 20, 0);

        assertEquals(1, viewModel.currentPage);
        verifyZeroInteractions(movieTask);
    }

    @Test
    public void given_isLoading_when_scrolled_then_doNothing() {
        verify(movieTask, times(1)).fetchMovies(Movie.SortMode.mostPopular, 1, viewModel.getMovieTaskCallback());
        viewModel.currentPage = 1;
        viewModel.totalPages = 4;
        viewModel.currentSortMode = Movie.SortMode.topRated;
        viewModel.isLoading.set(true);

        viewModel.onListScrolled(4, 20, 16);

        assertEquals(1, viewModel.currentPage);
        verifyZeroInteractions(movieTask);
    }

    @Test
    public void given_lastPage_when_scrolled_then_doNothing() {
        verify(movieTask, times(1)).fetchMovies(Movie.SortMode.mostPopular, 1, viewModel.getMovieTaskCallback());
        viewModel.currentPage = 1;
        viewModel.totalPages = 1;
        viewModel.currentSortMode = Movie.SortMode.topRated;
        viewModel.isLoading.set(false);

        viewModel.onListScrolled(4, 20, 16);

        assertEquals(1, viewModel.currentPage);
        verifyZeroInteractions(movieTask);
    }

    @Test
    public void given_moreItemsAvailable_when_scrolled_then_doNothing() {
        viewModel.currentSortMode = Movie.SortMode.mostPopular;
        verify(movieTask, times(1)).fetchMovies(Movie.SortMode.mostPopular, 1, viewModel.getMovieTaskCallback());
        viewModel.currentPage = 1;
        viewModel.totalPages = 3;
        viewModel.isLoading.set(false);

        viewModel.onListScrolled(4, 20, 15);

        assertEquals(1, viewModel.currentPage);
        verifyZeroInteractions(movieTask);
    }

    private ServerResponse<Movie> getPopularMovies() {
        String responseJson = TestUtils.loadJSONFromFile(this, "popular-movie-list.json");
        return new Gson().fromJson(responseJson, ServerResponse.class);
    }
}