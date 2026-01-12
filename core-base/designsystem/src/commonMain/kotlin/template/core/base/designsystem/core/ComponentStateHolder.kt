/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.designsystem.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * A concrete implementation of [ComponentState] that holds and manages component state.
 *
 * This class provides a thread-safe way to hold and update state values within components,
 * with automatic recomposition when the state changes. The state is observable by Compose
 * and will trigger recomposition of any composables that read the [value].
 *
 * The state holder is marked as [Stable], meaning Compose can make assumptions about
 * when it changes and optimize recomposition accordingly.
 *
 * Example usage:
 * ```
 * class MyComponentState(initialExpanded: Boolean) {
 *     private val _expanded = ComponentStateHolder(initialExpanded)
 *     val expanded: ComponentState<Boolean> = _expanded
 *
 *     fun toggleExpanded() {
 *         _expanded.update(!_expanded.value)
 *     }
 * }
 * ```
 *
 * @param T The type of value this state holder manages
 * @param initialValue The initial value for this state
 *
 * @see ComponentState
 * @see rememberComponentState
 */
@Stable
class ComponentStateHolder<T>(initialValue: T) : ComponentState<T> {
    /**
     * The current value of the state. Reading this property in a composable
     * will cause that composable to recompose when the value changes.
     *
     * The setter is private to ensure state changes go through [update],
     * which provides a clear API for state mutations.
     */
    override var value by mutableStateOf(initialValue)
        private set

    /**
     * Updates the state with a new value.
     *
     * This will trigger recomposition of any composables that read [value].
     * The update is performed immediately and synchronously.
     *
     * @param newValue The new value to set
     */
    override fun update(newValue: T) {
        value = newValue
    }
}

/**
 * Remembers a [ComponentState] instance across recompositions.
 *
 * This composable function creates a [ComponentStateHolder] that survives recomposition,
 * ensuring state is preserved when the composable is recomposed but not when the
 * composable is completely removed from the composition.
 *
 * The state will be recreated if the composition is completely rebuilt or if
 * the key used in the remember call changes.
 *
 * Example usage:
 * ```
 * @Composable
 * fun ExpandableCard() {
 *     val expandedState = rememberComponentState(initialValue = false)
 *
 *     Card(
 *         modifier = Modifier.clickable {
 *             expandedState.update(!expandedState.value)
 *         }
 *     ) {
 *         if (expandedState.value) {
 *             DetailContent()
 *         } else {
 *             SummaryContent()
 *         }
 *     }
 * }
 * ```
 *
 * @param T The type of value the state will hold
 * @param initialValue The initial value for the state
 * @return A [ComponentState] instance that persists across recompositions
 *
 * @see ComponentState
 * @see ComponentStateHolder
 * @see androidx.compose.runtime.remember
 */
@Composable
fun <T> rememberComponentState(initialValue: T): ComponentState<T> {
    return remember { ComponentStateHolder(initialValue) }
}
