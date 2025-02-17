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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import mifos_mobile.feature.loan.generated.resources.Res
import mifos_mobile.feature.loan.generated.resources.error_fetching_template
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.common.formatAmount
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesDataSource
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.templates.loans.LoanTemplate
import org.mifos.mobile.core.model.enums.LoanState
import org.mifos.mobile.feature.loan.loanAccountApplication.LoanApplicationUiState.Loading

internal class LoanApplicationViewModel(
    private val loanRepositoryImp: LoanRepository,
    userPreferencesDataSource: UserPreferencesDataSource,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var _loanUiState: MutableStateFlow<LoanApplicationUiState> = MutableStateFlow(Loading)
    var loanUiState: StateFlow<LoanApplicationUiState> = _loanUiState

    private val _isOnline = MutableStateFlow(false)
    val isOnline: StateFlow<Boolean> = _isOnline

    val loanId = savedStateHandle.getStateFlow<Long?>(key = Constants.LOAN_ID, initialValue = null)
    val loanState = savedStateHandle.getStateFlow(
        key = Constants.LOAN_STATE,
        initialValue = LoanState.CREATE,
    )
    private val clientId: StateFlow<Long?> = userPreferencesDataSource.clientId
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null,
        )

    private val _loanWithAssociations = MutableStateFlow<LoanWithAssociations?>(null)
    val loanWithAssociations: StateFlow<LoanWithAssociations?> = _loanWithAssociations

    fun loadLoanWithAssociations() {
        viewModelScope.launch {
            loanRepositoryImp.getLoanWithAssociations(Constants.TRANSACTIONS, loanId.value)
                .collect { dataState ->
                    _loanWithAssociations.value = when (dataState) {
                        is DataState.Success -> dataState.data
                        else -> null
                    }
                }
        }
    }

    private val _loanApplicationScreenData = MutableStateFlow(LoanApplicationScreenData())
    val loanApplicationScreenData: StateFlow<LoanApplicationScreenData> = _loanApplicationScreenData

    var loanTemplate: LoanTemplate = LoanTemplate()
    var productId: Int = 0
    var purposeId: Int = 0
    private var isLoanUpdatePurposesInitialization: Boolean = true

    init {
        _loanApplicationScreenData.update {
            it.copy(
                submittedDate = DateHelper.formattedFullDate,
                disbursementDate = DateHelper.formattedFullDate,
            )
        }

        viewModelScope.launch {
            networkMonitor.isOnline
                .collect { isOnline ->
                    _isOnline.value = isOnline
                }
        }
        loadLoanWithAssociations()
    }

    fun loadLoanApplicationTemplate(loanState: LoanState) {
        viewModelScope.launch {
            val errorMessage = getString(Res.string.error_fetching_template)
            loanRepositoryImp.template(clientId.value)
                .collect { result ->
                    val uiState = when (result) {
                        is DataState.Success -> {
                            loanTemplate = result.data ?: LoanTemplate()
                            if (loanState == LoanState.CREATE) {
                                showLoanTemplate(loanTemplate = loanTemplate)
                            } else {
                                showUpdateLoanTemplate(loanTemplate = loanTemplate)
                            }
                            LoanApplicationUiState.Success
                        }

                        is DataState.Loading -> Loading
                        is DataState.Error -> LoanApplicationUiState.Error(errorMessage)
                        else -> LoanApplicationUiState.Error("An Error occurred")
                    }
                    _loanUiState.value = uiState
                }
        }
    }

    private fun loadLoanApplicationTemplateByProduct(productId: Int?, loanState: LoanState) {
        viewModelScope.launch {
            val errorMessage = getString(Res.string.error_fetching_template)
            loanRepositoryImp.getLoanTemplateByProduct(clientId = clientId.value, productId = productId)
                .collect { result ->
                    val uiState = when (result) {
                        is DataState.Success -> {
                            result.data?.let {
                                if (loanState == LoanState.CREATE) {
                                    showLoanTemplateByProduct(loanTemplate = it)
                                } else {
                                    showUpdateLoanTemplateByProduct(loanTemplate = it)
                                }
                            }
                            LoanApplicationUiState.Success
                        }

                        is DataState.Loading -> Loading
                        is DataState.Error -> LoanApplicationUiState.Error(errorMessage)
                        else -> LoanApplicationUiState.Error("An Error occurred")
                    }
                    _loanUiState.value = uiState
                }
        }
    }

    private fun showLoanTemplate(loanTemplate: LoanTemplate) {
        val listLoanProducts = refreshLoanProductList(loanTemplate = loanTemplate)
        _loanApplicationScreenData.update {
            it.copy(listLoanProducts = listLoanProducts)
        }
    }

    private fun showUpdateLoanTemplate(loanTemplate: LoanTemplate) {
        val listLoanProducts = refreshLoanProductList(loanTemplate = loanTemplate)
        _loanApplicationScreenData.update {
            it.copy(
                listLoanProducts = listLoanProducts,
                selectedLoanProduct = loanWithAssociations.value?.loanProductName,
                accountNumber = loanWithAssociations.value?.accountNo,
                clientName = loanWithAssociations.value?.clientName,
                currencyLabel = loanWithAssociations.value?.currency?.displayLabel,
                principalAmount = formatAmount(loanWithAssociations.value?.principal ?: 0.0),
                submittedDate = loanWithAssociations.value?.timeline?.submittedOnDate
                    ?.map { date -> date.toLong() }
                    ?.let { date -> DateHelper.getDateAsString(date, "dd-MM-yyyy") },
                disbursementDate = loanWithAssociations.value?.timeline?.expectedDisbursementDate
                    ?.map { date -> date.toLong() }
                    ?.let { date -> DateHelper.getDateAsString(date, "dd-MM-yyyy") },
            )
        }
    }

    private fun showLoanTemplateByProduct(loanTemplate: LoanTemplate) {
        val loanPurposeList = refreshLoanPurposeList(loanTemplate = loanTemplate)
        _loanApplicationScreenData.update {
            it.copy(
                listLoanPurpose = loanPurposeList,
                selectedLoanPurpose = loanPurposeList[0],
                accountNumber = loanTemplate.clientAccountNo,
                clientName = loanTemplate.clientName,
                currencyLabel = loanTemplate.currency?.displayLabel,
                principalAmount = formatAmount(loanTemplate.principal ?: 0.0),
            )
        }
    }

    private fun showUpdateLoanTemplateByProduct(loanTemplate: LoanTemplate) {
        val loanPurposeList = refreshLoanPurposeList(loanTemplate = loanTemplate)
        if (isLoanUpdatePurposesInitialization && loanWithAssociations.value?.loanPurposeName != null) {
            _loanApplicationScreenData.update {
                it.copy(
                    listLoanPurpose = loanPurposeList,
                    selectedLoanPurpose = loanPurposeList[0],
                )
            }
        } else {
            _loanApplicationScreenData.update {
                it.copy(
                    listLoanPurpose = loanPurposeList,
                    selectedLoanPurpose = loanWithAssociations.value?.loanPurposeName,
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

    private fun refreshLoanProductList(loanTemplate: LoanTemplate): List<String?> {
        val loanProductList = _loanApplicationScreenData.value.listLoanProducts.toMutableList()
        for ((_, name) in loanTemplate.productOptions) {
            if (!loanProductList.contains(name)) {
                loanProductList.add(name)
            }
        }
        return loanProductList
    }

    fun productSelected(position: Int) {
        productId = loanTemplate.productOptions[position].id ?: 0
        loadLoanApplicationTemplateByProduct(productId, loanState.value)
        _loanApplicationScreenData.update {
            it.copy(selectedLoanProduct = loanApplicationScreenData.value.listLoanProducts[position])
        }
    }

    fun purposeSelected(position: Int) {
        loanTemplate.loanPurposeOptions.let {
            if (it.size > position) {
                purposeId = loanTemplate.loanPurposeOptions[position].id ?: 0
            }
        }
        _loanApplicationScreenData.update {
            it.copy(selectedLoanPurpose = loanApplicationScreenData.value.listLoanPurpose[position])
        }
    }

    fun setDisburseDate(date: String) {
        _loanApplicationScreenData.update { it.copy(disbursementDate = date) }
    }

    fun setPrincipalAmount(amount: String) {
        _loanApplicationScreenData.update { it.copy(principalAmount = amount) }
    }
}

internal data class LoanApplicationScreenData(
    var accountNumber: String? = null,
    var clientName: String? = null,
    var listLoanProducts: List<String?> = listOf(),
    var selectedLoanProduct: String? = null,
    var listLoanPurpose: List<String?> = listOf(),
    var selectedLoanPurpose: String? = null,
    var principalAmount: String? = null,
    var currencyLabel: String? = null,
    var selectedDisbursementDate: Instant? = null,
    var disbursementDate: String? = null,
    var submittedDate: String? = null,
)

internal sealed class LoanApplicationUiState {
    data object Loading : LoanApplicationUiState()
    data object Success : LoanApplicationUiState()
    data class Error(val errorMessageId: String) : LoanApplicationUiState()
}
