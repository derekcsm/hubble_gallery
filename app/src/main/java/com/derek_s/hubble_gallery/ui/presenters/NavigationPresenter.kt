package com.derek_s.hubble_gallery.ui.presenters

import com.derek_s.hubble_gallery.model.SectionObject
import com.derek_s.hubble_gallery.ui.adapters.nav_drawer.NavigationAdapterItem
import com.derek_s.hubble_gallery.ui.views.NavigationView
import java.util.*


class NavigationPresenter constructor(view: NavigationView) {

    var view: NavigationView

    init {
        this.view = view
    }

    fun populateAdapter() {
        // TODO
        var drawerItems = ArrayList<Any>()

        drawerItems.add(NavigationAdapterItem(null, NavigationAdapterItem.HEADER))

        var section = SectionObject()

        // Standalone
        section.sectionTitle = "Entire Collection"
        section.query = "entire"
        drawerItems.add(NavigationAdapterItem(section, NavigationAdapterItem.STANDALONE_SECTION))

        section.sectionTitle = "Hubble Heritage"
        section.query = "heritage"
        drawerItems.add(NavigationAdapterItem(section, NavigationAdapterItem.STANDALONE_SECTION))


        // Groups TODO
//        var adapterItem = NavigationAdapterItem(section, NavigationAdapterItem.GROUP_SECTION);
//        section.sectionTitle = "The Universe"
//        section.query = "the_universe"
//        drawerItems.add(NavigationAdapterItem(adapterItem))

    }
}
