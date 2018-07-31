package com.derek_s.hubble_gallery.nav_drawer.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.derek_s.hubble_gallery.R
import com.derek_s.hubble_gallery.nav_drawer.model.SectionChildObject
import com.derek_s.hubble_gallery.utils.ui.FontFactory
import com.derek_s.hubble_gallery.utils.ui.expandablerecyclerview.ViewHolder.ChildViewHolder

class SectionViewHolder private constructor(itemView: View) : ChildViewHolder(itemView) {

  val tvTitle: TextView

  init {
    tvTitle = itemView.findViewById(R.id.tv_title)
    beautifyViews()
  }

  fun beautifyViews() {
    tvTitle.typeface = FontFactory.getRegular(itemView.context)
  }

  fun onBind(section: SectionChildObject, listener: NavDrawerAdapter.NavAdapterListener,
             pos: Int, selectedQuery: String) {
    tvTitle.text = section.sectionTitle

    if (section.query.equals(selectedQuery)) {
      tvTitle.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.focused_color));
      tvTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.seleted_item_color));
    } else {
      tvTitle.setBackgroundResource(R.drawable.selector_default);
      tvTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.body_dark_theme));
    }

    tvTitle.setOnClickListener {
      listener.onSectionClicked(section)
      listener.setSelectedQuery(section.query)
    }
  }

  companion object {
    fun create(context: Context, viewGroup: ViewGroup): SectionViewHolder {
      return SectionViewHolder(LayoutInflater.from(context).inflate(R.layout.item_section,
          viewGroup, false))
    }
  }
}
