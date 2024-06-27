package org.mifos.mobile.utils

import android.graphics.Bitmap
import org.mifos.mobile.core.model.entity.client.Client


sealed class UserDetailUiState {
    object Loading : UserDetailUiState()
    data class ShowError(val message: Int) : UserDetailUiState()
    data class ShowUserDetails(val client: Client) : UserDetailUiState()
    data class ShowUserImage(val image: Bitmap?) : UserDetailUiState()
}
