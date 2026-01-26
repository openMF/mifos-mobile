/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.loanAccountSummary

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import mifos_mobile.feature.loan_account.generated.resources.Res
import mifos_mobile.feature.loan_account.generated.resources.feature_generic_error_server
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_account_number_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_account_status_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_auto_debit_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_currency_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_expected_payoff_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_fees_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_fees_waived_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_interest_paid_off_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_interest_payoff_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_interest_rate_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_interest_waived_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_linked_account_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_months_left_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_next_payment_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_penalties_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_penalty_waived_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_principal_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_principal_paid_off_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_regular_payment_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_scheme_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_total_outstanding_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_type_label
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.core.ui.utils.ScreenUiState.Network
import org.mifos.mobile.feature.loanaccount.loanAccountDetails.LabelValueItem

/**
 * ViewModel for the loan account summary screen.
 * It is responsible for fetching and displaying the summary of a loan account.
 *
 * @param loanAccountRepositoryImp The repository for fetching loan account data.
 * @param networkMonitor The network monitor to observe network connectivity.
 * @param savedStateHandle The saved state handle for the view model.
 */
internal class LoanAccountSummaryViewModel(
    private val loanAccountRepositoryImp: LoanRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<LoanAccountSummaryState, LoanAccountSummaryEvent, LoanAccountSummaryAction>(
    initialState = run {
        val route = savedStateHandle.toRoute<AccountSummaryRoute>()
        LoanAccountSummaryState(
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
                    sendAction(LoanAccountSummaryAction.ReceiveNetworkStatus(isOnline))
                }
        }
    }

    private fun fetchLoanSummary() {
        viewModelScope.launch {
            loanAccountRepositoryImp.getLoanWithAssociations(
                Constants.REPAYMENT_SCHEDULE,
                state.accountId,
            ).collect { result ->
                sendAction(LoanAccountSummaryAction.Internal.LoanSummaryReceived(result))
            }
        }
    }

    /**
     * Handles incoming UI actions.
     */
    override fun handleAction(action: LoanAccountSummaryAction) {
        when (action) {
            LoanAccountSummaryAction.OnNavigateBack -> sendEvent(LoanAccountSummaryEvent.NavigateBack)

            is LoanAccountSummaryAction.ReceiveNetworkStatus -> handleNetworkStatus(action.isOnline)

            LoanAccountSummaryAction.Retry -> retry()

            is LoanAccountSummaryAction.Internal.LoanSummaryReceived ->
                handleLoanAccountSummaryResult(action.dataState)

            LoanAccountSummaryAction.DismissDialog -> handleDismissDialog()
        }
    }

    private fun updateState(update: (LoanAccountSummaryState) -> LoanAccountSummaryState) {
        mutableStateFlow.update(update)
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
                fetchLoanSummary()
            }
        }
    }

    private fun retry() {
        viewModelScope.launch {
            if (!state.networkStatus) {
                updateState { it.copy(uiState = ScreenUiState.Network) }
            } else {
                fetchLoanSummary()
            }
        }
    }

    /**
     * Handles the dismissal of dialog state.
     */
    private fun handleDismissDialog() {
        mutableStateFlow.update { it.copy(dialogState = null) }
    }

    /**
     * Processes the loan account result from the repository.
     */
    private fun handleLoanAccountSummaryResult(dataState: DataState<LoanWithAssociations?>) {
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

            DataState.Loading -> {
                mutableStateFlow.update {
                    it.copy(uiState = ScreenUiState.Loading)
                }
            }

            is DataState.Success -> {
                extractDetails(dataState.data)
            }
        }
    }

    /**
     * Extracts relevant loan account details and updates the UI state.
     */
    private fun extractDetails(loan: LoanWithAssociations?) {
        val currencyCode = loan?.currency?.code
        val maxDigits = loan?.currency?.decimalPlaces?.toInt()

        val accountDetails = listOf(
            LabelValueItem(Res.string.feature_loan_account_number_label, loan?.accountNo ?: "N/A"),
            LabelValueItem(Res.string.feature_loan_type_label, loan?.loanType?.value ?: "N/A"),
            LabelValueItem(Res.string.feature_loan_scheme_label, loan?.loanProductName ?: "N/A"),
            LabelValueItem(
                Res.string.feature_loan_account_status_label,
                loan?.status?.value ?: "N/A",
            ),
        )

        val payOffDetails = listOf(
            LabelValueItem(
                Res.string.feature_loan_expected_payoff_label,
                CurrencyFormatter.format(
                    loan?.summary?.totalExpectedRepayment ?: 0.0,
                    currencyCode,
                    maxDigits,
                ),
            ),
            LabelValueItem(
                Res.string.feature_loan_interest_payoff_label,
                CurrencyFormatter.format(
                    loan?.summary?.interestCharged ?: 0.0,
                    currencyCode,
                    maxDigits,
                ),
            ),
            LabelValueItem(
                Res.string.feature_loan_principal_label,
                CurrencyFormatter.format(
                    loan?.summary?.principalDisbursed ?: 0.0,
                    currencyCode,
                    maxDigits,
                ),
            ),
            LabelValueItem(
                Res.string.feature_loan_currency_label,
                loan?.currency?.displaySymbol ?: "N/A",
            ),
            LabelValueItem(
                Res.string.feature_loan_interest_rate_label,
                loan?.interestRatePerPeriod?.let { "$it%" } ?: "N/A",
            ),
        )

        val chargeDetails = listOf(
            LabelValueItem(
                Res.string.feature_loan_fees_label,
                CurrencyFormatter.format(
                    loan?.summary?.feeChargesCharged ?: 0.0,
                    currencyCode,
                    maxDigits,
                ),
            ),
            LabelValueItem(
                Res.string.feature_loan_penalties_label,
                CurrencyFormatter.format(
                    loan?.summary?.penaltyChargesCharged ?: 0.0,
                    currencyCode,
                    maxDigits,
                ),
            ),
        )

        val waiversDetails = listOf(
            LabelValueItem(
                Res.string.feature_loan_interest_waived_label,
                CurrencyFormatter.format(
                    loan?.summary?.interestWaived ?: 0.0,
                    currencyCode,
                    maxDigits,
                ),
            ),
            LabelValueItem(
                Res.string.feature_loan_penalty_waived_label,
                CurrencyFormatter.format(
                    loan?.summary?.penaltyChargesWaived ?: 0.0,
                    currencyCode,
                    maxDigits,
                ),
            ),
            LabelValueItem(
                Res.string.feature_loan_fees_waived_label,
                CurrencyFormatter.format(
                    loan?.summary?.feeChargesWaived ?: 0.0,
                    currencyCode,
                    maxDigits,
                ),
            ),
        )

        val paidOffDetails = listOf(
            LabelValueItem(
                Res.string.feature_loan_interest_paid_off_label,
                CurrencyFormatter.format(
                    loan?.summary?.interestPaid ?: 0.0,
                    currencyCode,
                    maxDigits,
                ),
            ),
            LabelValueItem(
                Res.string.feature_loan_principal_paid_off_label,
                CurrencyFormatter.format(
                    loan?.summary?.principalPaid ?: 0.0,
                    currencyCode,
                    maxDigits,
                ),
            ),
        )

        val outStandingDetails = listOf(
//            TODO not found in model
//            LabelValueItem(
//                Res.string.feature_loan_interest_outstanding_label,
//                CurrencyFormatter.format(
//                    loan?.summary?.interestOutstanding,
//                    currencyCode,
//                    maxDigits,
//                ),
//            ),
//            LabelValueItem(
//                Res.string.feature_loan_principal_outstanding_label,
//                CurrencyFormatter.format(
//                    loan?.summary?.principalOutstanding,
//                    currencyCode,
//                    maxDigits,
//                ),
//            ),
            LabelValueItem(
                Res.string.feature_loan_total_outstanding_label,
                CurrencyFormatter.format(
                    loan?.summary?.totalOutstanding ?: 0.0,
                    currencyCode,
                    maxDigits,
                ),
            ),
        )

        val firstPeriod = loan?.repaymentSchedule?.periods?.firstOrNull()

        val unpaidPeriodsCount = loan?.repaymentSchedule
            ?.periods
            ?.count { it.complete == false } ?: 0

        val installmentDetails = listOf(
            LabelValueItem(
                Res.string.feature_loan_regular_payment_label,
                CurrencyFormatter.format(
                    firstPeriod?.totalDueForPeriod,
                    currencyCode,
                    maxDigits,
                ),
            ),
            LabelValueItem(
                Res.string.feature_loan_next_payment_label,
                firstPeriod?.dueDate?.let { DateHelper.getDateAsString(it) } ?: "N/A",
            ),
            LabelValueItem(
                Res.string.feature_loan_months_left_label,
                "$unpaidPeriodsCount/${loan?.termFrequency ?: 0}",
            ),
            LabelValueItem(
                Res.string.feature_loan_auto_debit_label,
                if (loan?.npa == true) "Off" else "On",
            ),
//            TODO Not found in the api
            LabelValueItem(
                Res.string.feature_loan_linked_account_label,
//                loan?.linkedAccount?.displayName ?: "N/A",
                "N/A",
            ),
        )
        mutableStateFlow.update {
            it.copy(
                accountDetails = accountDetails,
                payOffDetails = payOffDetails,
                chargeDetails = chargeDetails,
                waiversDetails = waiversDetails,
                paidOffDetails = paidOffDetails,
                outStandingDetails = outStandingDetails,
                installmentDetails = installmentDetails,
                uiState = ScreenUiState.Success,
            )
        }
    }
}

