package de.proximity.kinoshka.di;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import de.proximity.kinoshka.ui.moviedetails.MovieDetailsFragment;

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract MovieDetailsFragment contributeMovieDetailsFragment();

}
