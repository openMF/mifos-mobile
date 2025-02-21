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
