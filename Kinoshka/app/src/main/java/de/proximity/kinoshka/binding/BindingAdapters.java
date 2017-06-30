/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.proximity.kinoshka.binding;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.proximity.kinoshka.R;

/**
 * Data Binding adapters specific to the app.
 */
public class BindingAdapters {
    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("fadeVisibleGone")
    public static void setFadeVisible(View view, boolean visible) {
        animateVisibility(view, visible, View.GONE);
    }

    @BindingAdapter("fadeVisibleInvisible")
    public static void visibleInvisible(View view, boolean visible) {
        animateVisibility(view, visible, View.INVISIBLE);
    }

    private static void animateVisibility(View view, boolean visible, int hideVisibility) {
        if (view.getTag() == null) {
            view.setTag(true);
            view.setVisibility(visible ? View.VISIBLE : hideVisibility);
        } else {
            view.animate().cancel();
            if (visible) {
                view.setVisibility(View.VISIBLE);
                // view.setAlpha(0);
                view.animate().setDuration(300).alpha(1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setAlpha(1);
                    }
                });

            } else {
                view.setAlpha(1);
                view.setVisibility(View.VISIBLE);
                view.animate().setDuration(300).alpha(0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setAlpha(0);
                        view.setVisibility(hideVisibility);
                    }
                });
            }
        }
    }

    @BindingAdapter("imageUrl")
    public static void bindImage(ImageView imageView, String url) {
        loadImage(imageView, url, null);
    }

    @BindingAdapter({"imageUrl", "callback"})
    public static void bindImage(ImageView imageView, String url, ImageReadyCallback callback) {
        loadImage(imageView, url, callback);
    }

    private static void loadImage(ImageView imageView, String url, ImageReadyCallback callback) {
        Picasso.with(imageView.getContext())
                .load(url).noFade()
                .error(ContextCompat.getDrawable(imageView.getContext(), R.drawable.ic_image))
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (callback != null) callback.onImageReady();
                    }

                    @Override
                    public void onError() {
                        if (callback != null) callback.onImageReady();
                    }
                });
    }
}
