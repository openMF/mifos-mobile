/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.platform

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import template.core.base.platform.context.AppContext
import template.core.base.platform.context.activity
import template.core.base.platform.intent.IntentManagerImpl
import template.core.base.platform.review.AppReviewManagerImpl
import template.core.base.platform.update.AppUpdateManagerImpl

/**
 * Android-specific implementation of the LocalManagerProvider composable function.
 *
 * This implementation initializes the platform-specific manager instances required
 * by the application and provides them to the composition hierarchy through
 * CompositionLocal providers. The function handles the creation and provision of:
 *
 * - AppReviewManager: For managing in-app review requests through Google Play
 * - IntentManager: For handling Android-specific intent operations
 * - AppUpdateManager: For managing application updates through Google Play
 *
 * The function retrieves the current Activity from the provided AppContext and uses
 * it to initialize each manager implementation. All managers are then made available
 * to child composables through their respective CompositionLocal providers.
 *
 * @param context The Android Context used to initialize the managers
 * @param content The composable content where the managers will be available
 */
@Composable
actual fun LocalManagerProvider(
    context: AppContext,
    content: @Composable () -> Unit,
) {
    val activity = context.activity as Activity
    CompositionLocalProvider(
        LocalAppReviewManager provides AppReviewManagerImpl(activity),
        LocalIntentManager provides IntentManagerImpl(activity),
        LocalAppUpdateManager provides AppUpdateManagerImpl(activity),
    ) {
        content()
    }
}
