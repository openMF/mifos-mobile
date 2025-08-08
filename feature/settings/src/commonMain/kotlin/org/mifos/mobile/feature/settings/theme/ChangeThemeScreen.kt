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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_theme
import mifos_mobile.feature.settings.generated.resources.feature_settings_apply_theme
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_dark
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_light
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_system
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosRadioButton
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.DarkThemeConfig
import org.mifos.mobile.core.ui.component.MifosIconButton
import org.mifos.mobile.core.ui.utils.DevicePreview
import org.mifos.mobile.core.ui.utils.EventsEffect

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
        ThemeScreenContent(
            currentTheme = uiState.currentTheme,
            setTheme = onAction,
        )
    }
}

@Composable
internal fun ThemeScreenContent(
    currentTheme: DarkThemeConfig,
    setTheme: (ThemeAction.SetTheme) -> Unit,
) {
    var selectedTheme by rememberSaveable { mutableStateOf(currentTheme) }
    val appliedTheme = when (setTheme) {
        ThemeAction.SetTheme(DarkThemeConfig.FOLLOW_SYSTEM) -> DarkThemeConfig.FOLLOW_SYSTEM
        ThemeAction.SetTheme(DarkThemeConfig.DARK) -> DarkThemeConfig.DARK
        ThemeAction.SetTheme(DarkThemeConfig.LIGHT) -> DarkThemeConfig.LIGHT
        else -> DarkThemeConfig.LIGHT
    }
    Column(
        modifier = Modifier.fillMaxWidth().statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        MifosRadioButton(
            label = stringResource(Res.string.feature_settings_theme_system),
            modifier = Modifier.fillMaxWidth().padding(horizontal = DesignToken.padding.extraLarge),
            selected = selectedTheme == DarkThemeConfig.FOLLOW_SYSTEM,
            onClick = {
                selectedTheme = DarkThemeConfig.FOLLOW_SYSTEM
            },
            selectedTextStyle = MifosTypography.titleSmallEmphasized.copy(
                color = AppColors.primaryBlue,
            ),
            unselectedTextStyle = when (appliedTheme) {
                DarkThemeConfig.DARK -> MifosTypography.titleSmallEmphasized.copy(
                    color = AppColors.customWhite,
                )
                else -> MifosTypography.titleSmallEmphasized.copy(
                    color = AppColors.customBlack,
                )
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        MifosRadioButton(
            label = stringResource(Res.string.feature_settings_theme_dark),
            modifier = Modifier.fillMaxWidth().padding(horizontal = DesignToken.padding.extraLarge),
            selected = selectedTheme == DarkThemeConfig.DARK,
            onClick = {
                selectedTheme = DarkThemeConfig.DARK
            },
            selectedTextStyle = MifosTypography.titleSmallEmphasized.copy(
                color = AppColors.primaryBlue,
            ),
            unselectedTextStyle = when (appliedTheme) {
                DarkThemeConfig.DARK -> MifosTypography.titleSmallEmphasized.copy(
                    color = AppColors.customWhite,
                )
                else -> MifosTypography.titleSmallEmphasized.copy(
                    color = AppColors.customBlack,
                )
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        MifosRadioButton(
            label = stringResource(Res.string.feature_settings_theme_light),
            modifier = Modifier.fillMaxWidth().padding(horizontal = DesignToken.padding.extraLarge),
            selected = selectedTheme == DarkThemeConfig.LIGHT,
            onClick = {
                selectedTheme = DarkThemeConfig.LIGHT
            },
            selectedTextStyle = MifosTypography.titleSmallEmphasized.copy(
                color = AppColors.primaryBlue,
            ),
            unselectedTextStyle = when (appliedTheme) {
                DarkThemeConfig.DARK -> MifosTypography.titleSmallEmphasized.copy(
                    color = AppColors.customWhite,
                )
                else -> MifosTypography.titleSmallEmphasized.copy(
                    color = AppColors.customBlack,
                )
            },
        )
        Spacer(modifier = Modifier.height(24.dp))
        MifosIconButton(
            label = stringResource(Res.string.feature_settings_apply_theme),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = MifosIcons.ArrowRight,
            onClick = {
                setTheme(ThemeAction.SetTheme(selectedTheme))
            },
        )
    }
}

@DevicePreview
@Composable
fun ThemeScreenPreview() {
    MifosMobileTheme {
        ThemeScreenContent(
            currentTheme = DarkThemeConfig.LIGHT,
            setTheme = {},
        )
    }
}
