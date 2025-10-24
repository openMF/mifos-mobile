/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.accounts.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.accounts.generated.resources.Res
import mifos_mobile.feature.accounts.generated.resources.feature_filters_count
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.designsystem.utils.onClick
import org.mifos.mobile.feature.accounts.model.CheckboxStatus
import org.mifos.mobile.feature.accounts.model.TransactionCheckboxStatus

/**
 * Composable function for the Filter Section.
 *
 * @param title The title of the filter section.
 * @param filtersSelected The number of filters selected.
 * @param isExpanded Boolean indicating whether the filter section is expanded.
 * @param onToggle The function to be called when the filter section is toggled.
 * @param filters The list of filters to be displayed.
 * @param onCheckChanged The function to be called when a filter is checked.
 * @param modifier Optional modifier for the filter section.
 * @param isRadio Boolean indicating whether the filter section is a radio button.
 * @param selectedRadioButton The currently selected radio button.
 */
@Composable
internal fun FilterSection(
    title: String,
    filtersSelected: Int,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    filters: List<Any>,
    onCheckChanged: (StringResource) -> Unit,
    modifier: Modifier = Modifier,
    isRadio: Boolean = false,
    selectedRadioButton: StringResource? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(
                start = DesignToken.spacing.extraLargeIncreased,
                end = DesignToken.spacing.small,
                top = DesignToken.padding.medium,
            ),
            verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = MifosTypography.labelLargeEmphasized,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(DesignToken.spacing.extraSmall),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (filtersSelected != 0) {
                        Text(
                            text = stringResource(Res.string.feature_filters_count, filtersSelected),
                            style = MifosTypography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                    Icon(
                        modifier = Modifier
                            .width(DesignToken.sizes.iconSmall)
                            .height(DesignToken.sizes.iconSmall),
                        imageVector = if (isExpanded) MifosIcons.ChevronUp else MifosIcons.ChevronDown,
                        contentDescription = null,
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = slideInVertically(initialOffsetY = { -40 }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { 40 }) + fadeOut(),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(DesignToken.padding.medium),
                ) {
                    filters.forEach { filter ->
                        when (filter) {
                            is CheckboxStatus ->
                                FilterCheckboxUI(
                                    filter.statusLabel,
                                    filter.isChecked,
                                    { onCheckChanged(filter.statusLabel) },
                                )
                            is TransactionCheckboxStatus -> {
                                FilterCheckboxUI(
                                    filter.statusLabel,
                                    if (isRadio) {
                                        filter.statusLabel == selectedRadioButton
                                    } else {
                                        filter.isChecked
                                    },
                                    { onCheckChanged(filter.statusLabel) },
                                    isRadio = isRadio,
                                )
                            }
                        }
                    }
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(top = DesignToken.padding.medium)
                .height(1.dp),
        )
    }
}

/**
 * Composable function for the Filter Checkbox UI.
 *
 * @param statusLabel The label of the filter checkbox.
 * @param isChecked Boolean indicating whether the filter checkbox is checked.
 * @param onCheckedChange The function to be called when the filter checkbox is checked.
 * @param modifier Optional modifier for the filter checkbox.
 * @param isRadio Boolean indicating whether the filter checkbox is a radio button.
 */
@Composable
fun FilterCheckboxUI(
    statusLabel: StringResource,
    isChecked: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier,
    isRadio: Boolean = false,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .onClick {
                onCheckedChange()
            }
            .padding(horizontal = DesignToken.padding.largeIncreased),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isRadio) {
            RadioButton(
                modifier = Modifier.size(DesignToken.sizes.iconSmall),
                selected = isChecked,
                onClick = { onCheckedChange() },
            )
        } else {
            Checkbox(
                modifier = Modifier.size(DesignToken.sizes.iconSmall),
                checked = isChecked,
                onCheckedChange = { onCheckedChange() },
            )
        }

        Spacer(modifier = Modifier.width(DesignToken.spacing.small))
        Text(
            text = stringResource(statusLabel),
            style = MifosTypography.labelMediumEmphasized,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}
