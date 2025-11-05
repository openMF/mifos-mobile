/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_theme
import mifos_mobile.feature.settings.generated.resources.feature_settings_apply_theme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosRadioButton
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.MifosThemeConfig
import org.mifos.mobile.core.ui.utils.DevicePreview
import org.mifos.mobile.core.ui.utils.EventsEffect

/**
 * A stateful composable that constructs the "Change Theme" screen.
 *
 * It connects to the [ChangeThemeViewModel] to observe the current UI state and
 * listen for one-time events, such as navigation. It delegates the rendering
 * of the UI to the stateless [ThemeScreenContent] composable.
 *
 * @param onNavigateBack A lambda function to be invoked when a back navigation event is triggered.
 * @param modifier The [Modifier] to be applied to this screen.
 * @param viewmodel The ViewModel responsible for the screen's logic and state management.
 */
@Composable
internal fun ChangeThemeScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewmodel: ChangeThemeViewModel = koinViewModel(),
) {
    val uiState by viewmodel.stateFlow.collectAsStateWithLifecycle()
    EventsEffect(viewmodel.eventFlow) { event ->
        when (event) {
            ThemeEvent.OnNavigateBack -> onNavigateBack.invoke()
        }
    }
    ThemeScreenContent(
        uiState = uiState,
        modifier = modifier,
        onAction = remember(viewmodel) {
            { viewmodel.trySendAction(it) }
        },
    )
}

/**
 * A stateless composable that renders the UI for the theme selection screen.
 *
 * It includes the scaffold with a top bar, a list of theme options presented as
 * radio buttons, and an "Apply" button to save the selection.
 *
 * @param uiState The current state of the theme screen, containing the available themes and the selection.
 * @param modifier The [Modifier] to be applied to the layout.
 * @param onAction A callback to send actions (like theme selection or navigation) to the ViewModel.
 */
@Composable
internal fun ThemeScreenContent(
    uiState: ThemeState,
    modifier: Modifier = Modifier,
    onAction: (ThemeAction) -> Unit,
) {
    MifosElevatedScaffold(
        modifier = modifier.fillMaxSize(),
        topBarTitle = stringResource(Res.string.feature_settings_action_theme),
        onNavigateBack = {
            onAction(ThemeAction.NavigateBack)
        },
    ) {
        Column(
            modifier = Modifier.padding(DesignToken.padding.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(DesignToken.padding.largeIncreased),
        ) {
            uiState.themeOptions.forEach { (theme, labelRes) ->
                MifosRadioButton(
                    label = stringResource(labelRes),
                    modifier = Modifier.fillMaxWidth(),
                    selected = uiState.currentTheme == theme,
                    onClick = { onAction(ThemeAction.ThemeSelection(theme)) },
                    selectedTextStyle = MifosTypography.titleSmallEmphasized.copy(
                        color = AppColors.primaryBlue,
                    ),
                    unselectedTextStyle = MifosTypography.titleSmallEmphasized.copy(
                        MaterialTheme.colorScheme.onSurface,
                    ),
                )
            }

            MifosButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DesignToken.sizes.buttonHeight),
                shape = DesignToken.shapes.medium,
                onClick = {
                    onAction(ThemeAction.SetTheme)
                },
            ) {
                Text(
                    text = stringResource(Res.string.feature_settings_apply_theme),
                    style = MifosTypography.titleMedium,
                )
            }
        }
    }
}

/**
 * A Jetpack Compose preview for the [ThemeScreenContent].
 *
 * This provides a design-time visualization of the theme selection UI in Android Studio,
 * configured with a default state.
 */
@DevicePreview
@Composable
fun ThemeScreenPreview() {
    MifosMobileTheme {
        ThemeScreenContent(
            uiState = ThemeState(currentTheme = MifosThemeConfig.FOLLOW_SYSTEM),
            onAction = {},
        )
    }
}
