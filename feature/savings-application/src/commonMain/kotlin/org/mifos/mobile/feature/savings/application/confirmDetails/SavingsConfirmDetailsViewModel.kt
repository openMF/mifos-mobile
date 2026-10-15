/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.application.confirmDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.savings_application.generated.resources.Res
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_applicant_name
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_currency
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_frequency
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_frequency_type
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_minimum_opening_balance
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_overdraft
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_savings_product
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_label_submission_date
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_status_failure
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_status_failure_action
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_status_success
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_status_success_action
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.EventType
import org.mifos.mobile.core.model.StatusNavigationDestination
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.core.ui.utils.AuthResult
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ResultNavigator
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.core.ui.utils.observe

/**
 * `ViewModel` for the confirm details screen of the savings application process.
 *
 * This ViewModel is responsible for:
 * - Receiving and displaying savings application details from the previous screen.
 * - Handling user authentication via a passcode.
 * - Submitting the savings application to the server.
 * - Handling success and failure states of the submission and navigating accordingly.
 *
 * @param userPreferencesRepositoryImpl Repository for accessing user preferences, such as client ID.
 * @param repo Repository for submitting the savings application.
 * @param resultNavigator A navigator to observe and receive results from other screens, like authentication.
 * @param savedStateHandle A handle to saved state data, used to retrieve navigation arguments.
 */
