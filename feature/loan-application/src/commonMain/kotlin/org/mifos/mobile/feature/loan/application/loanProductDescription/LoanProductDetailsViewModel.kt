/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.loanProductDescription

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.loan_application.generated.resources.Res
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_error_server
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.templates.loans.LoanTemplate
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState

/**
 * `ViewModel` for the Loan Product Details screen.
 *
 * This ViewModel is responsible for fetching and displaying detailed information about a
 * specific loan product, handling user interactions like checking a checkbox, and
 * initiating the loan application process. It uses a [BaseViewModel] to manage its state
 * ([LoanProductDetailsState]), handle actions ([LoanProductDetailsAction]), and
 * emit events ([LoanProductDetailsEvent]). It also monitors network connectivity.
 *
 * @param savedStateHandle A handle to saved state data, used to retrieve navigation arguments.
 * @param loanAccountRepositoryImpl Repository for fetching loan-related data.
 * @param userPreferencesRepositoryImpl Repository for accessing user preferences, specifically the client ID.
 * @param networkMonitor Monitors the network connectivity status.
 */
internal class LoanProductDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val loanAccountRepositoryImpl: LoanRepository,
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
    private val networkMonitor: NetworkMonitor,
) : BaseViewModel<LoanProductDetailsState, LoanProductDetailsEvent, LoanProductDetailsAction>(
    initialState = run {
        val route = savedStateHandle.toRoute<LoanProductDetailsRoute>()
        LoanProductDetailsState(
            clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
            productId = route.productId,
            productName = route.productName,
        )
    },
) {

    init {
        observeNetworkStatus()
    }

    /**
     * Observes the network connectivity status and updates the UI state accordingly.
     * If the network becomes available, it triggers a fetch of the loan template.
     */
    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    sendAction(LoanProductDetailsAction.ReceiveNetworkStatus(isOnline))
                }
        }
    }

    /**
     * Handles incoming actions from the UI and dispatches them to the appropriate
     * business logic functions.
     *
     * @param action The [LoanProductDetailsAction] to be handled.
     */
    override fun handleAction(action: LoanProductDetailsAction) {
        when (action) {
            is LoanProductDetailsAction.ApplyLoan -> {
                if (state.checked) {
                    sendEvent(LoanProductDetailsEvent.ApplyLoan(state.productId, state.productName))
                }
            }

            is LoanProductDetailsAction.OnChecked -> {
                mutableStateFlow.update {
                    it.copy(checked = action.checked)
                }
            }

            is LoanProductDetailsAction.ReceiveNetworkStatus -> handleNetworkStatus(action.isOnline)

            LoanProductDetailsAction.NavigateBack -> {
                sendEvent(LoanProductDetailsEvent.NavigateBack)
            }

            is LoanProductDetailsAction.Internal.ReceiveLoanTemplateByProduct -> handleLoanTemplate(action.template)

            is LoanProductDetailsAction.Retry -> retry()
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
                fetchLoanTemplateByProduct()
            }
        }
    }

    /**
     * Retries the data fetching process. If the network is unavailable, it shows
     * a network error dialog. Otherwise, it triggers the `fetchLoanTemplate` function.
     */
    private fun retry() {
        viewModelScope.launch {
            if (!state.networkStatus) {
                updateState { it.copy(uiState = ScreenUiState.Network) }
            } else {
                fetchLoanTemplateByProduct()
            }
        }
    }

    /**
     * A helper function to update the mutable state flow.
     *
     * @param update A lambda function that takes the current state and returns a new state.
     */
    private fun updateState(update: (LoanProductDetailsState) -> LoanProductDetailsState) {
        mutableStateFlow.update(update)
    }

    /**
     * Sets the dialog state to a full-screen loading spinner.
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
        updateState { it.copy(dialogState = LoanProductDetailsState.DialogState.Error(error)) }
    }

    /**
     * Fetches the loan template data for the specific loan product from the repository.
     */
    private fun fetchLoanTemplateByProduct() {
        showLoading()
        viewModelScope.launch {
            loanAccountRepositoryImpl.getLoanTemplateByProduct(state.clientId, state.productId)
                .collect { result ->
                    sendAction(LoanProductDetailsAction.Internal.ReceiveLoanTemplateByProduct(result))
                }
        }
    }

    /**
     * Handles the result of the `fetchLoanTemplate` network call.
     * Updates the state with the principal amount and interest rates on success.
     * On failure, it displays an error dialog.
     *
     * @param template The [DataState] containing the loan template data.
     */
    private fun handleLoanTemplate(template: DataState<LoanTemplate?>) {
        when (template) {
            is DataState.Loading -> showLoading()
            is DataState.Success -> {
                val loanTemplate = template.data
                // TODO: The calculation for principal, minInterest, and maxInterest might need refinement
                // based on the actual business logic. The current implementation uses a simple format
                // and assumes min/max are the same.
                val principalText =
                    "${loanTemplate?.currency?.displaySymbol}${formatAmount(loanTemplate?.principal ?: 0.0)}"
                val minInterest = loanTemplate?.annualInterestRate
                val maxInterest = loanTemplate?.annualInterestRate

                updateState {
                    it.copy(
                        principalText = principalText,
                        minInterest = minInterest.toString(),
                        maxInterest = maxInterest.toString(),
                        uiState = ScreenUiState.Success,
                    )
                }
            }

            is DataState.Error -> {
                updateState {
                    it.copy(
                        uiState = ScreenUiState.Error(Res.string.feature_apply_loan_error_server),
                    )
                }
            }
        }
    }

    /**
     * Formats a given amount into a more readable string (e.g., "1.5 Million").
     *
     * @param amount The amount to format.
     * @return The formatted string representation of the amount.
     */
    private fun formatAmount(amount: Double): String {
        val formatted = when {
            amount >= 1_000_000 -> "${amount / 1_000_000} Million"
            amount >= 1_000 -> "${amount / 1_000} Thousand"
            else -> "$amount"
        }
        return formatted
    }
}

