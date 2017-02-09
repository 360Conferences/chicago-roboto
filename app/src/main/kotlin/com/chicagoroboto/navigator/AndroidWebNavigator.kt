package com.chicagoroboto.navigator

import android.content.Context
import android.content.Intent
import android.net.Uri

class AndroidWebNavigator(private val context: Context) : WebNavigator {

    override fun navigateToUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

}