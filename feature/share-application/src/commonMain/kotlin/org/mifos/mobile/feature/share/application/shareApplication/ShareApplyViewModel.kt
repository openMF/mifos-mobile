/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.share.application.shareApplication

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import mifos_mobile.feature.share_application.generated.resources.Res
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_error_server
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_error_submit_failed
import mifos_mobile.feature.share_application.generated.resources.feature_apply_share_unsaved_changes_message
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.data.repository.HomeRepository
import org.mifos.mobile.core.data.repository.ShareAccountRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.core.model.entity.templates.shares.SharePageItem
import org.mifos.mobile.core.model.entity.templates.shares.ShareProduct
import org.mifos.mobile.core.ui.utils.BaseViewModel
/**
 * `ViewModel` for the savings application screen, responsible for handling user input,
 * business logic, and UI state management.
 *
 * It uses a [BaseViewModel] as its foundation to manage [ShareApplicationState],
 * handle [ShareApplicationAction] from the UI, and send [ShareApplicationEvent]
 * to trigger one-time UI side effects.
 *
 * @property userPreferencesRepositoryImpl Repository for accessing user preferences, specifically the client ID.
 * @property shareAccountRepositoryImpl Repository for interacting with savings-related data from a remote source.
 * @property homeRepositoryImpl Repository for accessing home-related data, such as client information.
 * @property networkMonitor Monitors the network connectivity status.
 */
