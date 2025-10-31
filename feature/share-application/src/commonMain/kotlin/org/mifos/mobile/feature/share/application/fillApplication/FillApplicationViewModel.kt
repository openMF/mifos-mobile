/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.share.application.fillApplication

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import mifos_mobile.feature.share_application.generated.resources.Res
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_error_frequency_required
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_error_server
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_error_shares_required
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_error_too_many_attempts
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_frequency_invalid
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_shares_invalid
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_status_failure
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_status_failure_action
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_status_failure_tip
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_status_success
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_status_success_action
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_status_success_tip
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_unsaved_changes_message
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.repository.ShareAccountRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.EventType
import org.mifos.mobile.core.model.StatusNavigationDestination
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.model.entity.payload.ShareApplicationPayload
import org.mifos.mobile.core.model.entity.templates.shareProductDetails.AccountingItem
import org.mifos.mobile.core.model.entity.templates.shareProductDetails.AccountingMappings
import org.mifos.mobile.core.model.entity.templates.shareProductDetails.Currency
import org.mifos.mobile.core.model.entity.templates.shareProductDetails.EnumOption
import org.mifos.mobile.core.model.entity.templates.shareProductDetails.ShareProductDetails
import org.mifos.mobile.core.ui.utils.AmountValidationResult
import org.mifos.mobile.core.ui.utils.AuthResult
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ResultNavigator
import org.mifos.mobile.core.ui.utils.ValidationHelper
import org.mifos.mobile.core.ui.utils.observe
import kotlin.String
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import org.mifos.mobile.core.model.entity.Currency as ModelCurrency

private const val DEFAULT_DECIMAL_PLACES = 2
private const val DEFAULT_IN_MULTIPLES_OF = 1.0

/**
 * ViewModel for the share fill application screen.
 *
 * This ViewModel manages the UI state, user interactions, and business logic for the
 * share application form. It handles data fetching, input validation, network status,
 * and navigation to other screens.
 *
 * @param userPreferencesRepositoryImpl The repository for user preferences.
 * @param shareAccountRepositoryImpl The repository for share account operations.
 * @param accountsRepositoryImpl The repository for fetching savings accounts
 * @param networkMonitor The utility to monitor network connectivity.
 * @param resultNavigator The navigator to handle results from other screens, like authentication.
 * @param savedStateHandle The handle to access saved state from the navigation route.
 */
