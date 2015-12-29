package com.derek_s.hubble_gallery.ui.adapters.nav_drawer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.*
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder
import com.derek_s.hubble_gallery.R
import com.derek_s.hubble_gallery.model.SectionObject
import com.derek_s.hubble_gallery.utils.ui.FontFactory

class GroupViewHolder private constructor(itemView: View) : ParentViewHolder(itemView) {

    @Bind(R.id.tv_group_title)
    lateinit var tvTitle: TextView

    init {
        ButterKnife.bind(this, itemView)
        beautifyViews()
    }

    fun beautifyViews() {
        tvTitle.setTypeface(FontFactory.getMedium(itemView.getContext()))
    }

    fun onBind(item: NavigationAdapterItem<SectionObject>) {
        //tvTitle.setText(item.getObject().getName())
    }

    companion object {
        fun create(context: Context, viewGroup: ViewGroup): GroupViewHolder {
            return GroupViewHolder(LayoutInflater.from(context).inflate(R.layout.item_group,
                    viewGroup, false))
        }
    }
}
