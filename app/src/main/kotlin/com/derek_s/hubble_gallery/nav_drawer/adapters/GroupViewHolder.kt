package com.derek_s.hubble_gallery.nav_drawer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.Bind
import butterknife.ButterKnife
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder
import com.derek_s.hubble_gallery.R
import com.derek_s.hubble_gallery.nav_drawer.model.SectionObject
import com.derek_s.hubble_gallery.utils.ui.FontFactory

class GroupViewHolder private constructor(itemView: View) : ParentViewHolder(itemView) {

  @Bind(R.id.tv_group_title)
  lateinit var tvTitle: TextView
  @Bind(R.id.iv_expand)
  lateinit var ivExpand: ImageView

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

    ivExpand.setOnClickListener {
      if (isExpanded()) {
        collapseView()
      } else {
        expandView()
      }
    }
  }

  override fun shouldItemViewClickToggleExpansion(): Boolean {
    return false
  }

  companion object {
    fun create(context: Context, viewGroup: ViewGroup): GroupViewHolder {
      return GroupViewHolder(LayoutInflater.from(context).inflate(R.layout.item_group,
          viewGroup, false))
    }
  }


}
