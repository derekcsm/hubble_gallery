package com.derek_s.hubble_gallery.nav_drawer.presenters

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem
import com.derek_s.hubble_gallery.nav_drawer.adapters.NavDrawerAdapter
import com.derek_s.hubble_gallery.nav_drawer.adapters.NavigationAdapterItem
import com.derek_s.hubble_gallery.nav_drawer.model.SectionObject
import com.derek_s.hubble_gallery.nav_drawer.views.NavigationView
import java.util.*

class NavigationPresenter constructor(view: NavigationView, context: Context) :
    NavDrawerAdapter.NavAdapterListener {

  final val TAG = "NavigationPresenter"
  var view: NavigationView
  var context: Context
  var mAdapter: NavDrawerAdapter? = null

  init {
    this.view = view
    this.context = context
  }

  fun populateAdapter() {
    // TODO
    var drawerItems = ArrayList<ParentListItem>()

    // TODO header here

    // Standalone
    var entireSection = SectionObject()
    entireSection.sectionTitle = "Entire Collection"
    entireSection.query = "entire"
    drawerItems.add(NavigationAdapterItem(entireSection, NavigationAdapterItem.STANDALONE_SECTION))

    var heritageSection = SectionObject()
    heritageSection.sectionTitle = "Hubble Heritage"
    heritageSection.query = "heritage"
    drawerItems.add(NavigationAdapterItem(heritageSection, NavigationAdapterItem.STANDALONE_SECTION))

    //Groups TODO
    var universeGroup = SectionObject()
    universeGroup.sectionTitle = "The Universe"
    universeGroup.query = "the_universe"
    drawerItems.add(NavigationAdapterItem(universeGroup, NavigationAdapterItem.GROUP))



    // TODO footer below groups

    mAdapter = NavDrawerAdapter(context, drawerItems, this)

    view.recycler.layoutManager = LinearLayoutManager(context)
    view.recycler.adapter = mAdapter
  }

  override fun onSectionClicked(section: SectionObject) {
    // todo
    Log.d(TAG, "onSectionClicked: " + section.sectionTitle)
  }
}
