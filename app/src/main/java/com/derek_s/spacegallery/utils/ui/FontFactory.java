package com.derek_s.spacegallery.utils.ui;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by dereksmith on 15-03-07.
 */
public class FontFactory {

    /*
    To use: tv1.setTypeface(FontFactory.getRegular(getContext());
     */

    private static Typeface t1;

    public static Typeface getBlack(Context c) {
        if (t1 == null) {
            t1 = Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-Black.ttf");
        }
        return t1;
    }

    private static Typeface t2;

    public static Typeface getBlackItalic(Context c) {
        if (t2 == null) {
            t2 = Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-BlackItalic.ttf");
        }
        return t2;
    }

    private static Typeface t3;

    public static Typeface getBold(Context c) {
        if (t3 == null) {
            t3 = Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-Bold.ttf");
        }
        return t3;
    }

    private static Typeface t4;

    public static Typeface getBoldItalic(Context c) {
        if (t4 == null) {
            t4 = Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-BoldItalic.ttf");
        }
        return t4;
    }

    private static Typeface t5;

    public static Typeface getItalic(Context c) {
        if (t5 == null) {
            t5 = Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-Italic.ttf");
        }
        return t5;
    }

    private static Typeface t6;

    public static Typeface getLight(Context c) {
        if (t6 == null) {
            t6 = Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-Light.ttf");
        }
        return t6;
    }

    private static Typeface t7;

    public static Typeface getLightItalic(Context c) {
        if (t7 == null) {
            t7 = Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-LightItalic.ttf");
        }
        return t7;
    }

    private static Typeface t8;

    public static Typeface getMedium(Context c) {
        if (t8 == null) {
            t8 = Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-Medium.ttf");
        }
        return t8;
    }

    private static Typeface t9;

    public static Typeface getMediumItalic(Context c) {
        if (t9 == null) {
            t9 = Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-MediumItalic.ttf");
        }
        return t9;
    }

    private static Typeface t10;

    public static Typeface getRegular(Context c) {
        if (t10 == null) {
            t10 = Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-Regular.ttf");
        }
        return t10;
    }

    private static Typeface t11;

    public static Typeface getThin(Context c) {
        if (t11 == null) {
            t11 = Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-Thin.ttf");
        }
        return t11;
    }

    private static Typeface t12;

    public static Typeface getThinItalic(Context c) {
        if (t12 == null) {
            t12 = Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-ThinItalic.ttf");
        }
        return t12;
    }

    private static Typeface t13;

    public static Typeface getCondensedBold(Context c) {
        if (t13 == null) {
            t13 = Typeface.createFromAsset(c.getAssets(), "fonts/RobotoCondensed-Bold.ttf");
        }
        return t13;
    }

    private static Typeface t14;

    public static Typeface getCondensedBoldItalic(Context c) {
        if (t14 == null) {
            t14 = Typeface.createFromAsset(c.getAssets(), "fonts/RobotoCondensed-BoldItalic.ttf");
        }
        return t14;
    }

    private static Typeface t15;

    public static Typeface getCondensedItalic(Context c) {
        if (t15 == null) {
            t15 = Typeface.createFromAsset(c.getAssets(), "fonts/RobotoCondensed-Italic.ttf");
        }
        return t15;
    }

    private static Typeface t16;

    public static Typeface getCondensedLight(Context c) {
        if (t16 == null) {
            t16 = Typeface.createFromAsset(c.getAssets(), "fonts/RobotoCondensed-Light.ttf");
        }
        return t16;
    }

    private static Typeface t17;

    public static Typeface getCondensedLightItalic(Context c) {
        if (t17 == null) {
            t17 = Typeface.createFromAsset(c.getAssets(), "fonts/RobotoCondensed-LightItalic.ttf");
        }
        return t17;
    }

    private static Typeface t18;

    public static Typeface getCondensedRegular(Context c) {
        if (t18 == null) {
            t18 = Typeface.createFromAsset(c.getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        }
        return t18;
    }
}