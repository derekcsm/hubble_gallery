package com.derek_s.hubble_gallery.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.derek_s.hubble_gallery.R;
import com.derek_s.hubble_gallery._shared.model.TileObject;
import com.derek_s.hubble_gallery._shared.model.Tiles;
import com.derek_s.hubble_gallery.api.GetAlbum;
import com.derek_s.hubble_gallery.base.Constants;
import com.derek_s.hubble_gallery.base.FragBase;
import com.derek_s.hubble_gallery.ui.adapters.MainGridAdapter;
import com.derek_s.hubble_gallery.ui.adapters.RecyclerViewItemClickListener;
import com.derek_s.hubble_gallery.utils.Animation.SquareFlipper;
import com.derek_s.hubble_gallery.utils.ui.FontFactory;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragMain extends FragBase implements ObservableScrollViewCallbacks {

  public static int currentPage = 1;
  private static String CURRENT_PAGE = "current_page";
  private static String CURRENT_TILES = "current_tiles";
  private static String CAN_LOAD_MORE = "can_load_more";
  private static String IS_HIRES = "is_hires";
  private static String CURRENT_QUERY = "current_query";
  public GridLayoutManager gManager;
  public MainGridAdapter mAdapter;
  public boolean isLoading = false;
  public boolean canLoadMore = true;
  public boolean hiRes = false;
  public String currentQuery = "entire";
  public int loadAmount = 128;
  public int mode = 0;
  public SquareFlipper squareFlipper = new SquareFlipper();
  @Bind(R.id.rv_main)
  ObservableRecyclerView rvMain;
  @Bind(R.id.square)
  View square;
  @Bind(R.id.zero_state)
  RelativeLayout zeroState;
  @Bind(R.id.tv_zero_state)
  TextView tvZeroTitle;
  @Bind(R.id.tv_retry)
  TextView tvRetry;
  private FragMainCallbacks mCallbacks;

  @Override
  public void onCreate(Bundle savedState) {
    super.onCreate(savedState);
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
    ButterKnife.bind(this, rootView);


    rvMain.setScrollViewCallbacks(this);
    rvMain.setTouchInterceptionViewGroup((ViewGroup) getActivity().findViewById(R.id.container));
    if (getActivity() instanceof ObservableScrollViewCallbacks) {
      rvMain.setScrollViewCallbacks((ObservableScrollViewCallbacks) getActivity());
    }
    int gridRows = getContext().getResources().getInteger(R.integer.grid_columns);
    gManager = new GridLayoutManager(getContext(), gridRows);
    rvMain.setLayoutManager(gManager);
    mAdapter = new MainGridAdapter(getActivity(), getActivity());
    rvMain.setAdapter(mAdapter);

    if (savedInstanceState == null) {
      loadInitialItems(currentQuery);
    } else {
      currentQuery = savedInstanceState.getString(CURRENT_QUERY);
      canLoadMore = savedInstanceState.getBoolean(CAN_LOAD_MORE);
      hiRes = savedInstanceState.getBoolean(IS_HIRES);

      Gson gson = new Gson();
      Tiles nTiles = gson.fromJson(savedInstanceState.getString(CURRENT_TILES), Tiles.class);
      if (nTiles.getTiles().isEmpty()) {
        loadInitialItems(currentQuery);
      } else {
        mAdapter.addItems(gson.fromJson(savedInstanceState.getString(CURRENT_TILES), Tiles.class).getTiles());
      }
    }

    /*
    set scroll listener after either initial load
    or saved state re-instantiation, to avoid rustling jimmies
    */
    rvMain.setOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
      }

      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
              /*
         * Algorithm to check if the last item is visible or not, only load more if
         * we're NOT in favorites mode
         */
        if (mode == Constants.LOADED_MODE || mode == Constants.SEARCH_MODE) {
          int visibleItemCount = recyclerView.getChildCount();
          int totalItemCount = gManager.getItemCount();
          int firstVisibleItem = gManager.findFirstVisibleItemPosition();
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
                mAdapter.addItems(result);
                canLoadMore = (result.size() == loadAmount);
                isLoading = false;
              }
            });
          }
        }
      }
    });

    rvMain.addOnItemTouchListener(new RecyclerViewItemClickListener(getContext(),
        new RecyclerViewItemClickListener.OnItemClickListener() {
          @Override
          public void onItemClick(View v, int position) {
            if (mCallbacks != null) {
              mCallbacks.onGridItemClicked(mAdapter.getItemAtPosition(position));
            }
          }
        }));

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
    mAdapter.clear();
    //ActHome.instance.toggleFilterVisible(true);
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
        rvMain.smoothScrollToPosition(0);
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
    //ActHome.instance.toggleFilterVisible(false);
    mode = Constants.FAVORITES_MODE;
    mAdapter.clear();
    if (favoriteUtils.getFavorites() != null) {
      mAdapter.addItems(favoriteUtils.getFavorites().getTiles());
      if (scroll)
        rvMain.smoothScrollToPosition(0);
    } else {
      showZeroState(true);
    }

    if (mAdapter.getItemCount() == 0) {
      showZeroState(true);
    }
  }

  public void showLoadingAnimation(boolean show) {
    showZeroState(false);
    if (show) {
      showSquareFlipper(true);
      YoYo.with(Techniques.FadeOut).duration(200).playOn(rvMain);
    } else {
      showSquareFlipper(false);
      YoYo.with(Techniques.FadeIn).duration(320).playOn(rvMain);
    }
  }

  public void showSquareFlipper(boolean show) {
    if (show) {
      squareFlipper.startAnimation(square);
    } else {
      squareFlipper.stopAnimation();
    }
  }

  public void showZeroState(boolean show) {

    if (show) {
      tvZeroTitle.setTypeface(FontFactory.getCondensedRegular(getActivity()));

      if (mode == Constants.FAVORITES_MODE) {
        tvRetry.setVisibility(View.GONE);
        int min = 0;
        int max = 2;
        int zero = min + (int) (Math.random() * ((max - min) + 1));

        switch (zero) {
          case 0:
            tvZeroTitle.setText(R.string.zero_state_favorites_1);
            break;
          case 1:
            tvZeroTitle.setText(R.string.zero_state_favorites_2);
            break;
          case 2:
            tvZeroTitle.setText(R.string.zero_state_favorites_3);
            break;
        }

      } else {
        // bad connection
        tvRetry.setVisibility(View.VISIBLE);
        tvZeroTitle.setText(R.string.no_connection);
        tvRetry.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            loadInitialItems(currentQuery);
          }
        });
      }

      showSquareFlipper(false);
      YoYo.with(Techniques.FadeOut).duration(200).playOn(rvMain);

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
    if (scrollState != null)
      mCallbacks.adjustToolbar(scrollState, rvMain);
  }

  public interface FragMainCallbacks {
    void onGridItemClicked(TileObject tileObject);

    void adjustToolbar(ScrollState scrollState, ObservableRecyclerView gridView);
  }

}
