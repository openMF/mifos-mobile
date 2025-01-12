/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import org.koin.dsl.module
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.network.KtorfitClient
import org.mifos.mobile.core.network.ktorHttpClient
import org.mifos.mobile.core.network.utils.BaseURL
import org.mifos.mobile.core.network.utils.KtorInterceptor

val NetworkModule = module {

    single<HttpClient>(KtorClient) {
        val preferencesRepository = get<PreferencesHelper>()

        ktorHttpClient.config {
            install(Auth)
            install(KtorInterceptor) {
                getToken = { preferencesRepository.authToken }
            }
        }
    }

    single<KtorfitClient>(MifosClient) {
        KtorfitClient.builder()
            .httpClient(get(KtorBaseClient))
            .baseURL(BaseURL().url)
            .build()
    }

    single {
        DataManager(ktorfitClient = get(MifosClient))
    }
}
