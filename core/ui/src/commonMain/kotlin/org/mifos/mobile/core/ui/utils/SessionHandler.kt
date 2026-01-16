/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.core.common.SessionManager

@Composable
fun SessionHandler(
    sessionManager: SessionManager,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val isExpired by sessionManager.isExpired.collectAsStateWithLifecycle()

    Box(
        modifier = modifier.pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent(pass = PointerEventPass.Initial)

                    if (event.changes.any { it.changedToDown() }) {
                        if (isExpired) {
                            event.changes.forEach { it.consume() }
                        } else {
                            sessionManager.userInteracted()
                        }
                    }
                }
            }
        }.onPreviewKeyEvent { event ->
            if (event.type == KeyEventType.KeyUp) {
                if (isExpired) {
                    return@onPreviewKeyEvent true
                } else {
                    sessionManager.userInteracted()
                }
            }
            false
        },
    ) {
        content()
    }
}
