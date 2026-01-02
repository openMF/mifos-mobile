/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Convenience method for observing event flow from [BaseViewModel].
 *
 * By default, events will only be consumed when the associated screen is
 * resumed, to avoid bugs like duplicate navigation calls. To override
 * this behavior, a given event type can implement [BackgroundEvent].
 */
@Composable
fun <E> EventsEffect(
    viewModel: BaseViewModel<*, E, *>,
    lifecycleOwner: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    handler: suspend (E) -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow
            .filter {
                it is BackgroundEvent ||
                    lifecycleOwner.currentState.isAtLeast(Lifecycle.State.RESUMED)
            }
            .onEach { handler.invoke(it) }
            .launchIn(this)
    }
}

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
    lifecycleOwner: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    handler: suspend (E) -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        eventFlow
            .filter {
                it is BackgroundEvent ||
                    lifecycleOwner.currentState.isAtLeast(Lifecycle.State.RESUMED)
            }
            .onEach { handler.invoke(it) }
            .launchIn(this)
    }
}
