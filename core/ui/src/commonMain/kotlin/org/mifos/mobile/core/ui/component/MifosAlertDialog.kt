/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import org.mifos.mobile.core.designsystem.component.MifosTextButton
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreviews

@Composable
fun MifosAlertDialog(
    dialogTitle: String,
    dialogText: String,
    dismissText: String,
    confirmationText: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    AlertDialog(
        icon = {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = null)
            }
        },
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        confirmButton = {
            MifosTextButton(
                text = { Text(text = confirmationText) },
                onClick = onConfirmation,
            )
        },
        dismissButton = {
            MifosTextButton(
                text = { Text(text = dismissText) },
                onClick = onDismissRequest,
            )
        },
    )
}

@DevicePreviews
@Composable
private fun MifosAlertDialogPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        MifosAlertDialog(
            dialogTitle = "Dialog Title",
            dialogText = "Dialog Text",
            dismissText = "Dismiss",
            confirmationText = "Confirm",
            onDismissRequest = {},
            onConfirmation = {},
            modifier = modifier,
        )
    }
}
