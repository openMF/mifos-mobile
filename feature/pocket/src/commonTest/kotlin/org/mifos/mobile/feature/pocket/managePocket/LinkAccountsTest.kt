/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.pocket.managePocket

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.ui.utils.ScreenUiState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
internal class LinkAccountsTest {

    @Test
    fun givenLinkAccountsState_thenAvailableAccountsAreDisplayed() = runComposeUiTest {
        val state = ManagePocketState(
            dialogState = ManagePocketDialogState.LinkAccounts,
            availableAccounts = listOf(
                AvailablePocketAccount(
                    accountId = 101L,
                    name = "New Savings",
                    accountNumber = "1234567890",
                    accountType = AccountType.SAVINGS,
                ),
            ),
            selectedTab = AccountType.SAVINGS,
            uiState = ScreenUiState.Success,
        )

        setContent { LinkAccountsSheet(state = state, onAction = {}) }

        onNodeWithText("New Savings").assertIsDisplayed()
        onNodeWithText("1234567890").assertIsDisplayed()
    }

    @Test
    fun whenAvailableAccountIsClicked_thenSelectionActionIsEmitted() = runComposeUiTest {
        var emittedAction: ManagePocketAction? = null
        val state = ManagePocketState(
            dialogState = ManagePocketDialogState.LinkAccounts,
            availableAccounts = listOf(
                AvailablePocketAccount(
                    accountId = 101L,
                    name = "New Savings",
                    accountNumber = "1234567890",
                    accountType = AccountType.SAVINGS,
                ),
            ),
            selectedTab = AccountType.SAVINGS,
            uiState = ScreenUiState.Success,
        )

        setContent { LinkAccountsSheet(state = state, onAction = { emittedAction = it }) }

        onNodeWithText("New Savings").performClick()

        assertTrue(emittedAction is ManagePocketAction.AccountSelectionChanged)
        assertEquals(
            ManagePocketAction.AccountSelectionChanged(accountId = 101L, selected = true),
            emittedAction,
        )
    }

    @Test
    fun whenSelectedAccountsAreSubmitted_thenLinkActionIsEmitted() = runComposeUiTest {
        var emittedAction: ManagePocketAction? = null
        val state = ManagePocketState(
            dialogState = ManagePocketDialogState.LinkAccounts,
            availableAccounts = listOf(
                AvailablePocketAccount(
                    accountId = 101L,
                    name = "New Savings",
                    accountNumber = "1234567890",
                    accountType = AccountType.SAVINGS,
                ),
            ),
            selectedTab = AccountType.SAVINGS,
            selectedAccountIds = setOf(101L),
            uiState = ScreenUiState.Success,
        )

        setContent { LinkAccountsSheet(state = state, onAction = { emittedAction = it }) }

        onNodeWithText("Link Selected (1)").performClick()

        assertEquals(ManagePocketAction.LinkSelectedAccounts, emittedAction)
    }
}
