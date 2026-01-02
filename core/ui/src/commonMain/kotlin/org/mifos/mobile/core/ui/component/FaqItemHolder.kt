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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import org.mifos.mobile.core.designsystem.component.MifosCustomCard
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.utils.DevicePreview
import template.core.base.designsystem.theme.KptTheme

@Composable
fun FaqItemHolder(
    index: Int,
    isSelected: Boolean,
    onItemSelected: (Int) -> Unit,
    question: String?,
    answer: String?,
    modifier: Modifier = Modifier,
) {
    MifosCustomCard(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = DesignToken.strokes.thin,
                color = KptTheme.colorScheme.primary,
                shape = KptTheme.shapes.medium,
            ),
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    onItemSelected.invoke(index)
                }
                .padding(all = DesignToken.padding.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = question.orEmpty(),
                style = MifosTypography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = KptTheme.colorScheme.primary,
            )

            Icon(
                imageVector = MifosIcons.ArrowDropDown,
                contentDescription = "drop down",
                modifier = Modifier
                    .scale(1f, if (isSelected) -1f else 1f),
            )
        }

        AnimatedVisibility(
            visible = isSelected,
            enter = fadeIn() + expandVertically(
                animationSpec = spring(
                    stiffness = Spring.StiffnessMedium,
                ),
            ),
        ) {
            Text(
                text = answer.orEmpty(),
                style = KptTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = DesignToken.padding.medium),
            )
        }

        HorizontalDivider()
    }
}

@DevicePreview
@Composable
fun FaqItemHolderPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        FaqItemHolder(
            index = 0,
            isSelected = true,
            onItemSelected = {},
            question = "What is Mifos?",
            answer = "Mifos is a platform for financial inclusion.",
            modifier = modifier,
        )
    }
}