/**
 * Represents the state of the loan account summary screen.
 *
 * @property accountId The ID of the loan account.
 * @property accountDetails A list of label-value pairs for the account details.
 * @property payOffDetails A list of label-value pairs for the pay-off details.
 * @property chargeDetails A list of label-value pairs for the charge details.
 * @property waiversDetails A list of label-value pairs for the waivers details.
 * @property paidOffDetails A list of label-value pairs for the paid-off details.
 * @property outStandingDetails A list of label-value pairs for the outstanding details.
 * @property installmentDetails A list of label-value pairs for the installment details.
 * @property dialogState The state of the dialog to display.
 * @property networkStatus The network connectivity status.
 * @property uiState The overall state of the screen.
 */
@Immutable
internal data class LoanAccountSummaryState(
    val accountId: Long,
    val accountDetails: List<LabelValueItem>? = emptyList(),
    val payOffDetails: List<LabelValueItem>? = emptyList(),
    val chargeDetails: List<LabelValueItem>? = emptyList(),
    val waiversDetails: List<LabelValueItem>? = emptyList(),
    val paidOffDetails: List<LabelValueItem>? = emptyList(),
    val outStandingDetails: List<LabelValueItem>? = emptyList(),
    val installmentDetails: List<LabelValueItem>? = emptyList(),

    val dialogState: DialogState? = null,
    val networkStatus: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {
    /**
     * Represents UI dialog states.
     */
    sealed interface DialogState {
        /**
         * An error dialog with a message.
         *
         * @param message The error message to display.
         */
        data class Error(val message: String) : DialogState
    }
}

/**
 * Represents the one-time events that can be sent from the ViewModel to the UI.
 */
sealed interface LoanAccountSummaryEvent {
    /**
     * Event to trigger navigation back to the previous screen.
     */
    data object NavigateBack : LoanAccountSummaryEvent
}

/**
 * Represents the actions that can be taken on the loan account summary screen.
 */
sealed interface LoanAccountSummaryAction {
    /**
     * Action to navigate back from the screen.
     */
    data object OnNavigateBack : LoanAccountSummaryAction

    /**
     * Action to retry a failed operation.
     */
    data object Retry : LoanAccountSummaryAction

    /**
     * Action to dismiss any open dialog.
     */
    data object DismissDialog : LoanAccountSummaryAction

    /**
     * Action to receive the network status.
     *
     * @param isOnline Whether the device is online.
     */
    data class ReceiveNetworkStatus(val isOnline: Boolean) : LoanAccountSummaryAction

    /**
     * Internal-only actions triggered by repository/data flow.
     */
    sealed interface Internal : LoanAccountSummaryAction {
        /**
         * Action to receive the loan summary from the repository.
         *
         * @param dataState The result of the data fetch.
         */
        data class LoanSummaryReceived(val dataState: DataState<LoanWithAssociations?>) : Internal
    }
}
