package de.proximity.kinoshka.ui;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;

import org.parceler.Parcels;

import de.proximity.kinoshka.entity.Movie;
import de.proximity.kinoshka.ui.moviedetails.MovieDetailsActivity;

public class NavigationController {
    public static final String IS_FROM_FAVORITES_KEY = "IS_FROM_FAVORITES_KEY";

    private static void navigateToMovie(Activity activity, Movie movie, View v, boolean isFromFavorites) {
        Intent intent = new Intent(activity, MovieDetailsActivity.class);

        Bundle extras = new Bundle();
        extras.putBoolean(IS_FROM_FAVORITES_KEY, isFromFavorites);
        extras.putParcelable(Movie.ITEM_KEY, Parcels.wrap(movie));
        extras.putString(Movie.TRANSITION_NAME_KEY, ViewCompat.getTransitionName(v));
        intent.putExtras(extras);

        Bundle bundle = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            bundle = ActivityOptions.makeSceneTransitionAnimation(activity, v,
                    v.getTransitionName()).toBundle();
        }

        activity.startActivity(intent, bundle);
    }

    public static void navigateToMovieDetailsFromFavorites(Activity activity, Movie movie, View v) {
        navigateToMovie(activity, movie, v, true);
    }

    public static void navigateToMovieDetails(Activity activity, Movie movie, View v) {
        navigateToMovie(activity, movie, v, false);
    }
}