/**
 * Represents the UI state for the Loan Product Details screen.
 *
 * @property clientId The ID of the current client.
 * @property checked A boolean indicating if the terms and conditions checkbox is checked.
 * @property productId The ID of the current loan product.
 * @property productName The name of the current loan product.
 * @property networkStatus A boolean indicating the current network connectivity status.
 * @property dialogState The state of any dialog to be shown on the screen.
 * @property principalText The formatted string for the loan's principal amount.
 * @property minInterest The minimum annual interest rate as a string.
 * @property maxInterest The maximum annual interest rate as a string.
 */
internal data class LoanProductDetailsState(
    val clientId: Long,
    val checked: Boolean = false,
    val productId: Int,
    val productName: String,
    val networkStatus: Boolean = true,
    val dialogState: DialogState? = null,
    val principalText: String = "",
    val minInterest: String = "",
    val maxInterest: String = "",

    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {
    /**
     * A boolean indicating if the "Apply Loan" button should be enabled.
     */
    val isEnabled
        get() = checked

    /**
     * A sealed interface representing the different types of dialogs that can be
     * shown on the Loan Product Details screen.
     */
    sealed interface DialogState {
        /**
         * Represents a generic error dialog with a message.
         * @property error The [StringResource] for the error message.
         */
        data class Error(val error: StringResource) : DialogState
    }
}

/**
 * A sealed interface representing one-time events that trigger UI side effects,
 * such as navigation.
 */
internal sealed interface LoanProductDetailsEvent {
    /**
     * Event to navigate to the loan application screen.
     * @property productId The ID of the loan product to apply for.
     * @property productName The Name of the loan product to apply for.
     */
    data class ApplyLoan(val productId: Int?, val productName: String) : LoanProductDetailsEvent

    /** Event to navigate back from the screen. */
    data object NavigateBack : LoanProductDetailsEvent
}

/**
 * A sealed interface representing user actions or internal events that the
 * ViewModel needs to handle for the Loan Product Details screen.
 */
internal sealed interface LoanProductDetailsAction {
    /**
     * User action when the terms and conditions checkbox state changes.
     * @property checked The new state of the checkbox.
     */
    data class OnChecked(val checked: Boolean) : LoanProductDetailsAction

    /** User action to navigate back from the screen. */
    data object NavigateBack : LoanProductDetailsAction

    /** User action to apply for the loan. */
    data object ApplyLoan : LoanProductDetailsAction

    /** Action to retry fetching data after an error or network issue. */
    data object Retry : LoanProductDetailsAction

    /** Action to observe network status */
    data class ReceiveNetworkStatus(val isOnline: Boolean) : LoanProductDetailsAction

    /**
     * A sealed interface for internal actions, which are not triggered directly by the UI.
     */
    sealed interface Internal : LoanProductDetailsAction {
        /**
         * An internal action to handle the result of fetching a loan template by product.
         * @property template The [DataState] containing the loan template data.
         */
        data class ReceiveLoanTemplateByProduct(val template: DataState<LoanTemplate?>) : Internal
    }
}
