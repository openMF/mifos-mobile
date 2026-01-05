/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.third.party.transfer.thirdPartyTransfer

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.third_party_transfer.generated.resources.Res
import mifos_mobile.feature.third_party_transfer.generated.resources.feature_tpt_error_amount_invalid
import mifos_mobile.feature.third_party_transfer.generated.resources.feature_tpt_error_amount_required
import mifos_mobile.feature.third_party_transfer.generated.resources.feature_tpt_error_remarks_empty
import mifos_mobile.feature.third_party_transfer.generated.resources.feature_tpt_error_server
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.ThirdPartyTransferRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.payload.ReviewTransferPayload
import org.mifos.mobile.core.model.entity.templates.account.AccountOption
import org.mifos.mobile.core.model.entity.templates.account.AccountOptionsTemplate
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState

/**
 * ViewModel for the Make Transfer screen.
 *
 * This ViewModel handles the business logic for making a transfer, including fetching
 * account options, validating user input, and initiating the transfer process.
 *
 * @param thirdPartyTransferRepositoryImpl The repository for third-party transfer operations.
 * @param networkMonitor A utility to monitor network connectivity.
 * @param userPreferencesRepositoryImpl The repository for accessing user preferences, like client ID.
 */
internal class TptViewModel(
    private val thirdPartyTransferRepositoryImpl: ThirdPartyTransferRepository,
    private val networkMonitor: NetworkMonitor,
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
) : BaseViewModel<TptState, TptEvent, TptAction>(
    initialState = run {
        TptState(
            clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
        )
    },
) {

    init {
        observeNetworkStatus()
    }

    private var validationJob: Job? = null

    /**
     * Functions related to UI State and Dialogs
     */

    /**
     * A helper function to update the mutable state flow.
     *
     * @param update A lambda function that takes the current state and returns a new state.
     */
    private fun updateState(update: (TptState) -> TptState) {
        mutableStateFlow.update(update)
    }

    /**
     * Dismisses any currently shown dialog by setting the dialog state to null.
     */
    private fun dismissDialog() {
        updateState { it.copy(dialogState = null) }
    }

    /**
     * Sets the UI state to a full-screen loading spinner.
     */
    private fun showLoading() {
        updateState { it.copy(uiState = ScreenUiState.Loading) }
    }

    /**
     * Displays an error dialog with a given message.
     *
     * @param error The [StringResource] for the error message to display.
     */
    @Suppress("UnusedPrivateMember")
    private fun showErrorDialog(error: StringResource) {
        updateState { it.copy(dialogState = TptState.DialogState.Error(error)) }
    }

    /**
     * Handles incoming actions from the UI.
     *
     * @param action The [TptAction] dispatched from the UI.
     */
    override fun handleAction(action: TptAction) {
        when (action) {
            is TptAction.ReceiveNetworkStatus -> handleNetworkStatus(action.isOnline)

            is TptAction.OnToAccountSelected -> handleToAccountChange(action.accountNo)

            is TptAction.OnFromAccountSelected -> handleFromAccountChange(action.accountNo)

            is TptAction.OnAmountChanged -> handleAmountChange(action.amount)

            is TptAction.OnRemarksChanged -> handleRemarkChange(action.remarks)

            TptAction.OnMakeTransferClicked -> validateAndPerformTransfer()

            TptAction.OnNotificationClicked -> sendEvent(
                TptEvent.NavigateToNotificationScreen,
            )

            TptAction.OnAddBeneficiaryClicked -> sendEvent(
                TptEvent.NavigateToAddBeneficiaryScreen,
            )

            TptAction.DismissDialog -> dismissDialog()

            is TptAction.Internal.PerformTransfer -> performTransfer()

            is TptAction.Internal.ReceiveTransferTemplateResult ->
                handleTransferTemplateResult(action.dataState)

            TptAction.OnRetry -> retry()
        }
    }

    /**
     * Retries the last failed operation.
     *
     * Checks network status and either shows a network error or re-fetches the accounts.
     */
    private fun retry() {
        viewModelScope.launch {
            if (!state.networkStatus) {
                updateState { it.copy(uiState = ScreenUiState.Network) }
            } else {
                fetchAccountOptions()
            }
        }
    }

    /**
     * Handles the selection of a "from" (origin) account.
     *
     * This function updates the state by:
     * 1. Finding the selected account from the full list of 'from' accounts.
     * 2. Filtering the list of 'to' (destination) accounts to exclude the selected 'from' account.
     *
     * @param fromAccount The account number of the selected 'from' account.
     */
    private fun handleFromAccountChange(fromAccount: String) {
        val fromAccountSelected = state.accountOptionsTemplate.fromAccountOptions
            .find { it.accountNo == fromAccount }

        val toAccounts = state.accountOptionsTemplate.toAccountOptions.filter {
            it.accountNo != fromAccount
        }
        updateState {
            it.copy(
                fromAccount = fromAccountSelected,
                toAccountOptions = toAccounts,
            )
        }
    }

    /**
     * Handles the selection of a "to" (destination) account.
     *
     * This function updates the state by:
     * 1. Finding the selected account from the full list of 'to' accounts.
     * 2. Filtering the list of 'from' (origin) accounts to exclude the selected 'to' account
     * and ensure only savings accounts are included.
     *
     * @param toAccount The account number of the selected 'to' account.
     */
    private fun handleToAccountChange(toAccount: String) {
        val toAccountSelected = state.accountOptionsTemplate.toAccountOptions
            .find { it.accountNo == toAccount }

        val fromAccounts = state.accountOptionsTemplate.fromAccountOptions.filter {
            it.accountNo != toAccount
        }

        updateState {
            it.copy(
                toAccount = toAccountSelected,
                fromAccountOptions = fromAccounts,
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
     * Validates all form fields and, if valid, proceeds to perform the transfer.
     *
     * It checks for validation errors in the amount and remarks fields. If valid,
     * it dispatches an internal action to perform the transfer.
     */
    private fun validateAndPerformTransfer() {
        val amountResult = validateAmount(state.amount)
        val remarkResult = validateRemark(state.remark)

        mutableStateFlow.update {
            it.copy(
                amountError = if (amountResult is ValidationResult.Error) amountResult.message else null,
                remarkError = if (remarkResult is ValidationResult.Error) remarkResult.message else null,
            )
        }

        val isValid = listOf(
            amountResult,
            remarkResult,
        ).all { it is ValidationResult.Success }
        if (isValid) {
            viewModelScope.launch {
                sendAction(TptAction.Internal.PerformTransfer)
            }
        }
    }

    /**
     * Initiates the transfer by creating a [ReviewTransferPayload] and sending a
     * navigation event to the UI.
     */
    private fun performTransfer() {
        val payload = ReviewTransferPayload(
            payToAccount = state.toAccount,
            payFromAccount = state.fromAccount,
            amount = state.amount,
            review = state.remark,
        )
        sendEvent(
            TptEvent.NavigateToTransferScreen(
                reviewTransferPayload = payload,
            ),
        )
    }

    /**
     * Validates the transfer amount.
     *
     * @param amount The string amount to validate.
     * @return A [ValidationResult] indicating success or an error.
     */
    private fun validateAmount(amount: String) = when {
        amount.isBlank() -> ValidationResult.Error(Res.string.feature_tpt_error_amount_required)
        amount.toDoubleOrNull() == null -> ValidationResult.Error(Res.string.feature_tpt_error_amount_invalid)
        else -> ValidationResult.Success
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
                ValidationResult.Error(Res.string.feature_tpt_error_remarks_empty)

            else -> ValidationResult.Success
        }

    /**
     * Observes the network status and updates the UI state accordingly.
     *
     * When the network is online, it triggers a data fetch. If the network is offline,
     * it sets the UI state to a network error.
     */
    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    sendAction(TptAction.ReceiveNetworkStatus(isOnline))
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
                fetchAccountOptions()
            }
        }
    }

    /**
     * Fetches the account transfer template which includes options for 'from' and 'to' accounts.
     * This function first shows a loading state and then calls the repository to get the data.
     */
    private fun fetchAccountOptions() {
        showLoading()
        viewModelScope.launch {
            thirdPartyTransferRepositoryImpl
                .thirdPartyTransferTemplate()
                .collect { result ->
                    sendAction(
                        TptAction.Internal.ReceiveTransferTemplateResult(result),
                    )
                }
        }
    }

    /**
     * Handles the result of fetching the account transfer template.
     *
     * Updates the UI state based on success, error, or loading states. If successful, it populates
     * the `fromAccountOptions` with savings accounts and the `toAccountOptions` with the full list.
     *
     * @param dataState The [DataState] of the [AccountOptionsTemplate] fetch operation.
     */
    private fun handleTransferTemplateResult(dataState: DataState<AccountOptionsTemplate>) {
        when (dataState) {
            is DataState.Error -> {
                updateState {
                    it.copy(
                        uiState = ScreenUiState.Error(
                            Res.string.feature_tpt_error_server,
                        ),
                    )
                }
            }

            DataState.Loading -> showLoading()

            is DataState.Success -> {
                updateState {
                    it.copy(
                        accountOptionsTemplate = dataState.data,
                        fromAccountOptions = dataState.data.fromAccountOptions,
                        toAccountOptions = dataState.data.toAccountOptions,
                        uiState = ScreenUiState.Success,
                    )
                }
            }
        }
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
 * @property clientId The ID of the current user.
 * @property outstandingBalance The outstanding balance of the primary account, if applicable.
 * @property amount The amount entered by the user for the transfer.
 * @property amountError The error message for the amount field, if any.
 * @property remark Optional remarks or notes for the transfer.
 * @property remarkError The error message for the remark field, if any.
 * @property accountOptionsTemplate The full template containing all 'from' and 'to' accounts.
 * @property fromAccountOptions List of accounts available to transfer from.
 * @property toAccountOptions List of accounts available to transfer to.
 * @property fromAccount The currently selected account to transfer from.
 * @property toAccount The currently selected account to transfer to.
 * @property dialogState The current state of any dialogs to be shown (e.g., loading, error).
 * @property networkStatus The current network connectivity status.
 * @property uiState The overall screen state (Loading, Success, Error, etc.).
 */
internal data class TptState(
    val accountId: Long = -1L,
    val clientId: Long = -1L,
    val outstandingBalance: Double? = null,
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
    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {
    /**
     * Represents the possible states of a dialog shown on the Make Transfer screen.
     */
    sealed interface DialogState {
        /**
         * Represents an error state, containing an error message.
         * @property message The error message to display.
         */
        data class Error(val message: StringResource) : DialogState
    }

    /**
     * A computed property that determines if the make transfer button should be enabled.
     * It requires a valid 'from' account, a valid 'to' account, a non-blank amount and remark,
     * and no validation errors.
     */
    val isEnabled: Boolean = fromAccount != null &&
        toAccount != null &&
        amount.isNotBlank() &&
        remark.isNotBlank() &&
        amountError == null &&
        remarkError == null
}

/**
 * Represents user actions that can occur on the Make Transfer screen.
 * These actions are dispatched from the UI to the [TptViewModel].
 */
internal sealed interface TptAction {
    /** Action triggered when a 'to' account is selected. @property accountNo The account number selected. */
    data class OnToAccountSelected(val accountNo: String) : TptAction

    /** Action triggered when a 'from' account is selected. @property accountNo The account number selected. */
    data class OnFromAccountSelected(val accountNo: String) : TptAction

    /** Action triggered when the transfer amount is changed. @property amount The new amount string. */
    data class OnAmountChanged(val amount: String) : TptAction

    /** Action triggered when the remarks are changed. @property remarks The new remarks string. */
    data class OnRemarksChanged(val remarks: String) : TptAction

    /** Action triggered when the 'Make Transfer' button is clicked. */
    data object OnMakeTransferClicked : TptAction

    /** Action triggered when the 'Notification' Icon clicked. */
    data object OnNotificationClicked : TptAction

    /** Action triggered when the 'Add Beneficiary' clicked. */
    data object OnAddBeneficiaryClicked : TptAction

    /** Action triggered to dismiss any currently shown dialog. */
    data object DismissDialog : TptAction

    /** Action triggered to retry a failed operation, typically fetching account options. */
    data object OnRetry : TptAction

    /** Action to observe network status */
    data class ReceiveNetworkStatus(val isOnline: Boolean) : TptAction

    /**
     * Represents internal actions used within the ViewModel, not directly triggered by the UI.
     */
    sealed interface Internal : TptAction {
        /** Internal action to initiate the transfer process. */
        data object PerformTransfer : Internal

        /**
         * Internal action representing the result of fetching account options.
         * @property dataState The result of the fetch operation.
         */
        data class ReceiveTransferTemplateResult(val dataState: DataState<AccountOptionsTemplate>) :
            Internal
    }
}

/**
 * Represents events that the [TptViewModel] can send to the UI.
 * These events typically trigger navigation or one-time UI updates.
 */
internal sealed interface TptEvent {
    /**
     * Event to navigate to the transfer review screen.
     *
     * @property reviewTransferPayload The payload containing details for the transfer review.
     */
    data class NavigateToTransferScreen(
        val reviewTransferPayload: ReviewTransferPayload,
    ) : TptEvent

    /**
     * Event to navigate to the Notification screen.
     *
     */
    data object NavigateToNotificationScreen : TptEvent

    /**
     * Event to navigate to the Add Beneficiary screen.
     *
     */
    data object NavigateToAddBeneficiaryScreen : TptEvent
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
