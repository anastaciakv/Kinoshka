package de.proximity.kinoshka.di;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import de.proximity.kinoshka.provider.KinoshkaContentProvider;

@Subcomponent
public interface ProviderSubComponent extends AndroidInjector<KinoshkaContentProvider> {
    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<KinoshkaContentProvider> {
    }
}
