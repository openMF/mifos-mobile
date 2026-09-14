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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repositories.BaseFakeClientService
import org.mifos.mobile.core.data.repositories.BaseFakePocketService
import org.mifos.mobile.core.data.repositories.BaseFakeShareAccountService
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.database.dao.PocketAccountDao
import org.mifos.mobile.core.database.entity.PendingPocketDelinkEntity
import org.mifos.mobile.core.database.entity.PocketAccountEntity
import org.mifos.mobile.core.model.entity.payload.PocketLinkPayload
import org.mifos.mobile.core.model.entity.pocket.AccountStatus
import org.mifos.mobile.core.model.entity.pocket.DetailedPocketAccount
import org.mifos.mobile.core.model.entity.pocket.LinkableAccount
import org.mifos.mobile.core.model.entity.pocket.PocketAccount
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.network.dto.accounts.AccountsResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.loanAccount.LoanAccountResponseDto
import org.mifos.mobile.core.network.dto.loanAccount.LoanStatusResponseDto
import org.mifos.mobile.core.network.dto.pocket.PocketAccountDto
import org.mifos.mobile.core.network.dto.pocket.PocketCommandResponse
import org.mifos.mobile.core.network.dto.pocket.PocketDelinkRequest
import org.mifos.mobile.core.network.dto.pocket.PocketLinkRequest
import org.mifos.mobile.core.network.dto.pocket.PocketResponseDto
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Verifies Pocket synchronization by combining fake network
 * services, a fake network monitor, and an in-memory Pocket DAO.
 */
class PocketRepositoryTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dataManager: DataManager
    private lateinit var networkMonitor: FakeNetworkMonitor
    private lateinit var pocketAccountDao: FakePocketAccountDao
    private lateinit var repository: PocketRepositoryImp

    private lateinit var fakeClientService: FakeClientService
    private lateinit var fakePocketService: FakePocketService
    private lateinit var fakeShareAccountService: BaseFakeShareAccountService

    @BeforeTest
    fun setUp() {
        fakeClientService = FakeClientService()
        fakePocketService = FakePocketService()
        fakeShareAccountService = BaseFakeShareAccountService()

        dataManager = object : DataManager() {
            override val clientsApi = fakeClientService
            override val pocketApi = fakePocketService
            override val shareAccountApi = fakeShareAccountService
        }
        networkMonitor = FakeNetworkMonitor()
        pocketAccountDao = FakePocketAccountDao()
        repository = PocketRepositoryImp(
            dataManager = dataManager,
            networkMonitor = networkMonitor,
            pocketAccountDao = pocketAccountDao,
            ioDispatcher = testDispatcher,
        )
    }

    /** Maps loan, savings, and share mappings and persists the domain values. */
    @Test
    fun getPocketAccountsMapsAllPocketTypesAndPersistsTheDomainMapping() = runTest(testDispatcher) {
        fakePocketService.response = PocketResponseDto(
            loanAccounts = listOf(pocketDto(1L, 10L, "LN-10", 100L, AccountType.LOAN)),
            savingsAccounts = listOf(pocketDto(2L, 20L, "SV-20", 200L, AccountType.SAVINGS)),
            shareAccounts = listOf(pocketDto(3L, 30L, "SH-30", 300L, AccountType.SHARE)),
        )

        val result = repository.getPocketAccounts()

        val accounts = assertIs<DataState.Success<List<PocketAccount>>>(result).data
        assertEquals(
            listOf(AccountType.LOAN, AccountType.SAVINGS, AccountType.SHARE),
            accounts.map { it.accountType },
        )
        assertEquals(listOf(10L, 20L, 30L), accounts.map { it.accountId })
        assertEquals(listOf("LN-10", "SV-20", "SH-30"), accounts.map { it.accountNumber })
        assertEquals(listOf(100L, 200L, 300L), accounts.map { it.id })
        assertEquals(accounts.map { it.accountId }, pocketAccountDao.accounts.map { it.accountId })
    }

    /** Returns persisted mappings when the remote Pocket request fails. */
    @Test
    fun getPocketAccountsReturnsCachedAccountsWhenNetworkCallFails() = runTest(testDispatcher) {
        val cachedAccount = localEntity(10L, AccountType.LOAN, 100L)
        pocketAccountDao.accounts = mutableListOf(cachedAccount)
        fakePocketService.failure = IllegalStateException("server unavailable")

        val result = repository.getPocketAccounts()

        val accounts = assertIs<DataState.Success<List<PocketAccount>>>(result).data
        assertEquals(listOf(10L), accounts.map { it.accountId })
    }

    /** Joins a Pocket mapping with client account product and balance details. */
    @Test
    fun getDetailedPocketAccountsEnrichesPocketWithClientAccountDetails() = runTest(testDispatcher) {
        fakePocketService.response = PocketResponseDto(
            loanAccounts = listOf(pocketDto(1L, 10L, "LN-10", 100L, AccountType.LOAN)),
        )
        fakeClientService.accounts = AccountsResponseDto(
            loanAccounts = listOf(loanAccount(10L, "Personal loan", 1250.5)),
        )

        val result = repository.getDetailedPocketAccounts(clientId = 7L).first { it is DataState.Success }

        val account = assertIs<DataState.Success<List<DetailedPocketAccount>>>(result).data.single()
        assertEquals(10L, account.pocket.accountId)
        assertEquals("Personal loan", account.productName)
        assertEquals(1250.5, account.balance)
        assertEquals("USD", account.currencyCode)
        assertEquals(2, account.decimalPlaces)
        assertEquals(AccountStatus.ACTIVE, account.status)
    }

    /** Persists one typed account and sends the matching link request. */
    @Test
    fun linkAccountPersistsTypedAccountAndSendsSingleAccountRequest() = runTest(testDispatcher) {
        val result = repository.linkAccount(
            accountId = 42L,
            accountType = AccountType.SAVINGS,
            accountNumber = "SV-42",
        )

        assertIs<DataState.Success<Unit>>(result)
        assertEquals(1, pocketAccountDao.accounts.size)
        assertEquals(42L, pocketAccountDao.accounts.single().accountId)
        assertEquals(AccountType.SAVINGS.name, pocketAccountDao.accounts.single().accountType)
        assertEquals(
            listOf(PocketLinkRequest.AccountDetail("42", AccountType.SAVINGS.name)),
            fakePocketService.lastLinkRequest?.accountsDetail,
        )
    }

    /** Excludes linked accounts and maps the remaining client accounts. */
    @Test
    fun getAvailableAccountsToLinkExcludesAccountsAlreadyInPocketAndMapsRemainingAccounts() =
        runTest(testDispatcher) {
            fakePocketService.response = PocketResponseDto(
                loanAccounts = listOf(pocketDto(1L, 10L, "LN-10", 100L, AccountType.LOAN)),
            )
            fakeClientService.accounts = AccountsResponseDto(
                loanAccounts = listOf(
                    loanAccount(10L, "Already linked"),
                    loanAccount(11L, "Available loan", 90.0),
                ),
            )
            repository.getDetailedPocketAccounts(clientId = 7L).first { it is DataState.Success }

            val result = repository.getAvailableAccountsToLink(clientId = 7L).first { it is DataState.Success }

            val accounts = assertIs<
                DataState.Success<List<LinkableAccount>>,
                >(result).data
            assertEquals(1, accounts.size)
            assertEquals(11L, accounts.single().accountId)
            assertEquals("Available loan", accounts.single().productName)
            assertEquals(90.0, accounts.single().balance)
            assertEquals(AccountType.LOAN, accounts.single().accountType)
        }

    /** Sends a multi-account payload and refreshes detailed Pocket data. */
    @Test
    fun linkAccountsMapsPayloadAndRefreshesDetailedCache() = runTest(testDispatcher) {
        fakePocketService.response = PocketResponseDto(
            loanAccounts = listOf(pocketDto(1L, 11L, "LN-11", 101L, AccountType.LOAN)),
        )
        fakeClientService.accounts = AccountsResponseDto(
            loanAccounts = listOf(loanAccount(11L, "New loan")),
        )

        val result = repository.linkAccounts(
            payload = PocketLinkPayload(
                accountsDetail = listOf(PocketLinkPayload.AccountDetail("11", AccountType.LOAN)),
            ),
            explicitlyAddedAccounts = emptyList(),
            clientId = 7L,
        )

        assertIs<DataState.Success<Unit>>(result)
        assertEquals("linkAccounts", fakePocketService.lastLinkCommand)
        assertEquals(
            PocketLinkRequest.AccountDetail(accountId = "11", accountType = "LOAN"),
            fakePocketService.lastLinkRequest?.accountsDetail?.single(),
        )

        val cached = repository.getDetailedPocketAccounts(clientId = 7L).first { it is DataState.Success }
        assertEquals(
            "New loan",
            assertIs<DataState.Success<List<DetailedPocketAccount>>>(cached).data.single().productName,
        )
    }

    /** Sends only real mapping IDs and removes the mapping from the cache. */
    @Test
    fun delinkAccountsSendsPositiveMappingIdsAndRemovesAccountFromCache() = runTest(testDispatcher) {
        fakePocketService.response = PocketResponseDto(
            loanAccounts = listOf(pocketDto(1L, 10L, "LN-10", 100L, AccountType.LOAN)),
        )
        fakeClientService.accounts = AccountsResponseDto(
            loanAccounts = listOf(loanAccount(10L, "Personal loan")),
        )
        repository.getDetailedPocketAccounts(clientId = 7L).first { it is DataState.Success }

        val result = repository.delinkAccounts(
            pocketAccountMappingIds = listOf(100L, -1L),
            clientId = 7L,
        )

        assertIs<DataState.Success<Unit>>(result)
        assertEquals(PocketDelinkRequest(listOf(100L)), fakePocketService.lastDelinkRequest)
        assertTrue(pocketAccountDao.pendingDelinks.isEmpty())
        val cached = repository.getDetailedPocketAccounts(clientId = 7L).first { it is DataState.Success }
        assertTrue(assertIs<DataState.Success<List<DetailedPocketAccount>>>(cached).data.isEmpty())
    }

    /** Keeps failed remote delinks pending and hides them on refresh. */
    @Test
    fun delinkAccountsKeepsFailedRemoteDelinkPendingAndFiltersServerRefresh() = runTest(testDispatcher) {
        pocketAccountDao.accounts = mutableListOf(localEntity(10L, AccountType.LOAN, 100L))
        fakePocketService.response = PocketResponseDto(
            loanAccounts = listOf(pocketDto(1L, 10L, "LN-10", 100L, AccountType.LOAN)),
        )
        fakePocketService.delinkFailure = IllegalStateException("server unavailable")

        val result = repository.delinkAccounts(
            pocketAccountMappingIds = listOf(100L),
            clientId = 7L,
        )

        assertIs<DataState.Success<Unit>>(result)
        assertEquals(setOf(100L), pocketAccountDao.pendingDelinks)
        assertTrue(pocketAccountDao.accounts.isEmpty())

        val refreshResult = repository.getPocketAccounts()

        val refreshedAccounts = assertIs<DataState.Success<List<PocketAccount>>>(refreshResult).data
        assertTrue(refreshedAccounts.isEmpty())
        assertEquals(setOf(100L), pocketAccountDao.pendingDelinks)
        assertEquals(PocketDelinkRequest(listOf(100L)), fakePocketService.lastDelinkRequest)
    }

    /** Retries persisted pending delinks and clears them after success. */
    @Test
    fun getPocketAccountsRetriesPersistedPendingDelinksAndClearsThemWhenSuccessful() = runTest(testDispatcher) {
        pocketAccountDao.pendingDelinks = mutableSetOf(100L)
        fakePocketService.response = PocketResponseDto()
        repository = PocketRepositoryImp(
            dataManager = dataManager,
            networkMonitor = networkMonitor,
            pocketAccountDao = pocketAccountDao,
            ioDispatcher = testDispatcher,
        )

        val result = repository.getPocketAccounts()

        assertIs<DataState.Success<List<PocketAccount>>>(result)
        assertEquals(PocketDelinkRequest(listOf(100L)), fakePocketService.lastDelinkRequest)
        assertTrue(pocketAccountDao.pendingDelinks.isEmpty())
    }

    /** Avoids sending a duplicate delink when detailed data is not cached. */
    @Test
    fun delinkAccountsWithoutDetailedCacheDoesNotSendDuplicateRemoteDelink() = runTest(testDispatcher) {
        pocketAccountDao.accounts = mutableListOf(localEntity(10L, AccountType.LOAN, 100L))
        fakePocketService.response = PocketResponseDto()

        val result = repository.delinkAccounts(
            pocketAccountMappingIds = listOf(100L),
            clientId = 7L,
        )

        assertIs<DataState.Success<Unit>>(result)
        assertEquals(listOf(PocketDelinkRequest(listOf(100L))), fakePocketService.delinkRequests)
        assertTrue(pocketAccountDao.pendingDelinks.isEmpty())
    }

    private fun pocketDto(
        pocketId: Long,
        accountId: Long,
        accountNumber: String,
        id: Long,
        accountType: AccountType,
    ) = PocketAccountDto(
        pocketId = pocketId,
        accountId = accountId,
        accountType = when (accountType) {
            AccountType.LOAN -> 1
            AccountType.SAVINGS -> 2
            AccountType.SHARE -> 3
        },
        accountNumber = accountNumber,
        id = id,
    )

    private fun loanAccount(
        id: Long,
        productName: String,
        balance: Double = 0.0,
    ) = LoanAccountResponseDto(
        id = id,
        accountNo = "LN-$id",
        productName = productName,
        loanBalance = balance,
        status = LoanStatusResponseDto(active = true),
        currency = CurrencyResponseDto(code = "USD", decimalPlaces = 2, displaySymbol = "$"),
        timeline = null,
    )

    private fun localEntity(accountId: Long, accountType: AccountType, id: Long) = PocketAccountEntity(
        id = id,
        pocketId = id,
        accountId = accountId,
        accountType = accountType.name,
        accountNumber = "${accountType.name.take(2)}-$accountId",
    )
}

