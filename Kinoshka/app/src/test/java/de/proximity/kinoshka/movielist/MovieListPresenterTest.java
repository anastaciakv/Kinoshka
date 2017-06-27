package de.proximity.kinoshka.movielist;

public class MovieListPresenterTest {
 /*   @Mock
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
        verify(movieTask).fetchMovies(anyInt(), anyInt(), any(MovieTask.MovieTaskCallback.class));
        assertEquals(Movie.SortMode.mostPopular, presenter.currentSortMode);
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
        presenter.fetchMovies();

        verify(view).showProgress(true);
    }

    @Test
    public void when_fetchMoviesSuccess_then_hideProgressAndHideEmptyView() throws Exception {
        presenter.fetchMovies();
        verify(movieTask).fetchMovies(anyInt(), anyInt(), movieTaskCallbackArgumentCaptor.capture());
        movieTaskCallbackArgumentCaptor.getValue().onMovieListFetched(getPopularMovies());

        verify(view).showProgress(false);
        verify(view).showEmptyView(false);
        verify(view).showList(true);
    }

    @Test
    public void when_fetchMoviesFail_then_hideProgressAndShowEmptyView() throws Exception {
        presenter.fetchMovies();
        verify(movieTask).fetchMovies(anyInt(), anyInt(), movieTaskCallbackArgumentCaptor.capture());
        movieTaskCallbackArgumentCaptor.getValue().onMovieListFetchError();

        verify(view).showProgress(false);
        verify(view).showEmptyView(true);
        verify(view).showList(false);
    }

    @Test
    public void given_someMoviesAlreadyFetched_when_fetchMoviesFail_then_hideProgress() throws Exception {
        presenter.movies = getPopularMovies().movies;
        presenter.fetchMovies();
        verify(movieTask).fetchMovies(anyInt(), anyInt(), movieTaskCallbackArgumentCaptor.capture());
        movieTaskCallbackArgumentCaptor.getValue().onMovieListFetchError();

        verify(view).showProgress(false);
        verify(view).showEmptyView(false);
        verify(view).showList(true);
    }

    @Test
    public void when_fetchMoviesSuccess_then_updateMoviewList() throws Exception {
        presenter.fetchMovies();
        verify(movieTask).fetchMovies(anyInt(), anyInt(), movieTaskCallbackArgumentCaptor.capture());
        MovieListResponse response = getPopularMovies();
        movieTaskCallbackArgumentCaptor.getValue().onMovieListFetched(response);

        verify(view).updateMovieList(response.movies);
    }

    @Test
    public void when_itemClicked_then_navigateToMovieDetails() throws Exception {
        List<Movie> movieList = getPopularMovies().movies;
        presenter.movies = movieList;
        presenter.onMovieClicked(2, null);
        verify(view).navigateToMovieDetails(movieList.get(2), null);
    }

    @Test
    public void given_currentSortByPopular_when_sortByMostPopular_then_doNothing() throws Exception {
        presenter.currentSortMode = Movie.SortMode.mostPopular;
        presenter.onSortByMostPopular();
        verifyZeroInteractions(movieTask);
    }

    @Test
    public void given_currentSortByTopRated_when_sortByTopRated_then_doNothing() throws Exception {
        presenter.currentSortMode = Movie.SortMode.topRated;
        presenter.onSortByTopRated();
        verifyZeroInteractions(movieTask);
    }

    @Test
    public void given_currentSortByPopular_when_sortByTopRated_then_UpdateMovieList() throws Exception {
        presenter.currentSortMode = Movie.SortMode.mostPopular;
        presenter.movies = getPopularMovies().movies;
        presenter.onSortByTopRated();
        verify(view).clearMovieList();
        assertTrue(presenter.movies.isEmpty());
        verify(movieTask).fetchMovies(Movie.SortMode.topRated, 1, presenter.getMovieTaskCallback());
    }

    @Test
    public void given_currentSortByTopRated_when_sortByPopular_then_UpdateMovieList() throws Exception {
        presenter.movies = getPopularMovies().movies;
        presenter.currentSortMode = Movie.SortMode.topRated;
        presenter.onSortByMostPopular();
        verify(view).clearMovieList();
        assertTrue(presenter.movies.isEmpty());
        verify(movieTask).fetchMovies(Movie.SortMode.mostPopular, 1, presenter.getMovieTaskCallback());
    }

    @Test
    public void given_currentPage5_when_sortChangedToTopRated_then_CurrentPage1() throws Exception {
        presenter.currentSortMode = Movie.SortMode.mostPopular;
        presenter.currentPage = 5;
        presenter.onSortByTopRated();
        assertEquals(1, presenter.currentPage);
    }

    @Test
    public void given_currentPage5_when_sortChangedToMostPopular_then_CurrentPage1() throws Exception {
        presenter.currentSortMode = Movie.SortMode.topRated;
        presenter.currentPage = 5;
        presenter.onSortByMostPopular();
        assertEquals(1, presenter.currentPage);
    }

    @Test
    public void given_currentPage5_sortPopular_when_sortNotChanged_then_PageNotChanged() throws Exception {
        presenter.currentSortMode = Movie.SortMode.mostPopular;
        presenter.currentPage = 5;
        presenter.onSortByMostPopular();
        assertEquals(5, presenter.currentPage);
    }

    @Test
    public void given_currentPage5_sortTopRated_when_sortNotChanged_then_PageNotChanged() throws Exception {
        presenter.currentSortMode = Movie.SortMode.topRated;
        presenter.currentPage = 5;
        presenter.onSortByTopRated();
        assertEquals(5, presenter.currentPage);
    }

    private MovieListResponse getPopularMovies() {
        String responseJson = TestUtils.loadJSONFromFile(this, "popular-movie-list.json");
        return new Gson().fromJson(responseJson, MovieListResponse.class);
    }*/
}