@Suppress("MaxLineLength")
internal class SavingsConfirmDetailsViewModel(
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
    private val repo: SavingsAccountRepository,
    private val resultNavigator: ResultNavigator,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<SavingsConfirmDetailsState, SavingsConfirmDetailsEvent, SavingsConfirmDetailsAction>(
    initialState = run {
        val route = savedStateHandle.toRoute<SavingsConfirmDetailsRoute>()
        // Build details map, filtering out empty optional fields
        val detailMap = buildMap {
            // Always show required fields
            put(Res.string.feature_apply_savings_label_applicant_name, route.applicantName)
            put(Res.string.feature_apply_savings_label_savings_product, route.savingsProductName)
            put(Res.string.feature_apply_savings_label_submission_date, route.submittedOnDate)
            put(Res.string.feature_apply_savings_label_currency, route.currency)

            // Only show optional fields if they have values
            if (route.minOpeningBalance.trim().isNotEmpty()) {
                put(Res.string.feature_apply_savings_label_minimum_opening_balance, route.minOpeningBalance)
            }
            if (route.frequency.trim().isNotEmpty()) {
                put(Res.string.feature_apply_savings_label_frequency, route.frequency)
            }
            if (route.frequencyTypeName.trim().isNotEmpty()) {
                put(Res.string.feature_apply_savings_label_frequency_type, route.frequencyTypeName)
            }
            if (route.allowOverdraft) {
                put(Res.string.feature_apply_savings_label_overdraft, "Yes")
            }
        }

        SavingsConfirmDetailsState(
            clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
            details = detailMap,
            savingsProductId = route.savingsProductId,
            savingsProductName = route.savingsProductName,
            currency = route.currency,
            minOpeningBalance = route.minOpeningBalance,
            frequency = route.frequency,
            frequencyTypeName = route.frequencyTypeName,
            frequencyTypeId = route.frequencyTypeId,
            allowOverdraft = route.allowOverdraft,
            submittedOnDate = route.submittedOnDate,
        )
    },
) {

    init {
        observeAuthResult()
    }

    /**
     * Observes the result from the authentication screen.
     * If the authentication is successful, it triggers the savings application submission.
     */
    private fun observeAuthResult() {
        viewModelScope.launch {
            resultNavigator.observe<AuthResult>()
                .collect { result ->
                    sendAction(SavingsConfirmDetailsAction.Internal.ReceiveAuthenticationResult(result.success))
                }
        }
    }

    /**
     * Helper function to update the mutable state flow.
     */
    private fun updateState(update: (SavingsConfirmDetailsState) -> SavingsConfirmDetailsState) {
        mutableStateFlow.update(update)
    }

    /**
     * Handles incoming actions from the UI and dispatches them to the appropriate
     * business logic functions.
     */
    override fun handleAction(action: SavingsConfirmDetailsAction) {
        when (action) {
            SavingsConfirmDetailsAction.OnNavigateBack -> {
                sendEvent(SavingsConfirmDetailsEvent.NavigateBack)
            }

            SavingsConfirmDetailsAction.NavigateToAuthenticate -> sendEvent(SavingsConfirmDetailsEvent.NavigateToAuthenticate())

            is SavingsConfirmDetailsAction.Internal.ReceiveAuthenticationResult -> {
                handleSavingsApplyRequest(action.result)
            }

            SavingsConfirmDetailsAction.Internal.ApplySavings -> applySavings()

            is SavingsConfirmDetailsAction.DismissDialog -> dismissDialog()
        }
    }

    /**
     * Dismisses any currently visible dialog by setting the dialog state to null.
     */
    private fun dismissDialog() {
        updateState {
            it.copy(dialogState = null)
        }
    }

    /**
     * Handles the result of the authentication screen. If authentication is successful,
     * it proceeds to submit the savings application.
     */
    private fun handleSavingsApplyRequest(isAuthenticated: Boolean) {
        if (isAuthenticated) {
            viewModelScope.launch {
                sendAction(SavingsConfirmDetailsAction.Internal.ApplySavings)
            }
        } else {
            // Authentication failed or was cancelled, hide any loading state
            updateState { it.copy(showOverlay = false) }
        }
    }

    /**
     * Handles the result of the `applySavings` network call.
     * On success, it navigates to a success status screen after dismissing the loading overlay.
     * On failure, it navigates to a failure status screen after dismissing the loading overlay.
     *
     * @param status The result state of the savings application submission
     */

    private fun applySavings() {
        updateState { it.copy(showOverlay = true) }
        viewModelScope.launch {
            val status = repo.submitSavingAccountApplication(
                payload = getSavingsPayload(),
            )

            when (status) {
                is DataState.Error -> {
                    updateState {
                        it.copy(showOverlay = false)
                    }
                    sendEvent(
                        SavingsConfirmDetailsEvent.NavigateToStatus(
                            eventType = EventType.FAILURE.name,
                            eventDestination = StatusNavigationDestination.PREVIOUS_SCREEN.name,
                            title = getString(Res.string.feature_apply_savings_status_failure),
                            subtitle = status.message.takeIf { it.isNotBlank() } ?: getString(Res.string.feature_apply_savings_status_failure),
                            buttonText = getString(Res.string.feature_apply_savings_status_failure_action),
                        ),
                    )
                }
                DataState.Loading -> {
                    updateState { it.copy(showOverlay = true) }
                }

                is DataState.Success -> {
                    updateState {
                        it.copy(showOverlay = false)
                    }
                    sendEvent(
                        SavingsConfirmDetailsEvent.NavigateToStatus(
                            eventType = EventType.SUCCESS.name,
                            eventDestination = StatusNavigationDestination.SAVINGS_APPLICATION.name,
                            title = getString(Res.string.feature_apply_savings_status_success),
                            subtitle = getString(Res.string.feature_apply_savings_status_success),
                            buttonText = getString(Res.string.feature_apply_savings_status_success_action),
                        ),
                    )
                }
            }
        }
    }

    /**
     * Creates the payload for the savings application submission using the data
     * stored in the ViewModel's state.
     *
     * Handles optional fields gracefully - fields can be empty/null except for:
     * - clientId, productId, submittedOnDate (required)
     * - lockinPeriodFrequencyType (required if frequency is provided)
     */
    private fun getSavingsPayload() = SavingsAccountApplicationPayload(
        clientId = state.clientId.toInt(),
        productId = state.savingsProductId.toInt(),
        submittedOnDate = state.submittedOnDate,
        minRequiredOpeningBalance = state.minOpeningBalance.trim().toDoubleOrNull(),
        lockinPeriodFrequency = state.frequency.trim().toIntOrNull(),
        lockinPeriodFrequencyType = if (state.frequencyTypeId > 0) state.frequencyTypeId.toInt() else null,
        allowOverdraft = state.allowOverdraft,
        locale = "en",
        dateFormat = "dd MMMM yyyy",
        monthDayFormat = "dd MMM",
    )
}

/**
 * Represents the UI state for the confirm details screen.
 */
internal data class SavingsConfirmDetailsState(
    val clientId: Long,
    val savingsProductId: Long,
    val savingsProductName: String,
    val currency: String,
    val minOpeningBalance: String,
    val frequency: String,
    val frequencyTypeName: String,
    val frequencyTypeId: Long,
    val allowOverdraft: Boolean,
    val submittedOnDate: String,
    val details: Map<StringResource, String> = emptyMap(),

    val showOverlay: Boolean = false,
    val dialogState: SavingsConfirmDetailsDialogState? = null,
    val uiState: ScreenUiState? = ScreenUiState.Success,
) {
    /**
     * A sealed interface representing the different types of dialogs that can be
     * shown on the confirm details screen.
     */
    sealed interface SavingsConfirmDetailsDialogState {
        /**
         * Represents a generic error dialog with a message.
         */
        data class Error(val message: StringResource) : SavingsConfirmDetailsDialogState
    }
}

/**
 * A sealed interface representing one-time events that trigger UI side effects
 * for the confirm details screen.
 */
sealed interface SavingsConfirmDetailsEvent {
    /** Navigates back from the current screen. */
    data object NavigateBack : SavingsConfirmDetailsEvent

    /**
     * Navigates to the passcode screen for authentication.
     */
    data class NavigateToAuthenticate(
        val status: String = EventType.SUCCESS.name,
    ) : SavingsConfirmDetailsEvent

    /**
     * Navigates to a generic status screen after an operation completes.
     */
    data class NavigateToStatus(
        val eventType: String,
        val eventDestination: String,
        val title: String,
        val subtitle: String,
        val buttonText: String,
    ) : SavingsConfirmDetailsEvent
}

/**
 * A sealed interface representing user actions or internal events that the
 * ViewModel needs to handle for the confirm details screen.
 */
sealed interface SavingsConfirmDetailsAction {
    /** User action to navigate back. */
    data object OnNavigateBack : SavingsConfirmDetailsAction

    /** User action to navigate to the authentication screen. */
    data object NavigateToAuthenticate : SavingsConfirmDetailsAction

    /** User action to dismiss a dialog. */
    data object DismissDialog : SavingsConfirmDetailsAction

    /**
     * A sealed interface for internal actions, which are not triggered directly by the UI.
     */
    sealed interface Internal : SavingsConfirmDetailsAction {
        /**
         * Receives the result from the authentication screen.
         */
        data class ReceiveAuthenticationResult(val result: Boolean) : Internal

        /**
         * Triggers the actual savings application submission request.
         */
        data object ApplySavings : Internal
    }
}
