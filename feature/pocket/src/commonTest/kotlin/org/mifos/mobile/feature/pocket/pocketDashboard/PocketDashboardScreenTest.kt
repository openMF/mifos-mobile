/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.pocket.pocketDashboard

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.runComposeUiTest
import mifos_mobile.feature.pocket.generated.resources.Res
import mifos_mobile.feature.pocket.generated.resources.feature_pocket_error_load_accounts
import org.mifos.mobile.core.model.entity.pocket.AccountStatus
import org.mifos.mobile.core.ui.utils.ScreenUiState
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
internal class PocketDashboardScreenTest {

    @Test
    fun givenSuccessState_whenRendering_thenDashboardShowsBalanceAndAllAccountCategories() =
        runComposeUiTest {
            val state = PocketDashboardState(
                totalBalance = "MX$ 10,000.00",
                savingsAccounts = listOf(
                    DetailedPocket(
                        accountId = 1L,
                        name = "Emergency Fund",
                        accountNumber = "1004859238",
                        balanceOrStatus = "$ 5,000.00",
                        status = AccountStatus.ACTIVE,
                    ),
                ),
                loanAccounts = listOf(
                    DetailedPocket(
                        accountId = 2L,
                        name = "Personal Loan",
                        accountNumber = "3009284756",
                        balanceOrStatus = "ACTIVE",
                        status = AccountStatus.ACTIVE,
                    ),
                ),
                shareAccounts = listOf(
                    DetailedPocket(
                        accountId = 3L,
                        name = "Company Shares",
                        accountNumber = "5001129384",
                        balanceOrStatus = "PENDING",
                        status = AccountStatus.PENDING,
                    ),
                ),
                uiState = ScreenUiState.Success,
            )

            setContent { PocketDashboardContent(state = state, onAction = {}) }

            onNodeWithText("MX$ 10,000.00").performScrollTo().assertIsDisplayed()
            onNodeWithText("Emergency Fund").performScrollTo().assertIsDisplayed()
            onNodeWithText("1004859238").performScrollTo().assertIsDisplayed()
            onNodeWithText("Personal Loan").performScrollTo().assertIsDisplayed()
            onNodeWithText("3009284756").performScrollTo().assertIsDisplayed()
            onNodeWithText("Company Shares").performScrollTo().assertIsDisplayed()
            onNodeWithText("5001129384").performScrollTo().assertIsDisplayed()
        }

    @Test
    fun givenSuccessState_whenManageIsClicked_thenManageActionIsEmitted() = runComposeUiTest {
        var emittedAction: PocketDashboardAction? = null

        setContent {
            PocketDashboardContent(
                state = dashboardState(),
                onAction = { emittedAction = it },
            )
        }

        onNodeWithText("Manage").performClick()

        assertEquals(PocketDashboardAction.ManagePocket, emittedAction)
    }

    @Test
    fun givenEmptyState_whenLinkFirstAccountIsClicked_thenLinkActionIsEmitted() = runComposeUiTest {
        var emittedAction: PocketDashboardAction? = null

        setContent {
            PocketDashboardContent(
                state = PocketDashboardState(uiState = ScreenUiState.Empty),
                onAction = { emittedAction = it },
            )
        }

        onNodeWithText("Your Pocket is Empty").assertIsDisplayed()
        onNodeWithText("Link Your First Account").performClick()

        assertEquals(PocketDashboardAction.LinkFirstAccount, emittedAction)
    }

    @Test
    fun givenErrorState_whenRendering_thenErrorMessageAndRetryActionAreShown() = runComposeUiTest {
        var emittedAction: PocketDashboardAction? = null

        setContent {
            PocketDashboardContent(
                state = PocketDashboardState(
                    uiState = ScreenUiState.Error(Res.string.feature_pocket_error_load_accounts),
                ),
                onAction = { emittedAction = it },
            )
        }

        onNodeWithText("Failed to load pocket accounts").assertIsDisplayed()
        onNodeWithText("Retry").performClick()

        assertEquals(PocketDashboardAction.Retry, emittedAction)
    }
}

private fun dashboardState() = PocketDashboardState(
    totalBalance = "$ 1,250.00",
    savingsAccounts = listOf(
        DetailedPocket(
            accountId = 1L,
            name = "Emergency Savings",
            accountNumber = "S-100",
            balanceOrStatus = "$ 250.00",
            status = AccountStatus.ACTIVE,
        ),
    ),
    loanAccounts = listOf(
        DetailedPocket(
            accountId = 2L,
            name = "Personal Loan",
            accountNumber = "L-200",
            balanceOrStatus = "PENDING",
            status = AccountStatus.PENDING,
        ),
    ),
    shareAccounts = listOf(
        DetailedPocket(
            accountId = 3L,
            name = "Member Shares",
            accountNumber = "SH-300",
            balanceOrStatus = "$ 50.00",
            status = AccountStatus.ACTIVE,
        ),
    ),
    uiState = ScreenUiState.Success,
)
