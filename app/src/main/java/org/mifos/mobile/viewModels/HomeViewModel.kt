package org.mifos.mobile.viewModels

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.models.accounts.loan.LoanAccount
import org.mifos.mobile.models.accounts.savings.SavingAccount
import org.mifos.mobile.repositories.HomeRepository
import org.mifos.mobile.utils.HomeUiState
import org.mifos.mobile.utils.ImageUtil
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepositoryImp: HomeRepository) :
    ViewModel() {

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState: StateFlow<HomeUiState> = _homeUiState

    fun loadClientAccountDetails() {
        viewModelScope.launch {
            _homeUiState.value = HomeUiState.Loading
            homeRepositoryImp.clientAccounts().catch {
                _homeUiState.value = HomeUiState.Error(R.string.error_fetching_accounts)
            }.collect { clientAccounts ->
                _homeUiState.value = HomeUiState.ClientAccountDetails(
                    getLoanAccountDetails(clientAccounts.loanAccounts),
                    getSavingAccountDetails(clientAccounts.savingsAccounts)
                )
            }
        }
    }

    val userDetails: Unit
        get() {
            viewModelScope.launch {
                homeRepositoryImp.currentClient().catch {
                    _homeUiState.value = HomeUiState.Error(R.string.error_fetching_client)
                }.collect { client ->
                    preferencesHelper.officeName = client.officeName
                    _homeUiState.value = HomeUiState.UserDetails(client)
                }
            }
        }

    val userImage: Unit
        get() {
            viewModelScope.launch {
                setUserProfile(preferencesHelper.userProfileImage)
                homeRepositoryImp.clientImage().catch {
                    _homeUiState.value = HomeUiState.UserImage(null)
                }.collect {
                    val encodedString = it.string()
                    val pureBase64Encoded =
                        encodedString.substring(encodedString.indexOf(',') + 1)
                    preferencesHelper.userProfileImage = pureBase64Encoded
                    setUserProfile(pureBase64Encoded)
                }
            }
        }

    fun setUserProfile(image: String?) {
        if (image == null) {
            return
        }
        val decodedBytes = Base64.decode(image, Base64.DEFAULT)
        val decodedBitmap = ImageUtil.instance?.compressImage(decodedBytes)
        _homeUiState.value = HomeUiState.UserImage(decodedBitmap)
    }

    val unreadNotificationsCount: Unit
        get() {
            viewModelScope.launch {
                homeRepositoryImp.unreadNotificationsCount().catch {
                    _homeUiState.value = HomeUiState.UnreadNotificationsCount(0)
                }.collect { integer ->
                    _homeUiState.value = HomeUiState.UnreadNotificationsCount(integer)
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
        for ((_, _, _, _, _, _, _, _, _, _, _, _, _, _, loanBalance) in loanAccountList) {
            totalAmount += loanBalance
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
        for ((_, _, _, _, _, accountBalance) in savingAccountList!!) {
            totalAmount += accountBalance
        }
        return totalAmount
    }
}