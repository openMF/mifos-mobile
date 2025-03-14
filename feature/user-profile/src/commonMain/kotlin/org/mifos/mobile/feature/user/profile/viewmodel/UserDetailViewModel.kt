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

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.user_profile.generated.resources.Res
import mifos_mobile.feature.user_profile.generated.resources.error_fetching_client
import mifos_mobile.feature.user_profile.generated.resources.internet_not_connected
import org.jetbrains.compose.resources.getString
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.FileUtils.Companion.logger
import org.mifos.mobile.core.data.repository.HomeRepository
import org.mifos.mobile.core.data.repository.UserDetailRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.datastore.model.UserData
import org.mifos.mobile.core.model.IgnoredOnParcel
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.model.entity.notification.NotificationRegisterPayload
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.ImageUtil
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

internal class UserDetailViewModel(
    private val userDetailRepositoryImp: UserDetailRepository,
    private val homeRepositoryImp: HomeRepository,
    private val userPreferencesRepositoryImpl: UserPreferencesRepository,
    networkMonitor: NetworkMonitor,
) : BaseViewModel<UserDetailState, UserDetailEvent, UserDetailAction>(
    initialState = UserDetailState(
        dialogState = null,
        clientId = requireNotNull(
            userPreferencesRepositoryImpl.clientId.value,
        ),
        token = requireNotNull(
            userPreferencesRepositoryImpl.token.value,
        ),

    ),
) {

    init {
        viewModelScope.launch {
            userPreferencesRepositoryImpl.userInfo
                .collect { userData ->
                    updateState {
                        it.copy(
                            userData = userData,
                        )
                    }
                }
        }
        viewModelScope.launch {
            val message = getString(Res.string.internet_not_connected)
            networkMonitor.isOnline.collect { isOnline ->
                updateState {
                    it.copy(
                        client = null,
                        isOnline = isOnline,
                    )
                }
                if (!isOnline) {
                    setDialogState(
                        UserDetailState.DialogState.Error(
                            message,
                        ),
                    )
                }
            }
        }
        loadUserData()
    }

    private fun updateState(update: (UserDetailState) -> UserDetailState) {
        mutableStateFlow.update(update)
    }

    private fun setDialogState(dialogState: UserDetailState.DialogState?) {
        updateState { it.copy(dialogState = dialogState) }
    }

    override fun handleAction(action: UserDetailAction) {
        when (action) {
            UserDetailAction.OnNavigate -> sendEvent(UserDetailEvent.Navigate)
            is UserDetailAction.RegisterNotification -> state.token?.let { registerNotification(it) }
            UserDetailAction.OnRetry -> loadUserData()
            UserDetailAction.OnChangePassword -> sendEvent(UserDetailEvent.ChangePassword)
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val errorMsg = getString(Res.string.error_fetching_client)
            homeRepositoryImp.currentClient(state.clientId ?: -1L).catch {
                updateState {
                    it.copy(
                        dialogState = UserDetailState.DialogState.Error(
                            errorMsg,
                        ),
                    )
                }
            }.collect { client ->
                when (client) {
                    is DataState.Error -> {
                        setDialogState(
                            UserDetailState.DialogState.Error(
                                client.message,
                            ),
                        )
                    }
                    DataState.Loading -> {
                        setDialogState(UserDetailState.DialogState.Loading)
                    }
                    is DataState.Success -> {
                        setDialogState(null)
                        updateState {
                            it.copy(
                                client = client.data,
                            )
                        }
                        val updatedUser = state.userData?.copy(
                            officeName = client.data.officeName ?: "",
                        )
                        updatedUser?.let {
                            userPreferencesRepositoryImpl.updateUser(
                                it,
                            )
                        }
                    }
                }
            }
        }

        viewModelScope.launch {
            homeRepositoryImp.clientImage(state.clientId ?: -1L).catch {
            }.collect { image ->
                when (image) {
                    is DataState.Error -> {
                        setDialogState(
                            UserDetailState.DialogState.Error(
                                image.message,
                            ),
                        )
                    }
                    DataState.Loading -> {
                        setDialogState(UserDetailState.DialogState.Loading)
                    }
                    is DataState.Success -> {
                        setDialogState(null)
                        userPreferencesRepositoryImpl.updateProfileImage(
                            image.data,
                        )

                        setUserProfile(image.data)
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
            updateState { it.copy(profileImage = decodedBitmap) }
        } catch (e: Exception) {
            logger.d { e.message.toString() }
        }
    }

    fun registerNotification(token: String) {
        viewModelScope.launch {
            val payload = state.clientId?.let { NotificationRegisterPayload(it, token) }
            val result = userDetailRepositoryImp.registerNotification(payload)
            when (result) {
                is DataState.Error -> {
                    payload?.let { getUserNotificationId(it, token) }
                }

                DataState.Loading -> {
                    setDialogState(UserDetailState.DialogState.Loading)
                }

                is DataState.Success -> {
                    userPreferencesRepositoryImpl.setSentTokenToServer(true)
                    userPreferencesRepositoryImpl.saveGcmToken(token)
                }
            }
        }
    }

    private fun getUserNotificationId(payload: NotificationRegisterPayload, token: String) {
        viewModelScope.launch {
            state.clientId?.let {
                userDetailRepositoryImp.getUserNotificationId(it).catch { e ->
                    setDialogState(
                        UserDetailState.DialogState.Error(
                            e.message ?: "An error occurred",
                        ),
                    )
                    logger.d { "Notifications@@@ $e" }
                }.collect { userDetail ->
                    when (userDetail) {
                        is DataState.Error -> {
                            setDialogState(
                                UserDetailState.DialogState.Error(
                                    userDetail.message,
                                ),
                            )
                        }
                        DataState.Loading -> {
                            setDialogState(UserDetailState.DialogState.Loading)
                        }
                        is DataState.Success -> {
                            updateRegistrationNotification(userDetail.data.id.toLong(), payload, token)
                        }
                    }
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
            val result = userDetailRepositoryImp.updateRegisterNotification(id, payload)
            when (result) {
                is DataState.Error -> {
                    logger.d { "Notifications@@@ ${result.message}" }
                    setDialogState(
                        UserDetailState.DialogState.Error(
                            result.message,
                        ),
                    )
                }
                DataState.Loading -> {
                    setDialogState(UserDetailState.DialogState.Loading)
                }
                is DataState.Success -> {
                    userPreferencesRepositoryImpl.setSentTokenToServer(true)
                    userPreferencesRepositoryImpl.saveGcmToken(token)
                }
            }
        }
    }
}

@Parcelize
data class UserDetailState(
    val clientId: Long? = null,
    val token: String? = null,
    val isOnline: Boolean = false,
    @IgnoredOnParcel
    val userData: UserData? = null,
    @IgnoredOnParcel
    val client: Client? = null,
    val profileImage: ByteArray? = null,
    val dialogState: DialogState?,
) : Parcelable {
    sealed interface DialogState : Parcelable {
        @Parcelize
        data class Error(val message: String) : DialogState

        @Parcelize
        data object Loading : DialogState
    }
}

sealed interface UserDetailEvent {
    data object Navigate : UserDetailEvent
    data class ShowToast(val message: String) : UserDetailEvent
    data object ChangePassword : UserDetailEvent
}

sealed interface UserDetailAction {
    data object OnNavigate : UserDetailAction
    data object OnChangePassword : UserDetailAction
    data object OnRetry : UserDetailAction
    data class RegisterNotification(val token: String) : UserDetailAction
}
