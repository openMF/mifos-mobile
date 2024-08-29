/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-wallet/blob/master/LICENSE.md
 */
package org.mifos.mifospay.lint.util

import com.android.tools.lint.client.api.Configuration
import com.android.tools.lint.detector.api.StringOption

open class StringSetLintOption(private val option: StringOption) : LintOption {
    var value: Set<String> = emptySet()
        private set

    override fun load(configuration: Configuration) {
        value = option.loadAsSet(configuration)
    }
}
