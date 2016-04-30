package com.derek_s.hubble_gallery.nav_drawer.presenters

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem
import com.derek_s.hubble_gallery.nav_drawer.adapters.NavDrawerAdapter
import com.derek_s.hubble_gallery.nav_drawer.adapters.NavigationAdapterItem
import com.derek_s.hubble_gallery.nav_drawer.model.SectionChildObject
import com.derek_s.hubble_gallery.nav_drawer.views.NavigationView
import java.util.*

class NavigationPresenter constructor(view: NavigationView, context: Context) :
    NavDrawerAdapter.NavAdapterListener {

  final val TAG = "NavigationPresenter"
  var view: NavigationView
  var context: Context
  var mAdapter: NavDrawerAdapter? = null

  val KEY_SELECTED_QUERY = "selected_query"

  init {
    this.view = view
    this.context = context
  }

  fun restoreState(savedState: Bundle?) {
    if (savedState == null)
      return

    if (savedState.containsKey(KEY_SELECTED_QUERY))
      mAdapter!!.setSelectedQuery(savedState.getString(KEY_SELECTED_QUERY))
  }

  fun getSelectedQuery() : String? {
    return mAdapter!!.getSelectedQuery()
  }

  fun populateAdapter() {
    var drawerItems = ArrayList<ParentListItem>()

    drawerItems.add(NavigationAdapterItem(SectionChildObject("Entire Collection", "entire"),
        NavigationAdapterItem.STANDALONE_SECTION, null))

    drawerItems.add(NavigationAdapterItem(SectionChildObject("Hubble Heritage", "heritage"),
        NavigationAdapterItem.STANDALONE_SECTION, null))

    /**
     * the universe
     */
    var universeItems = ArrayList<SectionChildObject>()
    universeItems.add(createChildObject("Distant Galaxies", "the_universe/distant_galaxies"))
    universeItems.add(createChildObject("GOODS", "the_universe/goods"))
    universeItems.add(createChildObject("Hubble Deep Field", "the_universe/hubble_deep_field"))
    universeItems.add(createChildObject("Hubble Ultra Deep Field", "the_universe/hubble_ultra_deep_field"))
    universeItems.add(createChildObject("Intergalactic Gas", "the_universe/intergalactic_gas"))
    universeItems.add(createChildObject("Medium Deep Survey", "the_universe/medium_deep_survey"))

    drawerItems.add(NavigationAdapterItem(SectionChildObject("The Universe", "the_universe"),
        NavigationAdapterItem.GROUP, universeItems))

    /**
     * exotic
     */
    var exoticItems = ArrayList<SectionChildObject>()
    exoticItems.add(createChildObject("Black Hole", "exotic/black_hole"))
    exoticItems.add(createChildObject("Dark Matter", "exotic/dark_matter"))
    exoticItems.add(createChildObject("Gamma Ray Burst", "exotic/gamma_ray_burst"))
    exoticItems.add(createChildObject("Gravitational Lens", "exotic/gravitational_lens"))

    drawerItems.add(NavigationAdapterItem(SectionChildObject("Exotic", "exotic"),
        NavigationAdapterItem.GROUP, exoticItems))

    /**
     * galaxies
     */
    var galaxyItems = ArrayList<SectionChildObject>()
    galaxyItems.add(createChildObject("Cluster", "galaxy/cluster"))
    galaxyItems.add(createChildObject("Dwarf", "galaxy/dwarf"))
    galaxyItems.add(createChildObject("Elliptical", "galaxy/elliptical"))
    galaxyItems.add(createChildObject("Interacting", "galaxy/interacting"))
    galaxyItems.add(createChildObject("Irregular", "galaxy/irregular"))
    galaxyItems.add(createChildObject("Magellanic Cloud", "galaxy/magellanic_cloud"))
    galaxyItems.add(createChildObject("Quasar/Active Nucleus", "galaxy/quasar_active_nucleus"))

    drawerItems.add(NavigationAdapterItem(SectionChildObject("Galaxies", "galaxy"),
        NavigationAdapterItem.GROUP, galaxyItems))

    /**
     * nebulae
     */
    var nebulaItems = ArrayList<SectionChildObject>()
    nebulaItems.add(createChildObject("Dark", "nebula/dark"))
    nebulaItems.add(createChildObject("Emission", "nebula/emission"))
    nebulaItems.add(createChildObject("Planetary", "nebula/planetary"))
    nebulaItems.add(createChildObject("Reflection", "nebula/reflection"))
    nebulaItems.add(createChildObject("Supernova Remnant", "nebula/supernova_remnant"))

    drawerItems.add(NavigationAdapterItem(SectionChildObject("Nebulae", "nebula"),
        NavigationAdapterItem.GROUP, nebulaItems))

    /**
     * solar system
     */
    var solarSystemItems = ArrayList<SectionChildObject>()
    solarSystemItems.add(createChildObject("Asteroid", "solar_system/comet"))
    solarSystemItems.add(createChildObject("Comet", "solar_system/emission"))
    solarSystemItems.add(createChildObject("Jupiter", "solar_system/jupiter"))
    solarSystemItems.add(createChildObject("Kuiper Belt Object", "solar_system/kuiper_belt_object"))
    solarSystemItems.add(createChildObject("Mars", "solar_system/mars"))
    solarSystemItems.add(createChildObject("Neptune", "solar_system/neptune"))
    solarSystemItems.add(createChildObject("Planetary Moon", "solar_system/planetary_moon"))
    solarSystemItems.add(createChildObject("Planetary Ring", "solar_system/planetary_ring"))
    solarSystemItems.add(createChildObject("Pluto", "solar_system/pluto"))
    solarSystemItems.add(createChildObject("Saturn", "solar_system/saturn"))
    solarSystemItems.add(createChildObject("Uranus", "solar_system/uranus"))
    solarSystemItems.add(createChildObject("Venus", "solar_system/venus"))
    solarSystemItems.add(createChildObject("Weather/Atmosphere", "solar_system/weather_atmosphere"))

    drawerItems.add(NavigationAdapterItem(SectionChildObject("Solar System", "solar_system"),
        NavigationAdapterItem.GROUP, solarSystemItems))

    /**
     * stars
     */
    var starItems = ArrayList<SectionChildObject>()
    starItems.add(createChildObject("Brown Dwarf", "star/brown_dwarf"))
    starItems.add(createChildObject("Massive Star", "star/massive_star"))
    starItems.add(createChildObject("Nova", "star/nova"))
    starItems.add(createChildObject("Protostellar Jet", "star/protostellar_jet"))
    starItems.add(createChildObject("Protoplanetary Disk", "star/protoplanetary_disk"))
    starItems.add(createChildObject("Pulsar", "star/pulsar"))
    starItems.add(createChildObject("Star Cluster", "star/star_cluster"))
    starItems.add(createChildObject("Star Field", "star/star_field"))
    starItems.add(createChildObject("Supernova", "star/supernova"))
    starItems.add(createChildObject("Variable Star", "star/variable_star"))
    starItems.add(createChildObject("White Dwarf", "star/white_dwarf"))

    drawerItems.add(NavigationAdapterItem(SectionChildObject("Stars", "star"),
        NavigationAdapterItem.GROUP, nebulaItems))


    mAdapter = NavDrawerAdapter(context, drawerItems, this)

    view.recycler.layoutManager = LinearLayoutManager(context)
    view.recycler.adapter = mAdapter
  }

  private fun createChildObject(title: String, query: String): SectionChildObject {
    return SectionChildObject(title, query)
  }

  override fun onSectionClicked(section: SectionChildObject) {
    view.selectSection(section)
  }

  override fun setSelectedQuery(query: String) {
    mAdapter!!.setSelectedQuery(query)
  }
}
