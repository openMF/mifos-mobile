/*
 * Copyright 2026 Mifos Initiative
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
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_details_not_available
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_disbursement_date_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_installments_left_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_installments_paid_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_principal_paid_off_label
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_total_installments_label
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
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
import org.mifos.mobile.feature.loanaccount.loanAccountRepaymentSchedule.RepaymentScheduleState.RepaymentScheduleTableData

/**
 * ViewModel for the repayment schedule screen.
 * It is responsible for fetching and displaying the repayment schedule for a loan account.
 *
 * @param loanRepositoryImp The repository for fetching loan data.
 * @param networkMonitor The network monitor to observe network connectivity.
 * @param savedStateHandle The saved state handle for the view model.
 */
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

            RepaymentScheduleAction.ExportToPdf -> {
                sendEvent(RepaymentScheduleEvent.ExportPdf)
            }

            is RepaymentScheduleAction.PdfExportError -> {
                updateState {
                    it.copy(
                        dialogState = RepaymentScheduleState.DialogState.Error(
                            action.title,
                            action.message,
                        ),
                    )
                }
            }

            RepaymentScheduleAction.DismissErrorDialog -> {
                updateState {
                    it.copy(dialogState = null)
                }
            }

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

    private fun mapPeriodsData(
        periods: List<Periods>,
        currencyCode: String?,
        maxDigits: Int?,
    ): List<RepaymentScheduleTableData.PeriodData> {
        return periods.map { period ->
            RepaymentScheduleTableData.PeriodData(
                number = period.period?.toString() ?: "",
                days = period.daysInPeriod?.toString() ?: "-",
                dueDate = DateHelper.getDateAsString(period.dueDate),
                paidDate = if (period.obligationsMetOnDate.isNotEmpty()) {
                    DateHelper.getDateAsString(period.obligationsMetOnDate)
                } else {
                    "-"
                },
                isPaid = period.complete == true,
                balanceOfLoan = CurrencyFormatter.format(
                    period.principalLoanBalanceOutstanding,
                    currencyCode,
                    maxDigits,
                ),
                principalDue = CurrencyFormatter.format(
                    period.principalDue,
                    currencyCode,
                    maxDigits,
                ),
                interest = CurrencyFormatter.format(period.interestDue, currencyCode, maxDigits),
                fees = CurrencyFormatter.format(
                    period.feeChargesDue ?: 0.0,
                    currencyCode,
                    maxDigits,
                ),
                penalties = CurrencyFormatter.format(
                    period.penaltyChargesDue ?: 0.0,
                    currencyCode,
                    maxDigits,
                ),
                due = CurrencyFormatter.format(period.totalDueForPeriod, currencyCode, maxDigits),
                paid = CurrencyFormatter.format(
                    period.totalPaidForPeriod ?: 0.0,
                    currencyCode,
                    maxDigits,
                ),
                inAdvance = CurrencyFormatter.format(
                    period.totalPaidInAdvanceForPeriod ?: 0.0,
                    currencyCode,
                    maxDigits,
                ),
                late = CurrencyFormatter.format(
                    period.totalPaidLateForPeriod ?: 0.0,
                    currencyCode,
                    maxDigits,
                ),
                outstanding = CurrencyFormatter.format(
                    period.totalOutstandingForPeriod ?: 0.0,
                    currencyCode,
                    maxDigits,
                ),
            )
        }
    }

    private fun calculateTotals(
        periods: List<Periods>,
        currencyCode: String?,
        maxDigits: Int?,
    ): RepaymentScheduleTableData.TotalsData {
        return RepaymentScheduleTableData.TotalsData(
            principalDue = CurrencyFormatter.format(
                periods.sumOf {
                    it.principalDue ?: 0.0
                },
                currencyCode,
                maxDigits,
            ),
            interest = CurrencyFormatter.format(
                periods.sumOf { it.interestDue ?: 0.0 },
                currencyCode,
                maxDigits,
            ),
            fees = CurrencyFormatter.format(
                periods.sumOf { it.feeChargesDue ?: 0.0 },
                currencyCode,
                maxDigits,
            ),
            penalties = CurrencyFormatter.format(
                periods.sumOf { it.penaltyChargesDue ?: 0.0 },
                currencyCode,
                maxDigits,
            ),
            due = CurrencyFormatter.format(
                periods.sumOf { it.totalDueForPeriod ?: 0.0 },
                currencyCode,
                maxDigits,
            ),
            paid = CurrencyFormatter.format(
                periods.sumOf { it.totalPaidForPeriod ?: 0.0 },
                currencyCode,
                maxDigits,
            ),
            inAdvance = CurrencyFormatter.format(
                periods.sumOf {
                    it.totalPaidInAdvanceForPeriod ?: 0.0
                },
                currencyCode,
                maxDigits,
            ),
            late = CurrencyFormatter.format(
                periods.sumOf { it.totalPaidLateForPeriod ?: 0.0 },
                currencyCode,
                maxDigits,
            ),
            outstanding = CurrencyFormatter.format(
                periods.sumOf {
                    it.totalOutstandingForPeriod ?: 0.0
                },
                currencyCode,
                maxDigits,
            ),
        )
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

            DataState.Loading -> updateState { it.copy(uiState = ScreenUiState.Loading) }
            is DataState.Success -> {
                viewModelScope.launch {
                    val result = dataState.data
                    val currencyCode = result?.currency?.code
                    val maxDigits = result?.currency?.decimalPlaces?.toInt()

                    val periods =
                        result?.repaymentSchedule?.periods?.filter { it.period != null }
                            ?: emptyList()

                    val totalsData = calculateTotals(periods, currencyCode, maxDigits)

                    val periodsData = mapPeriodsData(periods, currencyCode, maxDigits)
                    val notAvailableText = getString(Res.string.feature_loan_details_not_available)
                    val tableData = result?.let { loan ->
                        RepaymentScheduleTableData(
                            accountNo = loan.accountNo ?: notAvailableText,
                            clientName = loan.clientName ?: notAvailableText,
                            productName = loan.loanProductName ?: notAvailableText,
                            disbursementDate = loan.timeline?.actualDisbursementDate?.let { date ->
                                DateHelper.getDateAsString(date)
                            } ?: notAvailableText,
                            loanAmount = CurrencyFormatter.format(
                                loan.summary?.principalDisbursed,
                                currencyCode,
                                maxDigits,
                            ),
                            principalPaid = CurrencyFormatter.format(
                                loan.summary?.principalPaid,
                                currencyCode,
                                maxDigits,
                            ),
                            installmentsPaid = periods.count { it.complete == true }
                                .toString(),
                            installmentsLeft = periods.count { it.complete != true }
                                .toString(),
                            totalInstallments = loan.termFrequency?.toString() ?: "0",
                            currencyCode = currencyCode,
                            periods = periodsData,
                            totals = totalsData,
                        )
                    }

                    updateState {
                        it.copy(
                            repaymentScheduleTableData = tableData,
                            basicDetails = mapOf(
                                Res.string.feature_loan_account_number_label to tableData?.accountNo,
                                Res.string.feature_loan_disbursement_date_label to tableData?.disbursementDate,
                                Res.string.feature_loan_principal_paid_off_label to tableData?.principalPaid,
                                Res.string.feature_loan_installments_paid_label to tableData?.installmentsPaid,
                                Res.string.feature_loan_installments_left_label to tableData?.installmentsLeft,
                                Res.string.feature_loan_total_installments_label to tableData?.totalInstallments,
                            ),
                            uiState = ScreenUiState.Success,
                        )
                    }
                }
            }
        }
    }
}

