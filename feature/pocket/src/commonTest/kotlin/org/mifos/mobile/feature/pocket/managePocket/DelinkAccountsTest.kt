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
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.ui.utils.ScreenUiState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
internal class DelinkAccountsTest {

    private fun linkedAccount() = ManagePocketAccount(
        accountId = 1L,
        name = "Vacation Savings",
        accountNumber = "9988776655",
        accountType = AccountType.SAVINGS,
        mappingId = 1L,
    )

    @Test
    fun givenLinkedAccount_whenRemoveIsClicked_thenDelinkConfirmationIsOpened() = runComposeUiTest {
        var emittedAction: ManagePocketAction? = null

        setContent {
            ManagePocketContent(
                state = ManagePocketState(
                    linkedAccounts = listOf(linkedAccount()),
                    uiState = ScreenUiState.Success,
                ),
                onAction = { emittedAction = it },
            )
        }

        onNodeWithContentDescription("Remove").performClick()

        assertTrue(emittedAction is ManagePocketAction.OpenDelinkConfirmation)
        assertEquals(
            linkedAccount(),
            (emittedAction as ManagePocketAction.OpenDelinkConfirmation).account,
        )
    }

    @Test
    fun givenDelinkConfirmationState_whenRendering_thenAccountDetailsAndActionsAreDisplayed() =
        runComposeUiTest {
            val account = linkedAccount()

            setContent {
                RemoveLinkedAccountSheet(
                    account = account,
                    onCancelClick = {},
                    onRemoveClick = {},
                )
            }

            onNodeWithText("Remove linked account").assertIsDisplayed()
            onNodeWithText("Vacation Savings").assertIsDisplayed()
            onNodeWithText("A/c No: 9988776655").assertIsDisplayed()
            onNodeWithText(
                "This will only remove it from Pocket. You can still access the account from the main accounts list.",
            ).assertIsDisplayed()
            onNodeWithText("Cancel").assertIsDisplayed()
            onNodeWithText("Remove").assertIsDisplayed()
        }

    @Test
    fun whenRemoveIsConfirmed_thenDelinkAccountActionIsEmitted() = runComposeUiTest {
        val account = linkedAccount()
        var emittedAction: ManagePocketAction? = null

        setContent {
            RemoveLinkedAccountSheet(
                account = account,
                onCancelClick = { emittedAction = ManagePocketAction.DismissDialog },
                onRemoveClick = { emittedAction = ManagePocketAction.DelinkAccount(account) },
            )
        }

        onNodeWithText("Remove").performClick()

        assertTrue(emittedAction is ManagePocketAction.DelinkAccount)
        assertEquals(account, (emittedAction as ManagePocketAction.DelinkAccount).account)
    }

    @Test
    fun whenDelinkConfirmationIsCancelled_thenDismissDialogActionIsEmitted() = runComposeUiTest {
        var emittedAction: ManagePocketAction? = null
        val account = linkedAccount()

        setContent {
            RemoveLinkedAccountSheet(
                account = account,
                onCancelClick = { emittedAction = ManagePocketAction.DismissDialog },
                onRemoveClick = { emittedAction = ManagePocketAction.DelinkAccount(account) },
            )
        }

        onNodeWithText("Cancel").performClick()

        assertTrue(emittedAction is ManagePocketAction.DismissDialog)
    }
}
