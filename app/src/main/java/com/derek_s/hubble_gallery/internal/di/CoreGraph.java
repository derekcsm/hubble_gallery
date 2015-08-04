package com.derek_s.hubble_gallery.internal.di;

import com.derek_s.hubble_gallery.base.BaseActivity;
import com.derek_s.hubble_gallery.base.HubbleApplication;
import com.derek_s.hubble_gallery.ui.fragments.FragMain;
import com.derek_s.hubble_gallery.ui.fragments.FragNavigationDrawer;

/**
 * Created by derek on 15-08-03.
 */

public interface CoreGraph {
    void inject(HubbleApplication app);

    void inject(BaseActivity app);

    void inject(FragNavigationDrawer app);

    void inject(FragMain app);
}

