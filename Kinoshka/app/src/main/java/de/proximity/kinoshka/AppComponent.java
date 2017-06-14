package de.proximity.kinoshka;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import de.proximity.kinoshka.data.remote.ApiClient;
import de.proximity.kinoshka.data.remote.NetworkModule;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    Context getContext();

    ApiClient getApiCient();
}
