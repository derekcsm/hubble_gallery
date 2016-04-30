package com.derek_s.hubble_gallery.nav_drawer.adapters

import android.content.Context
import android.view.ViewGroup
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem
import com.derek_s.hubble_gallery.nav_drawer.model.SectionChildObject
import java.util.*

class NavDrawerAdapter(context: Context, parentItemList: ArrayList<ParentListItem>,
                       listener: NavAdapterListener) :
    ExpandableRecyclerAdapter<GroupViewHolder, SectionViewHolder>(parentItemList) {

  private val TAG = "NavDrawerAdapter"
  private val mContext: Context
  private val listener: NavAdapterListener

  private var selectedQuery = "entire";

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
    parentViewHolder.onBind(parentListItem as NavigationAdapterItem, listener, position, selectedQuery)
  }

  override fun onBindChildViewHolder(childViewHolder: SectionViewHolder, position: Int,
                                     childListItem: Any) {
    childViewHolder.onBind(childListItem as SectionChildObject, listener, position, selectedQuery)
  }

  fun setSelectedQuery(query: String) {
    selectedQuery = query
    notifyDataSetChanged()
  }

  fun getSelectedQuery() : String {
    return selectedQuery
  }

  interface NavAdapterListener {
    fun onSectionClicked(section: SectionChildObject)

    fun setSelectedQuery(selectedQuery: String)
  }

}
