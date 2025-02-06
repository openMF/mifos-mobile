/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.about

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.model.enums.AboutUsListItemId

internal data class AboutUsItem(
    val title: StringResource,
    val subtitle: StringResource? = null,
    val iconUrl: DrawableResource? = null,
    val itemId: AboutUsListItemId,
)
