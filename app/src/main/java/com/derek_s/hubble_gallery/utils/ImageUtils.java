package com.derek_s.hubble_gallery.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {

  public static String saveImage(ImageView imageView, String imgUrl, String targetPath, ImageLoadingListener listener) {
    if (null != imgUrl && imgUrl.length() > 0) {
      int endIndex = imgUrl.lastIndexOf("/");
      if (endIndex != -1) {
        imgUrl = imgUrl.substring(endIndex, imgUrl.length());
      }
    } else {
      listener.onImageLoadFailed();
      return null;
    }

    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
    Bitmap bitmap = drawable.getBitmap();

    if (bitmap == null) {
      listener.onImageLoadFailed();
      return null;
    }

    boolean mkDirsResult = true;
    File targetFile = new File(targetPath);
    if (!targetFile.exists()) {
      mkDirsResult = targetFile.mkdirs();
    }
    if (!mkDirsResult) {
      listener.onImageLoadFailed();
      return null;
    }

    File image = new File(targetPath, imgUrl);

    boolean success = false;
    // Encode the file as a PNG image.
    FileOutputStream outStream;
    try {

      outStream = new FileOutputStream(image);
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            /* 100 to keep full quality of the image */
      outStream.flush();
      outStream.close();
      success = true;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (success) {
      listener.onImageLoaded();
      return imgUrl;
    } else {
      listener.onImageLoadFailed();
      return null;
    }
  }

  public interface ImageLoadingListener {

    void onImageLoaded();

    void onImageLoadFailed();
  }

}
