package com.derek_s.hubble_gallery.utils.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.derek_s.hubble_gallery.R;
import com.derek_s.hubble_gallery.utils.ui.Toasty;

import javax.inject.Inject;

public class NetworkUtil {
  public static int TYPE_WIFI = 1;
  public static int TYPE_MOBILE = 2;
  public static int TYPE_NOT_CONNECTED = 0;

  private final ConnectivityManager connectivityManager;
  private Context context;

  @Inject
  public NetworkUtil(ConnectivityManager connectivityManager, Context context) {
    this.connectivityManager = connectivityManager;
    this.context = context;
  }

  public int getConnectivityStatus() {
    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
    if (null != activeNetwork) {
      if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
        return TYPE_WIFI;

      if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
        return TYPE_MOBILE;
    }
    return TYPE_NOT_CONNECTED;
  }

  public String getConnectivityStatusString() {
    int conn = getConnectivityStatus();
    String status = null;
    if (conn == NetworkUtil.TYPE_WIFI) {
      status = "Wifi enabled";
    } else if (conn == NetworkUtil.TYPE_MOBILE) {
      status = "Mobile data enabled";
    } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
      status = "Not connected to Internet";
    }
    return status;
  }

  public void toastNoConnection() {
    Toasty.show(context, R.string.no_connection, Toasty.LENGTH_MEDIUM);
  }

  public boolean isConnected() {
    int conn = getConnectivityStatus();
    return conn != NetworkUtil.TYPE_NOT_CONNECTED;
  }
}