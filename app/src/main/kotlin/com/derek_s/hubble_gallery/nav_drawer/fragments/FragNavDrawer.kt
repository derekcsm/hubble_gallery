package com.derek_s.hubble_gallery.nav_drawer.fragments;

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.Bind
import butterknife.ButterKnife
import com.derek_s.hubble_gallery.R
import com.derek_s.hubble_gallery.nav_drawer.model.SectionChildObject
import com.derek_s.hubble_gallery.nav_drawer.presenters.NavigationPresenter
import com.derek_s.hubble_gallery.nav_drawer.views.NavigationView

interface NavDrawerListeners {
  val toolbar: Toolbar
    get

  fun selectSection(section: SectionChildObject)
}

class FragNavDrawer : Fragment(), NavigationView {

  private var mDrawerToggle: ActionBarDrawerToggle? = null
  private var mDrawerLayout: DrawerLayout? = null
  private var presenter: NavigationPresenter? = null

  @Bind(R.id.rv_drawer)
  lateinit var rvDrawer: RecyclerView

  private var mCallbacks: NavDrawerListeners? = null

  override fun onCreate(savedState: Bundle?) {
    super.onCreate(savedState)
    presenter = NavigationPresenter(this, getContext())
  }

  override fun onAttach(context: Context) {
    super.onAttach(context);
    var act: Activity
    if (context is Activity) {
      act = context
      try {
        mCallbacks = act as NavDrawerListeners
      } catch (e: ClassCastException) {
        throw ClassCastException(act.toString() + " must implement listeners")
      }

    } else {
      throw ClassCastException("Must be attached to activity & implement listeners")
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {

    val rootView = inflater.inflate(R.layout.frag_nav_drawer, container, false)
    ButterKnife.bind(this, rootView)

    presenter!!.populateAdapter()

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


    //        if (savedInstanceState != null) {
    //            if (mCurSelectedPositions.get(0) === -2) {
    //                tvFavorites.setBackgroundColor(getResources().getColor(R.color.focused_color))
    //                tvFavorites.setTextColor(getResources().getColor(R.color.seleted_item_color))
    //            }
    //        }
    return rootView
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    mDrawerToggle?.onConfigurationChanged(newConfig)
  }

  fun setUp(drawerLayout: DrawerLayout) {
    mDrawerLayout = drawerLayout
    mDrawerLayout?.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START)

    mDrawerToggle = object : ActionBarDrawerToggle(getActivity(),
        drawerLayout,
        mCallbacks!!.toolbar,
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
      override fun run() {
        mDrawerToggle?.syncState()
      }
    })
    mDrawerLayout?.setDrawerListener(mDrawerToggle)
  }

  fun openDrawer() {
    mDrawerLayout!!.openDrawer(Gravity.LEFT)
  }

  fun closeDrawer() {
    mDrawerLayout!!.closeDrawer(Gravity.LEFT)
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

  override fun selectSection(section: SectionChildObject) {
    closeDrawer()
    mCallbacks!!.selectSection(section)

    // TODO select position
  }

  override val recycler: RecyclerView
    get() = rvDrawer
}