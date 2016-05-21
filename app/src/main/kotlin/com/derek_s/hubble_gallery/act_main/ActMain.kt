package com.derek_s.hubble_gallery.act_main;

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.RelativeLayout
import android.widget.TextSwitcher
import android.widget.TextView
import butterknife.bindView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.derek_s.hubble_gallery.R
import com.derek_s.hubble_gallery._shared.model.TileObject
import com.derek_s.hubble_gallery.base.ActBase
import com.derek_s.hubble_gallery.base.Constants
import com.derek_s.hubble_gallery.base.TinyDB
import com.derek_s.hubble_gallery.internal.di.ActivityComponent
import com.derek_s.hubble_gallery.nav_drawer.fragments.FragNavDrawer
import com.derek_s.hubble_gallery.nav_drawer.fragments.NavDrawerListeners
import com.derek_s.hubble_gallery.nav_drawer.model.SectionChildObject
import com.derek_s.hubble_gallery.ui.activities.ActDetails
import com.derek_s.hubble_gallery.ui.activities.ActWelcome
import com.derek_s.hubble_gallery.ui.fragments.FragMain
import com.derek_s.hubble_gallery.utils.network.NetworkUtil
import com.derek_s.hubble_gallery.utils.ui.Toasty
import com.derek_s.hubble_gallery.utils.ui.ToolbarTitle
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView
import com.github.ksoichiro.android.observablescrollview.ScrollState
import org.jetbrains.annotations.NotNull
import javax.inject.Inject

class ActMain : ActBase(), FragMain.FragMainCallbacks, NavDrawerListeners {

  private val TAG = "ActMain"
  var mTitle = ""
  var fragMain: FragMain? = null
  var navDrawer: FragNavDrawer? = null

  override val toolbar: Toolbar by bindView(R.id.toolbar)
  var switcherTitle: TextSwitcher? = null
  val mDrawerLayout: DrawerLayout by bindView(R.id.drawer_layout)

