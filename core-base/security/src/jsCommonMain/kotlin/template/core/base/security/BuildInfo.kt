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
 * JS/WasmJS release build detection.
 *
 * Covers both JS and WasmJS targets via the project's hierarchy template
 * (`jsCommon` includes `withJs()` + `withWasmJs()`).
 *
 * Defaults to `true` (release mode) for browser builds since Node.js
 * `NODE_ENV` detection is unreliable in browser contexts.
 */
actual fun isReleaseBuild(): Boolean = true