@Suppress("CyclomaticComplexMethod", "TooManyFunctions")
internal class ShareFillApplicationViewModel(
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
    private val shareAccountRepositoryImpl: ShareAccountRepository,
    private val accountsRepositoryImpl: AccountsRepository,
    private val networkMonitor: NetworkMonitor,
    private val resultNavigator: ResultNavigator,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ShareApplicationState, ShareApplicationEvent, ShareApplicationAction>(
    initialState = run {
        val route = savedStateHandle.toRoute<ShareFillApplicationRoute>()
        ShareApplicationState(
            uiState = ShareApplicationUiState.Loading,
            clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
            shareProductId = route.shareProductId,
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
     * Handles UI actions from the screen.
     *
     * @param action The action to be processed.
     */
    override fun handleAction(action: ShareApplicationAction) {
        when (action) {
            is ShareApplicationAction.CurrentPriceChange -> onCurrentPriceChange(action.price)

            is ShareApplicationAction.TotalNumberOfSharesChange -> onTotalSharesChange(action.shares)

            is ShareApplicationAction.DefaultSavingsAccountChange -> onDefaultSavingsAccountChange(
                action.defaultSavingsAccountId,
                action.defaultSavingsAccountName,
            )

            is ShareApplicationAction.MapFrequencyChange -> onMapFrequencyChange(action.mapFrequency)

            is ShareApplicationAction.MapFrequencyTypeChange -> onMapFrequencyTypeChange(action.id, action.value)

            is ShareApplicationAction.LipFrequencyChange -> onLipFrequencyChange(action.lipFrequency)

            is ShareApplicationAction.LipFrequencyTypeChange -> onLipFrequencyTypeChange(action.id, action.value)

            is ShareApplicationAction.RequestShareAccount -> submitShareAccountApplication()

            is ShareApplicationAction.Internal.ReceiveShareTemplate -> handleShareTemplateResult(action.template)

            is ShareApplicationAction.Internal.ReceiveAuthenticationResult -> handleShareApplyRequest(action.result)

            is ShareApplicationAction.Internal.ReceiveShareApplicationResult -> {
                viewModelScope.launch { handleShareApplicationResult(action.result) }
            }

            is ShareApplicationAction.Internal.ReceiveClientSavingsAccounts -> {
                viewModelScope.launch { handleClientAccountResult(action.accounts) }
            }

            is ShareApplicationAction.Internal.ReceiveNetworkResult -> handleNetworkResult(action.isOnline)

            is ShareApplicationAction.NavigateToAuthentication -> validateAndSubmit()

            is ShareApplicationAction.OnNavigateBack -> navigateBack()

            is ShareApplicationAction.ConfirmNavigation -> sendEvent(ShareApplicationEvent.NavigateBack)

            is ShareApplicationAction.DismissDialog -> dismissDialog()

            is ShareApplicationAction.Retry -> retry()

            is ShareApplicationAction.RetrySubmit -> resetSubmitAttempts()
        }
    }

    /**
     * Updates the UI state by applying a transformation function.
     *
     * @param update A function that takes the current state and returns the new state.
     */
    private fun updateState(update: (ShareApplicationState) -> ShareApplicationState) {
        mutableStateFlow.update(update)
    }

    /**
     * Observes the network status and updates the UI accordingly.
     *
     * If the network is offline, it sets the UI state to a network error.
     * When the network comes back online, it triggers a data fetch.
     */
    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .distinctUntilChanged()
                .collect { isOnline ->
                    sendAction(ShareApplicationAction.Internal.ReceiveNetworkResult(isOnline))
                }
        }
    }

    /**
     * Manages UI state changes based on network connectivity.
     *
     * This function updates the application's state to reflect whether the device is online or offline.
     *
     * When the app is **offline**:
     * - It immediately updates the `networkStatus` in the state to `false`.
     * to inform the user that a network connection is required.
     *
     * When the app is **online**:
     * - It immediately updates the `networkStatus` in the state to `true`.
     * - It then triggers essential functions to **refresh data** and ensure the UI is up-to-date,
     * specifically by calling `unreadNotificationsCount()` and `loadClientAccountDetails()`.
     *
     * @param isOnline A `Boolean` indicating the current network connectivity status.
     *
     */
    private fun handleNetworkResult(isOnline: Boolean) {
        updateState { it.copy(networkStatus = isOnline) }

        viewModelScope.launch {
            if (!isOnline) {
                updateState { current ->
                    if (current.uiState is ShareApplicationUiState.Loading ||
                        current.uiState is ShareApplicationUiState.Error ||
                        current.uiState is ShareApplicationUiState.Network
                    ) {
                        current.copy(uiState = ShareApplicationUiState.Network)
                    } else {
                        current
                    }
                }
            } else {
                fetchSavingsAccounts()
                fetchShareTemplateByProduct()
            }
        }
    }

    /**
     * Observes the authentication result from the `ResultNavigator`.
     *
     * Handles the outcome of the authentication flow triggered by the UI.
     */
    private fun observeAuthResult() {
        viewModelScope.launch {
            resultNavigator.observe<AuthResult>()
                .collect { result ->
                    sendAction(ShareApplicationAction.Internal.ReceiveAuthenticationResult(result.success))
                }
        }
    }

    /**
     * Retries the last failed operation.
     *
     * Checks network status and either shows a network error or re-fetches the share template.
     */
    private fun retry() {
        viewModelScope.launch {
            if (!state.networkStatus) {
                updateState { it.copy(uiState = ShareApplicationUiState.Network) }
            } else {
                fetchShareTemplateByProduct()
            }
        }
    }

    /**
     * Fetches the share product template details from the repository.
     *
     * Uses the `shareProductId` and `clientId` from the state to make the API call.
     */
    private fun fetchShareTemplateByProduct() {
        viewModelScope.launch {
            shareAccountRepositoryImpl
                .getShareProductById(state.shareProductId, state.clientId)
                .collect { result ->
                    sendAction(ShareApplicationAction.Internal.ReceiveShareTemplate(result))
                }
        }
    }

    private fun fetchSavingsAccounts() {
        viewModelScope.launch {
            accountsRepositoryImpl.loadAccounts(
                clientId = state.clientId,
                accountType = Constants.SAVINGS_ACCOUNTS,
            ).catch { e ->
                mutableStateFlow.update {
                    it.copy(
                        uiState = ShareApplicationUiState.Error(Res.string.feature_apply_share_error_server),
                    )
                }
            }.collect { clientAccounts ->
                sendAction(
                    ShareApplicationAction.Internal.ReceiveClientSavingsAccounts(
                        accounts = clientAccounts,
                    ),
                )
            }
        }
    }

    /**
     * Handles the result of the `loadAccounts` API call.
     *
     * Updates the UI state based on whether the result is loading, an error, or a success.
     * If successful, it populates the state with the received data.
     *
     * @param response The `DataState` containing the `ClientAccounts`.
     */
    private fun handleClientAccountResult(response: DataState<ClientAccounts>) {
        when (response) {
            is DataState.Loading -> showLoading()
            is DataState.Error -> {
                updateState {
                    it.copy(
                        uiState = ShareApplicationUiState.Error(
                            Res.string.feature_apply_share_error_server,
                        ),
                    )
                }
            }
            is DataState.Success -> {
                val shareTemplate = response.data
                updateState {
                    it.copy(
                        defaultAccounts = shareTemplate.savingsAccounts ?: emptyList(),
                        uiState = ShareApplicationUiState.Success,
                    )
                }
            }
        }
    }

    /**
     * Handles the result of the `getShareProductById` API call.
     *
     * Updates the UI state based on whether the result is loading, an error, or a success.
     * If successful, it populates the state with the received data.
     *
     * @param template The `DataState` containing the `ShareProductDetails`.
     */
    private fun handleShareTemplateResult(template: DataState<ShareProductDetails?>) {
        when (template) {
            is DataState.Loading -> showLoading()
            is DataState.Error -> {
                updateState {
                    it.copy(
                        uiState = ShareApplicationUiState.Error(
                            Res.string.feature_apply_share_error_server,
                        ),
                    )
                }
            }
            is DataState.Success -> {
                val shareTemplate = template.data ?: return
                updateState {
                    it.copy(
                        currency = shareTemplate.currency ?: Currency(),
                        shareProductName = shareTemplate.name ?: "",
                        mapFrequencyType = listOfNotNull(
                            EnumOption(
                                id = shareTemplate.minimumActivePeriodForDividendsTypeEnum?.id,
                                value = shareTemplate.minimumActivePeriodForDividendsTypeEnum?.value,
                            ),
                        ),
                        lipFrequencyType = listOfNotNull(
                            EnumOption(
                                id = shareTemplate.lockPeriodTypeEnum?.id,
                                value = shareTemplate.lockPeriodTypeEnum?.value,
                            ),
                        ),
                        uiState = ShareApplicationUiState.Success,
                    )
                }
            }
        }
    }

    /**
     * Sets the UI state to `Loading`.
     */
    private fun showLoading() {
        updateState { it.copy(uiState = ShareApplicationUiState.Loading) }
    }

    /**
     * Sets the dialog state to an `Error` dialog.
     *
     * @param error The string resource for the error message.
     */
    private fun showErrorDialog(error: StringResource) {
        updateState { it.copy(dialogState = ShareApplicationDialogState.Error(error)) }
    }

    /**
     * Dismisses any active dialog by setting `dialogState` to null.
     */
    private fun dismissDialog() {
        updateState { it.copy(dialogState = null) }
    }

    /**
     * Handles changes to the current price input field.
     *
     * Updates the state and triggers a debounced validation.
     *
     * @param newValue The new value of the price input.
     */
    private fun onCurrentPriceChange(newValue: String) {
        updateState { it.copy(currentPrice = newValue, currentPriceError = null, hasChanges = true) }
        debounceValidation {
            val result = validateCurrentPrice(state.currentPrice, state.currency.toModelCurrency())
            updateState { it.copy(currentPriceError = if (result is ValidationResult.Error) result.message else null) }
        }
    }

    /**
     * Validates the current price input.
     *
     * @param amount The string to validate.
     * @param currency The currency model for validation rules.
     * @return A `ValidationResult` indicating success or an error.
     */
    private fun validateCurrentPrice(amount: String, currency: ModelCurrency): ValidationResult {
        return when (val result = ValidationHelper.validateAmountWithDetails(amount, currency)) {
            is AmountValidationResult.Valid -> ValidationResult.Success
            is AmountValidationResult.Invalid -> ValidationResult.Error(result.errorResource)
        }
    }

    /**
     * Handles changes to the total number of shares input field.
     *
     * Updates the state and triggers a debounced validation.
     *
     * @param newValue The new value of the shares input.
     */
    private fun onTotalSharesChange(newValue: String) {
        updateState { it.copy(totalNumberOfShares = newValue, sharesError = null, hasChanges = true) }
        debounceValidation {
            val result = validateTotalShares(newValue)
            updateState { it.copy(sharesError = if (result is ValidationResult.Error) result.message else null) }
        }
    }

    /**
     * Validates the total number of shares input.
     *
     * @param newValue The string to validate.
     * @return A `ValidationResult` indicating success or an error.
     */
    private fun validateTotalShares(newValue: String): ValidationResult = when {
        newValue.isBlank() -> ValidationResult.Error(Res.string.feature_apply_share_error_shares_required)
        newValue.toLongOrNull() == null -> ValidationResult.Error(Res.string.feature_apply_share_shares_invalid)
        else -> ValidationResult.Success
    }

    /**
     * Handles changes to the default savings account selection.
     *
     * @param id The ID of the selected savings account.
     * @param name The name of the selected savings account.
     */
    private fun onDefaultSavingsAccountChange(id: Long, name: String) {
        updateState { it.copy(defaultSavingsAccountId = id, defaultSavingsAccountName = name, hasChanges = true) }
    }

    /**
     * Handles changes to the `mapFrequency` input field.
     *
     * Updates the state and triggers a debounced validation.
     *
     * @param newValue The new value of the `mapFrequency` input.
     */
    private fun onMapFrequencyChange(newValue: String) {
        updateState { it.copy(mapFrequency = newValue, mapFrequencyError = null, hasChanges = true) }
        debounceValidation {
            val result = validateFrequency(newValue)
            updateState { it.copy(mapFrequencyError = if (result is ValidationResult.Error) result.message else null) }
        }
    }

    /**
     * Validates a frequency input field.
     *
     * @param newValue The string to validate.
     * @return A `ValidationResult` indicating success or an error.
     */
    private fun validateFrequency(newValue: String): ValidationResult = when {
        newValue.isBlank() -> ValidationResult.Error(Res.string.feature_apply_share_error_frequency_required)
        newValue.toIntOrNull() == null -> ValidationResult.Error(Res.string.feature_apply_share_frequency_invalid)
        else -> ValidationResult.Success
    }

    /**
     * Handles changes to the selected `mapFrequencyType`.
     *
     * @param id The ID of the selected frequency type.
     * @param value The value of the selected frequency type.
     */
    private fun onMapFrequencyTypeChange(id: Long, value: String) {
        updateState {
            it.copy(
                selectedMapFrequencyTypeId = id,
                selectedMapFrequencyTypeName = value,
                hasChanges = true,
            )
        }
    }

    /**
     * Handles changes to the `lipFrequency` input field.
     *
     * Updates the state and triggers a debounced validation.
     *
     * @param newValue The new value of the `lipFrequency` input.
     */
    private fun onLipFrequencyChange(newValue: String) {
        updateState {
            it.copy(
                lipFrequency = newValue,
                lipFrequencyError = null,
                hasChanges = true,
            )
        }
        debounceValidation {
            val result = validateFrequency(newValue)
            updateState {
                it.copy(
                    lipFrequencyError = if (result is ValidationResult.Error) result.message else null,
                )
            }
        }
    }

    /**
     * Handles changes to the selected `lipFrequencyType`.
     *
     * @param id The ID of the selected frequency type.
     * @param value The value of the selected frequency type.
     */
    private fun onLipFrequencyTypeChange(id: Long, value: String) {
        updateState {
            it.copy(
                selectedLipFrequencyTypeId = id,
                selectedLipFrequencyTypeName = value,
                hasChanges = true,
            )
        }
    }

    /**
     * A debouncing function to delay validation.
     *
     * This prevents validation from running on every keystroke, improving performance.
     *
     * @param validation The validation function to be executed after the delay.
     */
    private fun debounceValidation(validation: suspend () -> Unit) {
        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            delay(300)
            validation()
        }
    }

    /**
     * Validates all form fields and initiates the submission process if valid.
     *
     * If validation fails, it increments `submitAttempts` and updates the state with errors.
     * If validation succeeds, it calls `handleSubmit`.
     */
    private fun validateAndSubmit() {
        if (submitAttempts >= maxSubmitAttempts) {
            showErrorDialog(Res.string.feature_apply_share_error_too_many_attempts)
            return
        }
        val priceResult =
            validateCurrentPrice(state.currentPrice, state.currency.toModelCurrency())
        val totalSharesResult = validateTotalShares(state.totalNumberOfShares)
        val mapFrequencyResult = validateFrequency(state.mapFrequency)
        val lipFrequencyResult = validateFrequency(state.lipFrequency)

        mutableStateFlow.update {
            it.copy(
                currentPriceError =
                if (priceResult is ValidationResult.Error) priceResult.message else null,
                sharesError =
                if (totalSharesResult is ValidationResult.Error) totalSharesResult.message else null,
                mapFrequencyError =
                if (mapFrequencyResult is ValidationResult.Error) mapFrequencyResult.message else null,
                lipFrequencyError =
                if (lipFrequencyResult is ValidationResult.Error) lipFrequencyResult.message else null,
            )
        }

        val isValid = listOf(
            priceResult,
            totalSharesResult,
            mapFrequencyResult,
            lipFrequencyResult,
        ).all { it is ValidationResult.Success }
        if (isValid) handleSubmit() else submitAttempts++
    }

    /**
     * Prepares the state for form submission and triggers the authentication event.
     */
    private fun handleSubmit() {
        updateState { it.copy(hasChanges = false) }
        submitAttempts = 0
        sendEvent(ShareApplicationEvent.NavigateToAuthentication)
    }

    /**
     * Handles the result of the authentication event.
     *
     * If authentication is successful, it triggers the request to submit the share account.
     *
     * @param isAuthenticated A boolean indicating if authentication was successful.
     */
    private fun handleShareApplyRequest(isAuthenticated: Boolean) {
        if (isAuthenticated) {
            viewModelScope.launch { sendAction(ShareApplicationAction.RequestShareAccount) }
        }
    }

    /**
     * Submits the share account application to the repository.
     *
     * Constructs the payload from the current state and calls the repository's submission function.
     */
    private fun submitShareAccountApplication() {
        updateState { it.copy(showOverlay = true) }
        viewModelScope.launch {
            val response = shareAccountRepositoryImpl.submitShareApplication(
                payload = state.toShareApplicationPayload(),
            )

            sendAction(ShareApplicationAction.Internal.ReceiveShareApplicationResult(response))
        }
    }

    /**
     * Handles the result of the share application submission.
     *
     * Based on the `DataState`, it navigates to the success or failure status screen.
     *
     * @param response The `DataState` containing the result of the submission.
     */
    @OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
    private suspend fun handleShareApplicationResult(response: DataState<String>) {
        when (response) {
            is DataState.Error -> {
                updateState { it.copy(showOverlay = false) }
                sendEvent(
                    ShareApplicationEvent.NavigateToStatus(
                        eventType = EventType.FAILURE.name,
                        eventDestination = StatusNavigationDestination.PREVIOUS_SCREEN.name,
                        title = getString(Res.string.feature_apply_share_status_failure),
                        subtitle = getString(
                            Res.string.feature_apply_share_status_failure_tip,
                            state.shareProductName,
                        ),
                        buttonText = getString(Res.string.feature_apply_share_status_failure_action),
                    ),
                )
            }

            DataState.Loading -> updateState { it.copy(showOverlay = true) }

            is DataState.Success -> sendEvent(
                ShareApplicationEvent.NavigateToStatus(
                    eventType = EventType.SUCCESS.name,
                    eventDestination = StatusNavigationDestination.SHARE_APPLICATION.name,
                    title = getString(Res.string.feature_apply_share_status_success),
                    subtitle = getString(
                        Res.string.feature_apply_share_status_success_tip,
                        state.shareProductName,
                    ),
                    buttonText = getString(Res.string.feature_apply_share_status_success_action),
                ),
            )
        }
    }

    /**
     * Navigates back, showing a confirmation dialog if there are unsaved changes.
     */
    private fun navigateBack() {
        if (state.hasChanges) {
            updateState {
                it.copy(
                    dialogState = ShareApplicationDialogState.UnsavedChanges(
                        Res.string.feature_apply_share_unsaved_changes_message,
                    ),
                )
            }
        } else {
            sendEvent(ShareApplicationEvent.NavigateBack)
        }
    }

    /**
     * Resets the submission attempt counter.
     */
    private fun resetSubmitAttempts() {
        submitAttempts = 0
    }

    /**
     * Called when the ViewModel is no longer used.
     *
     * Cancels the validation job to prevent memory leaks.
     */
    override fun onCleared() {
        super.onCleared()
        validationJob?.cancel()
    }

    /**
     * Converts the current `ShareApplicationState` to a `ShareApplicationPayload` for the API.
     *
     * @return The `ShareApplicationPayload` object.
     */
    // TODO need to add some more according to server payload request
    @Suppress("UnusedPrivateMember")
    private fun ShareApplicationState.toShareApplicationPayload(): ShareApplicationPayload {
        return ShareApplicationPayload(
            productId = state.shareProductId.toInt(),
            unitPrice = currentPrice.toDoubleOrNull() ?: 0.0,
            requestedShares = totalNumberOfShares.toIntOrNull() ?: 0,
            submittedDate = state.currentDate,
            savingsAccountId = defaultSavingsAccountId.toInt(),
            applicationDate = state.currentDate,
            locale = "en",
            dateFormat = "dd MMM yyyy",
            clientId = state.clientId,
        )
    }

    /**
     * Converts an `AccountingMappings` object to a list of `AccountingItem`s.
     *
     * @return A list of `AccountingItem` objects.
     */
    fun AccountingMappings.toAccountList(): List<AccountingItem> {
        return listOfNotNull(
            shareReferenceId,
            incomeFromFeeAccountId,
            shareEquityId,
            shareSuspenseId,
        )
    }
}

