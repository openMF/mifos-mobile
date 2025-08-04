/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.transfer.process.makeTransfer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.TransferSuccessDestination
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.entity.templates.account.AccountOption
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.utils.BaseViewModel

/**
 * ViewModel for the Make Transfer screen.
 *
 * This ViewModel handles the business logic for making a transfer, including fetching
 * account options, validating user input, and initiating the transfer process.
 *
 * @param savingsAccountRepositoryImp Repository for savings account operations.
 * @param savedStateHandle Handle to saved state, used for retrieving navigation arguments.
 * @param networkMonitor Utility to monitor network connectivity.
 * @param accountsRepositoryImpl Repository for general account operations.
 * @param userPreferencesRepository Repository for accessing user preferences, like client ID.
 */
internal class MakeTransferViewModel(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    savedStateHandle: SavedStateHandle,
    private val networkMonitor: NetworkMonitor,
    private val accountsRepositoryImpl: AccountsRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) : BaseViewModel<MakeTransferState, MakeTransferEvent, MakeTransferAction>(
    initialState = run {
        val route = savedStateHandle.toRoute<MakeTransferRoute>()
        MakeTransferState(
            accountId = route.accountId,
            outstandingBalance = route.outstandingBalance,
            transferTarget = if (route.transferTarget != null) {
                enumValueOf<TransferType>(route.transferTarget)
            } else {
                null
            },
            transferType = route.transferType,
            transferSuccessDestination = if (route.transferSuccessDestination != null) {
                enumValueOf<TransferSuccessDestination>(route.transferSuccessDestination)
            } else {
                null
            },
        )
    },
) {

    init {
        initializeClient()
        observeNetworkStatus()
    }

    /**
     * Handles incoming actions from the UI.
     *
     * @param action The [MakeTransferAction] dispatched from the UI.
     */
    override fun handleAction(action: MakeTransferAction) {
        when (action) {
            is MakeTransferAction.OnToAccountSelected -> {
                val accountNo = action.accountNo
                val toAccountSelected = state.accountOptionsTemplate.toAccountOptions
                    .find { it.accountNo == accountNo }
                val fromAccounts = state.accountOptionsTemplate.fromAccountOptions.filter {
                    it.accountNo != accountNo
                }
                updateState {
                    it.copy(
                        toAccount = toAccountSelected,
                        fromAccountOptions = fromAccounts,
                    )
                }
            }

            is MakeTransferAction.OnFromAccountSelected -> {
                val accountNo = action.accountNo
                val fromAccountSelected = state.accountOptionsTemplate.fromAccountOptions
                    .find { it.accountNo == accountNo }
                val toAccounts = state.accountOptionsTemplate.toAccountOptions.filter {
                    it.accountNo != accountNo
                }
                updateState {
                    it.copy(
                        fromAccount = fromAccountSelected,
                        toAccountOptions = toAccounts,
                    )
                }
            }

            is MakeTransferAction.OnAmountChanged -> updateState {
                it.copy(amount = action.amount)
            }

            is MakeTransferAction.OnRemarksChanged -> updateState {
                it.copy(remarks = action.remarks)
            }

            MakeTransferAction.OnMakeTransferClicked -> {
                val isError = state.amount.any {
                    !it.isDigit()
                }
                updateState {
                    it.copy(amountError = isError)
                }
                if (!isError) {
                    viewModelScope.launch {
                        sendAction(MakeTransferAction.Internal.PerformTransfer)
                    }
                }
            }

            MakeTransferAction.DismissDialog -> updateState {
                it.copy(dialogState = null)
            }

            is MakeTransferAction.Internal.PerformTransfer -> {
                val payload = ReviewTransferPayload(
                    payToAccount = state.toAccount,
                    payFromAccount = state.fromAccount,
                    amount = state.amount,
                    review = state.remarks,
                )
                sendEvent(
                    MakeTransferEvent.NavigateToTransferScreen(
                        reviewTransferPayload = payload,
                        transferType = state.transferTarget ?: TransferType.SELF,
                        destination = state.transferSuccessDestination
                            ?: TransferSuccessDestination.HOME,
                    ),
                )
            }

            is MakeTransferAction.Internal.ReceiveAccountOptionsTemplateResult -> {
                handleTransferResult(action.dataState)
            }

            MakeTransferAction.NavigateBack -> {
                sendEvent(MakeTransferEvent.NavigateBack)
            }

            MakeTransferAction.OnRetry -> {
                fetchAccountOptions()
            }

            is MakeTransferAction.Internal.ReceiveActiveAccountsResult -> {
                handleActiveAccountsResult(action.dataState)
            }
        }
    }

    /**
     * Fetches the active loan account for the current client if the initial accountId is invalid.
     * This is typically a fallback or initial loading mechanism.
     */
    private fun fetchActiveAccount() {
        viewModelScope.launch {
            accountsRepositoryImpl.loadAccounts(
                clientId = state.clientId,
                accountType = Constants.LOAN_ACCOUNTS,
            ).collect { result ->
                sendAction(
                    MakeTransferAction
                        .Internal.ReceiveActiveAccountsResult(result),
                )
            }
        }
    }

    /**
     * Helper function to update the [MakeTransferState].
     *
     * @param update Lambda function that takes the current state and returns the updated state.
     */
    private fun updateState(update: (MakeTransferState) -> MakeTransferState) {
        mutableStateFlow.update(update)
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
                                MakeTransferState.DialogState.Network
                            } else {
                                null
                            },
                        )
                    }
                    if (!isOffline) {
                        fetchAccountOptions()
                    }
                }
        }
    }

    private fun initializeClient() {
        viewModelScope.launch {
            userPreferencesRepository.clientId.collect { client ->
                updateState {
                    state.copy(
                        clientId = client ?: -1L,
                    )
                }
            }
        }
    }

    /**
     * Fetches the account transfer template which includes options for 'from' and 'to' accounts.
     * If the current `accountId` in the state is invalid (-1L), it first attempts to
     * [fetchActiveAccount]. Otherwise, it proceeds to fetch the account options.
     */
    private fun fetchAccountOptions() {
        if (state.accountId == -1L) {
            fetchActiveAccount()
        } else {
            updateState { it.copy(dialogState = MakeTransferState.DialogState.Loading) }
            viewModelScope.launch {
                savingsAccountRepositoryImp
                    .accountTransferTemplate(accountId = state.accountId, accountType = 2L)
                    .collect { result ->
                        sendAction(
                            MakeTransferAction
                                .Internal.ReceiveAccountOptionsTemplateResult(result),
                        )
                    }
            }
        }
    }

    /**
     * Handles the result of fetching the account transfer template.
     * Updates the UI state based on success, error, or loading states.
     *
     * @param dataState The [DataState] of the [AccountOptionsTemplate] fetch operation.
     */
    private fun handleTransferResult(dataState: DataState<AccountOptionsTemplate>) {
        when (dataState) {
            is DataState.Error -> {
                updateState {
                    it.copy(
                        dialogState = MakeTransferState.DialogState.Error(
                            dataState.message,
                        ),
                    )
                }
            }
            DataState.Loading -> {
                updateState {
                    it.copy(
                        dialogState = MakeTransferState.DialogState.Loading,
                    )
                }
            }
            is DataState.Success -> {
                updateState {
                    it.copy(
                        accountOptionsTemplate = dataState.data,
                        fromAccountOptions = dataState.data.fromAccountOptions,
                        toAccountOptions = dataState.data.toAccountOptions,
                        dialogState = null,
                    )
                }
            }
        }
    }

    private fun handleActiveAccountsResult(result: DataState<ClientAccounts>) {
        when (result) {
            is DataState.Success -> {
                val activeAccount = result.data.loanAccounts.firstOrNull { it.status?.active == true }
                activeAccount?.let { acc ->
                    updateState {
                        it.copy(
                            accountId = acc.id,
                        )
                    }
                    fetchAccountOptions()
                }
            }

            is DataState.Error -> {
                updateState {
                    it.copy(
                        accountId = -1L,
                        dialogState = MakeTransferState.DialogState.Error(
                            result.message,
                        ),
                    )
                }
            }

            DataState.Loading -> {
                updateState {
                    it.copy(
                        dialogState = MakeTransferState.DialogState.Loading,
                    )
                }
            }
        }
    }
}

