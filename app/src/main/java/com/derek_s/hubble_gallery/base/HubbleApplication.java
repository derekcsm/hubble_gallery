package com.derek_s.hubble_gallery.base;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.derek_s.hubble_gallery.internal.di.AppComponent;
import com.derek_s.hubble_gallery.internal.di.AppModule;
import com.derek_s.hubble_gallery.internal.di.DaggerAppComponent;

import io.fabric.sdk.android.Fabric;

public class HubbleApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
        Fabric.with(this, new Crashlytics());
    }

    private void initializeInjector() {
        this.appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();

        this.appComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
