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

class DialogAbout(context: Context, listener: DialogAboutListener) {

  private val context: Context
  private val listener: DialogAboutListener
  private var dialog: MaterialDialog? = null

  private var tvTitle: TextView? = null
  private var tvVersion: TextView? = null
  private var tvBody: TextView? = null

  init {
    this.context = context
    this.listener = listener
  }

  fun getDialog(): MaterialDialog {
    if (dialog == null) {
      return MaterialDialog.Builder(context)
          .customView(R.layout.dialog_about, false)
          .backgroundColorRes(R.color.background)
          .theme(Theme.DARK)
          .forceStacking(true)
          .positiveText(context.resources.getString(R.string.show_intro))
          .onPositive { materialDialog, dialogAction -> listener.onShowIntroClicked() }
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
    tvBody!!.text = removeHtmlBottomPadding(Html.fromHtml(context.resources.getString(R.string.about_body)))
  }

  private fun removeHtmlBottomPadding(mText: CharSequence?): CharSequence? {
    var text: CharSequence = mText ?: return null
    if (text.length == 0)
      return text

    while (text[text.length - 1] == '\n') {
      text = text.subSequence(0, text.length - 1)
    }
    return text
  }

  private fun findViews(dView: View?) {
    if (dView == null)
      return

    tvTitle = dView.findViewById(R.id.tv_title)
    tvVersion = dView.findViewById(R.id.tv_version)
    tvBody = dView.findViewById(R.id.tv_body)
  }

  private fun beautifyViews() {
    tvTitle!!.typeface = FontFactory.getCondensedRegular(context)
    tvVersion!!.typeface = FontFactory.getMedium(context)
    tvBody!!.typeface = FontFactory.getRegular(context)
  }
}