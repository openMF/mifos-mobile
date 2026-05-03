/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.designsystem.layout

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.PaneExpansionState
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldPaneScope
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldScope
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * A generic, adaptive list-detail layout scaffold for responsive UIs in CMP applications.
 *
 * This scaffold supports navigation between a list and a detail view in an adaptive layout,
 * adjusting its layout behavior based on screen size (e.g., displaying panes side-by-side or stacked).
 *
 * The list and detail panes accept composables that support animated shared transitions and visibility scopes.
 * Interaction and pane transitions (e.g., selecting an item, going back) are handled internally,
 * enabling consumers to focus only on content composition.
 *
 * ## Example usage:
 * ```kotlin
 * AdaptiveNavigableListDetailPaneScaffold(
 *     items = myItems,
 *     listPaneItem = { item, isListAndDetailVisible, isListVisible, sharedTransitionScope, visibilityScope ->
 *         Text(text = item.title)
 *     },
 *     detailPaneContent = { item, isListAndDetailVisible, isDetailVisible, sharedTransitionScope, visibilityScope ->
 *         Text(text = item.details)
 *     }
 * )
 * ```
 *
 * @param items The list of items to render in the list pane.
 * @param listPaneItem The composable content for each list item inside a card.
 * Selection and navigation logic are handled internally.
 * @param detailPaneContent The composable content for the selected item in the detail pane.
 * @param modifier Modifier applied to the root of the scaffold layout. *(Optional)*
 * @param extraPaneContent Optional content for a third pane (e.g., settings, metadata). *(Optional)*
 * @param paneExpansionDragHandle Optional UI element for resizing panes interactively. *(Optional)*
 * @param paneExpansionState Optional state controller for pane expansion. Defaults to internal handling. *(Optional)*
 * @param cardShape Optional shape to override the default card shape for list items. *(Optional)*
 * @param cardElevation Optional elevation to override the default card elevation for list items. *(Optional)*
 * @param cardColors Optional colors to override default card colors for list items. *(Optional)*
 * @param cardBorder Optional border to override default list item card border behavior. *(Optional)*
 * @param testTag Optional testTag for the root of the scaffold layout.
 *
 * @see ListDetailPaneScaffold for platform-level behavior and layout management.
 * @see SelectionVisibilityState for selection handling behavior.
 */