class FakeNetworkMonitor : NetworkMonitor {
    var online = true

    override val isOnline: Flow<Boolean>
        get() = flowOf(online)
}

class FakePocketService : BaseFakePocketService() {
    var response = PocketResponseDto()
    var failure: Exception? = null
    var lastLinkCommand: String? = null
    var lastLinkRequest: PocketLinkRequest? = null
    var lastDelinkRequest: PocketDelinkRequest? = null
    val delinkRequests = mutableListOf<PocketDelinkRequest>()
    var delinkFailure: Exception? = null

    override suspend fun getPocketAccounts(): PocketResponseDto {
        failure?.let { throw it }
        return response
    }

    override suspend fun linkAccounts(command: String, request: PocketLinkRequest): PocketCommandResponse {
        lastLinkCommand = command
        lastLinkRequest = request
        return PocketCommandResponse(resourceId = 1L)
    }

    override suspend fun delinkAccounts(command: String, request: PocketDelinkRequest): PocketCommandResponse {
        lastDelinkRequest = request
        delinkRequests.add(request)
        delinkFailure?.let { throw it }
        return PocketCommandResponse(resourceId = 1L)
    }
}

class FakeClientService : BaseFakeClientService() {
    var accounts = AccountsResponseDto()

    override fun getClientAccounts(clientId: Long): Flow<AccountsResponseDto> = flowOf(accounts)
}

