/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.designsystem.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState

@Suppress("DEPRECATION")
@Composable
fun MifosTabPager(
    pagerState: PagerState,
    currentPage: Int,
    tabs: List<String>,
    setCurrentPage: (Int) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit,
) {
    Column(modifier = modifier) {
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = currentPage,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.surfaceTint,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[currentPage])
                        .padding(start = 36.dp, end = 36.dp),
                    color = MaterialTheme.colorScheme.surfaceTint,
                )
            },
        ) {
            tabs.forEachIndexed { index, tabTitle ->
                Tab(
                    modifier = Modifier.padding(all = 16.dp),
                    selectedContentColor = MaterialTheme.colorScheme.surfaceTint,
                    unselectedContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    selected = currentPage == index,
                    onClick = { setCurrentPage(index) },
                ) {
                    Text(text = tabTitle)
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            count = tabs.size,
            modifier = Modifier.fillMaxWidth(),
            content = { page ->
                content(page)
            },
        )
    }
}
