/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.application.confirmDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.loan_application.generated.resources.Res
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_error_server
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_label_applicant_name
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_label_disbursement_date
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_label_loan_product
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_label_principal_amount
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_label_purpose
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_status_failure
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_status_failure_action
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_status_success
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_status_success_action
import mifos_mobile.feature.loan_application.generated.resources.feature_apply_loan_status_success_tip
import okio.IOException
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.data.repository.ReviewLoanApplicationRepository
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.EventType
import org.mifos.mobile.core.model.StatusNavigationDestination
import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.model.entity.templates.loans.LoanTemplate
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.core.ui.utils.AuthResult
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ResultNavigator
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.core.ui.utils.observe

/**
 * `ViewModel` for the confirm details screen of the loan application process.
 *
 * This ViewModel is responsible for:
 * - Receiving and displaying loan application details from the previous screen.
 * - Handling user authentication via a passcode.
 * - Fetching the full loan template for the selected product.
 * - Submitting the loan application to the server.
 * - Handling success and failure states of the submission and navigating accordingly.
 *
 * It uses a [BaseViewModel] to manage its state ([ConfirmDetailsState]),
 * handle actions ([ConfirmDetailsAction]), and emit events ([ConfirmDetailsEvent]).
 *
 * @param userPreferencesRepositoryImpl Repository for accessing user preferences, such as client ID.
 * @param reviewLoanApplicationRepositoryImpl Repository for submitting the loan application.
 * @param loanAccountRepositoryImp Repository for fetching loan-related data.
 * @param resultNavigator A navigator to observe and receive results from other screens, like authentication.
 * @param savedStateHandle A handle to saved state data, used to retrieve navigation arguments.
 */
