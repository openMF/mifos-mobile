package org.mifos.mobile.ui.loan_account_application

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.models.accounts.loan.LoanWithAssociations
import org.mifos.mobile.models.templates.loans.LoanTemplate
import org.mifos.mobile.repositories.LoanRepository
import org.mifos.mobile.ui.enums.LoanState
import org.mifos.mobile.ui.loan_account_transaction.LoanAccountTransactionUiState
import org.mifos.mobile.utils.DateHelper
import org.mifos.mobile.utils.LoanUiState
import org.mifos.mobile.utils.Result
import org.mifos.mobile.utils.asResult
import org.mifos.mobile.utils.getTodayFormatted
import java.time.Instant
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LoanApplicationViewModel @Inject constructor(
    private val loanRepositoryImp: LoanRepository
) : ViewModel() {

    var loanUiState: StateFlow<LoanApplicationUiState> =
        MutableStateFlow(LoanApplicationUiState.Loading)

    private val _loanApplicationScreenData = MutableStateFlow(LoanApplicationScreenData())
    val loanApplicationScreenData: StateFlow<LoanApplicationScreenData> = _loanApplicationScreenData

    var loanState: LoanState = LoanState.CREATE
    var loanWithAssociations: LoanWithAssociations? = LoanWithAssociations()
    var loanTemplate: LoanTemplate = LoanTemplate()
    var productId: Int = 0
    var purposeId: Int = 0
    private var isLoanUpdatePurposesInitialization: Boolean = true

    init {
        _loanApplicationScreenData.update {
            it.copy(
                submittedDate = getTodayFormatted(),
                disbursementDate = getTodayFormatted()
            )
        }
    }

    fun loadLoanTemplate() {
        loadLoanApplicationTemplate(loanState)
    }

    private fun loadLoanApplicationTemplate(loanState: LoanState) {
        loanUiState = loanRepositoryImp.template()
            .asResult()
            .map { result ->
                when (result) {
                    is Result.Success -> {
                        loanTemplate = result.data ?: LoanTemplate()
                        if (loanState == LoanState.CREATE) showLoanTemplate(loanTemplate = loanTemplate)
                        else showUpdateLoanTemplate(loanTemplate = loanTemplate)
                        LoanApplicationUiState.Success
                    }

                    is Result.Loading -> LoanApplicationUiState.Loading
                    is Result.Error -> LoanApplicationUiState.Error(R.string.error_fetching_template)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = LoanApplicationUiState.Loading
            )
    }

    private fun loadLoanApplicationTemplateByProduct(productId: Int?, loanState: LoanState) {
        loanUiState = loanRepositoryImp.getLoanTemplateByProduct(productId)
            .asResult()
            .map { result ->
                when (result) {
                    is Result.Success -> {
                        result.data?.let {
                            if (loanState == LoanState.CREATE) showLoanTemplateByProduct(loanTemplate = it)
                            else showUpdateLoanTemplateByProduct(loanTemplate = it)
                        }
                        LoanApplicationUiState.Success
                    }
                    is Result.Loading -> LoanApplicationUiState.Loading
                    is Result.Error -> LoanApplicationUiState.Error(R.string.error_fetching_template)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = LoanApplicationUiState.Loading
            )
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
                selectedLoanProduct = loanWithAssociations?.loanProductName,
                accountNumber = loanWithAssociations?.accountNo,
                clientName = loanWithAssociations?.clientName,
                currencyLabel = loanWithAssociations?.currency?.displayLabel,
                principalAmount = String.format(
                    Locale.getDefault(),
                    "%.2f",
                    loanWithAssociations?.principal
                ),
                submittedDate = DateHelper.getDateAsString(
                    loanWithAssociations?.timeline?.submittedOnDate,
                    "dd-MM-yyyy"
                ),
                disbursementDate = DateHelper.getDateAsString(
                    loanWithAssociations?.timeline?.expectedDisbursementDate,
                    "dd-MM-yyyy"
                )
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
                principalAmount = String.format(Locale.getDefault(), "%.2f", loanTemplate.principal),
            )
        }
    }

    private fun showUpdateLoanTemplateByProduct(loanTemplate: LoanTemplate) {
        val loanPurposeList = refreshLoanPurposeList(loanTemplate = loanTemplate)
        if (isLoanUpdatePurposesInitialization && loanWithAssociations?.loanPurposeName != null) {
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
                    selectedLoanPurpose = loanWithAssociations?.loanPurposeName,
                    accountNumber = loanTemplate.clientAccountNo,
                    clientName = loanTemplate.clientName,
                    currencyLabel = loanTemplate.currency?.displayLabel,
                    principalAmount = String.format(
                        Locale.getDefault(),
                        "%.2f",
                        loanTemplate.principal
                    ),
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
        productId = loanTemplate?.productOptions?.get(position)?.id ?: 0
        loadLoanApplicationTemplateByProduct(productId, loanState)
        _loanApplicationScreenData.update {
            it.copy(selectedLoanProduct = loanApplicationScreenData.value.listLoanProducts[position])
        }
    }

    fun purposeSelected(position: Int) {
        loanTemplate.loanPurposeOptions.let {
            if (it.size > position) {
                purposeId = loanTemplate.loanPurposeOptions.get(position).id ?: 0
            }
        }
        _loanApplicationScreenData.update { it.copy(selectedLoanPurpose = loanApplicationScreenData.value.listLoanPurpose[position]) }
    }

    fun setDisburseDate(date: String) {
        _loanApplicationScreenData.update { it.copy(disbursementDate = date) }
    }

    fun setPrincipalAmount(amount: String) {
        _loanApplicationScreenData.update { it.copy(principalAmount = amount) }
    }
}

data class LoanApplicationScreenData(
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

sealed class LoanApplicationUiState() {
    data object Loading : LoanApplicationUiState()
    data object Success : LoanApplicationUiState()
    data class Error(val errorMessageId: Int) : LoanApplicationUiState()
}
