/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.home.viewmodel

import android.util.Base64
import android.util.Log
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repository.HomeRepository
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.designsystem.icons.MifosIcons
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.ui.utils.ImageUtil
import org.mifos.mobile.feature.home.R
import org.mifos.mobile.feature.home.utils.HomeState
import org.mifos.mobile.feature.home.utils.HomeUiState
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val homeRepositoryImp: HomeRepository,
    private val preferencesHelper: PreferencesHelper,
) : ViewModel() {

    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState: StateFlow<HomeUiState> get() = _homeUiState

    private val _notificationsCount = MutableStateFlow(0)
    val notificationsCount: StateFlow<Int> get() = _notificationsCount

    val clientId = preferencesHelper.clientId

    init {
        loadClientAccountDetails()
        getUserDetails()
        getUserImage()
    }

    private fun loadClientAccountDetails() {
        viewModelScope.launch {
            homeRepositoryImp.clientAccounts().catch {
                _homeUiState.value = HomeUiState.Error(R.string.no_internet_connection)
            }.collect { clientAccounts ->
                var currentState =
                    (_homeUiState.value as? HomeUiState.Success)?.homeState ?: HomeState()
                currentState = currentState.copy(
                    loanAmount = getLoanAccountDetails(clientAccounts.loanAccounts),
                    savingsAmount = getSavingAccountDetails(clientAccounts.savingsAccounts),
                )
                _homeUiState.value = HomeUiState.Success(currentState)
            }
        }
    }

    private fun getUserDetails() {
        viewModelScope.launch {
            homeRepositoryImp.currentClient().catch {
                _homeUiState.value = HomeUiState.Error(R.string.error_fetching_client)
            }.collect { client ->
                preferencesHelper.officeName = client.officeName
                var currentState =
                    (_homeUiState.value as? HomeUiState.Success)?.homeState ?: HomeState()
                currentState = currentState.copy(username = client.displayName)
                _homeUiState.value = HomeUiState.Success(currentState)
            }
        }
    }

    private fun getUserImage() {
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
        val decodedBitmap = ImageUtil.compressImage(decodedBytes)
        var currentState = (_homeUiState.value as? HomeUiState.Success)?.homeState ?: HomeState()
        currentState = currentState.copy(image = decodedBitmap)
        _homeUiState.value = HomeUiState.Success(currentState)
    }

    /**
     * Fetches the count of unread notifications
     *
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
     */

    /**
     * Returns total Loan balance
     *
     * @param loanAccountList [List] of [LoanAccount] associated with the client
     * @return Returns `totalAmount` which is calculated by adding all [LoanAccount]
     * balance.
     */
    private fun getLoanAccountDetails(loanAccountList: List<LoanAccount>): Double {
        var totalAmount = 0.0
        for (loanAccount in loanAccountList) {
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
        for (savingAccount in savingAccountList!!) {
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
            HomeCardItem.SurveyCard,
        )
    }
}

sealed class HomeCardItem(
    val titleId: Int,
    val imageVector: ImageVector,
) {
    data object AccountCard : HomeCardItem(
        titleId = R.string.accounts,
        imageVector = MifosIcons.AccountBalance,
    )

    data object TransferCard : HomeCardItem(
        titleId = R.string.transfer,
        imageVector = MifosIcons.CompareArrows,
    )

    data object ChargesCard : HomeCardItem(
        titleId = R.string.charges,
        imageVector = MifosIcons.AccountBalanceWallet,
    )

    data object LoanCard : HomeCardItem(
        titleId = R.string.apply_for_loan,
        imageVector = MifosIcons.RealEstateAgent,
    )

    data object BeneficiariesCard : HomeCardItem(
        titleId = R.string.beneficiaries,
        imageVector = MifosIcons.People,
    )

    data object SurveyCard : HomeCardItem(
        R.string.survey,
        MifosIcons.Assignment,
    )
}

enum class HomeNavigationItems(
    val nameResId: Int,
    val imageVector: ImageVector,
) {
    Home(
        nameResId = R.string.home,
        imageVector = MifosIcons.AccountBalance,
    ),

    Accounts(
        nameResId = R.string.accounts,
        imageVector = MifosIcons.AccountBalanceWallet,
    ),

    RecentTransactions(
        nameResId = R.string.recent_transactions,
        imageVector = MifosIcons.Label,
    ),

    Charges(
        nameResId = R.string.charges,
        imageVector = MifosIcons.Paid,
    ),

    ThirdPartyTransfer(
        nameResId = R.string.third_party_transfer,
        imageVector = MifosIcons.CompareArrows,
    ),

    ManageBeneficiaries(
        nameResId = R.string.manage_beneficiaries,
        imageVector = MifosIcons.People,
    ),

    Settings(
        nameResId = R.string.settings,
        imageVector = MifosIcons.Settings,
    ),

    AboutUs(
        nameResId = R.string.about_us,
        imageVector = MifosIcons.People,
    ),

    Help(
        nameResId = R.string.help,
        imageVector = MifosIcons.Help,
    ),

    Share(
        nameResId = R.string.share,
        imageVector = MifosIcons.Share,
    ),

    AppInfo(
        nameResId = R.string.app_info,
        imageVector = MifosIcons.Info,
    ),

    Logout(
        nameResId = R.string.logout,
        imageVector = MifosIcons.Logout,
    ),
}
