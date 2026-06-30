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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.asDataStateFlow
import org.mifos.mobile.core.data.mapper.accounts.toModel
import org.mifos.mobile.core.data.mapper.pocket.toAccountStatus
import org.mifos.mobile.core.data.mapper.pocket.toDomainList
import org.mifos.mobile.core.data.mapper.share.toModel
import org.mifos.mobile.core.data.repository.PocketRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.data.util.runAsDataState
import org.mifos.mobile.core.data.util.withNetworkCheck
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.model.entity.pocket.AccountStatus
import org.mifos.mobile.core.model.entity.pocket.DetailedPocketAccount
import org.mifos.mobile.core.model.entity.pocket.LinkableAccount
import org.mifos.mobile.core.model.entity.pocket.PocketAccount
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.network.dto.pocket.PocketDelinkRequest
import org.mifos.mobile.core.network.dto.pocket.PocketLinkRequest

class PocketRepositoryImp(
    private val dataManager: DataManager,
    private val networkMonitor: NetworkMonitor,
    private val ioDispatcher: CoroutineDispatcher,
) : PocketRepository {

    private val detailedPocketCache = MutableStateFlow<DataState<List<DetailedPocketAccount>>?>(null)

    private var cachedClientId: Long? = null

    private suspend fun fetchBasicPocketsFromNetwork(): List<PocketAccount> {
        return dataManager.pocketApi.getPocketAccounts().toDomainList()
    }

    override suspend fun getPocketAccounts(): DataState<List<PocketAccount>> {
        return runAsDataState(networkMonitor, ioDispatcher) {
            fetchBasicPocketsFromNetwork()
        }
    }

    private suspend fun syncPockets(clientId: Long, forceRefresh: Boolean = false) {
        if (cachedClientId != clientId) {
            detailedPocketCache.value = null
            cachedClientId = clientId
        }

        if (!forceRefresh && cachedClientId == clientId && detailedPocketCache.value is DataState.Loading) return
        if (!forceRefresh && cachedClientId == clientId && detailedPocketCache.value is DataState.Success) return

        detailedPocketCache.value = DataState.Loading

        detailedPocketCache.value = runAsDataState(networkMonitor, ioDispatcher) {
            val basicPockets = fetchBasicPocketsFromNetwork()
            val clientAccounts = dataManager.clientsApi.getClientAccounts(clientId).first().toModel()

            basicPockets.map { pocket ->
                addAccountDetails(pocket, clientAccounts)
            }
        }
    }

    private suspend fun addAccountDetails(
        pocket: PocketAccount,
        clientAccounts: ClientAccounts,
    ): DetailedPocketAccount {
        return when (pocket.accountType) {
            AccountType.LOAN -> {
                val detail = clientAccounts.loanAccounts.find { it.id == pocket.accountId }
                DetailedPocketAccount(
                    pocket = pocket,
                    balance = detail?.loanBalance,
                    productName = detail?.productName,
                    currencyCode = detail?.currency?.code,
                    decimalPlaces = detail?.currency?.decimalPlaces?.toInt(),
                    status = detail?.status?.toAccountStatus(),
                )
            }
            AccountType.SAVINGS -> {
                val detail = clientAccounts.savingsAccounts?.find { it.id == pocket.accountId }
                DetailedPocketAccount(
                    pocket = pocket,
                    balance = detail?.accountBalance,
                    productName = detail?.productName,
                    currencyCode = detail?.currency?.code,
                    decimalPlaces = detail?.currency?.decimalPlaces,
                    status = detail?.status?.toAccountStatus(),
                )
            }
            AccountType.SHARE -> {
                var balance = 0.0
                var productName: String?
                var currencyCode: String?
                var decimalPlaces: Int?
                var accountStatus: AccountStatus?

                try {
                    val shareAccountDetails = dataManager
                        .shareAccountApi
                        .getShareAccountDetails(pocket.accountId).first().toModel()

                    productName = shareAccountDetails.productName
                    currencyCode = shareAccountDetails.currency?.code
                    decimalPlaces = shareAccountDetails.currency?.decimalPlaces
                    accountStatus = shareAccountDetails.status?.toAccountStatus()

                    val approvedShares = shareAccountDetails.summary?.totalApprovedShares ?: 0
                    val currentMarketPrice = shareAccountDetails.currentMarketPrice ?: 0.0
                    balance = approvedShares * currentMarketPrice
                } catch (e: Exception) {
                    val detail = clientAccounts.shareAccounts.find { it.id == pocket.accountId }
                    productName = detail?.productName
                    currencyCode = detail?.currency?.code
                    decimalPlaces = detail?.currency?.decimalPlaces
                    accountStatus = detail?.status?.toAccountStatus()
                }

                DetailedPocketAccount(
                    pocket = pocket,
                    balance = balance,
                    productName = productName,
                    currencyCode = currencyCode,
                    decimalPlaces = decimalPlaces,
                    status = accountStatus,
                )
            }
        }
    }

    override fun getDetailedPocketAccounts(
        clientId: Long,
        forceRefresh: Boolean,
    ): Flow<DataState<List<DetailedPocketAccount>>> {
        return networkMonitor.withNetworkCheck(
            flow {
                syncPockets(clientId, forceRefresh)

                detailedPocketCache.collect { state ->
                    if (state != null) emit(state)
                }
            },
        ).flowOn(ioDispatcher)
    }

    override suspend fun linkAccounts(
        request: PocketLinkRequest,
        explicitlyAddedAccount: DetailedPocketAccount,
        clientId: Long,
    ): DataState<Unit> {
        return runAsDataState(networkMonitor, ioDispatcher) {
            dataManager.pocketApi.linkAccounts(request = request)

            val updatedBasicPockets = fetchBasicPocketsFromNetwork()

            val newlyGeneratedPocket = updatedBasicPockets.find {
                it.accountId == explicitlyAddedAccount.pocket.accountId
            }

            val currentState = detailedPocketCache.value
            if (newlyGeneratedPocket != null && currentState is DataState.Success) {
                val finalAccount = explicitlyAddedAccount.copy(pocket = newlyGeneratedPocket)

                val updatedList = currentState.data.toMutableList()
                updatedList.add(finalAccount)
                detailedPocketCache.value = DataState.Success(updatedList)
            } else {
                syncPockets(clientId = clientId, forceRefresh = true)
            }
        }
    }

    override suspend fun delinkAccounts(pocketAccountMappingIds: List<Long>, clientId: Long): DataState<Unit> {
        return runAsDataState(networkMonitor, ioDispatcher) {
            val request = PocketDelinkRequest(pocketAccountMappingIds)
            dataManager.pocketApi.delinkAccounts(request = request)

            val currentState = detailedPocketCache.value
            if (currentState is DataState.Success) {
                val updatedList = currentState.data.filter { it.pocket.id !in pocketAccountMappingIds }
                detailedPocketCache.value = DataState.Success(updatedList)
            } else {
                syncPockets(clientId = clientId, forceRefresh = true)
            }
        }
    }

    override fun getAvailableAccountsToLink(clientId: Long): Flow<DataState<List<LinkableAccount>>> {
        return networkMonitor.withNetworkCheck(
            flow {
                val clientAccounts = dataManager.clientsApi.getClientAccounts(clientId).first().toModel()
                val availableAccounts = mutableListOf<LinkableAccount>()

                val alreadyLinkedAccountIds = (detailedPocketCache.value as? DataState.Success)
                    ?.data?.map { it.pocket.accountId } ?: emptyList()

                clientAccounts.loanAccounts.forEach { loan ->
                    if (loan.id !in alreadyLinkedAccountIds) {
                        availableAccounts.add(
                            LinkableAccount(
                                accountId = loan.id,
                                productName = loan.productName,
                                accountNumber = loan.accountNo,
                                accountType = AccountType.LOAN,
                                balance = loan.loanBalance,
                                currencyCode = loan.currency?.code,
                                decimalPlaces = loan.currency?.decimalPlaces?.toInt(),
                                status = loan.status?.toAccountStatus(),
                            ),
                        )
                    }
                }

                clientAccounts.savingsAccounts?.forEach { savings ->
                    if (savings.id !in alreadyLinkedAccountIds) {
                        availableAccounts.add(
                            LinkableAccount(
                                accountId = savings.id,
                                productName = savings.productName,
                                accountNumber = savings.accountNo,
                                accountType = AccountType.SAVINGS,
                                balance = savings.accountBalance,
                                currencyCode = savings.currency?.code,
                                decimalPlaces = savings.currency?.decimalPlaces,
                                status = savings.status?.toAccountStatus(),
                            ),
                        )
                    }
                }

                clientAccounts.shareAccounts.forEach { share ->
                    if (share.id !in alreadyLinkedAccountIds) {
                        var balance = 0.0
                        var currencyCode: String? = share.currency?.code
                        var decimalPlaces: Int? = share.currency?.decimalPlaces

                        try {
                            val shareAccountDetails = dataManager.shareAccountApi
                                .getShareAccountDetails(share.id).first()
                            val approvedShares = shareAccountDetails.summary?.totalApprovedShares ?: 0
                            val currentMarketPrice = shareAccountDetails.currentMarketPrice ?: 0.0
                            balance = approvedShares * currentMarketPrice

                            currencyCode = shareAccountDetails.currency?.code ?: currencyCode
                            decimalPlaces = shareAccountDetails.currency?.decimalPlaces ?: decimalPlaces
                        } catch (e: Exception) {
                            // do nothing
                        }

                        availableAccounts.add(
                            LinkableAccount(
                                accountId = share.id,
                                productName = share.productName,
                                accountNumber = share.accountNo,
                                accountType = AccountType.SHARE,
                                balance = balance,
                                currencyCode = currencyCode,
                                decimalPlaces = decimalPlaces,
                                status = share.status?.toAccountStatus(),
                            ),
                        )
                    }
                }

                emit(availableAccounts)
            }.asDataStateFlow(),
        ).flowOn(ioDispatcher)
    }
}
