/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.auth.accountChoice

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals

@Serializable
internal data object PreviousRoute

/** Supplies the lifecycle and view model store a NavHost needs, which the UI test host lacks. */
private class TestNavHostOwner : LifecycleOwner, ViewModelStoreOwner {
    override val lifecycle = LifecycleRegistry(this).apply { currentState = Lifecycle.State.RESUMED }
    override val viewModelStore = ViewModelStore()
}

@Composable
private fun WithNavHostOwner(content: @Composable () -> Unit) {
    val owner = remember { TestNavHostOwner() }
    CompositionLocalProvider(
        LocalLifecycleOwner provides owner,
        LocalViewModelStoreOwner provides owner,
        content = content,
    )
}

private const val PREVIOUS_SCREEN = "Previous screen"
private const val QUESTION = "Do you already have an active account on Mifos?"

@OptIn(ExperimentalTestApi::class)
internal class AccountChoiceScreenTest {

    @Test
    fun navigatingToRouteShowsScreenAndBackReturnsToPreviousScreen() = runComposeUiTest {
        lateinit var navController: NavHostController

        setContent {
            WithNavHostOwner {
                navController = rememberNavController()
                NavHost(navController = navController, startDestination = PreviousRoute) {
                    composable<PreviousRoute> { Text(PREVIOUS_SCREEN) }
                    accountChoiceDestination(onExistingCustomer = {}, onNewCustomer = {})
                }
            }
        }
        onNodeWithText(PREVIOUS_SCREEN).assertIsDisplayed()

        runOnUiThread { navController.navigateToAccountChoiceScreen() }
        onNodeWithText(QUESTION).assertIsDisplayed()

        runOnUiThread { navController.popBackStack() }
        onNodeWithText(PREVIOUS_SCREEN).assertIsDisplayed()
    }

    @Test
    fun answersFromDestinationInvokeGraphCallbacks() = runComposeUiTest {
        val answers = mutableListOf<String>()

        setContent {
            WithNavHostOwner {
                NavHost(navController = rememberNavController(), startDestination = AccountChoiceRoute) {
                    accountChoiceDestination(
                        onExistingCustomer = { answers += "existing" },
                        onNewCustomer = { answers += "new" },
                    )
                }
            }
        }

        onNodeWithText("Yes").performClick()
        onNodeWithText("No").performClick()

        assertEquals(listOf("existing", "new"), answers)
    }

    @Test
    fun questionAndBothAnswersAreDisplayed() = runComposeUiTest {
        setContent { AccountChoiceScreen(onExistingCustomer = {}, onNewCustomer = {}) }

        onNodeWithText("You have an account.").assertIsDisplayed()
        onNodeWithText(QUESTION).assertIsDisplayed()
        onNodeWithText("Yes").assertIsDisplayed()
        onNodeWithText("No").assertIsDisplayed()
    }

    @Test
    fun yesInvokesOnlyExistingCustomerCallback() = runComposeUiTest {
        var existingCustomerClicks = 0
        var newCustomerClicks = 0

        setContent {
            AccountChoiceScreen(
                onExistingCustomer = { existingCustomerClicks++ },
                onNewCustomer = { newCustomerClicks++ },
            )
        }

        onNodeWithText("Yes").performClick()

        assertEquals(1, existingCustomerClicks)
        assertEquals(0, newCustomerClicks)
    }

    @Test
    fun noInvokesOnlyNewCustomerCallback() = runComposeUiTest {
        var existingCustomerClicks = 0
        var newCustomerClicks = 0

        setContent {
            AccountChoiceScreen(
                onExistingCustomer = { existingCustomerClicks++ },
                onNewCustomer = { newCustomerClicks++ },
            )
        }

        onNodeWithText("No").performClick()

        assertEquals(0, existingCustomerClicks)
        assertEquals(1, newCustomerClicks)
    }
}
