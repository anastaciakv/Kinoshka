package de.proximity.kinoshka.di;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import de.proximity.kinoshka.moviedetails.MovieDetailsViewModel;
import de.proximity.kinoshka.viewmodel.KinoshkaViewModelFactory;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel.class)
    abstract ViewModel bindMovieDetailsViewModel(MovieDetailsViewModel userViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(KinoshkaViewModelFactory factory);
}
