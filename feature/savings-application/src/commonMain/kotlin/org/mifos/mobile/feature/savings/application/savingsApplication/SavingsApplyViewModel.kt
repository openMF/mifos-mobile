/*
 * Copyright 2025 Mifos Initiative
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import mifos_mobile.feature.savings_application.generated.resources.Res
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_error_product_empty
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_error_server
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_error_submit_failed
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_error_too_many_attempts
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_unsaved_changes_message
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.HomeRepository
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.core.model.entity.templates.savings.SavingsProduct
import org.mifos.mobile.core.ui.utils.BaseViewModel

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
        savingsApplicationDialogState = SavingsApplicationDialogState.Loading,
        clientId = requireNotNull(userPreferencesRepository.clientId.value),
        applicantName = "",
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
                    mutableStateFlow.update {
                        it.copy(
                            networkStatus = isOnline,
                            savingsApplicationDialogState = if (!isOnline) {
                                SavingsApplicationDialogState.Network
                            } else {
                                null
                            },
                        )
                    }

                    if (isOnline) {
                        fetchClient()
                        fetchSavingsTemplate()
                    }
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
            is SavingsApplicationAction.FieldOfficerChange -> {
                onFieldOfficerChange(action.fieldOfficerId, action.fieldOfficeName)
            }

            is SavingsApplicationAction.SavingsProductChange -> {
                onSavingsProductChange(action.id, action.name)
            }

            is SavingsApplicationAction.NavigateToConfirmDetails -> validateAndSubmit()

            is SavingsApplicationAction.OnNavigateBack -> navigateBack()

            is SavingsApplicationAction.ConfirmNavigation -> sendEvent(SavingsApplicationEvent.NavigateBack)

            is SavingsApplicationAction.RetrySubmit -> resetSubmitAttempts()

            is SavingsApplicationAction.DismissDialog -> dismissDialog()

            is SavingsApplicationAction.GetFieldOfficer -> fetchFieldOfficer()

            is SavingsApplicationAction.Internal.ReceiveSavingsTemplate -> handleSavingsTemplate(action.template)

            is SavingsApplicationAction.Internal.ReceiveSavingsFieldOfficers ->
                handleFieldOfficers(action.template)

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
                updateState { it.copy(savingsApplicationDialogState = SavingsApplicationDialogState.Network) }
            } else {
                fetchClient()
                fetchSavingsTemplate()
            }
        }
    }

    /**
     * Fetches the current client's details, specifically the activation date,
     * from the repository.
     */
    private fun fetchClient() {
        viewModelScope.launch {
            homeRepositoryImpl.currentClient(state.clientId)
                .catch {
                    showErrorDialog(Res.string.feature_apply_savings_error_server)
                }
                .collect { response ->
                    updateState {
                        it.copy(
                            applicantName = response.data?.displayName ?: "",
                        )
                    }
                }
        }
    }

    /**
     * Fetches the savings template data from the repository. The template contains
     * savings product options and currency information.
     */
    private fun fetchSavingsTemplate() {
        viewModelScope.launch {
            savingsAccountRepositoryImpl.getSavingAccountApplicationTemplate(state.clientId)
                .collect { result ->
                    sendAction(SavingsApplicationAction.Internal.ReceiveSavingsTemplate(result))
                }
        }
    }

    /**
     * Fetches field officer options based on the currently selected savings product.
     * Shows a loading overlay while the data is being fetched.
     */
    private fun fetchFieldOfficer() {
        showOverlayLoading()
        viewModelScope.launch {
            savingsAccountRepositoryImpl.getSavingAccountApplicationTemplateByProduct(
                state.clientId,
                state.selectedSavingsProductId,
            )
                .collect { result ->
                    sendAction(SavingsApplicationAction.Internal.ReceiveSavingsFieldOfficers(result))
                }
        }
    }

    /**
     * Sets the dialog state to a full-screen loading spinner.
     */
    private fun showLoading() {
        updateState { it.copy(savingsApplicationDialogState = SavingsApplicationDialogState.Loading) }
    }

    /**
     * Sets the dialog state to an overlay loading spinner.
     */
    private fun showOverlayLoading() {
        updateState { it.copy(savingsApplicationDialogState = SavingsApplicationDialogState.OverlayLoading) }
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
    private fun handleSavingsTemplate(template: DataState<SavingsAccountTemplate?>) {
        when (template) {
            is DataState.Loading -> showLoading()
            is DataState.Success -> {
                val savingsTemplate = template.data
                updateState {
                    it.copy(
                        productOptions = savingsTemplate?.productOptions ?: emptyList(),
                        savingsApplicationDialogState = null,
                    )
                }
            }

            is DataState.Error -> {
                showErrorDialog(Res.string.feature_apply_savings_error_server)
            }
        }
    }

    /**
     * Handles the result of the `fetchFieldOfficer` network call.
     * On success, it maps the field officer options and updates the state.
     * On failure, it shows an error dialog and navigates back.
     *
     * @param template The [DataState] containing the savings template data,
     * including field officer options.
     */
    private fun handleFieldOfficers(template: DataState<SavingsAccountTemplate?>) {
        when (template) {
            is DataState.Loading -> showOverlayLoading()
            is DataState.Success -> {
                val mappedSavingsFieldOfficer: Map<Long, String> = template.data?.fieldOfficerOptions
                    ?.mapNotNull { option ->
                        val id = option.id?.toLong()
                        val name = option.displayName
                        if (id != null && name != null) id to name else null
                    }
                    ?.takeIf { it.isNotEmpty() }
                    ?.toMap() ?: emptyMap()

                updateState {
                    it.copy(
                        savingsProductTemplate = template.data,
                        savingsFieldOfficer = mappedSavingsFieldOfficer,
                        savingsApplicationDialogState = null,
                    )
                }
            }

            is DataState.Error -> {
                showErrorDialog(Res.string.feature_apply_savings_error_server)
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
                selectedFieldOfficer = "",
                savingsProductError = null,
                hasChanges = true,
            )
        }
        viewModelScope.launch {
            sendAction(SavingsApplicationAction.GetFieldOfficer)
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
     * Handles changes to the field officer selection.
     *
     * @param id The new id of the field officer selection.
     * @param name The new name of the field officer selection.
     */
    private fun onFieldOfficerChange(id: Long, name: String) {
        mutableStateFlow.update {
            it.copy(
                selectedFieldOfficerId = id,
                selectedFieldOfficer = name,
                hasChanges = true,
            )
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
        showOverlayLoading()
        viewModelScope.launch {
            try {
                updateState {
                    it.copy(
                        hasChanges = false,
                        savingsApplicationDialogState = null,
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
                    savingsApplicationDialogState = SavingsApplicationDialogState.UnsavedChanges(
                        Res.string.feature_apply_savings_unsaved_changes_message,
                    ),
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
    val applicantName: String,
    val productOptions: List<SavingsProduct> = emptyList(),
    val selectedSavingsProduct: String = "",
    val selectedSavingsProductId: Long = 0,
    val savingsFieldOfficer: Map<Long, String> = emptyMap(),
    val selectedFieldOfficer: String = "",
    val selectedFieldOfficerId: Long = 0,
    val savingsApplicationDialogState: SavingsApplicationDialogState? = null,
    val savingsProductTemplate: SavingsAccountTemplate? = null,
    val savingsProductError: StringResource? = null,
    val hasChanges: Boolean = false,
    val networkStatus: Boolean = false,
) {
    /**
     * A boolean indicating if the entire form is valid for submission.
     * This is based on the absence of errors and non-empty fields.
     */
    val isFormValid: Boolean
        get() = selectedSavingsProductId != 0L &&
            selectedFieldOfficerId != 0L &&
            savingsProductError == null &&
            applicantName.isNotBlank() &&
            selectedSavingsProduct.isNotBlank()

    /**
     * A map of savings product IDs to their names, derived from `productOptions`.
     */
    val productOptionsMap: Map<Long, String> = productOptions.associate { option ->
        val id = option.id.toLong()
        val name = option.name
        id to name
    }

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
    /** Represents an overlay loading state. */
    data object OverlayLoading : SavingsApplicationDialogState

    /** Represents a full-screen loading state. */
    data object Loading : SavingsApplicationDialogState

    /** Represents a network error state. */
    data object Network : SavingsApplicationDialogState

    /**
     * Represents a generic error dialog with a message.
     * @property message The [StringResource] for the error message.
     */
    data class Error(val message: StringResource) : SavingsApplicationDialogState

    /**
     * Represents a dialog to confirm navigation with unsaved changes.
     * @property message The [StringResource] for the confirmation message.
     */
    data class UnsavedChanges(val message: StringResource) : SavingsApplicationDialogState
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

    /**
     * User action when a savings product is selected.
     * @property id The ID of the selected savings product.
     * @property name The name of the selected savings product.
     */
    data class SavingsProductChange(val id: Long, val name: String) : SavingsApplicationAction

    /**
     * User action when the field officer selection changes.
     * @property fieldOfficerId The new value of the field officer id selection.
     * @property fieldOfficeName The new value of the field officer name selection.
     */
    data class FieldOfficerChange(
        val fieldOfficerId: Long,
        val fieldOfficeName: String,
    ) : SavingsApplicationAction

    /** User action to get the field officer options for the selected product. */
    data object GetFieldOfficer : SavingsApplicationAction

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
        data class ReceiveSavingsTemplate(
            val template: DataState<SavingsAccountTemplate?>,
        ) : Internal

        /**
         * An internal action to handle the result of fetching field officer options.
         * @property template The [DataState] containing the savings template data.
         */
        data class ReceiveSavingsFieldOfficers(
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
