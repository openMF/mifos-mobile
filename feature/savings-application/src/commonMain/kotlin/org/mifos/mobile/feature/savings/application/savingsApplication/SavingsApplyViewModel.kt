/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.application.savingsApplication

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import mifos_mobile.feature.savings_application.generated.resources.Res
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_error_product_empty
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_error_server
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_error_submit_failed
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_error_too_many_attempts
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.HomeRepository
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.core.model.entity.templates.savings.SavingsProduct
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * A `ViewModel` for the savings application screen, responsible for handling user input,
 * business logic, and UI state management.
 *
 * It uses a [BaseViewModel] as its foundation to manage [SavingsApplicationState],
 * handle [SavingsApplicationAction] from the UI, and send [SavingsApplicationEvent]
 * to trigger one-time UI side effects.
 *
 * @property userPreferencesRepository Repository for accessing user preferences, specifically the client ID.
 * @property savingsAccountRepositoryImpl Repository for interacting with savings-related data from a remote source.
 * @property homeRepositoryImpl Repository for accessing home-related data, such as client information.
 * @property networkMonitor Monitors the network connectivity status.
 */
@Suppress("CyclomaticComplexMethod", "TooManyFunctions")
internal class SavingsApplyViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val savingsAccountRepositoryImpl: SavingsAccountRepository,
    private val homeRepositoryImpl: HomeRepository,
    private val networkMonitor: NetworkMonitor,
) : BaseViewModel<SavingsApplicationState, SavingsApplicationEvent, SavingsApplicationAction>(
    initialState = SavingsApplicationState(
        clientId = requireNotNull(userPreferencesRepository.clientId.value),
    ),
) {

    init {
        observeNetworkStatus()
    }

    /**
     * Observes the network connectivity status and updates the UI state accordingly.
     * If the network is unavailable, it sets the `networkUnavailable` flag in the state
     * and shows a network-related dialog.
     */
    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    sendAction(SavingsApplicationAction.ReceiveNetworkStatus(isOnline))
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
                    if (
                        current.uiState is ScreenUiState.Loading ||
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
                getClientDataAndTemplate()
            }
        }
    }

    private var validationJob: Job? = null
    private var submitAttempts = 0
    private val maxSubmitAttempts = 5

    /**
     * Handles incoming actions from the UI and dispatches them to the appropriate
     * business logic functions.
     *
     * @param action The [SavingsApplicationAction] to be handled.
     */
    override fun handleAction(action: SavingsApplicationAction) {
        when (action) {
            is SavingsApplicationAction.SavingsProductChange -> {
                onSavingsProductChange(action.id, action.name)
            }

            is SavingsApplicationAction.NavigateToConfirmDetails -> validateAndSubmit()

            is SavingsApplicationAction.OnNavigateBack -> navigateBack()

            is SavingsApplicationAction.ConfirmNavigation -> sendEvent(SavingsApplicationEvent.NavigateBack)

            is SavingsApplicationAction.RetrySubmit -> resetSubmitAttempts()

            is SavingsApplicationAction.DismissDialog -> dismissDialog()

            is SavingsApplicationAction.ReceiveNetworkStatus -> handleNetworkStatus(action.isOnline)

            is SavingsApplicationAction.Internal.ReceiveClientAndTemplateResult ->
                handleClientAndSavingsTemplate(action.client, action.template)

            SavingsApplicationAction.Retry -> retry()
        }
    }

    /**
     * A helper function to update the mutable state flow.
     *
     * @param update A lambda function that takes the current state and returns a new state.
     */
    private fun updateState(update: (SavingsApplicationState) -> SavingsApplicationState) {
        mutableStateFlow.update(update)
    }

    /**
     * Retries the data fetching process. If the network is unavailable, it shows
     * a network error dialog. Otherwise, it triggers the `fetchSavingsTemplate` `fetchClient`,
     */
    private fun retry() {
        viewModelScope.launch {
            if (!state.networkStatus) {
                updateState { it.copy(uiState = ScreenUiState.Network) }
            } else {
                getClientDataAndTemplate()
            }
        }
    }

    /**
     * Fetches client data, a generic saving template, and a product-specific loan purpose template
     * from the repositories.
     * The results are combined and handled in a single flow to manage loading and error states.
     */
    private fun getClientDataAndTemplate() {
        showLoading()
        viewModelScope.launch {
            combine(
                homeRepositoryImpl.currentClient(state.clientId),
                savingsAccountRepositoryImpl.getSavingAccountApplicationTemplate(state.clientId),
            ) { client, template ->
                client to template
            }
                .catch { throwable ->

                    updateState {
                        it.copy(
                            uiState = if (throwable.cause is IOException) {
                                ScreenUiState.Network
                            } else {
                                ScreenUiState.Error(Res.string.feature_apply_savings_error_server)
                            },
                        )
                    }
                }
                .collect { (client, template) ->
                    sendAction(
                        SavingsApplicationAction.Internal.ReceiveClientAndTemplateResult(
                            client,
                            template,
                        ),
                    )
                }
        }
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
    private fun showErrorDialog(error: StringResource) {
        updateState { it.copy(savingsApplicationDialogState = SavingsApplicationDialogState.Error(error)) }
    }

    /**
     * Handles the result of the `fetchSavingsTemplate` network call.
     * Updates the state with product options and currency on success,
     * or displays an error and navigates back on failure.
     *
     * @param template The [DataState] containing the savings template data.
     */
    private fun handleClientAndSavingsTemplate(
        client: DataState<Client>,
        template: DataState<SavingsAccountTemplate?>,
    ) {
        when {
            listOf(client, template).any { it is DataState.Loading } -> {
                showLoading()
            }

            client is DataState.Success && template is DataState.Success -> {
                updateState {
                    it.copy(
                        applicantName = client.data.displayName ?: "",
                        productOptions = template.data?.productOptions ?: emptyList(),
                        uiState = ScreenUiState.Success,
                    )
                }
            }

            else -> updateState {
                it.copy(
                    uiState = ScreenUiState.Error(Res.string.feature_apply_savings_error_server),
                )
            }
        }
    }

    /**
     * Validates that a savings product has been selected.
     *
     * @param savingsProduct The name of the selected savings product.
     * @return A [ValidationResult] indicating success or an error if the field is empty.
     */
    private fun validateSavingsProduct(savingsProduct: String): ValidationResult = when {
        savingsProduct.isEmpty() -> ValidationResult.Error(Res.string.feature_apply_savings_error_product_empty)
        else -> ValidationResult.Success
    }

    /**
     * Handles changes to the selected savings product.
     * It updates the state, fetches the corresponding field officer options,
     * and debounces validation.
     *
     * @param id The ID of the selected savings product.
     * @param name The name of the selected savings product.
     */
    private fun onSavingsProductChange(id: Long, name: String) {
        mutableStateFlow.update {
            it.copy(
                selectedSavingsProduct = name,
                selectedSavingsProductId = id,
                savingsProductError = null,
                hasChanges = true,
            )
        }

        debounceValidation {
            val result = validateSavingsProduct(name)
            mutableStateFlow.update {
                it.copy(
                    savingsProductError = if (result is ValidationResult.Error) result.message else null,
                )
            }
        }
    }

    /**
     * Validates all form fields before submitting. If validation is successful,
     * it proceeds to `handleSubmit`. If not, it updates the state with errors
     * and increments the submit attempt counter.
     *
     * If the maximum number of attempts is reached, it displays an error dialog.
     */
    private fun validateAndSubmit() {
        if (submitAttempts >= maxSubmitAttempts) {
            mutableStateFlow.update {
                it.copy(
                    savingsApplicationDialogState = SavingsApplicationDialogState.Error(
                        Res.string.feature_apply_savings_error_too_many_attempts,
                    ),
                )
            }
            return
        }
        val savingsProductResult = validateSavingsProduct(state.selectedSavingsProduct)

        mutableStateFlow.update {
            it.copy(
                savingsProductError = if (savingsProductResult is ValidationResult.Error) {
                    savingsProductResult.message
                } else {
                    null
                },
            )
        }

        val isValid = listOf(
            savingsProductResult,
        ).all { it is ValidationResult.Success }
        if (isValid) {
            handleSubmit()
        } else {
            submitAttempts++
        }
    }

    /**
     * Handles the successful submission of the savings application.
     * It shows a loading overlay, resets the submit attempts,
     * and sends an event to navigate to the confirmation screen.
     */
    private fun handleSubmit() {
        viewModelScope.launch {
            try {
                updateState {
                    it.copy(
                        hasChanges = false,
                        savingsApplicationDialogState = null,
                        showOverlay = false,
                    )
                }
                submitAttempts = 0
                sendEvent(SavingsApplicationEvent.NavigateToFillDetailsScreen)
            } catch (e: Exception) {
                submitAttempts++
                showErrorDialog(Res.string.feature_apply_savings_error_submit_failed)
            }
        }
    }

    /**
     * A utility function to debounce validation logic. It cancels any
     * previous validation job and starts a new one after a short delay.
     *
     * @param validation The suspending lambda function containing the validation logic.
     */
    private fun debounceValidation(validation: suspend () -> Unit) {
        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            delay(300)
            validation()
        }
    }

    /**
     * Dismisses any currently visible dialog by setting the dialog state to null.
     */
    private fun dismissDialog() {
        mutableStateFlow.update {
            it.copy(savingsApplicationDialogState = null)
        }
    }

    /**
     * Handles the back navigation. If there are unsaved changes, it shows a
     * confirmation dialog. Otherwise, it sends an event to navigate back.
     */
    private fun navigateBack() {
        if (state.hasChanges) {
            mutableStateFlow.update {
                it.copy(
                    savingsApplicationDialogState = SavingsApplicationDialogState.UnsavedChanges,
                )
            }
        } else {
            sendEvent(SavingsApplicationEvent.NavigateBack)
        }
    }

    /**
     * Resets the submit attempts counter.
     */
    private fun resetSubmitAttempts() {
        submitAttempts = 0
    }

    /**
     * Called when the ViewModel is no longer used and will be destroyed.
     * It cancels the validation job to prevent memory leaks.
     */
    override fun onCleared() {
        super.onCleared()
        validationJob?.cancel()
    }
}

