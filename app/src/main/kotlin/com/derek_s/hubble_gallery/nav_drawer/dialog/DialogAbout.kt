package com.derek_s.hubble_gallery.nav_drawer.dialog

import android.content.Context
import android.content.pm.PackageInfo
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.Theme
import com.derek_s.hubble_gallery.R
import com.derek_s.hubble_gallery.utils.ui.FontFactory

class DialogAbout(context: Context) {

  private val context: Context
  private var dialog: MaterialDialog? = null

  private var tvTitle: TextView? = null
  private var tvVersion: TextView? = null
  private var tvBody: TextView? = null

  init {
    this.context = context
  }

  fun getDialog() : MaterialDialog {
    if (dialog == null) {
      return MaterialDialog.Builder(context)
          .customView(R.layout.dialog_about, false)
          .backgroundColorRes(R.color.background)
          .theme(Theme.DARK)
          .forceStacking(true)
          .positiveText(context.resources.getString(R.string.show_intro))
          .build()
    } else {
      return dialog!!
    }
  }

  fun show() {
    dialog = getDialog();
    dialog!!.show()

    findViews(dialog!!.customView)
    beautifyViews()

    var pInfo: PackageInfo = context.packageManager.getPackageInfo(context.getPackageName(), 0)
    var version = pInfo.versionName;
    tvVersion!!.text = "V " + version

    tvBody!!.movementMethod = LinkMovementMethod.getInstance()
    tvBody!!.text = Html.fromHtml(context.resources.getString(R.string.about_body))
  }

  private fun findViews(dView: View?) {
    if (dView == null)
      return

    tvTitle = dView.findViewById(R.id.tv_title) as TextView
    tvVersion = dView.findViewById(R.id.tv_version) as TextView
    tvBody = dView.findViewById(R.id.tv_body) as TextView
  }

  private fun beautifyViews() {
    tvTitle!!.typeface = FontFactory.getCondensedRegular(context)
    tvVersion!!.typeface = FontFactory.getMedium(context)
    tvBody!!.typeface = FontFactory.getRegular(context)
  }
}