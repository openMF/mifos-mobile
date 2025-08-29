/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.onboarding.language

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.core.ui.generated.resources.ic_icon_logo_1
import mifos_mobile.feature.onboarding_language.generated.resources.Res
import mifos_mobile.feature.onboarding_language.generated.resources.feature_onboarding_choose_your_app_language
import mifos_mobile.feature.onboarding_language.generated.resources.feature_onboarding_chosen_language_can_be_changed_later_in_the_settings
import mifos_mobile.feature.onboarding_language.generated.resources.feature_onboarding_submit
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosRadioButton
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.LanguageConfig
import org.mifos.mobile.core.ui.component.MifosPoweredCard

@Composable
internal fun OnboardingLanguageScreen(
    modifier: Modifier = Modifier,
    viewModel: SetOnboardingLanguageViewModel = koinViewModel(),
) {
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    OnboardingLanguageScreenContent(
        uiState = uiState,
        modifier = modifier,
        onAction = remember(viewModel) {
            { viewModel.trySendAction(it) }
        },
    )
}

@Composable
internal fun OnboardingLanguageScreenContent(
    uiState: OnboardingLanguageState,
    modifier: Modifier = Modifier,
    onAction: (OnboardingLanguageAction) -> Unit,
) {
    var selectedLanguage by rememberSaveable { mutableStateOf(uiState.currentLanguage) }
    MifosScaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
            ) {
                MifosButton(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(DesignToken.padding.large)
                        .height(DesignToken.sizes.buttonHeight),
                    shape = DesignToken.shapes.medium,
                    onClick = { onAction(OnboardingLanguageAction.SetLanguage(selectedLanguage)) },
                ) {
                    Text(
                        text = stringResource(Res.string.feature_onboarding_submit),
                        style = MifosTypography.titleMedium,
                    )
                }
                MifosPoweredCard()
            }
        },
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 75.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = DesignToken.padding.large),
            ) {
                Image(
                    modifier = Modifier.height(48.dp).width(165.dp),
                    painter = painterResource(
                        mifos_mobile.core.ui.generated.resources.Res.drawable.ic_icon_logo_1,
                    ),
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.height(DesignToken.spacing.extraExtraLarge))

                Text(
                    text = stringResource(Res.string.feature_onboarding_choose_your_app_language),
                    style = MifosTypography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(DesignToken.spacing.medium))

                Text(
                    text = stringResource(
                        Res.string.feature_onboarding_chosen_language_can_be_changed_later_in_the_settings,
                    ),
                    style = MifosTypography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )

                Spacer(modifier = Modifier.height(DesignToken.spacing.large))

                LanguageSelectionContent(
                    selectedLanguage = selectedLanguage,
                    onSetLanguage = { selectedLanguage = it },
                )
            }
        }
    }
}

@Composable
internal fun LanguageSelectionContent(
    selectedLanguage: LanguageConfig,
    modifier: Modifier = Modifier,
    onSetLanguage: (LanguageConfig) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
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
                selectedTextStyle = MifosTypography.titleSmallEmphasized.copy(
                    color = AppColors.primaryBlue,
                ),
                unselectedTextStyle = MifosTypography.titleSmallEmphasized.copy(
                    MaterialTheme.colorScheme.onSurface,
                ),
            )
        }
    }
}

@Preview
@Composable
private fun Onboarding_Screen_Preview() {
    MifosMobileTheme {
        OnboardingLanguageScreenContent(
            uiState = OnboardingLanguageState(
                currentLanguage = LanguageConfig.DEFAULT,
            ),
            onAction = {},
        )
    }
}
