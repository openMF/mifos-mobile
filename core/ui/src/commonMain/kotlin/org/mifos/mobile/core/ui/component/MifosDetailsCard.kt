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

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.component.CardVariant
import org.mifos.mobile.core.designsystem.component.MifosCustomCard
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun MifosDetailsCard(
    keyValuePairs: Map<StringResource, String>,
    modifier: Modifier = Modifier,
) {
    MifosCustomCard(
        variant = CardVariant.OUTLINED,
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.secondaryContainer,
                DesignToken.shapes.medium,
            ),
        shape = DesignToken.shapes.medium,
    ) {
        Column(modifier = Modifier.padding(DesignToken.padding.large)) {
            keyValuePairs.forEach { (key, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = DesignToken.padding.small),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "${stringResource(key)} :",
                        style = MifosTypography.labelMediumEmphasized,
                    )
                    Text(
                        text = value,
                        style = MifosTypography.labelMedium,
                        textAlign = TextAlign.Right,
                    )
                }
            }
        }
    }
}
