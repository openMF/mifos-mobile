/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savingsaccount.savingsAccountWithdraw

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import mifos_mobile.core.ui.generated.resources.internal_server_error
import mifos_mobile.feature.savings_account.generated.resources.Res
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_account_number_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_client_name_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_product_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_request_back_to_home
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_request_try_again
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_update_submission_date_label
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_withdraw_request_failed
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_withdraw_request_failed_message
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_withdraw_request_successful
import mifos_mobile.feature.savings_account.generated.resources.feature_savings_withdraw_request_successful_message
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.model.EventType
import org.mifos.mobile.core.model.StatusNavigationDestination
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountWithdrawPayload
import org.mifos.mobile.core.ui.utils.AuthResult
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ResultNavigator
import org.mifos.mobile.core.ui.utils.observe
import org.mifos.mobile.feature.savingsaccount.savingsAccountUpdate.SavingsAccountUpdateRoute
import mifos_mobile.core.ui.generated.resources.Res as UiRes

/**
 * ViewModel responsible for handling the logic of withdrawing a savings account product.
 *
 * This includes:
 * - Fetching initial state from navigation arguments and user preferences
 * - Listening for authentication result from passcode screen
 * - Performing the withdraw operation
 * - Displaying loading and error states
 * - Emitting events for navigation to other screens
 *
 * @property savingsAccountRepositoryImp Repository for performing savings account operations
 * @property navigator Used to receive result from the authentication screen (passcode)
 * @property savedStateHandle Used to retrieve initial route arguments (`SavingsAccountUpdateRoute`)
 */
