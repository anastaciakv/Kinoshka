package de.proximity.kinoshka.ui;

import android.support.v4.app.FragmentManager;

import javax.inject.Inject;

import de.proximity.kinoshka.R;
import de.proximity.kinoshka.ui.moviedetails.MovieDetailsFragment;
import de.proximity.kinoshka.ui.movielist.MovieListFragment;

/**
 * A utility class that handles navigation in {@link MainActivity}.
 */
public class NavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;

    @Inject
    public NavigationController(MainActivity mainActivity) {
        this.containerId = R.id.container;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
    }

    public void navigateToMovieList() {
        MovieListFragment fragment = MovieListFragment.newInstance();
        fragmentManager.beginTransaction().replace(containerId, fragment).commit();
    }

    public void navigateToMovieDetails() {
        MovieDetailsFragment fragment = MovieDetailsFragment.newInstance();
        fragmentManager.beginTransaction().replace(containerId, fragment).addToBackStack(null).commit();
    }
}
