/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.home.screens

import android.graphics.Bitmap
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.designsystem.theme.MifosMobileTheme
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.utils.DevicePreviews
import org.mifos.mobile.feature.home.R
import org.mifos.mobile.feature.home.navigation.HomeDestinations
import org.mifos.mobile.feature.home.utils.HomeState
import org.mifos.mobile.feature.home.utils.HomeUiState
import org.mifos.mobile.feature.home.viewmodel.HomeCardItem
import org.mifos.mobile.feature.home.viewmodel.HomeViewModel

@Composable
internal fun HomeScreen(
    callHelpline: () -> Unit,
    mailHelpline: () -> Unit,
    onNavigate: (HomeDestinations) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val notificationCount by viewModel.notificationsCount.collectAsStateWithLifecycle()

    HomeScreen(
        homeUiState = homeUiState,
        notificationCount = notificationCount,
        userProfile = { onNavigate(HomeDestinations.PROFILE) },
        totalSavings = { onNavigate(HomeDestinations.SAVINGS_ACCOUNT) },
        totalLoan = { onNavigate(HomeDestinations.LOAN_ACCOUNT) },
        callHelpline = callHelpline,
        mailHelpline = mailHelpline,
        homeCards = viewModel.getHomeCardItems(),
        openNotifications = { onNavigate(HomeDestinations.NOTIFICATIONS) },
        onNavigate = onNavigate,
        modifier = modifier,
    )
}

@Composable
private fun HomeScreen(
    homeUiState: HomeUiState,
    notificationCount: Int,
    userProfile: () -> Unit,
    totalSavings: () -> Unit,
    totalLoan: () -> Unit,
    callHelpline: () -> Unit,
    mailHelpline: () -> Unit,
    onNavigate: (HomeDestinations) -> Unit,
    homeCards: List<HomeCardItem>,
    openNotifications: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (homeUiState) {
        is HomeUiState.Success -> {
            HomeContent(
                username = homeUiState.homeState.username ?: "",
                totalLoanAmount = homeUiState.homeState.loanAmount,
                totalSavingsAmount = homeUiState.homeState.savingsAmount,
                userBitmap = homeUiState.homeState.image,
                notificationCount = notificationCount,
                homeCards = homeCards,
                userProfile = userProfile,
                totalSavings = totalSavings,
                totalLoan = totalLoan,
                callHelpline = callHelpline,
                mailHelpline = mailHelpline,
                openNotifications = openNotifications,
                onNavigate = onNavigate,
                modifier = modifier,
            )
        }

        is HomeUiState.Loading -> {
            MifosProgressIndicator(modifier = Modifier.fillMaxSize())
        }

        is HomeUiState.Error -> {
            EmptyDataView(icon = R.drawable.ic_error_black_24dp, error = homeUiState.errorMessage)
        }
    }
}

@DevicePreviews
@Composable
private fun HomeScreenPreview() {
    val homeState = HomeState(
        username = "",
        savingsAmount = 34.43,
        loanAmount = 34.45,
        image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888),
    )
    MifosMobileTheme {
        HomeScreen(
            homeUiState = HomeUiState.Success(homeState),
            callHelpline = {},
            mailHelpline = {},
            totalSavings = {},
            totalLoan = {},
            userProfile = {},
            onNavigate = {},
            homeCards = listOf(),
            notificationCount = 0,
            openNotifications = {},
        )
    }
}
