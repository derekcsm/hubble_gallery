package com.derek_s.hubble_gallery.nav_drawer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.Bind
import butterknife.ButterKnife
import butterknife.bindView
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder
import com.derek_s.hubble_gallery.R
import com.derek_s.hubble_gallery.nav_drawer.model.SectionObject
import com.derek_s.hubble_gallery.utils.ui.FontFactory

class StandaloneSectionViewHolder private constructor(itemView: View) : ParentViewHolder(itemView) {

  val tvTitle: TextView by bindView(R.id.tv_title)

  init {
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
    fun create(context: Context, viewGroup: ViewGroup): StandaloneSectionViewHolder {
      return StandaloneSectionViewHolder(LayoutInflater.from(context).inflate(R.layout.item_section,
          viewGroup, false))
    }
  }
}
