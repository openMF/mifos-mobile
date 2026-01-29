/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.available_balance
import mifos_mobile.core.ui.generated.resources.customer_name
import mifos_mobile.core.ui.generated.resources.error_loading_balance
import mifos_mobile.core.ui.generated.resources.ic_icon_dashboard
import mifos_mobile.core.ui.generated.resources.pay_from
import mifos_mobile.core.ui.generated.resources.product
import mifos_mobile.core.ui.generated.resources.savings_account
import mifos_mobile.core.ui.generated.resources.select_other_payment_account
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import template.core.base.designsystem.theme.KptTheme

@Composable
fun MifosPayFromDropdownUI(
    accounts: List<Pair<String, String>>,
    onAccountSelected: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    selectedAccountNo: String = "",
    selectedAccountName: String = "",
    showExtendedDetails: Boolean = false,
    productName: String? = null,
    availableBalance: String? = null,
    isBalanceLoading: Boolean = false,
    balanceError: Boolean = false,
    label: String = stringResource(Res.string.pay_from),
) {
    Column {
        MifosDropDownPayFromComponent(
            accountNumber = selectedAccountNo,
            customerName = selectedAccountName,
            modifier = modifier,
            label = label,
            showExtendedDetails = showExtendedDetails,
            productName = productName,
            availableBalance = availableBalance,
            isBalanceLoading = isBalanceLoading,
            balanceError = balanceError,
        )
        AccountDropdownList(
            accounts = accounts,
            selectedAccount = selectedAccountNo,
            onAccountSelected = { accountNumber, balance ->
                onAccountSelected(accountNumber, balance)
            },
        )
    }
}