/**
 * Represents the UI state for the savings application screen.
 *
 * @property clientId The ID of the current client.
 * @property applicantName The name of the applicant.
 * @property productOptions A list of available savings product options.
 * @property selectedSavingsProduct The name of the currently selected savings product.
 * @property selectedSavingsProductId The ID of the currently selected savings product.
 * @property savingsFieldOfficer A map of field officer IDs to their names, based on the selected product.
 * @property selectedFieldOfficer The name of the currently selected field officer.
 * @property selectedFieldOfficerId The ID of the currently selected field officer.
 * @property savingsApplicationDialogState The state of any dialog to be shown on the screen.
 * @property savingsProductTemplate The full savings template object for the selected product.
 * @property savingsProductError A resource string for an error message for the savings product field, or null.
 * @property hasChanges A boolean indicating if there are unsaved changes.
 * @property networkStatus A boolean indicating if the network is unavailable.
 */
@OptIn(ExperimentalMaterial3Api::class)
internal data class SavingsApplicationState(
    val clientId: Long,
    val applicantName: String = "",
    val productOptions: List<SavingsProduct> = emptyList(),
    val selectedSavingsProduct: String = "",
    val selectedSavingsProductId: Long = 0,
    val selectedFieldOfficerId: Long = 0,
    val savingsApplicationDialogState: SavingsApplicationDialogState? = null,
    val savingsProductTemplate: SavingsAccountTemplate? = null,
    val savingsProductError: StringResource? = null,
    val hasChanges: Boolean = false,

    val networkStatus: Boolean = false,
    val uiState: ScreenUiState? = ScreenUiState.Loading,
    val showOverlay: Boolean = false,
) {
    /**
     * A map of savings product IDs to their names, derived from `productOptions`.
     */
    val productOptionsMap: Map<Long, String> = productOptions.associate { option ->
        val id = option.id.toLong()
        val name = option.name
        id to name
    }

    @OptIn(ExperimentalTime::class)
    val submittedOnDate: String
        get() {
            val todayMillis = Clock.System.now().toEpochMilliseconds()
            println(todayMillis)
            return DateHelper.getDateMonthYearString(todayMillis)
        }
}