/**
 * Represents the UI state for the share application screen.
 *
 * @property clientId The ID of the current user.
 * @property shareProductId The ID of the share product.
 * @property shareProductName The name of the share product.
 * @property currency The currency details.
 * @property currentPrice The user-entered current price.
 * @property totalNumberOfShares The user-entered number of shares.
 * @property defaultSavingsAccountId The ID of the selected savings account.
 * @property defaultSavingsAccountName The name of the selected savings account.
 * @property defaultAccounts The list of available default accounts.
 * @property mapFrequencyType The list of options for map frequency.
 * @property mapFrequency The user-entered map frequency.
 * @property selectedMapFrequencyTypeName The name of the selected map frequency type.
 * @property selectedMapFrequencyTypeId The ID of the selected map frequency type.
 * @property lipFrequencyType The list of options for LIP frequency.
 * @property lipFrequency The user-entered LIP frequency.
 * @property selectedLipFrequencyTypeName The name of the selected LIP frequency type.
 * @property selectedLipFrequencyTypeId The ID of the selected LIP frequency type.
 * @property currentPriceError The error message for the price field.
 * @property sharesError The error message for the shares field.
 * @property mapFrequencyError The error message for the map frequency field.
 * @property lipFrequencyError The error message for the LIP frequency field.
 * @property hasChanges A flag indicating if the form has been modified.
 * @property networkStatus The current network connectivity status.
 * @property dialogState The state of the dialog to be displayed.
 * @property uiState The overall UI state (e.g., loading, success, error).
 * @property showOverlay A flag to show an overlay progress indicator.
 */
