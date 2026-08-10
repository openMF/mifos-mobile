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

import io.ktor.client.statement.HttpResponse
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.util.NetworkMonitor
import org.mifos.mobile.core.database.dao.PocketAccountDao
import org.mifos.mobile.core.database.entity.PocketAccountEntity
import org.mifos.mobile.core.model.entity.payload.PocketLinkPayload
import org.mifos.mobile.core.model.entity.pocket.AccountStatus
import org.mifos.mobile.core.model.entity.pocket.DetailedPocketAccount
import org.mifos.mobile.core.model.entity.pocket.PocketAccount
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.network.dto.accounts.AccountsResponseDto
import org.mifos.mobile.core.network.dto.client.ClientResponseDto
import org.mifos.mobile.core.network.dto.common.PageResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.loanAccount.LoanAccountResponseDto
import org.mifos.mobile.core.network.dto.loanAccount.LoanStatusResponseDto
import org.mifos.mobile.core.network.dto.payloads.ShareApplicationPayloadDto
import org.mifos.mobile.core.network.dto.pocket.PocketAccountDto
import org.mifos.mobile.core.network.dto.pocket.PocketCommandResponse
import org.mifos.mobile.core.network.dto.pocket.PocketDelinkRequest
import org.mifos.mobile.core.network.dto.pocket.PocketLinkRequest
import org.mifos.mobile.core.network.dto.pocket.PocketResponseDto
import org.mifos.mobile.core.network.dto.products.share.ShareProductDetailsResponseDto
import org.mifos.mobile.core.network.dto.products.share.ShareProductResponseDto
import org.mifos.mobile.core.network.dto.shareAccount.ShareWithAssociationsResponseDto
import org.mifos.mobile.core.network.services.ClientService
import org.mifos.mobile.core.network.services.PocketService
import org.mifos.mobile.core.network.services.ShareAccountService
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class PocketRepositoryTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dataManager: DataManager
    private lateinit var clientService: FakeClientService
    private lateinit var pocketService: FakePocketService
    private lateinit var shareAccountService: FakeShareAccountService
    private lateinit var networkMonitor: FakeNetworkMonitor
    private lateinit var pocketAccountDao: FakePocketAccountDao
    private lateinit var repository: PocketRepositoryImp

    @BeforeTest
    fun setUp() {
        dataManager = mockk()
        clientService = FakeClientService()
        pocketService = FakePocketService()
        shareAccountService = FakeShareAccountService()
        every { dataManager.clientsApi } returns clientService
        every { dataManager.pocketApi } returns pocketService
        every { dataManager.shareAccountApi } returns shareAccountService
        networkMonitor = FakeNetworkMonitor()
        pocketAccountDao = FakePocketAccountDao()
        repository = PocketRepositoryImp(
            dataManager = dataManager,
            networkMonitor = networkMonitor,
            pocketAccountDao = pocketAccountDao,
            ioDispatcher = testDispatcher,
        )
    }

    @Test
    fun getPocketAccounts_mapsAllPocketTypesAndPersistsTheDomainMapping() = runTest(testDispatcher) {
        pocketService.response = PocketResponseDto(
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

    @Test
    fun getPocketAccounts_returnsCachedAccountsWhenNetworkCallFails() = runTest(testDispatcher) {
        val cachedAccount = localEntity(10L, AccountType.LOAN, 100L)
        pocketAccountDao.accounts = mutableListOf(cachedAccount)
        pocketService.failure = IllegalStateException("server unavailable")

        val result = repository.getPocketAccounts()

        val accounts = assertIs<DataState.Success<List<PocketAccount>>>(result).data
        assertEquals(listOf(10L), accounts.map { it.accountId })
    }

    @Test
    fun getDetailedPocketAccounts_enrichesPocketWithClientAccountDetails() = runTest(testDispatcher) {
        pocketService.response = PocketResponseDto(
            loanAccounts = listOf(pocketDto(1L, 10L, "LN-10", 100L, AccountType.LOAN)),
        )
        clientService.accounts = AccountsResponseDto(
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

    @Test
    fun getAvailableAccountsToLink_excludesAccountsAlreadyInPocketAndMapsRemainingAccounts() =
        runTest(testDispatcher) {
            pocketService.response = PocketResponseDto(
                loanAccounts = listOf(pocketDto(1L, 10L, "LN-10", 100L, AccountType.LOAN)),
            )
            clientService.accounts = AccountsResponseDto(
                loanAccounts = listOf(
                    loanAccount(10L, "Already linked"),
                    loanAccount(11L, "Available loan", 90.0),
                ),
            )
            repository.getDetailedPocketAccounts(clientId = 7L).first { it is DataState.Success }

            val result = repository.getAvailableAccountsToLink(clientId = 7L).first { it is DataState.Success }

            val accounts = assertIs<
                DataState.Success<List<org.mifos.mobile.core.model.entity.pocket.LinkableAccount>>,
                >(result).data
            assertEquals(1, accounts.size)
            assertEquals(11L, accounts.single().accountId)
            assertEquals("Available loan", accounts.single().productName)
            assertEquals(90.0, accounts.single().balance)
            assertEquals(AccountType.LOAN, accounts.single().accountType)
        }

    @Test
    fun linkAccounts_mapsPayloadAndRefreshesDetailedCache() = runTest(testDispatcher) {
        pocketService.response = PocketResponseDto(
            loanAccounts = listOf(pocketDto(1L, 11L, "LN-11", 101L, AccountType.LOAN)),
        )
        clientService.accounts = AccountsResponseDto(
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
        assertEquals("linkAccounts", pocketService.lastLinkCommand)
        assertEquals(
            PocketLinkRequest.AccountDetail(accountId = "11", accountType = "LOAN"),
            pocketService.lastLinkRequest?.accountsDetail?.single(),
        )

        val cached = repository.getDetailedPocketAccounts(clientId = 7L).first { it is DataState.Success }
        assertEquals(
            "New loan",
            assertIs<DataState.Success<List<DetailedPocketAccount>>>(cached).data.single().productName,
        )
    }

    @Test
    fun delinkAccounts_sendsPositiveMappingIdsAndRemovesAccountFromCache() = runTest(testDispatcher) {
        pocketService.response = PocketResponseDto(
            loanAccounts = listOf(pocketDto(1L, 10L, "LN-10", 100L, AccountType.LOAN)),
        )
        clientService.accounts = AccountsResponseDto(
            loanAccounts = listOf(loanAccount(10L, "Personal loan")),
        )
        repository.getDetailedPocketAccounts(clientId = 7L).first { it is DataState.Success }

        val result = repository.delinkAccounts(
            pocketAccountMappingIds = listOf(100L, -1L),
            clientId = 7L,
        )

        assertIs<DataState.Success<Unit>>(result)
        assertEquals(PocketDelinkRequest(listOf(100L)), pocketService.lastDelinkRequest)
        val cached = repository.getDetailedPocketAccounts(clientId = 7L).first { it is DataState.Success }
        assertTrue(assertIs<DataState.Success<List<DetailedPocketAccount>>>(cached).data.isEmpty())
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

private class FakeNetworkMonitor : NetworkMonitor {
    var online = true

    override val isOnline: Flow<Boolean>
        get() = flowOf(online)
}

private class FakePocketService : PocketService {
    var response = PocketResponseDto()
    var failure: Exception? = null
    var lastLinkCommand: String? = null
    var lastLinkRequest: PocketLinkRequest? = null
    var lastDelinkRequest: PocketDelinkRequest? = null

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
        return PocketCommandResponse(resourceId = 1L)
    }
}

private class FakeShareAccountService : ShareAccountService {
    override fun getShareProducts(clientId: Long?): Flow<PageResponseDto<ShareProductResponseDto>> =
        error("Not used by pocket tests")

    override fun getShareProductById(
        productId: Long,
        clientId: Long?,
    ): Flow<ShareProductDetailsResponseDto> = error("Not used by pocket tests")

    override suspend fun submitShareApplication(payload: ShareApplicationPayloadDto?): HttpResponse =
        error("Not used by pocket tests")

    override fun getShareAccountDetails(
        accountId: Long,
        associations: String,
    ): Flow<ShareWithAssociationsResponseDto> = error("Not used by pocket tests")
}

private class FakeClientService : ClientService {
    var accounts = AccountsResponseDto()

    override fun clients(): Flow<PageResponseDto<ClientResponseDto>> = error("Not used by pocket tests")

    override fun getClientForId(clientId: Long): Flow<ClientResponseDto> = error("Not used by pocket tests")

    override fun getClientImage(clientId: Long): Flow<HttpResponse> = error("Not used by pocket tests")

    override fun getClientAccounts(clientId: Long): Flow<AccountsResponseDto> = flowOf(accounts)

    override fun getAccounts(clientId: Long, accountType: String?): Flow<AccountsResponseDto> =
        error("Not used by pocket tests")
}

private class FakePocketAccountDao : PocketAccountDao {
    var accounts = mutableListOf<PocketAccountEntity>()

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

    override suspend fun replaceAllPocketAccounts(pockets: List<PocketAccountEntity>) {
        accounts = pockets.toMutableList()
    }
}
