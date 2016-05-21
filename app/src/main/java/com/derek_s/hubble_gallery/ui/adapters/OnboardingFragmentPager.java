package com.derek_s.hubble_gallery.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.derek_s.hubble_gallery.ui.fragments.FragWelcomeInfo;
import com.derek_s.hubble_gallery.ui.fragments.FragWelcomeOutline;

/**
 * Created by dereksmith on 15-05-04.
 */
public class OnboardingFragmentPager extends FragmentPagerAdapter {
  private String TAG = getClass().getSimpleName();

  public OnboardingFragmentPager(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int position) {
    Log.i(TAG, "getItem pos: " + position);
    if (position == 0) {
      return new FragWelcomeOutline();
    } else {
      return FragWelcomeInfo.newInstance(position);
    }
  }

  @Override
  public int getCount() {
    return 5;
  }

}

