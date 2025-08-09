package org.mifos.mobile.feature.settings.profile

import androidx.lifecycle.viewModelScope
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openFilePicker
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.profile_image_delete_failed
import mifos_mobile.feature.settings.generated.resources.profile_image_update_failed
import mifos_mobile.feature.settings.generated.resources.profile_image_update_success
import mifos_mobile.feature.settings.generated.resources.profile_load_failed
import mifos_mobile.feature.settings.generated.resources.profile_name_empty_error
import mifos_mobile.feature.settings.generated.resources.profile_name_invalid_format_error
import mifos_mobile.feature.settings.generated.resources.profile_name_too_long_error
import mifos_mobile.feature.settings.generated.resources.profile_name_too_short_error
import mifos_mobile.feature.settings.generated.resources.profile_too_many_attempts
import mifos_mobile.feature.settings.generated.resources.profile_unsaved_changes_message
import mifos_mobile.feature.settings.generated.resources.profile_update_failed
import mifos_mobile.feature.settings.generated.resources.profile_update_success
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.ui.utils.BaseViewModel
import org.mifos.mobile.core.ui.utils.EmailValidationResult
import org.mifos.mobile.core.ui.utils.PhoneValidationResult
import org.mifos.mobile.core.ui.utils.ValidationHelper
import org.mifos.mobile.feature.settings.password.ValidationResult

