package com.derek_s.hubble_gallery.ui.adapters.nav_drawer

import android.support.annotation.IntDef
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem

class SectionAdapterItem<T>(`object`: T, viewType: Int) : ParentListItem {

  @SectionAdapterItem.ViewType
  var viewType: Int = 0
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
  @IntDef(FAVORITE_HEADER, FAVORITE, FAVORITE_DASHBOARD, FAVORITE_ALL_TASKS, PROJECT_HEADER,
      GROUP, PROJECT, PROJECT_STANDALONE)
  annotation class ViewType

  companion object {
    const val FAVORITE_HEADER: Long = 0
    const val FAVORITE: Long = 1
    const val FAVORITE_DASHBOARD: Long = 2
    const val FAVORITE_ALL_TASKS: Long = 3
    const val PROJECT_HEADER: Long = 4
    const val GROUP: Long = 5
    const val PROJECT: Long = 6
    const val PROJECT_STANDALONE: Long = 7
  }
}
