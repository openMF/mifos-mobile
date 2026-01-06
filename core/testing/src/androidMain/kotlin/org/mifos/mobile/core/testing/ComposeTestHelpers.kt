/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.testing

/**
 * Compose UI Test Helper Documentation.
 *
 * This module provides TestTags and testing utilities. For Compose UI testing,
 * add the following dependencies to your module's build.gradle.kts:
 *
 * ```kotlin
 * androidInstrumentedTest.dependencies {
 *     implementation(libs.androidx.compose.ui.test)
 * }
 * ```
 *
 * Then use TestTags with ComposeContentTestRule:
 *
 * ```kotlin
 * import org.mifos.mobile.core.testing.util.TestTags
 *
 * class LoginScreenTest {
 *     @get:Rule
 *     val composeTestRule = createComposeRule()
 *
 *     @Test
 *     fun loginScreen_displaysAllElements() {
 *         composeTestRule.setContent {
 *             LoginScreen()
 *         }
 *         composeTestRule.onNodeWithTag(TestTags.Auth.LOGIN_BUTTON).assertIsDisplayed()
 *         composeTestRule.onNodeWithTag(TestTags.Auth.USERNAME_FIELD).assertIsDisplayed()
 *     }
 *
 *     @Test
 *     fun loginButton_disabledWhenCredentialsEmpty() {
 *         composeTestRule.setContent {
 *             LoginScreen(state = LoginState(username = "", password = ""))
 *         }
 *         composeTestRule.onNodeWithTag(TestTags.Auth.LOGIN_BUTTON).assertIsNotEnabled()
 *     }
 *
 *     @Test
 *     fun inputCredentials_enablesLoginButton() {
 *         composeTestRule.setContent {
 *             LoginScreen(state = LoginState(username = "user", password = "pass"))
 *         }
 *         composeTestRule.onNodeWithTag(TestTags.Auth.LOGIN_BUTTON).assertIsEnabled()
 *     }
 * }
 * ```
 *
 * For adding TestTags to Composables:
 *
 * ```kotlin
 * @Composable
 * fun LoginButton(onClick: () -> Unit) {
 *     Button(
 *         onClick = onClick,
 *         modifier = Modifier.testTag(TestTags.Auth.LOGIN_BUTTON)
 *     ) {
 *         Text("Login")
 *     }
 * }
 * ```
 */
object ComposeTestHelpers {
    /**
     * Common timeout for waiting for compose idle state in tests.
     */
    const val DEFAULT_TIMEOUT_MS = 5000L

    /**
     * Extended timeout for network operations in UI tests.
     */
    const val NETWORK_TIMEOUT_MS = 30000L
}
