package com.derek_s.hubble_gallery.nav_drawer.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import kotterknife.bindView
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder
import com.derek_s.hubble_gallery.R
import com.derek_s.hubble_gallery.nav_drawer.model.SectionChildObject
import com.derek_s.hubble_gallery.utils.ui.FontFactory

class GroupViewHolder private constructor(itemView: View) : ParentViewHolder(itemView) {

  val tvTitle: TextView by bindView(R.id.tv_title)
  val ivExpand: ImageView by bindView(R.id.iv_expand)

  private val INITIAL_POSITION = 0.0f
  private val ROTATED_POSITION = 180f

  init {
    beautifyViews()
  }

  fun beautifyViews() {
    tvTitle.typeface = FontFactory.getMedium(itemView.context)
  }

  fun onBind(item: NavigationAdapterItem, listener: NavDrawerAdapter.NavAdapterListener,
             pos: Int, selectedQuery: String) {
    var section: SectionChildObject = item.`object` as SectionChildObject

    if (section.query.equals(selectedQuery)) {
      tvTitle.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.focused_color));
      tvTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.seleted_item_color));
    } else {
      tvTitle.setBackgroundResource(R.drawable.selector_default);
      tvTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.body_dark_theme));
    }

    tvTitle.text = section.sectionTitle
    tvTitle.setOnClickListener {
      listener.onSectionClicked(section)
      listener.setSelectedQuery(section.query)
      tvTitle.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.focused_color));
      tvTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.seleted_item_color));
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

  @SuppressLint("NewApi")
  override fun setExpanded(expanded: Boolean) {
    super.setExpanded(expanded)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      if (expanded) {
        ivExpand.rotation = ROTATED_POSITION
      } else {
        ivExpand.rotation = INITIAL_POSITION
      }
    }
  }

  override fun onExpansionToggled(expanded: Boolean) {
    super.onExpansionToggled(expanded)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      val rotateAnimation: RotateAnimation
      if (expanded) {
        // rotate clockwise
        rotateAnimation = RotateAnimation(ROTATED_POSITION,
            INITIAL_POSITION,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f)
      } else {
        // rotate counterclockwise
        rotateAnimation = RotateAnimation(-1 * ROTATED_POSITION,
            INITIAL_POSITION,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f)
      }

      rotateAnimation.duration = 200
      rotateAnimation.fillAfter = true
      ivExpand.startAnimation(rotateAnimation)
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
