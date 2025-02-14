/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.library.passcode

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.library.passcode.component.MifosIcon
import org.mifos.library.passcode.component.PasscodeForgotButton
import org.mifos.library.passcode.component.PasscodeHeader
import org.mifos.library.passcode.component.PasscodeKeys
import org.mifos.library.passcode.component.PasscodeMismatchedDialog
import org.mifos.library.passcode.component.PasscodeSkipButton
import org.mifos.library.passcode.component.PasscodeToolbar
import org.mifos.library.passcode.theme.blueTint
import org.mifos.library.passcode.utility.Constants.PASSCODE_LENGTH
import org.mifos.library.passcode.utility.ShakeAnimation.performShakeAnimation
import org.mifos.library.passcode.viewmodels.PasscodeAction
import org.mifos.library.passcode.viewmodels.PasscodeEvent
import org.mifos.library.passcode.viewmodels.PasscodeViewModel
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun PasscodeScreen(
    onForgotButton: () -> Unit,
    onSkipButton: () -> Unit,
    onPasscodeConfirm: (String) -> Unit,
    onPasscodeRejected: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PasscodeViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    val xShake = remember { Animatable(initialValue = 0.0F) }
    var passcodeRejectedDialogVisible by remember { mutableStateOf(false) }

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is PasscodeEvent.PasscodeConfirmed -> {
                onPasscodeConfirm(event.passcode)
            }

            is PasscodeEvent.PasscodeRejected -> {
                passcodeRejectedDialogVisible = true
                scope.launch {
                    performShakeAnimation(xShake)
                }
                onPasscodeRejected()
            }
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PasscodeToolbar(activeStep = state.activeStep, state.hasPasscode)

            PasscodeSkipButton(
                hasPassCode = state.hasPasscode,
                onSkipButton = onSkipButton,
            )

            MifosIcon(modifier = Modifier.fillMaxWidth())

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PasscodeHeader(
                    activeStep = state.activeStep,
                    isPasscodeAlreadySet = state.hasPasscode,
                )
                PasscodeView(
                    restart = remember(viewModel) {
                        { viewModel.trySendAction(PasscodeAction.Restart) }
                    },
                    togglePasscodeVisibility = remember(viewModel) {
                        { viewModel.trySendAction(PasscodeAction.TogglePasscodeVisibility) }
                    },
                    filledDots = state.filledDots,
                    passcodeVisible = state.passcodeVisible,
                    currentPasscode = state.currentPasscodeInput,
                    passcodeRejectedDialogVisible = passcodeRejectedDialogVisible,
                    onDismissDialog = { passcodeRejectedDialogVisible = false },
                    xShake = xShake,
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            PasscodeKeys(
                enterKey = remember(viewModel) {
                    { viewModel.trySendAction(PasscodeAction.EnterKey(it)) }
                },
                deleteKey = remember(viewModel) {
                    { viewModel.trySendAction(PasscodeAction.DeleteKey) }
                },
                deleteAllKeys = remember(viewModel) {
                    { viewModel.trySendAction(PasscodeAction.DeleteAllKeys) }
                },
            )

            Spacer(modifier = Modifier.height(8.dp))

            PasscodeForgotButton(
                hasPassCode = state.hasPasscode,
                onForgotButton = onForgotButton,
            )
        }
    }
}

@Composable
private fun PasscodeView(
    restart: () -> Unit,
    togglePasscodeVisibility: () -> Unit,
    filledDots: Int,
    passcodeVisible: Boolean,
    currentPasscode: String,
    passcodeRejectedDialogVisible: Boolean,
    onDismissDialog: () -> Unit,
    xShake: Animatable<Float, *>,
    modifier: Modifier = Modifier,
) {
    PasscodeMismatchedDialog(
        visible = passcodeRejectedDialogVisible,
        onDismiss = {
            onDismissDialog.invoke()
            restart()
        },
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.offset { IntOffset(xShake.value.toInt(), 0) },
            horizontalArrangement = Arrangement.spacedBy(
                space = 26.dp,
                alignment = Alignment.CenterHorizontally,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(PASSCODE_LENGTH) { dotIndex ->
                if (passcodeVisible && dotIndex < currentPasscode.length) {
                    Text(
                        text = currentPasscode[dotIndex].toString(),
                        color = blueTint,
                    )
                } else {
                    val isFilledDot = dotIndex + 1 <= filledDots
                    val dotColor = animateColorAsState(
                        if (isFilledDot) blueTint else Color.Gray,
                        label = "",
                    )

                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(
                                color = dotColor.value,
                                shape = CircleShape,
                            ),
                    )
                }
            }
        }

        IconButton(
            onClick = togglePasscodeVisibility,
            modifier = Modifier.padding(start = 10.dp),
        ) {
            Icon(
                imageVector = if (passcodeVisible) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                },
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
private fun PasscodeScreenPreview() {
    PasscodeScreen(
        onForgotButton = {},
        onSkipButton = {},
        onPasscodeConfirm = {},
        onPasscodeRejected = {},
    )
}