@OptIn(ExperimentalMaterial3Api::class)
internal data class ShareApplicationState(
    val clientId: Long,
    val shareProductId: Long,
    val shareProductName: String = "",
    val currency: Currency = Currency(),
    val currentPrice: String = "",
    val totalNumberOfShares: String = "",
    val defaultSavingsAccountId: Long = 0L,
    val defaultSavingsAccountName: String = "",
    // TODO we have to receive savings accounts from share product API it self but not getting
    //  from api call but in the web we are able to receive
//    val defaultAccounts: List<AccountingItem> = emptyList(),
    val defaultAccounts: List<SavingAccount> = emptyList(),
    val mapFrequencyType: List<EnumOption> = emptyList(),
    val mapFrequency: String = "",
    val selectedMapFrequencyTypeName: String = "",
    val selectedMapFrequencyTypeId: Long = 0,
    val lipFrequencyType: List<EnumOption> = emptyList(),
    val lipFrequency: String = "",
    val selectedLipFrequencyTypeName: String = "",
    val selectedLipFrequencyTypeId: Long = 0,
    val currentPriceError: StringResource? = null,
    val sharesError: StringResource? = null,
    val mapFrequencyError: StringResource? = null,
    val lipFrequencyError: StringResource? = null,
    val hasChanges: Boolean = false,
    val networkStatus: Boolean = false,
    val dialogState: ShareApplicationDialogState? = null,
    val uiState: ShareApplicationUiState?,
    val showOverlay: Boolean = false,
) {
    /**
     * The current date formatted as a string.
     */
    @OptIn(ExperimentalTime::class)
    val currentDate: String
        get() = DateHelper.getDateMonthYearString(Clock.System.now().toEpochMilliseconds())

    /**
     * A computed property that returns true if the form is valid for submission.
     */
    val isFormValid: Boolean
        get() = networkStatus &&
            currentPrice.isNotBlank() &&
            totalNumberOfShares.isNotBlank() &&
            selectedMapFrequencyTypeName.isNotBlank() &&
            selectedLipFrequencyTypeName.isNotBlank() &&
            currentPriceError == null &&
            sharesError == null &&
            mapFrequencyError == null &&
            lipFrequencyError == null

    /**
     * A map of account IDs to a display-friendly string like "000000029 - WALLET".
     */
    val accountIdNameMap: Map<Long, String> =
        defaultAccounts
            .filter { acc -> acc.status?.active == true }
            .associate { acc ->
                acc.id to "${acc.accountNo ?: "-"} - ${acc.productName ?: "Unknown"}"
            }

    /**
     * A map of MAP frequency IDs to their names.
     */
    val mapFrequencyMap: Map<Long, String> =
        mapFrequencyType.associate { (it.id?.toLong() ?: -1L) to (it.value ?: "Unknown") }

    /**
     * A map of LIP frequency IDs to their names.
     */
    val lipFrequencyMap: Map<Long, String> =
        lipFrequencyType.associate { (it.id?.toLong() ?: -1L) to (it.value ?: "Unknown") }
}

