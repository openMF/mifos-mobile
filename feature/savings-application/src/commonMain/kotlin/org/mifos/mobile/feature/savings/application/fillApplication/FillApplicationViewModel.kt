/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.savings.application.fillApplication

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import mifos_mobile.feature.savings_application.generated.resources.Res
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_error_amount_too_large
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_error_amount_too_small
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_error_frequency_invalid
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_error_frequency_required
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_error_server
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_error_submit_failed
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_error_too_many_attempts
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_status_failure
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_status_failure_action
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_status_failure_tip
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_status_success
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_status_success_action
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_status_success_tip
import mifos_mobile.feature.savings_application.generated.resources.feature_apply_savings_unsaved_changes_message
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.EventType
import org.mifos.mobile.core.model.entity.accounts.savings.Currency
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.core.model.entity.templates.savings.SavingsOptions
import org.mifos.mobile.core.ui.utils.AmountValidationResult
import org.mifos.mobile.core.ui.utils.AuthResult
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ResultNavigator
import org.mifos.mobile.core.ui.utils.ValidationHelper
import org.mifos.mobile.core.ui.utils.observe
import org.mifos.mobile.feature.savings.application.navigation.SavingsApplicationNavGraph
import kotlin.String
import org.mifos.mobile.core.model.entity.Currency as ModelCurrency

private const val DEFAULT_DECIMAL_PLACES = 2
private const val DEFAULT_IN_MULTIPLES_OF = 1.0

/**
 * `ViewModel` for the savings account application screen.
 *
 * This ViewModel is responsible for handling user input, validating fields,
 * and submitting a savings account application. It uses a [BaseViewModel] as
 * its foundation to manage [SavingsApplicationState], handle [SavingsApplicationAction]
 * from the UI, and send [SavingsApplicationEvent] to trigger one-time UI side effects.
 *
 * @property userPreferencesRepository Repository for accessing user preferences, specifically the client ID.
 * @property savingsAccountRepositorImpl Repository for interacting with savings account-related data.
 * @property networkMonitor Monitors the network connectivity status.
 * @property resultNavigator A navigator to observe and receive results from other screens, like authentication.
 * @property savedStateHandle A handle to saved state data, used to retrieve navigation arguments.
 */
