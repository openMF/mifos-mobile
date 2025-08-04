/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.beneficiary.beneficiaryApplicationConfirmation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.add_beneficiary
import mifos_mobile.feature.beneficiary.generated.resources.beneficiary_updated_successfully
import mifos_mobile.feature.beneficiary.generated.resources.update_beneficiary
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class BeneficiaryApplicationConfirmationViewModel(
    private val beneficiaryRepositoryImp: BeneficiaryRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<BeneficiaryApplicationConfirmationState, BeneficiaryApplicationConfirmationEvent, BeneficiaryApplicationConfirmationAction>(
    initialState = run {
        val route = savedStateHandle.toRoute<BeneficiaryApplicationConfirmationNavRoute>()
        BeneficiaryApplicationConfirmationState(
            dialogState = null,
            beneficiaryId = route.beneficiaryId,
            beneficiaryState = enumValueOf<BeneficiaryState>(route.beneficiaryState),
            name = route.name,
            officeName = route.officeName,
            accountType = route.accountType,
            accountNumber = route.accountNumber,
            transferLimit = route.transferLimit,
        )
    },
) {

    init {
        viewModelScope.launch {
            observeNetworkStatus()
            getTopBarTitle()
        }
    }

    private fun updateState(update: (BeneficiaryApplicationConfirmationState) -> BeneficiaryApplicationConfirmationState) {
        mutableStateFlow.update(update)
    }

    private fun setDialogState(dialogState: BeneficiaryApplicationConfirmationState.DialogState?) {
        updateState { it.copy(dialogState = dialogState) }
    }

    override fun handleAction(action: BeneficiaryApplicationConfirmationAction) {
        when (action) {
            BeneficiaryApplicationConfirmationAction.OnNavigate -> sendEvent(
                BeneficiaryApplicationConfirmationEvent.Navigate,
            )

            BeneficiaryApplicationConfirmationAction.SubmitBeneficiary -> {
                // TODO: in future if we want Update also compare and send payload as needed
                val payload = BeneficiaryPayload(
                    name = state.name,
                    accountNumber = state.accountNumber,
                    transferLimit = state.transferLimit,
                    officeName = state.officeName,
                    accountType = state.accountType,
                    locale = "en",
                )
                createBeneficiary(payload)
            }
        }
    }

    private fun createBeneficiary(payload: BeneficiaryPayload?) {
        setDialogState(BeneficiaryApplicationConfirmationState.DialogState.Loading)
        viewModelScope.launch {
//            val successMsg = getString(Res.string.beneficiary_created_successfully)
            val response = beneficiaryRepositoryImp.createBeneficiary(payload)

            when (response) {
                is DataState.Error -> {
                    setDialogState(null)
                }

                DataState.Loading -> setDialogState(BeneficiaryApplicationConfirmationState.DialogState.Loading)

                is DataState.Success -> {
                    setDialogState(null)
                    sendEvent(BeneficiaryApplicationConfirmationEvent.Navigate)
                }
            }
        }
    }

    // TODO: Change Based on need
    private fun updateBeneficiary(beneficiaryId: Long?, payload: BeneficiaryUpdatePayload?) {
        setDialogState(BeneficiaryApplicationConfirmationState.DialogState.Loading)
        viewModelScope.launch {
            val successMsg = getString(Res.string.beneficiary_updated_successfully)
            val response = beneficiaryRepositoryImp.updateBeneficiary(beneficiaryId, payload)
            when (response) {
                is DataState.Error -> {
                    setDialogState(null)
                }
                DataState.Loading -> setDialogState(BeneficiaryApplicationConfirmationState.DialogState.Loading)
                is DataState.Success -> {
                    setDialogState(null)
                }
            }
        }
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .map(Boolean::not)
                .distinctUntilChanged()
                .collect { isOffline ->
                    updateState {
                        it.copy(
                            networkUnavailable = isOffline,
                            dialogState = if (isOffline) {
                                BeneficiaryApplicationConfirmationState.DialogState.Network
                            } else {
                                null
                            },
                        )
                    }
                }
        }
    }

    private fun getTopBarTitle() {
        val update = Res.string.update_beneficiary
        val add = Res.string.add_beneficiary
        updateState {
            it.copy(
                topBarTitle = when (state.beneficiaryState) {
                    BeneficiaryState.UPDATE -> update
                    else -> add
                },
            )
        }
    }
}

data class BeneficiaryApplicationConfirmationState(
    val topBarTitle: StringResource = Res.string.add_beneficiary,
    val beneficiaryId: Int,
    val name: String,
    val officeName: String,
    val accountType: Int,
    val accountNumber: String,
    val transferLimit: Int,
    val networkUnavailable: Boolean = false,
    val beneficiaryState: BeneficiaryState = BeneficiaryState.CREATE_MANUAL,
    val dialogState: DialogState?,
) {
    sealed interface DialogState {
        data object Loading : DialogState

        data object Network : DialogState
    }
}

sealed interface BeneficiaryApplicationConfirmationEvent {
    data object Navigate : BeneficiaryApplicationConfirmationEvent
}

sealed interface BeneficiaryApplicationConfirmationAction {
    data object SubmitBeneficiary : BeneficiaryApplicationConfirmationAction
    data object OnNavigate : BeneficiaryApplicationConfirmationAction
}
