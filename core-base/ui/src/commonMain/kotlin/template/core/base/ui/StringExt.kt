/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.ui

/**
 * Extension property that returns a string with the first letter of each word capitalized.
 * For example, "hello world" becomes "Hello World".
 */
val String.capitalizeEachWord: String
    get() = this.split(" ").joinToString(" ") { word ->
        word.takeIf { it.isNotEmpty() }
            ?.let { it.first().uppercase() + it.substring(1).lowercase() }
            ?: ""
    }
