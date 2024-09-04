/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.testing.util

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Used to debug the semantic tree.
 */
fun ComposeTestRule.dumpSemanticNodes() {
    this.onRoot().printToLog(tag = "MifosLog")
}

fun runTestWithLogging(
    context: CoroutineContext = EmptyCoroutineContext,
    timeout: Duration = 30.seconds,
    testBody: suspend TestScope.() -> Unit,
) = runTest(context, timeout) {
    runCatching {
        testBody()
    }.onFailure { exception ->
        exception.printStackTrace()
        throw exception
    }
}
