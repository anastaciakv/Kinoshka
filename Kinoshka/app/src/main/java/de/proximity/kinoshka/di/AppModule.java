package de.proximity.kinoshka.di;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static dagger.internal.Preconditions.checkNotNull;

@Module(includes = ViewModelModule.class)
public class AppModule {
    private Context appContext;

    public AppModule(@NonNull Context context) {
        appContext = checkNotNull(context);
    }

    @Provides
    @Singleton
    public Context providesContext() {
        return appContext;
    }
}
