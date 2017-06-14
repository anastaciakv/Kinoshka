package de.proximity.kinoshka.movielist;


import dagger.Module;
import dagger.Provides;
import de.proximity.kinoshka.data.MovieTask;
import de.proximity.kinoshka.data.remote.ApiClient;
import de.proximity.kinoshka.data.remote.MovieTaskImpl;

@Module
public class MovieListModule {
    private final MovieListContract.View view;

    public MovieListModule(MovieListContract.View view) {
        this.view = view;
    }

    @Provides
    MovieListContract.View provideMovieListView() {
        return view;
    }

    @Provides
    MovieTask provideMovieTask(ApiClient apiClient) {
        return new MovieTaskImpl(apiClient);
    }

    @Provides
    MovieListContract.Presenter providesMovieListPresenter(MovieTask movieTask) {
        return new MovieListPresenter(view, movieTask);
    }
}
