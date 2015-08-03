package com.derek_s.hubble_gallery.base;

import android.app.Application;
import android.content.Context;

import com.derek_s.hubble_gallery.internal.di.HubbleComponent;
import com.derek_s.hubble_gallery.internal.di.DaggerHubbleComponent;
import com.derek_s.hubble_gallery.internal.di.SystemServicesModule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Created by derek on 15-08-03.
 */

public class HubbleBaseApplication extends Application {

    private HubbleComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        buildComponentAndInject();
    }

    public void buildComponentAndInject() {
        component = DaggerComponentInitializer.init(this);
    }

    public static HubbleComponent component(Context context) {
        return ((HubbleBaseApplication) context.getApplicationContext()).component;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public final static class DaggerComponentInitializer {

        public static HubbleComponent init(HubbleBaseApplication app) {
            return DaggerHubbleComponent.builder()
                    .systemServicesModule(new SystemServicesModule(app))
                    .build();
        }

    }
}