package com.derek_s.hubble_gallery.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.derek_s.hubble_gallery.R;
import com.derek_s.hubble_gallery.base.Constants;
import com.derek_s.hubble_gallery.utils.ui.Toasty;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dereksmith on 15-04-11.
 */
public class ImageUtils {

    public static String saveImage(ImageView imageView, String imgUrl, Context context, boolean toastOnSuccess) {
        String savedUri = null;

        if (null != imgUrl && imgUrl.length() > 0) {
            int endIndex = imgUrl.lastIndexOf("/");
            if (endIndex != -1) {
                imgUrl = imgUrl.substring(endIndex, imgUrl.length());
            }
        } else {
            Toasty.show(context, R.string.error_saving_image, Toasty.LENGTH_LONG);
            return savedUri;
        }

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        if (bitmap == null) {
            Toasty.show(context, R.string.error_saving_image, Toasty.LENGTH_LONG);
            return savedUri;
        }

        new File(Constants.imageDirectory()).mkdirs();
        File image = new File(Constants.imageDirectory(), imgUrl);

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
            if (toastOnSuccess)
                Toasty.show(context, R.string.image_saved, Toasty.LENGTH_MEDIUM);
            return imgUrl;
        } else {
            Toasty.show(context, R.string.error_saving_image, Toasty.LENGTH_LONG);
            return null;
        }
    }

}
