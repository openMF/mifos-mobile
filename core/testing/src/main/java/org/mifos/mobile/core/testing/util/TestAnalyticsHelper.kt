/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.testing.util

import org.mifos.mobile.core.logs.AnalyticsEvent
import org.mifos.mobile.core.logs.AnalyticsHelper

class TestAnalyticsHelper : AnalyticsHelper {

    private val events = mutableListOf<AnalyticsEvent>()

    override fun logEvent(event: AnalyticsEvent) {
        events.add(event)
    }

    fun hasLogged(event: AnalyticsEvent) = event in events
}
