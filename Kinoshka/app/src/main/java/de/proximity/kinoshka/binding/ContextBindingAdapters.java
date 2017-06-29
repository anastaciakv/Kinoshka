package de.proximity.kinoshka.binding;


import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import de.proximity.kinoshka.R;

public class ContextBindingAdapters {
    final Context context;

    @Inject
    public ContextBindingAdapters(Context context) {
        this.context = context;
    }

    @BindingAdapter("imageUrl")
    public void bindImage(ImageView imageView, String url) {
        Picasso.with(context)
                .load(url)
                .error(ContextCompat.getDrawable(context, R.drawable.ic_image))
                .into(imageView);
    }
}
