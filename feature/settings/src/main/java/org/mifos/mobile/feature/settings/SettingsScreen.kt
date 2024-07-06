package org.mifos.mobile.feature.settings

import android.content.Context
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosRadioButtonDialog
import org.mifos.mobile.core.ui.component.MifosTopBarTitle
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.core.model.enums.AppTheme
import org.mifos.mobile.core.common.utils.LanguageHelper
import org.mifos.mobile.core.model.enums.MifosAppLanguage
import java.util.Locale

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToLoginScreen: () -> Unit,
    changePassword: () -> Unit,
    changePasscode: (String) -> Unit,
    languageChanged: () -> Unit
) {

    val baseURL = viewModel.baseUrl.collectAsStateWithLifecycle()
    val tenant = viewModel.tenant.collectAsStateWithLifecycle()
    val passcode = viewModel.passcode.collectAsStateWithLifecycle()
    val theme = viewModel.theme.collectAsStateWithLifecycle()
    val language = viewModel.language.collectAsStateWithLifecycle()

    val context = LocalContext.current

    SettingsScreen(
        navigateBack = navigateBack,
        selectedLanguage = language.value,
        selectedTheme = theme.value,
        baseURL = baseURL.value ?: "",
        tenant = tenant.value ?: "",
        updateLanguage = {
            val isSystemLanguage = viewModel.updateLanguage(it)
            updateLanguageLocale(context = context, language = language.value, isSystemLanguage = isSystemLanguage)
            languageChanged()
        },
        updateTheme = { viewModel.updateTheme(it) },
        changePassword = changePassword,
        changePasscode = { changePasscode(passcode.value ?: "") },
        handleEndpointUpdate = { baseURL, tenant ->
            if(viewModel.tryUpdatingEndpoint(selectedBaseUrl = baseURL, selectedTenant = tenant)) {
                navigateToLoginScreen()
            }
        },
    )
}


@Composable
fun SettingsScreen(
    navigateBack: () -> Unit,
    selectedLanguage: MifosAppLanguage,
    selectedTheme: AppTheme,
    baseURL: String,
    tenant: String,
    changePassword: () -> Unit,
    changePasscode: () -> Unit,
    handleEndpointUpdate: (baseURL: String, tenant: String) -> Unit,
    updateTheme: (theme: AppTheme) -> Unit,
    updateLanguage: (language: MifosAppLanguage) -> Unit
) {

    var showLanguageUpdateDialog by rememberSaveable { mutableStateOf(false) }
    var showEndpointUpdateDialog by rememberSaveable { mutableStateOf(false) }
    var showThemeUpdateDialog by rememberSaveable { mutableStateOf(false) }

    MFScaffold(
        topBar = {
            MifosTopBarTitle(
                navigateBack = navigateBack,
                topBarTitleResId = R.string.settings
            )
        }
    ) {
        Column(
            Modifier.padding(it)
        ) {
            SettingsCards(
                settingsCardClicked = { item ->
                    when(item) {
                        SettingsCardItem.PASSWORD -> changePassword()
                        SettingsCardItem.PASSCODE -> changePasscode()
                        SettingsCardItem.LANGUAGE -> showLanguageUpdateDialog = true
                        SettingsCardItem.THEME -> showThemeUpdateDialog = true
                        SettingsCardItem.ENDPOINT -> showEndpointUpdateDialog = true
                    }
                }
            )
        }
    }

    if(showLanguageUpdateDialog) {
        MifosRadioButtonDialog(
            titleResId = R.string.choose_language,
            items = stringArrayResource(R.array.languages),
            selectItem = { _, index -> updateLanguage(MifosAppLanguage.entries[index]) },
            onDismissRequest = { showLanguageUpdateDialog = false },
            selectedItem = selectedLanguage.displayName
        )
    }

    if(showThemeUpdateDialog) {
        MifosRadioButtonDialog(
            titleResId = R.string.change_app_theme,
            items = AppTheme.entries.map { it.themeName }.toTypedArray(),
            selectItem = { _, index -> updateTheme(AppTheme.entries[index]) },
            onDismissRequest = { showThemeUpdateDialog = false },
            selectedItem = selectedTheme.themeName
        )
    }

    if(showEndpointUpdateDialog) {
        UpdateEndpointDialogScreen(
            initialBaseURL = baseURL,
            initialTenant = tenant,
            onDismissRequest = { showEndpointUpdateDialog = false },
            handleEndpointUpdate = handleEndpointUpdate
        )
    }
}

@Composable
fun SettingsCards(
    settingsCardClicked: (SettingsCardItem) -> Unit,
) {
    LazyColumn {
        items(SettingsCardItem.entries) { card ->
            if (card.firstItemInSubclass) {
                TitleCard(title = card.subclassOf)
            }

            SettingsCardItem(
                title = card.title,
                details = card.details,
                icon = card.icon,
                onclick = { settingsCardClicked(card) }
            )

            if (card.showDividerInBottom) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().height(1.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                )
            }
        }
    }
}

@Composable
fun SettingsCardItem(
    title: Int,
    details: Int,
    icon: Int,
    onclick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(0.dp),
        onClick = { onclick.invoke() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp),
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.weight(0.2f)
            )
            Column(
                modifier = Modifier.weight(0.8f)
            ) {
                Text(
                    text = stringResource(id = title),
                    style = MaterialTheme.typography.bodyMedium
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
fun TitleCard(
    title: Int
) {
    Spacer(modifier = Modifier.height(16.dp))
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(0.2f))
        Text(
            text = stringResource(id = title),
            modifier = Modifier.weight(0.8f),
            fontSize = 14.sp
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
}

fun updateLanguageLocale(context: Context, language: MifosAppLanguage, isSystemLanguage: Boolean) {
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
@Preview(showSystemUi = true, showBackground = true)
fun PreviewSettingsScreen() {
    MifosMobileTheme {
        SettingsScreen(
            selectedLanguage = MifosAppLanguage.SYSTEM_LANGUAGE,
            selectedTheme = AppTheme.SYSTEM,
            baseURL = "",
            tenant = "",
            handleEndpointUpdate = { _, _ -> },
            updateLanguage = {},
            updateTheme = {},
            navigateBack = {},
            changePassword = {},
            changePasscode = {}
        )
    }
}