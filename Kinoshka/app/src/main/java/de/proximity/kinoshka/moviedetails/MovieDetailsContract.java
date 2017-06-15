package de.proximity.kinoshka.moviedetails;


import de.proximity.kinoshka.utils.BasePresenter;
import de.proximity.kinoshka.utils.BaseView;

public interface MovieDetailsContract {
    public interface View extends BaseView<Presenter> {

        void setMovieTitle(String title);

        void setMovieDescription(String overview);

        void setMoviePoster(String imgUrl);

        void setMovieReleaseDate(String releaseDate);

        void setMovieRating(String rating);

        void displayBigPoster(String imgUrl);

        void hideBigPoster();
    }

    public interface Presenter extends BasePresenter {

        void onPosterClicked();

        void onBigPosterClicked();
    }
}
