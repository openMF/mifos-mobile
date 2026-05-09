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

/**
 * Configurable security policy. Consumer apps can adjust thresholds
 * based on their risk profile.
 */
data class SecurityPolicy(
    val requireBiometricForSensitiveOps: Boolean = true,
    val lockAfterFailedAttempts: Int = 5,
    val wipeAfterFailedAttempts: Int = 10,
    val sessionTimeoutMinutes: Int = 30,
    val clipboardWipeSeconds: Int = 60,
) {
    companion object {
        fun default(): SecurityPolicy = SecurityPolicy()
    }
}
