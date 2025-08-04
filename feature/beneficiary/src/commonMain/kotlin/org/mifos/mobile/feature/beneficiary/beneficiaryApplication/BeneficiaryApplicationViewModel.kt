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

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.beneficiary.generated.resources.Res
import mifos_mobile.feature.beneficiary.generated.resources.add_beneficiary
import mifos_mobile.feature.beneficiary.generated.resources.enter_account_number
import mifos_mobile.feature.beneficiary.generated.resources.enter_beneficiary_name
import mifos_mobile.feature.beneficiary.generated.resources.enter_office_name
import mifos_mobile.feature.beneficiary.generated.resources.enter_transfer_limit
import mifos_mobile.feature.beneficiary.generated.resources.invalid_amount
import mifos_mobile.feature.beneficiary.generated.resources.select_account_type
import mifos_mobile.feature.beneficiary.generated.resources.update_beneficiary
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.feature.beneficiary.beneficiaryApplication.BeneficiaryApplicationEvent.*

internal class BeneficiaryApplicationViewModel(
    private val beneficiaryRepositoryImp: BeneficiaryRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<BeneficiaryApplicationState, BeneficiaryApplicationEvent, BeneficiaryApplicationAction>(
    initialState = run {
        val route = savedStateHandle.toRoute<BeneficiaryApplicationNavRoute>()
        BeneficiaryApplicationState(
            dialogState = null,
            beneficiaryId = route.beneficiaryId,
            beneficiaryState = enumValueOf<BeneficiaryState>(route.beneficiaryState),
        )
    },
) {

    init {
        viewModelScope.launch {
            observeNetworkStatus()
            getTopBarTitle()
            loadBeneficiaryAndTemplate()
        }
    }

    private fun getTopBarTitle() {
        viewModelScope.launch {
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

    private fun updateState(update: (BeneficiaryApplicationState) -> BeneficiaryApplicationState) {
        mutableStateFlow.update(update)
    }

    private fun setDialogState(dialogState: BeneficiaryApplicationState.DialogState?) {
        updateState { it.copy(dialogState = dialogState) }
    }

    override fun handleAction(action: BeneficiaryApplicationAction) {
        when (action) {
            BeneficiaryApplicationAction.LoadBeneficiaryTemplate -> {
                viewModelScope.launch {
                    loadBeneficiaryAndTemplate()
                }
            }
            is BeneficiaryApplicationAction.SubmitBeneficiary -> requestPayload(action.payload)

            BeneficiaryApplicationAction.OnNavigate -> sendEvent(
                BeneficiaryApplicationEvent.Navigate,
            )

            BeneficiaryApplicationAction.OnRetry -> {
                viewModelScope.launch {
                    loadBeneficiaryAndTemplate()
                }
            }

            is BeneficiaryApplicationAction.OnFieldChange -> onFieldChange(
                accountType = action.accountType,
                accountNumber = action.accountNumber,
                officeName = action.officeName,
                transferLimit = action.transferLimit,
                beneficiaryName = action.beneficiaryName,
            )

            is BeneficiaryApplicationAction.Internal.ReceiveBeneficiaryResult -> {
                updateStateFromResults(
                    action.beneficiaryList,
                    action.beneficiaryTemplate)
            }
        }
    }

    private fun loadBeneficiaryAndTemplate() {
        combine(
            beneficiaryRepositoryImp.beneficiaryList(),
            beneficiaryRepositoryImp.beneficiaryTemplate(),
        ) { beneficiaryList, beneficiaryTemplate ->
            sendAction(BeneficiaryApplicationAction.Internal.ReceiveBeneficiaryResult(beneficiaryList, beneficiaryTemplate))
        }.catch { error ->
            setDialogState(
                BeneficiaryApplicationState.DialogState.Error(
                error.message ?: "An error occurred",),)
        }.launchIn(viewModelScope)
    }

    private fun updateStateFromResults(
        beneficiaryList: DataState<List<Beneficiary>>,
        beneficiaryTemplate: DataState<BeneficiaryTemplate>,
    ) {
        when {
            beneficiaryList is DataState.Loading || beneficiaryTemplate is DataState.Loading -> {
                setDialogState( BeneficiaryApplicationState.DialogState.Loading)
            }
            beneficiaryList is DataState.Error || beneficiaryTemplate is DataState.Error -> {
                val error = (beneficiaryList as? DataState.Error)?.exception?.message
                    ?: (beneficiaryTemplate as? DataState.Error)?.exception?.message
                    ?: "An error occurred"
                setDialogState( BeneficiaryApplicationState.DialogState.Error(error))
            }
            beneficiaryList is DataState.Success && beneficiaryTemplate is DataState.Success -> {
                updateState { currentState ->
                    currentState.copy(
                        dialogState = null,
                        beneficiary = beneficiaryList.data.find { it.id == currentState.beneficiaryId },
                        template = beneficiaryTemplate.data,
                    )
                }
            }
        }
    }

    private fun requestPayload(payload: BeneficiaryPayload){
        if(validateFields(payload)){
            viewModelScope.launch {
                sendEvent(BeneficiaryApplicationEvent.SubmitBeneficiary(payload,state.beneficiaryState))
            }
        }
    }

    private fun validateFields(payload: BeneficiaryPayload): Boolean {
        var hasError = false

        val updatedState = state.copy(
            accountTypeError = if (state.beneficiaryState != BeneficiaryState.UPDATE && payload.accountType == -1) {
                hasError = true
                Res.string.select_account_type
            } else {
                null
            },

            accountNumberError = if (state.beneficiaryState != BeneficiaryState.UPDATE &&
                payload.accountNumber?.trim()?.isEmpty() == true
            ) {
                hasError = true
                Res.string.enter_account_number
            } else {
                null
            },

            officeNameError = if (state.beneficiaryState != BeneficiaryState.UPDATE &&
                payload.officeName?.trim()?.isEmpty() == true
            ) {
                hasError = true
                Res.string.enter_office_name
            } else {
                null
            },

            transferLimitError = when {
                payload.transferLimit == 0 -> {
                    hasError = true
                    Res.string.enter_transfer_limit
                }
                payload.transferLimit?.rem(1) != 0 -> {
                    hasError = true
                    Res.string.invalid_amount
                }
                else -> null
            },

            beneficiaryNameError = if (payload.name?.trim()?.isEmpty() == true) {
                hasError = true
                Res.string.enter_beneficiary_name
            } else {
                null
            },
        )

        updateState { updatedState }

        return !hasError
    }

    private fun onFieldChange(
        accountType: Int? = null,
        accountNumber: String? = null,
        officeName: String? = null,
        transferLimit: String? = null,
        beneficiaryName: String? = null,
    ) {
        updateState { currentState ->
            currentState.copy(
                accountTypeError = if (accountType != null) null else currentState.accountTypeError,
                accountNumberError = if (accountNumber != null) null else currentState.accountNumberError,
                officeNameError = if (officeName != null) null else currentState.officeNameError,
                transferLimitError = if (transferLimit != null) null else currentState.transferLimitError,
                beneficiaryNameError = if (beneficiaryName != null) null else currentState.beneficiaryNameError,
            )
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
                                BeneficiaryApplicationState.DialogState.Network
                            } else {
                                null
                            },
                        )
                    }
                }
        }
    }
}

