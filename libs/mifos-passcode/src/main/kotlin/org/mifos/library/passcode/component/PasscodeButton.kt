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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mifos.library.passcode.R
import org.mifos.library.passcode.theme.forgotButtonStyle
import org.mifos.library.passcode.theme.skipButtonStyle

@Composable
internal fun PasscodeSkipButton(
    hasPassCode: Boolean,
    onSkipButton: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!hasPassCode) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(
                onClick = { onSkipButton.invoke() },
            ) {
                Text(text = stringResource(R.string.library_mifos_passcode_skip), style = skipButtonStyle)
            }
        }
    }
}

@Composable
internal fun PasscodeForgotButton(
    hasPassCode: Boolean,
    onForgotButton: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (hasPassCode) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            TextButton(
                onClick = { onForgotButton.invoke() },
            ) {
                Text(
                    text = stringResource(R.string.library_mifos_passcode_login_manually),
                    style = forgotButtonStyle,
                )
            }
        }
    }
}
