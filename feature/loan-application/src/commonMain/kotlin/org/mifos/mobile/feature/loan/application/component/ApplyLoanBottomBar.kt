/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.loan_application.generated.resources.Res
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_title
import mifos_mobile.feature.loan_application.generated.resources.feature_loan_product_details_terms
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.designsystem.utils.onClick

/**
 * A bottom bar for the loan application screen, featuring a terms agreement
 * checkbox and a submission button.
 *
 * @param checked The current selection state of the terms agreement checkbox.
 * @param isEnabled Controls whether the 'Apply' button is clickable.
 * @param onCheckedChange Callback invoked when the terms checkbox is toggled.
 * @param onApplyClick Action to perform when the 'Apply' button is clicked.
 */
@Composable
fun ApplyLoanBottomBar(
    checked: Boolean,
    isEnabled: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onApplyClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(DesignToken.padding.large),
        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.large),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onClick { onCheckedChange(!checked) },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                modifier = Modifier.height(18.dp),
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = AppColors.primaryBlue,
                ),
            )
            Text(
                text = stringResource(Res.string.feature_loan_product_details_terms),
                style = MifosTypography.labelMediumEmphasized,
            )
        }

        MifosButton(
            modifier = Modifier.fillMaxWidth().height(DesignToken.sizes.inputHeight),
            onClick = onApplyClick,
            enabled = isEnabled,
            shape = DesignToken.shapes.medium,
        ) {
            Text(
                text = stringResource(Res.string.feature_apply_loan_title),
                style = MifosTypography.titleMedium,
            )
        }
    }
}
