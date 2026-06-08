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

import android.os.Debug

/**
 * Android release build detection using debugger attachment status.
 *
 * Uses [Debug.isDebuggerConnected] which requires no Android context,
 * making it safe to call at any initialization time including Koin
 * module loading. Debug builds typically have a debugger available;
 * release builds do not.
 */
actual fun isReleaseBuild(): Boolean = !Debug.isDebuggerConnected()
