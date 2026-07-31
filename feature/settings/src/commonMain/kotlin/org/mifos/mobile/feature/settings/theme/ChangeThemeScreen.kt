/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.cancel
import mifos_mobile.feature.settings.generated.resources.dialog_action_ok
import mifos_mobile.feature.settings.generated.resources.feature_settings_action_theme
import mifos_mobile.feature.settings.generated.resources.feature_settings_apply_theme
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_appearance
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_apply
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_brand_aurora_description
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_brand_graphite_description
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_brand_ipoteka_description
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_brand_subtitle
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_choose_dark_mode_ends_at
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_choose_dark_mode_starts_at
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_choose_dark_mode_time
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_dark
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_light
import mifos_mobile.feature.settings.generated.resources.feature_settings_theme_mode
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.datastore.model.TimeBasedTheme
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosElevatedScaffold
import org.mifos.mobile.core.designsystem.component.MifosRadioButton
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.designsystem.theme.brandColorScheme
import org.mifos.mobile.core.model.MifosBrandTheme
import org.mifos.mobile.core.model.MifosThemeConfig
import org.mifos.mobile.core.ui.utils.DevicePreview
import org.mifos.mobile.core.ui.utils.EventsEffect
import template.core.base.designsystem.theme.KptTheme

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
@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun ThemeScreenContent(
    uiState: ThemeState,
    modifier: Modifier = Modifier,
    onAction: (ThemeAction) -> Unit,
) {
    MifosElevatedScaffold(
        modifier = modifier
            .fillMaxSize()
            .semantics { testTagsAsResourceId = true },
        topBarTitle = stringResource(Res.string.feature_settings_action_theme),
        onNavigateBack = {
            onAction(ThemeAction.NavigateBack)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(KptTheme.spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(DesignToken.padding.largeIncreased),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.md),
            ) {
                Text(
                    text = stringResource(Res.string.feature_settings_theme_appearance),
                    style = MifosTypography.titleLarge,
                )
                Text(
                    text = stringResource(Res.string.feature_settings_theme_brand_subtitle),
                    style = MifosTypography.bodyMedium,
                    color = KptTheme.colorScheme.onSurfaceVariant,
                )

                uiState.brandOptions.forEach { (brand, labelRes) ->
                    BrandThemeCard(
                        brand = brand,
                        name = stringResource(labelRes),
                        description = brandDescription(brand),
                        selected = uiState.currentBrand == brand,
                        darkPreview = uiState.currentTheme == MifosThemeConfig.DARK,
                        onClick = { onAction(ThemeAction.BrandSelection(brand)) },
                    )
                }

                Text(
                    text = stringResource(Res.string.feature_settings_theme_mode),
                    style = MifosTypography.titleMedium,
                    modifier = Modifier.padding(top = KptTheme.spacing.sm),
                )

                uiState.themeOptions.forEach { (theme, labelRes) ->
                    MifosRadioButton(
                        label = if (theme == MifosThemeConfig.BASED_ON_TIME) {
                            stringResource(labelRes) +
                                "\n" +
                                stringResource(Res.string.feature_settings_theme_dark) + " [" +
                                DateHelper.formatTimeRange(
                                    uiState.timeBasedTheme.hourStart,
                                    uiState.timeBasedTheme.timeStart,
                                    uiState.timeBasedTheme.hourEnd,
                                    uiState.timeBasedTheme.timeEnd,
                                ) +
                                "]\n" +
                                stringResource(Res.string.feature_settings_theme_light) + " [" +
                                DateHelper.formatTimeRange(
                                    uiState.timeBasedTheme.hourEnd,
                                    uiState.timeBasedTheme.timeEnd,
                                    uiState.timeBasedTheme.hourStart,
                                    uiState.timeBasedTheme.timeStart,
                                ) +
                                "]"
                        } else {
                            stringResource(labelRes)
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("theme.mode.${theme.name.lowercase()}"),
                        selected = uiState.currentTheme == theme,
                        onClick = { onAction(ThemeAction.ThemeSelection(theme)) },
                        selectedTextStyle = MifosTypography.titleSmallEmphasized.copy(
                            color = KptTheme.colorScheme.primary,
                        ),
                        unselectedTextStyle = MifosTypography.titleSmallEmphasized.copy(
                            KptTheme.colorScheme.onSurface,
                        ),
                    )
                }
            }

            MifosButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DesignToken.sizes.buttonHeight)
                    .testTag("theme.apply"),
                shape = KptTheme.shapes.medium,
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

        if (uiState.showTimeBasedDialog) {
            TimeBasedThemeDialog(
                initialTheme = uiState.timeBasedTheme,
                onDismiss = {
                    onAction(ThemeAction.HideTimeBasedDialog)
                },
                onConfirm = {
                    onAction(ThemeAction.UpdateTimeBasedTheme(it))
                },
            )
        }
    }
}

