package org.mifos.mobile.ui.home

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.core.data.repositories.HomeRepository
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.utils.ImageUtil
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepositoryImp: HomeRepository) :
    ViewModel() {

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    private val _homeUiState = mutableStateOf<HomeUiState>(HomeUiState.Loading)
    val homeUiState: State<HomeUiState> get() = _homeUiState

    private val _notificationsCount = MutableStateFlow<Int>(0)
    val notificationsCount: StateFlow<Int> get() = _notificationsCount

    fun loadClientAccountDetails() {
        viewModelScope.launch {
            homeRepositoryImp.clientAccounts().catch {
                _homeUiState.value = HomeUiState.Error(R.string.no_internet_connection)
            }.collect { clientAccounts ->
                var currentState = (_homeUiState.value as? HomeUiState.Success)?.homeState ?: HomeState()
                currentState = currentState.copy(
                    loanAmount = getLoanAccountDetails(clientAccounts.loanAccounts),
                    savingsAmount = getSavingAccountDetails(clientAccounts.savingsAccounts)
                )
                _homeUiState.value = HomeUiState.Success(currentState)
            }
        }
    }

    fun getUserDetails() {
        viewModelScope.launch {
            homeRepositoryImp.currentClient().catch {
                _homeUiState.value = HomeUiState.Error(R.string.error_fetching_client)
            }.collect { client ->
                preferencesHelper.officeName = client.officeName
                var currentState = (_homeUiState.value as? HomeUiState.Success)?.homeState ?: HomeState()
                currentState = currentState.copy(username = client.displayName)
                _homeUiState.value = HomeUiState.Success(currentState)
            }
        }
    }

    fun getUserImage() {
        viewModelScope.launch {
            setUserProfile(preferencesHelper.userProfileImage)
            homeRepositoryImp.clientImage().catch {
                Log.e("Client Image Exception", it.message ?: "")
            }.collect {
                val encodedString = it.string()
                val pureBase64Encoded =
                    encodedString.substring(encodedString.indexOf(',') + 1)
                preferencesHelper.userProfileImage = pureBase64Encoded
                setUserProfile(pureBase64Encoded)
            }
        }
    }


    private fun setUserProfile(image: String?) {
        if (image == null) {
            return
        }
        val decodedBytes = Base64.decode(image, Base64.DEFAULT)
        val decodedBitmap = ImageUtil.instance?.compressImage(decodedBytes)
        var currentState = (_homeUiState.value as? HomeUiState.Success)?.homeState ?: HomeState()
        currentState = currentState.copy(image = decodedBitmap)
        _homeUiState.value = HomeUiState.Success(currentState)
    }

    val unreadNotificationsCount: Unit
        get() {
            viewModelScope.launch {
                homeRepositoryImp.unreadNotificationsCount().catch {
                    _notificationsCount.value = 0
                }.collect { integer ->
                    _notificationsCount.value = integer
                }
            }
        }


    /**
     * Returns total Loan balance
     *
     * @param loanAccountList [List] of [LoanAccount] associated with the client
     * @return Returns `totalAmount` which is calculated by adding all [LoanAccount]
     * balance.
     */
    private fun getLoanAccountDetails(loanAccountList: List<LoanAccount>): Double {
        var totalAmount = 0.0
        for (loanAccount  in loanAccountList) {
            totalAmount += loanAccount.loanBalance
        }
        return totalAmount
    }

    /**
     * Returns total Savings balance
     *
     * @param savingAccountList [List] of [SavingAccount] associated with the client
     * @return Returns `totalAmount` which is calculated by adding all [SavingAccount]
     * balance.
     */
    private fun getSavingAccountDetails(savingAccountList: List<SavingAccount>?): Double {
        var totalAmount = 0.0
        for (savingAccount  in savingAccountList!!) {
            totalAmount += savingAccount.accountBalance
        }
        return totalAmount
    }

    fun getHomeCardItems(): List<HomeCardItem> {
        return listOf(
            HomeCardItem.AccountCard,
            HomeCardItem.TransferCard,
            HomeCardItem.ChargesCard,
            HomeCardItem.LoanCard,
            HomeCardItem.BeneficiariesCard,
            HomeCardItem.SurveyCard
        )
    }
}

sealed class HomeCardItem(
    val titleId: Int,
    val drawableResId: Int
) {
    data object AccountCard :
        HomeCardItem(R.string.accounts, R.drawable.ic_account_balance_black_24dp)

    data object TransferCard :
        HomeCardItem(R.string.transfer, R.drawable.ic_compare_arrows_black_24dp)

    data object ChargesCard :
        HomeCardItem(R.string.charges, R.drawable.ic_account_balance_wallet_black_24dp)

    data object LoanCard : HomeCardItem(R.string.apply_for_loan, R.drawable.ic_loan)
    data object BeneficiariesCard :
        HomeCardItem(R.string.beneficiaries, R.drawable.ic_beneficiaries_48px)

    data object SurveyCard : HomeCardItem(R.string.survey, R.drawable.ic_surveys_48px)
}

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Error(val errorMessage: Int) : HomeUiState()
    data class Success(val homeState: HomeState) : HomeUiState()
}

data class HomeState(
    val username: String? = "",
    val image: Bitmap? = null,
    val loanAmount: Double = 0.0,
    val savingsAmount: Double = 0.0
)