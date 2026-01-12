/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.designsystem.component

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun KptSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    snackbar: @Composable (SnackbarData) -> Unit = { Snackbar(it) },
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier.testTag("KptSnackbarHost"),
        snackbar = snackbar,
    )
}

suspend fun SnackbarHostState.showKptSnackbar(config: SnackbarConfiguration): SnackbarResult {
    return showSnackbar(
        message = config.message,
        actionLabel = config.actionLabel,
        duration = config.duration,
        withDismissAction = config.withDismissAction,
    )
}

@Immutable
data class SnackbarConfiguration(
    val message: String,
    val actionLabel: String? = null,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val withDismissAction: Boolean = false,
    val onActionClick: (() -> Unit)? = null,
)