/**
 * Represents the various states of the UI.
 *
 * This sealed interface is a core component of a unidirectional data flow architecture, ensuring the UI is a
 * function of the current state and providing a clear, predictable way to manage UI behavior.
 */
sealed interface ShareApplicationUiState {

    /**
     * Represents a state where the UI is actively fetching initial data from a remote source.
     * The UI should display a loading indicator, such as a progress bar or spinner, to inform the user
     * that an operation is in progress.
     */
    data object Loading : ShareApplicationUiState

    /**
     * Indicates that a fatal error has occurred, preventing the UI from rendering correctly.
     * This state is typically used for network errors or server issues. The [message] parameter
     * provides a localized string resource to display a user-friendly error message.
     *
     * @property message The localized string resource to display.
     */
    data class Error(val message: StringResource) : ShareApplicationUiState

    /**
     * Signifies that all necessary initial data has been loaded successfully. The UI is now ready for
     * user interaction and can display all form fields and relevant information.
     */
    data object Success : ShareApplicationUiState

    /**
     * A specific error state indicating a lack of internet connectivity. This is useful for providing a
     * distinct UI experience that guides the user to check their network connection.
     */
    data object Network : ShareApplicationUiState

    /**
     * Represents a loading state that overlays the entire UI. This is used for operations that prevent
     * user interaction, such as submitting the form, to indicate that a critical process is underway.
     */
    data object OverlayLoading : ShareApplicationUiState
}

