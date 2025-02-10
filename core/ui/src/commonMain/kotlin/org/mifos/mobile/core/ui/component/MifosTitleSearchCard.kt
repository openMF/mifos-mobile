/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.core_common_working
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.mifos.mobile.core.designsystem.component.MifosSearchTextField
import org.mifos.mobile.core.designsystem.icon.MifosIcons
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.utils.DevicePreview

@Composable
fun MifosTitleSearchCard(
    titleResourceId: StringResource,
    searchQuery: (String) -> Unit,
    onSearchDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isSearching by rememberSaveable { mutableStateOf(false) }
    var searchedQuery by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(""),
        )
    }

    if (!isSearching) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(titleResourceId),
                color = MaterialTheme.colorScheme.onSurface,
                style = TextStyle(fontSize = 24.sp),
                modifier = Modifier.weight(1f),
                maxLines = 1,
            )

            IconButton(onClick = { isSearching = true }) {
                Icon(
                    imageVector = MifosIcons.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    } else {
        Row(
            modifier = Modifier
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MifosSearchTextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchedQuery,
                onValueChange = {
                    searchedQuery = it
                    searchQuery(it.text)
                },
                onSearchDismiss = {
                    searchedQuery = TextFieldValue("")
                    isSearching = false
                    onSearchDismiss.invoke()
                },
            )
        }
    }
}

@DevicePreview
@Composable
fun MifosTitleSearchCardPreview(
    modifier: Modifier = Modifier,
) {
    MifosMobileTheme {
        MifosTitleSearchCard(
            titleResourceId = Res.string.core_common_working,
            searchQuery = {},
            onSearchDismiss = {},
            modifier = modifier,
        )
    }
}
