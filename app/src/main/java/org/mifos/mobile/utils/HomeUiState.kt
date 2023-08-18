package org.mifos.mobile.utils

import android.graphics.Bitmap
import org.mifos.mobile.models.client.Client

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Error(val errorMessage: Int) : HomeUiState()
    data class ClientAccountDetails(val loanAccounts: Double, val savingsAccounts: Double) : HomeUiState()
    data class UserDetails(val client: Client) : HomeUiState()
    data class UserImage(val image: Bitmap?) : HomeUiState()
    data class UnreadNotificationsCount(val count: Int) : HomeUiState()
}