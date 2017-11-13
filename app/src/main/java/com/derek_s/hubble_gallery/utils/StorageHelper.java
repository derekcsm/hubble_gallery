package com.derek_s.hubble_gallery.utils;

import android.content.Context;
import android.os.Environment;

import com.derek_s.hubble_gallery.base.Constants;

public class StorageHelper {

  private final Context context;

  public StorageHelper(Context context) {
    this.context = context;
  }

  public String getDirectoryPath() {
    if (isExternalStorageWritable()) {
      return Constants.externalImageDirectory();
    }
    return Constants.internalImageDirectory(context.getFilesDir());
  }

  private boolean isExternalStorageWritable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state)) {
      return true;
    }
    return false;
  }
}
