/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.security

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import co.touchlab.kermit.Logger
import org.koin.compose.koinInject

/**
 * Root security composable that auto-wires all runtime security behavior.
 *
 * Wrap your app content with this to get:
 * - Tamper detection at startup (logs warning, updates [SecurityState.isCompromised])
 * - Session timeout check on every app resume
 * - Session touch on every user interaction (pointer input)
 * - Biometric re-authentication when session expires (if available)
 * - [LocalSecurityState] provided to the entire composable tree
 *
 * This composable does NOT block UI, show lock screens, or navigate.
 * Consumer apps read [LocalSecurityState] to decide how to respond.
 *
 * Usage:
 * ```kotlin
 * @Composable
 * fun App() {
 *     SecurityGate {
 *         AppTheme { NavHost(...) }
 *     }
 * }
 * ```
 */
@Composable
fun SecurityGate(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val tamperDetector = koinInject<TamperDetector>()
    val sessionManager = koinInject<SessionManager>()
    val biometric = koinInject<BiometricAuthenticator>()
    val securityState = remember { SecurityState() }

    // Collect session state from SessionManager
    val isSessionActive by sessionManager.isSessionActive.collectAsState()
    securityState.isSessionActive = isSessionActive

    // Track whether the session was previously active to detect active→inactive transitions.
    // This prevents biometric prompts on cold start when no session has been started yet.
    val wasSessionActive = remember { mutableStateOf(false) }

    // One-time tamper check at startup
    LaunchedEffect(Unit) {
        val compromised = tamperDetector.isDeviceCompromised()
        securityState.isCompromised = compromised
        if (compromised) {
            Logger.w("SecurityGate") { "Device integrity check failed" }
        }
    }

    // Auto biometric re-auth when session transitions from active to inactive
    LaunchedEffect(isSessionActive) {
        if (wasSessionActive.value && !isSessionActive && biometric.isAvailable()) {
            val result = biometric.authenticate("Session expired. Verify identity.")
            if (result is BiometricResult.Success) {
                sessionManager.startSession()
            }
        }
        wasSessionActive.value = isSessionActive
    }

    // Session timeout check on lifecycle resume
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                sessionManager.checkTimeout()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // Auto-touch session on any pointer interaction
    Box(
        modifier = modifier.pointerInput(Unit) {
            awaitEachGesture {
                awaitFirstDown(pass = PointerEventPass.Initial)
                sessionManager.checkTimeout()
                sessionManager.touch()
            }
        },
    ) {
        CompositionLocalProvider(LocalSecurityState provides securityState) {
            content()
        }
    }
}
