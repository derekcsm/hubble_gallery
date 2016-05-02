package com.derek_s.hubble_gallery.utils.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.derek_s.hubble_gallery.R;

public class ToolbarTitle {
  private Context c;
  private ViewFactory mFactory = new ViewFactory() {

    @Override
    public View makeView() {
      TextView t = new TextView(c);
      t.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
      t.setTextAppearance(c, R.style.text_title_dark);
      t.setTypeface(FontFactory.getCondensedRegular(c));
      t.setAllCaps(true);
      return t;
    }
  };

  public TextSwitcher init(TextSwitcher switcher, Context context) {
    c = context;
    switcher.setFactory(mFactory);

    Animation in = AnimationUtils.loadAnimation(c,
        R.anim.fade_in_right);
    Animation out = AnimationUtils.loadAnimation(c,
        R.anim.fade_out_right);

    switcher.setInAnimation(in);
    switcher.setOutAnimation(out);

    return switcher;
  }
}