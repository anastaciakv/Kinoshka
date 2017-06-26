package de.proximity.kinoshka;


import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import de.proximity.kinoshka.di.AppInjector;
import timber.log.Timber;

public class MyApplication extends Application implements HasActivityInjector {
    public static String THE_MOVIE_DB_API_KEY;
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        THE_MOVIE_DB_API_KEY = getString(R.string.the_movie_db_api_key);
        initTimber();
        AppInjector.init(this);
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