@Composable
fun MifosDropDownPayFromComponent(
    accountNumber: String,
    customerName: String,
    label: String,
    modifier: Modifier = Modifier,
    showExtendedDetails: Boolean = false,
    productName: String? = null,
    availableBalance: String? = null,
    isBalanceLoading: Boolean = false,
    balanceError: Boolean = false,
) {
    Box(modifier = modifier.padding(top = KptTheme.spacing.sm)) {
        Box(
            modifier = Modifier
                .clip(KptTheme.shapes.large)
                .height(DesignToken.sizes.boxDp128)
                .fillMaxWidth(),
        ) {
            Image(
                modifier = Modifier.matchParentSize(),
                painter = painterResource(Res.drawable.ic_icon_dashboard),
                contentDescription = "Background Image",
                contentScale = ContentScale.Crop,
            )

            if (showExtendedDetails) {
                ExtendedCardContent(
                    accountNumber = accountNumber,
                    customerName = customerName,
                    productName = productName,
                    availableBalance = availableBalance,
                    isBalanceLoading = isBalanceLoading,
                    balanceError = balanceError,
                )
            } else {
                DefaultCardContent(
                    accountNumber = accountNumber,
                    customerName = customerName,
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = KptTheme.spacing.md, y = (-KptTheme.spacing.sm))
                .zIndex(1f)
                .background(
                    color = KptTheme.colorScheme.background,
                    shape = KptTheme.shapes.medium,
                )
                .padding(horizontal = KptTheme.spacing.sm),
        ) {
            Text(
                text = label,
                style = KptTheme.typography.bodySmall,
                color = KptTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
fun AccountDropdownItem(
    accountNumber: String,
    balance: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(DesignToken.padding.medium),
    ) {
        Text(
            text = accountNumber,
            style = KptTheme.typography.titleSmall,
            color = KptTheme.colorScheme.onPrimary,
        )
        Spacer(modifier = Modifier.height(KptTheme.spacing.xs))
        Text(
            text = balance,
            style = KptTheme.typography.bodySmall,
            color = KptTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
private fun DefaultCardContent(
    accountNumber: String,
    customerName: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = KptTheme.spacing.md,
                vertical = KptTheme.spacing.lg,
            ),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                text = stringResource(Res.string.savings_account),
                style = KptTheme.typography.bodySmall,
                color = AppColors.customWhite.copy(alpha = 0.5f),
            )
            Text(
                text = accountNumber,
                style = KptTheme.typography.titleSmall,
                color = AppColors.customWhite,
            )
        }
        Column {
            Text(
                text = stringResource(Res.string.customer_name),
                style = KptTheme.typography.bodySmall,
                color = AppColors.customWhite.copy(alpha = 0.5f),
            )
            Text(
                text = customerName,
                style = KptTheme.typography.titleSmall,
                color = AppColors.customWhite,
            )
        }
    }
}

@Composable
private fun ExtendedCardContent(
    accountNumber: String,
    customerName: String,
    productName: String?,
    availableBalance: String?,
    isBalanceLoading: Boolean,
    balanceError: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = KptTheme.spacing.md,
                vertical = KptTheme.spacing.lg,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = stringResource(Res.string.savings_account),
                    style = KptTheme.typography.bodySmall,
                    color = AppColors.customWhite.copy(alpha = 0.5f),
                )
                Text(
                    text = accountNumber.ifEmpty { "--" },
                    style = KptTheme.typography.titleSmall,
                    color = AppColors.customWhite,
                )
            }
            Column {
                Text(
                    text = stringResource(Res.string.customer_name),
                    style = KptTheme.typography.bodySmall,
                    color = AppColors.customWhite.copy(alpha = 0.5f),
                )
                Text(
                    text = customerName.ifEmpty { "--" },
                    style = KptTheme.typography.titleSmall,
                    color = AppColors.customWhite,
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End,
        ) {
            Column(
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = stringResource(Res.string.product),
                    style = KptTheme.typography.bodySmall,
                    color = AppColors.customWhite.copy(alpha = 0.5f),
                )
                Text(
                    text = productName ?: "--",
                    style = KptTheme.typography.titleSmall,
                    color = AppColors.customWhite,
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = stringResource(Res.string.available_balance),
                    style = KptTheme.typography.bodySmall,
                    color = AppColors.customWhite.copy(alpha = 0.5f),
                )
                when {
                    isBalanceLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(DesignToken.sizes.iconSmall),
                            color = AppColors.customWhite,
                            strokeWidth = DesignToken.strokes.thin,
                        )
                    }
                    balanceError -> {
                        Text(
                            text = stringResource(Res.string.error_loading_balance),
                            style = KptTheme.typography.titleMedium,
                            color = AppColors.customWhite.copy(alpha = 0.7f),
                        )
                    }
                    else -> {
                        Text(
                            text = availableBalance ?: "--",
                            style = KptTheme.typography.titleSmall.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                            color = AppColors.customWhite,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AccountDropdownList(
    selectedAccount: String,
    accounts: List<Pair<String, String>>,
    onAccountSelected: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(KptTheme.shapes.medium)
            .background(KptTheme.colorScheme.tertiary),
    ) {
        Column(
            Modifier.fillMaxWidth(),
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(KptTheme.spacing.sm)
                    .clip(KptTheme.shapes.medium)
                    .background(KptTheme.colorScheme.background),
            )
            Row(
                modifier = Modifier
                    .padding(KptTheme.spacing.sm)
                    .clickable { expanded = !expanded }
                    .align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.xs),
            ) {
                Text(
                    text = stringResource(Res.string.select_other_payment_account),
                    style = KptTheme.typography.labelSmall,
                    color = AppColors.customWhite,
                )
                Icon(
                    imageVector = if (expanded) MifosIcons.ChevronUp else MifosIcons.ChevronDown,
                    contentDescription = "Arrow icon",
                    modifier = Modifier.size(DesignToken.sizes.iconTiny),
                    tint = AppColors.customWhite,
                )
            }
        }

        AnimatedVisibility(visible = expanded) {
            Column(
                Modifier
                    .padding(bottom = KptTheme.spacing.md),
            ) {
                accounts.forEach { (accountNumber, balance) ->
                    AccountDropdownItem(
                        accountNumber = accountNumber,
                        balance = balance,
                        modifier = if (selectedAccount == accountNumber) {
                            Modifier.background(KptTheme.colorScheme.onTertiaryContainer)
                        } else {
                            Modifier.background(KptTheme.colorScheme.tertiary)
                        },
                        onClick = {
                            expanded = !expanded
                            onAccountSelected(accountNumber, balance)
                        },
                    )
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
            modifier = Modifier
                .fillMaxSize()
                .padding(KptTheme.spacing.md),
        ) {
            MifosPayFromDropdownUI(
                accounts = listOf(
                    "267282972" to "$ 23,786.00",
                    "6572992762" to "$ 123,786.00",
                    "52682926" to "$ 78,786.00",
                    "678292726" to "$ 923,786.00",
                ),
                onAccountSelected = {
                        _, _ ->
                },
            )

            MifosPayFromDropdownUI(
                accounts = listOf(
                    "267282972" to "$ 23,786.00",
                    "6572992762" to "$ 123,786.00",
                    "52682926" to "$ 78,786.00",
                    "678292726" to "$ 923,786.00",
                ),
                onAccountSelected = { _, _ -> },
                selectedAccountNo = "267282972",
                selectedAccountName = "Mark R",
                showExtendedDetails = true,
                productName = "WALLET",
                availableBalance = "$572.98",
            )
        }
    }
}
