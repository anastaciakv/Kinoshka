package de.proximity.kinoshka;


import android.app.Application;

import de.proximity.kinoshka.data.remote.NetworkModule;
import timber.log.Timber;

public class MyApplication extends Application {
    public static String THE_MOVIE_DB_API_KEY;
    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        THE_MOVIE_DB_API_KEY = getString(R.string.the_movie_db_api_key);
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .networkModule(new NetworkModule())
                .build();
        initTimber();
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
