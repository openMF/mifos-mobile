package org.mifos.mobile.feature.home.screens

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.mifos.mobile.core.ui.component.MifosHiddenTextRow
import org.mifos.mobile.core.ui.component.MifosLinkText
import org.mifos.mobile.core.ui.component.MifosUserImage
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.core.common.utils.CurrencyUtil
import org.mifos.mobile.core.ui.component.MFScaffold
import org.mifos.mobile.core.ui.component.MifosAlertDialog
import org.mifos.mobile.feature.home.R
import org.mifos.mobile.feature.home.components.HomeNavigationDrawer
import org.mifos.mobile.feature.home.components.HomeTopBar
import org.mifos.mobile.feature.home.components.TransferDialog
import org.mifos.mobile.feature.home.navigation.HomeDestinations
import org.mifos.mobile.feature.home.navigation.toDestination
import org.mifos.mobile.feature.home.viewmodel.HomeCardItem
import org.mifos.mobile.feature.home.viewmodel.HomeNavigationItems

@Composable
fun HomeContent(
    username: String,
    totalLoanAmount: Double,
    totalSavingsAmount: Double,
    userBitmap: Bitmap?,
    notificationCount: Int,
    homeCards: List<HomeCardItem>,
    userProfile: () -> Unit,
    totalSavings: () -> Unit,
    totalLoan: () -> Unit,
    callHelpline: () -> Unit,
    mailHelpline: () -> Unit,
    onNavigate: (HomeDestinations) -> Unit,
    openNotifications: () -> Unit,
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    HomeNavigationDrawer(
        drawerState = drawerState,
        userBitmap = userBitmap,
        username = username,
        navigateItem = {
            coroutineScope.launch { drawerState.close() }
            when(it) {
                HomeNavigationItems.Logout -> showLogoutDialog = true
                else -> onNavigate(it.toDestination())
            }
        },
        content = {
            MFScaffold(
                topBar = {
                    HomeTopBar(
                        openNavigationDrawer = {
                            coroutineScope.launch { drawerState.open() }
                        },
                        notificationCount = notificationCount,
                        openNotifications = openNotifications
                    )
                }
            ) {
                HomeContent(
                    modifier = Modifier.padding(it),
                    username = username,
                    totalLoanAmount = totalLoanAmount,
                    totalSavingsAmount = totalSavingsAmount,
                    userBitmap = userBitmap,
                    homeCards = homeCards,
                    userProfile = userProfile,
                    totalSavings = totalSavings,
                    totalLoan = totalLoan,
                    callHelpline = callHelpline,
                    mailHelpline = mailHelpline,
                    onNavigate = onNavigate
                )
            }
        }
    )

    if(showLogoutDialog) {
        MifosAlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            dismissText = stringResource(id = R.string.cancel),
            onConfirmation = {
                onNavigate(HomeDestinations.LOGOUT)
                showLogoutDialog = false
            },
            confirmationText = stringResource(id = R.string.logout),
            dialogTitle = stringResource(id = R.string.dialog_logout),
            dialogText = ""
        )
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    username: String,
    totalLoanAmount: Double,
    totalSavingsAmount: Double,
    userBitmap: Bitmap?,
    homeCards: List<HomeCardItem>,
    userProfile: () -> Unit,
    totalSavings: () -> Unit,
    totalLoan: () -> Unit,
    callHelpline: () -> Unit,
    mailHelpline: () -> Unit,
    onNavigate: (HomeDestinations) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {
        UserDetailsRow(
            userBitmap = userBitmap,
            username = username,
            userProfile = userProfile
        )

        Spacer(modifier = Modifier.height(8.dp))

        AccountOverviewCard(
            totalLoanAmount = totalLoanAmount,
            totalSavingsAmount = totalSavingsAmount,
            totalLoan = totalLoan,
            totalSavings = totalSavings
        )

        Spacer(modifier = Modifier.height(8.dp))

        HomeCards(onNavigate = onNavigate, homeCards = homeCards)

        ContactUsRow(callHelpline = callHelpline, mailHelpline = mailHelpline)
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HomeCards(
    onNavigate: (HomeDestinations) -> Unit,
    homeCards: List<HomeCardItem>
) {
    var showTransferDialog by rememberSaveable { mutableStateOf(false) }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        maxItemsInEachRow = 3,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        homeCards.forEach { card ->
            HomeCard(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 8.dp),
                titleId = card.titleId,
                drawableResId = card.drawableResId,
                onClick = {
                    if(card == HomeCardItem.TransferCard) {
                        showTransferDialog = true
                    } else {
                        onNavigate(card.toDestination())
                    }
                }
            )
        }
    }

    if(showTransferDialog) {
        TransferDialog(
            onDismissRequest = { showTransferDialog = false },
            navigateToTransfer = { onNavigate(HomeDestinations.TRANSFER) },
            navigateToThirdPartyTransfer = {  onNavigate(HomeDestinations.THIRD_PARTY_TRANSFER) }
        )
    }
}

