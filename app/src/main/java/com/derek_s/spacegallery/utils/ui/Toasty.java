package com.derek_s.spacegallery.utils.ui;

import android.content.Context;

import com.github.johnpersano.supertoasts.SuperToast;

/**
 * Created by dereksmith on 15-04-11.
 */
public class Toasty {

    public static int LENGTH_LONG = 3000;
    public static int LENGTH_MEDIUM = 2000;
    public static int LENGTH_SHORT = 1000;

    public static void show(Context c, String text, int duration) {
        SuperToast superToast = new SuperToast(c);
        superToast.setDuration(duration);
        superToast.setText(text);
        superToast.setAnimations(SuperToast.Animations.POPUP);
        superToast.setBackground(SuperToast.Background.GRAY);
        superToast.show();
    }

    public static void show(Context c, int textRes, int duration) {
        SuperToast superToast = new SuperToast(c);
        superToast.setDuration(duration);
        superToast.setText(c.getResources().getString(textRes));
        superToast.setAnimations(SuperToast.Animations.POPUP);
        superToast.setBackground(SuperToast.Background.GRAY);
        superToast.show();
    }
}