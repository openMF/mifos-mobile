/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.designsystem.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun MifosScaffold(
    @StringRes
    topBarTitleResId: Int,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    floatingActionButtonContent: FloatingActionButtonContent? = null,
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    var padding by remember { mutableStateOf(PaddingValues()) }
    Scaffold(
        topBar = {
            MifosTopBarTitle(
                topBarTitleResId = topBarTitleResId,
                navigateBack = navigateBack,
            )
        },
        floatingActionButton = {
            floatingActionButtonContent?.let { content ->
                FloatingActionButton(
                    modifier = Modifier.padding(
                        end = padding.calculateEndPadding(LayoutDirection.Ltr),
                    ),
                    onClick = content.onClick,
                    contentColor = content.contentColor,
                    containerColor = MaterialTheme.colorScheme.primary,
                    content = content.content,
                )
            }
        },
        snackbarHost = snackbarHost,
        modifier = modifier,
        content = {
            padding = it
            content(it)
        },
    )
}

@Composable
fun MifosScaffold(
    topBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    floatingActionButtonContent: FloatingActionButtonContent? = null,
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    var padding by remember { mutableStateOf(PaddingValues()) }
    Scaffold(
        topBar = topBar,
        floatingActionButton = {
            floatingActionButtonContent?.let { content ->
                FloatingActionButton(
                    modifier = Modifier.padding(
                        end = padding.calculateEndPadding(LayoutDirection.Ltr),
                    ),
                    onClick = content.onClick,
                    contentColor = content.contentColor,
                    containerColor = MaterialTheme.colorScheme.primary,
                    content = content.content,
                )
            }
        },
        snackbarHost = snackbarHost,
        modifier = modifier,
        content = {
            padding = it
            content(it)
        },
    )
}

data class FloatingActionButtonContent(
    val onClick: (() -> Unit),
    val contentColor: Color,
    val content: (@Composable () -> Unit),
)
