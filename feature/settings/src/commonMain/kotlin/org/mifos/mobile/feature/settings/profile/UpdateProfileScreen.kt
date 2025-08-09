package org.mifos.mobile.feature.settings.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.request.ImageRequest
import coil3.request.crossfade
import mifos_mobile.feature.settings.generated.resources.Res
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.BasicDialogState
import org.mifos.mobile.core.designsystem.component.MifosBasicDialog
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosTextField
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppSizes
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.component.MifosSuccessDialog
import org.mifos.mobile.core.ui.component.SuccessDialogState
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun UpdateProfileScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateProfileViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(eventFlow = viewModel.eventFlow) { event ->
        when (event) {
            ProfileEvent.OnNavigateBack -> navigateBack.invoke()
        }
    }

    UpdateProfileScreen(
        modifier = modifier,
        state = state,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )

    ProfileDialog(
        dialogState = state.dialogState,
        onConfirm = remember(viewModel) {
            { viewModel.trySendAction(ProfileAction.DismissDialog) }
        },
        onDismiss = remember(viewModel) {
            { viewModel.trySendAction(ProfileAction.DismissDialog) }
        },
        discardChanges = remember(viewModel) {
            { viewModel.trySendAction(ProfileAction.DiscardChanges) }
        },
    )
}

@Composable
internal fun UpdateProfileScreen(
    state: ProfileState,
    modifier: Modifier = Modifier,
    onAction: (action: ProfileAction) -> Unit,
) {
    MifosElevatedScaffold(
        modifier = modifier.fillMaxSize(),
        onNavigateBack = remember(state) {
            { onAction(ProfileAction.NavigateBack) }
        },
        topBarTitle = stringResource(Res.string.feature_settings_profile_topbar_title),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppSizes().headerToContentHeight),
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )
            } else {
                ProfileScreenContent(
                    state = state,
                    onAction = onAction,
                )
            }
        }
    }
}

@Composable
internal fun ProfileScreenContent(
    state: ProfileState,
    modifier: Modifier = Modifier,
    onAction: (action: ProfileAction) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 24.dp)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(DesignToken.padding.medium),
    ) {
        ProfileImageSection(
            image = state.image,
            onUpdate = onAction,
            onDelete = onAction,
        )

        MifosTextField(
            value = state.name,
            label = Res.string.feature_settings_profile_label_full_name,
            onValueChange = { onAction(ProfileAction.OnNameChanged(it)) },
            isError = state.nameError != null,
            errorText = state.nameError,
        )

        MbsTextField(
            value = state.email,
            label = Res.string.feature_settings_profile_label_email,
            onValueChange = { onAction(ProfileAction.OnEmailChanged(it)) },
            isError = state.emailError != null,
            errorText = state.emailError,
            keyboardType = KeyboardType.Email,
        )

        MbsTextField(
            value = state.account,
            label = Res.string.feature_settings_profile_label_customer_account,
            onValueChange = {},
            readOnly = true,
        )

        MbsTextField(
            value = state.mobile,
            label = Res.string.feature_settings_profile_label_phone_number,
            onValueChange = { onAction(ProfileAction.OnMobileChanged(it)) },
            isError = state.mobileError != null,
            errorText = state.mobileError,
            keyboardType = KeyboardType.Phone,
        )

        MifosButton(
            text = {
                Text(stringResource(Res.string.feature_settings_profile_submit_changes))
            },
            onClick = { onAction(ProfileAction.OnSubmit) },
            enabled = state.hasChanges && !state.isLoading,
            modifier = Modifier.padding(vertical = 24.dp),
        )
    }
}

@Composable
private fun ProfileImageSection(
    image: Any?,
    onUpdate: (ProfileAction.PickImage) -> Unit,
    onDelete: (ProfileAction.DeleteImage) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(DesignToken.padding.medium),
        modifier = modifier.padding(vertical = DesignToken.padding.average),
    ) {
        Box(
            modifier = Modifier
                .size(DesignToken.sizes.profile)
                .clip(CircleShape)
                .border(
                    width = DesignToken.padding.tiny,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            val context = LocalPlatformContext.current
            val newImage = remember(image) { image ?: UIRes.drawable.ic_icon_logo }
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(newImage)
                    .crossfade(true)
                    .build(),
                imageLoader = rememberImageLoader(context),
                error = painterResource(UIRes.drawable.ic_icon_logo),
                onLoading = {
                    @androidx.compose.runtime.Composable {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
                contentDescription = stringResource(
                    Res.string.feature_settings_profile_content_description_profile_icon,
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }

        Column(
            modifier = Modifier.height(DesignToken.sizes.profile),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            ProfileImageAction(
                icon = UIRes.drawable.ic_icon_edit,
                text = stringResource(Res.string.feature_settings_profile_update_photo),
                onClick = { onUpdate(ProfileAction.PickImage) },
                enabled = true,
            )

            ProfileImageAction(
                icon = MifosIcons.Delete,
                text = stringResource(Res.string.feature_settings_profile_delete_photo),
                onClick = { onDelete(ProfileAction.DeleteImage) },
                enabled = image != null,
            )
        }
    }
}

@Composable
private fun ProfileImageAction(
    icon: Any,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
            .clickable(enabled = enabled, onClick = onClick)
            .alpha(if (enabled) 1f else 0.6f),
    ) {
        when (icon) {
            is DrawableResource -> {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = text,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(DesignToken.sizes.iconMiny),
                )
            }

            is ImageVector -> {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(DesignToken.sizes.iconMiny),
                )
            }
        }

        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            style = MifosTypography.bodyMedium,
        )
    }
}

@Composable
private fun ProfileDialog(
    dialogState: ProfileState.DialogState?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    discardChanges: () -> Unit,
) {
    when (dialogState) {
        is ProfileState.DialogState.Success -> {
            MifosSuccessDialog(
                visibilityState = SuccessDialogState.Shown(
                    title = dialogState.message,
                    message = Res.string.profile_update_success_message,
                    buttonText = Res.string.profile_update_dialog_button,
                    onBtnClick = onConfirm,
                ),
            )
        }

        is ProfileState.DialogState.Error -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    title = stringResource(Res.string.profile_error_dialog_title),
                    message = stringResource(dialogState.message),
                ),
                onDismissRequest = onDismiss,
            )
        }

        is ProfileState.DialogState.UnsavedChanges -> {
            MifosBasicDialog(
                visibilityState = BasicDialogState.Shown(
                    title = stringResource(Res.string.profile_unsaved_changes_title),
                    message = stringResource(dialogState.message),
                ),
                onConfirm = onDismiss,
                onDismissRequest = discardChanges,
                confirmText = stringResource(Res.string.profile_unsaved_changes_stay),
                cancelText = stringResource(Res.string.profile_unsaved_changes_discard),
            )
        }

        is ProfileState.DialogState.Loading -> {
            MifosProgressIndicator()
        }

        null -> Unit
    }
}