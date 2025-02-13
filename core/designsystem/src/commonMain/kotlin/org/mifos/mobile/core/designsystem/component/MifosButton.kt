/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Mifos button with generic content slot. Wraps Material 3 [Button].
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param contentPadding The spacing values to apply internally between the container and the
 * content.
 * @param content The button content.
 */
@Composable
fun MifosButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    content: @Composable RowScope.() -> Unit = {},
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = enabled,
        colors = colors,
        shape = shape,
        elevation = elevation,
        contentPadding = contentPadding,
        content = content,
    )
}

/**
 * Mifos button with generic content slot. Wraps Material 3 [Button].
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param contentPadding The spacing values to apply internally between the container and the
 * content.
 * @param content The button content.
 */
@Composable
fun MifosButton(
    onClick: () -> Unit,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    colors: ButtonColors = ButtonDefaults.buttonColors(),
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = enabled,
        colors = colors,
        shape = shape,
        elevation = elevation,
        contentPadding = contentPadding,
        content = {
            text()
        },
    )
}

/**
 * Mifos outlined button with generic content slot. Wraps Material 3 [OutlinedButton].
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param contentPadding The spacing values to apply internally between the container and the
 * content.
 * @param content The button content.
 */
@Composable
fun MifosOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.outlinedShape,
    border: BorderStroke? = ButtonDefaults.outlinedButtonBorder(enabled),
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit = {},
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(48.dp),
        enabled = enabled,
        shape = shape,
        colors = colors,
        border = border,
        contentPadding = contentPadding,
        content = content,
    )
}

/**
 * Mifos text button with generic content slot. Wraps Material 3 [TextButton].
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param content The button content.
 */
@Composable
fun MifosTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit = {},
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
        content = content,
    )
}

/**
 * Mifos text button with text and icon content slots.
 *
 * @param onClick Will be called when the user clicks the button.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be
 * clickable and will appear disabled to accessibility services.
 * @param text The button text label content.
 * @param leadingIcon The button leading icon content. Pass `null` here for no leading icon.
 */
@Composable
fun MifosTextButton(
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    MifosTextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        MifosButtonContent(
            text = text,
            leadingIcon = leadingIcon,
        )
    }
}

/**
 * Internal Mifos button content layout for arranging the text label and leading icon.
 *
 * @param text The button text label content.
 * @param leadingIcon The button leading icon content. Default is `null` for no leading icon.Ï
 */
@Composable
private fun MifosButtonContent(
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    Row(modifier) {
        if (leadingIcon != null) {
            Box(Modifier.sizeIn(maxHeight = ButtonDefaults.IconSize)) {
                leadingIcon()
            }
        }
        Box(
            Modifier
                .padding(
                    start = if (leadingIcon != null) {
                        ButtonDefaults.IconSpacing
                    } else {
                        0.dp
                    },
                ),
        ) {
            text()
        }
    }
}

/**
 * Mifos button default values.
 */
@Suppress("ForbiddenComment")
object MifosButtonDefaults {
    // TODO: File bug
    // OutlinedButton border color doesn't respect disabled state by default
    const val DISABLED_OUTLINED_BUTTON_BORDER_ALPHA = 0.12f

    // TODO: File bug
    // OutlinedButton default border width isn't exposed via ButtonDefaults
    val OutlinedButtonBorderWidth = 1.dp
}
