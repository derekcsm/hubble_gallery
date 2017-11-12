package com.derek_s.hubble_gallery.detailspage;

import android.content.Intent;
import android.widget.ImageView;

import com.derek_s.hubble_gallery._shared.model.DetailsObject;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

public interface DetailsContract {

  void showLoadingAnimation(boolean show, int type);

  void populateDetails(DetailsObject detailsObject);

  void showZeroState(boolean show);

  void generatePalette();

  void openActivityIntent(Intent intent);

  void openImageViewer();

  ImageView getIvDisplay();

  ObservableScrollView getScrollView();

  void updateMenu();

  void showMessage(String messageText);
}
