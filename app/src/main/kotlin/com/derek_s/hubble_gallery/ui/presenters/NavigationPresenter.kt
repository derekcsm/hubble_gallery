package com.derek_s.hubble_gallery.ui.presenters

import android.content.Context
import com.derek_s.hubble_gallery.ui.views.NavigationView

class NavigationPresenter constructor(view: NavigationView, context: Context) {

    var view: NavigationView
    var context: Context

    init {
        this.view = view
        this.context = context
    }

    fun populateAdapter() {
        // TODO
//        var drawerItems = ArrayList<Any>()
//
//        drawerItems.add(NavigationAdapterItem(null, NavigationAdapterItem.HEADER))
//
//        var section = SectionObject()
//
//        // Standalone
//        section.sectionTitle = "Entire Collection"
//        section.query = "entire"
//        drawerItems.add(NavigationAdapterItem(section, NavigationAdapterItem.STANDALONE_SECTION))
//
//        section.sectionTitle = "Hubble Heritage"
//        section.query = "heritage"
//        drawerItems.add(NavigationAdapterItem(section, NavigationAdapterItem.STANDALONE_SECTION))


        // Groups TODO
//        var adapterItem = NavigationAdapterItem(section, NavigationAdapterItem.GROUP_SECTION);
//        section.sectionTitle = "The Universe"
//        section.query = "the_universe"
//        drawerItems.add(NavigationAdapterItem(adapterItem))

       // var parentItems = ArrayList<ParentListItem>()


       // var navAdapter = NavDrawerAdapter(context, parentItems)
    }
}