/**
 * Represents the state of the Make Transfer screen.
 *
 * @property accountId The ID of the primary account involved in the transfer (e.g., loan account for repayment).
 * @property outstandingBalance The outstanding balance of the primary account, if applicable.
 * @property transferType A string representation of the type of transfer being made (e.g., "LOAN_REPAYMENT").
 * @property transferTarget The enumerated [TransferType] indicating the nature of the transfer.
 * @property transferSuccessDestination The destination screen to navigate to upon successful transfer.
 * @property amount The amount entered by the user for the transfer.
 * @property amountError Flag indicating if there's an error in the entered amount.
 * @property remarks Optional remarks or notes for the transfer.
 * @property accountOptionsTemplate The template containing lists of available 'from' and 'to' accounts.
 * @property fromAccountOptions List of accounts available to transfer from.
 * @property toAccountOptions List of accounts available to transfer to.
 * @property fromAccount The currently selected account to transfer from.
 * @property toAccount The currently selected account to transfer to.
 * @property dialogState The current state of any dialogs to be shown (e.g., loading, error).
 * @property isEnabled Computed property indicating if the transfer button should be enabled.
 */
internal data class MakeTransferState(
    val accountId: Long = -1L,
    val clientId: Long = -1L,
    val outstandingBalance: Double? = null,
    val transferType: String? = null,
    val transferTarget: TransferType? = null,
    val transferSuccessDestination: TransferSuccessDestination? = null,
    val amount: String = "",
    val amountError: Boolean = false,
    val remarks: String = "",
    var accountOptionsTemplate: AccountOptionsTemplate = AccountOptionsTemplate(),
    var fromAccountOptions: List<AccountOption> = emptyList(),
    var toAccountOptions: List<AccountOption> = emptyList(),
    val fromAccount: AccountOption? = null,
    val toAccount: AccountOption? = null,
    val dialogState: DialogState? = null,
    val networkUnavailable: Boolean = false,
) {
    /**
     * Represents the possible states of a dialog shown on the Make Transfer screen.
     */
    sealed interface DialogState {
        /**
         * Represents an error state, containing an error message.
         * @property message The error message to display.
         */
        data class Error(val message: String) : DialogState

        /** Represents a loading state, typically shown when data is being fetched. */
        data object Loading : DialogState

        /** Represents a network error state */
        data object Network : DialogState
    }

    /**
     * Determines if the make transfer button should be enabled.
     * True if a 'from' account, 'to' account are selected, and an amount is entered.
     */
    val isEnabled: Boolean = fromAccount != null && toAccount != null && amount.isNotBlank()
}

