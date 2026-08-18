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

@OptIn(ExperimentalTestApi::class)
internal class ManagePocketScreenTest {

    @Test
    fun givenSuccessState_withLinkedAccounts_thenAccountsAreDisplayed() = runComposeUiTest {
        val state = ManagePocketState(
            linkedAccounts = listOf(
                ManagePocketAccount(
                    accountId = 1L,
                    name = "Vacation Savings",
                    accountNumber = "9988776655",
                    accountType = AccountType.SAVINGS,
                    mappingId = 1L,
                ),
            ),
            uiState = ScreenUiState.Success,
        )

        setContent { ManagePocketContent(state = state, onAction = {}) }

        onNodeWithText("Vacation Savings").assertIsDisplayed()
        onNodeWithText("9988776655").assertIsDisplayed()
    }

    @Test
    fun givenSuccessState_whenLinkMoreAccountsIsClicked_thenLinkActionIsEmitted() = runComposeUiTest {
        var emittedAction: ManagePocketAction? = null

        setContent {
            ManagePocketContent(
                state = ManagePocketState(uiState = ScreenUiState.Success),
                onAction = { emittedAction = it },
            )
        }

        onNodeWithText("Link").performClick()

        assertEquals(ManagePocketAction.OpenLinkAccounts, emittedAction)
    }

    @Test
    fun givenSuccessState_withoutLinkedAccounts_thenEmptyMessageIsDisplayed() = runComposeUiTest {
        setContent {
            ManagePocketContent(
                state = ManagePocketState(uiState = ScreenUiState.Success),
                onAction = {},
            )
        }

        onNodeWithText("No accounts are linked to pocket").assertIsDisplayed()
    }
}
