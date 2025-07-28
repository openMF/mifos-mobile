package org.mifos.mobile.core.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.ic_icon_dashboard
import mifos_mobile.core.ui.generated.resources.powered_by
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.designsystem.theme.MifosTypography

@Composable
fun MifosPayFromDropdownUI(
    accounts: List<Pair<String, String>>,
) {
    var selectedAccount by remember { mutableStateOf("") }
    var selectedBalance by remember { mutableStateOf("") }
    Column {
        MifosDropDownPayFromComponent(
            accountNumber = selectedAccount,
            availableBalance = selectedBalance,
        )
        Spacer(modifier = Modifier.height(8.dp))
        AccountDropdownList(
            accounts = accounts,
            onAccountSelected = { accountNumber,balance->
                selectedAccount = accountNumber
                selectedBalance = balance
            }
        )
    }
}


@Composable
fun MifosDropDownPayFromComponent(
    modifier: Modifier= Modifier,
    accountNumber:String,
    availableBalance:String,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .height(128.dp)
            .fillMaxWidth(),
    ){
        Image(
            modifier = Modifier
                .matchParentSize(),
            painter = painterResource(Res.drawable.ic_icon_dashboard),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(DesignToken.padding.large),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = "Savings Account",
                    style = MifosTypography.bodySmall,
                    color = AppColors.customWhite.copy(alpha = 0.5f),
                )
                Text(
                    text = accountNumber,
                    style = MifosTypography.titleMediumEmphasized,
                    color = AppColors.customWhite,
                )
            }
            Column{
                Text(
                    text = "Available Balance",
                    style = MifosTypography.bodySmall,
                    color = AppColors.customWhite.copy(alpha = 0.5f),
                )
                Text(
                    text = availableBalance,
                    style = MifosTypography.titleMediumEmphasized,
                    color = AppColors.customWhite,
                )
            }
        }
    }
}

@Composable
fun AccountDropdownItem(
    accountNumber: String,
    balance: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(16.dp)
    ) {
        Text(
            text = accountNumber,
            style = MifosTypography.titleMediumEmphasized,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Available Balance - $balance",
            style = MifosTypography.bodySmall,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}
@Composable
fun AccountDropdownList(
    accounts: List<Pair<String, String>>,
    onAccountSelected: (String, String) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = DesignToken.padding.medium)
            .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
            .background(MaterialTheme.colorScheme.tertiary)
    ) {
        Row(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Select Other Payment Account")
            Icon(
                imageVector = if (expanded) MifosIcons.ArrowDropUp else MifosIcons.ArrowDropDown,
                contentDescription = null
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column {
                accounts.forEach { (accountNumber, balance) ->
                    AccountDropdownItem(
                        accountNumber = accountNumber,
                        balance = balance
                    ) {
                        expanded=!expanded
                        onAccountSelected(accountNumber, balance)
                    }
                    Divider(color = Color.White.copy(alpha = 0.1f))
                }
            }
        }
    }
}



@Preview
@Composable
private fun MifosDropDownPayFromComponentPreview() {
    MifosMobileTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MifosDropDownPayFromComponent(
                accountNumber = "1200-67867-8474",
                availableBalance = "25,600"
            )
        }
    }
}
