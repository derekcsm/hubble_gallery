package com.derek_s.hubble_gallery.utils.ui;

import android.content.Context;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperToast;

public class Toasty {

    public static int LENGTH_LONG = 3000;
    public static int LENGTH_MEDIUM = 2000;
    public static int LENGTH_SHORT = 1000;

    public static void show(Context c, String text, int duration) {
        SuperToast
                .create(c, text, duration, Style.grey())
                .setAnimations(Style.ANIMATIONS_POP)
                .show();
    }

    public static void show(Context c, int textRes, int duration) {
        SuperToast
                .create(c, c.getString(textRes), duration, Style.grey())
                .setAnimations(Style.ANIMATIONS_POP)
                .show();
    }
}