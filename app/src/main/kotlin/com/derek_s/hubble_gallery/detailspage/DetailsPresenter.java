package com.derek_s.hubble_gallery.detailspage;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.MenuItem;

import com.derek_s.hubble_gallery.R;
import com.derek_s.hubble_gallery._shared.model.DetailsObject;
import com.derek_s.hubble_gallery._shared.model.TileObject;
import com.derek_s.hubble_gallery.api.GetDetails;
import com.derek_s.hubble_gallery.base.Constants;
import com.derek_s.hubble_gallery.utils.FavoriteUtils;
import com.derek_s.hubble_gallery.utils.ImageUtils;
import com.derek_s.hubble_gallery.utils.ui.Toasty;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DetailsPresenter {
  public TileObject tileObject;
  public DetailsObject detailsObject;
  public String successfulSrc;
  public int scrollYPos;
  public int darkVibrantColor;
  public int lightVibrantColor;
  public int toolbarBgColorAlpha;
  @Inject Context context;
  @Inject Resources resources;
  @Inject FavoriteUtils favoriteUtils;
  private String TAG = getClass().getSimpleName();
  private DetailsContract view;
  private int imgLoadAttempt = 0;

  @Inject
  public DetailsPresenter() {
  }

  public void setView(DetailsContract view) {
    this.view = view;
  }

  public void onCreate(Bundle savedState) {
    view.showLoadingAnimation(true, 0);

    if (savedState != null && detailsObject != null)
      view.populateDetails(detailsObject);

    if (savedState != null) {
      loadImage(tileObject.getSrc());
      view.getScrollView().post(new Runnable() {
        @Override
        public void run() {
          view.getScrollView().scrollTo(0, scrollYPos);
        }
      });

      if (detailsObject == null) {
        loadPage();
      } else if (detailsObject.getDescription() == null ||
          detailsObject.getDescription().length() == 0) {
        loadPage();
      }
    } else {
      loadPage();
    }
  }

  public void handleIntent(Intent intent) {
    tileObject = TileObject.Companion.create(intent.getStringExtra(Constants.PARAM_TILE_KEY));
  }

  public void onStart() {
  }

  public void onStop() {
  }

  public void loadPage() {
    getDetails();
    loadImage(tileObject.getSrc());
  }

  private void getDetails() {
    final GetDetails getDetails = new GetDetails(tileObject.getHref());
    getDetails.setGetDetailsCompleteListener(new GetDetails.OnTaskComplete() {
      @Override
      public void setTaskComplete(DetailsObject result, String newsUrl) {
        detailsObject = result;
        String description = detailsObject.getDescription();
        if (description != null) {
          /**
           * Note this is sometimes suffixed at the end of image descriptions, but it doesn't
           * make sense to say in the app so we trim it out here
           */
          description = description.replace("To access available information and downloadable versions " +
              "of images in this news release, click on any of the images below:", "");

          detailsObject.setDescription(description);
          view.populateDetails(detailsObject);
          view.showLoadingAnimation(false, 1);
        } else {
          view.showZeroState(true);
        }
      }
    });
    getDetails.execute();
  }

  private void loadImage(final String src) {
    String nSrc = src;
    switch (imgLoadAttempt) {
      case 0:
        nSrc = nSrc.replace("-web", "-xlarge_web");
        break;
      case 1:
        nSrc = nSrc.replace("-web", "-large_web");
        break;
      case 2:
        // keep it the same "-web"
        break;
    }
    successfulSrc = nSrc;
    Log.i(TAG, "loadImage " + nSrc);
    Picasso.with(context).load(nSrc).into(view.getIvDisplay(), new Callback() {
      @Override
      public void onSuccess() {
        imgLoadAttempt = 0;
        view.generatePalette();
        view.showLoadingAnimation(false, 0);
      }

      @Override
      public void onError() {
        Log.i(TAG, "onError");
        imgLoadAttempt = imgLoadAttempt + 1;
        loadImage(src);
      }
    });
  }

  public Toolbar.OnMenuItemClickListener getMenuItemListener() {
    return new Toolbar.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        String filename;
        String path;
        File f;
        Uri imgUri;
        switch (id) {
          case R.id.action_expand:
            view.openImageViewer();
            break;
          case R.id.action_favorite:
            if (favoriteUtils.isFavorited(tileObject))
              favoriteUtils.removeFavorite(tileObject);
            else
              favoriteUtils.saveFavorite(tileObject);
            view.updateMenu();

            break;
          case R.id.action_share_image:
            filename = ImageUtils.saveImage(view.getIvDisplay(), successfulSrc, context, false);
            path = Constants.imageDirectory() + filename;
            f = new File(path);
            imgUri = Uri.fromFile(f);

            Log.i(TAG, "imgUri" + imgUri);
            if (imgUri != null) {
              Intent shareIntent = new Intent(Intent.ACTION_SEND);
              shareIntent.setType("*/*");
              shareIntent.putExtra(Intent.EXTRA_TEXT, "via http://bit.ly/1dm23ZQ");
              shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);

              view.openActivityIntent(Intent.createChooser(shareIntent,
                  resources.getString(R.string.share_image)));
            } else
              Toasty.show(context, R.string.error_saving_image, Toasty.LENGTH_LONG);

            break;
          case R.id.action_save_image:
            ImageUtils.saveImage(view.getIvDisplay(), successfulSrc, context, true);
            break;
          case R.id.action_set_image:
            filename = ImageUtils.saveImage(view.getIvDisplay(), successfulSrc, context, false);
            path = Constants.imageDirectory() + filename;
            f = new File(path);
            imgUri = Uri.fromFile(f);

            Log.i(TAG, "imgUri" + imgUri);
            if (imgUri != null) {
              Intent attachIntent = new Intent(Intent.ACTION_ATTACH_DATA);
              attachIntent.setDataAndType(imgUri, "image/jpeg");
              Intent openInChooser = Intent.createChooser(attachIntent,
                  resources.getString(R.string.set_as));
              view.openActivityIntent(openInChooser);
            } else
              Toasty.show(context, R.string.error_saving_image, Toasty.LENGTH_LONG);
            break;
          case R.id.action_open_in_browser:
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://hubblesite.org" + tileObject.getHref()));
            view.openActivityIntent(browserIntent);
            break;
          case R.id.action_share_link:
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                tileObject.getTitle() + " - http://hubblesite.org" + tileObject.getHref() + " via http://bit.ly/1dm23ZQ");
            sendIntent.setType("text/plain");
            view.openActivityIntent(sendIntent);
            break;
          case R.id.action_copy_link:
            @SuppressWarnings("deprecation")
            ClipboardManager _clipboard =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            _clipboard.setText("http://hubblesite.org" + tileObject.getHref());
            Toasty.show(context, "Copied link to clipboard", Toasty.LENGTH_MEDIUM);
            break;
        }
        return true;
      }
    };
  }

  public void setPaletteColors(Palette palette) {
    darkVibrantColor = palette.getDarkVibrantColor(ContextCompat.getColor(context, R.color.title_background));
    lightVibrantColor = palette.getLightVibrantColor(ContextCompat.getColor(context, R.color.accent));
  }

  /**
   * Returns darker version of specified <code>color</code>.
   */
  public int darker(int color, float factor) {
    int a = Color.alpha(color);
    int r = Color.red(color);
    int g = Color.green(color);
    int b = Color.blue(color);

    return Color.argb(a,
        Math.max((int) (r * factor), 0),
        Math.max((int) (g * factor), 0),
        Math.max((int) (b * factor), 0));
  }

}
