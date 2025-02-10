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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.core_ui_money_in
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreview

@Composable
fun MifosMobileIcon(
    mobileIcon: DrawableResource,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Image(
            painter = painterResource(mobileIcon),
            contentDescription = "Mobile Icon",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 56.dp),
        )
    }
}

@DevicePreview
@Composable
fun MifosMobileIconPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        MifosMobileIcon(
            mobileIcon = Res.drawable.core_ui_money_in,
            modifier = modifier,
        )
    }
}
