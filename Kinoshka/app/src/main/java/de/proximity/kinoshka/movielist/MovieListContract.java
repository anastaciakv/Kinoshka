package de.proximity.kinoshka.movielist;


import java.util.List;

import de.proximity.kinoshka.entity.Movie;
import de.proximity.kinoshka.utils.BasePresenter;
import de.proximity.kinoshka.utils.BaseView;

public interface MovieListContract {
    interface View extends BaseView<Presenter> {

        void showProgress(boolean show);

        void showEmptyView(boolean show);

        void showList(boolean show);

        void updateMovieList(List<Movie> movies);

        void navigateToMovieDetails(Movie movie);
    }

    interface Presenter extends BasePresenter {

        void onMovieClicked(int clickedItemIndex);

        void onListScrolled(int visibleItemCount, int totalItemCount, int firstVisibleItemPosition);
    }
}
