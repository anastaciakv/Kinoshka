package de.proximity.kinoshka.di;

import android.content.ContentProvider;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.ContentProviderKey;
import dagger.multibindings.IntoMap;
import de.proximity.kinoshka.provider.KinoshkaContentProvider;

@Module(subcomponents = ProviderSubComponent.class)
public abstract class ProviderModule {
    @Binds
    @IntoMap
    @ContentProviderKey(KinoshkaContentProvider.class)
    abstract AndroidInjector.Factory<? extends ContentProvider>
    bindProviderInjectorFactory(ProviderSubComponent.Builder builder);
}
