/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.account.clientAccount.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mifos_mobile.feature.account.generated.resources.Res
import mifos_mobile.feature.account.generated.resources.feature_account_cancel
import mifos_mobile.feature.account.generated.resources.feature_account_clear_filters
import mifos_mobile.feature.account.generated.resources.feature_account_filter
import mifos_mobile.feature.account.generated.resources.feature_account_select_you_want
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.component.MifosTextButton
import org.mifos.mobile.feature.account.utils.CheckboxStatus

@Composable
internal fun ClientAccountFilterDialog(
    title: String,
    filterList: List<CheckboxStatus>,
    cancelDialog: () -> Unit,
    clearFilter: () -> Unit,
    updateFilterList: (checkBoxList: List<CheckboxStatus>) -> Unit,
    modifier: Modifier = Modifier,
) {
    var checkBoxList: List<CheckboxStatus> = filterList

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

                ClientAccountFilterCheckBox(
                    accountStatusList = filterList,
                    updateList = { checkBoxList = it },
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    MifosTextButton(
                        onClick = clearFilter,
                        text = { stringResource(Res.string.feature_account_clear_filters) },
                    )

                    Row {
                        MifosTextButton(
                            onClick = cancelDialog,
                            text = { stringResource(Res.string.feature_account_cancel) },
                        )
                        MifosTextButton(
                            onClick = { updateFilterList(checkBoxList) },
                            text = { stringResource(Res.string.feature_account_filter) },
                        )
                    }
                }
            }
        },
        confirmButton = {},
    )
}

@Composable
private fun ClientAccountFilterCheckBox(
    accountStatusList: List<CheckboxStatus>,
    updateList: (List<CheckboxStatus>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyColumnState = rememberLazyListState()

    var checkBoxList by rememberSaveable { mutableStateOf(accountStatusList) }

    LazyColumn(
        state = lazyColumnState,
        modifier = modifier,
    ) {
        items(checkBoxList.size) { index ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Checkbox(
                    checked = checkBoxList[index].isChecked,
                    onCheckedChange = {
                        val updatedList = checkBoxList.toMutableList()
                        updatedList[index] = checkBoxList[index].copy(isChecked = it)
                        checkBoxList = updatedList
                        updateList.invoke(checkBoxList)
                    },
                    colors = CheckboxColors(
                        checkedBoxColor = checkBoxList[index].color,
                        uncheckedBoxColor = MaterialTheme.colorScheme.surfaceVariant,
                        checkedCheckmarkColor = MaterialTheme.colorScheme.onPrimary,
                        uncheckedCheckmarkColor = MaterialTheme.colorScheme.onSurface,
                        checkedBorderColor = checkBoxList[index].color,
                        uncheckedBorderColor = checkBoxList[index].color,
                        disabledBorderColor = MaterialTheme.colorScheme.onSurface,
                        disabledIndeterminateBorderColor = MaterialTheme.colorScheme.onSurface,
                        disabledCheckedBoxColor = MaterialTheme.colorScheme.onSurface,
                        disabledUncheckedBoxColor = MaterialTheme.colorScheme.onSurface,
                        disabledIndeterminateBoxColor = MaterialTheme.colorScheme.onSurface,
                        disabledUncheckedBorderColor = MaterialTheme.colorScheme.onSurface,
                    ),
                )
                Text(
                    text = stringResource(resource = checkBoxList[index].status!!),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
