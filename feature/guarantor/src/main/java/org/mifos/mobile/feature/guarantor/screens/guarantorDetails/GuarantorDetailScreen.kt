/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.guarantor.screens.guarantorDetails

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.ui.component.MifosAlertDialog
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.guarantor.R

@Composable
internal fun GuarantorDetailScreen(
    navigateBack: () -> Unit,
    updateGuarantor: (index: Int, loanId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GuarantorDetailViewModel = hiltViewModel(),
) {
    val uiState = viewModel.guarantorUiState.collectAsStateWithLifecycle()

    GuarantorDetailScreen(
        uiState = uiState.value,
        navigateBack = navigateBack,
        modifier = modifier,
        deleteGuarantor = viewModel::deleteGuarantor,
        updateGuarantor = { updateGuarantor(viewModel.index.value, viewModel.loanId.value) },
    )
}

@Composable
private fun GuarantorDetailScreen(
    uiState: GuarantorDetailUiState,
    navigateBack: () -> Unit,
    deleteGuarantor: (Long) -> Unit,
    updateGuarantor: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var openAlertDialog by rememberSaveable { mutableStateOf(false) }
    val guarantorItem = rememberSaveable { mutableStateOf(GuarantorPayload()) }

    MifosScaffold(
        topBar = {
            GuarantorDetailTopBar(
                navigateBack = navigateBack,
                deleteGuarantor = { openAlertDialog = true },
                updateGuarantor = updateGuarantor,
            )
        },
        content = {
            Box(modifier = Modifier.padding(it)) {
                GuarantorDetailContent(data = guarantorItem.value)
                when (uiState) {
                    is GuarantorDetailUiState.Loading -> {
                        MifosProgressIndicatorOverlay()
                    }

                    is GuarantorDetailUiState.Error -> {
                        MifosErrorComponent(
                            isNetworkConnected = Network.isConnected(context),
                            isEmptyData = false,
                            isRetryEnabled = false,
                        )
                    }

                    is GuarantorDetailUiState.ShowDetail -> {
                        if (uiState.guarantorItem != null) {
                            guarantorItem.value = uiState.guarantorItem
                        } else {
                            MifosErrorComponent(isEmptyData = true)
                        }
                    }

                    is GuarantorDetailUiState.GuarantorDeletedSuccessfully -> {
                        Toast.makeText(
                            context,
                            stringResource(id = uiState.messageResId),
                            Toast.LENGTH_SHORT,
                        ).show()
                        navigateBack()
                    }
                }
            }
            if (openAlertDialog) {
                MifosAlertDialog(
                    onDismissRequest = { openAlertDialog = false },
                    dismissText = stringResource(id = R.string.dismiss),
                    confirmationText = stringResource(id = R.string.yes),
                    dialogTitle = stringResource(id = R.string.delete_guarantor),
                    onConfirmation = {
                        deleteGuarantor.invoke(guarantorItem.value.id ?: -1)
                        openAlertDialog = false
                    },
                    dialogText = stringResource(
                        R.string.dialog_are_you_sure_that_you_want_to_string,
                        stringResource(R.string.delete_guarantor),
                    ),
                )
            }
        },
        modifier = modifier,
    )
}

internal class UiStatesParameterProvider : PreviewParameterProvider<GuarantorDetailUiState> {
    override val values: Sequence<GuarantorDetailUiState>
        get() = sequenceOf(
            GuarantorDetailUiState.ShowDetail(GuarantorPayload()),
            GuarantorDetailUiState.Loading,
            GuarantorDetailUiState.Error(message = null),
        )
}

@DevicePreviews
@Composable
private fun GuarantorDetailScreenPreview(
    @PreviewParameter(UiStatesParameterProvider::class)
    guarantorDetailUiState: GuarantorDetailUiState,
) {
    MifosMobileTheme {
        GuarantorDetailScreen(
            uiState = guarantorDetailUiState,
            navigateBack = {},
            deleteGuarantor = {},
            updateGuarantor = {},
        )
    }
}
