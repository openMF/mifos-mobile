/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.third.party.transfer

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.third_party_transfer.generated.resources.Res
import mifos_mobile.feature.third_party_transfer.generated.resources.internet_not_connected
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
import org.mifos.mobile.core.data.repository.ThirdPartyTransferRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.IgnoredOnParcel
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.entity.templates.account.AccountOption
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class ThirdPartyTransferViewModel(
    private val transferRepository: ThirdPartyTransferRepository,
    private val beneficiaryRepository: BeneficiaryRepository,
    private val networkMonitor: NetworkMonitor,
) : BaseViewModel<ThirdPartyTransferState, ThirdPartyTransferEvent, ThirdPartyTransferAction>(
    initialState = ThirdPartyTransferState(dialogState = null),
) {

    init {
        viewModelScope.launch {
            fetchAndUpdateTemplateState()
            val message = getString(Res.string.internet_not_connected)
            networkMonitor.isOnline.collect { isConnected ->
                updateState { it.copy(isOnline = isConnected) }
                if (!isConnected) {
                    updateState {
                        it.copy(
                            dialogState = ThirdPartyTransferState.DialogState.Error(message),
                        )
                    }
                }
            }
        }
    }

    private fun updateState(update: (ThirdPartyTransferState) -> ThirdPartyTransferState) {
        mutableStateFlow.update(update)
    }

    override fun handleAction(action: ThirdPartyTransferAction) {
        when (action) {
            ThirdPartyTransferAction.OnAddBeneficiary -> sendEvent(ThirdPartyTransferEvent.AddBeneficiary)
            ThirdPartyTransferAction.OnNavigate -> sendEvent(ThirdPartyTransferEvent.Navigate)
            is ThirdPartyTransferAction.OnReviewTransfer -> {
                sendEvent(
                    ThirdPartyTransferEvent.ReviewTransfer(
                        action.reviewTransferPayload,
                        action.transferType,
                    ),
                )
            }
        }
    }

    private suspend fun fetchAndUpdateTemplateState() {
        combine(
            transferRepository.thirdPartyTransferTemplate(),
            beneficiaryRepository.beneficiaryList(),
        ) { templateResult, beneficiariesResult ->
            updateStateFromResults(templateResult, beneficiariesResult)
        }.catch { error ->
            updateState {
                it.copy(dialogState = ThirdPartyTransferState.DialogState.Error(error.message ?: "An error occurred"))
            }
        }.collect { }
    }

    private fun updateStateFromResults(
        templateResult: DataState<AccountOptionsTemplate>,
        beneficiariesResult: DataState<List<Beneficiary>>,
    ) {
        when {
            templateResult is DataState.Loading || beneficiariesResult is DataState.Loading -> {
                updateState { it.copy(dialogState = ThirdPartyTransferState.DialogState.Loading) }
            }
            templateResult is DataState.Error || beneficiariesResult is DataState.Error -> {
                val error = (templateResult as? DataState.Error)?.exception?.message
                    ?: (beneficiariesResult as? DataState.Error)?.exception?.message
                    ?: "An error occurred"
                val errorMessage = "An error occurred"
                updateState { it.copy(dialogState = ThirdPartyTransferState.DialogState.Error(errorMessage)) }
            }
            templateResult is DataState.Success && beneficiariesResult is DataState.Success -> {
                updateState {
                    it.copy(
                        fromAccountDetail = templateResult.data.fromAccountOptions
                            .filter { savingsAccount ->
                                savingsAccount.accountType?.value == "Savings Account"
                            },
                        toAccountOption = templateResult.data.toAccountOptions,
                        beneficiaries = beneficiariesResult.data,
                        dialogState = null,
                    )
                }
            }
        }
    }
}

@Parcelize
data class ThirdPartyTransferState(
    val isOnline: Boolean = false,
    @IgnoredOnParcel
    val fromAccountDetail: List<AccountOption>? = listOf(),
    @IgnoredOnParcel
    val toAccountOption: List<AccountOption>? = listOf(),
    @IgnoredOnParcel
    val beneficiaries: List<Beneficiary>? = listOf(),
    val dialogState: DialogState? = null,
) : Parcelable {

    sealed interface DialogState : Parcelable {
        @Parcelize
        data class Error(val message: String) : DialogState

        @Parcelize
        data object Loading : DialogState
    }
}

sealed interface ThirdPartyTransferEvent {
    data object Navigate : ThirdPartyTransferEvent
    data object AddBeneficiary : ThirdPartyTransferEvent
    data class ReviewTransfer(
        val reviewTransferPayload: ReviewTransferPayload,
        val transferType: TransferType,
    ) : ThirdPartyTransferEvent
}

sealed interface ThirdPartyTransferAction {
    data object OnNavigate : ThirdPartyTransferAction
    data object OnAddBeneficiary : ThirdPartyTransferAction
    data class OnReviewTransfer(
        val reviewTransferPayload: ReviewTransferPayload,
        val transferType: TransferType,
    ) : ThirdPartyTransferAction
}