/**
 * Represents the state of a dialog to be shown on the screen.
 *
 * By managing dialog state through the ViewModel, you ensure that the dialog's visibility and content
 * are tied to the overall application state, making it resilient to configuration changes.
 */
internal sealed interface ShareApplicationDialogState {

    /**
     * A state for displaying a confirmation dialog to the user when they attempt to navigate away from
     * the form with unsaved changes.
     *
     * @property message The localized string resource for the warning text.
     */
    data class UnsavedChanges(val message: StringResource) : ShareApplicationDialogState

    /**
     * A state for showing a generic error dialog. This can be triggered by various issues, such as
     * exceeding the maximum number of submission attempts or receiving a non-specific server error.
     *
     * @property message The localized string resource for the dialog's content.
     */
    data class Error(val message: StringResource) : ShareApplicationDialogState
}

/**
 * Represents one-time, non-state-driven events that the ViewModel sends to the UI layer.
 *
 * These events are crucial for handling side effects like navigation, launching external activities,
 * or showing transient messages. Unlike [ShareApplicationUiState], which can persist, events are
 * consumed once by the UI and then forgotten.
 */
internal sealed interface ShareApplicationEvent {

    /**
     * A navigation event that signals the UI to pop the current screen from the back stack.
     */
    data object NavigateBack : ShareApplicationEvent

