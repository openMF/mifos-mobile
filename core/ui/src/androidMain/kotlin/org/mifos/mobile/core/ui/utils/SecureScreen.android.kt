/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

/**
 * Global counter to handle overlapping lifecycles of screens in NavHost.
 * When navigating from one secure screen to another, both might be active
 * momentarily during transition.
 */
private var secureScreenCount = 0

@Composable
actual fun SecureScreen() {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context.findActivity()
        val window = activity?.window

        if (window != null) {
            secureScreenCount++
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }

        onDispose {
            val activityOnDispose = context.findActivity()
            val windowOnDispose = activityOnDispose?.window
            if (windowOnDispose != null) {
                secureScreenCount--
                if (secureScreenCount <= 0) {
                    windowOnDispose.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                    secureScreenCount = 0
                }
            }
        }
    }
}

private fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}
