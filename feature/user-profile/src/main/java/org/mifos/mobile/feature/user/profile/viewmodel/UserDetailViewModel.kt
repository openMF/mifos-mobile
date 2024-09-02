/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.user.profile.viewmodel

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.core.data.repository.HomeRepository
import org.mifos.mobile.core.data.repository.UserDetailRepository
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.model.entity.notification.NotificationRegisterPayload
import org.mifos.mobile.core.ui.utils.ImageUtil
import org.mifos.mobile.feature.user.profile.R
import org.mifos.mobile.feature.user.profile.viewmodel.UserDetailUiState.Loading
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userDetailRepositoryImp: UserDetailRepository,
    private val homeRepositoryImp: HomeRepository,
) : ViewModel() {

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    private val _userDetailUiState = MutableStateFlow<UserDetailUiState>(Loading)
    val userDetailUiState = _userDetailUiState.asStateFlow()

    init {
        loadUserData()
    }

    fun loadUserData() {
        var userData = Client()

        viewModelScope.launch {
            homeRepositoryImp.currentClient().catch {
                _userDetailUiState.value =
                    UserDetailUiState.ShowError(R.string.error_fetching_client)
            }.collect { client ->
                preferencesHelper.officeName = client.officeName
                _userDetailUiState.value = UserDetailUiState.ShowUserDetails(client, null)
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
                _userDetailUiState.value =
                    UserDetailUiState.ShowUserDetails(userData, getUserProfile(pureBase64Encoded))
            }
        }
    }

    private fun getUserProfile(image: String?): Bitmap? {
        if (image == null) {
            return null
        }
        val decodedBytes = Base64.decode(image, Base64.DEFAULT)
        val decodedBitmap = ImageUtil.compressImage(decodedBytes)
        return decodedBitmap
    }

    fun registerNotification(token: String) {
        viewModelScope.launch {
            val payload = preferencesHelper.clientId?.let { NotificationRegisterPayload(it, token) }
            userDetailRepositoryImp.registerNotification(payload).catch {
                payload?.let { getUserNotificationId(it, token) }
            }.collect {
                preferencesHelper.setSentTokenToServer(true)
                preferencesHelper.saveGcmToken(token)
            }
        }
    }

    private fun getUserNotificationId(payload: NotificationRegisterPayload, token: String) {
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

    private fun updateRegistrationNotification(
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
    data object Loading : UserDetailUiState()
    data class ShowError(val message: Int) : UserDetailUiState()
    data class ShowUserDetails(val client: Client, val image: Bitmap?) : UserDetailUiState()
}
