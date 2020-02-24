package com.chicagoroboto.navigator

import android.app.Application
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import javax.inject.Inject

class AndroidWebNavigator @Inject constructor(
    private val app: Application
) : WebNavigator {

  override fun navigateToUrl(url: String) = app.startActivity(Intent(ACTION_VIEW, Uri.parse(url)))

}
