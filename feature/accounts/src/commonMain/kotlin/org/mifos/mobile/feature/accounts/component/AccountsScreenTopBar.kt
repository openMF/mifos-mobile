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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.mifos.mobile.core.designsystem.component.MifosSearchTextField
import org.mifos.mobile.core.designsystem.icon.MifosIcons

@Composable
internal fun AccountsScreenTopBar(
    navigateBack: () -> Unit,
    onChange: (String) -> Unit,
    openFilterDialog: () -> Unit,
    closeSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var isSearchActive by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = {
                if (isSearchActive) {
                    query = TextFieldValue("")
                    isSearchActive = false
                    closeSearch.invoke()
                } else {
                    navigateBack.invoke()
                }
            },
            modifier = Modifier.size(40.dp),
        ) {
            Icon(
                imageVector = MifosIcons.ArrowBack,
                contentDescription = "Back Arrow",
            )
        }

        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = "Accounts",
                style = MaterialTheme.typography.titleLarge,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = { isSearchActive = true },
                    modifier = Modifier.size(40.dp),
                ) {
                    Image(
                        imageVector = MifosIcons.Search,
                        contentDescription = "Add account",
                    )
                }
                IconButton(
                    onClick = openFilterDialog,
                    modifier = Modifier.size(40.dp),
                ) {
                    Image(
                        imageVector = MifosIcons.FilterList,
                        contentDescription = "Add account",
                    )
                }
            }

            if (isSearchActive) {
                MifosSearchTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        onChange(it.text)
                    },
                    modifier = Modifier
                        .padding(end = 40.dp)
                        .height(52.dp)
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.background),
                    onSearchDismiss = {
                        query = TextFieldValue("")
                        closeSearch.invoke()
                        isSearchActive = false
                    },
                )
            }
        }
    }
}
