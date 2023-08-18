package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.models.AccountOptionAndBeneficiary
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.beneficiary.BeneficiaryDetail
import org.mifos.mobile.models.payload.AccountDetail
import org.mifos.mobile.models.templates.account.AccountOption
import org.mifos.mobile.repositories.ThirdPartyTransferRepository
import org.mifos.mobile.utils.ThirdPartyTransferUiState
import javax.inject.Inject

@HiltViewModel
class ThirdPartyTransferViewModel @Inject constructor(private val thirdPartyTransferRepositoryImp: ThirdPartyTransferRepository) :
    ViewModel() {

    private lateinit var accountOptionAndBeneficiary: AccountOptionAndBeneficiary

    private val _thirdPartyTransferUiState = MutableLiveData<ThirdPartyTransferUiState>()
    val thirdPartyTransferUiState: LiveData<ThirdPartyTransferUiState> get() = _thirdPartyTransferUiState

    fun loadTransferTemplate() {
        viewModelScope.launch {
            _thirdPartyTransferUiState.value = ThirdPartyTransferUiState.Loading
            try {
                val thirdPartyTransferTemplate =
                    flowOf(thirdPartyTransferRepositoryImp.thirdPartyTransferTemplate())
                val beneficiaryList = flowOf(thirdPartyTransferRepositoryImp.beneficiaryList())
                thirdPartyTransferTemplate.zip(beneficiaryList) { templateResult, beneficiaryListResult ->
                    templateResult?.body()?.let {
                        beneficiaryListResult?.body()?.let { it1 ->
                            accountOptionAndBeneficiary = AccountOptionAndBeneficiary(
                                it,
                                it1,
                            )
                        }
                    }
                }
                _thirdPartyTransferUiState.value =
                    ThirdPartyTransferUiState.ShowThirdPartyTransferTemplate(
                        accountOptionAndBeneficiary.accountOptionsTemplate
                    )
                _thirdPartyTransferUiState.value =
                    ThirdPartyTransferUiState.ShowBeneficiaryList(
                        accountOptionAndBeneficiary.beneficiaryList
                    )

            } catch (e: Throwable) {
                _thirdPartyTransferUiState.value =
                    ThirdPartyTransferUiState.Error(R.string.error_fetching_third_party_transfer_template)
            }

        }

    }

    /**
     * Retrieving [List] of `accountNumbers` from [List] of [AccountOption]
     *
     * @param accountOptions [List] of [AccountOption]
     * @return Returns [List] containing `accountNumbers`
     */
    fun getAccountNumbersFromAccountOptions(
        accountOptions: List<AccountOption>?,
        accountTypeLoanString: String?
    ): List<AccountDetail> {
        return accountOptions.orEmpty()
            .filter { (_, _, accountType) -> accountType?.code != accountTypeLoanString }
            .map { (_, accountNo, accountType) ->
                AccountDetail(
                    accountNo!!,
                    accountType?.value!!,
                )
            }
    }


    /**
     * Retrieving [List] of `accountNumbers` from [List] of [Beneficiary]
     *
     * @param beneficiaries [List] of [Beneficiary]
     * @return Returns [List] containing `accountNumbers`
     */

    fun getAccountNumbersFromBeneficiaries(beneficiaries: List<Beneficiary?>?): List<BeneficiaryDetail> {
        return beneficiaries.orEmpty()
            .mapNotNull { beneficiary ->
                beneficiary?.let { (_, name, _, _, _, accountNumber) ->
                    BeneficiaryDetail(accountNumber, name)
                }
            }
    }


    /**
     * Searches for a [AccountOption] with provided `accountNo` from [List] of
     * [AccountOption] and returns it.
     *
     * @param accountOptions [List] of [AccountOption]
     * @param accountNo      Account Number which needs to searched in [List] of
     * [AccountOption]
     * @return Returns [AccountOption] which has Account Number same as the provided
     * `accountNo` in function parameter.
     */
    fun searchAccount(accountOptions: List<AccountOption>?, accountNo: String?): AccountOption {
        return accountOptions.orEmpty()
            .firstOrNull { (_, accountNo1) -> accountNo1 == accountNo }
            ?: AccountOption()
    }

}