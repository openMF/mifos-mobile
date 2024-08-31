/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryApplication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.beneficiary.R

@Composable
internal fun BeneficiaryApplicationScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BeneficiaryApplicationViewModel = hiltViewModel(),
) {
    val beneficiaryState by viewModel.beneficiaryState.collectAsStateWithLifecycle()
    val beneficiary by viewModel.beneficiary.collectAsStateWithLifecycle()
    val uiState by viewModel.beneficiaryUiState.collectAsStateWithLifecycle()

    BeneficiaryApplicationScreen(
        uiState = uiState,
        beneficiaryState = beneficiaryState,
        beneficiary = beneficiary,
        navigateBack = navigateBack,
        onRetry = viewModel::loadBeneficiaryTemplate,
        onSubmit = viewModel::submitBeneficiary,
        modifier = modifier,
    )
}

@Composable
private fun BeneficiaryApplicationScreen(
    uiState: BeneficiaryApplicationUiState,
    beneficiaryState: BeneficiaryState,
    beneficiary: Beneficiary?,
    navigateBack: () -> Unit,
    onRetry: () -> Unit,
    onSubmit: (BeneficiaryPayload) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    MifosScaffold(
        topBarTitleResId = when (beneficiaryState) {
            BeneficiaryState.UPDATE -> R.string.update_beneficiary
            else -> R.string.add_beneficiary
        },
        navigateBack = navigateBack,
        modifier = modifier,
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues = paddingValues),
            ) {
                when (uiState) {
                    is BeneficiaryApplicationUiState.Error -> {
                        MifosErrorComponent(
                            isNetworkConnected = Network.isConnected(context),
                            isRetryEnabled = true,
                            onRetry = onRetry,
                            message = stringResource(id = uiState.errorResId),
                        )
                    }

                    is BeneficiaryApplicationUiState.Loading -> {
                        MifosProgressIndicator()
                    }

                    is BeneficiaryApplicationUiState.Success -> {
                        navigateBack()
                    }

                    is BeneficiaryApplicationUiState.Template -> {
                        BeneficiaryApplicationContent(
                            prefilledBeneficiary = beneficiary,
                            beneficiaryTemplate = uiState.beneficiaryTemplate,
                            beneficiaryState = beneficiaryState,
                            onSubmit = onSubmit,
                        )
                    }
                }
            }
        },
    )
}

@DevicePreviews
@Composable
private fun BeneficiaryApplicationScreenPreview(
    @PreviewParameter(BeneficiaryApplicationUiPreview::class)
    uiState: BeneficiaryApplicationUiState,
) {
    MifosMobileTheme {
        BeneficiaryApplicationScreen(
            uiState = uiState,
            beneficiaryState = BeneficiaryState.CREATE_QR,
            beneficiary = Beneficiary(),
            navigateBack = { },
            onRetry = { },
            onSubmit = { },
        )
    }
}

internal class BeneficiaryApplicationUiPreview :
    PreviewParameterProvider<BeneficiaryApplicationUiState> {
    override val values: Sequence<BeneficiaryApplicationUiState>
        get() = sequenceOf(
            BeneficiaryApplicationUiState.Template(BeneficiaryTemplate()),
            BeneficiaryApplicationUiState.Error(1),
            BeneficiaryApplicationUiState.Loading,
        )
}
