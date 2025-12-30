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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.transfer_process.generated.resources.Res
import mifos_mobile.feature.transfer_process.generated.resources.feature_make_transfer_error_amount_invalid
import mifos_mobile.feature.transfer_process.generated.resources.feature_make_transfer_error_amount_required
import mifos_mobile.feature.transfer_process.generated.resources.feature_make_transfer_error_remarks_empty
import mifos_mobile.feature.transfer_process.generated.resources.feature_make_transfer_error_server
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.StatusNavigationDestination
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.entity.templates.account.AccountOption
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.core.ui.utils.BaseViewModel

/**
 * ViewModel for the Make Transfer screen.
 *
 * This ViewModel handles the business logic for making a transfer, including fetching
 * account options, validating user input, and initiating the transfer process.
 *
 * @param savingsAccountRepositoryImpl Repository for savings account operations.
 * @param savedStateHandle Handle to saved state, used for retrieving navigation arguments.
 * @param networkMonitor Utility to monitor network connectivity.
 * @param accountsRepositoryImpl Repository for general account operations.
 * @param userPreferencesRepositoryImpl Repository for accessing user preferences, like client ID.
 */
internal class MakeTransferViewModel(
    private val savingsAccountRepositoryImpl: SavingsAccountRepository,
    savedStateHandle: SavedStateHandle,
    private val networkMonitor: NetworkMonitor,
    private val accountsRepositoryImpl: AccountsRepository,
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
) : BaseViewModel<MakeTransferState, MakeTransferEvent, MakeTransferAction>(
    initialState = run {
        val route = savedStateHandle.toRoute<MakeTransferRoute>()
        MakeTransferState(
            clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
            accountId = route.accountId,
            outstandingBalance = route.outstandingBalance?.toDouble(),
            transferTarget = if (route.transferTarget != null) {
                enumValueOf<TransferType>(route.transferTarget)
            } else {
                null
            },
            transferType = route.transferType,
            transferSuccessDestination = route.transferSuccessDestination
                ?: StatusNavigationDestination.HOME.name,
            uiState = MakeTransferState.MakeTransferScreenState.Loading,
        )
    },
) {

    init {
        observeNetworkStatus()
    }

    private var validationJob: Job? = null

    /**
     * Handles incoming actions from the UI.
     *
     * @param action The [MakeTransferAction] dispatched from the UI.
     */
    override fun handleAction(action: MakeTransferAction) {
        when (action) {
            is MakeTransferAction.Internal.ReceiveNetworkStatus -> handleNetworkStatus(action.isOnline)

            is MakeTransferAction.OnToAccountSelected -> handleToAccountChange(action.accountNo)

            is MakeTransferAction.OnFromAccountSelected -> handleFromAccountChange(action.accountNo)

            is MakeTransferAction.OnAmountChanged -> handleAmountChange(action.amount)

            is MakeTransferAction.OnRemarksChanged -> handleRemarkChange(action.remark)

            MakeTransferAction.OnMakeTransferClicked -> validateTransfer()

            MakeTransferAction.DismissDialog -> updateState {
                it.copy(dialogState = null)
            }

            is MakeTransferAction.Internal.PerformTransfer -> performTransfer()

            is MakeTransferAction.Internal.ReceiveAccountOptionsTemplateResult -> {
                handleTransferResult(action.dataState)
            }

            MakeTransferAction.NavigateBack -> {
                sendEvent(MakeTransferEvent.NavigateBack)
            }

            MakeTransferAction.OnRetry -> retry()

            is MakeTransferAction.Internal.ReceiveActiveAccountsResult -> {
                handleActiveAccountsResult(action.dataState)
            }
        }
    }

    /**
     * Retries the data fetching process. If the network is unavailable, it shows
     * a network error dialog. Otherwise, it triggers the `fetchAccountOptions` ,
     */
    private fun retry() {
        viewModelScope.launch {
            if (!state.networkStatus) {
                updateState { it.copy(uiState = MakeTransferState.MakeTransferScreenState.Network) }
            } else {
                fetchAccountOptions()
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

    /**
     * Handles changes to the `to` account selection.
     *
     * This function updates the ViewModel's state to reflect the newly selected `to` account.
     * It finds the selected account from the available `toAccountOptions` and updates the
     * `toAccount` property in the state. It also filters the `fromAccountOptions` to ensure
     * the same account cannot be selected for both 'from' and 'to' fields, providing a
     * smooth user experience.
     *
     * @param toAccountNo The account number of the newly selected `to` account.
     */
    private fun handleToAccountChange(toAccountNo: String) {
        val toAccountSelected = state.accountOptionsTemplate.toAccountOptions
            .find { it.accountNo == toAccountNo }

        val fromAccounts = state.accountOptionsTemplate.fromAccountOptions
            .filter {
                it.accountType?.value == AccountType.SAVINGS.value &&
                    it.accountNo != toAccountNo
            }

        updateState {
            it.copy(
                toAccount = toAccountSelected,
                fromAccountOptions = fromAccounts,
            )
        }
    }

    /**
     * Handles changes to the `from` account selection.
     *
     * This function updates the ViewModel's state with the newly selected `from` account.
     * It identifies the selected account from `fromAccountOptions` and sets it as the
     * `fromAccount` in the state. It also filters the `toAccountOptions` to prevent
     * the same account from being selected for both 'from' and 'to' fields.
     *
     * @param fromAccountNo The account number of the newly selected `from` account.
     */
    private fun handleFromAccountChange(fromAccountNo: String) {
        val fromAccountSelected = state.accountOptionsTemplate.fromAccountOptions
            .filter { it.accountType?.value == AccountType.SAVINGS.value }
            .find { it.accountNo == fromAccountNo }

        val toAccounts = state.accountOptionsTemplate.toAccountOptions
            .filter { it.accountNo != fromAccountNo }

        updateState {
            it.copy(
                fromAccount = fromAccountSelected,
                toAccountOptions = toAccounts,
            )
        }
    }

    /**
     * Handles changes to the transfer amount input field.
     *
     * This function updates the state with the new amount and triggers a debounced validation
     * to prevent validation on every keystroke.
     *
     * @param amount The new string value of the amount.
     */
    private fun handleAmountChange(amount: String) {
        updateState {
            it.copy(
                amount = amount,
                amountError = null,
            )
        }

        debounceValidation {
            val result = validateAmount(amount)
            mutableStateFlow.update {
                it.copy(
                    amountError = if (result is ValidationResult.Error) {
                        result.message
                    } else {
                        null
                    },
                )
            }
        }
    }

    /**
     * Validates the transfer amount.
     *
     * @param amount The string amount to validate.
     * @return A [ValidationResult] indicating success or an error.
     */
    private fun validateAmount(amount: String) = when {
        amount.isBlank() -> ValidationResult.Error(Res.string.feature_make_transfer_error_amount_required)
        !Regex("^\\d+(\\.\\d+)?$").matches(amount) ->
            ValidationResult.Error(Res.string.feature_make_transfer_error_amount_invalid)

        else -> ValidationResult.Success
    }

    /**
     * Handles changes to the remarks input field.
     *
     * This function updates the state with the new remark and triggers a debounced validation.
     *
     * @param remark The new string value of the remark.
     */
    private fun handleRemarkChange(remark: String) {
        updateState {
            it.copy(
                remark = remark,
                remarkError = null,
            )
        }

        debounceValidation {
            val result = validateRemark(remark)
            mutableStateFlow.update {
                it.copy(
                    remarkError = if (result is ValidationResult.Error) {
                        result.message
                    } else {
                        null
                    },
                )
            }
        }
    }

    /**
     * Validates the remarks field.
     *
     * @param remark The string remark to validate.
     * @return A [ValidationResult] indicating success or an error.
     */
    private fun validateRemark(remark: String): ValidationResult =
        when {
            remark.isEmpty() ->
                ValidationResult.Error(Res.string.feature_make_transfer_error_remarks_empty)

            else -> ValidationResult.Success
        }

    /**
     * Observes the network status and triggers an action when it changes.
     *
     * This function uses a `viewModelScope` to launch a coroutine that collects
     * network status changes from the `networkMonitor`. It uses `distinctUntilChanged`
     * to ensure the action is only dispatched when the status actually changes,
     * and then sends a `ReceiveNetworkStatus` action to `handleAction` for processing.
     */
    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    sendAction(MakeTransferAction.Internal.ReceiveNetworkStatus(isOnline))
                }
        }
    }

    /**
     * Handles network status changes and updates the UI state accordingly.
     *
     * This function is invoked when a network status change is detected. It updates the
     * `networkStatus` in the state.
     * - **If the device goes offline:** It checks if the current screen state is `Error` or `Network`
     * and updates it to `Network` to show a network-related error.
     * - **If the device comes online:** It triggers a call to `fetchAccountOptions()` to
     * reload necessary data and restore the UI.
     *
     * @param isOnline A `Boolean` indicating the current network connectivity status.
     */
    private fun handleNetworkStatus(isOnline: Boolean) {
        updateState { it.copy(networkStatus = isOnline) }

        viewModelScope.launch {
            if (!isOnline) {
                updateState { current ->
                    if (current.uiState is MakeTransferState.MakeTransferScreenState.Error ||
                        current.uiState is MakeTransferState.MakeTransferScreenState.Network ||
                        current.uiState is MakeTransferState.MakeTransferScreenState.Loading
                    ) {
                        current.copy(uiState = MakeTransferState.MakeTransferScreenState.Network)
                    } else {
                        current
                    }
                }
            } else {
                fetchAccountOptions()
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
            updateState { it.copy(uiState = MakeTransferState.MakeTransferScreenState.Loading) }
            viewModelScope.launch {
                savingsAccountRepositoryImpl
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
                        uiState = MakeTransferState.MakeTransferScreenState.Error(
                            Res.string.feature_make_transfer_error_server,
                        ),
                    )
                }
            }

            DataState.Loading -> {
                updateState {
                    it.copy(
                        uiState = MakeTransferState.MakeTransferScreenState.Loading,
                    )
                }
            }

            is DataState.Success -> {
                updateState { current ->
                    val template = dataState.data

                    when (current.transferSuccessDestination) {
                        StatusNavigationDestination.SAVINGS_ACCOUNT.name,
                        StatusNavigationDestination.LOAN_ACCOUNT.name,
                        -> {
                            val savingsFromAccounts = template.fromAccountOptions.filter {
                                it.accountType?.value == AccountType.SAVINGS.value
                            }

                            val prepopulatedFromAccount =
                                when (current.transferSuccessDestination) {
                                    StatusNavigationDestination.SAVINGS_ACCOUNT.name,
                                    StatusNavigationDestination.LOAN_ACCOUNT.name,
                                    -> {
                                        savingsFromAccounts.firstOrNull { it.accountId?.toLong() == current.accountId }
                                    }

                                    else -> current.fromAccount
                                }

                            val prepopulatedToAccount = when (current.transferSuccessDestination) {
                                StatusNavigationDestination.LOAN_ACCOUNT.name -> {
                                    template.toAccountOptions.firstOrNull {
                                        it.accountId?.toLong() == current.accountId
                                    }
                                }

                                else -> current.toAccount
                            }

                            val amount = current.outstandingBalance?.toString() ?: current.amount

                            val filteredFromAccounts = savingsFromAccounts.filter {
                                it.accountNo != prepopulatedToAccount?.accountNo
                            }
                            val filteredToAccounts = template.toAccountOptions.filter {
                                it.accountNo != prepopulatedFromAccount?.accountNo
                            }

                            current.copy(
                                accountOptionsTemplate = template,
                                fromAccountOptions = filteredFromAccounts,
                                toAccountOptions = filteredToAccounts,
                                fromAccount = prepopulatedFromAccount,
                                toAccount = prepopulatedToAccount,
                                amount = amount,
                                uiState = MakeTransferState.MakeTransferScreenState.Success,
                            )
                        }

                        else -> {
                            current.copy(
                                accountOptionsTemplate = template,
                                fromAccountOptions = template.fromAccountOptions,
                                toAccountOptions = template.toAccountOptions,
                                uiState = MakeTransferState.MakeTransferScreenState.Success,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun handleActiveAccountsResult(result: DataState<ClientAccounts>) {
        when (result) {
            is DataState.Success -> {
                val activeAccount =
                    result.data.loanAccounts.firstOrNull { it.status?.active == true }
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
                        uiState = MakeTransferState.MakeTransferScreenState.Error(
                            Res.string.feature_make_transfer_error_server,
                        ),
                    )
                }
            }

            DataState.Loading -> {
                updateState {
                    it.copy(
                        uiState = MakeTransferState.MakeTransferScreenState.Loading,
                    )
                }
            }
        }
    }

    /**
     * Validates all required fields for a transfer.
     *
     * This function performs validation on the `amount` and `remark` fields. It updates the
     * state with any validation errors. If all validations pass, it calls
     * [performTransfer] to proceed with the transfer.
     */
    private fun validateTransfer() {
        val amountResult = validateAmount(state.amount)
        val remarkResult = validateRemark(state.remark)

        mutableStateFlow.update {
            it.copy(
                amountError = if (amountResult is ValidationResult.Error) {
                    amountResult.message
                } else {
                    null
                },
                remarkError = if (remarkResult is ValidationResult.Error) remarkResult.message else null,
            )
        }

        val isValid = listOf(
            amountResult,
            remarkResult,
        ).all { it is ValidationResult.Success }
        if (isValid) {
            viewModelScope.launch {
                sendAction(MakeTransferAction.Internal.PerformTransfer)
            }
        }
    }

    /**
     * Initiates the transfer process by sending a navigation event.
     *
     * This function constructs a `ReviewTransferPayload` from the current state and sends a
     * `MapsToTransferScreen` event to the UI. This event is intended to trigger navigation
     * to a review or confirmation screen before the transfer is finalized.
     */
    private fun performTransfer() {
        val payload = ReviewTransferPayload(
            payToAccount = state.toAccount,
            payFromAccount = state.fromAccount,
            amount = state.amount,
            review = state.remark,
        )
        sendEvent(
            MakeTransferEvent.NavigateToTransferScreen(
                reviewTransferPayload = payload,
                transferType = state.transferTarget ?: TransferType.SELF,
                destination = state.transferSuccessDestination,
            ),
        )
    }

    /**
     * Cancels any ongoing validation and launches the given validation block after a delay.
     * Used for debounced validation of form fields to prevent unnecessary updates on every keystroke.
     */
    private fun debounceValidation(validation: suspend () -> Unit) {
        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            delay(300)
            validation()
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
 * @property amountError StringResource if there's an error in the entered amount.
 * @property remark remarks or notes for the transfer.
 * @property amountError StringResource if there's an error in the entered remark.
 * @property accountOptionsTemplate The template containing lists of available 'from' and 'to' accounts.
 * @property fromAccountOptions List of accounts available to transfer from.
 * @property toAccountOptions List of accounts available to transfer to.
 * @property fromAccount The currently selected account to transfer from.
 * @property toAccount The currently selected account to transfer to.
 * @property dialogState The current state of any dialogs to be shown (e.g., loading, error).
 * @property isEnabled Computed property indicating if the transfer button should be enabled.
 * @property uiState The current state of the screen, such as loading or error states.
 */
internal data class MakeTransferState(
    val accountId: Long = -1L,
    val clientId: Long = -1L,
    val outstandingBalance: Double? = null,
    val transferType: String? = null,
    val transferTarget: TransferType? = null,
    val transferSuccessDestination: String = "",
    val amount: String = "",
    val amountError: StringResource? = null,
    val remark: String = "",
    val remarkError: StringResource? = null,
    var accountOptionsTemplate: AccountOptionsTemplate = AccountOptionsTemplate(),
    var fromAccountOptions: List<AccountOption> = emptyList(),
    var toAccountOptions: List<AccountOption> = emptyList(),
    val fromAccount: AccountOption? = null,
    val toAccount: AccountOption? = null,
    val dialogState: DialogState? = null,
    val networkStatus: Boolean = false,
    val uiState: MakeTransferScreenState? = null,
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
    }

    sealed interface MakeTransferScreenState {
        /** Represents a full-screen loading state. */
        data object Loading : MakeTransferScreenState

        /**
         * Represents an error state with a message.
         * @property message The string message for the error.
         */
        data class Error(val message: StringResource) : MakeTransferScreenState

        /** Represents a successful state where content can be displayed. */
        data object Success : MakeTransferScreenState

        /** Represents a state where there is a network connectivity issue. */
        data object Network : MakeTransferScreenState

        /** Represents a state where an overlay loading spinner should be shown. */
        data object OverlayLoading : MakeTransferScreenState
    }

    /**
     * Determines if the make transfer button should be enabled.
     * True if a 'from' account, 'to' account are selected, and an amount is entered.
     */
    val isEnabled = networkStatus &&
        fromAccount != null &&
        toAccount != null &&
        amount.isNotBlank() &&
        remark.isNotBlank() &&
        amountError == null &&
        remarkError == null
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
    data class OnRemarksChanged(val remark: String) : MakeTransferAction

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
        data class ReceiveAccountOptionsTemplateResult(val dataState: DataState<AccountOptionsTemplate>) :
            Internal

        data class ReceiveActiveAccountsResult(val dataState: DataState<ClientAccounts>) : Internal

        /**
         * Internal Action triggered by network status observation.
         * @property isOnline A boolean indicating if the device is online.
         */
        data class ReceiveNetworkStatus(val isOnline: Boolean) : Internal
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
        val destination: String,
    ) : MakeTransferEvent
}

/**
 * Represents the result of a field validation operation.
 *
 * This sealed class provides a structured way to return the outcome of a validation check,
 * indicating either success or a specific error.
 */
internal sealed class ValidationResult {
    /** Indicates that the validation passed successfully. */
    data object Success : ValidationResult()

    /**
     * Indicates that the validation failed.
     * @property message The localized string resource for the error message.
     */
    data class Error(val message: StringResource) : ValidationResult()
}
