/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.library.passcode.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mifos.library.passcode.R
import org.mifos.library.passcode.theme.PasscodeKeyButtonStyle
import org.mifos.library.passcode.theme.blueTint

@Composable
internal fun PasscodeKeys(
    enterKey: (String) -> Unit,
    deleteKey: () -> Unit,
    deleteAllKeys: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val onEnterKeyClick = { keyTitle: String ->
        enterKey(keyTitle)
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            PasscodeKey(
                modifier = Modifier.weight(weight = 1.0F),
                keyTitle = "1",
                onClick = onEnterKeyClick,
            )
            PasscodeKey(
                modifier = Modifier.weight(weight = 1.0F),
                keyTitle = "2",
                onClick = onEnterKeyClick,
            )
            PasscodeKey(
                modifier = Modifier.weight(weight = 1.0F),
                keyTitle = "3",
                onClick = onEnterKeyClick,
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            PasscodeKey(
                modifier = Modifier.weight(weight = 1.0F),
                keyTitle = "4",
                onClick = onEnterKeyClick,
            )
            PasscodeKey(
                modifier = Modifier.weight(weight = 1.0F),
                keyTitle = "5",
                onClick = onEnterKeyClick,
            )
            PasscodeKey(
                modifier = Modifier.weight(weight = 1.0F),
                keyTitle = "6",
                onClick = onEnterKeyClick,
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            PasscodeKey(
                modifier = Modifier.weight(weight = 1.0F),
                keyTitle = "7",
                onClick = onEnterKeyClick,
            )
            PasscodeKey(
                modifier = Modifier.weight(weight = 1.0F),
                keyTitle = "8",
                onClick = onEnterKeyClick,
            )
            PasscodeKey(
                modifier = Modifier.weight(weight = 1.0F),
                keyTitle = "9",
                onClick = onEnterKeyClick,
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            PasscodeKey(modifier = Modifier.weight(weight = 1.0F))
            PasscodeKey(
                modifier = Modifier.weight(weight = 1.0F),
                keyTitle = "0",
                onClick = onEnterKeyClick,
            )
            PasscodeKey(
                modifier = Modifier.weight(weight = 1.0F),
                keyIcon = ImageVector.vectorResource(id = R.drawable.lib_mifos_passcode_ic_delete),
                keyIconContentDescription = "Delete Passcode Key Button",
                onClick = {
                    deleteKey()
                },
                onLongClick = {
                    deleteAllKeys()
                },
            )
        }
    }
}

@Composable
internal fun PasscodeKey(
    modifier: Modifier = Modifier,
    keyTitle: String = "",
    keyIcon: ImageVector? = null,
    keyIconContentDescription: String = "",
    onClick: ((String) -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
    ) {
        CombinedClickableIconButton(
            onClick = {
                onClick?.invoke(keyTitle)
            },
            onLongClick = {
                onLongClick?.invoke()
            },
            modifier = Modifier
                .padding(all = 4.dp),
        ) {
            if (keyIcon == null) {
                Text(
                    text = keyTitle,
                    style = PasscodeKeyButtonStyle.copy(color = blueTint),
                )
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = keyIconContentDescription,
                    tint = blueTint,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CombinedClickableIconButton(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .size(size = size)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
                enabled = enabled,
                role = Role.Button,
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val contentAlpha =
            if (enabled) LocalContentColor.current else LocalContentColor.current.copy(alpha = 0f)
        CompositionLocalProvider(LocalContentColor provides contentAlpha, content = content)
    }
}

@Preview
@Composable
private fun PasscodeKeysPreview() {
    PasscodeKeys({}, {}, {})
}