@Composable
private fun BrandThemeCard(
    brand: MifosBrandTheme,
    name: String,
    description: String,
    selected: Boolean,
    darkPreview: Boolean,
    onClick: () -> Unit,
) {
    val colors = brandColorScheme(brand, darkPreview)
    val tag = "theme.skin.${brand.name.lowercase()}"
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(tag)
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            ),
        shape = KptTheme.shapes.large,
        color = if (selected) colors.primaryContainer else colors.surfaceContainerLow,
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) colors.primary else colors.outlineVariant,
        ),
        tonalElevation = if (selected) KptTheme.elevation.level2 else KptTheme.elevation.level0,
    ) {
        Column(
            modifier = Modifier.padding(KptTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm),
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    listOf(colors.primary, colors.secondary, colors.tertiary).forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(color),
                        )
                    }
                    Text(
                        text = name,
                        style = MifosTypography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = if (selected) colors.onPrimaryContainer else colors.onSurface,
                        modifier = Modifier.padding(start = KptTheme.spacing.xs),
                    )
                }
                if (selected) {
                    Text(
                        text = "✓",
                        style = MaterialTheme.typography.titleLarge,
                        color = colors.primary,
                    )
                }
            }
            Text(
                text = description,
                style = MifosTypography.bodySmall,
                color = if (selected) {
                    colors.onPrimaryContainer.copy(alpha = 0.76f)
                } else {
                    colors.onSurfaceVariant
                },
            )
        }
    }
}

@Composable
private fun brandDescription(brand: MifosBrandTheme): String = stringResource(
    when (brand) {
        MifosBrandTheme.IPOTEKA ->
            Res.string.feature_settings_theme_brand_ipoteka_description
        MifosBrandTheme.AURORA ->
            Res.string.feature_settings_theme_brand_aurora_description
        MifosBrandTheme.GRAPHITE ->
            Res.string.feature_settings_theme_brand_graphite_description
    },
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeBasedThemeDialog(
    initialTheme: TimeBasedTheme,
    onDismiss: () -> Unit,
    onConfirm: (TimeBasedTheme) -> Unit,
) {
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    var startHour by remember { mutableStateOf(initialTheme.hourStart) }
    var startMinute by remember { mutableStateOf(initialTheme.timeStart) }

    var endHour by remember { mutableStateOf(initialTheme.hourEnd) }
    var endMinute by remember { mutableStateOf(initialTheme.timeEnd) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(Res.string.feature_settings_theme_choose_dark_mode_time),
                style = MifosTypography.titleLarge,
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                TimeRow(
                    label = stringResource(Res.string.feature_settings_theme_choose_dark_mode_starts_at),
                    time = "$startHour:$startMinute",
                    onClick = { showStartPicker = true },
                )

                TimeRow(
                    label = stringResource(Res.string.feature_settings_theme_choose_dark_mode_ends_at),
                    time = "$endHour:$endMinute",
                    onClick = { showEndPicker = true },
                )
            }
        },
        confirmButton = {
            MifosButton(
                onClick = {
                    onConfirm(
                        TimeBasedTheme(
                            hourStart = startHour,
                            timeStart = startMinute,
                            hourEnd = endHour,
                            timeEnd = endMinute,
                        ),
                    )
                },
            ) {
                Text(stringResource(Res.string.feature_settings_theme_apply))
            }
        },
        dismissButton = {
            MifosButton(
                onClick = onDismiss,
                colors = androidx.compose.material3.ButtonDefaults.textButtonColors(),
            ) {
                Text(stringResource(Res.string.cancel))
            }
        },
    )

    if (showStartPicker) {
        TimePickerDialog(
            initialHour = startHour,
            initialMinute = startMinute,
            onDismiss = { showStartPicker = false },
            onConfirm = { h, m ->
                startHour = h
                startMinute = m
                showStartPicker = false
            },
        )
    }

    if (showEndPicker) {
        TimePickerDialog(
            initialHour = endHour,
            initialMinute = endMinute,
            onDismiss = { showEndPicker = false },
            onConfirm = { h, m ->
                endHour = h
                endMinute = m
                showEndPicker = false
            },
        )
    }
}

@Composable
private fun TimeRow(
    label: String,
    time: String,
    onClick: () -> Unit,
) {
    Column {
        Text(
            text = label,
            style = MifosTypography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(4.dp))

        androidx.compose.material3.Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            shape = DesignToken.shapes.medium,
            tonalElevation = 1.dp,
        ) {
            Text(
                text = time,
                modifier = Modifier.padding(16.dp),
                style = MifosTypography.titleMedium,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
) {
    val state = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            MifosButton(
                onClick = {
                    onConfirm(state.hour, state.minute)
                },
            ) {
                Text(stringResource(Res.string.dialog_action_ok))
            }
        },
        dismissButton = {
            MifosButton(
                onClick = onDismiss,
                colors = androidx.compose.material3.ButtonDefaults.textButtonColors(),
            ) {
                Text(stringResource(Res.string.cancel))
            }
        },
        text = {
            TimePicker(state = state)
        },
    )
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
            uiState = ThemeState(
                currentTheme = MifosThemeConfig.FOLLOW_SYSTEM,
                currentBrand = MifosBrandTheme.IPOTEKA,
            ),
            onAction = {},
        )
    }
}
