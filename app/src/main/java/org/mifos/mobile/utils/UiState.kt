package org.mifos.mobile.utils

sealed class UiState {
    data class Error(val exception: Throwable) : UiState()
    object Success : UiState()
    object Loading : UiState()
}