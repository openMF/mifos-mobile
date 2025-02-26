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

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.home.generated.resources.Res
import mifos_mobile.feature.home.generated.resources.about_us
import mifos_mobile.feature.home.generated.resources.accounts
import mifos_mobile.feature.home.generated.resources.app_info
import mifos_mobile.feature.home.generated.resources.apply_for_loan
import mifos_mobile.feature.home.generated.resources.beneficiaries
import mifos_mobile.feature.home.generated.resources.charges
import mifos_mobile.feature.home.generated.resources.error_fetching_client
import mifos_mobile.feature.home.generated.resources.help
import mifos_mobile.feature.home.generated.resources.home
import mifos_mobile.feature.home.generated.resources.logout
import mifos_mobile.feature.home.generated.resources.manage_beneficiaries
import mifos_mobile.feature.home.generated.resources.no_internet_connection
import mifos_mobile.feature.home.generated.resources.recent_transactions
import mifos_mobile.feature.home.generated.resources.settings
import mifos_mobile.feature.home.generated.resources.share
import mifos_mobile.feature.home.generated.resources.survey
import mifos_mobile.feature.home.generated.resources.third_party_transfer
import mifos_mobile.feature.home.generated.resources.transfer
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.FileUtils.Companion.logger
import org.mifos.mobile.core.data.repository.HomeRepository
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.model.IgnoredOnParcel
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.accounts.loan.LoanAccount
import org.mifos.mobile.core.model.entity.accounts.savings.SavingAccount
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ImageUtil
import org.mifos.mobile.feature.home.navigation.HomeDestinations
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

