package com.derek_s.hubble_gallery.ui.activities;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.derek_s.hubble_gallery.R;
import com.derek_s.hubble_gallery.base.ActBase;
import com.derek_s.hubble_gallery.internal.di.ActivityComponent;
import com.derek_s.hubble_gallery.ui.adapters.OnboardingFragmentPager;
import com.derek_s.hubble_gallery.utils.ui.starfield.StarField;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class ActWelcome extends ActBase {

  @Bind(R.id.rl_onboarding)
  RelativeLayout rlOnboarding;
  @Bind(R.id.sv_starfield)
  SurfaceView svStarfield;
  @Bind(R.id.vertical_pager)
  VerticalViewPager verticalViewPager;
  int width;
  int height;
  boolean fromOnCreate = false;
  private StarField starField;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    fromOnCreate = true;
    setContentView(R.layout.act_welcome);
    ButterKnife.bind(this);

    setWindowAttributes();
    setWindowSizes();

    starField = new StarField(svStarfield.getHolder(), width, height);
    starField.start();

    // remove all fragments from manager
    if (getSupportFragmentManager().getFragments() != null)
      for (Fragment fragment : getSupportFragmentManager().getFragments()) {
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
      }

    verticalViewPager.setAdapter(new OnboardingFragmentPager(getSupportFragmentManager()));
    verticalViewPager.setOffscreenPageLimit(5);
  }

  @Override
  public void onPause() {
    starField.stop();
    super.onPause();
  }

  @Override
  public void onResume() {
    if (fromOnCreate) {
      fromOnCreate = false;
    } else {
      setWindowSizes();
      starField = new StarField(svStarfield.getHolder(), width, height);
      starField.start();
    }
    super.onResume();
  }

  private void setWindowSizes() {
    Display display = getWindowManager().getDefaultDisplay();
    Point size = new Point();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      display.getRealSize(size);
    } else {
      display.getSize(size);
    }
    width = size.x;
    height = size.y;
  }

  private void setWindowAttributes() {
    Window window = getWindow();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(Color.TRANSPARENT);
      window.setNavigationBarColor(Color.TRANSPARENT);
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
      window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      getWindow().getDecorView().setSystemUiVisibility(
          View.SYSTEM_UI_FLAG_LAYOUT_STABLE
              | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
              | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
  }

  @Override
  protected void injectComponent(ActivityComponent activityComponent) {
    activityComponent.inject(this);
  }
}
