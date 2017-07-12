package de.proximity.kinoshka;


import android.app.Activity;
import android.app.Application;
import android.content.ContentProvider;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasContentProviderInjector;
import de.proximity.kinoshka.di.AppInjector;
import timber.log.Timber;

public class MyApplication extends Application implements HasActivityInjector, HasContentProviderInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    @Inject
    DispatchingAndroidInjector<ContentProvider> dispatchingAndroidProviderInjector;
    private boolean needInject = true;

    @Override
    public void onCreate() {
        super.onCreate();
        initTimber();
        injectIfNeeded();
    }

    private void injectIfNeeded() {
        if (needInject) {
            AppInjector.init(this);
            needInject = false;
        }
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

    @Override
    public AndroidInjector<ContentProvider> contentProviderInjector() {
        injectIfNeeded();
        return dispatchingAndroidProviderInjector;
    }
}
