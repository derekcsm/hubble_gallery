package com.derek_s.hubble_gallery.internal.di;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import com.derek_s.hubble_gallery.base.TinyDB;
import com.derek_s.hubble_gallery.utils.dagger.PerApp;
import com.derek_s.hubble_gallery.utils.network.NetworkUtil;

import dagger.Module;
import dagger.Provides;

/**
 * Created by derek on 15-08-03.
 */

@Module
public class SystemServicesModule {

    private final Application application;

    public SystemServicesModule(Application application) {
        this.application = application;
    }

    @Provides
    Context provideContext() {
        return application.getBaseContext();
    }

    @Provides
    @PerApp
    ConnectivityManager provideConnectivityManager() {
        return (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    @PerApp
    NetworkUtil provideNetworkStateManager(ConnectivityManager connectivityManagerCompat, Context context) {
        return new NetworkUtil(connectivityManagerCompat, context);
    }

    @Provides
    @PerApp
    TinyDB provideTinyDb(Context context) {
        return new TinyDB(context);
    }
}
