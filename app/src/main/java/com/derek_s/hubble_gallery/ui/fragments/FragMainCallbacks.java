package com.derek_s.hubble_gallery.ui.fragments;

import com.derek_s.hubble_gallery.model.TileObject;
import com.github.ksoichiro.android.observablescrollview.ObservableGridView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

/**
 * Created by pedro on 18/11/2015.
 */
public interface FragMainCallbacks {
    void onGridItemClicked(TileObject tileObject);

    void adjustToolbar(ScrollState scrollState, ObservableGridView gridView);
}
