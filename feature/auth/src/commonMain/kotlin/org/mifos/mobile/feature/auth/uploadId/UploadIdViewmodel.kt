/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.uploadId

import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.size
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.auth.generated.resources.Res
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_error_dob_required
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_error_field_empty
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_error_id_required
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_error_mobile_not_valid
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_error_photo_required
import mifos_mobile.feature.auth.generated.resources.feature_upload_id_upload_failed
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.toBase64DataUri
import org.mifos.mobile.core.ui.utils.BaseViewModel

internal class UploadIdViewModel :
    BaseViewModel<UploadIdUiState, UploadIdEvent, UploadIdAction>(
        initialState = UploadIdUiState(dialogState = null),
    ) {
    override fun handleAction(action: UploadIdAction) {
        when (action) {
            UploadIdAction.OnBackClick -> sendEvent(UploadIdEvent.BackClick)

            is UploadIdAction.OnDOBChange -> handleDobChange(action.dob)

            is UploadIdAction.OnMobileChange -> handleCellPhoneChange(action.cellPhone)

            is UploadIdAction.OnNationalIdChange -> handleNationalIdChange(action.nationalId)

            is UploadIdAction.ToggleShowDatePicker -> toggleDatePicker()

            is UploadIdAction.OnSubmit -> validateDetails()

            is UploadIdAction.OnPickId -> handleImageUpload(action)

            is UploadIdAction.OnPickImage -> handleImageUpload(action)

            is UploadIdAction.DismissDialog -> dismissDialog()

            is UploadIdAction.OnRemoveId -> {
                mutableStateFlow.update {
                    it.copy(
                        idFile = null,
                        idFileName = null,
                        idFileSize = null,
                    )
                }
            }

            is UploadIdAction.OnRemoveImage -> {
                mutableStateFlow.update {
                    it.copy(
                        imageFile = null,
                        imageFileName = null,
                        imageFileSize = null,
                    )
                }
            }
        }
    }

    private var validationJob: Job? = null

    private fun handleCellPhoneChange(cellPhone: String) {
        mutableStateFlow.update {
            it.copy(
                cellPhone = cellPhone,
                cellPhoneError = null,
            )
        }

        debounceValidation {
            val result = validateCellPhone(cellPhone)
            mutableStateFlow.update {
                it.copy(
                    cellPhoneError = result,
                )
            }
        }
    }

    private fun handleNationalIdChange(nationalId: String) {
        mutableStateFlow.update {
            it.copy(
                nationalId = nationalId,
                nationalIdError = null,
            )
        }

        debounceValidation {
            val result = validateNationalId(nationalId)
            mutableStateFlow.update {
                it.copy(
                    nationalIdError = result,
                )
            }
        }
    }

    private fun handleDobChange(dob: String) {
        mutableStateFlow.update {
            it.copy(
                dob = dob,
                dobError = null,
            )
        }

        debounceValidation {
            val result = validateDob(dob)
            mutableStateFlow.update {
                it.copy(
                    dobError = result,
                )
            }
        }
    }

    private fun validateDob(dob: String): StringResource? {
        return when {
            dob.isBlank() -> Res.string.feature_upload_id_error_dob_required
            else -> null
        }
    }

    private fun validateCellPhone(cellPhone: String): StringResource? {
        return when {
            cellPhone.isBlank() -> Res.string.feature_upload_id_error_field_empty
//            !ValidationHelper.isValidPhoneNumber(cellPhone) -> Res.string.feature_upload_id_error_mobile_not_valid
            cellPhone.length > 10 -> Res.string.feature_upload_id_error_mobile_not_valid
            else -> null
        }
    }

    private fun validateNationalId(nationalId: String): StringResource? {
        return when {
            nationalId.isBlank() -> Res.string.feature_upload_id_error_field_empty
            else -> null
        }
    }

    private fun validateDetails() {
        val nationalIdError = validateNationalId(state.nationalId)
        val mobileError = validateCellPhone(state.cellPhone)
        val dobError = validateDob(state.dob)

        mutableStateFlow.update {
            it.copy(
                nationalIdError = nationalIdError,
                cellPhoneError = mobileError,
                dobError = dobError,
            )
        }

        val errorMsg = when {
            state.idFile == null -> Res.string.feature_upload_id_error_id_required
            state.imageFile == null -> Res.string.feature_upload_id_error_photo_required
            else -> null
        }

        val errorFree = nationalIdError == null &&
            mobileError == null &&
            dobError == null

        if (errorFree && errorMsg == null) {
            uploadDetails()
        } else if (errorMsg != null) {
            mutableStateFlow.update {
                it.copy(
                    dialogState = UploadIdUiState.DialogState.Error(errorMsg),
                )
            }
        }
    }

    private fun uploadDetails() {
        // TODO call api
//        viewModelScope.launch {
//            mutableStateFlow.update {
//                it.copy(dialogState = UploadIdUiState.DialogState.Loading)
//            }
//            delay(3000)
//            sendEvent(UploadIdEvent.NavigateToOtp)
//        }
    }

    private fun toggleDatePicker() {
        mutableStateFlow.update {
            it.copy(
                showDatePicker = !state.showDatePicker,
            )
        }
    }

    private fun handleImageUpload(action: UploadIdAction) {
        viewModelScope.launch {
            try {
                val image = FileKit.openFilePicker(type = FileKitType.Image)
                image?.let { file ->
                    val sizeInKB = file.size() / 1024
                    val sizeInMb = sizeInKB / 1024
                    val showFileSize = if (sizeInMb >= 1) {
                        "$sizeInMb MB"
                    } else {
                        "$sizeInKB KB"
                    }

                    when (action) {
                        is UploadIdAction.OnPickId -> {
                            mutableStateFlow.update {
                                it.copy(
                                    idFile = file.readBytes().toBase64DataUri(),
                                    idFileName = file.name,
                                    idFileSize = showFileSize,
                                )
                            }
                        }
                        is UploadIdAction.OnPickImage -> {
                            mutableStateFlow.update {
                                it.copy(
                                    imageFile = file.readBytes().toBase64DataUri(),
                                    imageFileName = file.name,
                                    imageFileSize = showFileSize,
                                )
                            }
                        }
                        else -> {}
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                mutableStateFlow.update {
                    it.copy(
                        dialogState = UploadIdUiState.DialogState.Error(
                            Res.string.feature_upload_id_upload_failed,
                        ),
                    )
                }
            }
        }
    }

    private fun debounceValidation(validation: suspend () -> Unit) {
        validationJob?.cancel()
        validationJob = viewModelScope.launch {
            delay(300)
            validation()
        }
    }

    private fun dismissDialog() {
        mutableStateFlow.update {
            it.copy(dialogState = null)
        }
    }
}

internal data class UploadIdUiState(
    val idUpload: String = "",
    val photoUpload: String = "",
    val cellPhone: String = "",
    val nationalId: String = "",
    val dob: String = "",
    val showDatePicker: Boolean = false,

    val idFile: String? = null,
    val idFileName: String? = null,
    val idFileSize: String? = null,

    val imageFile: String? = null,
    val imageFileName: String? = null,
    val imageFileSize: String? = null,

    val cellPhoneError: StringResource? = null,
    val nationalIdError: StringResource? = null,
    val dobError: StringResource? = null,

    val dialogState: DialogState?,
) {
    sealed interface DialogState {
        data class Error(val message: StringResource) : DialogState

        data object Loading : DialogState
    }
}

internal sealed interface UploadIdAction {
    data object OnSubmit : UploadIdAction
    data object OnBackClick : UploadIdAction
    data object ToggleShowDatePicker : UploadIdAction
    data object OnRemoveId : UploadIdAction
    data object OnRemoveImage : UploadIdAction
    data class OnMobileChange(val cellPhone: String) : UploadIdAction
    data class OnNationalIdChange(val nationalId: String) : UploadIdAction
    data class OnDOBChange(val dob: String) : UploadIdAction

    data object OnPickId : UploadIdAction
    data object OnPickImage : UploadIdAction

    data object DismissDialog : UploadIdAction
}

internal sealed interface UploadIdEvent {
    data object NavigateToOtp : UploadIdEvent
    data object BackClick : UploadIdEvent
}