internal class ConfirmDetailsViewModel(
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
    private val reviewLoanApplicationRepositoryImpl: ReviewLoanApplicationRepository,
    private val loanAccountRepositoryImp: LoanRepository,
    private val resultNavigator: ResultNavigator,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ConfirmDetailsState, ConfirmDetailsEvent, ConfirmDetailsAction>(
    initialState = run {
        val route = savedStateHandle.toRoute<ConfirmDetailsRoute>()
        val detailMap = mapOf(
            Res.string.feature_apply_loan_label_applicant_name to route.applicantName,
            Res.string.feature_apply_loan_label_loan_product to route.loanProductName,
            Res.string.feature_apply_loan_label_purpose to route.loanPurpose,
            Res.string.feature_apply_loan_label_disbursement_date to route.disbursementDate,
            Res.string.feature_apply_loan_label_principal_amount to route.principalAmount,
        )

        ConfirmDetailsState(
            clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
            details = detailMap,
            loanProductId = route.loanProductId,
            loanProductName = route.loanProductName,
            loanPurpose = route.loanPurpose,
            disbursementDate = route.disbursementDate,
            principalAmount = route.principalAmount,
        )
    },
) {

    init {
        observeAuthResult()
    }

    /**
     * Observes the result from the authentication screen.
     * If the authentication is successful, it triggers the loan application flow.
     */
    private fun observeAuthResult() {
        viewModelScope.launch {
            resultNavigator.observe<AuthResult>()
                .collect { result ->
                    sendAction(ConfirmDetailsAction.Internal.ReceiveAuthenticationResult(result.success))
                }
        }
    }

    /**
     * Helper function to update the mutable state flow.
     *
     * @param update A lambda function that takes the current state and returns a new state.
     */
    private fun updateState(update: (ConfirmDetailsState) -> ConfirmDetailsState) {
        mutableStateFlow.update(update)
    }

    /**
     * Sets the dialog state to a full-screen loading spinner.
     */
    @Suppress("UnusedPrivateMember")
    private fun showLoading() {
        updateState { it.copy(uiState = ScreenUiState.Loading) }
    }

    /**
     * Sets the dialog state to an overlay loading spinner.
     */
    private fun showOverlayLoading() {
        updateState { it.copy(showOverlay = !state.showOverlay) }
    }

    /**
     * Displays an error dialog with a given message.
     *
     * @param error The [StringResource] for the error message to display.
     */
    @Suppress("UnusedPrivateMember")
    private fun showErrorDialog(error: StringResource) {
        updateState { it.copy(dialogState = ConfirmDetailsDialogState.Error(error)) }
    }

    /**
     * Handles incoming actions from the UI and dispatches them to the appropriate
     * business logic functions.
     *
     * @param action The [ConfirmDetailsAction] to be handled.
     */
    override fun handleAction(action: ConfirmDetailsAction) {
        when (action) {
            ConfirmDetailsAction.OnNavigateBack -> {
                sendEvent(ConfirmDetailsEvent.NavigateBack)
            }

            ConfirmDetailsAction.NavigateToAuthenticate -> sendEvent(ConfirmDetailsEvent.NavigateToAuthenticate())

            is ConfirmDetailsAction.Internal.ReceiveAuthenticationResult -> {
                handleLoanApplyRequest(action.result)
            }

            ConfirmDetailsAction.Internal.ApplyLoan -> appyLoan()

            ConfirmDetailsAction.FetchProductDetails -> fetchProductDetails()

            is ConfirmDetailsAction.Internal.ReceiveProductDetails -> {
                viewModelScope.launch {
                    handleProductDetails(action.loanTemplate)
                }
            }

            is ConfirmDetailsAction.Internal.ReceiveLoanApplyStatus -> {
                viewModelScope.launch {
                    handleLoanApplyStatus(action.status)
                }
            }

            is ConfirmDetailsAction.DismissDialog -> dismissDialog()
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
     * it proceeds to fetch the product details before submitting the loan.
     *
     * @param isAuthenticated A boolean indicating if the user was successfully authenticated.
     */
    private fun handleLoanApplyRequest(isAuthenticated: Boolean) {
        if (isAuthenticated) {
            viewModelScope.launch {
                sendAction(ConfirmDetailsAction.FetchProductDetails)
            }
        }
    }

    /**
     * Fetches the full loan template for the selected product.
     */
    private fun fetchProductDetails() {
        showOverlayLoading()
        viewModelScope.launch {
            loanAccountRepositoryImp.getLoanTemplateByProduct(
                state.clientId,
                state.loanProductId.toInt(),
            )
                .collect { result ->
                    sendAction(ConfirmDetailsAction.Internal.ReceiveProductDetails(result))
                }
        }
    }

    /**
     * Handles the result of the `fetchProductDetails` network call.
     * On success, it stores the template in the state and triggers the loan submission.
     *
     * @param template The [DataState] containing the loan template data.
     */
    private suspend fun handleProductDetails(template: DataState<LoanTemplate?>) {
        when (template) {
            is DataState.Loading -> showOverlayLoading()
            is DataState.Success -> {
                updateState {
                    it.copy(
                        showOverlay = false,
                        loanTemplate = template.data,
                    )
                }
                sendAction(ConfirmDetailsAction.Internal.ApplyLoan)
            }
            is DataState.Error -> {
                updateState {
                    it.copy(
                        showOverlay = false,
                        uiState = if (template.exception is IOException) {
                            ScreenUiState.Network
                        } else {
                            ScreenUiState.Error(Res.string.feature_apply_loan_error_server)
                        },
                    )
                }
            }
        }
    }

    /**
     * Submits the loan application to the server using the data from the state
     * and the fetched loan template.
     */
    private fun appyLoan() {
        showOverlayLoading()
        viewModelScope.launch {
            val response = reviewLoanApplicationRepositoryImpl.submitLoan(
                LoanState.CREATE,
                getLoanPayload(),
                loanId = -1,
            )
            dismissDialog()
            sendAction(ConfirmDetailsAction.Internal.ReceiveLoanApplyStatus(response))
        }
    }

    /**
     * Handles the result of the `applyLoan` network call.
     * On success, it navigates to a success status screen.
     * On failure, it navigates to a failure status screen.
     *
     * @param status The [DataState] containing the status of the loan submission.
     */
    private suspend fun handleLoanApplyStatus(status: DataState<String>) {
        when (status) {
            is DataState.Error -> {
                updateState {
                    it.copy(showOverlay = false)
                }
                sendEvent(
                    ConfirmDetailsEvent.NavigateToStatus(
                        eventType = EventType.FAILURE.name,
                        eventDestination = StatusNavigationDestination.PREVIOUS_SCREEN.name,
                        title = getString(Res.string.feature_apply_loan_status_failure),
                        subtitle = status.message,
                        buttonText = getString(Res.string.feature_apply_loan_status_failure_action),
                    ),
                )
            }
            DataState.Loading -> showOverlayLoading()
            is DataState.Success -> {
                sendEvent(
                    ConfirmDetailsEvent.NavigateToStatus(
                        eventType = EventType.SUCCESS.name,
                        eventDestination = StatusNavigationDestination.LOAN_APPLICATION.name,
                        title = getString(Res.string.feature_apply_loan_status_success),
                        subtitle = getString(Res.string.feature_apply_loan_status_success_tip),
                        buttonText = getString(Res.string.feature_apply_loan_status_success_action),
                    ),
                )
            }
        }
    }

    /**
     * Creates the payload for the loan application submission using the data
     * stored in the ViewModel's state.
     *
     * @return The [LoansPayload] object ready for submission.
     */
    private fun getLoanPayload() = LoansPayload(
        locale = "en",
        dateFormat = "dd MMMM yyyy",
        productId = state.loanProductId.toInt(),
        principal = state.principalAmount.toDoubleOrNull(),
        loanTermFrequency = state.loanTemplate?.termFrequency,
        loanTermFrequencyType = state.loanTemplate?.interestRateFrequencyType?.id,
        numberOfRepayments = state.loanTemplate?.numberOfRepayments,
        repaymentEvery = state.loanTemplate?.repaymentEvery,
        repaymentFrequencyType = state.loanTemplate?.interestRateFrequencyType?.id,
        interestRatePerPeriod = state.loanTemplate?.interestRatePerPeriod,
        interestType = state.loanTemplate?.interestType?.id,
        interestCalculationPeriodType = state.loanTemplate?.interestCalculationPeriodType?.id,
        amortizationType = state.loanTemplate?.amortizationType?.id,
        expectedDisbursementDate = state.disbursementDate,
        transactionProcessingStrategyCode = state.loanTemplate?.transactionProcessingStrategyCode,
        clientId = state.clientId.toInt(),
        loanType = "individual",
        submittedOnDate = state.disbursementDate,
    )
}

/**
 * Represents the UI state for the confirm details screen.
 *
 * @property clientId The ID of the current client.
 * @property loanProductId The ID of the selected loan product.
 * @property loanProductName The name of the selected loan product.
 * @property loanPurpose The purpose of the loan.
 * @property disbursementDate The disbursement date.
 * @property principalAmount The principal loan amount.
 * @property details A map of display labels to the corresponding detail values.
 * @property loanTemplate The full loan template object for the selected product.
 * @property dialogState The state of any dialog to be shown on the screen.
 */
internal data class ConfirmDetailsState(
    val clientId: Long,
    val loanProductId: Long,
    val loanProductName: String,
    val loanPurpose: String,
    val disbursementDate: String,
    val principalAmount: String,
    val details: Map<StringResource, String> = emptyMap(),
    val loanTemplate: LoanTemplate? = null,

    val showOverlay: Boolean = false,
    val dialogState: ConfirmDetailsDialogState? = null,
    val uiState: ScreenUiState? = ScreenUiState.Success,
)

/**
 * A sealed interface representing one-time events that trigger UI side effects
 * for the confirm details screen.
 */
sealed interface ConfirmDetailsEvent {
    /** Navigates back from the current screen. */
    data object NavigateBack : ConfirmDetailsEvent

    /**
     * Navigates to the passcode screen for authentication.
     *
     * @property status Status string used for context (defaults to SUCCESS).
     */
    data class NavigateToAuthenticate(
        val status: String = EventType.SUCCESS.name,
    ) : ConfirmDetailsEvent

    /**
     * Navigates to a generic status screen after an operation completes.
     *
     * @property eventType Status type (`SUCCESS` or `FAILURE`).
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
    ) : ConfirmDetailsEvent
}

/**
 * A sealed interface representing user actions or internal events that the
 * ViewModel needs to handle for the confirm details screen.
 */
sealed interface ConfirmDetailsAction {
    /** User action to navigate back. */
    data object OnNavigateBack : ConfirmDetailsAction

    /** User action to trigger fetching of product details. */
    data object FetchProductDetails : ConfirmDetailsAction

    /** User action to navigate to the authentication screen. */
    data object NavigateToAuthenticate : ConfirmDetailsAction

    /** User action to dismiss a dialog. */
    data object DismissDialog : ConfirmDetailsAction

    /**
     * A sealed interface for internal actions, which are not triggered directly by the UI.
     */
    sealed interface Internal : ConfirmDetailsAction {
        /**
         * Receives the result from the authentication screen.
         *
         * @property result `true` if the user was authenticated.
         */
        data class ReceiveAuthenticationResult(val result: Boolean) : Internal

        /**
         * Triggers the actual loan application submission request.
         */
        data object ApplyLoan : Internal

        /**
         * Receives the result of fetching the loan template.
         *
         * @property loanTemplate The [DataState] containing the loan template data.
         */
        data class ReceiveProductDetails(val loanTemplate: DataState<LoanTemplate?>) : Internal

        /**
         * Receives the result of the loan application submission.
         *
         * @property status The [DataState] containing the status message.
         */
        data class ReceiveLoanApplyStatus(val status: DataState<String>) : Internal
    }
}

/**
 * A sealed interface representing the different types of dialogs that can be
 * shown on the confirm details screen.
 */
internal sealed interface ConfirmDetailsDialogState {
    /**
     * Represents a generic error dialog with a message.
     * @property message The [StringResource] for the error message.
     */
    data class Error(val message: StringResource) : ConfirmDetailsDialogState
}
