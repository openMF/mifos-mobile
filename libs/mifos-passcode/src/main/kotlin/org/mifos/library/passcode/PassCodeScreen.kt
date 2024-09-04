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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.library.passcode.component.MifosIcon
import org.mifos.library.passcode.component.PasscodeForgotButton
import org.mifos.library.passcode.component.PasscodeHeader
import org.mifos.library.passcode.component.PasscodeKeys
import org.mifos.library.passcode.component.PasscodeMismatchedDialog
import org.mifos.library.passcode.component.PasscodeSkipButton
import org.mifos.library.passcode.component.PasscodeToolbar
import org.mifos.library.passcode.theme.blueTint
import org.mifos.library.passcode.utility.Constants.PASSCODE_LENGTH
import org.mifos.library.passcode.utility.PreferenceManager
import org.mifos.library.passcode.utility.ShakeAnimation.performShakeAnimation
import org.mifos.library.passcode.utility.VibrationFeedback.vibrateFeedback
import org.mifos.library.passcode.viewmodels.PasscodeViewModel

@Composable
internal fun PasscodeScreen(
    onForgotButton: () -> Unit,
    onSkipButton: () -> Unit,
    onPasscodeConfirm: (String) -> Unit,
    onPasscodeRejected: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PasscodeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }

    val activeStep by viewModel.activeStep.collectAsStateWithLifecycle()
    val filledDots by viewModel.filledDots.collectAsStateWithLifecycle()
    val passcodeVisible by viewModel.passcodeVisible.collectAsStateWithLifecycle()
    val currentPasscode by viewModel.currentPasscodeInput.collectAsStateWithLifecycle()

    val xShake = remember { Animatable(initialValue = 0.0F) }
    var passcodeRejectedDialogVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = viewModel.onPasscodeConfirmed) {
        viewModel.onPasscodeConfirmed.collect {
            onPasscodeConfirm(it)
        }
    }

    LaunchedEffect(key1 = viewModel.onPasscodeRejected) {
        viewModel.onPasscodeRejected.collect {
            passcodeRejectedDialogVisible = true
            vibrateFeedback(context)
            performShakeAnimation(xShake)
            onPasscodeRejected()
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
            PasscodeToolbar(activeStep = activeStep, preferenceManager.hasPasscode)

            PasscodeSkipButton(
                hasPassCode = preferenceManager.hasPasscode,
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
                    activeStep = activeStep,
                    isPasscodeAlreadySet = preferenceManager.hasPasscode,
                )
                PasscodeView(
                    restart = { viewModel.restart() },
                    togglePasscodeVisibility = { viewModel.togglePasscodeVisibility() },
                    filledDots = filledDots,
                    passcodeVisible = passcodeVisible,
                    currentPasscode = currentPasscode,
                    passcodeRejectedDialogVisible = passcodeRejectedDialogVisible,
                    onDismissDialog = { passcodeRejectedDialogVisible = false },
                    xShake = xShake,
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            PasscodeKeys(
                enterKey = viewModel::enterKey,
                deleteKey = viewModel::deleteKey,
                deleteAllKeys = viewModel::deleteAllKeys,
                modifier = Modifier.padding(horizontal = 12.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            PasscodeForgotButton(
                hasPassCode = preferenceManager.hasPasscode,
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

@Preview(showBackground = true)
@Composable
private fun PasscodeScreenPreview() {
    PasscodeScreen(
        onForgotButton = {},
        onSkipButton = {},
        onPasscodeConfirm = {},
        onPasscodeRejected = {},
    )
}
