package org.mifos.mobile.feature.settings.password

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.core.ui.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosPasswordField
import org.mifos.mobile.core.designsystem.theme.AppSizes
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun ChangePasswordScreen(
    navigateBack: () -> Unit,
    navigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewmodel: ChangePasswordViewModel = koinViewModel(),
) {
    val state = viewmodel.stateFlow.collectAsStateWithLifecycle().value

    EventsEffect(eventFlow = viewmodel.eventFlow) { event ->
        when (event) {
            PasswordEvent.OnNavigateBack -> navigateBack.invoke()
            PasswordEvent.OnNavigateToLogin -> navigateToLogin.invoke()
        }
    }

    ChangePasswordScreen(
        modifier = modifier,
        state = state,
        onAction = remember(viewmodel) {
            { viewmodel.trySendAction(it) }
        },
    )

    PasswordDialog(
        dialogState = state.dialogState,
        onConfirm = remember(viewmodel) {
            { viewmodel.trySendAction(PasswordAction.NavigateToLogin) }
        },
        onDismiss = remember(viewmodel) {
            { viewmodel.trySendAction(PasswordAction.DismissDialog) }
        },
    )
}

@Composable
internal fun ChangePasswordScreen(
    state: PasswordState,
    modifier: Modifier = Modifier,
    onAction: (action: PasswordAction) -> Unit,
) {
    MifosElevatedScaffold(
        modifier = modifier,
        onNavigateBack = remember(state) {
            { onAction(PasswordAction.NavigateBack) }
        },
        topBarTitle = stringResource(Res.string.feature_settings_password),
    ) {
        PasswordScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppSizes().headerToContentHeight),
            state = state,
            onAction = onAction,
        )
    }
}

@Composable
internal fun PasswordScreenContent(
    state: PasswordState,
    modifier: Modifier = Modifier,
    onAction: (action: PasswordAction) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 24.dp)
            .statusBarsPadding(),
    ) {
        MifosPasswordField(
            modifier = Modifier,
            value = state.oldPassword,
            hint = if(state.oldPasswordError!=null){
                stringResource(state.oldPasswordError)
            }else{null},
            showPassword = state.oldPasswordVisible,
            label = Res.string.feature_settings_old_password,
            showPasswordChange = { onAction(PasswordAction.OldPasswordVisibleClick) },
            onValueChange = { onAction(PasswordAction.OnOldPasswordChange(it)) },
        )

        Spacer(modifier = Modifier.height(12.dp))

        MifosPasswordField(
            modifier = Modifier,
            value = state.newPassword,
            errors = state.passwordFeedback,
            errorText = state.newPasswordError,
            showPassword = state.newPasswordVisible,
            label = Res.string.feature_settings_new_password,
            passwordStrengthState = state.passwordStrengthState,
            showPasswordChange = { onAction(PasswordAction.NewPasswordVisibleClick) },
            onValueChange = { onAction(PasswordAction.OnNewPasswordChange(it)) },
        )

        Spacer(modifier = Modifier.height(12.dp))

        MifosPasswordField(
            modifier = Modifier,
            value = state.confirmPassword,
            hint = state.confirmPasswordError,
            showPassword = state.confirmPasswordVisible,
            label = Res.string.feature_settings_confirm_new_password,
            showPasswordChange = { onAction(PasswordAction.ConfirmPasswordVisibleClick) },
            onValueChange = {
                onAction(PasswordAction.OnConfirmPasswordChange(it))
            },
        )

        Spacer(modifier = Modifier.height(24.dp))

        MbsIconButton(
            text = stringResource(Res.string.feature_settings_next),
            trailingIcon = Res.drawable.ic_icon_arrow_narrow_right,
            onClick = {
                onAction.invoke(PasswordAction.SubmitClick)
            },
        )
    }
}

@Composable
private fun PasswordDialog(
    dialogState: PasswordState.DialogState?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    when (dialogState) {
        is PasswordState.DialogState.Success -> MbsSuccessDialog(
            visibilityState = SuccessDialogState.Shown(
                message = Res.string.password_update_success_message,
                title = dialogState.message,
                buttonText = Res.string.password_update_dialog_button,
                onBtnClick = onConfirm,
            ),
        )

        is PasswordState.DialogState.Error -> MbsBasicDialog(
            visibilityState = BasicDialogState.Shown(
                message = stringResource(dialogState.message),
            ),
            onDismissRequest = onDismiss,
        )

        is PasswordState.DialogState.Loading -> {
            MbsLoadingDialog(visibilityState = LoadingDialogState.Shown)
        }

        null -> Unit
    }
}