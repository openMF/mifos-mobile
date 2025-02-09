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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme

@Composable
fun MifosLoadingDialog(
    visibilityState: LoadingDialogState,
    modifier: Modifier = Modifier,
) {
    when (visibilityState) {
        is LoadingDialogState.Hidden -> Unit
        is LoadingDialogState.Shown -> {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false,
                ),
            ) {
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    ),
                    modifier = modifier
                        .semantics {
                            testTag = "AlertPopup"
                        }
                        .fillMaxWidth()
                        .wrapContentHeight(),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Loading..",
                            modifier = Modifier
                                .testTag("AlertTitleText")
                                .padding(
                                    top = 24.dp,
                                    bottom = 8.dp,
                                ),
                        )
                        CircularProgressIndicator(
                            modifier = Modifier
                                .testTag("AlertProgressIndicator")
                                .padding(
                                    top = 8.dp,
                                    bottom = 24.dp,
                                ),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MifosLoadingDialog_preview() {
    MifosMobileTheme {
        MifosLoadingDialog(
            visibilityState = LoadingDialogState.Shown,
        )
    }
}

/**
 * Models display of a [MifosLoadingDialog].
 */
sealed class LoadingDialogState {
    data object Hidden : LoadingDialogState()

    data object Shown : LoadingDialogState()
}
