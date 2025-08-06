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
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.core.ui.utils.BaseViewModel
import kotlin.Int

/**
 * ViewModel for handling the Beneficiary Application logic including form validation,
 * network state observation, and interaction with repository for data operations.
 */
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
            beneficiaryName = route.name,
            accountType = route.accountType,
            accountNumber = route.accountNumber,
            officeName = route.officeName,
            beneficiaryState = enumValueOf<BeneficiaryState>(route.beneficiaryState),
        )
    },
) {

    /**
     * Initializes the ViewModel by observing the network status,
     * setting the top bar title, and loading beneficiary and template data.
     */
    init {
        viewModelScope.launch {
            observeNetworkStatus()
            getTopBarTitle()
        }
    }

    /**
     * Sets the top bar title based on the beneficiary state (Add or Update).
     */
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

    /**
     * Updates the ViewModel state using the provided lambda transformation.
     */
    private fun updateState(update: (BeneficiaryApplicationState) -> BeneficiaryApplicationState) {
        mutableStateFlow.update(update)
    }

    /**
     * Updates only the dialog state in the ViewModel.
     */
    private fun setDialogState(dialogState: BeneficiaryApplicationState.DialogState?) {
        updateState { it.copy(dialogState = dialogState) }
    }

    /**
     * Handles UI actions by triggering appropriate ViewModel logic or events.
     */
    override fun handleAction(action: BeneficiaryApplicationAction) {
        when (action) {
            BeneficiaryApplicationAction.LoadBeneficiaryTemplate -> {
                viewModelScope.launch {
                    loadBeneficiaryAndTemplate()
                }
            }

            is BeneficiaryApplicationAction.SubmitBeneficiary -> {
                requestPayload()
            }

            BeneficiaryApplicationAction.OnNavigate -> sendEvent(BeneficiaryApplicationEvent.Navigate)

            BeneficiaryApplicationAction.OnRetry -> {
                viewModelScope.launch {
                    loadBeneficiaryAndTemplate()
                }
            }

            is BeneficiaryApplicationAction.Internal.ReceiveBeneficiaryResult -> {
                updateStateFromResults(
                    action.beneficiaryList,
                    action.beneficiaryTemplate,
                )
            }

            is BeneficiaryApplicationAction.OnAccountNumberChanged -> {
                updateState {
                    it.copy(
                        accountNumber = action.accountNumber,
                    )
                }
            }

            is BeneficiaryApplicationAction.OnAccountTypeChanged -> {
                updateState {
                    it.copy(
                        accountType = action.accountType,
                    )
                }
            }

            is BeneficiaryApplicationAction.OnBeneficiaryNameChanged -> {
                updateState {
                    it.copy(
                        beneficiaryName = action.beneficiaryName,
                    )
                }
            }

            is BeneficiaryApplicationAction.OnOfficeNameChanged -> {
                updateState {
                    it.copy(
                        officeName = action.officeName,
                    )
                }
            }

            is BeneficiaryApplicationAction.OnTransferLimitChanged -> {
                updateState {
                    it.copy(
                        transferLimit = action.transferLimit,
                    )
                }
            }

            BeneficiaryApplicationAction.NavigateToQR -> {
                sendEvent(BeneficiaryApplicationEvent.NavigateToQR)
            }
        }
    }

    /**
     * Loads both the beneficiary list and template from the repository.
     */
    private fun loadBeneficiaryAndTemplate() {
        setDialogState(BeneficiaryApplicationState.DialogState.Loading)
        combine(
            beneficiaryRepositoryImp.beneficiaryList(),
            beneficiaryRepositoryImp.beneficiaryTemplate(),
        ) { beneficiaryList, beneficiaryTemplate ->
            sendAction(
                BeneficiaryApplicationAction.Internal.ReceiveBeneficiaryResult(
                    beneficiaryList,
                    beneficiaryTemplate,
                ),
            )
        }.catch { error ->
            setDialogState(
                BeneficiaryApplicationState.DialogState.Error(
                    error.message ?: "An error occurred",
                ),
            )
        }.launchIn(viewModelScope)
    }

    /**
     * Updates the ViewModel state based on the results from the beneficiary list and template APIs.
     */
    private fun updateStateFromResults(
        beneficiaryList: DataState<List<Beneficiary>>,
        beneficiaryTemplate: DataState<BeneficiaryTemplate>,
    ) {
        when {
            beneficiaryList is DataState.Loading || beneficiaryTemplate is DataState.Loading -> {
                setDialogState(BeneficiaryApplicationState.DialogState.Loading)
            }
            beneficiaryList is DataState.Error || beneficiaryTemplate is DataState.Error -> {
                val error = (beneficiaryList as? DataState.Error)?.exception?.message
                    ?: (beneficiaryTemplate as? DataState.Error)?.exception?.message
                    ?: "An error occurred"
                setDialogState(BeneficiaryApplicationState.DialogState.Error(error))
            }
            beneficiaryList is DataState.Success && beneficiaryTemplate is DataState.Success -> {
                val beneficiary = beneficiaryList.data.find { it.id == state.beneficiaryId }
                updateState { currentState ->
                    currentState.copy(
                        dialogState = null,
                        template = beneficiaryTemplate.data,
                    )
                }
                if (beneficiary != null) {
                    updateState {
                        it.copy(
                            beneficiary = beneficiary,
                            accountType = beneficiary.accountType?.id ?: -1,
                            accountNumber = beneficiary.accountNumber ?: "",
                            officeName = beneficiary.officeName ?: "",
                            transferLimit = (beneficiary.transferLimit ?: 0).toLong().toString(),
                            beneficiaryName = beneficiary.name ?: "",
                        )
                    }
                }
            }
        }
    }

    /**
     * Validates form fields and submits the payload if validation passes.
     */
    private fun requestPayload() {
        if (validateFields()) {
            viewModelScope.launch {
                sendEvent(
                    BeneficiaryApplicationEvent.SubmitBeneficiary(
                        beneficiaryId = state.beneficiaryId,
                        beneficiaryState = state.beneficiaryState.name,
                        name = state.beneficiaryName.trim(),
                        officeName = state.officeName.trim(),
                        accountType = state.accountType,
                        accountNumber = state.accountNumber.trim(),
                        transferLimit = state.transferLimit.toDouble().toInt(),
                    ),
                )
            }
        }
    }

    /**
     * Validates the input fields for the beneficiary form and updates the error states.
     * Returns true if validation passes; false otherwise.
     */
    private fun validateFields(): Boolean {
        var hasError = false

        val updatedState = state.copy(
            accountTypeError = if (state.beneficiaryState != BeneficiaryState.UPDATE && state.accountType == -1) {
                hasError = true
                Res.string.select_account_type
            } else {
                null
            },

            accountNumberError = if (
                state.beneficiaryState != BeneficiaryState.UPDATE && state.accountNumber.trim().isEmpty()
            ) {
                hasError = true
                Res.string.enter_account_number
            } else {
                null
            },

            officeNameError = if (state.beneficiaryState != BeneficiaryState.UPDATE && state.officeName.trim().isEmpty()
            ) {
                hasError = true
                Res.string.enter_office_name
            } else {
                null
            },

            transferLimitError = when {
                state.transferLimit.isEmpty() -> {
                    hasError = true
                    Res.string.enter_transfer_limit
                }
                state.transferLimit.toDoubleOrNull() == null -> {
                    hasError = true
                    Res.string.invalid_amount
                }
                else -> null
            },

            beneficiaryNameError = if (state.beneficiaryName.trim().isEmpty()) {
                hasError = true
                Res.string.enter_beneficiary_name
            } else {
                null
            },
        )

        updateState { updatedState }

        return !hasError
    }

    /**
     * Observes the network status and updates the ViewModel state accordingly.
     */
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
                    if (!isOffline) {
                        loadBeneficiaryAndTemplate()
                    }
                }
        }
    }
}

