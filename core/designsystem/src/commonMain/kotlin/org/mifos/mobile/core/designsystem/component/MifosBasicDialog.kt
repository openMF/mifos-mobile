/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.designsystem.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme

@Composable
fun MifosBasicDialog(
    visibilityState: BasicDialogState,
    onDismissRequest: () -> Unit,
): Unit = when (visibilityState) {
    BasicDialogState.Hidden -> Unit
    is BasicDialogState.Shown -> {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                MifosTextButton(
                    content = {
                        Text(text = "Ok")
                    },
                    onClick = onDismissRequest,
                    modifier = Modifier.testTag("AcceptAlertButton"),
                )
            },
            title = visibilityState.title.let {
                {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.testTag("AlertTitleText"),
                    )
                }
            },
            text = {
                Text(
                    text = visibilityState.message,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.testTag("AlertContentText"),
                )
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.semantics {
                testTag = "AlertPopup"
            },
        )
    }
}

@Composable
fun MifosBasicDialog(
    visibilityState: BasicDialogState,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
): Unit = when (visibilityState) {
    BasicDialogState.Hidden -> Unit
    is BasicDialogState.Shown -> {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                MifosTextButton(
                    content = {
                        Text(text = "Ok")
                    },
                    onClick = onConfirm,
                    modifier = Modifier.testTag("AcceptAlertButton"),
                )
            },
            dismissButton = {
                MifosTextButton(
                    content = {
                        Text(text = "Cancel")
                    },
                    onClick = onDismissRequest,
                    modifier = Modifier.testTag("DismissAlertButton"),
                )
            },
            title = visibilityState.title.let {
                {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.testTag("AlertTitleText"),
                    )
                }
            },
            text = {
                Text(
                    text = visibilityState.message,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.testTag("AlertContentText"),
                )
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.semantics {
                testTag = "AlertPopup"
            },
        )
    }
}

@Preview
@Composable
private fun MifosBasicDialog_preview() {
    MifosMobileTheme {
        MifosBasicDialog(
            visibilityState = BasicDialogState.Shown(
                title = "An error has occurred.",
                message = "Username or password is incorrect. Try again.",
            ),
            onDismissRequest = {},
        )
    }
}

/**
 * Models display of a [MifosBasicDialog].
 */
sealed class BasicDialogState {

    data object Hidden : BasicDialogState()

    data class Shown(
        val message: String,
        val title: String = "An Error Occurred!",
    ) : BasicDialogState()
}
