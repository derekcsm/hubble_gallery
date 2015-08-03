package com.derek_s.hubble_gallery.base;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by derek on 15-08-03.
 */

// private String TAG = getClass().getSimpleName();
public class HubbleApplication extends HubbleBaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());
    }
}
