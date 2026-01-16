/*
 * Copyright 2026 Mifos Initiative
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.no_internet
import mifos_mobile.core.ui.generated.resources.pay_from
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.CardVariant
import org.mifos.mobile.core.designsystem.component.MifosCustomCard
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import template.core.base.designsystem.KptTheme
import template.core.base.designsystem.theme.KptTheme
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun MifosDetailsCard(
    keyValuePairs: Map<StringResource, String?>,
    modifier: Modifier = Modifier,
) {
    MifosCustomCard(
        variant = CardVariant.OUTLINED,
        modifier = modifier
            .fillMaxWidth()
            .border(
                DesignToken.strokes.thin,
                KptTheme.colorScheme.secondaryContainer,
                KptTheme.shapes.medium,
            ),
        shape = KptTheme.shapes.medium,
    ) {
        Column(modifier = Modifier.padding(KptTheme.spacing.md)) {
            keyValuePairs.forEach { (key, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = KptTheme.spacing.sm),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "${stringResource(key)} :",
                        style = MifosTypography.labelMediumEmphasized,
                    )
                    Text(
                        text = value ?: "",
                        style = MifosTypography.labelMedium,
                        textAlign = TextAlign.Right,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MifosDetailsCardPreview() {
    MifosMobileTheme {
        MifosDetailsCard(
            keyValuePairs = mapOf(
                Res.string.pay_from to "12345",
                Res.string.no_internet to "53736 ncc",
                Res.string.pay_from to "random",
            ),
            modifier = Modifier.padding(16.dp),
        )
    }
}
