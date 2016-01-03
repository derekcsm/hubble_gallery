package com.derek_s.hubble_gallery.ui.adapters.nav_drawer

import android.content.Context
import android.view.ViewGroup
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem
import java.util.*


class NavDrawerAdapter(context: Context, parentItemList: List<ParentListItem>)  :
        ExpandableRecyclerAdapter<GroupViewHolder, SectionViewHolder>(parentItemList) {

    init {
        //super(parentItemList)
    }

    override fun onCreateChildViewHolder(childViewGroup: ViewGroup?): SectionViewHolder? {
        throw UnsupportedOperationException()
    }

    override fun onCreateParentViewHolder(parentViewGroup: ViewGroup?): GroupViewHolder? {
        throw UnsupportedOperationException()
    }

    override fun onBindChildViewHolder(childViewHolder: SectionViewHolder?, position: Int, childListItem: Any?) {
        throw UnsupportedOperationException()
    }

    override fun onBindParentViewHolder(parentViewHolder: GroupViewHolder?, position: Int, parentListItem: ParentListItem?) {
        throw UnsupportedOperationException()
    }
}