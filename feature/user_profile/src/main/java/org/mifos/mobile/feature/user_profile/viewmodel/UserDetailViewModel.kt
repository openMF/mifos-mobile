package org.mifos.mobile.feature.user_profile.viewmodel

import android.graphics.Bitmap
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repositories.HomeRepository
import org.mifos.mobile.core.data.repositories.UserDetailRepository
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.model.entity.notification.NotificationRegisterPayload
import org.mifos.mobile.feature.third.party.user_profile.R
import org.mifos.mobile.feature.user_profile.utils.ImageUtil
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userDetailRepositoryImp: UserDetailRepository,
    private val homeRepositoryImp: HomeRepository
) : ViewModel() {

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    private val _userDetailUiState = MutableStateFlow<UserDetailUiState>(UserDetailUiState.Loading)
    val userDetailUiState: StateFlow<UserDetailUiState> = _userDetailUiState

    fun loadUserData() {
        var userData : Client = Client()

        viewModelScope.launch {
            homeRepositoryImp.currentClient().catch {
                _userDetailUiState.value = UserDetailUiState.ShowError(R.string.error_fetching_client)
            }.collect { client ->
                preferencesHelper.officeName = client.officeName
                _userDetailUiState.value = UserDetailUiState.ShowUserDetails( client, null)
                userData = client
            }
        }

        viewModelScope.launch {
            homeRepositoryImp.clientImage().catch {

            }.collect {
                val encodedString = it.string()
                val pureBase64Encoded =
                    encodedString.substring(encodedString.indexOf(',') + 1)
                preferencesHelper.userProfileImage = pureBase64Encoded
                _userDetailUiState.value = UserDetailUiState.ShowUserDetails( userData, getUserProfile(pureBase64Encoded))
            }
        }
    }

    fun getUserProfile(image: String?): Bitmap? {
        if (image == null) {
            return null
        }
        val decodedBytes = Base64.decode(image, Base64.DEFAULT)
        val decodedBitmap = ImageUtil.instance?.compressImage(decodedBytes)
        return decodedBitmap
    }

    fun registerNotification(token: String) {
        viewModelScope.launch {
            val payload = preferencesHelper.clientId?.let { NotificationRegisterPayload(it, token) }
            userDetailRepositoryImp.registerNotification(payload).catch { e ->
                if (e is retrofit2.HttpException && e.code() == 500) {
                    payload?.let { getUserNotificationId(it, token) }
                }
            }.collect {
                preferencesHelper.setSentTokenToServer(true)
                preferencesHelper.saveGcmToken(token)
            }
        }
    }

    fun getUserNotificationId(payload: NotificationRegisterPayload, token: String) {
        viewModelScope.launch {
            preferencesHelper.clientId?.let {
                userDetailRepositoryImp.getUserNotificationId(it).catch { e ->
                    Log.e(UserDetailViewModel::class.java.simpleName, e.toString())
                }.collect { userDetail ->
                    updateRegistrationNotification(userDetail.id.toLong(), payload, token)
                }
            }
        }
    }

    fun updateRegistrationNotification(
        id: Long,
        payload: NotificationRegisterPayload,
        token: String,
    ) {
        viewModelScope.launch {
            userDetailRepositoryImp.updateRegisterNotification(id, payload).catch { e ->
                Log.e(UserDetailViewModel::class.java.simpleName, e.toString())
            }.collect {
                preferencesHelper.setSentTokenToServer(true)
                preferencesHelper.saveGcmToken(token)
            }
        }
    }
}

sealed class UserDetailUiState {
    object Loading : UserDetailUiState()
    data class ShowError(val message: Int) : UserDetailUiState()
    data class ShowUserDetails(val client: Client, val image: Bitmap?) : UserDetailUiState()
}