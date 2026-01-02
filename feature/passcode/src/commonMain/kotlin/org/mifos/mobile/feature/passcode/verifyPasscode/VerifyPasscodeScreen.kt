/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.passcode.verifyPasscode

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.passcode.generated.resources.Res
import mifos_mobile.feature.passcode.generated.resources.feature_passcode_authenticate
import mifos_mobile.feature.passcode.generated.resources.feature_passcode_authenticate_tip
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.ui.component.MifosPoweredCard
import org.mifos.mobile.core.ui.utils.EventsEffect
import template.core.base.designsystem.theme.KptTheme

@Composable
internal fun VerifyPasscodeScreen(
    onPasscodeConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VerifyPasscodeViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            is VerifyPasscodeEvent.PasscodeAccepted -> {
                onPasscodeConfirm.invoke()
            }
        }
    }

    VerifyPasscodeScreenContent(
        state = state,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
private fun VerifyPasscodeScreenContent(
    state: VerifyPasscodeState,
    modifier: Modifier = Modifier,
    onAction: (VerifyPasscodeAction) -> Unit,
) {
    MifosScaffold(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(KptTheme.spacing.md)
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = MifosIcons.LockFilled,
                    contentDescription = null,
                    modifier = Modifier
                        .background(
                            color = KptTheme.colorScheme.primary.copy(alpha = 0.2f),
                            shape = CircleShape,
                        )
                        .padding(KptTheme.spacing.sm),
                    tint = KptTheme.colorScheme.primary,
                )

                Spacer(modifier = Modifier.height(DesignToken.spacing.dp24))

                Text(
                    text = stringResource(Res.string.feature_passcode_authenticate),
                    style = MifosTypography.titleMedium,
                    color = KptTheme.colorScheme.onBackground,
                )

                Spacer(modifier = Modifier.height(KptTheme.spacing.sm))

                Text(
                    text = stringResource(Res.string.feature_passcode_authenticate_tip),
                    style = MifosTypography.bodySmall,
                    color = KptTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(KptTheme.spacing.xl))

                Row(horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.md)) {
                    repeat(state.maxDigits) { index ->
                        val filled = index < state.filledDots
                        val color = when {
                            filled -> KptTheme.colorScheme.primary
                            else -> Color.Transparent
                        }
                        val borderColor =
                            if (state.passcodeError) {
                                KptTheme.colorScheme.error
                            } else {
                                KptTheme.colorScheme.primary
                            }
                        Box(
                            modifier = Modifier
                                .size(DesignToken.sizes.iconSmall)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    DesignToken.strokes.thin,
                                    borderColor,
                                    CircleShape,
                                ),
                        )
                    }
                }
            }

            MifosPoweredCard(
                modifier = Modifier.fillMaxWidth(),
            )

            NumericKeyboard(
                onDigitClick = { digit -> onAction(VerifyPasscodeAction.OnDigitClick(digit)) },
                onBackspaceClick = { onAction(VerifyPasscodeAction.OnBackspaceClick) },
                onSendClick = { onAction(VerifyPasscodeAction.OnContinueClick) },
                isSendEnabled = state.filledDots == state.maxDigits,
            )
        }
    }
}

@Composable
fun NumericKeyboard(
    onDigitClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onSendClick: () -> Unit,
    isSendEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val layout = listOf(
        listOf(PasscodeKey.Digit("1"), PasscodeKey.Digit("2"), PasscodeKey.Digit("3")),
        listOf(PasscodeKey.Digit("4"), PasscodeKey.Digit("5"), PasscodeKey.Digit("6")),
        listOf(PasscodeKey.Digit("7"), PasscodeKey.Digit("8"), PasscodeKey.Digit("9")),
        listOf(PasscodeKey.Backspace, PasscodeKey.Digit("0"), PasscodeKey.Send),
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(KptTheme.colorScheme.surface)
            .padding(KptTheme.spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm),
    ) {
        layout.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm),
            ) {
                row.forEach { key ->
                    when (key) {
                        is PasscodeKey.Digit -> DigitKeyButton(
                            modifier = Modifier.weight(1f),
                            digit = key.number,
                            letters = getLettersForDigit(key.number),
                            onClick = { onDigitClick(key.number) },
                        )

                        PasscodeKey.Backspace -> KeyButton(
                            modifier = Modifier.weight(1f),
                            backgroundColor = KptTheme.colorScheme.tertiaryContainer,
                            content = {
                                Icon(
                                    imageVector = MifosIcons.Backspace,
                                    contentDescription = "Backspace",
                                )
                            },
                            onClick = onBackspaceClick,
                        )

                        PasscodeKey.Send -> KeyButton(
                            modifier = Modifier.weight(1f),
                            backgroundColor = KptTheme.colorScheme.inversePrimary,
                            content = {
                                Icon(
                                    imageVector = MifosIcons.Send,
                                    contentDescription = "Send",
                                )
                            },
                            onClick = onSendClick,
                            enabled = isSendEnabled,
                        )
                    }
                }
            }
        }
    }
}

fun getLettersForDigit(digit: String): String {
    return when (digit) {
        "2" -> "ABC"
        "3" -> "DEF"
        "4" -> "GHI"
        "5" -> "JKL"
        "6" -> "MNO"
        "7" -> "PQRS"
        "8" -> "TUV"
        "9" -> "WXYZ"
        else -> ""
    }
}

@Composable
fun KeyButton(
    backgroundColor: Color,
    content: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .width(DesignToken.sizes.boxDp107)
            .height(DesignToken.sizes.boxDp41)
            .clip(DesignToken.shapes.dp100)
            .background(backgroundColor)
            .clickable(onClick = onClick, enabled = enabled),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
fun DigitKeyButton(
    digit: String,
    letters: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .width(DesignToken.sizes.boxDp107)
            .height(DesignToken.sizes.boxDp41)
            .clip(DesignToken.shapes.dp100)
            .background(KptTheme.colorScheme.surfaceContainerLow)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = digit,
                style = MifosTypography.keyBoardNumeric,
                color = KptTheme.colorScheme.onBackground,
            )
            if (letters.isNotEmpty()) {
                Text(
                    text = letters,
                    style = MifosTypography.keyBoardAlpha,
                    color = KptTheme.colorScheme.outline,
                )
            }
        }
    }
}

sealed class PasscodeKey {
    data class Digit(val number: String) : PasscodeKey()
    object Backspace : PasscodeKey()
    object Send : PasscodeKey()
}

@Preview
@Composable
private fun Passcode_Preview() {
    MifosMobileTheme {
        VerifyPasscodeScreenContent(
            state = VerifyPasscodeState(),
            onAction = {},
        )
    }
}
