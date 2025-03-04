/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.accounts.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.accounts.generated.resources.Res
import mifos_mobile.feature.accounts.generated.resources.feature_account_cancel
import mifos_mobile.feature.accounts.generated.resources.feature_account_clear_filters
import mifos_mobile.feature.accounts.generated.resources.feature_account_filter
import mifos_mobile.feature.accounts.generated.resources.feature_account_select_you_want
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.component.MifosTextButton
import org.mifos.mobile.feature.accounts.model.CheckboxStatus

@Composable
internal fun AccountFilterDialog(
    title: String,
    checkboxOptions: List<CheckboxStatus>,
    cancelDialog: () -> Unit,
    clearFilter: () -> Unit,
    updateCheckboxList: (checkBoxList: List<CheckboxStatus>) -> Unit,
    modifier: Modifier = Modifier,
) {
    var checkBoxList by remember { mutableStateOf(checkboxOptions) }

    AlertDialog(
        onDismissRequest = cancelDialog,
        modifier = modifier,
        text = {
            Column {
                Text(modifier = Modifier.padding(bottom = 8.dp), text = "Filter $title")

                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = stringResource(Res.string.feature_account_select_you_want),
                )

                AccountFilterCheckBox(
                    checkboxOptions = checkboxOptions,
                    updateCheckboxList = { checkBoxList = it },
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    MifosTextButton(
                        onClick = clearFilter,
                        text = {
                            Text(text = stringResource(Res.string.feature_account_clear_filters))
                        },
                    )

                    Row {
                        MifosTextButton(
                            onClick = cancelDialog,
                            text = {
                                Text(text = stringResource(Res.string.feature_account_cancel))
                            },
                        )
                        MifosTextButton(
                            onClick = { updateCheckboxList(checkBoxList) },
                            text = {
                                Text(text = stringResource(Res.string.feature_account_filter))
                            },
                        )
                    }
                }
            }
        },
        confirmButton = {},
    )
}