/**
 * Represents the state of the repayment schedule screen.
 *
 * @property accountId The ID of the loan account.
 * @property basicDetails A map of basic details about the loan.
 * @property repaymentScheduleTableData The repayment schedule data.
 * @property dialogState The state of the dialog to display.
 * @property networkStatus The network connectivity status.
 * @property uiState The overall state of the screen.
 */
internal data class RepaymentScheduleState(
    val accountId: Long? = null,
    val basicDetails: Map<StringResource, String?> = emptyMap(),
    val repaymentScheduleTableData: RepaymentScheduleTableData? = null,
    val dialogState: DialogState? = null,
    val networkStatus: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {
    /**
     * Represents the possible dialog states.
     */
    sealed interface DialogState {

        /**
         * An error dialog with a message.
         *
         * @param title The error title to display.
         * @param message The error message to display.
         */
        data class Error(val title: String, val message: String) : DialogState
    }

    data class RepaymentScheduleTableData(
        val accountNo: String,
        val clientName: String,
        val productName: String,
        val disbursementDate: String,
        val loanAmount: String,
        val principalPaid: String,
        val installmentsPaid: String,
        val installmentsLeft: String,
        val totalInstallments: String,
        val currencyCode: String?,
        val periods: List<PeriodData>,
        val totals: TotalsData,
    ) {
        data class PeriodData(
            val number: String,
            val days: String,
            val dueDate: String,
            val paidDate: String,
            val isPaid: Boolean,
            val balanceOfLoan: String,
            val principalDue: String,
            val interest: String,
            val fees: String,
            val penalties: String,
            val due: String,
            val paid: String,
            val inAdvance: String,
            val late: String,
            val outstanding: String,
        )

        data class TotalsData(
            val principalDue: String,
            val interest: String,
            val fees: String,
            val penalties: String,
            val due: String,
            val paid: String,
            val inAdvance: String,
            val late: String,
            val outstanding: String,
        )
    }
}

/**
 * Represents the one-time events that can be sent from the ViewModel to the UI.
 */
sealed interface RepaymentScheduleEvent {
    /**
     * Event to navigate back to the previous screen.
     */
    data object NavigateBack : RepaymentScheduleEvent

    /**
     * Event to export the repayment schedule to PDF.
     */
    data object ExportPdf : RepaymentScheduleEvent

    /**
     * Event to pay an installment.
     *
     * @property accountId The ID of the loan account.
     * @property outStandingBalance The outstanding balance of the installment.
     * @property transferTyp The type of transfer.
     * @property transferTarget The target of the transfer.
     * @property transferSuccessDestination The destination to navigate to after a successful transfer.
     */
    data class PayInstallment(
        val accountId: Long,
        val outStandingBalance: Double?,
        val transferTyp: String,
        val transferTarget: TransferType,
        val transferSuccessDestination: String,
    ) : RepaymentScheduleEvent
}

/**
 * Represents the actions that can be taken on the repayment schedule screen.
 */
sealed interface RepaymentScheduleAction {
    /**
     * Action to retry a failed operation.
     */
    data object Retry : RepaymentScheduleAction

    /**
     * Action to navigate back from the screen.
     */
    data object OnNavigateBack : RepaymentScheduleAction

    /**
     * Action to export the repayment schedule to PDF.
     */
    data object ExportToPdf : RepaymentScheduleAction

    /**
     * Action to handle PDF export error.
     *
     * @param title The error title to display.
     * @param message The error message to display.
     */
    data class PdfExportError(val title: String, val message: String) : RepaymentScheduleAction

    /**
     * Action to dismiss the error dialog.
     */
    data object DismissErrorDialog : RepaymentScheduleAction

    /**
     * Action to receive the network status.
     *
     * @param isOnline Whether the device is online.
     */
    data class ReceiveNetworkStatus(val isOnline: Boolean) : RepaymentScheduleAction

    /**
     * Action to pay an installment.
     *
     * @property accountId The ID of the loan account.
     * @property outStandingBalance The outstanding balance of the installment.
     * @property transferTyp The type of transfer.
     * @property transferTarget The target of the transfer.
     * @property transferSuccessDestination The destination to navigate to after a successful transfer.
     */
    data class OnPayInstallment(
        val accountId: Long,
        val outStandingBalance: Double?,
        val transferTyp: String,
        val transferTarget: TransferType,
        val transferSuccessDestination: TransferSuccessDestination,
    ) : RepaymentScheduleAction

    /**
     * Internal-only actions triggered by repository/data flow.
     */
    sealed interface Internal : RepaymentScheduleAction {
        /**
         * Action to receive the repayment schedule from the repository.
         *
         * @param dataState The result of the data fetch.
         */
        data class ReceivedRepaymentSchedule(val dataState: DataState<LoanWithAssociations?>) :
            Internal
    }
}
