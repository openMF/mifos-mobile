/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.loanApplication

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import mifos_mobile.feature.loan_application.generated.resources.Res
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_error_amount_too_large
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_error_amount_too_small
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_error_date_empty
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_error_name_empty
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_error_name_invalid_format
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_error_name_too_long
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_error_name_too_short
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_error_server
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_error_submit_failed
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_error_too_many_attempts
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_unsaved_changes_message
import okio.IOException
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.HomeRepository
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.model.entity.templates.loans.Currency
import org.mifos.mobile.core.model.entity.templates.loans.LoanTemplate
import org.mifos.mobile.core.ui.utils.AmountValidationResult
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.core.ui.utils.ValidationHelper
import org.mifos.mobile.core.model.entity.Currency as ModelCurrency

/**
 * Fallback map for loan purposes if the network call fails or returns empty.
 */
internal val fallbackLoanPurposeMap = mapOf(
    1L to "Home Loan",
    2L to "Education Loan",
    3L to "Vehicle Loan",
    4L to "Business Loan",
    5L to "Medical Emergency",
)

/**
 * A `ViewModel` for the loan application screen, responsible for handling user input,
 * business logic, and UI state management.
 *
 * It uses a [BaseViewModel] as its foundation to manage [LoanApplicationState],
 * handle [LoanApplicationAction] from the UI, and send [LoanApplicationEvent]
 * to trigger one-time UI side effects.
 *
 * @property userPreferencesRepository Repository for accessing user preferences, specifically the client ID.
 * @property loanAccountRepositoryImp Repository for interacting with loan-related data from a remote source.
 * @property homeRepositoryImpl Repository for accessing home-related data, such as client information.
 * @property networkMonitor Monitors the network connectivity status.
 */
