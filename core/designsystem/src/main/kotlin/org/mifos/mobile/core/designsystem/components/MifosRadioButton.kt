/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.designsystem.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme

@Composable
fun MifosRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    textResId: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
        )
        Text(text = stringResource(id = textResId))
    }
}

@Preview(showSystemUi = true)
@Composable
private fun MifosRadioButtonPreview() {
    MifosMobileTheme {
        MifosRadioButton(
            selected = false,
            onClick = {},
            textResId = 1,
        )
    }
}
