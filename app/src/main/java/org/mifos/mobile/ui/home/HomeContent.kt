package org.mifos.mobile.ui.home

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosHiddenTextRow
import org.mifos.mobile.core.ui.component.MifosLinkText
import org.mifos.mobile.core.ui.component.MifosUserImage
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.core.common.utils.CurrencyUtil

@Composable
fun HomeContent(
    username: String,
    totalLoanAmount: Double,
    totalSavingsAmount: Double,
    userBitmap: Bitmap?,
    homeCards: List<HomeCardItem>,
    userProfile: () -> Unit,
    totalSavings: () -> Unit,
    totalLoan: () -> Unit,
    callHelpline: (String) -> Unit,
    mailHelpline: (String) -> Unit,
    homeCardClicked: (HomeCardItem) -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
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

        HomeCards(homeCardClicked = homeCardClicked, homeCards = homeCards)

        ContactUsRow(callHelpline = callHelpline, mailHelpline = mailHelpline)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HomeCards(
    homeCardClicked: (HomeCardItem) -> Unit,
    homeCards: List<HomeCardItem>
) {
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
                onClick = { homeCardClicked(card) }
            )
        }
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

@OptIn(ExperimentalMaterial3Api::class)
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
                    hiddenText = CurrencyUtil.formatCurrency(context, totalSavingsAmount),
                    hiddenColor = colorResource(id = R.color.deposit_green),
                    hidingText = stringResource(id = R.string.hidden_amount),
                    visibilityIconId = R.drawable.ic_visibility_24px,
                    visibilityOffIconId = R.drawable.ic_visibility_off_24px,
                    onClick = totalSavings
                )

                Spacer(modifier = Modifier.height(8.dp))

                MifosHiddenTextRow(
                    title = stringResource(id = R.string.total_loan),
                    hiddenText = CurrencyUtil.formatCurrency(context, totalLoanAmount),
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
    callHelpline: (String) -> Unit,
    mailHelpline: (String) -> Unit
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
            homeCardClicked = {},
            homeCards = listOf()
        )
    }
}
