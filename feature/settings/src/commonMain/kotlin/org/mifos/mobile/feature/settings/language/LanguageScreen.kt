/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.language

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.feature_settings_change_language
import mifos_mobile.feature.settings.generated.resources.feature_settings_language
import mifos_mobile.feature.settings.generated.resources.language
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosRadioButton
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.LanguageConfig
import org.mifos.mobile.core.ui.utils.EventsEffect

@Composable
internal fun LanguageScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LanguageViewModel = koinViewModel(),
) {
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    EventsEffect(viewModel.eventFlow) { event ->
        when (event) {
            LanguageEvent.NavigateBack -> navigateBack.invoke()
        }
    }

    LanguageScreenContent(
        uiState = uiState,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
internal fun LanguageScreenContent(
    uiState: LanguageState,
    modifier: Modifier = Modifier,
    onAction: (LanguageAction) -> Unit,
) {
    MifosElevatedScaffold(
        onNavigateBack = { onAction(LanguageAction.OnNavigateBack) },
        topBarTitle = stringResource(Res.string.feature_settings_language),
        bottomBar = {
            MifosButton(
                modifier = modifier
                    .fillMaxWidth()
                    .height(DesignToken.sizes.buttonHeight)
                    .padding(horizontal = DesignToken.padding.large),
                shape = DesignToken.shapes.medium,
                onClick = { onAction(LanguageAction.SetLanguage(uiState.selectedLanguage)) },
            ) {
                Text(
                    text = stringResource(Res.string.feature_settings_change_language),
                    style = MifosTypography.titleMedium,
                )
            }
        },
    ) {
        LanguageSelectionContent(
            selectedLanguage = uiState.selectedLanguage,
            onSetLanguage = { onAction(LanguageAction.LanguageSelected(it)) },
        )
    }
}

@Composable
internal fun LanguageSelectionContent(
    selectedLanguage: LanguageConfig,
    modifier: Modifier = Modifier,
    onSetLanguage: (LanguageConfig) -> Unit,
) {
    LazyColumn(
        modifier = modifier.padding(DesignToken.padding.large),
        verticalArrangement = Arrangement.spacedBy(DesignToken.spacing.medium),
    ) {
        items(LanguageConfig.entries) {
            MifosRadioButton(
                modifier = Modifier.fillMaxWidth(),
                label = it.languageName,
                selected = selectedLanguage == it,
                onClick = {
                    onSetLanguage(it)
                },
            )
        }
    }
}

@Preview
@Composable
private fun Language_Screen_Preview() {
    MifosMobileTheme {
        LanguageScreenContent(
            uiState = LanguageState(
                currentLanguage = LanguageConfig.DEFAULT,
                selectedLanguage = LanguageConfig.DEFAULT,
            ),
            onAction = {},
        )
    }
}
