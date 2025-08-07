/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.ic_icon_success
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography

@Composable
fun MifosSuccessDialog(
    visibilityState: SuccessDialogState,
): Unit = when (visibilityState) {
    SuccessDialogState.Hidden -> Unit
    is SuccessDialogState.Shown -> {
        AlertDialog(
            onDismissRequest = {},
            icon = {
                Image(
                    painter = painterResource(Res.drawable.ic_icon_success),
                    contentDescription = "DialogIcon",
                )
            },
            confirmButton = {
                MifosButton(
                    text = {
                        Text(stringResource(visibilityState.buttonText))
                    },
                    onClick = visibilityState.onBtnClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("AcceptAlertButton"),
                )
            },
            title = {
                Text(
                    text = stringResource(visibilityState.title),
                    style = MifosTypography.titleSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("AlertTitleText"),
                )
            },
            text = {
                if (visibilityState.message != null) {
                    Text(
                        text = stringResource(visibilityState.message),
                        style = MifosTypography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("AlertContentText"),
                    )
                }
            },
            shape = DesignToken.shapes.large,
        )
    }
}

sealed interface SuccessDialogState {

    data object Hidden : SuccessDialogState

    data class Shown(
        val title: StringResource,
        val message: StringResource?,
        val buttonText: StringResource,
        val onBtnClick: () -> Unit,
    ) : SuccessDialogState
}
