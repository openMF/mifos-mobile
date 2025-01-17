/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.designsystem.component.scrollbar

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import kotlin.math.roundToInt

/**
 * Remembers a function to react to [Scrollbar] thumb position displacements for a [LazyListState]
 * @param itemsAvailable the amount of items in the list.
 */
@Composable
fun LazyListState.rememberDraggableScroller(
    itemsAvailable: Int,
): (Float) -> Unit = rememberDraggableScroller(
    itemsAvailable = itemsAvailable,
    scroll = ::scrollToItem,
)

/**
 * Remembers a function to react to [Scrollbar] thumb position displacements for a [LazyGridState]
 * @param itemsAvailable the amount of items in the grid.
 */
@Composable
fun LazyGridState.rememberDraggableScroller(
    itemsAvailable: Int,
): (Float) -> Unit = rememberDraggableScroller(
    itemsAvailable = itemsAvailable,
    scroll = ::scrollToItem,
)

/**
 * Remembers a function to react to [Scrollbar] thumb position displacements for a
 * [LazyStaggeredGridState]
 * @param itemsAvailable the amount of items in the staggered grid.
 */
@Composable
fun LazyStaggeredGridState.rememberDraggableScroller(
    itemsAvailable: Int,
): (Float) -> Unit = rememberDraggableScroller(
    itemsAvailable = itemsAvailable,
    scroll = ::scrollToItem,
)

/**
 * Generic function to react to [Scrollbar] thumb displacements in a lazy layout.
 * @param itemsAvailable the total amount of items available to scroll in the layout.
 * @param scroll a function to be invoked when an index has been identified to scroll to.
 */
@Composable
private inline fun rememberDraggableScroller(
    itemsAvailable: Int,
    crossinline scroll: suspend (index: Int) -> Unit,
): (Float) -> Unit {
    var percentage by remember { mutableFloatStateOf(Float.NaN) }
    val itemCount by rememberUpdatedState(itemsAvailable)

    LaunchedEffect(percentage) {
        if (percentage.isNaN()) return@LaunchedEffect
        val indexToFind = (itemCount * percentage).roundToInt()
        scroll(indexToFind)
    }
    return remember {
        { newPercentage -> percentage = newPercentage }
    }
}
