package com.derek_s.hubble_gallery.internal.di;

import com.derek_s.hubble_gallery.base.ActBase;
import com.derek_s.hubble_gallery.base.FragBase;
import com.derek_s.hubble_gallery.detailspage.DetailsActivity;
import com.derek_s.hubble_gallery.home.ActHome;
import com.derek_s.hubble_gallery.ui.activities.ActImageViewer;
import com.derek_s.hubble_gallery.ui.activities.ActWelcome;

import dagger.Component;

/**
 * This component injects Activities and Fragments
 */

@ActivityScope
@Component(dependencies = {AppComponent.class})
public interface ActivityComponent {

  void inject(ActBase app);

  void inject(DetailsActivity app);

  void inject(ActHome app);

  void inject(ActImageViewer app);

  void inject(ActWelcome app);

  void inject(FragBase app);
}
