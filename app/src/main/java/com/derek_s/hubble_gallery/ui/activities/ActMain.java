package com.derek_s.hubble_gallery.ui.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.derek_s.hubble_gallery.R;
import com.derek_s.hubble_gallery.base.Constants;
import com.derek_s.hubble_gallery.base.TinyDB;
import com.derek_s.hubble_gallery.model.TileObject;
import com.derek_s.hubble_gallery.ui.fragments.FragMain;
import com.derek_s.hubble_gallery.ui.fragments.FragNavigationDrawer;
import com.derek_s.hubble_gallery.utils.network.NetworkUtil;
import com.derek_s.hubble_gallery.utils.ui.Toasty;
import com.derek_s.hubble_gallery.utils.ui.ToolbarTitle;
import com.github.ksoichiro.android.observablescrollview.ObservableGridView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.Scrollable;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.fabric.sdk.android.Fabric;


public class ActMain extends ActionBarActivity implements FragMain.FragMainCallbacks {

    private static String TAG = "ActMain";
    private static String CUR_TITLE = "current_title";
    public FragMain fragMain;
    public FragNavigationDrawer mNavigationDrawerFragment;
    private DrawerLayout mDrawerLayout;
    public String mTitle = "";
    public static ActMain instance = null;
    private TinyDB DB;
    @InjectView(R.id.toolbar)
    public Toolbar toolbar;
    @InjectView(R.id.switcher_title)
    TextSwitcher switcherTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        instance = this;
        setContentView(R.layout.act_main);
        ButterKnife.inject(this);

        DB = new TinyDB(this);
        if (!DB.getBoolean(Constants.ONBOARDING_SHOWN)) {
            /*
            show user on-boarding screen
             */
            DB.putBoolean(Constants.ONBOARDING_SHOWN, true);
            Intent intent = new Intent(ActMain.this, ActOnboarding.class);
            startActivity(intent);
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavigationDrawerFragment.toggleDrawerState();
            }
        });
        toolbar.inflateMenu(R.menu.act_main);
        ToolbarTitle toolbarTitle = new ToolbarTitle();
        switcherTitle = toolbarTitle.init(switcherTitle, instance);

        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString(CUR_TITLE);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.primary_dark));

        mNavigationDrawerFragment = (FragNavigationDrawer)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragMain = (FragMain) fragmentManager.findFragmentById(R.id.container);
        if (fragMain == null) {
            fragMain = new FragMain();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragMain)
                    .commit();
        }

        if (fragMain.hiRes) {
            MenuItem res = toolbar.getMenu().findItem(R.id.filter_resolution);
            res.setChecked(true);
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);

            TextView currentView = (TextView) switcherTitle.getCurrentView();

            if (currentView.getText().toString().equals(mTitle)) {
            /*
            don't animate
             */
                switcherTitle.setCurrentText(mTitle);
            } else {
            /*
            animate!
             */
                switcherTitle.setText(mTitle);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(CUR_TITLE, mTitle);
        super.onSaveInstanceState(outState);
    }

    private Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        getMenuInflater().inflate(R.menu.act_main, menu);
        restoreActionBar();

        if (fragMain.hiRes) {
            MenuItem res = menu.findItem(R.id.filter_resolution);
            res.setChecked(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_popular:
                item.setChecked(true);
                fragMain.hiRes = false;
                fragMain.loadInitialItems(fragMain.currentQuery);
                break;
            case R.id.filter_resolution:
                item.setChecked(true);
                fragMain.hiRes = true;
                fragMain.loadInitialItems(fragMain.currentQuery);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGridItemClicked(TileObject tileObject) {
        if (NetworkUtil.isConnected(this)) {
            Intent intent = new Intent(this, ActDetails.class);
            intent.putExtra(Constants.PARAM_TILE_KEY, tileObject.serialize());
            startActivity(intent);
            this.overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out_shadow);
        } else {
            Toasty.show(this, R.string.no_connection, Toasty.LENGTH_MEDIUM);
        }
    }


    @Override
    public void adjustToolbar(ScrollState scrollState, ObservableGridView gridView) {
        int toolbarHeight = toolbar.getHeight();
        final Scrollable scrollable = gridView;
        if (scrollable == null) {
            return;
        }
        int scrollY = scrollable.getCurrentScrollY();
        if (scrollState == ScrollState.DOWN) {
            showToolbar();
        } else if (scrollState == ScrollState.UP) {
            if (toolbarHeight <= scrollY) {
                hideToolbar();
            } else {
                showToolbar();
            }
        }
    }

    public void toggleFilterVisible(boolean visible) {
        Log.i(TAG, "toggleFilterVisible " + visible);
        MenuItem actionFilter = toolbar.getMenu().findItem(R.id.action_filter);
        MenuItem actionFilterPopular = toolbar.getMenu().findItem(R.id.filter_popular);
        MenuItem actionFilterResolution = toolbar.getMenu().findItem(R.id.filter_resolution);
        if (actionFilter != null)
            if (visible) {
                actionFilter.setVisible(true);
                actionFilterPopular.setVisible(true);
                actionFilterResolution.setVisible(true);
            } else {
                actionFilter.setVisible(false);
                actionFilterPopular.setVisible(false);
                actionFilterResolution.setVisible(false);
            }
    }

    boolean toolbarIsShown = true;

    public void showToolbar() {
        if (!toolbarIsShown)
            expand(toolbar);
    }

    public void hideToolbar() {
        if (toolbarIsShown)
            collapse(toolbar);
    }

    public void expand(final View v) {
        YoYo.with(Techniques.FadeInDown).duration(200).playOn(toolbar);
        toolbarIsShown = true;
        v.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RelativeLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(200);
        v.startAnimation(a);
    }

    public void collapse(final View v) {
        YoYo.with(Techniques.FadeOutUp).duration(100).playOn(toolbar);
        toolbarIsShown = false;
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(200);
        v.startAnimation(a);
    }
}
