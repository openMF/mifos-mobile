/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.common.manager

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainCoroutineDispatcher

interface DispatcherManager {
    /**
     * The default [CoroutineDispatcher] for the app.
     */
    val default: CoroutineDispatcher

    /**
     * The [MainCoroutineDispatcher] for the app.
     */
    val main: MainCoroutineDispatcher

    /**
     * The IO [CoroutineDispatcher] for the app.
     */
    val io: CoroutineDispatcher

    /**
     * The unconfined [CoroutineDispatcher] for the app.
     */
    val unconfined: CoroutineDispatcher

    val appScope: CoroutineScope
}
