/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.loanType

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
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
import org.mifos.mobile.core.model.entity.templates.loans.ProductOptions
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState

/**
 * `ViewModel` for the Select Loan Type screen.
 *
 * This ViewModel handles the logic for fetching and displaying a list of available
 * loan products. It uses a [BaseViewModel] to manage its state ([SelectLoanTypeState]),
 * handle actions ([SelectLoanTypeAction]), and emit events ([SelectLoanTypeEvent]).
 * It also monitors network connectivity to handle network-related errors and retries.
 *
 * @param loanAccountRepositoryImpl Repository for fetching loan-related data.
 * @param userPreferencesRepositoryImpl Repository for accessing user preferences, specifically the client ID.
 * @param networkMonitor Monitors the network connectivity status.
 */
internal class SelectLoanTypeViewModel(
    private val loanAccountRepositoryImpl: LoanRepository,
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
    private val networkMonitor: NetworkMonitor,
) :
    BaseViewModel<SelectLoanTypeState, SelectLoanTypeEvent, SelectLoanTypeAction>(
        initialState = run {
            SelectLoanTypeState(
                clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
                productOptions = null,
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
                    sendAction(SelectLoanTypeAction.ReceiveNetworkStatus(isOnline))
                }
        }
    }

    /**
     * Handles incoming actions from the UI and dispatches them to the appropriate
     * business logic functions.
     *
     * @param action The [SelectLoanTypeAction] to be handled.
     */
    override fun handleAction(action: SelectLoanTypeAction) {
        when (action) {
            is SelectLoanTypeAction.NavigateBack -> {
                sendEvent(SelectLoanTypeEvent.NavigateBack)
            }

            is SelectLoanTypeAction.NavigateTo -> {
                sendEvent(SelectLoanTypeEvent.NavigateTo(action.productId, action.productName))
            }

            is SelectLoanTypeAction.Internal.ReceiveLoanTemplate -> handleLoanTemplate(action.template)

            is SelectLoanTypeAction.ReceiveNetworkStatus -> handleNetworkStatus(action.isOnline)

            SelectLoanTypeAction.DismissDialog -> handleDismissDialog()

            SelectLoanTypeAction.Retry -> retry()
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
                fetchLoanTemplate()
            }
        }
    }

    /**
     * Dismisses any currently visible dialog by setting the dialog state to null.
     */
    private fun handleDismissDialog() {
        updateState {
            it.copy(dialogState = null)
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
                fetchLoanTemplate()
            }
        }
    }

    /**
     * A helper function to update the mutable state flow.
     *
     * @param update A lambda function that takes the current state and returns a new state.
     */
    private fun updateState(update: (SelectLoanTypeState) -> SelectLoanTypeState) {
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
        updateState { it.copy(dialogState = SelectLoanTypeState.DialogState.Error(error)) }
    }

    /**
     * Fetches the loan template data from the repository. The template contains
     * loan product options and currency information.
     */
    private fun fetchLoanTemplate() {
        showLoading()
        viewModelScope.launch {
            loanAccountRepositoryImpl.template(state.clientId)
                .collect { result ->
                    sendAction(SelectLoanTypeAction.Internal.ReceiveLoanTemplate(result))
                }
        }
    }

    /**
     * Handles the result of the `fetchLoanTemplate` network call.
     * Updates the state with product options on success. If the list of
     * options is empty, it sets the `isEmpty` flag to true. On failure,
     * it displays an error dialog.
     *
     * @param template The [DataState] containing the loan template data.
     */
    private fun handleLoanTemplate(template: DataState<LoanTemplate?>) {
        when (template) {
            is DataState.Loading -> showLoading()
            is DataState.Success -> {
                val loanTemplate = template.data
                if (loanTemplate?.productOptions?.isEmpty() == true) {
                    updateState {
                        it.copy(
                            uiState = ScreenUiState.Empty,
                        )
                    }
                    return
                }
                updateState {
                    it.copy(
                        uiState = ScreenUiState.Success,
                        productOptions = loanTemplate?.productOptions,
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
}

/**
 * Represents the UI state for the Select Loan Type screen.
 *
 * @property clientId The ID of the current client.
 * @property productOptions The list of available loan product options, or `null` if not yet loaded.
 * @property dialogState The state of any dialog to be shown on the screen.
 * @property networkStatus A boolean indicating the current network connectivity status.
 * @property uiState The generic screen UI state, such as
 * [ScreenUiState.Loading], [ScreenUiState.Success], or [ScreenUiState.Error].
 */
@Immutable
internal data class SelectLoanTypeState(
    val clientId: Long,
    val productOptions: List<ProductOptions>? = null,
    val dialogState: DialogState? = null,
    val networkStatus: Boolean = true,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
) {
    /**
     * A sealed interface representing the different types of dialogs that can be
     * shown on the Select Loan Type screen.
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
internal sealed interface SelectLoanTypeEvent {
    /** Event to navigate back from the screen. */
    data object NavigateBack : SelectLoanTypeEvent

    /**
     * Event to navigate to the loan application screen for a specific loan type.
     *
     * @property productId The ID of the selected loan product to pass to the next screen.
     * @property productName The name of the selected loan product.
     */
    data class NavigateTo(val productId: Int, val productName: String) : SelectLoanTypeEvent
}

/**
 * A sealed interface representing user actions or internal events that the
 * ViewModel needs to handle for the Select Loan Type screen.
 */
internal sealed interface SelectLoanTypeAction {
    /** Action to dismiss a dialog. */
    data object DismissDialog : SelectLoanTypeAction

    /** Action to retry fetching data after an error or network issue. */
    data object Retry : SelectLoanTypeAction

    /** Action to navigate back from the screen. */
    data object NavigateBack : SelectLoanTypeAction

    /**
     * An action to receive and handle the network connectivity status.
     * @property isOnline A boolean indicating whether the network is available.
     */
    data class ReceiveNetworkStatus(val isOnline: Boolean) : SelectLoanTypeAction

    /**
     * Action to navigate to the loan application screen for a specific loan type.
     *
     * @property productId The ID of the selected loan product.
     * @property productName The name of the selected loan product.
     */
    data class NavigateTo(val productId: Int, val productName: String) : SelectLoanTypeAction

    /**
     * A sealed interface for internal actions, which are not triggered directly by the UI.
     */
    sealed interface Internal : SelectLoanTypeAction {
        /**
         * An internal action to handle the result of fetching a loan template.
         * @property template The [DataState] containing the loan template data.
         */
        data class ReceiveLoanTemplate(val template: DataState<LoanTemplate?>) : Internal
    }
}