data class BeneficiaryApplicationState(
    val topBarTitle: StringResource = Res.string.add_beneficiary,
    val beneficiaryId: Int =-1,
    val networkUnavailable: Boolean = false,
    val template: BeneficiaryTemplate? = null,
    val beneficiary: Beneficiary? = null,
    val beneficiaryState: BeneficiaryState = BeneficiaryState.CREATE_MANUAL,
    val dialogState: DialogState?,

    val accountTypeError: StringResource? = null,
    val accountNumberError: StringResource? = null,
    val officeNameError: StringResource? = null,
    val transferLimitError: StringResource? = null,
    val beneficiaryNameError: StringResource? = null,
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState

        data object Loading : DialogState

        data object Network : DialogState
    }
}

sealed interface BeneficiaryApplicationEvent {
    data object Navigate : BeneficiaryApplicationEvent
    data class SubmitBeneficiary(val payload: BeneficiaryPayload, val state: BeneficiaryState) : BeneficiaryApplicationEvent
}

sealed interface BeneficiaryApplicationAction {
    data object LoadBeneficiaryTemplate : BeneficiaryApplicationAction
    data class SubmitBeneficiary(val payload: BeneficiaryPayload) : BeneficiaryApplicationAction
    data object OnNavigate : BeneficiaryApplicationAction
    data object OnRetry : BeneficiaryApplicationAction

    data class OnFieldChange(
        val accountType: Int? = null,
        val accountNumber: String? = null,
        val officeName: String? = null,
        val transferLimit: String? = null,
        val beneficiaryName: String? = null,
    ) : BeneficiaryApplicationAction

    sealed interface Internal : BeneficiaryApplicationAction {

        data class ReceiveBeneficiaryResult(val beneficiaryList: DataState<List<Beneficiary>>,
                                       val beneficiaryTemplate: DataState<BeneficiaryTemplate>,) : Internal
    }
}
