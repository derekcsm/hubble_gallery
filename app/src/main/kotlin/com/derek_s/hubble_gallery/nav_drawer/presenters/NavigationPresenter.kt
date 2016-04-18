package com.derek_s.hubble_gallery.nav_drawer.presenters

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem
import com.derek_s.hubble_gallery.nav_drawer.adapters.NavDrawerAdapter
import com.derek_s.hubble_gallery.nav_drawer.adapters.NavigationAdapterItem
import com.derek_s.hubble_gallery.nav_drawer.model.SectionChildObject
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
    var drawerItems = ArrayList<ParentListItem>()

    var entireSection = SectionObject()
    entireSection.sectionTitle = "Entire Collection"
    entireSection.query = "entire"
    drawerItems.add(NavigationAdapterItem(entireSection, NavigationAdapterItem.STANDALONE_SECTION, null))

    var heritageSection = SectionObject()
    heritageSection.sectionTitle = "Hubble Heritage"
    heritageSection.query = "heritage"
    drawerItems.add(NavigationAdapterItem(heritageSection, NavigationAdapterItem.STANDALONE_SECTION, null))

    /**
     * the universe
     */
    var universeGroup = SectionObject()
    universeGroup.sectionTitle = "The Universe"
    universeGroup.query = "the_universe"

    var universeItems = ArrayList<SectionChildObject>()

    var uni1 = SectionChildObject()
    uni1.sectionTitle = "Distant Galaxies"
    uni1.query = "the_universe/distant_galaxies"
    universeItems.add(uni1)

    var uni2 = SectionChildObject()
    uni2.sectionTitle = "Intergalactic Gas"
    uni2.query = "the_universe/intergalactic_gas"
    universeItems.add(uni2)

    var uni3 = SectionChildObject()
    uni3.sectionTitle = "GOODS"
    uni3.query = "the_universe/goods"
    universeItems.add(uni3)

    var uni4 = SectionChildObject()
    uni4.sectionTitle = "Hubble Deep Field"
    uni4.query = "the_universe/hubble_deep_field"
    universeItems.add(uni4)

    var uni5 = SectionChildObject()
    uni5.sectionTitle = "Hubble Ultra Deep Field"
    uni5.query = "the_universe/hubble_ultra_deep_field"
    universeItems.add(uni5)

    var uni6 = SectionChildObject()
    uni6.sectionTitle = "Medium Deep Survey"
    uni6.query = "the_universe/medium_deep_survey"
    universeItems.add(uni6)

    drawerItems.add(NavigationAdapterItem(universeGroup, NavigationAdapterItem.GROUP, universeItems))

    mAdapter = NavDrawerAdapter(context, drawerItems, this)

    view.recycler.layoutManager = LinearLayoutManager(context)
    view.recycler.adapter = mAdapter
  }

  override fun onSectionClicked(section: SectionObject) {
    // todo
    Log.d(TAG, "onSectionClicked: " + section.sectionTitle)
  }
}
