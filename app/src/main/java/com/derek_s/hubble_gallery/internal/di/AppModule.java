package com.derek_s.hubble_gallery.internal.di;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;

import com.derek_s.hubble_gallery.base.TinyDB;
import com.derek_s.hubble_gallery.utils.StorageHelper;
import com.derek_s.hubble_gallery.utils.network.NetworkUtil;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * This module resolves everything on an AppContext, things that need to be instantiated
 * only once or need an App Context.
 */

@Module
public class AppModule {

  private final Application application;

  public AppModule(Application application) {
    this.application = application;
  }

  @Provides
  Context provideContext() {
    return application.getBaseContext();
  }

  @Provides
  @Singleton
  Resources provideResources(Context context) {
    return context.getResources();
  }

  @Provides
  @Singleton
  ConnectivityManager provideConnectivityManager() {
    return (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
  }

  @Provides
  @Singleton
  StorageHelper provideStorageHelper(Context context) {
    return new StorageHelper(context);
  }

  @Provides
  @Singleton
  NetworkUtil provideNetworkStateManager(ConnectivityManager connectivityManagerCompat, Context context) {
    return new NetworkUtil(connectivityManagerCompat, context);
  }

  @Provides
  @Singleton
  TinyDB provideTinyDb(Context context) {
    return new TinyDB(context);
  }

  @Provides
  @Singleton
  Gson provideGson() {
    return new Gson();
  }
}
