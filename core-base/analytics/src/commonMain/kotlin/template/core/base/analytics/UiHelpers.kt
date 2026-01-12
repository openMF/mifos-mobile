/*
 * Copyright 2023 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.analytics

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier

/**
 * Global key used to obtain access to the AnalyticsHelper through a CompositionLocal.
 */
val LocalAnalyticsHelper = staticCompositionLocalOf<AnalyticsHelper> {
    // Provide a default AnalyticsHelper which does nothing. This is so that tests and previews
    // do not have to provide one. For real app builds provide a different implementation.
    NoOpAnalyticsHelper()
}

/**
 * Composable function to track screen views automatically
 */
@Composable
@Suppress("SpreadOperator")
fun TrackScreenView(
    screenName: String,
    sourceScreen: String? = null,
    additionalParams: Map<String, String> = emptyMap(),
) {
    val analytics = LocalAnalyticsHelper.current

    LaunchedEffect(screenName) {
        analytics.logScreenView(screenName, sourceScreen)
        // Log additional params if provided
        if (additionalParams.isNotEmpty()) {
            analytics.logEvent(
                Types.SCREEN_VIEW,
                mapOf(
                    ParamKeys.SCREEN_NAME to screenName,
                    *additionalParams.toList().toTypedArray(),
                ).plus(sourceScreen?.let { mapOf(ParamKeys.SOURCE_SCREEN to it) } ?: emptyMap()),
            )
        }
    }
}

/**
 * Modifier extension for tracking button clicks
 */
fun Modifier.trackClick(
    buttonName: String,
    analytics: AnalyticsHelper,
    screenName: String? = null,
    additionalParams: Map<String, String> = emptyMap(),
): Modifier = this.clickable {
    analytics.logButtonClick(buttonName, screenName)
    if (additionalParams.isNotEmpty()) {
        analytics.logEvent(Types.BUTTON_CLICK, additionalParams)
    }
}

/**
 * Remember analytics helper from composition local
 */
@Composable
fun rememberAnalyticsHelper(): AnalyticsHelper = LocalAnalyticsHelper.current

/**
 * Effect for tracking when a composable enters/exits composition
 */
@Composable
fun TrackComposableLifecycle(
    name: String,
    trackEntry: Boolean = true,
    trackExit: Boolean = false,
) {
    val analytics = LocalAnalyticsHelper.current

    if (trackEntry) {
        LaunchedEffect(name) {
            analytics.logEvent(
                "composable_entered",
                ParamKeys.CONTENT_NAME to name,
            )
        }
    }

    if (trackExit) {
        DisposableEffect(name) {
            onDispose {
                analytics.logEvent(
                    "composable_exited",
                    ParamKeys.CONTENT_NAME to name,
                )
            }
        }
    }
}
