package de.proximity.kinoshka;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static dagger.internal.Preconditions.checkNotNull;

@Module
class AppModule {
    private Context appContext;

    AppModule(@NonNull Context context) {
        appContext = checkNotNull(context);
    }

    @Provides
    @Singleton
    Context providesContext() {
        return appContext;
    }
}