    /**
     * An event to initiate the authentication process. The UI receives this event and launches the
     * appropriate authentication screen.
     */
    data object NavigateToAuthentication : ShareApplicationEvent

    /**
     * An event to navigate to a status screen after a major operation (e.g., form submission).
     *
     * @property eventType The type of event (e.g., success or failure).
     * @property eventDestination The serializable name of the navigation graph to navigate to.
     * @property title The title to display on the status screen.
     * @property subtitle A descriptive subtitle for the status.
     * @property buttonText The text for the primary action button on the status screen.
     */
    data class NavigateToStatus(
        val eventType: String,
        val eventDestination: String,
        val title: String,
        val subtitle: String,
        val buttonText: String,
    ) : ShareApplicationEvent
}

/**
 * Represents user actions on the screen.
 *
 * This sealed interface defines all possible actions that a user can perform on the UI, which are
 * then handled by the ViewModel.
 */
internal sealed interface ShareApplicationAction {

    /**
     * Action triggered when the user attempts to navigate back.
     */
    data object OnNavigateBack : ShareApplicationAction

    /**
     * Action to dismiss any currently showing dialog.
     */
    data object DismissDialog : ShareApplicationAction

    /**
     * Action to retry a failed operation, such as fetching initial data.
     */
    data object Retry : ShareApplicationAction

