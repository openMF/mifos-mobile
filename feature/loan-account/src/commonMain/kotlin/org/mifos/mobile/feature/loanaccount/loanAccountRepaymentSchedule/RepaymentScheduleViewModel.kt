/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.loanAccountRepaymentSchedule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import mifos_mobile.feature.loan_account.generated.resources.Res
import mifos_mobile.feature.loan_account.generated.resources.feature_generic_error_server
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_account_number_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_disbursement_date_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_installments_left_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_installments_paid_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_principal_paid_off_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_total_installments_label
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.entity.TransferSuccessDestination
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.accounts.loan.Periods
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState

internal class RepaymentScheduleViewModel(
    private val loanRepositoryImp: LoanRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<RepaymentScheduleState, RepaymentScheduleEvent, RepaymentScheduleAction>(
    initialState = run {
        val route = savedStateHandle.toRoute<RepaymentScheduleRoute>()
        RepaymentScheduleState(
            accountId = route.accountId,
        )
    },
) {
    init {
        observeNetwork()
    }

    /**
     * Observes the network connectivity status and updates state accordingly.
     */
    private fun observeNetwork() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    sendAction(RepaymentScheduleAction.ReceiveNetworkStatus(isOnline))
                }
        }
    }

    override fun handleAction(action: RepaymentScheduleAction) {
        when (action) {
            RepaymentScheduleAction.OnNavigateBack -> sendEvent(RepaymentScheduleEvent.NavigateBack)

            is RepaymentScheduleAction.ReceiveNetworkStatus -> handleNetworkStatus(action.isOnline)

            is RepaymentScheduleAction.Retry -> retry()

            is RepaymentScheduleAction.Internal.ReceivedRepaymentSchedule ->
                handleRepaymentScheduleResult(action.dataState)

            is RepaymentScheduleAction.OnPayInstallment -> {
                sendEvent(
                    RepaymentScheduleEvent.PayInstallment(
                        action.accountId,
                        action.outStandingBalance,
                        action.transferTyp,
                        action.transferTarget,
                        action.transferSuccessDestination.name,
                    ),
                )
            }
        }
    }

    /**
     * Handles changes in network connectivity.
     *
     * It updates the `networkStatus` state. If the network is offline, it sets the
     * `uiState` to [ScreenUiState.Network]. If the network is online, it
     * automatically triggers a data fetch to refresh the content.
     *
     * @param isOnline A boolean indicating the current network status.
     */
    private fun handleNetworkStatus(isOnline: Boolean) {
        updateState { it.copy(networkStatus = isOnline) }

        viewModelScope.launch {
            if (!isOnline) {
                updateState { current ->
                    if (current.uiState is ScreenUiState.Loading ||
                        current.uiState is ScreenUiState.Error ||
                        current.uiState is ScreenUiState.Empty ||
                        current.uiState is ScreenUiState.Network
                    ) {
                        current.copy(uiState = ScreenUiState.Network)
                    } else {
                        current
                    }
                }
            } else {
                fetchLoanWithAssociations()
            }
        }
    }

    private fun retry() {
        viewModelScope.launch {
            if (!state.networkStatus) {
                updateState { it.copy(uiState = ScreenUiState.Network) }
            } else {
                fetchLoanWithAssociations()
            }
        }
    }

    private fun updateState(update: (RepaymentScheduleState) -> RepaymentScheduleState) {
        mutableStateFlow.update(update)
    }

    private fun fetchLoanWithAssociations() {
        updateState { it.copy(uiState = ScreenUiState.Loading) }
        viewModelScope.launch {
            loanRepositoryImp.getLoanWithAssociations(Constants.REPAYMENT_SCHEDULE, state.accountId)
                .catch { error ->
                    updateState {
                        it.copy(
                            uiState = if (error.cause is IOException) {
                                ScreenUiState.Network
                            } else {
                                ScreenUiState.Error(Res.string.feature_generic_error_server)
                            },
                        )
                    }
                }
                .collect { loanData ->
                    sendAction(RepaymentScheduleAction.Internal.ReceivedRepaymentSchedule(loanData))
                }
        }
    }

    private fun handleRepaymentScheduleResult(dataState: DataState<LoanWithAssociations?>) {
        when (dataState) {
            is DataState.Error -> {
                updateState {
                    it.copy(
                        uiState = if (dataState.exception is IOException) {
                            ScreenUiState.Network
                        } else {
                            ScreenUiState.Error(Res.string.feature_generic_error_server)
                        },
                    )
                }
            }

            DataState.Loading -> updateState {
                it.copy(
                    uiState = ScreenUiState.Loading,
                )
            }

            is DataState.Success -> {
                val result = dataState.data
                println("from success ${ result?.repaymentSchedule?.periods}")
                val currencyCode = result?.currency?.code
                val maxDigits = result?.currency?.decimalPlaces?.toInt()
                val basicDetails = mapOf(
                    Res.string.feature_loan_account_number_label to (result?.accountNo ?: "N/A"),
                    Res.string.feature_loan_disbursement_date_label to (
                        result?.timeline?.actualDisbursementDate?.let {
                            DateHelper.getDateAsString(
                                it,
                            )
                        } ?: "N/A"
                        ),
                    Res.string.feature_loan_principal_paid_off_label to CurrencyFormatter.format(
                        result?.summary?.principalPaid,
                        currencyCode,
                        maxDigits,
                    ),
                    Res.string.feature_loan_installments_paid_label to
                        result?.repaymentSchedule?.periods?.count { it.complete == true }
                            ?.toString(),
                    Res.string.feature_loan_installments_left_label to
                        result?.repaymentSchedule?.periods?.count { it.complete != true }
                            ?.toString(),
                    Res.string.feature_loan_total_installments_label to
                        result?.termFrequency?.toString(),
                )

                updateState {
                    it.copy(
                        basicDetails = basicDetails,
                        loanWithAssociations = result,
                        uiState = ScreenUiState.Success,
                    )
                }
            }
        }
    }
}

internal data class RepaymentScheduleState(
    val accountId: Long? = null,
    val loanWithAssociations: LoanWithAssociations? = null,
    val periods: List<Periods> = emptyList(),
    val basicDetails: Map<StringResource, String?> = emptyMap(),

    val dialogState: DialogState? = null,
    val networkStatus: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {
    sealed interface DialogState {

        data class Error(val message: String) : DialogState
    }

    val getPeriods =
        loanWithAssociations
            ?.repaymentSchedule
            ?.periods
            .orEmpty()
            .filter { it.period != null }
}

sealed interface RepaymentScheduleEvent {
    data object NavigateBack : RepaymentScheduleEvent

    data class PayInstallment(
        val accountId: Long,
        val outStandingBalance: Double?,
        val transferTyp: String,
        val transferTarget: TransferType,
        val transferSuccessDestination: String,
    ) : RepaymentScheduleEvent
}

sealed interface RepaymentScheduleAction {
    data object Retry : RepaymentScheduleAction
    data object OnNavigateBack : RepaymentScheduleAction
    data class ReceiveNetworkStatus(val isOnline: Boolean) : RepaymentScheduleAction

    data class OnPayInstallment(
        val accountId: Long,
        val outStandingBalance: Double?,
        val transferTyp: String,
        val transferTarget: TransferType,
        val transferSuccessDestination: TransferSuccessDestination,
    ) : RepaymentScheduleAction

    sealed interface Internal : RepaymentScheduleAction {
        data class ReceivedRepaymentSchedule(val dataState: DataState<LoanWithAssociations?>) :
            Internal
    }
}
