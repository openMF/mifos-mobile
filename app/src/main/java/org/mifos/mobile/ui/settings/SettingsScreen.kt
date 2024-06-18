package org.mifos.mobile.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import org.mifos.mobile.R
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosRadioButtonAlertDialog
import org.mifos.mobile.core.ui.theme.MifosMobileTheme

@Composable
fun SettingsScreen(
    settingsCard: List<SettingsCardItem>,
    onBackPressed: () -> Unit,
    handleEndpointupdate: (etBaseURL: String, etTenant: String) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
    prefsHelper: PreferencesHelper,
    changePassword: () -> Unit,
    changePasscode: () -> Unit,
) {
    val context = LocalContext.current

    fun handleSettingsCardClick(settingsCardItem: SettingsCardItem) {
        when (settingsCardItem) {
            is SettingsCardItem.Password -> {
                changePassword.invoke()
            }

            is SettingsCardItem.Passcode -> {
                changePasscode.invoke()
            }

            is SettingsCardItem.Language -> {
                Snapshot.withMutableSnapshot {
                    viewModel.invokeLanguageUpdate = true
                }
            }

            is SettingsCardItem.Theme -> {
                Snapshot.withMutableSnapshot {
                    viewModel.invokeThemeUpdate = true
                }
            }

            is SettingsCardItem.EndPoint -> {
                Snapshot.withMutableSnapshot {
                    viewModel.invokeEndpointUpdate = true
                }
            }
        }
    }

    SettingsScreen(
        settingsCard = settingsCard,
        settingsCardClicked = { handleSettingsCardClick(it) },
        onBackPressed = onBackPressed,
    )

    if (viewModel.invokeEndpointUpdate) {
        UpdateEndpointDialogScreen(
            initialBaseURL = viewModel.getBaseUrl(),
            initialTenant = viewModel.getTenant(),
            updateInvokeEndpointValue = {
                Snapshot.withMutableSnapshot {
                    viewModel.invokeEndpointUpdate = false
                }
            },
            handleEndpointUpdate = handleEndpointupdate
        )
    }

    if (viewModel.invokeLanguageUpdate) {
        MifosRadioButtonAlertDialog(
            setTitle = stringResource(id = R.string.choose_language),
            setSingleChoiceItems = context.resources.getStringArray(R.array.languages),
            onClick = {
                updateLanguage(
                    context,
                    context.resources.getStringArray(R.array.languages_value)[it]
                )
            },
            onDismissRequest = {
                Snapshot.withMutableSnapshot {
                    viewModel.invokeLanguageUpdate = false
                }
            },
            setSelectedItemValue = context.resources.getStringArray(R.array.languages)[getSelectedLanguageIndex(
                context,
                prefsHelper
            )]
        )
    }

    if (viewModel.invokeThemeUpdate) {
        MifosRadioButtonAlertDialog(
            setTitle = stringResource(id = R.string.change_app_theme),
            setSingleChoiceItems = context.resources.getStringArray(R.array.themes),
            onClick = {
                updateTheme(it, prefsHelper)
            },
            onDismissRequest = {
                Snapshot.withMutableSnapshot {
                    viewModel.invokeThemeUpdate = false
                }
            },
            setSelectedItemValue = context.resources.getStringArray(R.array.themes)[getCurrentTheme(prefsHelper)]
        )
    }
}

@Composable
fun SettingsScreen(
    settingsCard: List<SettingsCardItem>,
    settingsCardClicked: (SettingsCardItem) -> Unit,
    onBackPressed: () -> Unit,
) {
    MFScaffold(
        topBarTitleResId = R.string.settings,
        navigateBack = { onBackPressed.invoke() }) {
        Column(
            Modifier.fillMaxSize().padding(it)
        ) {
            SettingsCards(
                settingsCardClicked = settingsCardClicked,
                settingsCards = settingsCard
            )
        }
    }
}

@Composable
fun SettingsCards(
    settingsCardClicked: (SettingsCardItem) -> Unit,
    settingsCards: List<SettingsCardItem>
) {
    LazyColumn {
        items(settingsCards) { card ->
            if (card.firstItemInSubclass) {
                TitleCard(title = card.subclassOf)
            }

            SettingsCardItem(
                title = card.title,
                details = card.details,
                icon = card.icon,
                onclick = {
                    settingsCardClicked(card)
                }
            )

            if (card.showDividerInBottom) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.1.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .3f)
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
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(0.dp),
        onClick = {
            onclick.invoke()
        })
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp),
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.weight(.2f)
            )
            Column(
                modifier = Modifier.weight(.8f)
            ) {
                Text(
                    text = stringResource(id = title),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    modifier = Modifier.padding(end = 16.dp),
                    text = stringResource(id = details),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .7f),
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 10.dp)
    ) {
        Text(
            text = "",
            modifier = Modifier.weight(.2f)
        )
        Text(
            text = stringResource(id = title),
            modifier = Modifier.weight(.8f),
            fontSize = 14.sp
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewSettingsScreen() {
    MifosMobileTheme {
        SettingsScreen(
            settingsCard = (listOf(
                SettingsCardItem.Password,
                SettingsCardItem.Passcode,
                SettingsCardItem.Language,
                SettingsCardItem.Theme,
                SettingsCardItem.EndPoint
            )),
            settingsCardClicked = {},
            onBackPressed = {},
        )
    }
}

