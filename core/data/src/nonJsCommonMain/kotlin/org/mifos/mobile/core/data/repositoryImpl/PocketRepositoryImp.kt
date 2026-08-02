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
import org.mifos.mobile.core.data.mapper.pocket.toDomain
import org.mifos.mobile.core.data.mapper.pocket.toDomainList
import org.mifos.mobile.core.data.mapper.pocket.toEntity
import org.mifos.mobile.core.data.mapper.share.toModel
import org.mifos.mobile.core.data.repository.PocketRepository
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.data.util.runAsDataState
import org.mifos.mobile.core.data.util.withNetworkCheck
import org.mifos.mobile.core.database.dao.PocketAccountDao
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.model.entity.payload.PocketLinkPayload
import org.mifos.mobile.core.model.entity.pocket.AccountStatus
import org.mifos.mobile.core.model.entity.pocket.DetailedPocketAccount
import org.mifos.mobile.core.model.entity.pocket.LinkableAccount
import org.mifos.mobile.core.model.entity.pocket.PocketAccount
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.network.dto.pocket.PocketDelinkRequest
import org.mifos.mobile.core.network.dto.pocket.PocketLinkRequest
import kotlin.time.Clock

class PocketRepositoryImp(
    private val dataManager: DataManager,
    private val networkMonitor: NetworkMonitor,
    private val pocketAccountDao: PocketAccountDao,
    private val ioDispatcher: CoroutineDispatcher,
) : PocketRepository {

    private val detailedPocketCache = MutableStateFlow<DataState<List<DetailedPocketAccount>>?>(null)

    private var cachedClientId: Long? = null

    private suspend fun syncPocketsWithServer() {
        if (!networkMonitor.isOnline.first()) return

        try {
            val serverBasicPockets = dataManager.pocketApi.getPocketAccounts().toDomainList()
            var localBasicPockets = pocketAccountDao.getAllPocketAccounts().map { it.toDomain() }

            val accountsToLink = localBasicPockets.filter { local ->
                serverBasicPockets.none { it.accountId == local.accountId && it.accountType == local.accountType }
            }

            val accountsToDelink = serverBasicPockets.filter { server ->
                localBasicPockets.none { it.accountId == server.accountId && it.accountType == server.accountType }
            }

            if (accountsToDelink.isNotEmpty()) {
                val delinkIds = accountsToDelink.map { it.id }
                if (delinkIds.isNotEmpty()) {
                    try {
                        dataManager.pocketApi.delinkAccounts(request = PocketDelinkRequest(delinkIds))
                    } catch (e: Exception) {
                        // do nothing
                    }
                }
            }

            if (accountsToLink.isNotEmpty()) {
                val linkRequest = PocketLinkRequest(
                    accountsDetail = accountsToLink.map {
                        PocketLinkRequest.AccountDetail(
                            accountId = it.accountId.toString(),
                            accountType = it.accountType.name,
                        )
                    },
                )
                try {
                    dataManager.pocketApi.linkAccounts(request = linkRequest)
                } catch (e: Exception) {
                    // do nothing
                }
            }

            val updatedServerPockets = try {
                if (accountsToLink.isNotEmpty() || accountsToDelink.isNotEmpty()) {
                    dataManager.pocketApi.getPocketAccounts().toDomainList()
                } else {
                    serverBasicPockets
                }
            } catch (e: Exception) {
                // do nothing
                serverBasicPockets
            }

            localBasicPockets = localBasicPockets.map { local ->
                val matchedServer = updatedServerPockets
                    .find { it.accountId == local.accountId && it.accountType == local.accountType }
                if (matchedServer != null) {
                    local.copy(id = matchedServer.id, pocketId = matchedServer.pocketId)
                } else {
                    local
                }
            }
            pocketAccountDao.deleteAll()
            pocketAccountDao.linkPocketAccounts(localBasicPockets.map { it.toEntity() })
        } catch (e: Exception) {
            // do nothing
        }
    }

    override suspend fun getPocketAccounts(): DataState<List<PocketAccount>> {
        return runAsDataState(context = ioDispatcher) {
            syncPocketsWithServer()
            pocketAccountDao.getAllPocketAccounts().map { it.toDomain() }
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
            syncPocketsWithServer()
            val basicPockets = pocketAccountDao.getAllPocketAccounts().map { it.toDomain() }
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
        payload: PocketLinkPayload,
        explicitlyAddedAccounts: List<DetailedPocketAccount>,
        clientId: Long,
    ): DataState<Unit> {
        return runAsDataState(context = ioDispatcher) {
            val currentTime = Clock.System.now().toEpochMilliseconds()
            val accountsWithGeneratedIds = explicitlyAddedAccounts.mapIndexed { index, account ->
                val generatedId = -(currentTime + index)
                account.copy(
                    pocket = account.pocket.copy(
                        id = generatedId,
                        pocketId = generatedId,
                    ),
                )
            }
            val allLocalPockets = pocketAccountDao.getAllPocketAccounts().map { it.toDomain() }.toMutableList()
            accountsWithGeneratedIds.forEach { added ->
                allLocalPockets.removeAll {
                    it.accountId == added.pocket.accountId && it.accountType == added.pocket.accountType
                }
                allLocalPockets.add(added.pocket)
            }
            pocketAccountDao.deleteAll()
            pocketAccountDao.linkPocketAccounts(allLocalPockets.map { it.toEntity() })

            val currentState = detailedPocketCache.value
            if (currentState is DataState.Success) {
                val updatedList = currentState.data.toMutableList()
                accountsWithGeneratedIds.forEach { added ->
                    updatedList.removeAll {
                        it.pocket.accountId == added.pocket.accountId &&
                            it.pocket.accountType == added.pocket.accountType
                    }
                    updatedList.add(added)
                }
                detailedPocketCache.value = DataState.Success(updatedList)
            }

            if (networkMonitor.isOnline.first()) {
                try {
                    val request = PocketLinkRequest(
                        accountsDetail = payload.accountsDetail.map {
                            PocketLinkRequest.AccountDetail(
                                accountId = it.accountId,
                                accountType = it.accountType.name,
                            )
                        },
                    )
                    dataManager.pocketApi.linkAccounts(request = request)
                    syncPocketsWithServer()

                    val updatedBasicPockets = pocketAccountDao.getAllPocketAccounts().map { it.toDomain() }
                    if (detailedPocketCache.value is DataState.Success) {
                        val currentList =
                            (detailedPocketCache.value as DataState.Success<List<DetailedPocketAccount>>).data
                        val finalUpdatedList = currentList.map { detailed ->
                            val matched = updatedBasicPockets.find {
                                it.accountId == detailed.pocket.accountId &&
                                    it.accountType == detailed.pocket.accountType
                            }
                            if (matched != null) {
                                detailed.copy(pocket = matched)
                            } else {
                                detailed
                            }
                        }
                        detailedPocketCache.value = DataState.Success(finalUpdatedList)
                    }
                } catch (e: Exception) {
                    // do nothing
                }
            }
        }
    }

    override suspend fun delinkAccounts(pocketAccountMappingIds: List<Long>, clientId: Long): DataState<Unit> {
        return runAsDataState(context = ioDispatcher) {
            pocketAccountDao.delinkPocketAccounts(pocketAccountMappingIds)

            val currentState = detailedPocketCache.value
            if (currentState is DataState.Success) {
                val updatedList = currentState.data.filter { it.pocket.id !in pocketAccountMappingIds }
                detailedPocketCache.value = DataState.Success(updatedList)
            } else {
                syncPockets(clientId = clientId, forceRefresh = true)
            }

            val serverIds = pocketAccountMappingIds.filter { it > 0 }
            if (networkMonitor.isOnline.first() && serverIds.isNotEmpty()) {
                try {
                    val request = PocketDelinkRequest(serverIds)
                    dataManager.pocketApi.delinkAccounts(request = request)
                } catch (e: Exception) {
                    // do nothing
                }
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

    override suspend fun resetPocketCache() {
        detailedPocketCache.value = null
        cachedClientId = null
    }
}
