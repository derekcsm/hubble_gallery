package com.derek_s.hubble_gallery.internal.di;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;

import com.derek_s.hubble_gallery.base.HubbleApplication;
import com.derek_s.hubble_gallery.base.TinyDB;
import com.derek_s.hubble_gallery.ui.presenters.DetailsPresenter;
import com.derek_s.hubble_gallery.utils.FavoriteUtils;
import com.derek_s.hubble_gallery.utils.network.NetworkUtil;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

  Context context();

  Resources resources();

  ConnectivityManager provideConnectivityManager();

  NetworkUtil provideNetworkUtil();

  TinyDB provideTinyDB();

  DetailsPresenter provideActDetailsPresenter();

  FavoriteUtils provideFavoriteUtils();

  Gson provideGson();

  void inject(HubbleApplication app);
}
