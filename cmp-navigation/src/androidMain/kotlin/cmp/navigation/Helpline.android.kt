/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.navigation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import co.touchlab.kermit.Logger
import org.koin.core.context.GlobalContext

actual fun callHelpline() {
    val context: Context = GlobalContext.get().get()
//    val context = androidContext()
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:8000000000")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    context.startActivity(intent)
}

actual fun mailHelpline() {
    val context: Context = GlobalContext.get().get()

    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf("support@mifos.org"))
        putExtra(Intent.EXTRA_SUBJECT, "User Query")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Logger.d("No dialer app found")
        Toast.makeText(
            context,
            "There is no application that support this action",
            Toast.LENGTH_SHORT,
        ).show()
    }
}
