/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.testing.fake

import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.TransferRepository
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.model.enums.TransferType

/**
 * Fake implementation of [TransferRepository] for testing.
 *
 * Usage:
 * ```kotlin
 * val fakeRepo = FakeTransferRepository()
 *
 * // Set success response
 * fakeRepo.setTransferResult(DataState.Success("Transfer successful"))
 *
 * // Set error response
 * fakeRepo.setTransferResult(DataState.Error(Exception("Insufficient funds")))
 *
 * // Use in tests
 * val viewModel = TransferViewModel(fakeRepo)
 * ```
 */
class FakeTransferRepository : TransferRepository {

    private var transferResult: DataState<String> = DataState.Success("Transfer successful")

    // Track method calls for verification
    var transferCallCount = 0
        private set
    var lastTransferPayload: TransferPayload? = null
        private set
    var lastTransferType: TransferType? = null
        private set

    fun setTransferResult(result: DataState<String>) {
        transferResult = result
    }

    fun reset() {
        transferResult = DataState.Success("Transfer successful")
        transferCallCount = 0
        lastTransferPayload = null
        lastTransferType = null
    }

    override suspend fun makeTransfer(
        payload: TransferPayload,
        transferType: TransferType?,
    ): DataState<String> {
        transferCallCount++
        lastTransferPayload = payload
        lastTransferType = transferType
        return transferResult
    }
}