internal class HomeViewModel(
    private val homeRepositoryImpl: HomeRepository,
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
) : BaseViewModel<HomeState, HomeEvent, HomeAction>(
    initialState = HomeState(
        clientId = requireNotNull(userPreferencesRepositoryImpl.clientId.value),
        dialogState = null,
    ),
) {

    init {
        unreadNotificationsCount()
        loadClientAccountDetails()
        getUserDetails()
        getUserImage()
        getHomeCardItems()
    }

    override fun handleAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnNavigate -> {
                sendEvent(HomeEvent.Navigate(action.destination))
            }

            HomeAction.OnCallHelpLine -> sendEvent(HomeEvent.CallHelpLine)
            HomeAction.OnMailHelpLine -> sendEvent(HomeEvent.MailHelpLine)
            is HomeAction.OnTotalLoan -> {
                sendEvent(HomeEvent.TotalLoan(action.destination))
            }
            is HomeAction.OnTotalSavings -> {
                sendEvent(HomeEvent.TotalSavings(action.destination))
            }
            is HomeAction.OnUserProfile -> {
                sendEvent(HomeEvent.UserProfile(action.destination))
            }
            is HomeAction.OnNotification -> {
                sendEvent(HomeEvent.Notification(action.destination))
            }
            HomeAction.OnLogoutClicked -> {
                updateState {
                    it.copy(
                        dialogState = HomeState.DialogState.LogoutConfirmationDialog(
                            onConfirm = { performLogout() },
                        ),
                    )
                }
            }

            HomeAction.OnLogoutConfirmed -> {
                updateState { it.copy(dialogState = null) }
                sendEvent(HomeEvent.Navigate(HomeDestinations.LOGOUT))
            }
            HomeAction.OnDismissDialog -> updateState { it.copy(dialogState = null) }
        }
    }

    private fun performLogout() {
        updateState { it.copy(dialogState = null) }
        sendEvent(HomeEvent.Navigate(HomeDestinations.LOGOUT))
    }

    private fun updateState(update: (HomeState) -> HomeState) {
        mutableStateFlow.update(update)
    }

    private fun loadClientAccountDetails() {
        updateState { it.copy(dialogState = HomeState.DialogState.Loading) }
        viewModelScope.launch {
            val internetConnection = getString(Res.string.no_internet_connection)
            homeRepositoryImpl.clientAccounts(clientId = state.clientId ?: 0).catch {
                updateState { it.copy(dialogState = HomeState.DialogState.Error(internetConnection)) }
            }.collect { clientAccounts ->
                when (clientAccounts) {
                    is DataState.Error -> updateState {
                        it.copy(
                            dialogState = HomeState
                                .DialogState.Error(clientAccounts.exception.message.toString()),
                        )
                    }

                    DataState.Loading -> updateState { it.copy(dialogState = HomeState.DialogState.Loading) }
                    is DataState.Success -> updateState {
                        getLoanAccountDetails(clientAccounts.data.loanAccounts)
                        getSavingAccountDetails(clientAccounts.data.savingsAccounts)
                        updateState { it.copy(clientAccounts = clientAccounts.data) }
                        it.copy(clientAccounts = clientAccounts.data)
                    }
                }
            }
            updateState { it.copy(dialogState = null) }
        }
    }

    private fun getUserDetails() {
        updateState { it.copy(dialogState = HomeState.DialogState.Loading) }
        viewModelScope.launch {
            val errorMessage = getString(Res.string.error_fetching_client)
            homeRepositoryImpl.currentClient(clientId = state.clientId ?: 0).catch {
                updateState { it.copy(dialogState = HomeState.DialogState.Error(errorMessage)) }
            }.collect { client ->
                when (client) {
                    is DataState.Error -> updateState {
                        it.copy(
                            dialogState = HomeState
                                .DialogState.Error(client.exception.message.toString()),
                        )
                    }

                    DataState.Loading -> updateState { it.copy(dialogState = HomeState.DialogState.Loading) }
                    is DataState.Success -> updateState {
                        it.copy(username = client.data.displayName ?: "")
                    }
                }
                updateState { it.copy(dialogState = null) }
            }
        }
    }

    private fun getUserImage() {
        viewModelScope.launch {
            setUserProfile(userPreferencesRepositoryImpl.profileImage)
            homeRepositoryImpl.clientImage(state.clientId ?: 0).catch {
            }.collect { response ->
                when (response) {
                    is DataState.Error -> updateState {
                        it.copy(
                            dialogState = HomeState
                                .DialogState.Error(response.exception.message.toString()),
                        )
                    }

                    DataState.Loading -> updateState { it.copy(dialogState = HomeState.DialogState.Loading) }
                    is DataState.Success -> {
                        userPreferencesRepositoryImpl.updateProfileImage(response.data)
                        setUserProfile(response.data)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun setUserProfile(image: String?) {
        if (image.isNullOrBlank()) return

        val base64String = image.substringAfter(",", image)
        if (!base64String.matches(Regex("^[A-Za-z0-9+/=]+$"))) return

        try {
            val decodedBytes = Base64.decode(base64String)
            val decodedBitmap = ImageUtil.compressImage(decodedBytes)
            updateState { it.copy(image = decodedBitmap) }
        } catch (e: Exception) {
            logger.d { e.message.toString() }
        }
    }

    private fun unreadNotificationsCount() {
        viewModelScope.launch {
            homeRepositoryImpl.unreadNotificationsCount().catch {
                updateState {
                    it.copy(notificationCount = 0)
                }
            }.collect { count ->
                when (count) {
                    is DataState.Error -> updateState {
                        it.copy(notificationCount = 0)
                    }

                    DataState.Loading -> Unit
                    is DataState.Success -> updateState {
                        it.copy(notificationCount = count.data)
                    }
                }
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
    private fun getLoanAccountDetails(loanAccountList: List<LoanAccount>) {
        var totalAmount = 0.0
        for (loanAccount in loanAccountList) {
            totalAmount += loanAccount.loanBalance
        }

        updateState { it.copy(loanAmount = totalAmount) }
    }

    private fun getSavingAccountDetails(savingAccountList: List<SavingAccount>?) {
        var totalAmount = 0.0
        for (savingAccount in savingAccountList!!) {
            totalAmount += savingAccount.accountBalance
        }
        updateState { it.copy(savingsAmount = totalAmount) }
    }

    private fun getHomeCardItems() {
        updateState {
            it.copy(
                homeCardItems = listOf(
                    HomeCardItem.AccountCard,
                    HomeCardItem.TransferCard,
                    HomeCardItem.ChargesCard,
                    HomeCardItem.LoanCard,
                    HomeCardItem.BeneficiariesCard,
                    HomeCardItem.SurveyCard,
                ),
            )
        }
    }
}

@Parcelize
data class HomeState(
    val clientId: Long? = 0,
    val username: String = "",
    val image: ByteArray? = null,
    val clientAccounts: ClientAccounts? = null,
    val notificationCount: Int = 0,
    val loanAmount: Double = 0.0,
    val savingsAmount: Double = 0.0,
    val logoutConfirmationDialog: Boolean = false,
    val dialogState: DialogState?,
    @IgnoredOnParcel
    val homeCardItems: List<HomeCardItem>? = null,
) : Parcelable {

    sealed interface DialogState : Parcelable {
        @Parcelize
        data class Error(val message: String) : DialogState

        @Parcelize
        data object Loading : DialogState

        @Parcelize
        data class LogoutConfirmationDialog(
            val onConfirm: () -> Unit,
        ) : DialogState
    }
}

sealed interface HomeEvent {
    data class Navigate(val destination: HomeDestinations) : HomeEvent
    data object CallHelpLine : HomeEvent
    data object MailHelpLine : HomeEvent
    data class UserProfile(val destination: HomeDestinations) : HomeEvent
    data class TotalSavings(val destination: HomeDestinations) : HomeEvent
    data class TotalLoan(val destination: HomeDestinations) : HomeEvent
    data class Notification(val destination: HomeDestinations) : HomeEvent
}

sealed interface HomeAction {
    data class OnNavigate(val destination: HomeDestinations) : HomeAction
    data object OnCallHelpLine : HomeAction
    data object OnMailHelpLine : HomeAction
    data class OnUserProfile(val destination: HomeDestinations) : HomeAction
    data class OnTotalSavings(val destination: HomeDestinations) : HomeAction
    data class OnTotalLoan(val destination: HomeDestinations) : HomeAction
    data class OnNotification(val destination: HomeDestinations) : HomeAction

    data object OnLogoutClicked : HomeAction
    data object OnLogoutConfirmed : HomeAction
    data object OnDismissDialog : HomeAction
}

sealed class HomeCardItem(
    val titleId: StringResource,
    val imageVector: ImageVector,
) {
    data object AccountCard : HomeCardItem(
        titleId = Res.string.accounts,
        imageVector = MifosIcons.AccountBalance,
    )

    data object TransferCard : HomeCardItem(
        titleId = Res.string.transfer,
        imageVector = MifosIcons.CompareArrows,
    )

    data object ChargesCard : HomeCardItem(
        titleId = Res.string.charges,
        imageVector = MifosIcons.AccountBalanceWallet,
    )

    data object LoanCard : HomeCardItem(
        titleId = Res.string.apply_for_loan,
        imageVector = MifosIcons.RealEstateAgent,
    )

    data object BeneficiariesCard : HomeCardItem(
        titleId = Res.string.beneficiaries,
        imageVector = MifosIcons.People,
    )

    data object SurveyCard : HomeCardItem(
        Res.string.survey,
        MifosIcons.Assignment,
    )
}

enum class HomeNavigationItems(
    val nameResId: StringResource,
    val imageVector: ImageVector,
) {
    Home(
        nameResId = Res.string.home,
        imageVector = MifosIcons.AccountBalance,
    ),

    Accounts(
        nameResId = Res.string.accounts,
        imageVector = MifosIcons.AccountBalanceWallet,
    ),

    RecentTransactions(
        nameResId = Res.string.recent_transactions,
        imageVector = MifosIcons.Label,
    ),

    Charges(
        nameResId = Res.string.charges,
        imageVector = MifosIcons.Paid,
    ),

    ThirdPartyTransfer(
        nameResId = Res.string.third_party_transfer,
        imageVector = MifosIcons.CompareArrows,
    ),

    ManageBeneficiaries(
        nameResId = Res.string.manage_beneficiaries,
        imageVector = MifosIcons.People,
    ),

    Settings(
        nameResId = Res.string.settings,
        imageVector = MifosIcons.Settings,
    ),

    AboutUs(
        nameResId = Res.string.about_us,
        imageVector = MifosIcons.People,
    ),

    Help(
        nameResId = Res.string.help,
        imageVector = MifosIcons.Help,
    ),

    Share(
        nameResId = Res.string.share,
        imageVector = MifosIcons.Share,
    ),

    AppInfo(
        nameResId = Res.string.app_info,
        imageVector = MifosIcons.Info,
    ),

    Logout(
        nameResId = Res.string.logout,
        imageVector = MifosIcons.Logout,
    ),
}
