/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model

/**
 * Models high level auth state for the application.
 */
sealed class AuthState {

    /**
     * Auth state is unknown.
     */
    data object Uninitialized : AuthState()

    /**
     * User is unauthenticated. Said another way, the app has no access token.
     */
    data object Unauthenticated : AuthState()

    /**
     * User is authenticated with the given access token.
     */
    data class Authenticated(val accessToken: String) : AuthState()
}
