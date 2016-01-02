package com.derek_s.hubble_gallery.ui.adapters.nav_drawer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.Bind
import butterknife.ButterKnife
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder
import com.derek_s.hubble_gallery.R
import com.derek_s.hubble_gallery.model.SectionObject
import com.derek_s.hubble_gallery.utils.ui.FontFactory


class FooterViewHolder private constructor(itemView: View) : ParentViewHolder(itemView) {

    @Bind(R.id.tv_favorites)
    lateinit var tvFavorites: TextView
    @Bind(R.id.tv_rate)
    lateinit var tvRate: TextView
    @Bind(R.id.tv_about)
    lateinit var tvAbout: TextView

    init {
        ButterKnife.bind(this, itemView)
        beautifyViews()
    }

    fun beautifyViews() {
        tvFavorites.setTypeface(FontFactory.getMedium(itemView.getContext()))
        tvRate.setTypeface(FontFactory.getMedium(itemView.getContext()))
        tvAbout.setTypeface(FontFactory.getMedium(itemView.getContext()))
    }

    fun onBind(item: NavigationAdapterItem<SectionObject>) {

        tvFavorites.setOnClickListener {
            // TODO
//            updateSelectedItem(-2, -2, "Favorites")
//            mAdapter.notifyDataSetChanged()
//            ActMain.instance!!.mTitle = "Favorites"
//            tvFavorites.setBackgroundColor(getResources().getColor(R.color.focused_color))
//            tvFavorites.setTextColor(getResources().getColor(R.color.seleted_item_color))
//            ActMain.instance!!.restoreActionBar()
//            if (ActMain.instance!!.toolbar != null)
//                ActMain.instance!!.showToolbar()
//            closeDrawer()
//            ActMain.instance!!.fragMain.openFavorites(true)
        }

        tvRate.setOnClickListener {
            // TODO
//            val appPackageName = itemView.getContext().getPackageName()
//            try {
//                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)))
//            } catch (anfe: android.content.ActivityNotFoundException) {
//                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)))
//            }
        }

        tvAbout.setOnClickListener {
            // TODO
            //val dialogAbout = DialogAbout(getActivity())
            //dialogAbout.displayDialog()
        }
    }

    companion object {
        fun create(context: Context, viewGroup: ViewGroup): FooterViewHolder {
            return FooterViewHolder(LayoutInflater.from(context).inflate(R.layout.item_footer_nav_drawer,
                    viewGroup, false))
        }
    }
}
