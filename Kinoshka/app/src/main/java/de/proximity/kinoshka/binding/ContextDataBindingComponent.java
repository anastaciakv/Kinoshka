package de.proximity.kinoshka.binding;


import android.content.Context;

public class ContextDataBindingComponent implements android.databinding.DataBindingComponent {
    private final ContextBindingAdapters adapter;

    public ContextDataBindingComponent(Context context) {
        this.adapter = new ContextBindingAdapters(context);
    }

    @Override
    public ContextBindingAdapters getContextBindingAdapters() {
        return adapter;
    }
}
