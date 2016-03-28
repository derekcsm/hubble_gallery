package com.derek_s.hubble_gallery.nav_drawer.adapters

import android.support.annotation.IntDef
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem

class NavigationAdapterItem<T>(`object`: T, viewType: Long) : ParentListItem {

  @ViewType
  var viewType: Long = 0
  var `object`: T? = null
  var childObjectList: List<Any>? = null
  var isSelected: Boolean = false

  init {
    this.`object` = `object`
    this.viewType = viewType
  }

  override fun getChildItemList(): List<Any>? {
    return childObjectList
  }

  override fun isInitiallyExpanded(): Boolean {
    return isSelected
  }

  @Retention(AnnotationRetention.SOURCE)
  @IntDef(HEADER, STANDALONE_SECTION, SECTION, GROUP)
  annotation class ViewType

  companion object {
    const val HEADER: Long = 0
    const val STANDALONE_SECTION: Long = 1
    const val SECTION: Long = 2
    const val GROUP: Long = 3
  }
}
