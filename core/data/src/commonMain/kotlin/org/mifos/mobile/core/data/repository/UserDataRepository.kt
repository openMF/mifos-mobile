/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.datastore.model.AppSettings
import org.mifos.mobile.core.model.AuthState
import org.mifos.mobile.core.model.UserData

interface UserDataRepository {

    val activeUserId: Long?

    // TODO
    /**
     * Stream of [UserData]
     */
    val userData: Flow<DataState<UserData>>

    val authState: StateFlow<AuthState>

    val settingsState: StateFlow<AppSettings>

    suspend fun logOut(): DataState<String>
}
