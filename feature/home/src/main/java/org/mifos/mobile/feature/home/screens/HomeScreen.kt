package org.mifos.mobile.feature.home.screens

import android.graphics.Bitmap
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.mifos.mobile.core.ui.component.EmptyDataView
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.feature.home.R
import org.mifos.mobile.feature.home.utils.HomeState
import org.mifos.mobile.feature.home.utils.HomeUiState
import org.mifos.mobile.feature.home.viewmodel.HomeCardItem

@Composable
fun HomeScreen(
    homeUiState: HomeUiState,
    userProfile: () -> Unit,
    totalSavings: () -> Unit,
    totalLoan: () -> Unit,
    callHelpline: (String) -> Unit,
    mailHelpline: (String) -> Unit,
    homeCardClicked: (HomeCardItem) -> Unit,
    homeCards: List<HomeCardItem>
) {
    when (homeUiState) {
        is HomeUiState.Success -> {
            HomeContent(
                username = homeUiState.homeState.username ?: "",
                totalLoanAmount = homeUiState.homeState.loanAmount,
                totalSavingsAmount = homeUiState.homeState.savingsAmount,
                userBitmap = homeUiState.homeState.image,
                homeCards = homeCards,
                userProfile = userProfile,
                totalSavings = totalSavings,
                totalLoan = totalLoan,
                callHelpline = callHelpline,
                mailHelpline = mailHelpline,
                homeCardClicked = homeCardClicked
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
            homeCardClicked = {},
            homeCards = listOf()
        )
    }
}