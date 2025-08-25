/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount.savingsAccountUpdate

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import mifos_mobile.feature.savings_account.generated.resources.Res
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_account_number_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_client_name_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_product_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_request_back_to_home
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_request_failed
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_request_failed_message
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_request_successful
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_request_successful_message
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_request_try_again
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_submission_date_label
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.EventType
import org.mifos.mobile.core.model.StatusNavigationDestination
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountUpdatePayload
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.core.ui.utils.AuthResult
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ResultNavigator
import org.mifos.mobile.core.ui.utils.observe

/**
 * ViewModel responsible for handling the logic of updating a savings account product.
 *
 * This includes:
 * - Fetching initial state from navigation arguments and user preferences
 * - Listening for authentication result from passcode screen
 * - Performing the account update
 * - Displaying loading and error states
 * - Emitting events for navigation to other screens
 *
 * @property userPreferencesRepository Repository for accessing user preferences (clientId, etc.)
 * @property savingsAccountRepositoryImp Repository for performing savings account operations
 * @property navigator Used to receive result from the authentication screen (passcode)
 * @property savedStateHandle Used to retrieve initial route arguments (`SavingsAccountUpdateRoute`)
 */
internal class AccountUpdateViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    private val navigator: ResultNavigator,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<AccountUpdateState, AccountUpdateEvent, AccountUpdateAction>(
    initialState = run {
        val clientDetails = savedStateHandle.toRoute<SavingsAccountUpdateRoute>()
        val detailsMap = mapOf(
            Res.string.feature_savings_update_client_name_label to clientDetails.clientName,
            Res.string.feature_savings_update_submission_date_label to clientDetails.submissionData,
            Res.string.feature_savings_update_account_number_label to clientDetails.accountNumber,
            Res.string.feature_savings_update_product_label to clientDetails.product,
        )
        val productOptions = mapOf(
            1L to "Wallet",
            2L to "FDP",
            4L to "1 year Fixed Deposit",

        )
        AccountUpdateState(
            clientId = requireNotNull(userPreferencesRepository.clientId.value),
            accountId = requireNotNull(clientDetails.accountId),
            details = detailsMap,
            productOptions = productOptions,
            dialogState = null,
        )
    },
) {

    init {
        observeAuthResult()
    }

//    TODO get savings product from server
//    init {
//        observeSavingsProducts()
//    }

//    private fun observeSavingsProducts() {
//        viewModelScope.launch {
//            savingsAccountRepositoryImp.getSavingAccountApplicationTemplate(state.clientId)
//                .collect {
//                    sendAction(AccountUpdateAction.Internal.ReceiveProducts(it))
//                }
//        }
//    }

    /**
     * Observes authentication result (passcode confirmation) and triggers update flow if successful.
     */
    private fun observeAuthResult() {
        viewModelScope.launch {
            navigator.observe<AuthResult>()
                .collect { result ->
                    sendAction(AccountUpdateAction.Internal.ReceiveAuthenticationResult(result.success))
                }
        }
    }

    /**
     * Updates the state by applying a transformation function.
     *
     * @param update Lambda to transform the current state
     */
    private fun updateState(update: (AccountUpdateState) -> AccountUpdateState) {
        mutableStateFlow.update(update)
    }

    /**
     * Handles all types of actions from UI or internal flows.
     *
     * @param action Action dispatched from the UI or internal coroutine flows
     */
    override fun handleAction(action: AccountUpdateAction) {
        when (action) {
            AccountUpdateAction.OnNavigateBack -> sendEvent(AccountUpdateEvent.NavigateBack)

            AccountUpdateAction.RequestUpdate -> sendEvent(AccountUpdateEvent.NavigateToAuthenticate())

            is AccountUpdateAction.OnProductSelected -> handleProductChange(action.id, action.product)

//            TODO handle received products
            is AccountUpdateAction.Internal.ReceiveProducts -> { }

            is AccountUpdateAction.Internal.ReceiveUpdateRequestResult -> {
                viewModelScope.launch {
                    handleUpdateRequestResult(action.dataState)
                }
            }

            is AccountUpdateAction.Internal.ReceiveAuthenticationResult -> {
                handleRequestUpdate(action.result)
            }

            is AccountUpdateAction.Internal.PerformUpdate -> performUpdate()

            is AccountUpdateAction.DismissDialog -> handleDismissDialog()
        }
    }

    /**
     * Updates state based on selected product from dropdown.
     *
     * @param id Selected product ID
     * @param product Selected product name
     */
    private fun handleProductChange(id: Long, product: String) {
        updateState { it.copy(selectedProductId = id, selectedProduct = product) }
    }

    /**
     * Called after receiving authentication result.
     * If result is `true`, triggers PerformUpdate action.
     *
     * @param result Whether the authentication (passcode) was successful
     */
    private fun handleRequestUpdate(result: Boolean) {
        if (result) {
            viewModelScope.launch {
                sendAction(AccountUpdateAction.Internal.PerformUpdate)
            }
        }
    }

    /**
     * Performs the account update by making a repository call and dispatching result.
     */
    private fun performUpdate() {
        viewModelScope.launch {
            updateState { it.copy(dialogState = AccountUpdateState.DialogState.Loading) }

            val response = savingsAccountRepositoryImp.updateSavingsAccount(
                accountId = state.accountId,
                payload = SavingsAccountUpdatePayload(
                    clientId = state.clientId,
                    productId = state.selectedProductId,
                ),
            )

            sendAction(AccountUpdateAction.Internal.ReceiveUpdateRequestResult(response))
        }
    }

    /**
     * Handles the result from update account API.
     *
     * @param dataState Result of API call (Loading, Success, or Error)
     */
    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    private suspend fun handleUpdateRequestResult(dataState: DataState<String>) {
        when (dataState) {
            is DataState.Loading -> updateState {
                it.copy(
                    dialogState = AccountUpdateState.DialogState.Loading,
                )
            }

            is DataState.Error -> {
                sendEvent(
                    AccountUpdateEvent.NavigateToStatus(
                        eventType = EventType.FAILURE.name,
                        eventDestination = StatusNavigationDestination.PREVIOUS_SCREEN.name,
                        title = getString(Res.string.feature_savings_update_request_failed),
                        subtitle = getString(Res.string.feature_savings_update_request_failed_message),
                        buttonText = getString(Res.string.feature_savings_update_request_try_again),
                    ),
                )
            }

            is DataState.Success -> {
                sendEvent(
                    AccountUpdateEvent.NavigateToStatus(
                        eventType = EventType.SUCCESS.name,
                        eventDestination = StatusNavigationDestination.SAVINGS_UPDATE.name,
                        title = getString(Res.string.feature_savings_update_request_successful),
                        subtitle = getString(Res.string.feature_savings_update_request_successful_message),
                        buttonText = getString(Res.string.feature_savings_update_request_back_to_home),
                    ),
                )
            }
        }
    }

    /**
     * Dismisses the current dialog (error/loading).
     */
    private fun handleDismissDialog() {
        updateState { it.copy(dialogState = null) }
    }
}

