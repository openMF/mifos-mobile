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

import platform.Foundation.NSProcessInfo

/**
 * iOS/Native release build detection.
 *
 * Checks for `DYLD_INSERT_LIBRARIES` environment variable which is
 * typically present when a debugger or instrumentation tool is attached.
 * Defaults to `true` (release = more restrictive) when no debug
 * indicators are found.
 */
actual fun isReleaseBuild(): Boolean {
    val env = NSProcessInfo.processInfo.environment
    return env["DYLD_INSERT_LIBRARIES"] == null
}
