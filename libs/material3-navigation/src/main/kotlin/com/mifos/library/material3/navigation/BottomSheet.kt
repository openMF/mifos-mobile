/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package com.mifos.library.material3.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

/**
 * Helper function to create a [ModalBottomSheetLayout] from a [BottomSheetNavigator].
 *
 * @see [ModalBottomSheetLayout]
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun ModalBottomSheetLayout(
    bottomSheetNavigator: BottomSheetNavigator,
    modifier: Modifier = Modifier,
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = BottomSheetDefaults.Elevation,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        bottomSheetNavigator.sheetInitializer()
        content()
    }

    if (bottomSheetNavigator.sheetEnabled) {
        ModalBottomSheet(
            onDismissRequest = bottomSheetNavigator.onDismissRequest,
            sheetState = bottomSheetNavigator.sheetState,
            content = bottomSheetNavigator.sheetContent,
            sheetMaxWidth = sheetMaxWidth,
            shape = shape,
            containerColor = containerColor,
            contentColor = contentColor,
            tonalElevation = tonalElevation,
            scrimColor = scrimColor,
            dragHandle = dragHandle,
        )
    }
}
