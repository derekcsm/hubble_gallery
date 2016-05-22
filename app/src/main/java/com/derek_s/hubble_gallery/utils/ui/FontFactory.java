package com.derek_s.hubble_gallery.utils.ui;

import android.content.Context;
import android.graphics.Typeface;

import com.derek_s.hubble_gallery.R;

/**
 * Created by dereksmith on 15-03-07.
 */
public class FontFactory {

    /*
    To use: tv1.setTypeface(FontFactory.getRegular(getContext());
     */

  private static Typeface t1;
  private static Typeface t2;
  private static Typeface t3;
  private static Typeface t4;
  private static Typeface t5;
  private static Typeface t6;
  private static Typeface t7;
  private static Typeface t8;
  private static Typeface t9;
  private static Typeface t10;
  private static Typeface t11;
  private static Typeface t12;
  private static Typeface t13;
  private static Typeface t14;
  private static Typeface t15;
  private static Typeface t16;
  private static Typeface t17;
  private static Typeface t18;

  public static Typeface getBlack(Context c) {
    if (t1 == null) {
      t1 = Typeface.createFromAsset(c.getAssets(), c.getString(R.string.roboto_black));
    }
    return t1;
  }

  public static Typeface getBlackItalic(Context c) {
    if (t2 == null) {
      t2 = Typeface.createFromAsset(c.getAssets(), c.getString(R.string.roboto_black_italic));
    }
    return t2;
  }

  public static Typeface getBold(Context c) {
    if (t3 == null) {
      t3 = Typeface.createFromAsset(c.getAssets(), c.getString(R.string.roboto_bold));
    }
    return t3;
  }

  public static Typeface getBoldItalic(Context c) {
    if (t4 == null) {
      t4 = Typeface.createFromAsset(c.getAssets(), c.getString(R.string.roboto_bold_italic));
    }
    return t4;
  }

  public static Typeface getItalic(Context c) {
    if (t5 == null) {
      t5 = Typeface.createFromAsset(c.getAssets(), c.getString(R.string.roboto_italic));
    }
    return t5;
  }

  public static Typeface getLight(Context c) {
    if (t6 == null) {
      t6 = Typeface.createFromAsset(c.getAssets(), c.getString(R.string.roboto_light));
    }
    return t6;
  }

  public static Typeface getLightItalic(Context c) {
    if (t7 == null) {
      t7 = Typeface.createFromAsset(c.getAssets(), c.getString(R.string.roboto_light_italic));
    }
    return t7;
  }

  public static Typeface getMedium(Context c) {
    if (t8 == null) {
      t8 = Typeface.createFromAsset(c.getAssets(), c.getString(R.string.roboto_medium));
    }
    return t8;
  }

  public static Typeface getMediumItalic(Context c) {
    if (t9 == null) {
      t9 = Typeface.createFromAsset(c.getAssets(), c.getString(R.string.roboto_medium_italic));
    }
    return t9;
  }

  public static Typeface getRegular(Context c) {
    if (t10 == null) {
      t10 = Typeface.createFromAsset(c.getAssets(), c.getString(R.string.roboto_regular));
    }
    return t10;
  }

  public static Typeface getThin(Context c) {
    if (t11 == null) {
      t11 = Typeface.createFromAsset(c.getAssets(), c.getString(R.string.roboto_thin));
    }
    return t11;
  }

  public static Typeface getThinItalic(Context c) {
    if (t12 == null) {
      t12 = Typeface.createFromAsset(c.getAssets(), c.getString(R.string.roboto_thin_italic));
    }
    return t12;
  }

  public static Typeface getCondensedBold(Context c) {
    if (t13 == null) {
      t13 = Typeface.createFromAsset(c.getAssets(), c.getString(R.string.roboto_condensed_bold));
    }
    return t13;
  }

  public static Typeface getCondensedBoldItalic(Context c) {
    if (t14 == null) {
      t14 = Typeface.createFromAsset(c.getAssets(), c.getString(R.string.roboto_condensed_bold_italic));
    }
    return t14;
  }

  public static Typeface getCondensedItalic(Context c) {
    if (t15 == null) {
      t15 = Typeface.createFromAsset(c.getAssets(), c.getString(R.string.roboto_condensed_italic));
    }
    return t15;
  }

  public static Typeface getCondensedLight(Context c) {
    if (t16 == null) {
      t16 = Typeface.createFromAsset(c.getAssets(), c.getString(R.string.roboto_condensed_light));
    }
    return t16;
  }

  public static Typeface getCondensedLightItalic(Context c) {
    if (t17 == null) {
      t17 = Typeface.createFromAsset(c.getAssets(), c.getString(R.string.roboto_condensed_light_italic));
    }
    return t17;
  }

  public static Typeface getCondensedRegular(Context c) {
    if (t18 == null) {
      t18 = Typeface.createFromAsset(c.getAssets(), c.getString(R.string.roboto_condensed_regular));
    }
    return t18;
  }

}