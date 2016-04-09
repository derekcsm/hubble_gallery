package com.derek_s.hubble_gallery.nav_drawer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.Bind
import butterknife.ButterKnife
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder
import com.derek_s.hubble_gallery.R
import com.derek_s.hubble_gallery.nav_drawer.model.SectionObject
import com.derek_s.hubble_gallery.utils.ui.FontFactory

class SectionViewHolder private constructor(itemView: View) : ChildViewHolder(itemView) {

  @Bind(R.id.tv_title)
  lateinit var tvTitle: TextView

  init {
    ButterKnife.bind(this, itemView)
    beautifyViews()
  }

  fun beautifyViews() {
    tvTitle.typeface = FontFactory.getMedium(itemView.context)
  }

  fun onBind(item: NavigationAdapterItem<*>, listener: NavDrawerAdapter.NavAdapterListener) {
    var section: SectionObject = item.`object` as SectionObject

    tvTitle.text = section.sectionTitle

    tvTitle.setOnClickListener {
      listener.onSectionClicked(section)
    }
  }

  companion object {
    fun create(context: Context, viewGroup: ViewGroup): SectionViewHolder {
      return SectionViewHolder(LayoutInflater.from(context).inflate(R.layout.item_section,
          viewGroup, false))
    }
  }
}
