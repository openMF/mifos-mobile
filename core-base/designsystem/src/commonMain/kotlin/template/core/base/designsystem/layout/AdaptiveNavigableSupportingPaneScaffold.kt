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
import androidx.compose.material3.adaptive.layout.PaneExpansionState
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldPaneScope
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldScope
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.platform.testTag
import kotlinx.coroutines.launch

/**
 * A composable layout for adaptive UIs that implements a navigable two-pane structure
 * using Material 3's [SupportingPaneScaffold]. It handles navigation between the main and supporting panes,
 * optionally including a third extra pane for additional content.
 *
 * This layout automatically adapts to screen size and orientation, making it suitable for
 * responsive UIs across phones, tablets, and foldables. It also handles back navigation
 * internally via [BackHandler] integration.
 *
 * @param mainPaneContent The main pane content, typically representing the primary screen or list.
 * This lambda receives a navigation callback that should be invoked to trigger the transition to the supporting pane.
 *
 * @param supportingPaneContent The content of the supporting pane, shown after navigation.
 * This lambda receives a back navigation callback for returning to the main pane.
 *
 * @param modifier The [Modifier] applied to the scaffold layout. *(Default: [Modifier])*
 *
 * @param scaffoldNavigator Optional external [ThreePaneScaffoldNavigator] to control pane navigation.
 * If not provided, an internal one is created using [rememberSupportingPaneScaffoldNavigator].
 *
 * @param extraPaneContent Optional content for the third pane (e.g., info panel or context pane). *(Default: empty)*
 *
 * @param paneExpansionDragHandle Optional composable used for displaying a draggable divider
 * between panes for manual expansion. *(Default: null)*
 *
 * @param paneExpansionState Optional [PaneExpansionState] to control and observe pane expansion behavior.
 * If not provided, a state will be remembered internally based on scaffold layout state.
 *
 * @param testTag Optional testTag for the root SupportingPaneScaffold. If not provided, a default tag is used.
 */

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)
@Composable
fun AdaptiveNavigableSupportingPaneScaffold(
    mainPaneContent: @Composable ThreePaneScaffoldPaneScope.(navigateToSupporting: () -> Unit) -> Unit,
    supportingPaneContent: @Composable ThreePaneScaffoldPaneScope.(navigateBack: () -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    scaffoldNavigator: ThreePaneScaffoldNavigator<Any> = rememberSupportingPaneScaffoldNavigator(),
    extraPaneContent: @Composable ThreePaneScaffoldPaneScope.() -> Unit = {},
    paneExpansionDragHandle: @Composable (ThreePaneScaffoldScope.(PaneExpansionState) -> Unit)? = null,
    paneExpansionState: PaneExpansionState? = null,
    testTag: String? = null,
) {
    val scope = rememberCoroutineScope()

    BackHandler(enabled = scaffoldNavigator.canNavigateBack()) {
        scope.launch {
            scaffoldNavigator.navigateBack()
        }
    }

    SupportingPaneScaffold(
        directive = scaffoldNavigator.scaffoldDirective,
        value = scaffoldNavigator.scaffoldValue,
        mainPane = {
            mainPaneContent {
                scope.launch {
                    scaffoldNavigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                }
            }
        },
        supportingPane = {
            supportingPaneContent {
                scope.launch {
                    if (scaffoldNavigator.canNavigateBack()) {
                        scaffoldNavigator.navigateBack()
                    }
                }
            }
        },
        extraPane = extraPaneContent,
        modifier = modifier.then(Modifier.testTag(testTag ?: "KptAdaptiveNavigableSupportingPaneScaffold")),
        paneExpansionDragHandle = paneExpansionDragHandle,
        paneExpansionState = paneExpansionState,
    )
}
