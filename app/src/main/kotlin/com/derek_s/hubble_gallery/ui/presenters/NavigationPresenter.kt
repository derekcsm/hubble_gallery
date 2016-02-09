package com.derek_s.hubble_gallery.ui.presenters

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem
import com.derek_s.hubble_gallery.model.SectionObject
import com.derek_s.hubble_gallery.ui.adapters.nav_drawer.NavDrawerAdapter
import com.derek_s.hubble_gallery.ui.adapters.nav_drawer.NavigationAdapterItem
import com.derek_s.hubble_gallery.ui.views.NavigationView
import java.util.*

class NavigationPresenter constructor(view: NavigationView, context: Context) {

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

    var section = SectionObject()
    // Standalone
//    section.sectionTitle = "Entire Collection"
//    section.query = "entire"
//    drawerItems.add(NavigationAdapterItem(section, NavigationAdapterItem.STANDALONE_SECTION))
//
//    section.sectionTitle = "Hubble Heritage"
//    section.query = "heritage"
//    drawerItems.add(NavigationAdapterItem(section, NavigationAdapterItem.STANDALONE_SECTION))

    //Groups TODO
    section.sectionTitle = "The Universe"
    section.query = "the_universe"
    drawerItems.add(NavigationAdapterItem(section, NavigationAdapterItem.GROUP))

    // TODO footer below groups

    mAdapter = NavDrawerAdapter(context, drawerItems)

    view.recycler.layoutManager = LinearLayoutManager(context)
    view.recycler.adapter = mAdapter
  }
}
