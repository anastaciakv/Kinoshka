package de.proximity.kinoshka.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import de.proximity.kinoshka.ui.favorites.FavoritesActivity;
import de.proximity.kinoshka.ui.moviedetails.MovieDetailsActivity;
import de.proximity.kinoshka.ui.movielist.MovieListActivity;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract MovieListActivity contributeMainActivity();

    @ContributesAndroidInjector
    abstract MovieDetailsActivity contributeMovieDetailsActivity();

    @ContributesAndroidInjector
    abstract FavoritesActivity contributeFavoritesActivity();
}
