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
 * Android BiometricPrompt integration.
 *
 * Full BiometricPrompt wiring requires an Activity reference which is not
 * available at the core-base level. This stub provides the expect/actual
 * contract — consumer apps wire it with their Activity in the app module.
 */
actual class BiometricAuthenticator actual constructor() {

    actual fun isAvailable(): Boolean {
        // Requires PackageManager.FEATURE_FINGERPRINT or BiometricManager check.
        // Consumer apps should inject their own check.
        return false
    }

    actual suspend fun authenticate(reason: String): BiometricResult {
        // Consumer apps override with BiometricPrompt integration.
        return BiometricResult.Unavailable
    }
}
