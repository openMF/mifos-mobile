/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount.savingsAccountDetails

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.ktor.client.utils.EmptyContent.status
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import mifos_mobile.feature.savings_account.generated.resources.Res
import mifos_mobile.feature.savings_account.generated.resources.feature_generic_error_server
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_account_number_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_available_balance_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_nominal_interest_rate_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_status_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_total_deposits_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_total_withdrawals_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_transaction_amount_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_transaction_date_label
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.SavingStatus
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.templates.account.AccountType
import org.mifos.mobile.core.qr.getAccountDetailsInString
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.core.ui.utils.ScreenUiState.Network
import org.mifos.mobile.feature.savingsaccount.components.SavingsActionItems
import org.mifos.mobile.feature.savingsaccount.components.savingsAccountActions
/**
 * ViewModel for managing the state and logic of the Savings Account Details screen.
 *
 * @param savingsAccountRepositoryImp Repository for fetching savings account data.
 * @param savedStateHandle Used to retrieve route arguments such as accountId.
 */
internal class SavingsAccountDetailsViewModel(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    savedStateHandle: SavedStateHandle,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val networkMonitor: NetworkMonitor,
) : BaseViewModel<SavingsAccountDetailsState, SavingsAccountDetailsEvent, SavingsAccountDetailsAction>(
    initialState = run {
        val accountId = savedStateHandle.toRoute<SavingsAccountDetailsRoute>().accountId
        SavingsAccountDetailsState(
            accountId = accountId,
            items = savingsAccountActions,
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
                    sendAction(SavingsAccountDetailsAction.ReceiveNetworkStatus(isOnline))
                }
        }
    }

    private fun fetchSavingAccount() {
        viewModelScope.launch {
            savingsAccountRepositoryImp.getSavingsWithAssociations(
                state.accountId,
                Constants.TRANSACTIONS,
            ).collect { result ->
                sendAction(SavingsAccountDetailsAction.Internal.SavingsResultReceived(result))
            }
        }
    }

    /**
     * Handles incoming UI actions.
     */
    override fun handleAction(action: SavingsAccountDetailsAction) {
        when (action) {
            SavingsAccountDetailsAction.OnNavigateBack -> sendEvent(SavingsAccountDetailsEvent.NavigateBack)

            SavingsAccountDetailsAction.OnRetry -> retry()

            is SavingsAccountDetailsAction.OnNavigateToAction ->
                sendEvent(
                    SavingsAccountDetailsEvent.NavigateToAction(
                        action.route,
                    ),
                )
            is SavingsAccountDetailsAction.ReceiveNetworkStatus -> handleNetworkStatus(action.isOnline)

            is SavingsAccountDetailsAction.Internal.SavingsResultReceived ->
                handleSavingsAccountResult(action.dataState)

            SavingsAccountDetailsAction.DismissDialog -> handleDismissDialog()

            SavingsAccountDetailsAction.OnUpdateAccount -> sendEvent(
                SavingsAccountDetailsEvent.UpdateAccount,
            )
        }
    }

    /**
     * A helper function to update the mutable state flow.
     *
     * @param update A lambda function that takes the current state and returns a new state.
     */
    private fun updateState(update: (SavingsAccountDetailsState) -> SavingsAccountDetailsState) {
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
                fetchSavingAccount()
            }
        }
    }

    private fun retry() {
        viewModelScope.launch {
            if (!state.networkStatus) {
                updateState { it.copy(uiState = ScreenUiState.Network) }
            } else {
                fetchSavingAccount()
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
     * Processes the savings account result from the repository.
     */
    private fun handleSavingsAccountResult(dataState: DataState<SavingsWithAssociations>) {
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
                updateState {
                    it.copy(uiState = ScreenUiState.Loading)
                }
            }

            is DataState.Success -> {
                val savings = dataState.data
                extractDetails(savings)
            }
        }
    }

    fun getQrString(): String {
        val officeName = userPreferencesRepository.userInfo.value.officeName
        return if (officeName.isNotEmpty()) {
            return getAccountDetailsInString(
                clientName = state.clientName.toString(),
                accountNumber = state.accountNumber.toString(),
                accountType = AccountType(
                    id = 2,
                    code = "accountType.savings",
                    value = "Savings Account",
                ),
                officeName = officeName,
            )
        } else {
            ""
        }
    }

    private fun extractDetails(savings: SavingsWithAssociations) {
        val isActive = savings.status?.value == SavingStatus.ACTIVE.status
        val isUpdate = savings.status?.value == SavingStatus.SUBMIT_AND_PENDING_APPROVAL.status

        val currencyCode = savings.currency?.code
        val decimalPlaces = savings.currency?.decimalPlaces

        val displayItems = listOf(
            LabelValueItem(Res.string.feature_savings_account_number_label, savings.accountNo ?: "N/A"),
            LabelValueItem(
                Res.string.feature_savings_available_balance_label,
                CurrencyFormatter.format(savings.summary?.accountBalance, currencyCode, decimalPlaces),
            ),
            LabelValueItem(Res.string.feature_savings_status_label, savings.status?.value ?: "N/A"),
            LabelValueItem(
                Res.string.feature_savings_nominal_interest_rate_label,
                "${savings.getNominalAnnualInterestRate()} %",
            ),
            LabelValueItem(
                Res.string.feature_savings_total_deposits_label,
                savings.summary?.totalDeposits?.let {
                    CurrencyFormatter.format(it, currencyCode, decimalPlaces)
                } ?: "N/A",
            ),
            LabelValueItem(
                Res.string.feature_savings_total_withdrawals_label,
                savings.summary?.totalWithdrawals?.let {
                    CurrencyFormatter.format(it, currencyCode, decimalPlaces)
                } ?: "N/A",
            ),
        )

        val transactions = savings.transactions.firstOrNull()?.let { txn ->
            listOf(
                LabelValueItem(
                    Res.string.feature_savings_transaction_amount_label,
                    txn.amount.toString(),
                ),
                LabelValueItem(
                    Res.string.feature_savings_transaction_date_label,
                    DateHelper.getDateAsString(txn.submittedOnDate ?: emptyList()),
                ),
            )
        } ?: emptyList()

        updateState {
            it.copy(
                savingStatus = SavingStatus.fromStatus(savings.status?.value ?: ""),
                isActive = isActive,
                isUpdatable = isUpdate,
                accountId = savings.id ?: -1L,
                accountNumber = savings.accountNo,
                clientName = savings.clientName,
                product = savings.savingsProductName,
                submissionDate = DateHelper.getDateAsString(savings.timeline?.submittedOnDate ?: emptyList()),
                displayItems = displayItems,
                transactionList = transactions,
                uiState = ScreenUiState.Success,
            )
        }
    }
}