@Suppress("CyclomaticComplexMethod", "TooManyFunctions")
internal class LoanApplyViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val loanAccountRepositoryImp: LoanRepository,
    private val homeRepositoryImpl: HomeRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<LoanApplicationState, LoanApplicationEvent, LoanApplicationAction>(
    initialState = run {
        val route = savedStateHandle.toRoute<LoanApplyRoute>()
        LoanApplicationState(
            clientId = requireNotNull(userPreferencesRepository.clientId.value),
            applicantName = "",
            principalAmount = "",
            currency = Currency(),
            loanProductId = route.productId,
            loanProductName = route.productName,
        )
    },
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
                    sendAction(LoanApplicationAction.ReceiveNetworkStatus(isOnline))
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
     * @param action The [LoanApplicationAction] to be handled.
     */
    override fun handleAction(action: LoanApplicationAction) {
        when (action) {
            is LoanApplicationAction.ApplicantNameChange -> {
                onApplicantNameChange(action.name)
            }

            is LoanApplicationAction.PurposeOfLoanChange -> {
                onPurposeOfLoanChange(action.purposeOfLoan)
            }

            is LoanApplicationAction.PrincipalAmountChange -> {
                onPrincipalAmountChange(action.principalAmount)
            }

            is LoanApplicationAction.DisbursementDateChange -> {
                onDisbursementDateChange(action.disbursementDate)
            }

            is LoanApplicationAction.ReceiveNetworkStatus -> handleNetworkStatus(action.isOnline)

            is LoanApplicationAction.NavigateToConfirmDetails -> validateAndSubmit()

            is LoanApplicationAction.OnNavigateBack -> navigateBack()

            is LoanApplicationAction.ConfirmNavigation -> sendEvent(LoanApplicationEvent.NavigateBack)

            is LoanApplicationAction.ToggleDatePicker -> toggleDatePicker()

            is LoanApplicationAction.RetrySubmit -> resetSubmitAttempts()

            is LoanApplicationAction.DismissDialog -> dismissDialog()

            is LoanApplicationAction.Internal.ReceiveClientAndTemplateResult ->
                handleClientAndTemplateResult(
                    client = action.client,
                    template = action.template,
                    purpose = action.purpose,
                )

            LoanApplicationAction.Retry -> retry()
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
                getClientDataAndTemplate()
            }
        }
    }

    /**
     * Fetches client data, a generic loan template, and a product-specific loan purpose template
     * from the repositories.
     * The results are combined and handled in a single flow to manage loading and error states.
     */
    private fun getClientDataAndTemplate() {
        showLoading()
        viewModelScope.launch {
            combine(
                homeRepositoryImpl.currentClient(state.clientId),
                loanAccountRepositoryImp.template(state.clientId),
                loanAccountRepositoryImp.getLoanTemplateByProduct(state.clientId, state.loanProductId),
            ) { client, template, purpose ->
                Triple(client, template, purpose)
            }
                .catch { throwable ->

                    updateState {
                        it.copy(
                            uiState = if (throwable.cause is IOException) {
                                ScreenUiState.Network
                            } else {
                                ScreenUiState.Error(Res.string.feature_apply_loan_error_server)
                            },
                        )
                    }
                }
                .collect { (client, template, purpose) ->
                    sendAction(
                        LoanApplicationAction.Internal.ReceiveClientAndTemplateResult(
                            client,
                            template,
                            purpose,
                        ),
                    )
                }
        }
    }

    /**
     * Handles the result of the combined data fetching operation for client and loan templates.
     * It updates the UI state based on whether the data fetching was successful, loading, or failed.
     *
     * @param client The [DataState] of the client data.
     * @param template The [DataState] of the generic loan template.
     * @param purpose The [DataState] of the product-specific loan purpose template.
     */
    private fun handleClientAndTemplateResult(
        client: DataState<Client?>,
        template: DataState<LoanTemplate?>,
        purpose: DataState<LoanTemplate?>,
    ) {
        when {
            listOf(client, template, purpose).any { it is DataState.Loading } -> {
                showLoading()
            }

            client is DataState.Success && template is DataState.Success && purpose is DataState.Success -> {
                val mappedLoanPurposeOptions: Map<Long, String> = purpose.data?.loanPurposeOptions
                    ?.mapNotNull { option ->
                        val id = option.id?.toLong()
                        val name = option.name
                        if (id != null && name != null) id to name else null
                    }
                    ?.takeIf { it.isNotEmpty() }
                    ?.toMap()
                    ?: fallbackLoanPurposeMap

                client.data?.activationDate?.let { activationDate ->
                    updateState {
                        it.copy(
                            activationDate = DateHelper.getDateAsString(activationDate),
                        )
                    }
                }

                updateState {
                    it.copy(
                        currency = template.data?.currency ?: Currency(
                            code = "USD",
                            name = "US Dollar",
                            decimalPlaces = 2.0,
                            inMultiplesOf = 0,
                            displaySymbol = "$",
                            nameCode = "currency.USD",
                            displayLabel = "US Dollar ($)",
                        ),
                        loanPurposeOptions = mappedLoanPurposeOptions,
                        uiState = ScreenUiState.Success,
                    )
                }
            }

            else -> updateState { it.copy(uiState = ScreenUiState.Error(Res.string.feature_apply_loan_error_server)) }
        }
    }

    /**
     * Retries the data fetching process. If the network is unavailable, it shows
     * a network error dialog. Otherwise, it triggers the `fetchLoanTemplate` `fetchClient`,
     * `fetchLonPurpose` function.
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
     * A helper function to update the mutable state flow.
     *
     * @param update A lambda function that takes the current state and returns a new state.
     */
    private fun updateState(update: (LoanApplicationState) -> LoanApplicationState) {
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
    private fun showErrorDialog(error: StringResource) {
        updateState { it.copy(loanApplicationDialogState = LoanApplicationDialogState.Error(error)) }
    }

    /**
     * Validates the applicant's name based on length and format.
     *
     * @param name The applicant's name to validate.
     * @return A [ValidationResult] indicating success or a specific error.
     */
    private fun validateApplicantName(name: String): ValidationResult = when {
        name.isEmpty() -> ValidationResult.Error(Res.string.feature_apply_loan_error_name_empty)
        name.length < 4 -> ValidationResult.Error(Res.string.feature_apply_loan_error_name_too_short)
        name.length > 100 -> ValidationResult.Error(
            Res.string.feature_apply_loan_error_name_too_long,
        )

        !ValidationHelper.isValidName(name) -> ValidationResult.Error(
            Res.string.feature_apply_loan_error_name_invalid_format,
        )

        else -> ValidationResult.Success
    }

    /**
     * Validates the principal loan amount using a helper function.
     * On success, it updates the state with the normalized amount.
     *
     * @param amount The principal amount as a string.
     * @param currency The currency details for validation.
     * @return A [ValidationResult] indicating success or a specific error.
     */
    private fun validatePrincipalAmount(
        amount: String,
        currency: ModelCurrency,
    ): ValidationResult {
        return when (val result = ValidationHelper.validateAmountWithDetails(amount, currency)) {
            is AmountValidationResult.Valid -> {
                val value = result.normalizedAmount
                return if (value in 1000.0..10000.0) {
                    mutableStateFlow.update {
                        it.copy(principalAmount = value.toString())
                    }
                    ValidationResult.Success
                } else {
                    ValidationResult.Error(
                        if (value < 1000.0) {
                            Res.string.feature_apply_loan_error_amount_too_small
                        } else {
                            Res.string.feature_apply_loan_error_amount_too_large
                        },
                    )
                }
            }

            is AmountValidationResult.Invalid -> ValidationResult.Error(result.errorResource)
        }
    }

    /**
     * Validates that a disbursement date has been selected.
     *
     * @param dateString The disbursement date as a string.
     * @return A [ValidationResult] indicating success or an error if the field is empty.
     */
    private fun validateDisbursementDate(dateString: String): ValidationResult = when {
        dateString.isEmpty() -> ValidationResult.Error(Res.string.feature_apply_loan_error_date_empty)
        else -> ValidationResult.Success
    }

    /**
     * Handles changes to the applicant's name field.
     * It updates the state and debounces validation to prevent excessive checks.
     *
     * @param newValue The new value of the applicant name field.
     */
    private fun onApplicantNameChange(newValue: String) {
        mutableStateFlow.update {
            it.copy(
                applicantName = newValue,
                applicantNameError = null,
                hasChanges = true,
            )
        }
        debounceValidation {
            val result = validateApplicantName(newValue)
            mutableStateFlow.update {
                it.copy(
                    applicantNameError = if (result is ValidationResult.Error) result.message else null,
                )
            }
        }
    }

    /**
     * Handles changes to the principal amount field.
     * It updates the state and debounces validation.
     *
     * @param newValue The new value of the principal amount field.
     */
    private fun onPrincipalAmountChange(newValue: String) {
        mutableStateFlow.update {
            it.copy(
                principalAmount = newValue,
                principalAmountError = null,
                hasChanges = true,
            )
        }
        debounceValidation {
            val result =
                validatePrincipalAmount(state.principalAmount, state.currency.toModelCurrency())
            mutableStateFlow.update {
                it.copy(
                    principalAmountError = if (result is ValidationResult.Error) result.message else null,
                )
            }
        }
    }

    /**
     * Handles changes to the disbursement date field.
     * It updates the state and debounces validation.
     *
     * @param newValue The new value of the disbursement date field.
     */
    private fun onDisbursementDateChange(newValue: String) {
        mutableStateFlow.update {
            it.copy(
                disbursementDate = newValue,
                disbursementDateError = null,
                hasChanges = true,
            )
        }
        debounceValidation {
            val result = validateDisbursementDate(newValue)
            mutableStateFlow.update {
                it.copy(
                    disbursementDateError = if (result is ValidationResult.Error) result.message else null,
                )
            }
        }
    }

    /**
     * Handles changes to the purpose of loan field.
     *
     * @param newValue The new value of the purpose of loan field.
     */
    private fun onPurposeOfLoanChange(newValue: String) {
        mutableStateFlow.update {
            it.copy(
                selectedLoanPurpose = newValue,
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
                    loanApplicationDialogState = LoanApplicationDialogState.Error(
                        Res.string.feature_apply_loan_error_too_many_attempts,
                    ),
                )
            }
            return
        }
        val nameResult = validateApplicantName(state.applicantName)
        val amountResult = state.currency.toModelCurrency().let { currency ->
            validatePrincipalAmount(state.principalAmount, currency)
        }
        val dateResult = validateDisbursementDate(state.disbursementDate)

        mutableStateFlow.update {
            it.copy(
                applicantNameError = if (nameResult is ValidationResult.Error) nameResult.message else null,
                principalAmountError = if (amountResult is ValidationResult.Error) amountResult.message else null,
                disbursementDateError = if (dateResult is ValidationResult.Error) dateResult.message else null,
            )
        }

        val isValid = listOf(
            nameResult,
            amountResult,
            dateResult,
        ).all { it is ValidationResult.Success }
        if (isValid) {
            handleSubmit()
        } else {
            submitAttempts++
        }
    }

    /**
     * Handles the successful submission of the loan application.
     * It shows a loading overlay, resets the submit attempts,
     * and sends an event to navigate to the confirmation screen.
     */
    private fun handleSubmit() {
        viewModelScope.launch {
            try {
                updateState {
                    it.copy(
                        hasChanges = false,
                        loanApplicationDialogState = null,
                    )
                }
                submitAttempts = 0
                sendEvent(LoanApplicationEvent.NavigateToConfirmDetailsScreen)
            } catch (e: Exception) {
                submitAttempts++
                showErrorDialog(Res.string.feature_apply_loan_error_submit_failed)
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
            it.copy(loanApplicationDialogState = null)
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
                    loanApplicationDialogState = LoanApplicationDialogState.UnsavedChanges(
                        Res.string.feature_apply_loan_unsaved_changes_message,
                    ),
                )
            }
        } else {
            sendEvent(LoanApplicationEvent.NavigateBack)
        }
    }

    /**
     * Toggles the visibility of the date picker dialog.
     */
    private fun toggleDatePicker() {
        mutableStateFlow.update {
            it.copy(
                showDatePicker = !state.showDatePicker,
            )
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
 * Represents the UI state for the loan application screen.
 *
 * @property clientId The ID of the current client.
 * @property applicantName The name of the applicant.
 * @property loanProductName The name of the currently selected loan product.
 * @property loanProductId The ID of the currently selected loan product.
 * @property activationDate The client's activation date.
 * @property loanPurposeOptions A map of loan purpose IDs to their names, based on the selected product.
 * @property selectedLoanPurpose The name of the currently selected loan purpose.
 * @property selectedLoanPurposeId The ID of the currently selected loan purpose.
 * @property principalAmount The principal loan amount.
 * @property disbursementDate The date of disbursement.
 * @property currency The currency details for the loan.
 * @property loanApplicationDialogState The state of any dialog to be shown on the screen.
 * @property loanProductTemplate The full loan template object for the selected product.
 * @property applicantNameError A resource string for an error message for the applicant name field, or null.
 * @property loanProductError A resource string for an error message for the loan product field, or null.
 * @property principalAmountError A resource string for an error message for the principal amount field, or null.
 * @property disbursementDateError A resource string for an error message for the disbursement date field, or null.
 * @property hasChanges A boolean indicating if there are unsaved changes.
 * @property networkStatus A boolean indicating the network status
 */
@OptIn(ExperimentalMaterial3Api::class)
internal data class LoanApplicationState(
    val clientId: Long,
    val applicantName: String,
    val loanProductId: Int,
    val loanProductName: String,
    val activationDate: String = "",
    val loanPurposeOptions: Map<Long, String> = emptyMap(),
    val selectedLoanPurpose: String = "",
    val selectedLoanPurposeId: Int = 0,
    val principalAmount: String = "",
    val disbursementDate: String = "",
    val currency: Currency,
    val loanApplicationDialogState: LoanApplicationDialogState? = null,
    val loanProductTemplate: LoanTemplate? = null,
    val applicantNameError: StringResource? = null,
    val loanProductError: StringResource? = null,
    val principalAmountError: StringResource? = null,
    val disbursementDateError: StringResource? = null,
    val hasChanges: Boolean = false,
    val networkStatus: Boolean = false,

    val uiState: ScreenUiState? = ScreenUiState.Loading,
    val showDatePicker: Boolean = false,
) {
    /**
     * The current time in milliseconds, used for date pickers.
     */
    val currentDate: Long
        get() = Clock.System.now().toEpochMilliseconds()

    /**
     * A boolean indicating if the entire form is valid for submission.
     * This is based on the absence of errors and non-empty fields.
     */
    val isFormValid: Boolean
        get() = applicantNameError == null &&
            loanProductError == null &&
            principalAmountError == null &&
            disbursementDateError == null &&
            applicantName.isNotBlank() &&
            principalAmount.isNotBlank() &&
            disbursementDate.isNotBlank()

//    TODO Add all validations here for the fields that API Require
    /**
     * Validation Notes
     *
     * Date Validations:
     * 1. `submittedOnDate`
     *    - Must not be in the future.
     *    - Must not be before the client's activation date.
     *    - Defaults to the later of today's date or the activation date.
     *
     * 2. `disbursementDate`
     *    - Must be on or after the `submittedOnDate`.
     *    - If not, it falls back to `submittedOnDate`.
     *
     * Principal Amount Validation:
     * - The principal amount must be within the range of 1000.0 to 10000.0 (inclusive).
     * - Values outside this range are considered invalid and will trigger validation errors.
     */

    /**
     * The effective submission date, which is the later of today's date or the
     * client's activation date.
     */
    val submittedOnDate: String
        get() {
            val todayMillis = Clock.System.now().toEpochMilliseconds()
            val activationMillis =
                DateHelper.getDateAsLongFromList(DateHelper.getDateAsList(activationDate))
            val submittedMillis = maxOf(todayMillis, activationMillis ?: todayMillis)
            return DateHelper.getDateMonthYearString(submittedMillis)
        }

    /**
     * The safe disbursement date, which is the provided disbursement date if it is
     * on or after the submitted date. Otherwise, it defaults to the submitted date.
     */
    val safeDisbursementDate: String
        get() {
            val disbursementMillis =
                DateHelper.getDateAsLongFromList(DateHelper.getDateAsList(disbursementDate))
            val submittedMillis =
                DateHelper.getDateAsLongFromList(DateHelper.getDateAsList(submittedOnDate))

            return if (
                disbursementMillis != null &&
                submittedMillis != null &&
                disbursementMillis >= submittedMillis
            ) {
                disbursementDate
            } else {
                submittedOnDate
            }
        }
}

/**
 * A sealed interface representing the different types of dialogs that can be
 * shown on the loan application screen.
 */
internal sealed interface LoanApplicationDialogState {
    /**
     * Represents a generic error dialog with a message.
     * @property message The [StringResource] for the error message.
     */
    data class Error(val message: StringResource) : LoanApplicationDialogState

    /**
     * Represents a dialog to confirm navigation with unsaved changes.
     * @property message The [StringResource] for the confirmation message.
     */
    data class UnsavedChanges(val message: StringResource) : LoanApplicationDialogState
}

/**
 * A sealed interface representing one-time events that trigger UI side effects.
 */
internal sealed interface LoanApplicationEvent {
    /** Navigates back from the current screen. */
    data object NavigateBack : LoanApplicationEvent

    /** Navigates to the confirmation details screen. */
    data object NavigateToConfirmDetailsScreen : LoanApplicationEvent
}

/**
 * A sealed interface representing user actions or internal events that the
 * ViewModel needs to handle.
 */
internal sealed interface LoanApplicationAction {

    /** Action to observe network status */
    data class ReceiveNetworkStatus(val isOnline: Boolean) : LoanApplicationAction

    /** User action to navigate back. */
    data object OnNavigateBack : LoanApplicationAction

    /** User action to dismiss a dialog. */
    data object DismissDialog : LoanApplicationAction

    /** User action to retry a failed operation. */
    data object Retry : LoanApplicationAction

    /** User action to confirm navigation (e.g., dismissing an unsaved changes dialog). */
    data object ConfirmNavigation : LoanApplicationAction

    /**
     * User action when the applicant's name field changes.
     * @property name The new value of the name field.
     */
    data class ApplicantNameChange(val name: String) : LoanApplicationAction

    /**
     * User action when the purpose of loan field changes.
     * @property purposeOfLoan The new value of the purpose field.
     */
    data class PurposeOfLoanChange(val purposeOfLoan: String) : LoanApplicationAction

    /**
     * User action when the disbursement date changes.
     * @property disbursementDate The new value of the disbursement date field.
     */
    data class DisbursementDateChange(val disbursementDate: String) : LoanApplicationAction

    /**
     * User action when the principal amount changes.
     * @property principalAmount The new value of the principal amount field.
     */
    data class PrincipalAmountChange(val principalAmount: String) : LoanApplicationAction

    /** User action to toggle the visibility of the date picker. */
    data object ToggleDatePicker : LoanApplicationAction

    /** User action to navigate to the confirm details screen, triggering form validation. */
    data object NavigateToConfirmDetails : LoanApplicationAction

    /** User action to retry a form submission after an error. */
    data object RetrySubmit : LoanApplicationAction

    /**
     * A sealed interface for internal actions, which are not triggered directly by the UI.
     */
    sealed interface Internal : LoanApplicationAction {

        /**
         * An internal action to handle the combined results of fetching client, generic loan template,
         * and product-specific loan purpose data.
         *
         * The ViewModel uses this action to process the asynchronous results from the repositories
         * and update the UI state based on the success, loading, or error state of each data fetch.
         *
         * @property client The [DataState] of the client data.
         * @property template The [DataState] of the generic loan template.
         * @property purpose The [DataState] of the product-specific loan purpose template.
         */
        data class ReceiveClientAndTemplateResult(
            val client: DataState<Client?>,
            val template: DataState<LoanTemplate?>,
            val purpose: DataState<LoanTemplate?>,
        ) : LoanApplicationAction
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
        decimalPlaces = decimalPlaces?.toInt() ?: DEFAULT_DECIMAL_PLACES,
        inMultiplesOf = inMultiplesOf?.toDouble() ?: DEFAULT_IN_MULTIPLES_OF,
        displaySymbol = displaySymbol,
        nameCode = nameCode,
        displayLabel = displayLabel,
    )
}

private const val DEFAULT_DECIMAL_PLACES = 2
private const val DEFAULT_IN_MULTIPLES_OF = 1.0
