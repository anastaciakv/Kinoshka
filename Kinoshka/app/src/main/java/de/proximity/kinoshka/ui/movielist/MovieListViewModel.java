package de.proximity.kinoshka.ui.movielist;


import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

public class MovieListViewModel extends ViewModel {
    @Inject
    public MovieListViewModel() {
    }

    public boolean showList() {
        return false;
    }


}
