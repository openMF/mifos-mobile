/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loan.loanAccountApplication

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.loan.generated.resources.Res
import mifos_mobile.feature.loan.generated.resources.account_number
import mifos_mobile.feature.loan.generated.resources.error_fetching_template
import mifos_mobile.feature.loan.generated.resources.new_loan_application
import mifos_mobile.feature.loan.generated.resources.string_and_string
import mifos_mobile.feature.loan.generated.resources.update_loan_application
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.common.formatAmount
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.payload.LoansPayload
import org.mifos.mobile.core.model.entity.templates.loans.LoanTemplate
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.feature.loan.navigation.LoanApplicationArgs
import org.mifos.mobile.feature.loan.navigation.LoanDetails

internal class LoanApplicationViewModel(
    private val loanRepositoryImp: LoanRepository,
    private val networkMonitor: NetworkMonitor,
    userPreferencesRepositoryImpl: UserPreferencesRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<LoanApplicationState, LoanApplicationEvent, LoanApplicationAction>(
    initialState = LoanApplicationState(
        dialogState = null,
        clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
        loanId = savedStateHandle.getStateFlow<Long?>(
            key = Constants.LOAN_ID,
            initialValue = null,
        ).value,
        loanState = savedStateHandle.getStateFlow(
            key = Constants.LOAN_STATE,
            initialValue = LoanState.CREATE.name,
        ).value.let { LoanState.valueOf(it) },

    ),
) {

    private val isLoanUpdatePurposesInitialization: Boolean = true

    init {
        viewModelScope.launch {
            networkMonitor.isOnline.collect { isOnline ->
                updateState { it.copy(isOnline = isOnline) }
            }
        }

        loadLoanWithAssociations()
    }

    private fun updateState(update: (LoanApplicationState) -> LoanApplicationState) {
        mutableStateFlow.update(update)
    }

    override fun handleAction(action: LoanApplicationAction) {
        when (action) {
            is LoanApplicationAction.ProductSelected -> productSelected(action.position)
            is LoanApplicationAction.PurposeSelected -> purposeSelected(action.position)
            is LoanApplicationAction.SetDisburseDate -> setDisburseDate(action.date)
            is LoanApplicationAction.SetPrincipalAmount -> setPrincipalAmount(action.amount)
            is LoanApplicationAction.ReviewClicked -> {
                handleReviewClicked()
            }
            LoanApplicationAction.BackPress -> sendEvent(LoanApplicationEvent.NavigateBack)
            LoanApplicationAction.Retry -> loadLoanApplicationTemplate(state.loanState)
        }
    }

    private fun loadLoanWithAssociations() {
        updateState { it.copy(dialogState = LoanApplicationState.DialogState.Loading) }
        viewModelScope.launch {
            loanRepositoryImp.getLoanWithAssociations(Constants.TRANSACTIONS, state.loanId)
                .catch {
                    showError("An error occurred")
                }.collect { dataState ->
                    when (dataState) {
                        DataState.Loading -> showLoading()
                        is DataState.Success -> {
                            updateState {
                                it.copy(
                                    loanWithAssociations = dataState.data,
                                    dialogState = null,
                                )
                            }
                        }
                        is DataState.Error -> {
                            showError(dataState.message)
                        }
                    }
                }
        }
    }

    fun loadLoanApplicationTemplate(loanState: LoanState) {
        viewModelScope.launch {
            val errorMessage = getString(Res.string.error_fetching_template)
            loanRepositoryImp.template(state.clientId)
                .collect { result ->
                    when (result) {
                        is DataState.Loading -> {
                            showLoading()
                        }
                        is DataState.Success -> {
                            val loanTemplate = result.data ?: LoanTemplate()
                            updateState { it.copy(loanTemplate = loanTemplate, dialogState = null) }
                            if (loanState == LoanState.CREATE) {
                                showLoanTemplate(loanTemplate)
                            } else {
                                showUpdateLoanTemplate(loanTemplate)
                            }
                        }
                        is DataState.Error -> {
                            showError(errorMessage)
                        }
                    }
                }
        }
    }

    private fun showLoanTemplate(loanTemplate: LoanTemplate) {
        val listLoanProducts = refreshLoanProductList(loanTemplate)
        val selectedLoanProduct = listLoanProducts.firstOrNull()
        updateState {
            it.copy(
                listLoanProducts = listLoanProducts,
                selectedLoanProduct = selectedLoanProduct,
                accountNumber = loanTemplate.clientAccountNo,
                clientName = loanTemplate.clientName,
                currencyLabel = loanTemplate.currency?.displayLabel,
                principalAmount = formatAmount(loanTemplate.principal ?: 0.0),
                disbursementDate = DateHelper.formattedFullDate,
                submittedDate = DateHelper.formattedFullDate,
            )
        }

        val selectedPosition = listLoanProducts.indexOf(selectedLoanProduct)
        if (selectedPosition >= 0) {
            loadLoanApplicationTemplateByProduct(selectedPosition, LoanState.CREATE)
        }
    }

    private fun showUpdateLoanTemplate(loanTemplate: LoanTemplate) {
        val listLoanProducts = refreshLoanProductList(loanTemplate)
        updateState {
            it.copy(
                listLoanProducts = listLoanProducts,
                selectedLoanProduct = state.loanWithAssociations?.loanProductName,
                accountNumber = state.loanWithAssociations?.accountNo,
                clientName = state.loanWithAssociations?.clientName,
                currencyLabel = state.loanWithAssociations?.currency?.displayLabel,
                principalAmount = formatAmount(state.loanWithAssociations?.principal ?: 0.0),
                submittedDate = state.loanWithAssociations?.timeline?.submittedOnDate?.let { date,
                    ->
                    DateHelper.getDateAsString(date)
                },
                disbursementDate = state.loanWithAssociations?.timeline?.expectedDisbursementDate?.let { date,
                    ->
                    DateHelper.getDateAsString(date)
                },

            )
        }
    }

    private fun refreshLoanProductList(loanTemplate: LoanTemplate): List<String?> {
        val loanProductList = state.listLoanProducts.toMutableList()
        for ((_, name) in loanTemplate.productOptions) {
            if (!loanProductList.contains(name)) {
                loanProductList.add(name)
            }
        }
        return loanProductList
    }

    private fun productSelected(position: Int) {
        val selectedProduct = state.listLoanProducts.getOrNull(position)
        val productId = state.loanTemplate?.productOptions?.getOrNull(position)?.id
        updateState {
            it.copy(
                selectedLoanProduct = selectedProduct,
                selectedProductId = productId,
            )
        }
        loadLoanApplicationTemplateByProduct(position, state.loanState)
    }

    private fun loadLoanApplicationTemplateByProduct(position: Int, loanState: LoanState) {
        val productId = state.loanTemplate?.productOptions?.get(position)?.id ?: return
        viewModelScope.launch {
            val errorMessage = getString(Res.string.error_fetching_template)
            loanRepositoryImp.getLoanTemplateByProduct(
                clientId = state.clientId,
                productId = productId,
            )
                .collect { result ->
                    when (result) {
                        is DataState.Loading -> {
                            showLoading()
                        }
                        is DataState.Success -> {
                            result.data?.let {
                                if (loanState == LoanState.CREATE) {
                                    showLoanTemplateByProduct(loanTemplate = it)
                                } else {
                                    showUpdateLoanTemplateByProduct(loanTemplate = it)
                                }
                            }
                            updateState { it.copy(dialogState = null) }
                        }
                        is DataState.Error -> {
                            showError(errorMessage)
                        }
                    }
                }
        }
    }

    private fun showLoanTemplateByProduct(loanTemplate: LoanTemplate) {
        val loanPurposeList = refreshLoanPurposeList(loanTemplate)
        updateState {
            it.copy(
                listLoanPurpose = loanPurposeList,
                selectedLoanPurpose = loanPurposeList.firstOrNull(),
                accountNumber = loanTemplate.clientAccountNo,
                clientName = loanTemplate.clientName,
                currencyLabel = loanTemplate.currency?.displayLabel,
                principalAmount = formatAmount(loanTemplate.principal ?: 0.0),
            )
        }
    }

    private fun showUpdateLoanTemplateByProduct(loanTemplate: LoanTemplate) {
        val loanPurposeList = refreshLoanPurposeList(loanTemplate = loanTemplate)
        if (isLoanUpdatePurposesInitialization && state.loanWithAssociations?.loanPurposeName != null) {
            updateState {
                it.copy(
                    listLoanPurpose = loanPurposeList,
                    selectedLoanPurpose = loanPurposeList[0],
                )
            }
        } else {
            updateState {
                it.copy(
                    listLoanPurpose = loanPurposeList,
                    selectedLoanPurpose = state.loanWithAssociations?.loanPurposeName,
                    accountNumber = loanTemplate.clientAccountNo,
                    clientName = loanTemplate.clientName,
                    currencyLabel = loanTemplate.currency?.displayLabel,
                    principalAmount = formatAmount(loanTemplate.principal ?: 0.0),
                )
            }
        }
    }

    private fun refreshLoanPurposeList(loanTemplate: LoanTemplate): MutableList<String?> {
        val loanPurposeList = mutableListOf<String?>()
        loanPurposeList.add("Purpose not provided")
        for (loanPurposeOptions in loanTemplate.loanPurposeOptions) {
            loanPurposeList.add(loanPurposeOptions.name)
        }
        return loanPurposeList
    }

    private fun purposeSelected(position: Int) {
        val selectedPurpose = state.listLoanPurpose.getOrNull(position)
        if (selectedPurpose != null) {
            updateState {
                it.copy(selectedLoanPurpose = selectedPurpose)
            }
        }
    }

    private fun setDisburseDate(date: String) {
        updateState { it.copy(disbursementDate = date) }
    }

    private fun setPrincipalAmount(amount: String) {
        updateState { it.copy(principalAmount = amount) }
    }

    private fun getLoanPayload() = LoansPayload(
        locale = "en",
        dateFormat = "dd MMMM yyyy",
        productId = when (state.loanState) {
            LoanState.CREATE -> state.selectedProductId
            else -> state.loanWithAssociations?.loanProductId
        },
        principal = state.principalAmount?.toDoubleOrNull() ?: 0.0,
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
        transactionProcessingStrategyId = state.loanTemplate?.transactionProcessingStrategyId,
        clientId = state.loanTemplate?.clientId?.takeIf { state.loanState == LoanState.CREATE },
        loanPurposeId = state.loanPurposeId?.takeIf { it > 0 },
        loanType = "individual".takeIf { state.loanState == LoanState.CREATE },
        submittedOnDate = state.loanWithAssociations?.timeline?.submittedOnDate?.let {
            DateHelper.getDateAsString(it)
        },
    )

    private fun handleReviewClicked() {
        viewModelScope.launch {
            val payload = getLoanPayload()

            val loanName = getString(
                Res.string.string_and_string,
                getString(
                    if (state.loanState == LoanState.CREATE) {
                        Res.string.new_loan_application
                    } else {
                        Res.string.update_loan_application
                    },
                ),
                state.loanWithAssociations?.clientName ?: "",
            )

            val accountNo = getString(
                Res.string.string_and_string,
                getString(Res.string.account_number),
                state.loanWithAssociations?.accountNo ?: "",
            )

            val loanDetails = LoanDetails(
                loanState = state.loanState,
                loanId = state.loanId,
                loanName = loanName,
                accountNo = accountNo,
                loanPurpose = state.selectedLoanPurpose ?: "Not Provided",
                loanProduct = state.selectedLoanProduct ?: "",
                currency = state.loanWithAssociations?.currency?.code
                    ?: state.loanWithAssociations?.currency?.displaySymbol
                    ?: "",
            )

            val args = LoanApplicationArgs(
                loansPayload = payload,
                loanDetails = loanDetails,
            )

            val event = when (state.loanState) {
                LoanState.CREATE -> LoanApplicationEvent.ReviewLoanApplication(args)
                LoanState.UPDATE -> LoanApplicationEvent.SubmitUpdateLoanApplication(args)
            }

            sendEvent(event)
        }
    }

    private fun showLoading() = updateState {
        it.copy(dialogState = LoanApplicationState.DialogState.Loading)
    }

    private fun showError(message: String) = updateState {
        it.copy(dialogState = LoanApplicationState.DialogState.Error(message))
    }
}

@Parcelize
data class LoanApplicationState(
    val clientId: Long? = null,
    val isOnline: Boolean = false,
    val loanState: LoanState = LoanState.CREATE,
    val loanId: Long? = null,
    val loanWithAssociations: LoanWithAssociations? = null,
    val loanTemplate: LoanTemplate? = null,
    val errorMessage: String? = null,
    val listLoanProducts: List<String?> = listOf(),
    val selectedLoanProduct: String? = null,
    val listLoanPurpose: List<String?> = listOf(),
    val selectedLoanPurpose: String? = null,
    val loanPurposeId: Int? = null,
    val selectedProductId: Int? = null,
    val principalAmount: String? = null,
    val currencyLabel: String? = null,
    val accountNumber: String? = null,
    val clientName: String? = null,
    val disbursementDate: String? = null,
    val submittedDate: String? = null,
    val dialogState: DialogState?,
) : Parcelable {
    sealed interface DialogState : Parcelable {
        @Parcelize
        data object Loading : DialogState

        @Parcelize
        data class Error(val message: String) : DialogState
    }
}

sealed interface LoanApplicationEvent {
    data object NavigateBack : LoanApplicationEvent
    data class ReviewLoanApplication(
        val loanApplicationArgs: LoanApplicationArgs,
    ) : LoanApplicationEvent

    data class SubmitUpdateLoanApplication(
        val loanApplicationArgs: LoanApplicationArgs,
    ) : LoanApplicationEvent
}

sealed interface LoanApplicationAction {
    data class ProductSelected(val position: Int) : LoanApplicationAction
    data class PurposeSelected(val position: Int) : LoanApplicationAction
    data class SetDisburseDate(val date: String) : LoanApplicationAction
    data class SetPrincipalAmount(val amount: String) : LoanApplicationAction
    data object BackPress : LoanApplicationAction
    data object Retry : LoanApplicationAction
    data object ReviewClicked : LoanApplicationAction
}
