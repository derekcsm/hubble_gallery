package com.derek_s.hubble_gallery.ui.adapters.nav_drawer

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem
import com.derek_s.hubble_gallery.model.SectionObject
import java.util.*

class NavDrawerAdapter(context: Context, parentItemList: ArrayList<ParentListItem>) :
    ExpandableRecyclerAdapter<GroupViewHolder, SectionViewHolder>(parentItemList) {

  private val TAG = "NavDrawerAdapter"
  private val mContext: Context

  init {
    mContext = context
    this.mItemList = parentItemList as List<Any>?
  }

  override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    Log.d(TAG, "onCreateViewHolder type=" + viewType)

    when (viewType) {

      NavigationAdapterItem.GROUP.toInt() -> {
        val pvh = GroupViewHolder.create(mContext, viewGroup)
        return pvh
      }

      NavigationAdapterItem.SECTION.toInt() -> {
        return SectionViewHolder.create(mContext, viewGroup)
      }

      else -> throw IllegalStateException("Incorrect ViewType found")
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val currentItem = mItemList[position] as NavigationAdapterItem<SectionObject>

    if (holder is GroupViewHolder) {
      holder.onBind(currentItem)
    } else if (holder is SectionViewHolder) {
      holder.onBind(currentItem)
    } else {
      super.onBindViewHolder(holder, position)
    }
  }

  override fun onCreateChildViewHolder(childViewGroup: ViewGroup?): SectionViewHolder? {
    return null
  }

  override fun onCreateParentViewHolder(parentViewGroup: ViewGroup?): GroupViewHolder? {
    return null
  }

  override fun onBindChildViewHolder(childViewHolder: SectionViewHolder, position: Int, childListItem: Any?)
      = childViewHolder.onBind(childListItem as NavigationAdapterItem<SectionObject>)

  override fun onBindParentViewHolder(parentViewHolder: GroupViewHolder, position: Int, parentListItem: ParentListItem?)
      = parentViewHolder.onBind(parentListItem as NavigationAdapterItem<SectionObject>)

  override fun getItemViewType(position: Int): Int {
    return (mItemList[position] as NavigationAdapterItem<*>).viewType.toInt()
  }
}