/**
 * UI state for the account update screen.
 *
 * @property clientId ID of the client (from user preferences)
 * @property accountId ID of the savings account to be updated
 * @property details Map of string labels and corresponding data (name, submission date, etc.)
 * @property productOptions Map of available savings product IDs and names
 * @property selectedProductId ID of the product selected by the user
 * @property selectedProduct Name of the product selected
 * @property dialogState Current dialog state (loading or error)
 */
internal data class AccountUpdateState(
    val clientId: Long?,
    val accountId: Long?,
    val details: Map<StringResource, String?> = emptyMap(),
    val productOptions: Map<Long, String> = emptyMap(),
    val selectedProductId: Long? = null,
    val selectedProduct: String = "",
    val dialogState: DialogState?,
) {
    /**
     * Represents different dialog types that can appear in the UI.
     */
    sealed interface DialogState {
        /**
         * Error dialog with message to show.
         */
        data class Error(val message: String) : DialogState

        /**
         * Loading dialog shown during network operations.
         */
        data object Loading : DialogState
    }
}

/**
 * Events emitted from the ViewModel to the UI for navigation or user feedback.
 */
internal sealed interface AccountUpdateEvent {

    /**
     * Navigate back to the previous screen.
     */
    data object NavigateBack : AccountUpdateEvent

    /**
     * Navigate to the passcode screen for authentication.
     *
     * @property status Status string used for context (defaults to SUCCESS)
     */
    data class NavigateToAuthenticate(
        val status: String = EventType.SUCCESS.name,
    ) : AccountUpdateEvent

    /**
     * Navigate to status screen after update operation completes.
     *
     * @property eventType Status type (SUCCESS/FAILURE)
     * @property eventDestination Route to return to
     * @property title Title to show in the status screen
     * @property subtitle Subtitle for further info
     * @property buttonText Action button text
     */
    data class NavigateToStatus(
        val eventType: String,
        val eventDestination: String,
        val title: String,
        val subtitle: String,
        val buttonText: String,
    ) : AccountUpdateEvent
}

/**
 * Actions that can be dispatched from the UI layer or internally in the ViewModel.
 */
internal sealed interface AccountUpdateAction {

    /**
     * User clicked back button.
     */
    data object OnNavigateBack : AccountUpdateAction

    /**
     * User initiated update operation.
     */
    data object RequestUpdate : AccountUpdateAction

    /**
     * User dismissed dialog.
     */
    data object DismissDialog : AccountUpdateAction

    /**
     * User selected a different savings product.
     *
     * @property id Product ID
     * @property product Product name
     */
    data class OnProductSelected(
        val id: Long,
        val product: String,
    ) : AccountUpdateAction

    /**
     * Internal actions triggered by business logic or repository responses.
     */
    sealed interface Internal : AccountUpdateAction {

        /**
         * Receives savings product options from the backend (future extension).
         *
         * @property dataState State of savings template request
         */
        data class ReceiveProducts(val dataState: DataState<SavingsAccountTemplate>) : Internal

        /**
         * Receives result of savings account update request.
         *
         * @property dataState Result state (loading, success, error)
         */
        data class ReceiveUpdateRequestResult(val dataState: DataState<String>) : Internal

        /**
         * Receives result from authentication screen.
         *
         * @property result True if user was authenticated
         */
        data class ReceiveAuthenticationResult(val result: Boolean) : Internal

        /**
         * Triggers the actual account update request.
         */
        data object PerformUpdate : Internal
    }
}
