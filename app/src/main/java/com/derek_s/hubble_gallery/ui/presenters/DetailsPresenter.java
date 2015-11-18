package com.derek_s.hubble_gallery.ui.presenters;

import android.content.Context;

import com.derek_s.hubble_gallery.ui.views.DetailsView;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DetailsPresenter {
    private String TAG = getClass().getSimpleName();

    @Inject
    Context context;

    private DetailsView view;

    @Inject
    public DetailsPresenter() {
    }

    public void setView(DetailsView view) {
        this.view = view;
    }

}