    /**
     * Action to confirm a navigation event, typically from a dialog.
     */
    data object ConfirmNavigation : ShareApplicationAction

    /**
     * Action to initiate the authentication process.
     */
    data object NavigateToAuthentication : ShareApplicationAction

    /**
     * Action triggered when the "current price" input field changes.
     *
     * @property price The new string value of the input field.
     */
    data class CurrentPriceChange(val price: String) : ShareApplicationAction

    /**
     * Action triggered when the "total number of shares" input field changes.
     *
     * @property shares The new string value of the input field.
     */
    data class TotalNumberOfSharesChange(val shares: String) : ShareApplicationAction

    /**
     * Action triggered when the default savings account selection changes.
     *
     * @property defaultSavingsAccountId The ID of the newly selected account.
     * @property defaultSavingsAccountName The name of the newly selected account.
     */
    data class DefaultSavingsAccountChange(
        val defaultSavingsAccountId: Long,
        val defaultSavingsAccountName: String,
    ) : ShareApplicationAction

    /**
     * Action triggered when the "MAP frequency" input field changes.
     *
     * @property mapFrequency The new string value of the input field.
     */
    data class MapFrequencyChange(val mapFrequency: String) : ShareApplicationAction

    /**
     * Action triggered when the "MAP frequency type" selection changes.
     *
     * @property id The ID of the newly selected frequency type.
     * @property value The string value of the newly selected frequency type.
     */
    data class MapFrequencyTypeChange(
        val id: Long,
        val value: String,
    ) : ShareApplicationAction

    /**
     * Action triggered when the "LIP frequency" input field changes.
     *
     * @property lipFrequency The new string value of the input field.
     */
    data class LipFrequencyChange(
        val lipFrequency: String,
    ) : ShareApplicationAction

    /**
     * Action triggered when the "LIP frequency type" selection changes.
     *
     * @property id The ID of the newly selected frequency type.
     * @property value The string value of the newly selected frequency type.
     */
    data class LipFrequencyTypeChange(
        val id: Long,
        val value: String,
    ) : ShareApplicationAction

    /**
     * Action to reset the submission attempt counter.
     */
    data object RetrySubmit : ShareApplicationAction

    /**
     * Action to request the submission of the share account application.
     */
    data object RequestShareAccount : ShareApplicationAction

    /**
     * A sealed interface for internal actions, separate from UI actions. These actions are triggered
     * by the ViewModel's internal logic, such as network responses, and are not directly initiated by the UI.
     */
    sealed interface Internal : ShareApplicationAction {

        /**
         * Internal Action triggered by network status observation.
         * @property isOnline A boolean indicating if the device is online.
         */
        data class ReceiveNetworkResult(val isOnline: Boolean) : Internal

        /**
         * Action to handle the result of fetching a share product template.
         *
         * @property template The [DataState] containing the fetched template details.
         */
        data class ReceiveShareTemplate(val template: DataState<ShareProductDetails?>) : Internal

        /**
         * Action to handle the result of fetching a share product template.
         *
         * @property accounts The [DataState] containing the fetched template details.
         */
        data class ReceiveClientSavingsAccounts(val accounts: DataState<ClientAccounts>) : Internal

        /**
         * Action to handle the result of an authentication event.
         *
         * @property result A boolean indicating whether authentication was successful.
         */
        data class ReceiveAuthenticationResult(val result: Boolean) : Internal

        /**
         * Action to handle the result of the share application submission.
         *
         * @property result The [DataState] containing the result of the submission.
         */
        data class ReceiveShareApplicationResult(val result: DataState<String>) : Internal
    }
}

/**
 * Represents the result of a validation process.
 *
 * This sealed class provides a structured way to return the outcome of a validation check,
 * whether it's a success or a failure with a specific error message.
 */
sealed class ValidationResult {

    /**
     * Indicates that the validation passed successfully.
     */
    data object Success : ValidationResult()

    /**
     * Indicates that the validation failed.
     *
     * @property message The localized string resource for the error message.
     */
    data class Error(val message: StringResource) : ValidationResult()
}

/**
 * Converts a `Currency` object to a `ModelCurrency` object.
 *
 * @return The converted `ModelCurrency` object.
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

/**
 * Converts an `AccountingMappings` object to a list of `AccountingItem`s.
 *
 * @return A list of `AccountingItem` objects.
 */
fun AccountingMappings.toAccountList(): List<AccountingItem> {
    return listOfNotNull(
        shareReferenceId,
        incomeFromFeeAccountId,
        shareEquityId,
        shareSuspenseId,
    )
}