@OptIn(
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalComposeUiApi::class,
    ExperimentalSharedTransitionApi::class,
)
@Composable
fun <T : PaneScaffoldItem<*>> AdaptiveNavigableListDetailPaneScaffold(
    items: List<T>,
    listPaneItem: @Composable (
        // The item to display in the list pane
        item: T,
        isListAndDetailVisible: Boolean,
        isListVisible: Boolean,
        sharedTransitionScope: SharedTransitionScope,
        animatedVisibilityScope: AnimatedVisibilityScope,
    ) -> Unit,
    detailPaneContent: @Composable (
        // The selected item to display in the detail pane
        item: T,
        isListAndDetailVisible: Boolean,
        isDetailVisible: Boolean,
        sharedTransitionScope: SharedTransitionScope,
        animatedVisibilityScope: AnimatedVisibilityScope,
    ) -> Unit,
    modifier: Modifier = Modifier,
    extraPaneContent: @Composable (ThreePaneScaffoldPaneScope.() -> Unit)? = null,
    paneExpansionDragHandle: @Composable (ThreePaneScaffoldScope.(PaneExpansionState) -> Unit)? = null,
    paneExpansionState: PaneExpansionState? = null,
    cardShape: Shape? = null,
    cardElevation: CardElevation? = null,
    cardColors: CardColors? = null,
    cardBorder: BorderStroke? = null,
    testTag: String? = null,
) {
    var selectedItemIndex: Int? by rememberSaveable { mutableStateOf(null) }
    val navigator = rememberListDetailPaneScaffoldNavigator()
    val scope = rememberCoroutineScope()
    val isListAndDetailVisible =
        navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded &&
            navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded

    BackHandler(enabled = navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

    SharedTransitionLayout {
        AnimatedContent(targetState = isListAndDetailVisible, label = "AdaptiveListDetailLayout") {
            ListDetailPaneScaffold(
                directive = navigator.scaffoldDirective,
                value = navigator.scaffoldValue,
                listPane = {
                    val currentSelectedItemIndex = selectedItemIndex
                    val isDetailVisible =
                        navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded
                    AnimatedPane {
                        ListContent(
                            items = items,
                            selectionState = if (isDetailVisible && currentSelectedItemIndex != null) {
                                SelectionVisibilityState.ShowSelection(currentSelectedItemIndex)
                            } else {
                                SelectionVisibilityState.NoSelection
                            },
                            onIndexClick = { index ->
                                selectedItemIndex = index
                                scope.launch {
                                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                                }
                            },
                            isListAndDetailVisible = isListAndDetailVisible,
                            isListVisible = !isDetailVisible,
                            animatedVisibilityScope = this@AnimatedPane,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            listPaneItem = listPaneItem,
                            cardShape = cardShape,
                            cardElevation = cardElevation,
                            cardColors = cardColors,
                            cardBorder = cardBorder,
                            testTag = testTag,
                        )
                    }
                },
                detailPane = {
                    val selectedItem = selectedItemIndex?.let(items::get) ?: items[0]
                    val isDetailVisible =
                        navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded
                    AnimatedPane {
                        detailPaneContent(
                            selectedItem,
                            isListAndDetailVisible,
                            isDetailVisible,
                            this@SharedTransitionLayout,
                            this@AnimatedPane,
                        )
                    }
                },
                extraPane = extraPaneContent,
                modifier = modifier.then(Modifier.testTag(testTag ?: "KptAdaptiveListDetailScaffold")),
                paneExpansionDragHandle = paneExpansionDragHandle,
                paneExpansionState = paneExpansionState,
            )
        }
    }
}

/**
 * A reusable list pane layout for adaptive list-detail scaffolds with support for selection and transitions.
 *
 * This composable is designed to display a vertically scrolling list of items with built-in support
 * for selection states and animated transitions. Each item is displayed within a [Card], and the content
 * of the card is provided by the [listPaneItem] composable lambda.
 *
 * Handles interaction logic (clickable/selectable behavior), transition animation scopes,
 * and visual differentiation for selected items.
 *
 * @param items The list of items to render in the list pane.
 * @param selectionState Controls whether an item is selected and how it should be visually represented.
 * @param onIndexClick Callback invoked when an item is clicked. Passes the selected index.
 * @param isListAndDetailVisible True if both list and detail panes are shown side-by-side.
 * @param isListVisible True if the list pane is currently visible (not hidden in compact layouts).
 * @param sharedTransitionScope Scope for shared element transitions between list and detail.
 * @param animatedVisibilityScope Scope for managing animated enter/exit transitions.
 * @param modifier Modifier applied to the outer [LazyColumn].
 * @param listPaneItem Composable content lambda for each item, receiving animation and layout context.
 * @param cardShape Optional shape override for the item card.
 * @param cardElevation Optional elevation override for the item card.
 * @param cardColors Optional colors override for the item card.
 * @param cardBorder Optional border override for the item card.
 * @param testTag Optional testTag for the root of the list content.
 *
 * @see SelectionVisibilityState for controlling selection behavior.
 */

@Suppress("CyclomaticComplexMethod")
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun <T : PaneScaffoldItem<*>> ListContent(
    items: List<T>,
    selectionState: SelectionVisibilityState,
    onIndexClick: (index: Int) -> Unit,
    isListAndDetailVisible: Boolean,
    isListVisible: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    listPaneItem: @Composable (
        T,
        Boolean,
        Boolean,
        SharedTransitionScope,
        AnimatedVisibilityScope,
    ) -> Unit,
    modifier: Modifier = Modifier,
    cardShape: Shape? = null,
    cardElevation: CardElevation? = null,
    cardColors: CardColors? = null,
    cardBorder: BorderStroke? = null,
    testTag: String? = null,
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.then(Modifier.testTag(testTag ?: "KptAdaptiveListDetailList")),
    ) {
        itemsIndexed(
            items = items,
            key = { _, item -> item.id!! },
        ) { index, item ->

            val interactionModifier = when (selectionState) {
                SelectionVisibilityState.NoSelection -> {
                    Modifier.clickable(
                        onClick = { onIndexClick(index) },
                    )
                }

                is SelectionVisibilityState.ShowSelection -> {
                    Modifier.selectable(
                        selected = index == selectionState.selectedItemIndex,
                        onClick = { onIndexClick(index) },
                    )
                }
            }

            val containerColor = when (selectionState) {
                SelectionVisibilityState.NoSelection -> MaterialTheme.colorScheme.surface
                is SelectionVisibilityState.ShowSelection ->
                    if (index == selectionState.selectedItemIndex) {
                        MaterialTheme.colorScheme.surfaceVariant
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
            }

            val borderStroke = when (selectionState) {
                SelectionVisibilityState.NoSelection -> cardBorder ?: BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline,
                )

                is SelectionVisibilityState.ShowSelection ->
                    if (index == selectionState.selectedItemIndex) {
                        null
                    } else {
                        cardBorder ?: BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                        )
                    }
            }

            Card(
                colors = cardColors?.copy(containerColor = containerColor)
                    ?: CardDefaults.cardColors(containerColor = containerColor),
                border = borderStroke,
                elevation = cardElevation ?: CardDefaults.cardElevation(),
                shape = cardShape ?: CardDefaults.shape,
                modifier = Modifier
                    .then(interactionModifier)
                    .fillMaxWidth()
                    .testTag("KptAdaptiveListDetailItem_$index"),
            ) {
                listPaneItem(
                    item,
                    isListAndDetailVisible,
                    isListVisible,
                    sharedTransitionScope,
                    animatedVisibilityScope,
                )
            }
        }
    }
}

/**
 * Describes the current selection state for the list pane within an adaptive layout.
 *
 * Used to determine how list items should behave (clickable vs. selectable) and how they are styled.
 */
sealed interface SelectionVisibilityState {

    /**
     * No selection should be shown, and each item should be clickable.
     */
    data object NoSelection : SelectionVisibilityState

    /**
     * Selection state should be shown, and each item should be selectable.
     */
    data class ShowSelection(
        /**
         * The index of the word that is selected.
         */
        val selectedItemIndex: Int,
    ) : SelectionVisibilityState
}

interface PaneScaffoldItem<T> {
    val id: T
}
