package com.derek_s.hubble_gallery.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.derek_s.hubble_gallery.R;
import com.derek_s.hubble_gallery.ui.adapters.SectionsAdapter;
import com.derek_s.hubble_gallery.base.FragBase;
import com.derek_s.hubble_gallery.ui.activities.ActMain;
import com.derek_s.hubble_gallery.ui.dialog.DialogAbout;
import com.derek_s.hubble_gallery.ui.presenters.NavigationPresenter;
import com.derek_s.hubble_gallery.ui.views.NavigationView;
import com.derek_s.hubble_gallery.ui.widgets.AnimatedExpandableListView;
import com.derek_s.hubble_gallery.utils.ui.FontFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragNavigationDrawer extends FragBase implements NavigationView {

    private static final String SELECTED_POSITIONS = "selected_positions";
    private ActionBarDrawerToggle mDrawerToggle;
    public static DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;
    private static Context context;
    @Bind(R.id.lv_menu)
    public AnimatedExpandableListView lvMenu;
    public SectionsAdapter mAdapter;
    private boolean mFromSavedInstanceState;
    public static ArrayList<Integer> mCurSelectedPositions;

    private NavigationPresenter presenter;

    public FragNavigationDrawer() {
    }

    public static void openDrawer() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    public static void closeDrawer() {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    public static void lockDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public static void unlockDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public static boolean isOpen() {
        return mDrawerLayout.isDrawerOpen(Gravity.LEFT);
    }

    public static void toggleDrawerState() {
        if (isOpen()) {
            closeDrawer();
        } else {
            openDrawer();
        }
    }

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        presenter = new NavigationPresenter(this);
        context = getActivity();
        if (savedState != null) {
            mCurSelectedPositions = savedState.getIntegerArrayList(SELECTED_POSITIONS);
            mFromSavedInstanceState = true;
        } else {
            updateSelectedItem(0, -1, "Entire Collection");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private static TextView tvFavorites, tvRate, tvAbout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_nav_drawer, container, false);
        ButterKnife.bind(this, rootView);

        // header
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.item_header_nav_drawer, lvMenu, false);
        TextView tvVersionName = (TextView) header.findViewById(R.id.tv_version_name);
        tvVersionName.setTypeface(FontFactory.getCondensedRegular(getActivity()));
        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException ex) {
            ex.printStackTrace();
        }
        tvVersionName.setText("BETA V " + pInfo.versionName);

        // footer TODO
        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.item_footer_nav_drawer, lvMenu, false);
        tvFavorites = (TextView) footer.findViewById(R.id.tv_favorites);
        tvFavorites.setTypeface(FontFactory.getMedium(getActivity()));
        tvFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSelectedItem(-2, -2, "Favorites");
                mAdapter.notifyDataSetChanged();
                ActMain.instance.mTitle = "Favorites";
                tvFavorites.setBackgroundColor(getResources().getColor(R.color.focused_color));
                tvFavorites.setTextColor(getResources().getColor(R.color.seleted_item_color));
                ActMain.instance.restoreActionBar();
                if (ActMain.instance.toolbar != null)
                    ActMain.instance.showToolbar();
                closeDrawer();
                ActMain.instance.fragMain.openFavorites(true);
            }
        });
        tvRate = (TextView) footer.findViewById(R.id.tv_rate);
        tvRate.setTypeface(FontFactory.getMedium(getActivity()));
        tvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = context.getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        tvAbout = (TextView) footer.findViewById(R.id.tv_about);
        tvAbout.setTypeface(FontFactory.getMedium(getActivity()));
        tvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAbout dialogAbout = new DialogAbout(getActivity());
                dialogAbout.displayDialog();
            }
        });
        // TODO

        mAdapter = new SectionsAdapter(getActivity(), getActivity());
        lvMenu.addFooterView(footer, null, false);
        lvMenu.addHeaderView(header, null, false);
        lvMenu.setAdapter(mAdapter);
        mAdapter.addItems();

        /*
        on Lollipop add padding for status bar height
         */
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lvMenu.setPadding(0, getStatusBarHeight(), 0, 0);
        }

        if (savedInstanceState != null) {
            if (mCurSelectedPositions.get(0) == -2) {
                tvFavorites.setBackgroundColor(getResources().getColor(R.color.focused_color));
                tvFavorites.setTextColor(getResources().getColor(R.color.seleted_item_color));
            }
        }

        return rootView;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.LEFT);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
            }
        };

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public static void updateSelectedItem(int groupPosition, int childPosition, String title) {
        if (groupPosition != -2 && tvFavorites != null) {
            tvFavorites.setBackgroundResource(R.drawable.selector_default);
            tvFavorites.setTextColor(context.getResources().getColor(R.color.body_dark_theme));
        }
        /*
        if childPosition == -1, then there are no children
        for the group item
         */
        mCurSelectedPositions = new ArrayList<>();
        mCurSelectedPositions.add(0, groupPosition);
        mCurSelectedPositions.add(1, childPosition);
        ActMain.instance.mTitle = title;
        ActMain.instance.restoreActionBar();
        if (ActMain.instance.toolbar != null)
            ActMain.instance.showToolbar();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putIntegerArrayList(SELECTED_POSITIONS, mCurSelectedPositions);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

}
