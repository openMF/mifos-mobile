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

actual class TamperDetector actual constructor() {

    actual fun isDeviceCompromised(): Boolean = false

    actual fun isDebuggerAttached(): Boolean = false

    actual fun isSignatureValid(): Boolean = true
}
