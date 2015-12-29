package com.derek_s.hubble_gallery.ui.adapters.nav_drawer

import com.bignerdranch.expandablerecyclerview.Model.ParentObject

class NavigationAdapterItem<T>(`object`: T, viewType: Int) : ParentObject {

    var `object`: T
    @NavigationAdapterItem.ViewType
    var viewType: Int = 0
    private var mChildren: MutableList<Any>? = null

    override fun setChildObjectList(list: MutableList<Any>?) {
        mChildren = list
    }

    override fun getChildObjectList(): MutableList<Any>? {
        return mChildren
    }

    init {
        this.`object` = `object`
        this.viewType = viewType
    }

    annotation class ViewType

    companion object {
        val HEADER = 0
        val GROUP_SECTION = 1
        val CHILD_SECTION = 2
        val FOOTER = 3
    }
}