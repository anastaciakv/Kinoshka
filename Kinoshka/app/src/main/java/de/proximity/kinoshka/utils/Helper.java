package de.proximity.kinoshka.utils;


import android.app.Activity;
import android.util.DisplayMetrics;

public class Helper {
    public static int getNumberOfColumns(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }
}
