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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow

/**
 * Convenience method for observing event flow from [BaseViewModel].
 *
 * By default, events will only be consumed when the associated screen is
 * resumed, to avoid bugs like duplicate navigation calls. To override
 * this behavior, a given event type can implement [BackgroundEvent].
 */
@Composable
fun <E> EventsEffect(
    eventFlow: Flow<E>,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    handler: suspend (E) -> Unit,
) {
    LaunchedEffect(key1 = eventFlow, key2 = lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            eventFlow.collect {
                handler.invoke(it)
            }
        }
    }
}
