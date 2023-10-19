package org.mifos.mobile.utils

import okhttp3.ResponseBody

sealed class ReviewLoanApplicationUiState {
    object Initial : ReviewLoanApplicationUiState()
    object Loading : ReviewLoanApplicationUiState()
    data class ReviewSuccess(val responseBody: ResponseBody) : ReviewLoanApplicationUiState()
    data class Error(val throwable: Throwable) : ReviewLoanApplicationUiState()
}