  @Inject
  lateinit var db: TinyDB
  @Inject
  lateinit var networkUtil: NetworkUtil

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.act_main)
    /**
     * setup navigation drawer
     */
    navDrawer = supportFragmentManager.findFragmentById(R.id.navigation_drawer)as FragNavDrawer
    navDrawer!!.setUp(mDrawerLayout)
    if (!db.getBoolean(Constants.ONBOARDING_SHOWN)) {
      /*
       * TODO make an ActFirst Activity that directs to correct Activity instead of handling here
       * show user welcome screen
       */
      db.putBoolean(Constants.ONBOARDING_SHOWN, true)
      val intent = Intent(this, ActWelcome::class.java)
      startActivity(intent)
    }
    setSupportActionBar(toolbar)
    toolbar.setNavigationOnClickListener { navDrawer!!.toggleDrawerState() }
    toolbar.inflateMenu(R.menu.act_main)

    val toolbarTitle = ToolbarTitle()
    switcherTitle = findViewById(R.id.switcher_title) as TextSwitcher
    switcherTitle = toolbarTitle.init(switcherTitle, this)

    if (savedInstanceState != null) {
      mTitle = savedInstanceState.getString(CUR_TITLE)
    } else {
      // default selected item
      mTitle = "Entire Collection"
    }
    val fragmentManager = supportFragmentManager

    if (fragmentManager.findFragmentById(R.id.container) != null)
      fragMain = fragmentManager.findFragmentById(R.id.container) as FragMain
    if (fragMain == null) {
      fragMain = FragMain()
      fragmentManager.beginTransaction().replace(R.id.container, fragMain).commit()
    }
    if (fragMain!!.hiRes) {
      val res = toolbar.menu.findItem(R.id.filter_resolution)
      res.isChecked = true
    }
  }

  fun restoreActionBar() {
    val actionBar = supportActionBar
    if (actionBar != null) {
      actionBar.setDisplayShowTitleEnabled(false)
      val currentView = switcherTitle!!.currentView as TextView
      if (currentView.text.toString().equals(mTitle)) {
        // don't animate
        switcherTitle!!.setCurrentText(mTitle)
      } else {
        // animate
        switcherTitle!!.setText(mTitle)
      }
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    outState.putString(CUR_TITLE, mTitle)
    super.onSaveInstanceState(outState)
  }

  private var menu: Menu? = null

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    this.menu = menu
    menuInflater.inflate(R.menu.act_main, menu)
    restoreActionBar()
    if (fragMain!!.hiRes) {
      val res = menu.findItem(R.id.filter_resolution)
      res.isChecked = true
    }
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.filter_popular -> {
        item.isChecked = true
        fragMain!!.hiRes = false
        fragMain!!.loadInitialItems(fragMain!!.currentQuery)
      }
      R.id.filter_resolution -> {
        item.isChecked = true
        fragMain!!.hiRes = true
        fragMain!!.loadInitialItems(fragMain!!.currentQuery)
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onGridItemClicked(tileObject: TileObject) {
    if (networkUtil.isConnected) {
      val intent = Intent(this, ActDetails::class.java)
      intent.putExtra(Constants.PARAM_TILE_KEY, tileObject.serialize())
      startActivity(intent)
      this.overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out_shadow)
    } else {
      Toasty.show(this, R.string.no_connection, Toasty.LENGTH_MEDIUM)
    }
  }

  override fun adjustToolbar(scrollState: ScrollState, gridView: ObservableRecyclerView) {
    val toolbarHeight = toolbar.height
    val scrollable = gridView
    val scrollY = scrollable.currentScrollY
    if (scrollState === ScrollState.DOWN) {
      showToolbar()
    } else if (scrollState === ScrollState.UP) {
      if (toolbarHeight <= scrollY) {
        hideToolbar()
      } else {
        showToolbar()
      }
    }
  }

  fun toggleFilterVisible(visible: Boolean) {
    Log.i(TAG, "toggleFilterVisible " + visible)
    val actionFilter = toolbar.menu.findItem(R.id.action_filter)
    val actionFilterPopular = toolbar.menu.findItem(R.id.filter_popular)
    val actionFilterResolution = toolbar.menu.findItem(R.id.filter_resolution)
    if (actionFilter != null)
      if (visible) {
        actionFilter.isVisible = true
        actionFilterPopular.isVisible = true
        actionFilterResolution.isVisible = true
      } else {
        actionFilter.isVisible = false
        actionFilterPopular.isVisible = false
        actionFilterResolution.isVisible = false
      }
  }

  internal var toolbarIsShown = true
  fun showToolbar() {
    if (!toolbarIsShown)
      expand(toolbar)
  }

  fun hideToolbar() {
    if (toolbarIsShown)
      collapse(toolbar)
  }

  fun expand(v: View) {
    YoYo.with(Techniques.FadeInDown).duration(200).playOn(toolbar)
    toolbarIsShown = true
    v.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
    val targetHeight = v.measuredHeight
    v.layoutParams.height = 0
    v.visibility = View.VISIBLE
    val a = object : Animation() {
      override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        v.layoutParams.height = if (interpolatedTime == 1f)
          RelativeLayout.LayoutParams.WRAP_CONTENT
        else
          (targetHeight * interpolatedTime).toInt()
        v.requestLayout()
      }

      override fun willChangeBounds(): Boolean {
        return true
      }
    }
    a.duration = 200
    v.startAnimation(a)
  }

  fun collapse(v: View) {
    YoYo.with(Techniques.FadeOutUp).duration(100).playOn(toolbar)
    toolbarIsShown = false
    val initialHeight = v.measuredHeight
    val a = object : Animation() {
      override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        if (interpolatedTime == 1f) {
          v.visibility = View.GONE
        } else {
          v.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
          v.requestLayout()
        }
      }

      override fun willChangeBounds(): Boolean {
        return true
      }
    }
    a.duration = 200
    v.startAnimation(a)
  }

  override fun selectSection(@NotNull section: SectionChildObject) {
    mTitle = section.sectionTitle
    restoreActionBar()
    showToolbar()
    fragMain!!.loadInitialItems(section.query)
  }

  override fun openFavorites(scroll: Boolean) {
    mTitle = resources.getString(R.string.favorites)
    restoreActionBar()
    showToolbar()
    fragMain!!.openFavorites(scroll)
  }

  override fun injectComponent(component: ActivityComponent) {
    component.inject(this)
  }

  companion object {
    private val CUR_TITLE = "current_title"
  }
}

