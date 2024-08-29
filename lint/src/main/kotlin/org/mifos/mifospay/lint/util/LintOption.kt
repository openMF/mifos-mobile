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

/**
 * A layer of indirection for implementations of option loaders without needing to extend from
 * Detector. This goes along with [OptionLoadingDetector].
 */
interface LintOption {
    fun load(configuration: Configuration)
}
