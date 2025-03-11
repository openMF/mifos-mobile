/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mifos_mobile.feature.settings.generated.resources.Res
import mifos_mobile.feature.settings.generated.resources.change_app_theme
import mifos_mobile.feature.settings.generated.resources.choose_language
import mifos_mobile.feature.settings.generated.resources.settings
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.mifos.mobile.core.datastore.model.AppTheme
import org.mifos.mobile.core.datastore.model.MifosAppLanguage
import org.mifos.mobile.core.designsystem.component.MifosScaffold
import org.mifos.mobile.core.designsystem.component.MifosTopBarTitle
import org.mifos.mobile.core.ui.component.MifosRadioButtonDialog

@Composable
internal fun SettingsScreen(
    navigateBack: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    changePassword: () -> Unit,
    changePasscode: (String) -> Unit,
    languageChanged: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        changePassword = changePassword,
        changePasscode = { changePasscode(uiState.passcode) },
        handleEndpointUpdate = { url, selectedTenant ->
            if (viewModel.tryUpdatingEndpoint(
                    selectedBaseUrl = url,
                    selectedTenant = selectedTenant,
                )
            ) {
                navigateToLoginScreen()
            }
        },
        updateTheme = viewModel::updateTheme,
        updateLanguage = {
//            val isSystemLanguage = viewModel.updateLanguage(it)
            updateLanguageLocale(
//                language = language,
//                isSystemLanguage = isSystemLanguage,
            )
            languageChanged()
        },
        modifier = modifier,
    )
}

@Composable
private fun SettingsScreen(
    uiState: SettingsUiState,
    navigateBack: () -> Unit,
    changePassword: () -> Unit,
    changePasscode: () -> Unit,
    handleEndpointUpdate: (baseURL: String, tenant: String) -> Unit,
    updateTheme: (theme: AppTheme) -> Unit,
    updateLanguage: (language: MifosAppLanguage) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showLanguageUpdateDialog by rememberSaveable { mutableStateOf(false) }
    var showEndpointUpdateDialog by rememberSaveable { mutableStateOf(false) }
    var showThemeUpdateDialog by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    MifosScaffold(
        topBar = {
            MifosTopBarTitle(
                navigateBack = navigateBack,
                topBarTitleResId = Res.string.settings,
            )
        },
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    ) {
        Column(
            Modifier.padding(it),
        ) {
            SettingsCards(
                settingsCardClicked = { item ->
                    when (item) {
                        SettingsCardItem.PASSWORD -> changePassword()
                        SettingsCardItem.PASSCODE -> changePasscode()
                        SettingsCardItem.LANGUAGE -> showLanguageUpdateDialog = true
                        SettingsCardItem.THEME -> showThemeUpdateDialog = true
                        SettingsCardItem.ENDPOINT -> showEndpointUpdateDialog = true
                    }
                },
            )
        }
    }

    if (showLanguageUpdateDialog) {
        MifosRadioButtonDialog(
            titleResId = Res.string.choose_language,
            items = uiState.allLanguages.toTypedArray(),
            selectItem = { _, index -> updateLanguage(MifosAppLanguage.entries[index]) },
            onDismissRequest = { showLanguageUpdateDialog = false },
            selectedItem = uiState.language.displayName,
        )
    }

    if (showThemeUpdateDialog) {
        MifosRadioButtonDialog(
            titleResId = Res.string.change_app_theme,
            items = AppTheme.entries.map { it.themeName }.toTypedArray(),
            selectItem = { _, index -> updateTheme(AppTheme.entries[index]) },
            onDismissRequest = { showThemeUpdateDialog = false },
            selectedItem = uiState.theme.themeName,
        )
    }

    if (showEndpointUpdateDialog) {
        UpdateEndpointDialogScreen(
            initialBaseURL = uiState.baseUrl,
            initialTenant = uiState.tenant,
            onDismissRequest = { showEndpointUpdateDialog = false },
            handleEndpointUpdate = handleEndpointUpdate,
            snackbarHostState = snackbarHostState,
        )
    }
}

@Composable
private fun SettingsCards(
    settingsCardClicked: (SettingsCardItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier) {
        items(SettingsCardItem.entries) { card ->
            if (card.firstItemInSubclass) {
                Spacer(modifier = Modifier.height(16.dp))
                TitleCard(title = card.subclassOf)
                Spacer(modifier = Modifier.height(12.dp))
            }

            SettingsCardItem(
                title = card.title,
                details = card.details,
                icon = card.icon,
                onclick = { settingsCardClicked(card) },
            )

            if (card.showDividerInBottom) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                )
            }
        }
    }
}

@Composable
private fun SettingsCardItem(
    title: StringResource,
    details: StringResource,
    icon: DrawableResource,
    onclick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(0.dp),
        onClick = { onclick.invoke() },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp),
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.weight(0.2f),
            )
            Column(
                modifier = Modifier.weight(0.8f),
            ) {
                Text(
                    text = stringResource(title),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    modifier = Modifier.padding(end = 16.dp),
                    text = stringResource(details),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun TitleCard(
    title: StringResource,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(0.2f))
        Text(
            text = stringResource(title),
            modifier = Modifier.weight(0.8f),
            fontSize = 14.sp,
        )
    }
}

private fun updateLanguageLocale(
//    language: MifosAppLanguage,
//    isSystemLanguage: Boolean,
) {
//    if (!isSystemLanguage) {
//        LanguageHelper.setLocale(context, language.code)
//    } else {
//        val systemLanguageCode = Locale.getDefault().language
//        if (MifosAppLanguage.entries.find { it.code == systemLanguageCode } == null) {
//            LanguageHelper.setLocale(context, MifosAppLanguage.ENGLISH.code)
//        } else {
//            LanguageHelper.setLocale(context, systemLanguageCode)
//        }
//    }
}
