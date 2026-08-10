/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.pocket.managePocket

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.model.entity.payload.PocketLinkPayload
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.pocket.testing.FakePocketRepository
import org.mifos.mobile.feature.pocket.testing.FakeStringProvider
import org.mifos.mobile.feature.pocket.testing.FakeUserPreferencesRepository
import org.mifos.mobile.feature.pocket.testing.detailedPocketAccount
import org.mifos.mobile.feature.pocket.testing.linkableAccount
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class ManagePocketViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var preferences: FakeUserPreferencesRepository
    private lateinit var repository: FakePocketRepository
    private lateinit var stringProvider: FakeStringProvider
    private lateinit var viewModel: ManagePocketViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        preferences = FakeUserPreferencesRepository(initialClientId = 9L)
        repository = FakePocketRepository()
        stringProvider = FakeStringProvider()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun givenLinkedAccounts_whenViewModelLoads_thenAccountsAreMappedToManageState() = runTest(testDispatcher) {
        repository.setDetailedPocketAccounts(
            DataState.Success(
                listOf(
                    detailedAccount(
                        accountId = 101L,
                        mappingId = 201L,
                        type = AccountType.SAVINGS,
                        productName = null,
                    ),
                ),
            ),
        )
        createViewModel()

        advanceUntilIdle()

        val state = viewModel.stateFlow.value
        assertEquals(ScreenUiState.Success, state.uiState)
        assertEquals(9L, state.clientId)
        assertEquals(101L, state.linkedAccounts.single().accountId)
        assertEquals(201L, state.linkedAccounts.single().mappingId)
        assertEquals("Unknown Account", state.linkedAccounts.single().name)
        assertEquals("ACC-101", state.linkedAccounts.single().accountNumber)
    }

    @Test
    fun givenLinkedAccountsError_whenViewModelLoads_thenErrorStateIsPublished() = runTest(testDispatcher) {
        repository.setDetailedPocketAccounts(DataState.Error(Exception("network")))
        createViewModel()

        advanceUntilIdle()

        assertIs<ScreenUiState.Error>(viewModel.stateFlow.value.uiState)
    }

    @Test
    fun whenTabAndSearchActionsAreHandled_thenSelectionAndQueryAreStored() = runTest(testDispatcher) {
        createViewModel()

        viewModel.trySendAction(ManagePocketAction.TabSelected(AccountType.LOAN))
        viewModel.trySendAction(ManagePocketAction.SearchQueryChanged("car"))
        advanceUntilIdle()

        assertEquals(AccountType.LOAN, viewModel.stateFlow.value.selectedTab)
        assertEquals("car", viewModel.stateFlow.value.searchQuery)
    }

    @Test
    fun whenAccountSelectionChanges_thenIdentifierIsAddedAndRemoved() = runTest(testDispatcher) {
        createViewModel()

        viewModel.trySendAction(ManagePocketAction.AccountSelectionChanged(101L, true))
        advanceUntilIdle()
        assertTrue(101L in viewModel.stateFlow.value.selectedAccountIds)

        viewModel.trySendAction(ManagePocketAction.AccountSelectionChanged(101L, false))
        advanceUntilIdle()
        assertFalse(101L in viewModel.stateFlow.value.selectedAccountIds)
    }

    @Test
    fun whenOpenLinkAccountsIsHandled_thenAvailableAccountsAreLoaded() = runTest(testDispatcher) {
        repository.setAvailableAccountsToLink(
            DataState.Success(
                listOf(
                    linkableAccount(
                        accountId = 301L,
                        accountType = AccountType.SAVINGS,
                        accountNumber = "SAV-301",
                        productName = "Available savings",
                        balance = 10.0,
                    ),
                ),
            ),
        )
        createViewModel()

        viewModel.trySendAction(ManagePocketAction.OpenLinkAccounts)
        advanceUntilIdle()

        val state = viewModel.stateFlow.value
        assertEquals(ManagePocketDialogState.LinkAccounts, state.dialogState)
        assertEquals(1, state.availableAccounts.size)
        assertEquals("Available savings", state.availableAccounts.single().name)
        assertEquals(listOf(9L), repository.availableAccountRequests)
    }

    @Test
    fun whenLinkingSelectedAccountsSucceeds_thenSelectionIsClearedAndRepositoryReceivesPayload() =
        runTest(testDispatcher) {
            val available = linkableAccount(
                accountId = 301L,
                accountType = AccountType.SAVINGS,
                accountNumber = "SAV-301",
                productName = "Available savings",
                balance = 10.0,
            )
            repository.setAvailableAccountsToLink(DataState.Success(listOf(available)))
            createViewModel()
            advanceUntilIdle()

            viewModel.trySendAction(ManagePocketAction.OpenLinkAccounts)
            advanceUntilIdle()
            viewModel.trySendAction(ManagePocketAction.AccountSelectionChanged(301L, true))
            advanceUntilIdle()
            viewModel.trySendAction(ManagePocketAction.LinkSelectedAccounts)
            advanceUntilIdle()

            val state = viewModel.stateFlow.value
            assertNull(state.dialogState)
            assertTrue(state.selectedAccountIds.isEmpty())
            assertEquals("", state.searchQuery)
            assertEquals(
                PocketLinkPayload.AccountDetail("301", AccountType.SAVINGS),
                repository.linkRequests.single().accountsDetail.single(),
            )
            assertEquals("Available savings", repository.explicitlyAddedAccounts.single().single().productName)
            assertEquals(9L to true, repository.detailedAccountRequests.last())
        }

    @Test
    fun whenLinkingSelectedAccountsFails_thenLinkErrorDialogIsShown() = runTest(testDispatcher) {
        repository.setAvailableAccountsToLink(
            DataState.Success(
                listOf(
                    linkableAccount(301L, AccountType.SAVINGS, "SAV-301", "Available savings"),
                ),
            ),
        )
        repository.linkAccountsResult = DataState.Error(Exception("link failed"))
        createViewModel()
        advanceUntilIdle()

        viewModel.trySendAction(ManagePocketAction.OpenLinkAccounts)
        advanceUntilIdle()
        viewModel.trySendAction(ManagePocketAction.AccountSelectionChanged(301L, true))
        viewModel.trySendAction(ManagePocketAction.LinkSelectedAccounts)
        advanceUntilIdle()

        assertIs<ManagePocketDialogState.Error>(viewModel.stateFlow.value.dialogState)
    }

    @Test
    fun whenDelinkSucceeds_thenAccountIsRemovedAndLinkedAccountsReload() = runTest(testDispatcher) {
        val account = manageAccount()
        repository.setDetailedPocketAccounts(DataState.Success(listOf(account.toDetailed())))
        repository.detailedAccountsAfterDelink = DataState.Success(emptyList())
        createViewModel()
        advanceUntilIdle()

        viewModel.trySendAction(ManagePocketAction.OpenDelinkConfirmation(account))
        viewModel.trySendAction(ManagePocketAction.DelinkAccount(account))
        advanceUntilIdle()

        assertNull(viewModel.stateFlow.value.dialogState)
        assertTrue(viewModel.stateFlow.value.linkedAccounts.isEmpty())
        assertEquals(listOf(listOf(account.mappingId)), repository.delinkRequests)
        assertEquals(9L to true, repository.detailedAccountRequests.last())
    }

    @Test
    fun whenDelinkFails_thenDelinkErrorDialogIsShown() = runTest(testDispatcher) {
        repository.delinkAccountsResult = DataState.Error(Exception("delink failed"))
        createViewModel()
        advanceUntilIdle()
        val account = manageAccount()

        viewModel.trySendAction(ManagePocketAction.DelinkAccount(account))
        advanceUntilIdle()

        assertIs<ManagePocketDialogState.Error>(viewModel.stateFlow.value.dialogState)
    }

    @Test
    fun whenNavigateBackIsHandled_thenNavigateBackEventIsEmitted() = runTest(testDispatcher) {
        createViewModel()

        viewModel.eventFlow.test {
            viewModel.trySendAction(ManagePocketAction.NavigateBack)
            advanceUntilIdle()
            assertEquals(ManagePocketEvent.NavigateBack, awaitItem())
        }
    }

    private fun createViewModel() {
        viewModel = ManagePocketViewModel(
            pocketRepository = repository,
            userPreferencesRepository = preferences,
            stringProvider = stringProvider,
        )
    }

    private fun detailedAccount(
        accountId: Long,
        mappingId: Long,
        type: AccountType,
        productName: String?,
    ) = detailedPocketAccount(
        accountId = accountId,
        accountType = type,
        accountNumber = "ACC-$accountId",
        productName = productName,
        mappingId = mappingId,
    )

    private fun manageAccount() = ManagePocketAccount(
        accountId = 301L,
        mappingId = 401L,
        name = "Savings",
        accountNumber = "SAV-301",
        accountType = AccountType.SAVINGS,
    )

    private fun ManagePocketAccount.toDetailed() = detailedAccount(
        accountId = accountId,
        mappingId = mappingId,
        type = accountType,
        productName = name,
    )
}
