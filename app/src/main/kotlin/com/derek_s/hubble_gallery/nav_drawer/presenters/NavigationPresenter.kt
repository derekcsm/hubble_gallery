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
    /**
     * the universe
     */
    var universeGroup = SectionObject()
    universeGroup.sectionTitle = "The Universe"
    universeGroup.query = "the_universe"

    var universeItems = ArrayList<NavigationAdapterItem<*>>()

    var uni1 = SectionChildObject()
    uni1.sectionTitle = "Distant Galaxies"
    uni1.query = "the_universe/distant_galaxies"
    var uniAd1 = NavigationAdapterItem(uni1, NavigationAdapterItem.SECTION)
    universeItems.add(uniAd1);

    var uni2 = SectionChildObject()
    uni2.sectionTitle = "Intergalactic Gas"
    uni2.query = "the_universe/intergalactic_gas"
    var uniAd2 = NavigationAdapterItem(uni2, NavigationAdapterItem.SECTION)
    universeItems.add(uniAd2);

//    section = new SectionChildObject();
//    section.setSectionTitle("GOODS");
//    section.setQuery("the_universe/goods");
//    subList.add(section);
//
//    section = new SectionChildObject();
//    section.setSectionTitle("Hubble Deep Field");
//    section.setQuery("the_universe/hubble_deep_field");
//    subList.add(section);
//
//    section = new SectionChildObject();
//    section.setSectionTitle("Hubble Ultra Deep Field");
//    section.setQuery("the_universe/hubble_ultra_deep_field");
//    subList.add(section);
//
//    section = new SectionChildObject();
//    section.setSectionTitle("Medium Deep Survey");
//    section.setQuery("the_universe/medium_deep_survey");
//    subList.add(section);

    var universeAdapterItem = NavigationAdapterItem(universeGroup, NavigationAdapterItem.GROUP)
    universeAdapterItem.childObjectList = universeItems
    drawerItems.add(universeAdapterItem)
    

    // TODO footer below groups

    mAdapter = NavDrawerAdapter(context, drawerItems, this)

    //mAdapter.setParentAndIconExpandOnClick(true)
    //mAdapter.setParentClickableViewAnimationDefaultDuration()


    view.recycler.layoutManager = LinearLayoutManager(context)

    view.recycler.adapter = mAdapter
  }

  override fun onSectionClicked(section: SectionObject) {
    // todo
    Log.d(TAG, "onSectionClicked: " + section.sectionTitle)
  }
}
