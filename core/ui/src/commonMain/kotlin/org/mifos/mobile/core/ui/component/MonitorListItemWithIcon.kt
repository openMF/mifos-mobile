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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.core_common_working
import mifos_mobile.core.ui.generated.resources.core_ui_money_in
import mifos_mobile.core.ui.generated.resources.something_went_wrong
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreview

@Composable
fun MonitorListItemWithIcon(
    titleId: StringResource,
    subTitleId: StringResource,
    iconId: DrawableResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable { onClick.invoke() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MifosRoundIcon(
            iconId = iconId,
            modifier = Modifier.size(39.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = stringResource(titleId),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = stringResource(subTitleId),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .alpha(0.7f)
                    .fillMaxWidth(),
            )
        }
    }
}

@DevicePreview
@Composable
fun MonitorListItemWithIconPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        MonitorListItemWithIcon(
            titleId = Res.string.core_common_working,
            subTitleId = Res.string.something_went_wrong,
            iconId = Res.drawable.core_ui_money_in,
            onClick = {},
            modifier = modifier,
        )
    }
}
