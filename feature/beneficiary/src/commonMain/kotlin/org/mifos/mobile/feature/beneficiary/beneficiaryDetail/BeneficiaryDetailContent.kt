/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryDetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.account_number
import mifos_mobile.feature.beneficiary.generated.resources.beneficiary_name
import mifos_mobile.feature.beneficiary.generated.resources.delete
import mifos_mobile.feature.beneficiary.generated.resources.office_name
import mifos_mobile.feature.beneficiary.generated.resources.select_account_type
import mifos_mobile.feature.beneficiary.generated.resources.transfer_limit
import mifos_mobile.feature.beneficiary.generated.resources.update
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.component.MifosOutlinedTextField
import org.mifos.mobile.core.designsystem.component.MifosTextFieldConfig
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosBeneficiaryTopCard
import template.core.base.designsystem.theme.KptTheme

/**
 * Composable function to display beneficiary details.
 *
 * @param state The state of the beneficiary screen.
 * @param onAction The callback to handle actions on the beneficiary screen.
 * @param modifier The modifier for the composable.
 */
@Composable
internal fun BeneficiaryDetailContent(
    state: BeneficiaryDetailState?,
    onAction: (BeneficiaryDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(KptTheme.spacing.md)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.largeIncreased),
    ) {
        ActionBar(
            onAction = onAction,
        )

        MifosBeneficiaryTopCard(
            beneficiary = state?.beneficiary,
        )

        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state?.beneficiary?.name ?: "",
            onValueChange = {},
            label = stringResource(Res.string.beneficiary_name),
            config = MifosTextFieldConfig(
                enabled = false,
            ),
        )

        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state?.beneficiary?.accountNumber ?: "",
            onValueChange = {},
            label = stringResource(Res.string.account_number),
            config = MifosTextFieldConfig(
                enabled = false,
            ),
        )

        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state?.beneficiary?.accountType?.value ?: "",
            onValueChange = {},
            label = stringResource(Res.string.select_account_type),
            config = MifosTextFieldConfig(
                enabled = false,
            ),
        )

        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state?.beneficiary?.officeName ?: "",
            onValueChange = {},
            label = stringResource(Res.string.office_name),
            config = MifosTextFieldConfig(
                enabled = false,
            ),
        )

        MifosOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = (state?.beneficiary?.transferLimit ?: 0).toString(),
            onValueChange = {},
            label = stringResource(Res.string.transfer_limit),
            config = MifosTextFieldConfig(
                enabled = false,
            ),
        )
    }
}

/**
 * A composable function to display a row of actions for the beneficiary detail screen.
 *
 * @param onAction A callback to handle actions from the beneficiary detail screen.
 * @param modifier The modifier to apply to the composable.
 */
@Composable
internal fun ActionBar(
    onAction: (BeneficiaryDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = DesignToken.padding.medium),
        horizontalArrangement = Arrangement.End,
    ) {
        Row(
            modifier = Modifier.clickable {
                onAction(BeneficiaryDetailAction.ShowDeleteConfirmation)
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.xs),
        ) {
            Text(
                text = stringResource(Res.string.delete),
                color = KptTheme.colorScheme.primary,
                style = MifosTypography.bodySmallEmphasized,
            )

            Icon(
                modifier = Modifier.size(DesignToken.sizes.iconSmall),
                imageVector = MifosIcons.Delete,
                contentDescription = "",
                tint = KptTheme.colorScheme.primary,
            )
        }

        Spacer(modifier = Modifier.width(DesignToken.spacing.largeIncreased))

        Row(
            modifier = Modifier.clickable {
                onAction(BeneficiaryDetailAction.OnUpdateBeneficiary)
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.xs),
        ) {
            Text(
                text = stringResource(Res.string.update),
                color = KptTheme.colorScheme.primary,
                style = MifosTypography.bodySmallEmphasized,
            )

            Icon(
                modifier = Modifier.size(DesignToken.sizes.iconSmall),
                imageVector = MifosIcons.Edit,
                contentDescription = "",
                tint = KptTheme.colorScheme.primary,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewBeneficiaryDetailContent() {
    MifosMobileTheme {
        BeneficiaryDetailContent(
            state = BeneficiaryDetailState(beneficiaryDialog = null),
            modifier = Modifier,
            onAction = {},
        )
    }
}
