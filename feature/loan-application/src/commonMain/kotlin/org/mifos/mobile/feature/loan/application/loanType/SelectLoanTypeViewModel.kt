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
                dialogState = SelectLoanTypeState.DialogState.Loading,
                isEmpty = false,
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
                    updateState {
                        it.copy(
                            networkStatus = isOnline,
                            dialogState = if (!isOnline) {
                                SelectLoanTypeState.DialogState.Network
                            } else {
                                null
                            },
                        )
                    }

                    if (isOnline) {
                        fetchLoanTemplate()
                    }
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
                sendEvent(SelectLoanTypeEvent.NavigateTo(action.loanType))
            }

            is SelectLoanTypeAction.Internal.ReceiveLoanTemplate -> handleLoanTemplate(action.template)

            SelectLoanTypeAction.DismissDialog -> handleDismissDialog()

            SelectLoanTypeAction.Retry -> retry()
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
                updateState { it.copy(dialogState = SelectLoanTypeState.DialogState.Network) }
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
        updateState { it.copy(dialogState = SelectLoanTypeState.DialogState.Loading) }
    }

    /**
     * Displays an error dialog with a given message.
     *
     * @param error The [StringResource] for the error message to display.
     */
    private fun showErrorDialog(error: StringResource) {
        updateState { it.copy(dialogState = SelectLoanTypeState.DialogState.Error(error)) }
    }

    /**
     * Fetches the loan template data from the repository. The template contains
     * loan product options and currency information.
     */
    private fun fetchLoanTemplate() {
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
                        it.copy(isEmpty = true, dialogState = null)
                    }
                    return
                }
                updateState {
                    it.copy(
                        isEmpty = false,
                        productOptions = loanTemplate?.productOptions,
                        dialogState = null,
                    )
                }
            }
            is DataState.Error -> {
                showErrorDialog(Res.string.feature_apply_loan_error_server)
            }
        }
    }
}

/**
 * Represents the UI state for the Select Loan Type screen.
 *
 * @property clientId The ID of the current client.
 * @property isEmpty A boolean indicating if the list of loan products is empty.
 * @property productOptions The list of available loan product options, or `null`.
 * @property dialogState The state of any dialog to be shown on the screen.
 * @property networkStatus A boolean indicating the current network connectivity status.
 */
@Immutable
internal data class SelectLoanTypeState(
    val clientId: Long,
    val isEmpty: Boolean = false,
    val productOptions: List<ProductOptions>? = null,
    val dialogState: DialogState?,
    val networkStatus: Boolean = true,
) {
    /**
     * A sealed interface representing the different types of dialogs that can be
     * shown on the Select Loan Type screen.
     */
    sealed interface DialogState {
        /** Represents a full-screen loading state. */
        data object Loading : DialogState

        /**
         * Represents a generic error dialog with a message.
         * @property error The [StringResource] for the error message.
         */
        data class Error(val error: StringResource) : DialogState

        /** Represents a network connectivity error state. */
        data object Network : DialogState
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
     * @property loanType The selected [ProductOptions] to pass to the next screen.
     */
    data class NavigateTo(val loanType: ProductOptions) : SelectLoanTypeEvent
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
     * Action to navigate to the loan application screen for a specific loan type.
     * @property loanType The selected [ProductOptions].
     */
    data class NavigateTo(val loanType: ProductOptions) : SelectLoanTypeAction

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
