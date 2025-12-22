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

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import org.mifos.mobile.core.common.Constants
import org.mifos.mobile.core.common.paging.GenericPagingSource
import org.mifos.mobile.core.common.paging.PageResult
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.model.entity.accounts.AccountUiModel
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.network.DataManager

class AccountsRepositoryImp(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
) : AccountsRepository {

    override fun loadAccounts(
        clientId: Long?,
        accountType: String?
    ): Flow<PagingData<AccountUiModel>> {

        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GenericPagingSource<Int, AccountUiModel> { page, size ->

                    val response = dataManager.clientsApi
                        .getAccounts(clientId!!, accountType)
                        .first()

                    val allAccounts = when (accountType) {
                        Constants.LOAN_ACCOUNTS ->
                            response.loanAccounts.map { AccountUiModel.Loan(it) }

                        Constants.SAVINGS_ACCOUNTS ->
                            (response.savingsAccounts ?: emptyList())
                                .map { AccountUiModel.Savings(it) }

                        Constants.SHARE_ACCOUNTS ->
                            response.shareAccounts.map { AccountUiModel.Share(it) }

                        else -> emptyList()
                    }

                    val currentPage = page ?: 1
                    val from = (currentPage - 1) * size
                    val to = minOf(from + size, allAccounts.size)

                    val pageItems =
                        if (from >= allAccounts.size) emptyList()
                        else allAccounts.subList(from, to)

                    PageResult(
                        items = pageItems,
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (to >= allAccounts.size) null else currentPage + 1
                    )
                }
            }
        ).flow
    }

}
