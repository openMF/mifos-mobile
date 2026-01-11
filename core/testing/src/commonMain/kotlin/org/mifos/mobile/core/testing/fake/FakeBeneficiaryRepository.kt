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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryPayload
import org.mifos.mobile.core.model.entity.beneficiary.BeneficiaryUpdatePayload
import org.mifos.mobile.core.model.entity.templates.beneficiary.BeneficiaryTemplate

/**
 * Fake implementation of [BeneficiaryRepository] for testing.
 *
 * Usage:
 * ```kotlin
 * val fakeRepo = FakeBeneficiaryRepository()
 *
 * // Set beneficiary list
 * fakeRepo.setBeneficiaryList(DataState.Success(testBeneficiaries))
 *
 * // Set create result
 * fakeRepo.setCreateResult(DataState.Success("Beneficiary created"))
 *
 * // Use in tests
 * val viewModel = BeneficiaryViewModel(fakeRepo)
 * ```
 */
class FakeBeneficiaryRepository : BeneficiaryRepository {

    private val beneficiaryTemplateState = MutableStateFlow<DataState<BeneficiaryTemplate>>(
        DataState.Success(BeneficiaryTemplate()),
    )
    private val beneficiaryListState = MutableStateFlow<DataState<List<Beneficiary>>>(
        DataState.Success(emptyList()),
    )

    private var createResult: DataState<String> = DataState.Success("Beneficiary created")
    private var updateResult: DataState<String> = DataState.Success("Beneficiary updated")
    private var deleteResult: DataState<String> = DataState.Success("Beneficiary deleted")

    // Track method calls for verification
    var createCallCount = 0
        private set
    var updateCallCount = 0
        private set
    var deleteCallCount = 0
        private set
    var lastCreatedPayload: BeneficiaryPayload? = null
        private set
    var lastDeletedId: Long? = null
        private set

    fun setBeneficiaryTemplate(result: DataState<BeneficiaryTemplate>) {
        beneficiaryTemplateState.value = result
    }

    fun setBeneficiaryList(result: DataState<List<Beneficiary>>) {
        beneficiaryListState.value = result
    }

    fun emitBeneficiaryListLoading() {
        beneficiaryListState.value = DataState.Loading
    }

    fun emitBeneficiaryListSuccess(beneficiaries: List<Beneficiary>) {
        beneficiaryListState.value = DataState.Success(beneficiaries)
    }

    fun emitBeneficiaryListError(error: Throwable) {
        beneficiaryListState.value = DataState.Error(error)
    }

    fun setCreateResult(result: DataState<String>) {
        createResult = result
    }

    fun setUpdateResult(result: DataState<String>) {
        updateResult = result
    }

    fun setDeleteResult(result: DataState<String>) {
        deleteResult = result
    }

    fun reset() {
        beneficiaryTemplateState.value = DataState.Success(BeneficiaryTemplate())
        beneficiaryListState.value = DataState.Success(emptyList())
        createResult = DataState.Success("Beneficiary created")
        updateResult = DataState.Success("Beneficiary updated")
        deleteResult = DataState.Success("Beneficiary deleted")
        createCallCount = 0
        updateCallCount = 0
        deleteCallCount = 0
        lastCreatedPayload = null
        lastDeletedId = null
    }

    override fun beneficiaryTemplate(): Flow<DataState<BeneficiaryTemplate>> {
        return beneficiaryTemplateState.asStateFlow()
    }

    override suspend fun createBeneficiary(beneficiaryPayload: BeneficiaryPayload?): DataState<String> {
        createCallCount++
        lastCreatedPayload = beneficiaryPayload
        return createResult
    }

    override suspend fun updateBeneficiary(
        beneficiaryId: Long?,
        payload: BeneficiaryUpdatePayload?,
    ): DataState<String> {
        updateCallCount++
        return updateResult
    }

    override suspend fun deleteBeneficiary(beneficiaryId: Long?): DataState<String> {
        deleteCallCount++
        lastDeletedId = beneficiaryId
        return deleteResult
    }

    override fun beneficiaryList(): Flow<DataState<List<Beneficiary>>> {
        return beneficiaryListState.asStateFlow()
    }
}