@Suppress("CyclomaticComplexMethod", "TooManyFunctions")
internal class ShareApplyViewModel(
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
    private val shareAccountRepositoryImpl: ShareAccountRepository,
    private val homeRepositoryImpl: HomeRepository,
    private val networkMonitor: NetworkMonitor,
) : BaseViewModel<ShareApplicationState, ShareApplicationEvent, ShareApplicationAction>(
    initialState = ShareApplicationState(
        clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
        applicantName = "",
        uiState = ShareApplicationUiState.Loading,
    ),
) {

    init {
        observeNetworkStatus()
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
                            uiState = if (!isOnline) {
                                ShareApplicationUiState.Network
                            } else {
                                null
                            },
                        )
                    }

                    if (isOnline) {
                        fetchClient()
                        fetchShareTemplate()
                    }
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
                    showErrorDialog(Res.string.feature_apply_share_error_server)
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
    private fun fetchShareTemplate() {
        viewModelScope.launch {
            shareAccountRepositoryImpl.getShareProducts(state.clientId)
                .collect { result ->
                    sendAction(ShareApplicationAction.Internal.ReceiveShareTemplate(result))
                }
        }
    }

    /**
     * Handles the result of the `fetchShareTemplate` network call.
     * Updates the state with product options and currency on success,
     * or displays an error and navigates back on failure.
     *
     * @param template The [DataState] containing the savings template data.
     */
    private fun handleShareTemplate(template: DataState<Page<ShareProduct>>) {
        when (template) {
            is DataState.Loading -> showLoading()
            is DataState.Success -> {
                val products = template.data.pageItems.flatMap { it.pageItems ?: emptyList() }

                updateState {
                    it.copy(
                        productOptions = products,
                        uiState = if (products.isEmpty()) {
                            ShareApplicationUiState.Empty
                        } else {
                            ShareApplicationUiState.Success
                        },
                        dialogState = null,
                    )
                }
            }

            is DataState.Error -> {
                showErrorDialog(Res.string.feature_apply_share_error_server)
            }
        }
    }

    /**
     * Retries the data fetching process. If the network is unavailable, it shows
     * a network error dialog. Otherwise, it triggers the `fetchShareTemplate` `fetchClient`,
     */
    private fun retry() {
        viewModelScope.launch {
            if (!state.networkStatus) {
                updateState { it.copy(uiState = ShareApplicationUiState.Network) }
            } else {
                fetchClient()
                fetchShareTemplate()
            }
        }
    }

    /*
     * Functions related to UI State and Dialogs
     */

    /**
     * A helper function to update the mutable state flow.
     *
     * @param update A lambda function that takes the current state and returns a new state.
     */
    private fun updateState(update: (ShareApplicationState) -> ShareApplicationState) {
        mutableStateFlow.update(update)
    }

    /**
     * Sets the dialog state to an overlay loading spinner.
     */
    private fun showOverlayLoading() {
        updateState { it.copy(uiState = ShareApplicationUiState.OverlayLoading) }
    }

    /**
     * Sets the dialog state to a full-screen loading spinner.
     */
    private fun showLoading() {
        updateState { it.copy(uiState = ShareApplicationUiState.Loading) }
    }

    /**
     * Displays an error dialog with a given message.
     *
     * @param error The [StringResource] for the error message to display.
     */
    private fun showErrorDialog(error: StringResource) {
        updateState { it.copy(uiState = ShareApplicationUiState.Error(error)) }
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
     * Handles incoming actions from the UI and dispatches them to the appropriate
     * business logic functions.
     *
     * @param action The [ShareApplicationAction] to be handled.
     */
    override fun handleAction(action: ShareApplicationAction) {
        when (action) {
            is ShareApplicationAction.ShareProductChange -> {
                onShareProductChange(action.id, action.name)
            }

            is ShareApplicationAction.NavigateToConfirmDetails -> validateAndSubmit()

            is ShareApplicationAction.OnNavigateBack -> navigateBack()

            is ShareApplicationAction.ConfirmNavigation -> sendEvent(ShareApplicationEvent.NavigateBack)

            is ShareApplicationAction.DismissDialog -> dismissDialog()

            is ShareApplicationAction.Internal.ReceiveShareTemplate -> handleShareTemplate(action.template)

            ShareApplicationAction.Retry -> retry()
        }
    }

    /**
     * Handles changes to the selected savings product.
     * It updates the state, fetches the corresponding field officer options,
     * and debounces validation.
     *
     * @param id The ID of the selected savings product.
     * @param name The name of the selected savings product.
     */
    private fun onShareProductChange(id: Long, name: String) {
        mutableStateFlow.update {
            it.copy(
                selectedShareProduct = name,
                selectedShareProductId = id,
                hasChanges = true,
            )
        }
    }

    /**
     * Validates all form fields before submitting. If validation is successful,
     * it proceeds to `handleSubmit`.
     */
    private fun validateAndSubmit() {
        handleSubmit()
    }

    /**
     * Handles the successful submission of the savings application.
     * It shows a loading overlay and sends an event to navigate to the confirmation screen.
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
                sendEvent(ShareApplicationEvent.NavigateToConfirmDetailsScreen)
            } catch (e: Exception) {
                showErrorDialog(Res.string.feature_apply_share_error_submit_failed)
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
                    dialogState = ShareApplicationDialogState.UnsavedChanges(
                        Res.string.feature_apply_share_unsaved_changes_message,
                    ),
                )
            }
        } else {
            sendEvent(ShareApplicationEvent.NavigateBack)
        }
    }
}

/**
 * Represents the UI state for the savings application screen.
 *
 * @property clientId The ID of the current client.
 * @property applicantName The name of the applicant.
 * @property productOptions A list of available savings product options.
 * @property savingsProductTemplate The full savings template object for the selected product.
 * @property hasChanges A boolean indicating if there are unsaved changes.
 * @property networkStatus A boolean indicating if the network is unavailable.
 * @property uiState The primary UI state of the screen (e.g., Loading, Empty, Success).
 * @property dialogState The state of any dialogs that overlay the main content.
 */
@OptIn(ExperimentalMaterial3Api::class)
internal data class ShareApplicationState(
    val clientId: Long,
    val applicantName: String,
    val productOptions: List<SharePageItem> = emptyList(),
    val selectedShareProduct: String = "",
    val selectedShareProductId: Long = 0,
    val dialogState: ShareApplicationDialogState? = null,
    val savingsProductTemplate: SavingsAccountTemplate? = null,
    val hasChanges: Boolean = false,
    val networkStatus: Boolean = false,
    val uiState: ShareApplicationUiState?,
) {
    /**
     * A boolean indicating if the entire form is valid for submission.
     * This is based on the absence of errors and non-empty fields.
     */
    val isFormValid: Boolean
        get() = applicantName.isNotBlank() &&
            selectedShareProduct.isNotBlank()

    /**
     * A map of savings product IDs to their names, derived from `productOptions`.
     */
    val productOptionsMap: Map<Long, String> = productOptions
        .mapNotNull { option ->
            val id = option.id?.toLong()
            val name = option.name
            if (id != null && name != null) id to name else null
        }
        .toMap()

    val submittedOnDate: String
        get() {
            val todayMillis = Clock.System.now().toEpochMilliseconds()
            return DateHelper.getDateMonthYearString(todayMillis)
        }
}

/**
 * A sealed interface representing the different types of full-screen UI states
 * for the savings application screen.
 */
sealed interface ShareApplicationUiState {
    /** Represents a full-screen loading state. */
    data object Loading : ShareApplicationUiState

    /** Represents an empty state where no data is available. */
    data object Empty : ShareApplicationUiState

    /**
     * Represents an error state with a message.
     * @property message The [StringResource] for the error message.
     */
    data class Error(val message: StringResource) : ShareApplicationUiState

    /** Represents a successful state where content can be displayed. */
    data object Success : ShareApplicationUiState

    /** Represents a state where there is a network connectivity issue. */
    data object Network : ShareApplicationUiState

    /** Represents a state where an overlay loading spinner should be shown. */
    data object OverlayLoading : ShareApplicationUiState
}

/**
 * A sealed interface representing the different types of dialogs that can be
 * shown on the savings application screen.
 */
internal sealed interface ShareApplicationDialogState {
    /**
     * Represents a dialog to confirm navigation with unsaved changes.
     * @property message The [StringResource] for the confirmation message.
     */
    data class UnsavedChanges(val message: StringResource) : ShareApplicationDialogState
}

/**
 * A sealed interface representing one-time events that trigger UI side effects.
 */
internal sealed interface ShareApplicationEvent {
    /** Navigates back from the current screen. */
    data object NavigateBack : ShareApplicationEvent

    /** Navigates to the confirmation details screen. */
    data object NavigateToConfirmDetailsScreen : ShareApplicationEvent
}

/**
 * A sealed interface representing user actions or internal events that the
 * ViewModel needs to handle.
 */
internal sealed interface ShareApplicationAction {
    /** User action to navigate back. */
    data object OnNavigateBack : ShareApplicationAction

    /** User action to dismiss a dialog. */
    data object DismissDialog : ShareApplicationAction

    /** User action to retry a failed operation. */
    data object Retry : ShareApplicationAction

    /** User action to confirm navigation (e.g., dismissing an unsaved changes dialog). */
    data object ConfirmNavigation : ShareApplicationAction

    /**
     * User action when a savings product is selected.
     * @property id The ID of the selected savings product.
     * @property name The name of the selected savings product.
     */
    data class ShareProductChange(val id: Long, val name: String) : ShareApplicationAction

    /** User action to navigate to the confirm details screen, triggering form validation. */
    data object NavigateToConfirmDetails : ShareApplicationAction

    /**
     * A sealed interface for internal actions, which are not triggered directly by the UI.
     */
    sealed interface Internal : ShareApplicationAction {

        /**
         * An internal action to handle the result of fetching a savings template.
         * @property template The [DataState] containing the savings template data.
         */
        data class ReceiveShareTemplate(
            val template: DataState<Page<ShareProduct>>,
        ) : Internal
    }
}
