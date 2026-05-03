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

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneExpansionState
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldPaneScope
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldScope
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import kotlinx.coroutines.launch

/**
 * A layout scaffold for adaptive list-detail navigation using Material 3's [ListDetailPaneScaffold].
 *
 * This composable allows you to build responsive UIs with two primary panes: a main (list) pane and
 * a detail pane, with an optional third pane. It handles the internal navigation between panes using
 * a [ThreePaneScaffoldNavigator], which allows toggling between list-only, detail-only, or side-by-side layouts
 * depending on screen size and device posture.
 *
 * The provided [mainPaneContent] and [detailPaneContent] are composable lambdas that define the UI for each pane.
 * You can invoke the passed lambda (`navigateToDetail` or `navigateBack`) to trigger navigation.
 *
 * @param mainPaneContent The main list pane content. Use the provided `navigateToDetail: () -> Unit` callback
 * to programmatically transition to the detail pane.
 *
 * @param detailPaneContent The detail pane content. Use the provided `navigateBack: () -> Unit` callback
 * to programmatically navigate back to the list pane (if supported by screen layout).
 *
 * ## Example:
 * ```
 * AdaptiveListDetailPaneScaffold(
 *     mainPaneContent = { navigateToDetail ->
 *         LazyColumn {
 *             items(itemsList) { item ->
 *                 ListItem(
 *                     headlineText = { Text(item.title) },
 *                     modifier = Modifier.clickable { navigateToDetail() }
 *                 )
 *             }
 *         }
 *     },
 *     detailPaneContent = { navigateBack ->
 *         Column {
 *             Text("Detail view")
 *             Button(onClick = navigateBack) { Text("Back") }
 *         }
 *     }
 * )
 * ```
 *
 * @param modifier Modifier applied to the root [ListDetailPaneScaffold].
 * @param navigator The [ThreePaneScaffoldNavigator] to control pane transitions.
 * Defaults to a [rememberListDetailPaneScaffoldNavigator] instance.
 * @param extraPaneContent Optional content for a third pane, shown when screen size allows.
 * @param paneExpansionDragHandle Optional drag handle composable for resizing panes interactively.
 * @param paneExpansionState Optional override for the scaffold's expansion state.
 * @param testTag Optional testTag for the root ListDetailPaneScaffold.
 *
 * @see androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
 * @see androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
 */

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AdaptiveListDetailPaneScaffold(
    mainPaneContent: @Composable ThreePaneScaffoldPaneScope.(navigateToDetail: () -> Unit) -> Unit,
    detailPaneContent: @Composable ThreePaneScaffoldPaneScope.(navigateBack: () -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    navigator: ThreePaneScaffoldNavigator<Any> = rememberListDetailPaneScaffoldNavigator(),
    extraPaneContent: @Composable ThreePaneScaffoldPaneScope.() -> Unit = {},
    paneExpansionDragHandle: @Composable (ThreePaneScaffoldScope.(PaneExpansionState) -> Unit)? = null,
    paneExpansionState: PaneExpansionState? = null,
    testTag: String? = null,
) {
    val scope = rememberCoroutineScope()

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            mainPaneContent {
                scope.launch {
                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                }
            }
        },
        detailPane = {
            detailPaneContent {
                scope.launch {
                    if (navigator.canNavigateBack()) {
                        navigator.navigateBack()
                    }
                }
            }
        },
        extraPane = extraPaneContent,
        modifier = modifier.then(Modifier.testTag(testTag ?: "KptAdaptiveListDetailPaneScaffold")),
        paneExpansionDragHandle = paneExpansionDragHandle,
        paneExpansionState = paneExpansionState,
    )
}
