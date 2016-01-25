package com.derek_s.hubble_gallery.base;

import android.app.Application;

import com.derek_s.hubble_gallery.internal.di.AppComponent;
import com.derek_s.hubble_gallery.internal.di.AppModule;
import com.derek_s.hubble_gallery.internal.di.DaggerAppComponent;

public class HubbleApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
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