class FakePocketAccountDao : PocketAccountDao {
    var accounts = mutableListOf<PocketAccountEntity>()
    var pendingDelinks = mutableSetOf<Long>()

    override suspend fun getAllPocketAccounts(): List<PocketAccountEntity> = accounts.toList()

    override suspend fun linkPocketAccounts(pockets: List<PocketAccountEntity>) {
        val ids = pockets.map { it.id }.toSet()
        accounts = (accounts.filterNot { it.id in ids } + pockets).toMutableList()
    }

    override suspend fun delinkPocketAccounts(pocketAccountMappingIds: List<Long>) {
        accounts.removeAll { it.id in pocketAccountMappingIds }
    }

    override suspend fun deleteAll() {
        accounts.clear()
    }

    override suspend fun getPendingDelinkIds(): List<Long> = pendingDelinks.toList()

    override suspend fun insertPendingDelinks(pendingDelinks: List<PendingPocketDelinkEntity>) {
        this.pendingDelinks.addAll(pendingDelinks.map { it.pocketAccountMappingId })
    }

    override suspend fun deletePendingDelinks(pocketAccountMappingIds: List<Long>) {
        pendingDelinks.removeAll(pocketAccountMappingIds.toSet())
    }

    override suspend fun replaceAllPocketAccounts(pockets: List<PocketAccountEntity>) {
        accounts = pockets.toMutableList()
    }
}
