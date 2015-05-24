package com.derek_s.hubble_gallery.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.derek_s.hubble_gallery.R;
import com.derek_s.hubble_gallery.adapters.GridAdapter;
import com.derek_s.hubble_gallery.api.GetAlbum;
import com.derek_s.hubble_gallery.base.Constants;
import com.derek_s.hubble_gallery.model.TileObject;
import com.derek_s.hubble_gallery.model.Tiles;
import com.derek_s.hubble_gallery.ui.activities.ActMain;
import com.derek_s.hubble_gallery.ui.widgets.CircleProgressView;
import com.derek_s.hubble_gallery.utils.FavoriteUtils;
import com.derek_s.hubble_gallery.utils.ui.FontFactory;
import com.derek_s.hubble_gallery.utils.ui.IndeterminateAnimator;
import com.github.ksoichiro.android.observablescrollview.ObservableGridView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dereksmith on 15-02-26.
 */
public class FragMain extends Fragment implements ObservableScrollViewCallbacks {

    private static String CURRENT_PAGE = "current_page";
    private static String CURRENT_TILES = "current_tiles";
    private static String CAN_LOAD_MORE = "can_load_more";
    private static String IS_HIRES = "is_hires";
    private static String CURRENT_QUERY = "current_query";
    Context c;
    @InjectView(R.id.gv_main)
    ObservableGridView gvMain;
    @InjectView(R.id.cpv_grid)
    CircleProgressView cpvGrid;
    @InjectView(R.id.zero_state)
    RelativeLayout zeroState;
    public GridAdapter mAdapter;
    public static int currentPage = 1;
    public boolean isLoading = false;
    public boolean canLoadMore = true;
    public boolean hiRes = false;
    public String currentQuery = "entire";
    public int loadAmount = 128;
    private FragMainCallbacks mCallbacks;
    public int mode = 0;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        c = getActivity();
        if (savedState != null) {
            mode = savedState.getInt(Constants.MODE_KEY);
            currentPage = savedState.getInt(CURRENT_PAGE);
        } else {
            mode = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_main, container, false);
        ButterKnife.inject(this, rootView);

        gvMain.setScrollViewCallbacks(this);

        gvMain.setTouchInterceptionViewGroup((ViewGroup) getActivity().findViewById(R.id.container));

        if (getActivity() instanceof ObservableScrollViewCallbacks) {
            gvMain.setScrollViewCallbacks((ObservableScrollViewCallbacks) getActivity());
        }

        mAdapter = new GridAdapter(getActivity(), c);
        gvMain.setAdapter(mAdapter);


        if (savedInstanceState == null) {
            loadInitialItems(currentQuery);
        } else {
            currentQuery = savedInstanceState.getString(CURRENT_QUERY);
            canLoadMore = savedInstanceState.getBoolean(CAN_LOAD_MORE);
            hiRes = savedInstanceState.getBoolean(IS_HIRES);
            mAdapter.addItems(Tiles.create(savedInstanceState.getString(CURRENT_TILES)).getTiles());
        }

