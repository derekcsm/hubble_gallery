package com.derek_s.hubble_gallery.nav_drawer.adapters

import android.support.annotation.IntDef
import com.derek_s.hubble_gallery.utils.ui.expandablerecyclerview.Model.ParentListItem

class NavigationAdapterItem(`object`: Any?, viewType: ViewType, childObjectList: List<Any>?) :
    ParentListItem {

  var viewType: ViewType = ViewType.HEADER
  var `object`: Any?
  var childObjectList: List<Any>?
  var isSelected: Boolean = false

  init {
    this.`object` = `object`
    this.viewType = viewType
    this.childObjectList = childObjectList
  }

  override fun getChildItemList(): List<Any>? {
    return childObjectList
  }

  override fun isInitiallyExpanded(): Boolean {
    return isSelected
  }

  enum class ViewType {
    HEADER, STANDALONE_SECTION, SECTION, GROUP
  }
}
