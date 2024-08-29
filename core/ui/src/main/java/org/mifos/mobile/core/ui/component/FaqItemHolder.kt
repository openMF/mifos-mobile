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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.designsystem.icons.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreviews

@Composable
fun FaqItemHolder(
    index: Int,
    isSelected: Boolean,
    onItemSelected: (Int) -> Unit,
    question: String?,
    answer: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    onItemSelected.invoke(index)
                }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = question.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )

            Icon(
                imageVector = MifosIcons.ArrowDropDown,
                contentDescription = "drop down",
                tint = if (isSystemInDarkTheme()) Color.White else Color.Gray,
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
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            )
        }

        HorizontalDivider()
    }
}

@DevicePreviews
@Composable
private fun FaqItemHolderPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        FaqItemHolder(
            index = 0,
            isSelected = false,
            onItemSelected = {},
            question = "What is Mifos?",
            answer = "Mifos is a platform for financial inclusion.",
            modifier = modifier,
        )
    }
}