        /*
        set scroll listener after either initial load
        or saved state re-instantiation, to avoid rustling jimmies
         */
        gvMain.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                /*
                Algorithm to check if the last item is visible or not, only load more if
                we're NOT in favorites mode
                 */
                if (mode == Constants.LOADED_MODE || mode == Constants.SEARCH_MODE) {
                    final int lastItem = firstVisibleItem + visibleItemCount;
                    if (lastItem == totalItemCount && canLoadMore && !isLoading) {
                        isLoading = true;
                        // you have reached the end, load more data
                        final GetAlbum getAlbum = new GetAlbum(loadAmount, currentPage + 1, currentQuery, false);
                        getAlbum.execute();
                        //setting callback for AsyncTask class
                        getAlbum.setGetAlbumCompleteListener(new GetAlbum.OnTaskComplete() {
                            @Override
                            public void setTaskComplete(ArrayList<TileObject> result) {
                                mAdapter.addItemsToBottom(result);
                                canLoadMore = (result.size() == loadAmount);
                                isLoading = false;
                            }
                        });
                    }
                }
            }
        });

        gvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCallbacks != null) {
                    mCallbacks.onGridItemClicked(mAdapter.getItem(position));
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {

        if (mode == Constants.FAVORITES_MODE) {
            openFavorites(false);
        }

        super.onResume();
    }

    public void loadInitialItems(String query) {
        ActMain.instance.toggleFilterVisible(true);
        mode = Constants.LOADED_MODE;
        showLoadingAnimation(true);
        isLoading = true;
        currentQuery = query;
        final GetAlbum getAlbum = new GetAlbum(loadAmount, 1, query, hiRes);
        getAlbum.execute();
        getAlbum.setGetAlbumCompleteListener(new GetAlbum.OnTaskComplete() {
            @Override
            public void setTaskComplete(ArrayList<TileObject> result) {
                isLoading = false;
                /*
                scroll to the top (aka position 0)
                */
                gvMain.smoothScrollToPositionFromTop(0, 0);
                if (result == null || result.size() == 0) {
                    showZeroState(true);
                } else {
                    mAdapter.addItems(result);
                    canLoadMore = (result.size() == loadAmount);
                    showLoadingAnimation(false);
                }
            }
        });
    }

    public void openFavorites(boolean scroll) {
        ActMain.instance.toggleFilterVisible(false);
        mode = Constants.FAVORITES_MODE;
        FavoriteUtils favoriteUtils = new FavoriteUtils(getActivity());
        if (favoriteUtils.getFavorites() != null) {
            mAdapter.addItems(favoriteUtils.getFavorites().getTiles());
            if (scroll)
                gvMain.smoothScrollToPositionFromTop(0, 0);
        } else {
            showZeroState(true);
        }

        if (mAdapter.getCount() == 0) {
            showZeroState(true);
        }
    }

    public void showLoadingAnimation(boolean show) {
        showZeroState(false);
        if (show) {
            IndeterminateAnimator.show(cpvGrid, Techniques.FadeInUp);
            YoYo.with(Techniques.FadeOut).duration(200).playOn(gvMain);
        } else {
            IndeterminateAnimator.stop(cpvGrid, Techniques.FadeOutDown);
            YoYo.with(Techniques.FadeIn).duration(320).playOn(gvMain);
        }
    }

    public void showZeroState(boolean show) {

        if (show) {
            TextView tvZero = (TextView) zeroState.findViewById(R.id.tv_zero_state);
            tvZero.setTypeface(FontFactory.getCondensedRegular(getActivity()));

            if (mode == Constants.FAVORITES_MODE) {
                int min = 0;
                int max = 2;
                int zero = min + (int)(Math.random() * ((max - min) + 1));

                switch (zero) {
                    case 0:
                        tvZero.setText(R.string.zero_state_favorites_1);
                        break;
                    case 1:
                        tvZero.setText(R.string.zero_state_favorites_2);
                        break;
                    case 2:
                        tvZero.setText(R.string.zero_state_favorites_3);
                        break;
                }

            } else {
                // bad connection
                tvZero.setText(R.string.no_connection);
            }

            IndeterminateAnimator.stop(cpvGrid, Techniques.FadeOutDown);
            YoYo.with(Techniques.FadeOut).duration(200).playOn(gvMain);

            zeroState.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeInUp).duration(120).playOn(zeroState);

        } else {
            zeroState.setVisibility(View.INVISIBLE);
            /*
             TODO for test set listener with 120 sec delay
              after delay set visibility to invisible
              */
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (FragMainCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement FragMainCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Tiles mTiles = new Tiles();
        mTiles.setTiles(mAdapter.mTiles);
        outState.putString(CURRENT_TILES, mTiles.serialize());
        outState.putInt(CURRENT_PAGE, currentPage);
        outState.putBoolean(CAN_LOAD_MORE, canLoadMore);
        outState.putString(CURRENT_QUERY, currentQuery);
        outState.putBoolean(IS_HIRES, hiRes);
        outState.putInt(Constants.MODE_KEY, mode);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        mCallbacks.adjustToolbar(scrollState, gvMain);
    }

    public static interface FragMainCallbacks {
        void onGridItemClicked(TileObject tileObject);

        void adjustToolbar(ScrollState scrollState, ObservableGridView gridView);
    }

}