internal class AccountWithdrawViewModel(
    private val savingsAccountRepositoryImp: SavingsAccountRepository,
    private val navigator: ResultNavigator,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<AccountWithdrawState, AccountWithdrawEvent, AccountWithdrawAction>(
    initialState = run {
        val clientDetails = savedStateHandle.toRoute<SavingsAccountUpdateRoute>()
        val detailsMap = mapOf(
            Res.string.feature_savings_update_client_name_label to clientDetails.clientName,
            Res.string.feature_savings_update_submission_date_label to clientDetails.submissionData,
            Res.string.feature_savings_update_account_number_label to clientDetails.accountNumber,
            Res.string.feature_savings_update_product_label to clientDetails.product,
        )

        AccountWithdrawState(
            accountId = requireNotNull(clientDetails.accountId),
            details = detailsMap,
            dialogState = null,
        )
    },
) {

    init {
        observeAuthResult()
    }

    /**
     * Observes authentication result (passcode confirmation) and triggers update flow if successful.
     */
    private fun observeAuthResult() {
        viewModelScope.launch {
            navigator.observe<AuthResult>()
                .collect { result ->
                    sendAction(AccountWithdrawAction.Internal.ReceiveAuthenticationResult(result.success))
                }
        }
    }

    /**
     * Updates the state by applying a transformation function.
     *
     * @param update Lambda to transform the current state
     */
    private fun updateState(update: (AccountWithdrawState) -> AccountWithdrawState) {
        mutableStateFlow.update(update)
    }

    /**
     * Handles all types of actions from UI or internal flows.
     *
     * @param action Action dispatched from the UI or internal coroutine flows
     */
    override fun handleAction(action: AccountWithdrawAction) {
        when (action) {
            AccountWithdrawAction.OnNavigateBack -> sendEvent(AccountWithdrawEvent.NavigateBack)

            is AccountWithdrawAction.RemarkChange -> handleRemarkChange(action.remark)

            is AccountWithdrawAction.RequestWithdraw ->
                sendEvent(AccountWithdrawEvent.NavigateToAuthenticate())

            is AccountWithdrawAction.DismissDialog -> handleDismissDialog()

            is AccountWithdrawAction.Internal.PerformWithdraw -> performWithdraw()

            is AccountWithdrawAction.Internal.ReceiveAuthenticationResult ->
                handleRequestWithdraw(action.result)

            is AccountWithdrawAction.Internal.ReceiveWithdrawRequestResult -> {
                viewModelScope.launch {
                    handleWithdrawRequestResult(action.dataState)
                }
            }
        }
    }

    /**
     * Updates the current remark value in the UI state.
     *
     * This function is called when the user modifies the remarks field
     * (e.g., entering a reason for withdrawal).
     *
     * @param remark The new remark text entered by the user.
     */
    private fun handleRemarkChange(remark: String) {
        updateState { it.copy(remark = remark) }
    }

    /**
     * Called after receiving authentication result.
     * If result is `true`, triggers PerformWithdraw action.
     *
     * @param result Whether the authentication (passcode) was successful
     */
    private fun handleRequestWithdraw(result: Boolean) {
        if (result) {
            viewModelScope.launch {
                sendAction(AccountWithdrawAction.Internal.PerformWithdraw)
            }
        }
    }

    /**
     * Performs the withdraw request by making a repository call and dispatching result.
     */
    private fun performWithdraw() {
        val payload = SavingsAccountWithdrawPayload(
            note = state.remark,
            withdrawnOnDate = DateHelper.formattedFullDate,
            dateFormat = "dd MMMM yyyy",
            locale = "en",
        )

        viewModelScope.launch {
            updateState { it.copy(dialogState = AccountWithdrawState.DialogState.Loading) }

            val response = savingsAccountRepositoryImp.submitWithdrawSavingsAccount(
                accountId = state.accountId,
                payload = payload,
            )

            sendAction(AccountWithdrawAction.Internal.ReceiveWithdrawRequestResult(response))
        }
    }

    /**
     * Handles the result from update account API.
     *
     * @param dataState Result of API call (Loading, Success, or Error)
     */
    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    private suspend fun handleWithdrawRequestResult(dataState: DataState<String>) {
        when (dataState) {
            is DataState.Loading -> updateState {
                it.copy(
                    dialogState = AccountWithdrawState.DialogState.Loading,
                )
            }

            is DataState.Error -> {
                val errorMsg = if (dataState.exception.cause is ServerResponseException) {
                    getString(UiRes.string.internal_server_error)
                } else {
                    getString(Res.string.feature_savings_withdraw_request_failed_message)
                }

                sendEvent(
                    AccountWithdrawEvent.NavigateToStatus(
                        eventType = if (dataState.exception.cause is ServerResponseException) {
                            EventType.SERVER_EXCEPTION.name
                        } else {
                            EventType.FAILURE.name
                        },
                        eventDestination = StatusNavigationDestination.PREVIOUS_SCREEN.name,
                        title = getString(Res.string.feature_savings_withdraw_request_failed),
                        subtitle = errorMsg,
                        buttonText = getString(Res.string.feature_savings_update_request_try_again),
                    ),
                )
            }

            is DataState.Success -> {
                sendEvent(
                    AccountWithdrawEvent.NavigateToStatus(
                        eventType = EventType.SUCCESS.name,
                        eventDestination = StatusNavigationDestination.SAVINGS_WITHDRAW.name,
                        title = getString(Res.string.feature_savings_withdraw_request_successful),
                        subtitle = getString(Res.string.feature_savings_withdraw_request_successful_message),
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
 * UI state for the withdraw account screen.
 *
 * @property accountId ID of the savings account
 * @property details Map of string labels and corresponding data (client name, submission date, etc.)
 * @property remark remark entered by the user
 * @property dialogState Current dialog state (loading or error)
 */
internal data class AccountWithdrawState(
    val accountId: Long?,
    val details: Map<StringResource, String?> = emptyMap(),
    val remark: String = "",
    val dialogState: DialogState?,
) {
    /**
     * Represents different dialog types that can appear in the UI.
     */
    sealed interface DialogState {
        /**
         * Error dialog with a descriptive message.
         */
        data class Error(val message: String) : DialogState

        /**
         * Loading dialog shown during network operations.
         */
        data object Loading : DialogState
    }
}

/**
 * Events emitted from the ViewModel to the UI for navigation or one-time effects.
 */
internal sealed interface AccountWithdrawEvent {

    /**
     * Navigate back to the previous screen.
     */
    data object NavigateBack : AccountWithdrawEvent

    /**
     * Navigate to the passcode screen for authentication.
     *
     * @property status Optional status string, defaulting to "SUCCESS".
     */
    data class NavigateToAuthenticate(
        val status: String = EventType.SUCCESS.name,
    ) : AccountWithdrawEvent

    /**
     * Navigate to a status screen after the withdraw operation.
     *
     * @property eventType Type of event ("SUCCESS" or "FAILURE").
     * @property eventDestination Route identifier to navigate post-status.
     * @property title Title message to show on the status screen.
     * @property subtitle Subtitle or message details.
     * @property buttonText Action button text for user.
     */
    data class NavigateToStatus(
        val eventType: String,
        val eventDestination: String,
        val title: String,
        val subtitle: String,
        val buttonText: String,
    ) : AccountWithdrawEvent
}

/**
 * Actions that can be triggered by the UI or internally by the ViewModel.
 */
internal sealed interface AccountWithdrawAction {

    /**
     * User tapped the back button.
     */
    data object OnNavigateBack : AccountWithdrawAction

    /**
     * User initiated the withdraw request.
     */
    data object RequestWithdraw : AccountWithdrawAction

    /**
     * Action dispatched when the user changes the remark field.
     *
     * @property remark The new remark text entered by the user.
     */
    data class RemarkChange(val remark: String) : AccountWithdrawAction

    /**
     * User dismissed the error or loading dialog.
     */
    data object DismissDialog : AccountWithdrawAction

    /**
     * Internal-only actions triggered by internal logic.
     */
    sealed interface Internal : AccountWithdrawAction {

        /**
         * Result from withdraw API call.
         *
         * @property dataState Result of network call.
         */
        data class ReceiveWithdrawRequestResult(val dataState: DataState<String>) : Internal

        /**
         * Result from passcode authentication screen.
         *
         * @property result True if authentication passed.
         */
        data class ReceiveAuthenticationResult(val result: Boolean) : Internal

        /**
         * Triggers the actual withdraw logic.
         */
        data object PerformWithdraw : Internal
    }
}
