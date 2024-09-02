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

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.common.utils.LanguageHelper
import org.mifos.mobile.core.designsystem.components.MifosScaffold
import org.mifos.mobile.core.designsystem.components.MifosTopBarTitle
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.model.enums.AppTheme
import org.mifos.mobile.core.model.enums.MifosAppLanguage
import org.mifos.mobile.core.ui.component.MifosRadioButtonDialog
import org.mifos.mobile.core.ui.utils.DevicePreviews
import java.util.Locale

@Composable
internal fun SettingsScreen(
    navigateBack: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    changePassword: () -> Unit,
    changePasscode: (String) -> Unit,
    languageChanged: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val baseURL by viewModel.baseUrl.collectAsStateWithLifecycle()
    val tenant by viewModel.tenant.collectAsStateWithLifecycle()
    val passcode by viewModel.passcode.collectAsStateWithLifecycle()
    val theme by viewModel.theme.collectAsStateWithLifecycle()
    val language by viewModel.language.collectAsStateWithLifecycle()

    SettingsScreen(
        selectedLanguage = language,
        selectedTheme = theme,
        baseURL = baseURL ?: "",
        tenant = tenant ?: "",
        navigateBack = navigateBack,
        changePassword = changePassword,
        changePasscode = { changePasscode(passcode ?: "") },
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
            val isSystemLanguage = viewModel.updateLanguage(it)
            updateLanguageLocale(
                context = context,
                language = language,
                isSystemLanguage = isSystemLanguage,
            )
            languageChanged()
        },
        modifier = modifier,
    )
}

@Composable
private fun SettingsScreen(
    selectedLanguage: MifosAppLanguage,
    selectedTheme: AppTheme,
    baseURL: String,
    tenant: String,
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

    MifosScaffold(
        topBar = {
            MifosTopBarTitle(
                navigateBack = navigateBack,
                topBarTitleResId = R.string.settings,
            )
        },
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
            titleResId = R.string.choose_language,
            items = stringArrayResource(R.array.languages),
            selectItem = { _, index -> updateLanguage(MifosAppLanguage.entries[index]) },
            onDismissRequest = { showLanguageUpdateDialog = false },
            selectedItem = selectedLanguage.displayName,
        )
    }

    if (showThemeUpdateDialog) {
        MifosRadioButtonDialog(
            titleResId = R.string.change_app_theme,
            items = AppTheme.entries.map { it.themeName }.toTypedArray(),
            selectItem = { _, index -> updateTheme(AppTheme.entries[index]) },
            onDismissRequest = { showThemeUpdateDialog = false },
            selectedItem = selectedTheme.themeName,
        )
    }

    if (showEndpointUpdateDialog) {
        UpdateEndpointDialogScreen(
            initialBaseURL = baseURL,
            initialTenant = tenant,
            onDismissRequest = { showEndpointUpdateDialog = false },
            handleEndpointUpdate = handleEndpointUpdate,
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
    @StringRes title: Int,
    @StringRes details: Int,
    @DrawableRes icon: Int,
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
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.weight(0.2f),
            )
            Column(
                modifier = Modifier.weight(0.8f),
            ) {
                Text(
                    text = stringResource(id = title),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    modifier = Modifier.padding(end = 16.dp),
                    text = stringResource(id = details),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun TitleCard(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(0.2f))
        Text(
            text = stringResource(id = title),
            modifier = Modifier.weight(0.8f),
            fontSize = 14.sp,
        )
    }
}

private fun updateLanguageLocale(
    context: Context,
    language: MifosAppLanguage,
    isSystemLanguage: Boolean,
) {
    if (!isSystemLanguage) {
        LanguageHelper.setLocale(context, language.code)
    } else {
        val systemLanguageCode = Locale.getDefault().language
        if (MifosAppLanguage.entries.find { it.code == systemLanguageCode } == null) {
            LanguageHelper.setLocale(context, MifosAppLanguage.ENGLISH.code)
        } else {
            LanguageHelper.setLocale(context, systemLanguageCode)
        }
    }
}

@Composable
@DevicePreviews
private fun PreviewSettingsScreen() {
    MifosMobileTheme {
        SettingsScreen(
            selectedLanguage = MifosAppLanguage.SYSTEM_LANGUAGE,
            selectedTheme = AppTheme.SYSTEM,
            baseURL = "",
            tenant = "",
            navigateBack = {},
            changePassword = {},
            changePasscode = {},
            handleEndpointUpdate = { _, _ -> },
            updateTheme = {},
            updateLanguage = {},
        )
    }
}
