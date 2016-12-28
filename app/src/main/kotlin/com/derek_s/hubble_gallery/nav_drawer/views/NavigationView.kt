package com.derek_s.hubble_gallery.nav_drawer.views

import android.support.v7.widget.RecyclerView
import com.derek_s.hubble_gallery.nav_drawer.model.SectionChildObject

interface NavigationView {
  val recycler: RecyclerView
    get

  fun selectSection(section: SectionChildObject) {
  }

  fun deselectFavorites() {
  }
}