internal class UpdateProfileViewModel : BaseViewModel<ProfileState, ProfileEvent, ProfileAction>(
    initialState = ProfileState(),
) {
    private var validationJob: Job? = null
    private var submitAttempts = 0
    private val maxSubmitAttempts = 5

    init {
        loadProfile()
    }

    override fun handleAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.OnNameChanged -> onNameChange(action.name)

            is ProfileAction.OnEmailChanged -> onEmailChange(action.email)

            is ProfileAction.OnMobileChanged -> onMobileChange(action.mobile)

            is ProfileAction.Internal.LoadProfileResult -> handleLoadProfileResult(action)

            is ProfileAction.Internal.UpdateProfileResult -> handleUpdateProfileResult(action)

            is ProfileAction.Internal.HandleImageUpdateResult -> handleImageUpdateResult(action)

            ProfileAction.OnSubmit -> validateAndSubmit()
            ProfileAction.RetrySubmit -> resetSubmitAttempts()

            ProfileAction.NavigateBack -> navigateBack()
            ProfileAction.DismissDialog -> dismissDialog()
            ProfileAction.DiscardChanges -> handleDiscardChangesAndNavigateBack()

            ProfileAction.PickImage -> updateProfileImage()
            ProfileAction.DeleteImage -> deleteProfileImage()
        }
    }

    private fun loadProfile() {
        mutableStateFlow.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            try {
                // TODO: Load actual profile from repository
                sendAction(
                    ProfileAction.Internal.LoadProfileResult(ProfileLoadResult.Success),
                )
            } catch (_: Exception) {
                trySendAction(
                    ProfileAction.Internal.LoadProfileResult(
                        ProfileLoadResult.Failure(Res.string.profile_load_failed),
                    ),
                )
            }
        }
    }

    private fun validateName(name: String): ValidationResult = when {
        name.isEmpty() -> ValidationResult.Error(Res.string.profile_name_empty_error)
        name.length < 2 -> ValidationResult.Error(Res.string.profile_name_too_short_error)
        name.length > 50 -> ValidationResult.Error(Res.string.profile_name_too_long_error)
        !ValidationHelper.isValidName(name) -> ValidationResult.Error(Res.string.profile_name_invalid_format_error)
        else -> ValidationResult.Success
    }

    private fun validateEmail(email: String): ValidationResult {
        return when (val result = ValidationHelper.validateEmailWithDetails(email)) {
            is EmailValidationResult.Valid -> ValidationResult.Success
            is EmailValidationResult.Invalid -> ValidationResult.Error(result.errorResource)
        }
    }

    private fun validateMobile(mobile: String): ValidationResult {
        val result = ValidationHelper.validatePhoneNumberWithDetails(mobile)
        when (result) {
            is PhoneValidationResult.Valid -> {
                // Update the state with formatted number
                mutableStateFlow.update {
                    it.copy(mobile = result.formattedNumber)
                }
                return ValidationResult.Success
            }

            is PhoneValidationResult.Invalid -> {
                return ValidationResult.Error(result.errorResource)
            }
        }
    }

    private fun onNameChange(newValue: String) {
        mutableStateFlow.update {
            it.copy(
                name = newValue,
                nameError = null,
                hasChanges = true,
            )
        }

        debounceValidation {
            val result = validateName(newValue)
            mutableStateFlow.update {
                it.copy(
                    nameError = if (result is ValidationResult.Error) result.message else null,
                )
            }
        }
    }

    private fun onEmailChange(newValue: String) {
        mutableStateFlow.update {
            it.copy(
                email = newValue,
                emailError = null,
                hasChanges = true,
            )
        }

        debounceValidation {
            val result = validateEmail(newValue)
            mutableStateFlow.update {
                it.copy(
                    emailError = if (result is ValidationResult.Error) result.message else null,
                )
            }
        }
    }

    private fun onMobileChange(newValue: String) {
        mutableStateFlow.update {
            it.copy(
                mobile = newValue,
                mobileError = null,
                hasChanges = true,
            )
        }

        debounceValidation {
            val result = validateMobile(newValue)
            mutableStateFlow.update {
                it.copy(
                    mobileError = if (result is ValidationResult.Error) result.message else null,
                )
            }
        }
    }

    private fun validateAndSubmit() {
        if (submitAttempts >= maxSubmitAttempts) {
            mutableStateFlow.update {
                it.copy(
                    dialogState = ProfileState.DialogState.Error(
                        Res.string.profile_too_many_attempts,
                    ),
                )
            }
            return
        }

        val nameResult = validateName(state.name)
        val emailResult = validateEmail(state.email)
        val mobileResult = validateMobile(state.mobile)

        mutableStateFlow.update {
            it.copy(
                nameError = if (nameResult is ValidationResult.Error) nameResult.message else null,
                emailError = if (emailResult is ValidationResult.Error) emailResult.message else null,
                mobileError = if (mobileResult is ValidationResult.Error) mobileResult.message else null,
            )
        }

        val isValid =
            listOf(nameResult, emailResult, mobileResult).all { it is ValidationResult.Success }

        if (isValid) {
            handleSubmit()
        } else {
            submitAttempts++
        }
    }

    private fun handleSubmit() {
        mutableStateFlow.update {
            it.copy(dialogState = ProfileState.DialogState.Loading)
        }

        viewModelScope.launch {
            try {
                // TODO: Update profile in repository
                // val result = profileRepository.updateProfile(state.profile)

                delay(1000)

                trySendAction(
                    ProfileAction.Internal.UpdateProfileResult(
                        ProfileUpdateResult.Success(Res.string.profile_update_success),
                    ),
                )
            } catch (_: Exception) {
                trySendAction(
                    ProfileAction.Internal.UpdateProfileResult(
                        ProfileUpdateResult.Failure(Res.string.profile_update_failed),
                    ),
                )
            }
        }
    }

    private fun updateProfileImage() {
        viewModelScope.launch {
            try {
                val image = FileKit.openFilePicker(type = FileKitType.Image)
                image?.let { file ->
                    mutableStateFlow.update {
                        it.copy(
                            hasChanges = true,
                            image = file,
                        )
                    }
                }

                // TODO:: Upload Image to Server

                trySendAction(
                    ProfileAction.Internal.HandleImageUpdateResult(
                        ImageUpdateResult.Success(Res.string.profile_image_update_success),
                    ),
                )
            } catch (e: Exception) {
                e.printStackTrace()
                trySendAction(
                    ProfileAction.Internal.HandleImageUpdateResult(
                        ImageUpdateResult.Failure(Res.string.profile_image_update_failed),
                    ),
                )
            }
        }
    }

    private fun deleteProfileImage() {
        viewModelScope.launch {
            try {
                // TODO: Delete profile image from repository
                mutableStateFlow.update {
                    it.copy(
                        image = null,
                        hasChanges = true,
                    )
                }
            } catch (_: Exception) {
                mutableStateFlow.update {
                    it.copy(
                        dialogState = ProfileState.DialogState.Error(
                            Res.string.profile_image_delete_failed,
                        ),
                    )
                }
            }
        }
    }

    private fun handleLoadProfileResult(action: ProfileAction.Internal.LoadProfileResult) {
        when (val result = action.result) {
            is ProfileLoadResult.Success -> {
                mutableStateFlow.update {
                    it.copy(
                        name = "MobileByteSensei",
                        email = "test@gmail.com",
                        mobile = "+34908890098",
                        account = "129028932093",
                        isLoading = false,
                    )
                }
            }

            is ProfileLoadResult.Failure -> {
                mutableStateFlow.update {
                    it.copy(
                        isLoading = false,
                        dialogState = ProfileState.DialogState.Error(result.message),
                    )
                }
            }
        }
    }

    private fun handleUpdateProfileResult(action: ProfileAction.Internal.UpdateProfileResult) {
        when (val result = action.result) {
            is ProfileUpdateResult.Success -> {
                mutableStateFlow.update {
                    it.copy(
                        hasChanges = false,
                        dialogState = ProfileState.DialogState.Success(result.message),
                    )
                }
                submitAttempts = 0
            }

            is ProfileUpdateResult.Failure -> {
                submitAttempts++
                mutableStateFlow.update {
                    it.copy(
                        dialogState = ProfileState.DialogState.Error(result.message),
                    )
                }
            }
        }
    }

    private fun handleImageUpdateResult(action: ProfileAction.Internal.HandleImageUpdateResult) {
        when (val result = action.result) {
            is ImageUpdateResult.Success -> {
                mutableStateFlow.update {
                    it.copy(hasChanges = true)
                }
            }

            is ImageUpdateResult.Failure -> {
                mutableStateFlow.update {
                    it.copy(
                        dialogState = ProfileState.DialogState.Error(result.message),
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

    private fun navigateBack() {
        if (state.hasChanges) {
            mutableStateFlow.update {
                it.copy(
                    dialogState = ProfileState.DialogState.UnsavedChanges(
                        Res.string.profile_unsaved_changes_message,
                    ),
                )
            }
        } else {
            sendEvent(ProfileEvent.OnNavigateBack)
        }
    }

    private fun handleDiscardChangesAndNavigateBack() {
        mutableStateFlow.update {
            it.copy(
                hasChanges = false,
                dialogState = null,
            )
        }

        sendEvent(ProfileEvent.OnNavigateBack)
    }

    private fun resetSubmitAttempts() {
        submitAttempts = 0
    }

    override fun onCleared() {
        super.onCleared()
        validationJob?.cancel()
    }
}

internal data class ProfileState(
    val image: Any? = null,
    val name: String = "",
    val email: String = "",
    val account: String = "",
    val mobile: String = "",
    val nameError: StringResource? = null,
    val emailError: StringResource? = null,
    val mobileError: StringResource? = null,
    val isLoading: Boolean = false,
    val hasChanges: Boolean = false,
    val dialogState: DialogState? = null,
) {
    internal sealed interface DialogState {
        data object Loading : DialogState
        data class Success(val message: StringResource) : DialogState
        data class Error(val message: StringResource) : DialogState
        data class UnsavedChanges(val message: StringResource) : DialogState
    }
}

internal sealed interface ProfileEvent {
    data object OnNavigateBack : ProfileEvent
}

internal sealed interface ProfileAction {
    data class OnNameChanged(val name: String) : ProfileAction
    data class OnEmailChanged(val email: String) : ProfileAction
    data class OnMobileChanged(val mobile: String) : ProfileAction

    data object PickImage : ProfileAction
    data object DeleteImage : ProfileAction

    data object OnSubmit : ProfileAction
    data object RetrySubmit : ProfileAction

    data object NavigateBack : ProfileAction
    data object DismissDialog : ProfileAction
    data object DiscardChanges : ProfileAction

    sealed interface Internal : ProfileAction {
        data class LoadProfileResult(val result: ProfileLoadResult) : Internal
        data class UpdateProfileResult(val result: ProfileUpdateResult) : Internal
        data class HandleImageUpdateResult(val result: ImageUpdateResult) : Internal
    }
}

sealed class ProfileLoadResult {
    data object Success : ProfileLoadResult()
    data class Failure(val message: StringResource) : ProfileLoadResult()
}

sealed class ProfileUpdateResult {
    data class Success(val message: StringResource) : ProfileUpdateResult()
    data class Failure(val message: StringResource) : ProfileUpdateResult()
}

sealed class ImageUpdateResult {
    data class Success(val message: StringResource) : ImageUpdateResult()
    data class Failure(val message: StringResource) : ImageUpdateResult()
}