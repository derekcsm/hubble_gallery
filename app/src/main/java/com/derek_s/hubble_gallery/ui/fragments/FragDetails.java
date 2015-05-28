package com.derek_s.hubble_gallery.ui.fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.derek_s.hubble_gallery.R;
import com.derek_s.hubble_gallery.api.GetDetails;
import com.derek_s.hubble_gallery.base.Constants;
import com.derek_s.hubble_gallery.model.DetailsObject;
import com.derek_s.hubble_gallery.model.TileObject;
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
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/*
Work with -a-xlarge_web if image not available catch in picasso
error, then iterate down the resolution list to find the highest
available resolution

0: -xlarge_web
1: -large_web
2: -web
 */

public class FragDetails extends android.support.v4.app.Fragment implements ObservableScrollViewCallbacks {

    private static String TAG = "FragDetails";
    private TileObject tileObject;
    private DetailsObject detailsObject;
    @InjectView(R.id.iv_display)
    public ImageView ivDisplay;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_body)
    TextView tvBody;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.scroll)
    ObservableScrollView scrollView;
    @InjectView(R.id.square)
    View square;
    @InjectView(R.id.zero_state)
    View zeroState;
    @InjectView(R.id.tv_zero_state_info)
    TextView tvZeroStateInfo;
    @InjectView(R.id.tv_retry)
    TextView tvRetry;
    int imgLoadAttempt = 0;
    public static String successfulSrc;
    int titleBgColor;
    int alphaTitleBgColor;
    FavoriteUtils favoriteUtils;
    MenuItem actionFavorite;

    private OnFragmentInteractionListener mCallbacks;

    /*
    must pass a TileObject for the fragment to use
     */
    public static FragDetails newInstance(String tileParam) {
        FragDetails fragment = new FragDetails();
        Bundle args = new Bundle();
        args.putString(Constants.PARAM_TILE_KEY, tileParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        /*
        here we're prioritizing data sources for populating tileObject
        if the instance state isn't null but the arguments are set
        then go with arguments
         */
        if (savedInstanceState != null) {
            tileObject = TileObject.create(savedInstanceState.getString(Constants.PARAM_TILE_KEY));
            detailsObject = DetailsObject.create(savedInstanceState.getString(Constants.PARAM_DETAILS_KEY));
        }
        if (getArguments() != null) {
            tileObject = TileObject.create(getArguments().getString(Constants.PARAM_TILE_KEY));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_details, container, false);
        ButterKnife.inject(this, rootView);

        tvBody.setTextIsSelectable(true);
        tvBody.setMovementMethod(LinkMovementMethod.getInstance());

        tvZeroStateInfo.setTypeface(FontFactory.getRegular(getActivity()));
        tvRetry.setTypeface(FontFactory.getCondensedLight(getActivity()));

        titleBgColor = getResources().getColor(R.color.title_background);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                getActivity().overridePendingTransition(R.anim.fade_in_shadow, R.anim.slide_out_right);
            }
        });
        toolbar.inflateMenu(R.menu.menu_act_details);

        favoriteUtils = new FavoriteUtils(getActivity());
        actionFavorite = toolbar.getMenu().findItem(R.id.action_favorite);
        if (favoriteUtils.isFavorited(tileObject)) {
            actionFavorite.setIcon(R.drawable.ic_favorite_white_24dp);
            actionFavorite.setTitle(R.string.remove_favorite);
        } else {
            actionFavorite.setIcon(R.drawable.ic_favorite_outline_white_24dp);
            actionFavorite.setTitle(R.string.add_favorite);
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                String filename;
                String path;
                File f;
                Uri imgUri;
                switch (id) {
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
                        filename = ImageUtils.saveImage(ivDisplay, successfulSrc, getActivity(), false);
                        path = Constants.imageDirectory() + filename;
                        f = new File(path);
                        imgUri = Uri.fromFile(f);

                        Log.i(TAG, "imgUri" + imgUri);
                        if (imgUri != null) {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("*/*");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, "via Hubble Gallery"); // TODO replace with shortened playstore link
                            shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);

                            startActivity(Intent.createChooser(shareIntent, getActivity().getResources().getString(R.string.share_image)));
                        } else {
                            Toasty.show(getActivity(), R.string.error_saving_image, Toasty.LENGTH_LONG);
                        }

                        break;
                    case R.id.action_save_image:
                        ImageUtils.saveImage(ivDisplay, successfulSrc, getActivity(), true);
                        break;
                    case R.id.action_set_image:
                        filename = ImageUtils.saveImage(ivDisplay, successfulSrc, getActivity(), false);
                        path = Constants.imageDirectory() + filename;
                        f = new File(path);
                        imgUri = Uri.fromFile(f);

                        Log.i(TAG, "imgUri" + imgUri);
                        if (imgUri != null) {
                            PackageManager pm = getActivity().getPackageManager();
                            Intent attachIntent = new Intent(Intent.ACTION_ATTACH_DATA);
                            attachIntent.setDataAndType(imgUri, "image/jpeg");
                            Intent openInChooser = Intent.createChooser(attachIntent, getActivity().getResources().getString(R.string.set_as));

                            List<ResolveInfo> resInfo = pm.queryIntentActivities(attachIntent, 0);
                            Intent[] extraIntents = new Intent[resInfo.size()];
                            for (int i = 0; i < resInfo.size(); i++) {
                                ResolveInfo ri = resInfo.get(i);
                                String packageName = ri.activityInfo.packageName;
                                Intent intent = new Intent();
                                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                                intent.setAction(Intent.ACTION_ATTACH_DATA);
                                intent.setDataAndType(imgUri, "image/jpeg");
                                extraIntents[i] = new Intent(intent);
                            }
                            openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
                            startActivity(openInChooser);
                        } else {
                            Toasty.show(getActivity(), R.string.error_saving_image, Toasty.LENGTH_LONG);
                        }
                        break;
                    case R.id.action_open_in_browser:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://hubblesite.org" + tileObject.getHref()));
                        startActivity(browserIntent);
                        break;
                    case R.id.action_share_link:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, tileObject.getTitle() + " - http://hubblesite.org" + tileObject.getHref() + " - Hubble Gallery for Android");
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        break;
                    case R.id.action_copy_link:
                        @SuppressWarnings("deprecation")
                        ClipboardManager _clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        _clipboard.setText("http://hubblesite.org" + tileObject.getHref());
                        Toasty.show(getActivity(), "Copied link to clipboard", Toasty.LENGTH_MEDIUM);
                        break;
                }
                return true;
            }
        });

        tvTitle.setText(tileObject.getTitle());
        tvTitle.setTypeface(FontFactory.getMedium(getActivity()));

        tvBody.setTypeface(FontFactory.getRegular(getActivity()));
        if (savedInstanceState != null && detailsObject != null) {
            tvBody.setText(Html.fromHtml(detailsObject.getDescription()));
        }

        ivDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        scrollView.setScrollViewCallbacks(this);

        showLoadingAnimation(true, 0);
        if (savedInstanceState != null) {
            loadImage(tileObject.getSrc());
            onScrollChanged(scrollView.getCurrentScrollY(), false, false);
            alphaTitleBgColor = savedInstanceState.getInt(Constants.ALPHA_TITLE);
            toolbar.setBackgroundColor(alphaTitleBgColor);

            if (detailsObject == null) {
                loadPage();
            } else if (detailsObject.getDescription() == null || detailsObject.getDescription().length() == 0) {
                loadPage();
            }
        } else {
            loadPage();
        }

        return rootView;
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
        Picasso.with(getActivity()).load(nSrc).into(ivDisplay, new Callback() {
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
                    if (getActivity() != null)
                        getActivity().getWindow().setStatusBarColor(palette.getDarkMutedColor(android.R.color.black));
                }

                Palette.Swatch swatch = palette.getDarkVibrantSwatch();
                if (swatch != null) {
                    tvTitle.setTextColor(swatch.getTitleTextColor());
                }

            }
        });
    }

    private void showDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment newFragment = imageViewerDialogFragment.newInstance();
        newFragment.show(ft, "dialog");
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
                        YoYo.with(Techniques.FadeInUp).duration(240).playOn(tvBody);
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

    public SquareFlipper squareFlipper = new SquareFlipper();

    public void showSquareFlipper(boolean show) {
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

        @InjectView(R.id.ivTouch)
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
            ButterKnife.inject(this, rootView);

            Picasso.with(getActivity()).load(successfulSrc).into(ivTouch, new Callback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError() {
                    Log.i(TAG, "onError");
                }
            });

            return rootView;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outstate) {
        outstate.putString(Constants.PARAM_TILE_KEY, tileObject.serialize());
        if (detailsObject != null)
            outstate.putString(Constants.PARAM_DETAILS_KEY, detailsObject.serialize());
        outstate.putInt(Constants.ALPHA_TITLE, alphaTitleBgColor);
        super.onSaveInstanceState(outstate);
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        float alpha = 1 - (float) Math.max(0, ivDisplay.getHeight() - scrollY) / ivDisplay.getHeight();
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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
