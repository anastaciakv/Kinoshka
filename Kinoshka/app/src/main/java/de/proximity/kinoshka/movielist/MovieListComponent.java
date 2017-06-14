package de.proximity.kinoshka.movielist;

import dagger.Component;
import de.proximity.kinoshka.AppComponent;
import de.proximity.kinoshka.utils.ScreenScope;

@ScreenScope
@Component(dependencies = AppComponent.class, modules = MovieListModule.class)
public interface MovieListComponent {
    void inject(MovieListActivity activity);
}
