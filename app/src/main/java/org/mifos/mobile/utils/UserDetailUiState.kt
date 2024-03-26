package org.mifos.mobile.utils

import android.graphics.Bitmap
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.ui.user_profile.UserDetails


sealed class UserDetailUiState {
    object Loading : UserDetailUiState()
    data class ShowError(val message: Int) : UserDetailUiState()
    data class ShowUserDetails(val client: Client) : UserDetailUiState()
    data class ShowUserImage(val image: Bitmap?) : UserDetailUiState()
}

/**
data class UserDetailUiStates (
    var isLoading:Boolean? = false,
    var error: String? = null,
    var allUserDetails: List<Client> = listOf()
)
 */
