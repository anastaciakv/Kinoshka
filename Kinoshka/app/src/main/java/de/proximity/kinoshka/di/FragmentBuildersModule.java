package de.proximity.kinoshka.di;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import de.proximity.kinoshka.ui.moviedetails.MovieDetailsFragment;
import de.proximity.kinoshka.ui.movielist.MovieListFragment;

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract MovieDetailsFragment contributeMovieDetailsFragment();

    @ContributesAndroidInjector
    abstract MovieListFragment contributeMovieListFragment();
}
