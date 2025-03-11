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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.ktor.util.pipeline.StackWalkingFailedFrame.context
import kotlinx.coroutines.launch
import mifos_mobile.feature.guarantor.generated.resources.Res
import mifos_mobile.feature.guarantor.generated.resources.delete_guarantor
import mifos_mobile.feature.guarantor.generated.resources.dialog_are_you_sure_that_you_want_to_string
import mifos_mobile.feature.guarantor.generated.resources.dismiss
import mifos_mobile.feature.guarantor.generated.resources.yes
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.Network
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.model.entity.guarantor.GuarantorPayload
import org.mifos.mobile.core.ui.component.MifosAlertDialog
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosProgressIndicatorOverlay

@Composable
internal fun GuarantorDetailScreen(
    navigateBack: () -> Unit,
    updateGuarantor: (index: Int, loanId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GuarantorDetailViewModel = koinViewModel(),
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
    var openAlertDialog by rememberSaveable { mutableStateOf(false) }
    val guarantorItem = rememberSaveable { mutableStateOf(GuarantorPayload()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    MifosScaffold(
        topBar = {
            GuarantorDetailTopBar(
                navigateBack = navigateBack,
                deleteGuarantor = { openAlertDialog = true },
                updateGuarantor = updateGuarantor,
            )
        },
        snackbarHostState = snackbarHostState,
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
                        scope.launch {
                            snackbarHostState.showSnackbar(getString(uiState.messageStrRes))
                        }
                        navigateBack()
                    }
                }
            }
            if (openAlertDialog) {
                MifosAlertDialog(
                    onDismissRequest = { openAlertDialog = false },
                    dismissText = stringResource(Res.string.dismiss),
                    confirmationText = stringResource(Res.string.yes),
                    dialogTitle = stringResource(Res.string.delete_guarantor),
                    onConfirmation = {
                        deleteGuarantor.invoke(guarantorItem.value.id ?: -1)
                        openAlertDialog = false
                    },
                    dialogText = stringResource(
                        Res.string.dialog_are_you_sure_that_you_want_to_string,
                        stringResource(Res.string.delete_guarantor),
                    ),
                )
            }
        },
        modifier = modifier,
    )
}
