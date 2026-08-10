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

import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

interface StringProvider {
    suspend fun get(resource: StringResource, vararg formatArgs: Any = emptyArray()): String
}

class DefaultStringProvider : StringProvider {
    override suspend fun get(resource: StringResource, vararg formatArgs: Any): String {
        return getString(resource, *formatArgs)
    }
}
