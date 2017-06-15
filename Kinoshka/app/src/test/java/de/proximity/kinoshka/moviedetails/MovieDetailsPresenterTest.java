package de.proximity.kinoshka.moviedetails;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.proximity.kinoshka.data.remote.NetworkModule;
import de.proximity.kinoshka.entity.Movie;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class MovieDetailsPresenterTest {
    @Mock
    MovieDetailsContract.View view;
    MovieDetailsPresenter presenter;
    Movie movie;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        movie = new Movie();
        movie.originalTitle = "Movie Original Title";
        movie.posterPath = "some_path";
        movie.voteAverage = 4.7;
        movie.overview = "Some movie description";
        movie.releaseDate = "13/06/2016";

        presenter = new MovieDetailsPresenter(view, movie);
    }

    @Test
    public void setMovie() throws Exception {
        assertEquals(movie, presenter.movie);
    }

    @Test
    public void setMovie_Title() throws Exception {
        presenter.start();
        verify(view).setMovieTitle(movie.originalTitle);
    }

    @Test
    public void setMovie_Description() throws Exception {
        presenter.start();
        verify(view).setMovieDescription(movie.overview);
    }

    @Test
    public void setMovie_Poster() throws Exception {
        presenter.start();
        String imgUrl = NetworkModule.getImageUrl(NetworkModule.SupportedImageSize.w342, movie.posterPath);
        verify(view).setMoviePoster(imgUrl);
    }

    @Test
    public void setMovie_ReleaseDate() throws Exception {
        presenter.start();
        verify(view).setMovieReleaseDate(movie.releaseDate);
    }

    @Test
    public void setMovie_Rating() throws Exception {
        presenter.start();
        verify(view).setMovieRating(String.valueOf(movie.voteAverage));
    }

    @Test
    public void when_PosterClicked_then_showBigPoster() throws Exception {
        presenter.onPosterClicked();
        String imgUrl = NetworkModule.getImageUrl(NetworkModule.SupportedImageSize.w780, movie.posterPath);
        verify(view).displayBigPoster(imgUrl);
    }

    @Test
    public void when_BigPosterClicked_then_hideBigPoster() throws Exception {
        presenter.onBigPosterClicked();
        verify(view).hideBigPoster();
    }
}