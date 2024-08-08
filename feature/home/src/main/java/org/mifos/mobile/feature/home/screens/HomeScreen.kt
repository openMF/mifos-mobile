package org.mifos.mobile.feature.home.screens

import android.graphics.Bitmap
import androidx.camera.core.processing.SurfaceProcessorNode.In
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.home.R
import org.mifos.mobile.feature.home.navigation.HomeDestinations
import org.mifos.mobile.feature.home.navigation.HomeNavigation
import org.mifos.mobile.feature.home.navigation.toDestination
import org.mifos.mobile.feature.home.utils.HomeState
import org.mifos.mobile.feature.home.utils.HomeUiState
import org.mifos.mobile.feature.home.viewmodel.HomeCardItem
import org.mifos.mobile.feature.home.viewmodel.HomeNavigationItems
import org.mifos.mobile.feature.home.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    callHelpline: () -> Unit,
    mailHelpline: () -> Unit,
    onNavigate: (HomeDestinations) -> Unit
) {
    val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val notificationCount by viewModel.notificationsCount.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadClientAccountDetails()
        viewModel.getUserDetails()
        viewModel.getUserImage()
    }

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
        onNavigate = onNavigate
    )
}

@Composable
fun HomeScreen(
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
                onNavigate = onNavigate
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

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
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