data class BeneficiaryApplicationState(
    val topBarTitle: StringResource = Res.string.add_beneficiary,
    val beneficiaryId: Long = -1L,
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

    val accountType: Int = -1,
    val accountNumber: String = "",
    val officeName: String = "",
    val transferLimit: String = "",
    val beneficiaryName: String = "",
) {
    sealed interface DialogState {
        data class Error(val message: String) : DialogState

        data object Loading : DialogState

        data object Network : DialogState
    }
    val isEnabled = accountType != -1 &&
        accountNumber.isNotEmpty() &&
        officeName.isNotEmpty() &&
        transferLimit.isNotEmpty() &&
        beneficiaryName.isNotEmpty()
}

sealed interface BeneficiaryApplicationEvent {
    data object Navigate : BeneficiaryApplicationEvent
    data class SubmitBeneficiary(
        val beneficiaryId: Long,
        val beneficiaryState: String,
        val name: String,
        val officeName: String,
        val accountType: Int,
        val accountNumber: String,
        val transferLimit: Int,
    ) : BeneficiaryApplicationEvent
    data object NavigateToQR : BeneficiaryApplicationEvent
}

sealed interface BeneficiaryApplicationAction {
    data object LoadBeneficiaryTemplate : BeneficiaryApplicationAction
    data object SubmitBeneficiary : BeneficiaryApplicationAction
    data object OnNavigate : BeneficiaryApplicationAction
    data object OnRetry : BeneficiaryApplicationAction
    data object NavigateToQR : BeneficiaryApplicationAction

    data class OnAccountTypeChanged(val accountType: Int) : BeneficiaryApplicationAction
    data class OnAccountNumberChanged(val accountNumber: String) : BeneficiaryApplicationAction
    data class OnOfficeNameChanged(val officeName: String) : BeneficiaryApplicationAction
    data class OnTransferLimitChanged(val transferLimit: String) : BeneficiaryApplicationAction
    data class OnBeneficiaryNameChanged(val beneficiaryName: String) : BeneficiaryApplicationAction

    sealed interface Internal : BeneficiaryApplicationAction {

        data class ReceiveBeneficiaryResult(
            val beneficiaryList: DataState<List<Beneficiary>>,
            val beneficiaryTemplate: DataState<BeneficiaryTemplate>,
        ) : Internal
    }
}