/**
 * A sealed interface representing the different types of dialogs that can be
 * shown on the savings application screen.
 */
internal sealed interface SavingsApplicationDialogState {
    /**
     * Represents a generic error dialog with a message.
     * @property message The [StringResource] for the error message.
     */
    data class Error(val message: StringResource) : SavingsApplicationDialogState

    /**
     * Represents a dialog to confirm navigation with unsaved changes.
     */
    data object UnsavedChanges : SavingsApplicationDialogState
}

/**
 * A sealed interface representing one-time events that trigger UI side effects.
 */
internal sealed interface SavingsApplicationEvent {
    /** Navigates back from the current screen. */
    data object NavigateBack : SavingsApplicationEvent

    /** Navigates to the Filling Application details screen. */
    data object NavigateToFillDetailsScreen : SavingsApplicationEvent
}

/**
 * A sealed interface representing user actions or internal events that the
 * ViewModel needs to handle.
 */
internal sealed interface SavingsApplicationAction {
    /** User action to navigate back. */
    data object OnNavigateBack : SavingsApplicationAction

    /** User action to dismiss a dialog. */
    data object DismissDialog : SavingsApplicationAction

    /** User action to retry a failed operation. */
    data object Retry : SavingsApplicationAction

    /** User action to confirm navigation (e.g., dismissing an unsaved changes dialog). */
    data object ConfirmNavigation : SavingsApplicationAction

    /** Action to observe network status */
    data class ReceiveNetworkStatus(val isOnline: Boolean) : SavingsApplicationAction

    /**
     * User action when a savings product is selected.
     * @property id The ID of the selected savings product.
     * @property name The name of the selected savings product.
     */
    data class SavingsProductChange(val id: Long, val name: String) : SavingsApplicationAction

    /** User action to navigate to the confirm details screen, triggering form validation. */
    data object NavigateToConfirmDetails : SavingsApplicationAction

    /** User action to retry a form submission after an error. */
    data object RetrySubmit : SavingsApplicationAction

    /**
     * A sealed interface for internal actions, which are not triggered directly by the UI.
     */
    sealed interface Internal : SavingsApplicationAction {

        /**
         * An internal action to handle the result of fetching a savings template.
         * @property template The [DataState] containing the savings template data.
         */
        data class ReceiveClientAndTemplateResult(
            val client: DataState<Client>,
            val template: DataState<SavingsAccountTemplate?>,
        ) : Internal
    }
}

/**
 * A sealed class representing the result of a validation process.
 */
sealed class ValidationResult {
    /** Indicates that the validation was successful. */
    data object Success : ValidationResult()

    /**
     * Indicates that the validation failed.
     * @property message The [StringResource] for the error message.
     */
    data class Error(val message: StringResource) : ValidationResult()
}
