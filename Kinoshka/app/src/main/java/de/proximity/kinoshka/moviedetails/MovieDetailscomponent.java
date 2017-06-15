package de.proximity.kinoshka.moviedetails;

import dagger.Component;

@Component(modules = MovieDetailsModule.class)
public interface MovieDetailscomponent {
    void inject(MovieDetailsActivity activity);
}
