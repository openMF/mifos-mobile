/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.common

expect object LocalizedDateFormatter {
    /**
     * Formats the full date. Ensure implementation uses (year, month, day) order
     * for platform-specific date objects to avoid crashes.
     */
    fun formatFullDate(year: Int, month: Int, day: Int): String

    /**
     * Returns "Today", "Yesterday", or the localized equivalent.
     */
    fun getRelativePrefix(day: Int, month: Int, year: Int): String?
}