@Suppress("CyclomaticComplexMethod", "TooManyFunctions")
internal class SavingsFillApplicationViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val savingsAccountRepositorImpl: SavingsAccountRepository,
    private val networkMonitor: NetworkMonitor,
    private val resultNavigator: ResultNavigator,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<SavingsApplicationState, SavingsApplicationEvent, SavingsApplicationAction>(
    initialState = run {
        val route = savedStateHandle.toRoute<SavingsFillApplicationRoute>()
        SavingsApplicationState(
            dialogState = SavingsApplicationDialogState.Loading,
            clientId = requireNotNull(userPreferencesRepository.clientId.value),
            applicantName = "",
            currency = Currency(),
            savingsProductId = route.savingsProductId,
            fieldOfficerName = route.fieldOfficerName,
        )
    },
) {

    private var validationJob: Job? = null
    private var submitAttempts = 0
    private val maxSubmitAttempts = 5

    init {
        observeNetworkStatus()
        observeAuthResult()
    }

    /**
     * Handles incoming actions from the UI and dispatches them to the appropriate
     * business logic functions.
     *
     * @param action The [SavingsApplicationAction] to be handled.
     */
    override fun handleAction(action: SavingsApplicationAction) {
        when (action) {
            is SavingsApplicationAction.MinimumOpeningBalanceChange -> onMinimumOpeningBalanceChange(action.balance)

            is SavingsApplicationAction.Retry -> retry()

            is SavingsApplicationAction.FrequencyChange -> onFrequencyChange(action.frequency)

            is SavingsApplicationAction.FrequencyTypeChange -> onFrequencyTypeChange(action.id, action.value)

            is SavingsApplicationAction.Internal.ReceiveSavingsTemplate -> handleSavingsTemplateResult(action.template)

            is SavingsApplicationAction.OverDraftChange -> onOverDraftChange()

            is SavingsApplicationAction.OnChecked -> {
                mutableStateFlow.update { it.copy(checked = action.checked) }
            }

            is SavingsApplicationAction.RequestSavingsAccount -> submitSavingsAccountApplication()

            is SavingsApplicationAction.Internal.ReceiveSavingsApplicationResult -> {
                viewModelScope.launch { handleSavingsApplicationResult(action.result) }
            }

            is SavingsApplicationAction.Internal.ReceiveAuthenticationResult -> handleSavingsApplyRequest(action.result)

            is SavingsApplicationAction.NavigateToAuthentication -> validateAndSubmit()

            is SavingsApplicationAction.OnNavigateBack -> navigateBack()

            is SavingsApplicationAction.ConfirmNavigation -> sendEvent(SavingsApplicationEvent.NavigateBack)

            is SavingsApplicationAction.RetrySubmit -> resetSubmitAttempts()

            is SavingsApplicationAction.DismissDialog -> dismissDialog()
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

    /*
     * Functions related to Data Observation and Fetching
     */

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
                            dialogState = if (!isOnline) {
                                SavingsApplicationDialogState.Network
                            } else {
                                null
                            },
                        )
                    }
                    if (isOnline) {
                        fetchSavingsTemplateByProduct()
                    }
                }
        }
    }

    /**
     * Observes the result from the authentication screen.
     * If the authentication is successful, it triggers the loan application flow.
     */
    private fun observeAuthResult() {
        viewModelScope.launch {
            resultNavigator.observe<AuthResult>()
                .collect { result ->
                    sendAction(SavingsApplicationAction.Internal.ReceiveAuthenticationResult(result.success))
                }
        }
    }

    /**
     * Retries the data fetching process. If the network is unavailable, it shows
     * a network error dialog. Otherwise, it triggers the `fetchSavingsTemplateByProduct` function.
     */
    private fun retry() {
        viewModelScope.launch {
            if (!state.networkStatus) {
                updateState { it.copy(dialogState = SavingsApplicationDialogState.Network) }
            } else {
                fetchSavingsTemplateByProduct()
            }
        }
    }

    /**
     * Fetches the savings account template data for a specific product from the repository.
     */
    private fun fetchSavingsTemplateByProduct() {
        viewModelScope.launch {
            savingsAccountRepositorImpl
                .getSavingAccountApplicationTemplateByProduct(state.clientId, state.savingsProductId)
                .collect { result ->
                    sendAction(SavingsApplicationAction.Internal.ReceiveSavingsTemplate(result))
                }
        }
    }

    /*
     * Functions related to UI State and Dialogs
     */

    /**
     * Sets the dialog state to a full-screen loading spinner.
     */
    private fun showLoading() {
        updateState { it.copy(dialogState = SavingsApplicationDialogState.Loading) }
    }

    /**
     * Sets the dialog state to an overlay loading spinner.
     */
    private fun showOverlayLoading() {
        updateState { it.copy(dialogState = SavingsApplicationDialogState.OverlayLoading) }
    }

    /**
     * Displays an error dialog with a given message.
     *
     * @param error The [StringResource] for the error message to display.
     */
    private fun showErrorDialog(error: StringResource) {
        updateState { it.copy(dialogState = SavingsApplicationDialogState.Error(error)) }
    }

    /**
     * Dismisses any currently visible dialog by setting the dialog state to null.
     */
    private fun dismissDialog() {
        mutableStateFlow.update {
            it.copy(dialogState = null)
        }
    }

    /*
     * Functions related to User Input and Validation
     */

    /**
     * Handles changes to the minimum opening balance field.
     * It updates the state and debounces validation.
     *
     * @param newValue The new value of the minimum opening balance field.
     */
    private fun onMinimumOpeningBalanceChange(newValue: String) {
        mutableStateFlow.update {
            it.copy(
                minOpeningBalance = newValue,
                minOpeningBalanceError = null,
                hasChanges = true,
            )
        }
        debounceValidation {
            val result =
                validateMinimumOpeningBalanceChange(state.minOpeningBalance, state.currency.toModelCurrency())
            mutableStateFlow.update {
                it.copy(
                    minOpeningBalanceError = if (result is ValidationResult.Error) result.message else null,
                )
            }
        }
    }

    /**
     * Validates the minimum opening balance amount.
     *
     * @param amount The minimum opening balance amount as a string.
     * @param currency The currency details for validation.
     * @return A [ValidationResult] indicating success or a specific error.
     */
    private fun validateMinimumOpeningBalanceChange(
        amount: String,
        currency: ModelCurrency,
    ): ValidationResult {
        return when (val result = ValidationHelper.validateAmountWithDetails(amount, currency)) {
            is AmountValidationResult.Valid -> {
                val value = result.normalizedAmount
                return if (value in 1000.0..10000.0) {
                    mutableStateFlow.update {
                        it.copy(minOpeningBalance = value.toString(), minOpeningBalanceError = null)
                    }
                    ValidationResult.Success
                } else {
                    ValidationResult.Error(
                        if (value < 1000.0) {
                            Res.string.feature_apply_savings_error_amount_too_small
                        } else {
                            Res.string.feature_apply_savings_error_amount_too_large
                        },
                    )
                }
            }
            is AmountValidationResult.Invalid -> ValidationResult.Error(result.errorResource)
        }
    }

    /**
     * Handles changes to the frequency field.
     * It updates the state and debounces validation.
     *
     * @param newValue The new value of the frequency field.
     */
    private fun onFrequencyChange(newValue: String) {
        mutableStateFlow.update {
            it.copy(
                frequency = newValue,
                frequencyError = null,
                hasChanges = true,
            )
        }
        debounceValidation {
            val result = validateFrequencyChange(state.frequency)
            mutableStateFlow.update {
                it.copy(
                    frequencyError = if (result is ValidationResult.Error) result.message else null,
                )
            }
        }
    }

    /**
     * Validates the frequency field.
     *
     * @param newValue The new value of the frequency field.
     * @return A [ValidationResult] indicating success or a specific error.
     */
    private fun validateFrequencyChange(newValue: String): ValidationResult = when {
        newValue.isBlank() -> {
            ValidationResult.Error(Res.string.feature_apply_savings_error_frequency_required)
        }
        newValue.toIntOrNull() == null -> {
            ValidationResult.Error(Res.string.feature_apply_savings_error_frequency_invalid)
        }
        else -> {
            mutableStateFlow.update {
                it.copy(frequency = newValue, frequencyError = null)
            }
            ValidationResult.Success
        }
    }

    /**
     * Updates the selected frequency type in the state.
     *
     * @param id The ID of the selected frequency type.
     * @param value The name of the selected frequency type.
     */
    private fun onFrequencyTypeChange(id: Long, value: String) {
        mutableStateFlow.update {
            it.copy(
                selectedFrequencyTypeName = value,
                selectedFrequencyTypeId = id,
                hasChanges = true,
            )
        }
    }

    /**
     * Toggles the state of the "allow overdraft" checkbox.
     */
    private fun onOverDraftChange() {
        mutableStateFlow.update {
            it.copy(
                allowOverDraft = !it.allowOverDraft,
                hasChanges = true,
            )
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

    /*
     * Functions related to Form Submission
     */

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
                    dialogState = SavingsApplicationDialogState.Error(
                        Res.string.feature_apply_savings_error_too_many_attempts,
                    ),
                )
            }
            return
        }
        val minOpeningBalanceResult =
            validateMinimumOpeningBalanceChange(state.minOpeningBalance, state.currency.toModelCurrency())
        val frequencyResult = validateFrequencyChange(state.frequency)

        mutableStateFlow.update {
            it.copy(
                minOpeningBalanceError = if (minOpeningBalanceResult is ValidationResult.Error) {
                    minOpeningBalanceResult.message
                } else {
                    null
                },
                frequencyError = if (frequencyResult is ValidationResult.Error) frequencyResult.message else null,
            )
        }

        val isValid = listOf(
            minOpeningBalanceResult,
            frequencyResult,
        ).all { it is ValidationResult.Success }
        if (isValid) {
            handleSubmit()
        } else {
            submitAttempts++
        }
    }

    /**
     * Handles the successful submission of the savings account application.
     * It shows a loading overlay, resets the submit attempts,
     * and sends an event to navigate to the authentication screen.
     */
    private fun handleSubmit() {
        showOverlayLoading()
        viewModelScope.launch {
            try {
                updateState {
                    it.copy(
                        hasChanges = false,
                        dialogState = null,
                    )
                }
                submitAttempts = 0
                sendEvent(SavingsApplicationEvent.NavigateToAuthentication)
            } catch (e: Exception) {
                submitAttempts++
                showErrorDialog(Res.string.feature_apply_savings_error_submit_failed)
            }
        }
    }

    /**
     * Handles the result of the authentication screen. If authentication is successful,
     * it proceeds to submit the savings account application.
     *
     * @param isAuthenticated A boolean indicating if the user was successfully authenticated.
     */
    private fun handleSavingsApplyRequest(isAuthenticated: Boolean) {
        if (isAuthenticated) {
            viewModelScope.launch {
                sendAction(SavingsApplicationAction.RequestSavingsAccount)
            }
        }
    }

    /**
     * Submits the savings account application to the repository.
     */
    private fun submitSavingsAccountApplication() {
        updateState {
            it.copy(dialogState = SavingsApplicationDialogState.Loading)
        }
        viewModelScope.launch {
            val response = savingsAccountRepositorImpl.submitSavingAccountApplication(
                payload = state.toSavingsApplicationPayload(),
            )
            sendAction(SavingsApplicationAction.Internal.ReceiveSavingsApplicationResult(response))
        }
    }

    /**
     * Handles the result of the `fetchSavingsTemplateByProduct` network call.
     *
     * @param template The [DataState] containing the savings account template data.
     */
    private fun handleSavingsTemplateResult(template: DataState<SavingsAccountTemplate?>) {
        when (template) {
            is DataState.Loading -> showLoading()
            is DataState.Error -> {
                showErrorDialog(Res.string.feature_apply_savings_error_server)
                updateState { it.copy(dialogState = null) }
            }
            is DataState.Success -> {
                val savingsTemplate = template.data ?: return
                updateState {
                    it.copy(
                        dialogState = null,
                        isOverDraftAllowed = savingsTemplate.allowOverdraft ?: false,
                        applicantName = savingsTemplate.clientName ?: "",
                        currency = savingsTemplate.currency ?: Currency(),
                        frequencyType = savingsTemplate.lockinPeriodFrequencyTypeOptions,
                    )
                }
            }
        }
    }

    /**
     * Handles the result of the savings account application submission.
     *
     * @param response The [DataState] containing the result of the submission.
     */
    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    private suspend fun handleSavingsApplicationResult(response: DataState<String>) {
        dismissDialog()
        when (response) {
            is DataState.Error -> {
                sendEvent(
                    SavingsApplicationEvent.NavigateToStatus(
                        eventType = EventType.FAILURE.name,
                        eventDestination = SavingsApplicationNavGraph::class.serializer().descriptor.serialName,
                        title = getString(Res.string.feature_apply_savings_status_failure),
                        subtitle = getString(
                            Res.string.feature_apply_savings_status_failure_tip,
                            state.fieldOfficerName,
                        ),
                        buttonText = getString(Res.string.feature_apply_savings_status_failure_action),
                    ),
                )
            }
            DataState.Loading -> showOverlayLoading()
            is DataState.Success -> {
                sendEvent(
                    SavingsApplicationEvent.NavigateToStatus(
                        eventType = EventType.SUCCESS.name,
                        eventDestination = SavingsApplicationNavGraph::class.serializer().descriptor.serialName,
                        title = getString(Res.string.feature_apply_savings_status_success),
                        subtitle = getString(
                            Res.string.feature_apply_savings_status_success_tip,
                            state.fieldOfficerName,
                        ),
                        buttonText = getString(Res.string.feature_apply_savings_status_success_action),
                    ),
                )
            }
        }
    }

    /*
     * Functions related to Navigation and Lifecycle
     */

    /**
     * Handles the back navigation. If there are unsaved changes, it shows a
     * confirmation dialog. Otherwise, it sends an event to navigate back.
     */
    private fun navigateBack() {
        if (state.hasChanges) {
            mutableStateFlow.update {
                it.copy(
                    dialogState = SavingsApplicationDialogState.UnsavedChanges(
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

    /*
     * Utility Functions
     */

    /**
     * Converts the current [SavingsApplicationState] into a [SavingsAccountApplicationPayload]
     * for submission to the API.
     *
     * @return The payload object containing all necessary data for the savings account application.
     */
    // TODO add other backend-required fields if API needs them
    private fun SavingsApplicationState.toSavingsApplicationPayload(): SavingsAccountApplicationPayload {
        return SavingsAccountApplicationPayload(
            clientId = state.clientId.toInt(),
            productId = savingsProductId.toInt(),
            submittedOnDate = state.currentDate,
            minRequiredOpeningBalance = minOpeningBalance.toDoubleOrNull() ?: 0.0,
            lockinPeriodFrequency = state.frequency.toInt(),
            lockinPeriodFrequencyType = selectedFrequencyTypeId.toInt(),
            allowOverdraft = if (isOverDraftAllowed) checked else isOverDraftAllowed,
            locale = "en",
            dateFormat = "dd MMMM yyyy",
            monthDayFormat = "dd MMM",
        )
    }
}

/**
 * Represents the UI state for the savings account application screen.
 *
 * @property clientId The ID of the current client.
 * @property applicantName The name of the applicant.
 * @property savingsProductId The ID of the currently selected savings product.
 * @property fieldOfficerName The name of the field officer assigned to this application.
 * @property currency The currency details for the savings account.
 * @property minOpeningBalance The minimum opening balance entered by the user.
 * @property frequency The lock-in period frequency entered by the user.
 * @property frequencyType A list of lock-in period frequency type options.
 * @property selectedFrequencyTypeName The name of the selected frequency type.
 * @property selectedFrequencyTypeId The ID of the selected frequency type.
 * @property allowOverDraft A boolean indicating if overdraft is allowed for this product.
 * @property checked A boolean indicating if the user has checked the "allow overdraft" checkbox.
 * @property isOverDraftAllowed A boolean indicating if overdraft is an option for the product.
 * @property minOpeningBalanceError A resource string for an
 * error message for the minimum opening balance field, or null.
 * @property frequencyError A resource string for an error message for the frequency field, or null.
 * @property hasChanges A boolean indicating if there are unsaved changes.
 * @property networkStatus A boolean indicating the network status.
 * @property dialogState The state of any dialog to be shown on the screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
internal data class SavingsApplicationState(
    val clientId: Long,
    val applicantName: String,
    val savingsProductId: Long,
    val fieldOfficerName: String,
    val currency: Currency,
    val minOpeningBalance: String = "",
    val frequency: String = "",
    val frequencyType: List<SavingsOptions> = emptyList(),
    val selectedFrequencyTypeName: String = "",
    val selectedFrequencyTypeId: Long = 0,
    val allowOverDraft: Boolean = false,
    val checked: Boolean = false,
    val isOverDraftAllowed: Boolean = false,
    val minOpeningBalanceError: StringResource? = null,
    val frequencyError: StringResource? = null,
    val hasChanges: Boolean = false,
    val networkStatus: Boolean = false,
    val dialogState: SavingsApplicationDialogState? = null,
) {
    /**
     * The current date as a formatted string.
     */
    val currentDate: String
        get() = DateHelper.getDateMonthYearString(Clock.System.now().toEpochMilliseconds())

    /**
     * A map of savings product IDs to their names, derived from `frequencyType`.
     */
    val getFrequencyTypeMap: Map<Long, String> = frequencyType.associate { option ->
        val id = option.id.toLong()
        val name = option.value
        id to name
    }

    /**
     * A boolean indicating if the entire form is valid for submission.
     */
    val isFormValid: Boolean
        get() = minOpeningBalance.isNotBlank() &&
            selectedFrequencyTypeName.isNotBlank() &&
            minOpeningBalanceError == null &&
            frequencyError == null
}

/**
 * A sealed interface representing the different types of dialogs that can be
 * shown on the savings account application screen.
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

    /** Navigates to the authentication screen. */
    data object NavigateToAuthentication : SavingsApplicationEvent

    /**
     * Navigates to a generic status screen after a submission operation.
     * @property eventType The status type (SUCCESS or FAILURE).
     * @property eventDestination The route to return to after the status screen.
     * @property title The title to show on the status screen.
     * @property subtitle A subtitle for further information.
     * @property buttonText The text for the action button on the status screen.
     */
    data class NavigateToStatus(
        val eventType: String,
        val eventDestination: String,
        val title: String,
        val subtitle: String,
        val buttonText: String,
    ) : SavingsApplicationEvent
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

    /** User action to navigate to the authentication screen. */
    data object NavigateToAuthentication : SavingsApplicationAction

    /**
     * User action when the terms and conditions checkbox state changes.
     * @property checked The new state of the checkbox.
     */
    data class OnChecked(val checked: Boolean) : SavingsApplicationAction

    /**
     * User action when the minimum opening balance field changes.
     * @property balance The new value of the balance field.
     */
    data class MinimumOpeningBalanceChange(val balance: String) : SavingsApplicationAction

    /**
     * User action when the frequency field changes.
     * @property frequency The new value of the frequency field.
     */
    data class FrequencyChange(val frequency: String) : SavingsApplicationAction

    /**
     * User action when the frequency type changes.
     * @property id The ID of the selected frequency type.
     * @property value The name of the selected frequency type.
     */
    data class FrequencyTypeChange(
        val id: Long,
        val value: String,
    ) : SavingsApplicationAction

    /** User action to toggle the overdraft setting. */
    data object OverDraftChange : SavingsApplicationAction

    /** User action to retry a form submission after an error. */
    data object RetrySubmit : SavingsApplicationAction

    /** User action to submit the savings account application. */
    data object RequestSavingsAccount : SavingsApplicationAction

    /**
     * A sealed interface for internal actions, which are not triggered directly by the UI.
     */
    sealed interface Internal : SavingsApplicationAction {

        /**
         * An internal action to handle the result of fetching a savings account template.
         * @property template The [DataState] containing the savings account template data.
         */
        data class ReceiveSavingsTemplate(val template: DataState<SavingsAccountTemplate?>) : Internal

        /**
         * Receives the result from the authentication screen.
         *
         * @property result `true` if the user was authenticated.
         */
        data class ReceiveAuthenticationResult(val result: Boolean) : Internal

        /**
         * Receives the result of the savings account application submission.
         * @property result The [DataState] containing the result.
         */
        data class ReceiveSavingsApplicationResult(val result: DataState<String>) : Internal
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

/**
 * Maps the core [Currency] model from the loan template to the internal [ModelCurrency] entity.
 *
 * This is useful when converting API or network models into local models used throughout
 * the application domain layer.
 *
 * @receiver [Currency] from the loan template (external source).
 * @return [ModelCurrency] used within the app's domain/entity layer.
 */
fun Currency.toModelCurrency(): ModelCurrency {
    return ModelCurrency(
        code = code,
        name = name,
        decimalPlaces = decimalPlaces ?: DEFAULT_DECIMAL_PLACES,
        inMultiplesOf = inMultiplesOf?.toDouble() ?: DEFAULT_IN_MULTIPLES_OF,
        displaySymbol = displaySymbol,
        nameCode = nameCode,
        displayLabel = displayLabel,
    )
}
