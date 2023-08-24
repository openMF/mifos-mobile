package org.mifos.mobile.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri

class StartActivity(private val context: Context) {

    fun startActivity(uri: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
    }

    fun <T: Activity> startActivity(clazz: Class<T>) {
        context.startActivity(Intent(context, clazz))
    }

}