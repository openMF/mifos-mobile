/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network

import org.mifos.mobile.core.network.services.ClientService
import org.mifos.mobile.core.network.services.PocketService
import org.mifos.mobile.core.network.services.ShareAccountService

interface PocketDataManager {
    val clientsApi: ClientService
    val pocketApi: PocketService
    val shareAccountApi: ShareAccountService
}
