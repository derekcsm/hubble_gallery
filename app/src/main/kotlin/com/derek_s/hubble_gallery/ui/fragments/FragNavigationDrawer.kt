package com.derek_s.hubble_gallery.ui.fragments;

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.Bind
import butterknife.ButterKnife
import com.derek_s.hubble_gallery.R
import com.derek_s.hubble_gallery.ui.presenters.NavigationPresenter
import com.derek_s.hubble_gallery.ui.views.NavigationView
import java.util.*

class FragNavigationDrawer : Fragment(), NavigationView {

    private var mDrawerToggle: ActionBarDrawerToggle? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var presenter: NavigationPresenter? = null

    private val SELECTED_POSITIONS = "selected_positions"
    var mCurSelectedPositions = ArrayList<Int>()

    @Bind(R.id.rv_drawer)
    lateinit var rvDrawer: RecyclerView

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        presenter = NavigationPresenter(this)
        if (savedState != null)
            mCurSelectedPositions = savedState.getIntegerArrayList(SELECTED_POSITIONS)
        else
            updateSelectedItem(0, -1, "Entire Collection")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.frag_nav_drawer, container, false)
        ButterKnife.bind(this, rootView)

        // header
        // ViewGroup header = (ViewGroup) inflater.inflate(R.layout.item_header_nav_drawer, lvMenu, false);
        // TextView tvVersionName = (TextView) header.findViewById(R.id.tv_version_name);
        // tvVersionName.setTypeface(FontFactory.getCondensedRegular(getActivity()));
        // PackageInfo pInfo = null;
        // try {
        // pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        // } catch (PackageManager.NameNotFoundException ex) {
        // ex.printStackTrace();
        // }
        // tvVersionName.setText("BETA V " + pInfo.versionName);
        // adapter TODO
        // mAdapter = new SectionsAdapter(getActivity(), getActivity());
        // lvMenu.addFooterView(footer, null, false);
        // lvMenu.addHeaderView(header, null, false);
        // lvMenu.setAdapter(mAdapter);
        // mAdapter.addItems();
        /*
     on Lollipop add padding for status bar height
     */
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rvDrawer.setPadding(0, statusBarHeight, 0, 0)
        }
        //        if (savedInstanceState != null) {
        //            if (mCurSelectedPositions.get(0) === -2) {
        //                tvFavorites.setBackgroundColor(getResources().getColor(R.color.focused_color))
        //                tvFavorites.setTextColor(getResources().getColor(R.color.seleted_item_color))
        //            }
        //        }
        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putIntegerArrayList(SELECTED_POSITIONS, mCurSelectedPositions)
        super.onSaveInstanceState(outState)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle?.onConfigurationChanged(newConfig)
    }

    val statusBarHeight: Int
        get() {
            var result = 0
            val resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = getResources().getDimensionPixelSize(resourceId)
            }
            return result
        }

    fun setUp(drawerLayout: DrawerLayout) {
        mDrawerLayout = drawerLayout
        mDrawerLayout?.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START)

//        val actionBar = actionBar
//        actionBar.setDisplayHomeAsUpEnabled(true)
//        actionBar.setHomeButtonEnabled(true)

        mDrawerToggle = object : ActionBarDrawerToggle(getActivity(),
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                if (!isAdded())
                    return
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                if (!isAdded())
                    return
            }
        }
        mDrawerLayout?.post(object : Runnable {
            public override fun run() {
                mDrawerToggle?.syncState()
            }
        })
        mDrawerLayout?.setDrawerListener(mDrawerToggle)
    }


    fun openDrawer() {
        mDrawerLayout?.openDrawer(Gravity.LEFT)
    }

    fun closeDrawer() {
        mDrawerLayout?.closeDrawer(Gravity.LEFT)
    }

    fun lockDrawer() {
        mDrawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    fun unlockDrawer() {
        mDrawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    val isOpen: Boolean
        get() {
            if (mDrawerLayout == null)
                return false
            else
                return mDrawerLayout!!.isDrawerOpen(Gravity.LEFT)
        }

    fun toggleDrawerState() {
        if (isOpen) {
            closeDrawer()
        } else {
            openDrawer()
        }
    }

    fun updateSelectedItem(groupPosition: Int, childPosition: Int, title: String) {
        // if (groupPosition != -2 && tvFavorites != null) {
        // tvFavorites.setBackgroundResource(R.drawable.selector_default);
        // tvFavorites.setTextColor(context.getResources().getColor(R.color.body_dark_theme));
        // }
        /*
   if childPosition == -1, then there are no children
   for the group item
   */
        mCurSelectedPositions = ArrayList()
        mCurSelectedPositions.add(0, groupPosition)
        mCurSelectedPositions.add(1, childPosition)
        // ActMain.instance.mTitle = title;
        // ActMain.instance.restoreActionBar();
        // if (ActMain.instance.toolbar != null)
        // ActMain.instance.showToolbar();
    }

}