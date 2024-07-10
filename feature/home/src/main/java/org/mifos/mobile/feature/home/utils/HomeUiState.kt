package org.mifos.mobile.feature.home.utils

import android.graphics.Bitmap

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