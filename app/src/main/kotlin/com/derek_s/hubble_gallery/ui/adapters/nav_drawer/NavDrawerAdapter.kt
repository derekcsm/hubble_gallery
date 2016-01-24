package com.derek_s.hubble_gallery.ui.adapters.nav_drawer

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem
import com.derek_s.hubble_gallery.model.SectionObject

class NavDrawerAdapter(context: Context, parentItemList: List<SectionAdapterItem<Any?>>) :
    ExpandableRecyclerAdapter<GroupViewHolder, SectionViewHolder>(parentItemList) {

  private val mContext: Context

  init {
    mContext = context
    this.mItemList = parentItemList
  }

  override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    when (viewType) {
      SectionAdapterItem.GROUP.toInt() -> {
        val pvh = GroupViewHolder.create(mContext, viewGroup)
        //pvh.setParentItemClickListener(this) // TODO
        return pvh
      }
      SectionAdapterItem.PROJECT.toInt() -> return SectionViewHolder.create(mContext, viewGroup)
           else -> throw IllegalStateException("Incorrect ViewType found")
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val currentItem = mItemList[position] as SectionAdapterItem<SectionObject>

    if (holder is GroupViewHolder) {
      holder.onBind(currentItem)
    } else if (holder is SectionViewHolder) {
      holder.onBind(currentItem)
    }  else {
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
      = childViewHolder.onBind(childListItem as SectionAdapterItem<SectionObject>)

  override fun onBindParentViewHolder(parentViewHolder: GroupViewHolder, position: Int, parentListItem: ParentListItem?)
      = parentViewHolder.onBind(parentListItem as SectionAdapterItem<SectionObject>)

  override fun getItemViewType(position: Int): Int {
    return (mItemList[position] as SectionAdapterItem<*>).viewType
  }
}