package com.derek_s.hubble_gallery.imageviewer;

import android.content.Intent;
import android.os.Bundle;

import com.derek_s.hubble_gallery.R;
import com.derek_s.hubble_gallery.base.ActBase;
import com.derek_s.hubble_gallery.internal.di.ActivityComponent;
import com.derek_s.hubble_gallery.ui.widgets.TouchImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageViewerActivity extends ActBase {

  public static final String EXTRA_IMAGE_SRC = "extra_successful_src";
  @BindView(R.id.iv_fullscreen)
  TouchImageView ivFullscreen;
  private String imgSrc;

  @Override
  protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    setContentView(R.layout.act_image_viewer);
    ButterKnife.bind(this);

    Intent intent = getIntent();
    if (intent != null) {
      imgSrc = intent.getStringExtra(EXTRA_IMAGE_SRC);
    }

    if (savedState != null)
      imgSrc = savedState.getString(EXTRA_IMAGE_SRC);

    Picasso.with(this).load(imgSrc).into(ivFullscreen);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    outState.putString(EXTRA_IMAGE_SRC, imgSrc);
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void injectComponent(ActivityComponent component) {
    component.inject(this);
  }
}
