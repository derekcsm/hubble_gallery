package com.derek_s.hubble_gallery.internal.di;

import android.content.Context;
import android.net.ConnectivityManager;

import com.derek_s.hubble_gallery.base.HubbleApplication;
import com.derek_s.hubble_gallery.base.TinyDB;
import com.derek_s.hubble_gallery.ui.presenters.DetailsPresenter;
import com.derek_s.hubble_gallery.utils.network.NetworkUtil;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    Context context();

    ConnectivityManager provideConnectivityManager();

    NetworkUtil provideNetworkUtil();

    TinyDB provideTinyDB();

    DetailsPresenter provideActDetailsPresenter();

    void inject(HubbleApplication app);
}
