/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.loanaccount.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import mifos_mobile.feature.loan_account.generated.resources.Res
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_due
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_installment_number
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_paid
import mifos_mobile.feature.loan_account.generated.resources.feature_loan_repayment_pay
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DateHelper
import org.mifos.mobile.core.designsystem.component.CardVariant
import org.mifos.mobile.core.designsystem.component.MifosButton
import org.mifos.mobile.core.designsystem.component.MifosCustomCard
import org.mifos.mobile.core.designsystem.theme.AppColors
import org.mifos.mobile.core.designsystem.theme.DesignToken
import org.mifos.mobile.core.designsystem.theme.MifosTypography
import org.mifos.mobile.core.model.entity.accounts.loan.Periods
import template.core.base.designsystem.theme.KptTheme
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * A composable that displays a single item in a repayment schedule.
 * It shows details such as the installment number, due date, and amount.
 * It also provides a button to pay the installment if it's due.
 *
 * @param period The [Periods] object containing the details of the repayment period.
 * @param currencyCode The currency code to use for formatting the amount.
 * @param maxDigits The maximum number of digits to display for the fractional part of the amount.
 * @param modifier The modifier to be applied to the component.
 * @param onPayClick A callback that is invoked when the pay button is clicked.
 */
@OptIn(ExperimentalTime::class)
@Composable
fun RepaymentScheduleItem(
    period: Periods,
    currencyCode: String?,
    maxDigits: Int?,
    modifier: Modifier = Modifier,
    onPayClick: () -> Unit = {},
) {
    val isPaid = period.complete == true
    val dueDateMillis = DateHelper.getDateAsLongFromList(period.dueDate)
    val dueDate = DateHelper.getDateAsString(period.dueDate)
    val amount = CurrencyFormatter.format(period.totalDueForPeriod, currencyCode, maxDigits)

    val todayMillis = Clock.System.now().toEpochMilliseconds()
    val canPay = !isPaid && (dueDateMillis?.let { it <= todayMillis } ?: false)

    MifosCustomCard(
        variant = CardVariant.OUTLINED,
        modifier = modifier
            .fillMaxWidth()
            .border(
                DesignToken.strokes.thin,
                KptTheme.colorScheme.secondaryContainer,
                KptTheme.shapes.medium,
            ),
    ) {
        Row(
            modifier = Modifier
                .padding(KptTheme.spacing.md)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .size(DesignToken.sizes.iconExtraLarge)
                    .clip(CircleShape)
                    .background(KptTheme.colorScheme.primary),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = period.period?.toString() ?: "-",
                    color = Color.White,
                    style = MifosTypography.labelMedium,
                )
            }

            Spacer(modifier = Modifier.width(DesignToken.spacing.medium))
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stringResource(
                        Res.string.feature_loan_installment_number,
                        period.period?.let {
                            "$it${if (it % 100 in 11..13) {
                                "th"
                            } else {
                                when (it % 10) {
                                    1 -> "st"
                                    2 -> "nd"
                                    3 -> "rd"
                                    else -> "th"
                                }
                            }}"
                        } ?: "-",
                    ),
                    color = KptTheme.colorScheme.outline,
                    style = MifosTypography.labelMediumEmphasized,
                )

                Text(
                    text = dueDate,
                    style = MifosTypography.labelLargeEmphasized,
                    color = KptTheme.colorScheme.onSurface,
                )
            }
            if (canPay) {
                MifosButton(
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(DesignToken.sizes.buttonHeight),
                    onClick = onPayClick,
                    text = {
                        Text(
                            text = stringResource(Res.string.feature_loan_repayment_pay, amount),
                            style = MifosTypography.titleMedium,
                        )
                    },
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.wrapContentWidth(),
                ) {
                    Text(
                        text = if (isPaid) {
                            stringResource(Res.string.feature_loan_paid)
                        } else {
                            stringResource(Res.string.feature_loan_due)
                        },
                        style = MifosTypography.labelSmall.copy(
                            color = if (isPaid) AppColors.customEnable else KptTheme.colorScheme.error,
                        ),
                    )
                    Text(
                        text = amount,
                        style = MifosTypography.titleSmallEmphasized,
                        color = if (isPaid) AppColors.customEnable else KptTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}
