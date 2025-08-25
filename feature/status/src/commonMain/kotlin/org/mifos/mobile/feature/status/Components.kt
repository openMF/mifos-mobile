/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.status

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.ic_icon_error
import mifos_mobile.core.ui.generated.resources.ic_icon_success
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.model.EventType
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.component.MifosStatusComponent
import org.mifos.mobile.core.ui.utils.EventsEffect

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun StatusScreen(
    navigateToDestination: (String) -> Unit,
    viewModel: StatusViewModel = koinViewModel(),
) {
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    val clickHandler: () -> Unit = {
        coroutineScope.launch {
            when (uiState.eventDestination) {
                Constants.UNLOCKED -> {
                    viewModel.trySendAction(StatusAction.UnlockApp)
                }
                else -> {
                    viewModel.trySendAction(StatusAction.OnNextClick)
                }
            }
        }
    }

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is StatusEvent.NavigateNext -> navigateToDestination(event.nextScreen)
        }
    }

    MifosScaffold(
        bottomBar = {
            Surface {
                MifosPoweredCard(
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(DesignToken.padding.large),
            verticalArrangement = Arrangement.Center,
        ) {
            MifosStatusComponent(
                icon = if (uiState.eventType == EventType.SUCCESS.name) {
                    Res.drawable.ic_icon_success
                } else {
                    Res.drawable.ic_icon_error
                },
                title = uiState.title ?: "",
                subTitle = uiState.subtitle ?: "",
                buttonText = uiState.buttonText ?: "",
                onClick = clickHandler,
            )
        }
    }
}
