/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repositoryImpl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.MifosException
import org.mifos.mobile.core.common.asDataStateFlow
import org.mifos.mobile.core.common.safeDataStateCall
import org.mifos.mobile.core.data.util.toMifosExceptionSuspend

abstract class BaseRepository(
    protected val ioDispatcher: CoroutineDispatcher,
) {
    protected val exceptionMapper: suspend (Throwable) -> MifosException = {
        it.toMifosExceptionSuspend()
    }

    protected suspend fun <T> safeCall(
        block: suspend () -> T,
    ): DataState<T> = safeDataStateCall(
        dispatcher = ioDispatcher,
        exceptionMapper = exceptionMapper,
        block = block,
    )

    protected fun <T> Flow<T>.asDataState(): Flow<DataState<T>> =
        asDataStateFlow(exceptionMapper).flowOn(ioDispatcher)
}
