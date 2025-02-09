/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.designsystem.utils

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.LayoutDirection

@Stable
fun Modifier.mirrorIfRtl(): Modifier =
    composed(
        inspectorInfo = debugInspectorInfo {
            name = "mirrorIfRtl"
        },
    ) {
        if (LocalLayoutDirection.current == LayoutDirection.Rtl) {
            scale(scaleX = -1f, scaleY = 1f)
        } else {
            this
        }
    }

@Stable
fun Modifier.tabNavigation(): Modifier =
    composed(
        inspectorInfo = debugInspectorInfo {
            name = "tabNavigation"
        },
    ) {
        val focusManager = LocalFocusManager.current
        onPreviewKeyEvent { keyEvent ->
            if (keyEvent.key == Key.Tab && keyEvent.type == KeyEventType.KeyDown) {
                focusManager.moveFocus(
                    if (keyEvent.isShiftPressed) {
                        FocusDirection.Previous
                    } else {
                        FocusDirection.Next
                    },
                )
                true
            } else {
                false
            }
        }
    }

fun Modifier.onClick(
    indication: Indication? = null,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "onClickModifier"
        value = enabled
    },
) {
    val interactionSource = remember { MutableInteractionSource() }
    clickable(
        indication = indication,
        interactionSource = interactionSource,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
    ) {
        onClick.invoke()
    }
}
