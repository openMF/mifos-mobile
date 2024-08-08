package org.mifos.mobile.feature.home.viewmodel

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
import org.mifos.mobile.core.data.repositories.HomeRepository
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.feature.home.R
import org.mifos.mobile.feature.home.utils.HomeState
import org.mifos.mobile.feature.home.utils.HomeUiState
import org.mifos.mobile.feature.home.utils.ImageUtil
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val homeRepositoryImp: HomeRepository,
    val preferencesHelper: PreferencesHelper
) : ViewModel() {

    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState: StateFlow<HomeUiState> get() = _homeUiState

    private val _notificationsCount = MutableStateFlow<Int>(0)
    val notificationsCount: StateFlow<Int> get() = _notificationsCount

    val clientId = preferencesHelper.clientId

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
    data object AccountCard : HomeCardItem(R.string.accounts, R.drawable.ic_account_balance_black_24dp)
    data object TransferCard : HomeCardItem(R.string.transfer, R.drawable.ic_compare_arrows_black_24dp)
    data object ChargesCard : HomeCardItem(R.string.charges, R.drawable.ic_account_balance_wallet_black_24dp)
    data object LoanCard : HomeCardItem(R.string.apply_for_loan, R.drawable.ic_loan)
    data object BeneficiariesCard : HomeCardItem(R.string.beneficiaries, R.drawable.ic_beneficiaries_48px)
    data object SurveyCard : HomeCardItem(R.string.survey, R.drawable.ic_surveys_48px)
}

enum class HomeNavigationItems(
    val nameResId: Int,
    val iconResId: Int
) {
    Home(
        nameResId = R.string.home,
        iconResId = R.drawable.ic_account_balance_black_24dp
    ),

    Accounts(
        nameResId = R.string.accounts,
        iconResId = R.drawable.ic_account_balance_wallet_black_24dp
    ),

    RecentTransactions(
        nameResId = R.string.recent_transactions,
        iconResId = R.drawable.ic_label_black_24dp
    ),

    Charges(
        nameResId = R.string.charges,
        iconResId = R.drawable.ic_charges
    ),

    ThirdPartyTransfer(
        nameResId = R.string.third_party_transfer,
        iconResId = R.drawable.ic_compare_arrows_black_24dp
    ),

    ManageBeneficiaries(
        nameResId = R.string.manage_beneficiaries,
        iconResId = R.drawable.ic_beneficiaries_48px
    ),

    Settings(
        nameResId = R.string.settings,
        iconResId = R.drawable.ic_settings
    ),

    AboutUs(
        nameResId = R.string.about_us,
        iconResId = R.drawable.ic_about_us_black_24dp
    ),

    Help(
        nameResId = R.string.help,
        iconResId = R.drawable.ic_help_black_24dp
    ),

    Share(
        nameResId = R.string.share,
        iconResId = R.drawable.ic_share_black_24dp
    ),

    AppInfo(
        nameResId = R.string.app_info,
        iconResId = R.drawable.ic_info_black_24dp
    ),

    Logout(
        nameResId = R.string.logout,
        iconResId = R.drawable.ic_logout
    )
}

