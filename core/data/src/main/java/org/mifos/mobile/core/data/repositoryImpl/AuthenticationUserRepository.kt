/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mifos.mobile.core.data.repository.UserDataRepository
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.model.UserData
import javax.inject.Inject

class AuthenticationUserRepository @Inject constructor(
    private val preferencesHelper: PreferencesHelper,
) : UserDataRepository {

    override val userData: Flow<UserData> = flow {
        emit(
            UserData(
                isAuthenticated = !preferencesHelper.token.isNullOrEmpty(),
                userName = preferencesHelper.userName ?: "",
                clientId = preferencesHelper.clientId ?: 0,
            ),
        )
    }

    override fun logOut() {
        preferencesHelper.clear()
    }
}