/**
 * UI State for the Savings Account Details screen.
 *
 * @property accountId Unique ID for the savings account.
 * @property displayItems List of account metadata to be displayed.
 * @property transactionList List of most recent transaction details.
 * @property isActive True if the account is active.
 * @property items List of quick action items (Deposit, Transfer, etc.)
 * @property isUpdatable user can update only when status is submit and pending approval
 * @property dialogState State representing dialogs like error, loading, etc.
 */
@Immutable
internal data class SavingsAccountDetailsState(
    val accountId: Long = -1L,
    val clientName: String? = "",
    val submissionDate: String? = "",
    val accountNumber: String? = "",
    val product: String? = "",
    val displayItems: List<LabelValueItem> = emptyList(),
    val transactionList: List<LabelValueItem> = emptyList(),
    val savingStatus: SavingStatus? = null,
    val isActive: Boolean = false,
    val items: ImmutableList<SavingsActionItems>,

    val isUpdatable: Boolean = false,

    val dialogState: DialogState? = null,
    val networkStatus: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {
    /**
     * Represents UI dialog states.
     */
    sealed interface DialogState {
        /** Shown when an error occurs. */
        data class Error(val message: String) : DialogState
    }
}
val SavingStatus.allowedActions: Set<SavingsActionItems>
    get() = when (this) {
        SavingStatus.ACTIVE -> setOf(
            SavingsActionItems.Transactions,
            SavingsActionItems.Charges,
            SavingsActionItems.QrCode,
            SavingsActionItems.Transfer,
        )
        SavingStatus.INACTIVE -> setOf(
            SavingsActionItems.Transfer,
            SavingsActionItems.QrCode,
        )
        SavingStatus.CLOSED -> setOf(
            SavingsActionItems.QrCode,
            SavingsActionItems.Transfer,
            SavingsActionItems.Transactions,
        )
        SavingStatus.APPROVED -> setOf(
            SavingsActionItems.QrCode,
            SavingsActionItems.Transfer,
        )
        SavingStatus.SUBMIT_AND_PENDING_APPROVAL -> setOf(
            SavingsActionItems.QrCode,
        )
    }

/**
 * One-time navigation or effect events for the SavingsAccountDetails screen.
 */
sealed interface SavingsAccountDetailsEvent {
    /** Trigger navigation back. */
    data object NavigateBack : SavingsAccountDetailsEvent

    /** Trigger Event to navigate to respective screen. */
    data class NavigateToAction(val route: String) : SavingsAccountDetailsEvent

    /** Trigger Update Amount */
    data object UpdateAccount : SavingsAccountDetailsEvent
}

/**
 * Actions triggered from the UI layer to the ViewModel.
 */
sealed interface SavingsAccountDetailsAction {
    /** User tapped back. */
    data object OnNavigateBack : SavingsAccountDetailsAction

    /** User tapped on Action. */
    data class OnNavigateToAction(val route: String) : SavingsAccountDetailsAction

    /** When user retry */
    data object OnRetry : SavingsAccountDetailsAction

    /** User dismissed a dialog. */
    data object DismissDialog : SavingsAccountDetailsAction

    /** User tapped to update Account */
    data object OnUpdateAccount : SavingsAccountDetailsAction

    /** Action to observe network status */
    data class ReceiveNetworkStatus(val isOnline: Boolean) : SavingsAccountDetailsAction

    /**
     * Internal-only actions such as results from repository calls.
     */
    sealed interface Internal : SavingsAccountDetailsAction {
        /** Result of the savings account data fetch. */
        data class SavingsResultReceived(val dataState: DataState<SavingsWithAssociations>) : Internal
    }
}
