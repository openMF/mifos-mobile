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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.feature.accounts.model.CheckboxStatus

@Composable
internal fun AccountFilterCheckBox(
    checkboxOptions: List<CheckboxStatus>,
    updateCheckboxList: (List<CheckboxStatus>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyColumnState = rememberLazyListState()

    var checkBoxList by rememberSaveable { mutableStateOf(checkboxOptions) }

    LazyColumn(
        state = lazyColumnState,
        modifier = modifier,
    ) {
        items(
            items = checkBoxList,
            key = { it.statusLabel?.hashCode() ?: it.hashCode() },
        ) { checkBox ->
            AccountCheckBoxItem(
                checkboxStatus = checkBox,
                onCheckedChange = { isChecked ->
                    checkBoxList = checkBoxList.map {
                        if (it.statusLabel == checkBox.statusLabel) it.copy(isChecked = isChecked) else it
                    }
                    updateCheckboxList(checkBoxList)
                },
            )
        }
    }
}

@Composable
private fun AccountCheckBoxItem(
    checkboxStatus: CheckboxStatus,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Checkbox(
            checked = checkboxStatus.isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = checkboxStatus.statusLabel?.let { stringResource(it) } ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