/**
 * Represents user actions that can occur on the Make Transfer screen.
 * These actions are dispatched from the UI to the [MakeTransferViewModel].
 */
internal sealed interface MakeTransferAction {
    /** Action triggered when a 'to' account is selected. @param accountNo The account number selected. */
    data class OnToAccountSelected(val accountNo: String) : MakeTransferAction

    /** Action triggered when a 'from' account is selected. @param accountNo The account number selected. */
    data class OnFromAccountSelected(val accountNo: String) : MakeTransferAction

    /** Action triggered when the transfer amount is changed. @param amount The new amount string. */
    data class OnAmountChanged(val amount: String) : MakeTransferAction

    /** Action triggered when the remarks are changed. @param remarks The new remarks string. */
    data class OnRemarksChanged(val remarks: String) : MakeTransferAction

    /** Action triggered when the 'Make Transfer' button is clicked. */
    data object OnMakeTransferClicked : MakeTransferAction

    /** Action triggered to dismiss any currently shown dialog. */
    data object DismissDialog : MakeTransferAction

    /** Action triggered to navigate back from the current screen. */
    data object NavigateBack : MakeTransferAction

    /** Action triggered to retry a failed operation, typically fetching account options. */
    data object OnRetry : MakeTransferAction

    /**
     * Represents internal actions used within the ViewModel, not directly triggered by the UI.
     */
    sealed interface Internal : MakeTransferAction {
        /** Internal action to initiate the transfer process. */
        data object PerformTransfer : Internal

        /**
         * Internal action representing the result of fetching account options.
         * @param dataState The result of the fetch operation.
         */
        data class ReceiveAccountOptionsTemplateResult(val dataState: DataState<AccountOptionsTemplate>) : Internal

        data class ReceiveActiveAccountsResult(val dataState: DataState<ClientAccounts>) : Internal
    }
}

/**
 * Represents events that the [MakeTransferViewModel] can send to the UI.
 * These events typically trigger navigation or one-time UI updates.
 */
internal sealed interface MakeTransferEvent {
    /** Event to navigate back from the current screen. */
    data object NavigateBack : MakeTransferEvent

    /**
     * Event to navigate to the transfer review screen.
     * @param reviewTransferPayload The payload containing details for the transfer review.
     * @param transferType The type of transfer being performed.
     * @param destination The screen to navigate to after a successful transfer from the review screen.
     */
    data class NavigateToTransferScreen(
        val reviewTransferPayload: ReviewTransferPayload,
        val transferType: TransferType,
        val destination: TransferSuccessDestination,
    ) : MakeTransferEvent
}
