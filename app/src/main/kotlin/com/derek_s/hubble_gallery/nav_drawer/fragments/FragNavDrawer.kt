package com.derek_s.hubble_gallery.nav_drawer.fragments;

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.derek_s.hubble_gallery.R
import com.derek_s.hubble_gallery.nav_drawer.dialog.DialogAbout
import com.derek_s.hubble_gallery.nav_drawer.dialog.DialogAboutListener
import com.derek_s.hubble_gallery.nav_drawer.model.SectionChildObject
import com.derek_s.hubble_gallery.nav_drawer.presenters.NavigationPresenter
import com.derek_s.hubble_gallery.nav_drawer.views.NavigationView
import com.derek_s.hubble_gallery.ui.activities.ActWelcome
import com.derek_s.hubble_gallery.utils.ui.FontFactory

interface NavDrawerListeners {
  val toolbar: Toolbar
    get

  fun selectSection(section: SectionChildObject)

  fun openFavorites(scroll: Boolean)
}

class FragNavDrawer : Fragment(), NavigationView {

  private var mDrawerToggle: ActionBarDrawerToggle? = null
  private var mDrawerLayout: DrawerLayout? = null
  private var presenter: NavigationPresenter? = null

  lateinit var rvDrawer: RecyclerView
  lateinit var llFooterItems: LinearLayout
  lateinit var tvAbout: TextView
  lateinit var tvRate: TextView
  lateinit var rlFavorites: RelativeLayout
  lateinit var tvFavorites: TextView

  private var mCallbacks: NavDrawerListeners? = null
  private var dialogAbout: DialogAbout? = null

  override fun onCreate(savedState: Bundle?) {
    super.onCreate(savedState)
    presenter = NavigationPresenter(this, context!!)
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

    rvDrawer = rootView.findViewById(R.id.rv_drawer)
    llFooterItems = rootView.findViewById(R.id.ll_footer_items)
    tvAbout = rootView.findViewById(R.id.tv_about)
    tvRate = rootView.findViewById(R.id.tv_rate)
    rlFavorites = rootView.findViewById(R.id.rl_favorites)
    tvFavorites = rootView.findViewById(R.id.tv_favorites)

    beautifyViews();
    presenter!!.populateAdapter()
    presenter!!.restoreState(savedInstanceState)

    llFooterItems.setOnClickListener {} // intentionally empty

    dialogAbout = DialogAbout(context!!, object : DialogAboutListener {
      override fun onShowIntroClicked() {
        activity!!.startActivity(Intent(activity, ActWelcome::class.java))
      }
    })
    tvAbout.setOnClickListener {
      dialogAbout!!.show()
    }

    tvRate.setOnClickListener {
      var appPackageName = context!!.packageName;
      try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
      } catch (anfe: android.content.ActivityNotFoundException) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
      }
    }

    if (presenter!!.getSelectedQuery().equals("favorites")) {
      setFavoritesSelectedUI(true)
    } else {
      setFavoritesSelectedUI(false)
    }

    rlFavorites.setOnClickListener {
      mCallbacks!!.openFavorites(true)
      closeDrawer()
      setFavoritesSelectedUI(true)
      presenter!!.setSelectedQuery("favorites")
    }

    return rootView
  }

  override fun onSaveInstanceState(outState: Bundle) {
    if (presenter == null)
      return

    if (presenter!!.getSelectedQuery() != null)
      outState.putString(presenter!!.KEY_SELECTED_QUERY, presenter!!.getSelectedQuery())

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
        if (!isAdded)
          return
      }

      override fun onDrawerOpened(drawerView: View) {
        super.onDrawerOpened(drawerView)
        if (!isAdded)
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

  private fun setFavoritesSelectedUI(isSelected: Boolean) {
    val context = context!!
    if (isSelected) {
      rlFavorites.setBackgroundColor(ContextCompat.getColor(context, R.color.focused_color));
      tvFavorites.setTextColor(ContextCompat.getColor(context, R.color.seleted_item_color));
    } else {
      rlFavorites.setBackgroundResource(R.drawable.selector_default);
      tvFavorites.setTextColor(ContextCompat.getColor(context, R.color.body_dark_theme));
    }
  }

  override fun selectSection(section: SectionChildObject) {
    closeDrawer()
    mCallbacks!!.selectSection(section)
    setFavoritesSelectedUI(false)
  }

  override val recycler: RecyclerView
    get() = rvDrawer

  private fun beautifyViews() {
    tvAbout.typeface = FontFactory.getCondensedBold(context);
    tvRate.typeface = FontFactory.getCondensedBold(context);
    tvFavorites.typeface = FontFactory.getMedium(context);
  }
}