@Composable
fun UserDetailsRow(
    userBitmap: Bitmap?,
    username: String,
    userProfile: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MifosUserImage(
            modifier = Modifier
                .size(84.dp)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                ) { userProfile.invoke() },
            bitmap = userBitmap,
            username = username
        )
        Text(
            text = stringResource(R.string.hello_client, username),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(1f)
        )
    }
}

@Composable
private fun HomeCard(
    modifier: Modifier,
    titleId: Int,
    drawableResId: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = { onClick.invoke() }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = drawableResId),
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(id = titleId),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun AccountOverviewCard(
    totalLoanAmount: Double,
    totalSavingsAmount: Double,
    totalSavings: () -> Unit,
    totalLoan: () -> Unit
) {
    val context = LocalContext.current
    val isInPreview = LocalInspectionMode.current

    Row {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.accounts_overview),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Divider(modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))

                MifosHiddenTextRow(
                    title = stringResource(id = R.string.total_saving),
                    hiddenText = if (isInPreview) "" else CurrencyUtil.formatCurrency(
                        context,
                        totalSavingsAmount
                    ),
                    hiddenColor = colorResource(id = R.color.deposit_green),
                    hidingText = stringResource(id = R.string.hidden_amount),
                    visibilityIconId = R.drawable.ic_visibility_24px,
                    visibilityOffIconId = R.drawable.ic_visibility_off_24px,
                    onClick = totalSavings
                )

                Spacer(modifier = Modifier.height(8.dp))

                MifosHiddenTextRow(
                    title = stringResource(id = R.string.total_loan),
                    hiddenText = if (isInPreview) "" else CurrencyUtil.formatCurrency(
                        context,
                        totalLoanAmount
                    ),
                    hiddenColor = colorResource(id = R.color.red),
                    hidingText = stringResource(id = R.string.hidden_amount),
                    visibilityIconId = R.drawable.ic_visibility_24px,
                    visibilityOffIconId = R.drawable.ic_visibility_off_24px,
                    onClick = totalLoan
                )
            }
        }
    }
}

@Composable
private fun ContactUsRow(
    callHelpline: () -> Unit,
    mailHelpline: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.need_help),
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )

        Column {
            MifosLinkText(
                text = stringResource(id = R.string.help_line_number),
                modifier = Modifier.align(Alignment.End),
                onClick = callHelpline,
                isUnderlined = false
            )

            MifosLinkText(
                text = stringResource(id = R.string.contact_email),
                modifier = Modifier.align(Alignment.End),
                onClick = mailHelpline
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewHomeContent() {
    MifosMobileTheme {
        HomeContent(
            username = stringResource(id = R.string.app_name),
            totalLoanAmount = 32.32,
            totalSavingsAmount = 34.43,
            userBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888),
            callHelpline = {},
            mailHelpline = {},
            totalSavings = {},
            totalLoan = {},
            userProfile = {},
            onNavigate = {},
            notificationCount = 0,
            homeCards = listOf(),
            openNotifications = {},
        )
    }
}
