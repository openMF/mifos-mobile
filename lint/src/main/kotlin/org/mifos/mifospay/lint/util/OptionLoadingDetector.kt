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

import com.android.tools.lint.detector.api.Context
import com.android.tools.lint.detector.api.Detector

/** A [Detector] that supports reading the given [options]. */
abstract class OptionLoadingDetector(vararg options: LintOption) : Detector() {

    private val options = options.toList()

    override fun beforeCheckRootProject(context: Context) {
        super.beforeCheckRootProject(context)
        val config = context.configuration
        options.forEach { it.load(config) }
    }
}
