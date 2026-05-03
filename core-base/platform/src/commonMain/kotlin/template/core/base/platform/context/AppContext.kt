/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.platform.context

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal

/**
 * Represents an abstract context for the application that provides platform-specific
 * functionality. This class must be implemented in each platform-specific source set.
 */
expect abstract class AppContext

/**
 * A composition local that provides the current [AppContext] to the composition tree.
 * This allows composable functions to access the platform-specific context without
 * explicit parameters.
 */
expect val LocalContext: ProvidableCompositionLocal<AppContext>

/**
 * The platform-specific activity or view controller associated with the current context.
 *
 * This property is accessible only from within a Composable function.
 * The return type is [Any] to support different platform-specific types
 * (Activity on Android, UIViewController on iOS, etc.).
 */
@get:Composable
expect val AppContext.activity: Any
