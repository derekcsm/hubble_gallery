package com.derek_s.hubble_gallery.nav_drawer.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder
import com.derek_s.hubble_gallery.nav_drawer.model.SectionChildObject
import com.derek_s.hubble_gallery.nav_drawer.model.SectionObject
import java.util.*

class NavDrawerAdapter(context: Context, parentItemList: ArrayList<ParentListItem>,
                       listener: NavAdapterListener) :
    ExpandableRecyclerAdapter<GroupViewHolder, SectionViewHolder>(parentItemList) {

  private val TAG = "NavDrawerAdapter"
  private val mContext: Context
  private val listener: NavAdapterListener

  init {
    mContext = context
    this.listener = listener
  }

  override fun onCreateParentViewHolder(parentViewGroup: ViewGroup): GroupViewHolder? {
    return GroupViewHolder.create(mContext, parentViewGroup)
  }

  override fun onCreateChildViewHolder(childViewGroup: ViewGroup): SectionViewHolder? {
    return SectionViewHolder.create(mContext, childViewGroup)
  }

  override fun onBindParentViewHolder(parentViewHolder: GroupViewHolder, position: Int,
                                      parentListItem: ParentListItem) {
    parentViewHolder.onBind(parentListItem as NavigationAdapterItem, listener)
  }

  override fun onBindChildViewHolder(childViewHolder: SectionViewHolder, position: Int,
                                     childListItem: Any) {
    Log.d(TAG, "onBindChild type = " + childListItem)
    childViewHolder.onBind(childListItem as SectionChildObject, listener)
  }

  interface NavAdapterListener {
    fun onSectionClicked(section: SectionObject)
  }

}
