package com.derek_s.hubble_gallery.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.derek_s.hubble_gallery.R;
import com.derek_s.hubble_gallery.api.GetDetails;
import com.derek_s.hubble_gallery.base.ActBase;
import com.derek_s.hubble_gallery.base.Constants;
import com.derek_s.hubble_gallery.model.DetailsObject;
import com.derek_s.hubble_gallery.model.TileObject;
import com.derek_s.hubble_gallery.ui.views.ActDetailsView;
import com.derek_s.hubble_gallery.ui.widgets.TouchImageView;
import com.derek_s.hubble_gallery.utils.Animation.SquareFlipper;
import com.derek_s.hubble_gallery.utils.FavoriteUtils;
import com.derek_s.hubble_gallery.utils.ImageUtils;
import com.derek_s.hubble_gallery.utils.ui.FontFactory;
import com.derek_s.hubble_gallery.utils.ui.Toasty;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActDetails extends ActBase implements ObservableScrollViewCallbacks, ActDetailsView {

    private String TAG = getClass().getSimpleName();
    private static final String TOOLBAR_CURRENT_ALPHA = "toolbar_current_alpha";
    private static final String TOOLBAR_COLOR = "toolbar_current_color";

    @Bind(R.id.iv_display)
    ImageView ivDisplay;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_body)
    TextView tvBody;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.scroll)
    ObservableScrollView scrollView;
    @Bind(R.id.square)
    View square;
    @Bind(R.id.zero_state)
    View zeroState;
    @Bind(R.id.tv_zero_state_info)
    TextView tvZeroStateInfo;
    @Bind(R.id.tv_retry)
    TextView tvRetry;
    @Bind(R.id.fl_stretchy)
    FrameLayout flStretchy;

    private SquareFlipper squareFlipper = new SquareFlipper();

    private int titleBgColor;
    private int alphaTitleBgColor;
    private FavoriteUtils favoriteUtils;
    private MenuItem actionFavorite;
    private Menu menu;

    private int imgLoadAttempt = 0;
    private TileObject tileObject;
    private DetailsObject detailsObject;
    public static String successfulSrc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_details);
        ButterKnife.bind(this);
        beautifyViews();

        /**
         * here we're prioritizing data sources for populating tileObject
         * if the instance state isn't null but the arguments are set
         * then go with arguments
         */
        if (savedInstanceState != null) {
            tileObject = TileObject.create(savedInstanceState.getString(Constants.PARAM_TILE_KEY));
            detailsObject = DetailsObject.create(savedInstanceState.getString(Constants.PARAM_DETAILS_KEY));
        }
        if (getIntent().getExtras() != null) {
            tileObject = TileObject.create(getIntent().getStringExtra(Constants.PARAM_TILE_KEY));
        }

        /**
         * toolbar - actionbar setup
         */
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.fade_in_shadow, R.anim.slide_out_right);
            }
        });

        /**
         * set title and description from `Tile` object
         */
        tvTitle.setText(tileObject.getTitle());
        if (savedInstanceState != null && detailsObject != null) {
            tvBody.setText(Html.fromHtml(detailsObject.getDescription()));
        }

        flStretchy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageViewer();
            }
        });
        scrollView.setScrollViewCallbacks(this);

        showLoadingAnimation(true, 0);
        if (savedInstanceState != null) {
            loadImage(tileObject.getSrc());
            titleBgColor = savedInstanceState.getInt(TOOLBAR_COLOR);
            alphaTitleBgColor = savedInstanceState.getInt(TOOLBAR_CURRENT_ALPHA);
            toolbar.setBackgroundColor(alphaTitleBgColor);

            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.scrollTo(0, scrollView.getCurrentScrollY());
                }
            });

            if (detailsObject == null) {
                loadPage();
            } else if (detailsObject.getDescription() == null || detailsObject.getDescription().length() == 0) {
                loadPage();
            }
        } else {
            loadPage();
        }

    }

    private void loadPage() {
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
                    showLoadingAnimation(false, 1);
                    description = description.replace("To access available information and downloadable versions " +
                            "of images in this news release, click on any of the images below:", "");
                    detailsObject.setDescription(description);
                    tvBody.setText(Html.fromHtml(detailsObject.getDescription()));
                } else {
                    showZeroState(true);
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
        Picasso.with(this).load(nSrc).into(ivDisplay, new Callback() {
            @Override
            public void onSuccess() {
                imgLoadAttempt = 0;
                getPalete();
                showLoadingAnimation(false, 0);
            }

            @Override
            public void onError() {
                Log.i(TAG, "onError");
                imgLoadAttempt = imgLoadAttempt + 1;
                loadImage(src);
            }
        });
    }

    private void getPalete() {
        Palette.generateAsync(((BitmapDrawable) ivDisplay.getDrawable()).getBitmap(), new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {

                titleBgColor = palette.getDarkVibrantColor(R.color.title_background);
                tvTitle.setBackgroundColor(titleBgColor);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(palette.getDarkMutedColor(android.R.color.black));
                }

                Palette.Swatch swatch = palette.getDarkVibrantSwatch();
                if (swatch != null) {
                    tvTitle.setTextColor(swatch.getTitleTextColor());
                }

            }
        });
    }

    private void openImageViewer() {
        if (successfulSrc == null) {
            Toasty.show(this, R.string.error_loading_image, Toasty.LENGTH_MEDIUM);
        } else {
            Intent intent = new Intent(ActDetails.this, ActImageViewer.class);
            intent.putExtra(ActImageViewer.EXTRA_IMAGE_SRC, successfulSrc);
            startActivity(intent);
        }
    }

    public void showLoadingAnimation(boolean show, int type) {
        /**
         * type:
         * 0 = image
         * 1 = description
         */
        switch (type) {
            case 0:
                if (show) {
                    showSquareFlipper(true);
                    showZeroState(false);
                } else {
                    showSquareFlipper(false);

                    ivDisplay.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeIn).duration(1000).playOn(ivDisplay);

                    tvTitle.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeIn).duration(700).playOn(tvTitle);

                    toolbar.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.SlideInDown).duration(700).playOn(toolbar);

                    if (detailsObject != null)
                        showLoadingAnimation(false, 1);
                }
                break;
            case 1:
                if (ivDisplay != null && ivDisplay.getVisibility() == View.VISIBLE)
                    if (show) {
                        tvBody.setVisibility(View.INVISIBLE);
                    } else {
                        tvBody.setVisibility(View.VISIBLE);
                    }
                break;
        }
    }

    public void showZeroState(boolean show) {
        if (show) {
            zeroState.setVisibility(View.VISIBLE);
            tvBody.setVisibility(View.GONE);

            tvZeroStateInfo.setText("Unable to load description");
            tvRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadPage();
                }
            });
        } else {
            zeroState.setVisibility(View.INVISIBLE);
        }
    }

    private void showSquareFlipper(boolean show) {
        if (show) {
            squareFlipper.startAnimation(square);
        } else {
            squareFlipper.stopAnimation();
        }
    }

    public static class imageViewerDialogFragment extends DialogFragment {

        static imageViewerDialogFragment newInstance() {
            return new imageViewerDialogFragment();
        }

        @Bind(R.id.ivTouch)
        TouchImageView ivTouch;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.dialog_image_viewer, container, false);
            ButterKnife.bind(this, rootView);

            Picasso.with(getActivity()).load(successfulSrc).into(ivTouch, new Callback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError() {
                    Log.d("IV fragment", "onError");
                }
            });

            return rootView;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.fade_in_shadow, R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_act_details, menu);
        favoriteUtils = new FavoriteUtils(this);
        actionFavorite = menu.findItem(R.id.action_favorite);

        if (favoriteUtils.isFavorited(tileObject)) {
            actionFavorite.setIcon(R.drawable.ic_favorite_white_24dp);
            actionFavorite.setTitle(R.string.remove_favorite);
        } else {
            actionFavorite.setIcon(R.drawable.ic_favorite_outline_white_24dp);
            actionFavorite.setTitle(R.string.add_favorite);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String filename;
        String path;
        File f;
        Uri imgUri;
        switch (id) {
            case R.id.action_expand:
                openImageViewer();
                break;
            case R.id.action_favorite:
                if (favoriteUtils.isFavorited(tileObject)) {
                    favoriteUtils.removeFavorite(tileObject);
                    actionFavorite.setIcon(R.drawable.ic_favorite_outline_white_24dp);
                    actionFavorite.setTitle(R.string.add_favorite);
                } else {
                    favoriteUtils.saveFavorite(tileObject);
                    actionFavorite.setIcon(R.drawable.ic_favorite_white_24dp);
                    actionFavorite.setTitle(R.string.remove_favorite);
                }

                break;
            case R.id.action_share_image:
                filename = ImageUtils.saveImage(ivDisplay, successfulSrc, ActDetails.this, false);
                path = Constants.imageDirectory() + filename;
                f = new File(path);
                imgUri = Uri.fromFile(f);

                Log.i(TAG, "imgUri" + imgUri);
                if (imgUri != null) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("*/*");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "via http://bit.ly/1dm23ZQ");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);

                    startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share_image)));
                } else {
                    Toasty.show(ActDetails.this, R.string.error_saving_image, Toasty.LENGTH_LONG);
                }

                break;
            case R.id.action_save_image:
                ImageUtils.saveImage(ivDisplay, successfulSrc, ActDetails.this, true);
                break;
            case R.id.action_set_image:
                filename = ImageUtils.saveImage(ivDisplay, successfulSrc, ActDetails.this, false);
                path = Constants.imageDirectory() + filename;
                f = new File(path);
                imgUri = Uri.fromFile(f);

                Log.i(TAG, "imgUri" + imgUri);
                if (imgUri != null) {
                    Intent attachIntent = new Intent(Intent.ACTION_ATTACH_DATA);
                    attachIntent.setDataAndType(imgUri, "image/jpeg");
                    Intent openInChooser = Intent.createChooser(attachIntent, getResources().getString(R.string.set_as));
                    startActivity(openInChooser);
                } else {
                    Toasty.show(ActDetails.this, R.string.error_saving_image, Toasty.LENGTH_LONG);
                }
                break;
            case R.id.action_open_in_browser:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://hubblesite.org" + tileObject.getHref()));
                startActivity(browserIntent);
                break;
            case R.id.action_share_link:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, tileObject.getTitle() + " - http://hubblesite.org" + tileObject.getHref() + " via http://bit.ly/1dm23ZQ");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.action_copy_link:
                @SuppressWarnings("deprecation")
                ClipboardManager _clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                _clipboard.setText("http://hubblesite.org" + tileObject.getHref());
                Toasty.show(ActDetails.this, "Copied link to clipboard", Toasty.LENGTH_MEDIUM);
                break;
        }
        return true;
    }

    private void beautifyViews() {
        tvBody.setTextIsSelectable(true);
        tvBody.setMovementMethod(LinkMovementMethod.getInstance());

        tvZeroStateInfo.setTypeface(FontFactory.getRegular(this));
        tvRetry.setTypeface(FontFactory.getCondensedLight(this));
        tvTitle.setTypeface(FontFactory.getCondensedBold(this));
        tvBody.setTypeface(FontFactory.getRegular(this));
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        float alpha = 1 - (float) Math.max(0, ivDisplay.getHeight() - scrollY) / ivDisplay.getHeight();
        Log.d(TAG, "alpha: " + alpha);
        alphaTitleBgColor = ScrollUtils.getColorWithAlpha(alpha, titleBgColor);
        toolbar.setBackgroundColor(alphaTitleBgColor);
        ViewHelper.setTranslationY(ivDisplay, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }
}
