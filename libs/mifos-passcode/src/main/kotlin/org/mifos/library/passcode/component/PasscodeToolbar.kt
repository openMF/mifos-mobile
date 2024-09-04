/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.library.passcode.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mifos.library.passcode.R
import org.mifos.library.passcode.utility.Step

@Composable
internal fun PasscodeToolbar(
    activeStep: Step,
    hasPasscode: Boolean,
    modifier: Modifier = Modifier,
) {
    var exitWarningDialogVisible by remember { mutableStateOf(false) }

    AnimatedVisibility(exitWarningDialogVisible) {
        ExitWarningDialog(
            visible = exitWarningDialogVisible,
            onConfirm = {},
            onDismiss = {
                exitWarningDialogVisible = false
            },
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        if (!hasPasscode) {
            PasscodeStepIndicator(
                activeStep = activeStep,
            )
        }
    }
}

@Composable
private fun ExitWarningDialog(
    visible: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (visible) {
        AlertDialog(
            shape = MaterialTheme.shapes.large,
            containerColor = Color.White,
            title = {
                Text(
                    text = stringResource(R.string.library_mifos_passcode_exit_message),
                    color = Color.Black,
                )
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(text = stringResource(R.string.library_mifos_passcode_exit))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(R.string.lib_mifos_passcode_cancel))
                }
            },
            onDismissRequest = onDismiss,
        )
    }
}
