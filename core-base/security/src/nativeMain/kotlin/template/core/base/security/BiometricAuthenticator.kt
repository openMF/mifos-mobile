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

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSError
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthenticationWithBiometrics
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalForeignApi::class)
actual class BiometricAuthenticator actual constructor() {

    actual fun isAvailable(): Boolean {
        val context = LAContext()
        return context.canEvaluatePolicy(
            LAPolicyDeviceOwnerAuthenticationWithBiometrics,
            error = null,
        )
    }

    actual suspend fun authenticate(reason: String): BiometricResult {
        if (!isAvailable()) return BiometricResult.Unavailable

        return suspendCoroutine { continuation ->
            val context = LAContext()
            context.evaluatePolicy(
                LAPolicyDeviceOwnerAuthenticationWithBiometrics,
                localizedReason = reason,
            ) { success: Boolean, error: NSError? ->
                val result = when {
                    success -> BiometricResult.Success
                    error?.code == -2L -> BiometricResult.Cancelled // LAError.userCancel
                    else -> BiometricResult.Failure(error?.localizedDescription ?: "Unknown error")
                }
                continuation.resume(result)
            }
        }
    }
}
