package com.derek_s.hubble_gallery.nav_drawer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder
import com.derek_s.hubble_gallery.R
import com.derek_s.hubble_gallery.nav_drawer.model.SectionObject
import com.derek_s.hubble_gallery.utils.ui.FontFactory

class GroupViewHolder private constructor(itemView: View) : ParentViewHolder(itemView) {

  val tvTitle: TextView by bindView(R.id.tv_title)
  val ivExpand: ImageView by bindView(R.id.iv_expand)

  init {
    beautifyViews()
  }

  fun beautifyViews() {
    tvTitle.typeface = FontFactory.getMedium(itemView.context)
  }

  fun onBind(item: NavigationAdapterItem, listener: NavDrawerAdapter.NavAdapterListener) {
    var section: SectionObject = item.`object` as SectionObject

    tvTitle.text = section.sectionTitle

    tvTitle.setOnClickListener {
      listener.onSectionClicked(section)
    }

    if (item.childObjectList == null) {
      ivExpand.visibility = View.GONE
    } else {
      ivExpand.visibility = View.VISIBLE
      ivExpand.setOnClickListener {
        if (isExpanded) {
          collapseView()
        } else {
          expandView()
        }
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
