/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.pocket.pocketDashboard

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.mifos.mobile.core.common.CurrencyFormatter
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.model.entity.pocket.AccountStatus
import org.mifos.mobile.core.model.enums.AccountType
import org.mifos.mobile.core.ui.utils.ScreenUiState
import org.mifos.mobile.feature.pocket.testing.FakePocketRepository
import org.mifos.mobile.feature.pocket.testing.FakeStringProvider
import org.mifos.mobile.feature.pocket.testing.FakeUserPreferencesRepository
import org.mifos.mobile.feature.pocket.testing.detailedPocketAccount
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class PocketDashboardViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var preferences: FakeUserPreferencesRepository
    private lateinit var repository: FakePocketRepository
    private lateinit var stringProvider: FakeStringProvider
    private lateinit var viewModel: PocketDashboardViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        preferences = FakeUserPreferencesRepository(initialClientId = 7L)
        repository = FakePocketRepository()
        stringProvider = FakeStringProvider()
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun givenEmptyRepository_whenViewModelLoads_thenEmptyStateIsPublished() = runTest(testDispatcher) {
        repository.setDetailedPocketAccounts(DataState.Success(emptyList()))
        createViewModel()

        advanceUntilIdle()

        assertEquals(ScreenUiState.Empty, viewModel.stateFlow.value.uiState)
        assertEquals(7L, viewModel.stateFlow.value.clientId)
        assertTrue(repository.resetPocketCacheCalled)
    }

    @Test
    fun givenRepositoryLoading_whenViewModelLoads_thenLoadingStateIsPublished() = runTest(testDispatcher) {
        repository.setDetailedPocketAccounts(DataState.Loading)
        createViewModel()

        advanceUntilIdle()

        assertEquals(ScreenUiState.Loading, viewModel.stateFlow.value.uiState)
    }

    @Test
    fun givenRepositoryError_whenViewModelLoads_thenErrorStateIsPublished() = runTest(testDispatcher) {
        repository.setDetailedPocketAccounts(DataState.Error(Exception("network")))
        createViewModel()

        advanceUntilIdle()

        assertIs<ScreenUiState.Error>(viewModel.stateFlow.value.uiState)
        assertFalse(viewModel.stateFlow.value.isRefreshing)
    }

    @Test
    fun givenAccountsOfEachType_whenViewModelLoads_thenAccountsAreCategorizedAndTotalIsFormatted() =
        runTest(testDispatcher) {
            repository.setDetailedPocketAccounts(
                DataState.Success(
                    listOf(
                        detailedPocketAccount(101L, AccountType.SAVINGS, "Savings", "Savings", 100.0),
                        detailedPocketAccount(102L, AccountType.LOAN, "Loan", "Loan", 50.0),
                        detailedPocketAccount(103L, AccountType.SHARE, "Shares", "Shares", 25.0),
                        detailedPocketAccount(
                            accountId = 104L,
                            accountType = AccountType.SAVINGS,
                            accountNumber = "ACC-104",
                            productName = null,
                            balance = null,
                            status = AccountStatus.PENDING,
                        ),
                    ),
                ),
            )
            createViewModel()

            advanceUntilIdle()

            val state = viewModel.stateFlow.value
            assertEquals(ScreenUiState.Success, state.uiState)
            assertEquals(listOf(101L, 104L), state.savingsAccounts.map { it.accountId })
            assertEquals(listOf(102L), state.loanAccounts.map { it.accountId })
            assertEquals(listOf(103L), state.shareAccounts.map { it.accountId })
            assertEquals(CurrencyFormatter.format(175.0, "USD", 2), state.totalBalance)
            assertEquals("PENDING", state.savingsAccounts[1].balanceOrStatus)
            assertEquals("Unknown Account", state.savingsAccounts[1].name)
        }

    @Test
    fun whenRetryIsClicked_thenForceRefreshLoadsTheLatestState() = runTest(testDispatcher) {
        repository.setDetailedPocketAccounts(DataState.Error(Exception("first attempt")))
        createViewModel()
        advanceUntilIdle()

        repository.setDetailedPocketAccounts(DataState.Success(emptyList()))
        viewModel.trySendAction(PocketDashboardAction.Retry)
        advanceUntilIdle()

        assertEquals(ScreenUiState.Empty, viewModel.stateFlow.value.uiState)
        assertTrue(repository.detailedAccountRequests.last().second)
    }

    @Test
    fun givenMultipleCurrencies_whenViewModelLoads_thenTotalIsFormattedPerCurrency() =
        runTest(testDispatcher) {
            repository.setDetailedPocketAccounts(
                DataState.Success(
                    listOf(
                        detailedPocketAccount(101L, AccountType.SAVINGS, "US-101", "US savings", 100.0),
                        detailedPocketAccount(102L, AccountType.LOAN, "EU-102", "Euro loan", 50.0)
                            .copy(currencyCode = "EUR"),
                    ),
                ),
            )
            createViewModel()

            advanceUntilIdle()

            assertEquals(
                listOf(
                    CurrencyFormatter.format(100.0, "USD", 2),
                    CurrencyFormatter.format(50.0, "EUR", 2),
                ).joinToString("\n"),
                viewModel.stateFlow.value.totalBalance,
            )
        }

    @Test
    fun whenRefreshIsClicked_thenRefreshingStateIsClearedAfterSuccess() = runTest(testDispatcher) {
        repository.setDetailedPocketAccounts(DataState.Success(emptyList()))
        createViewModel()
        advanceUntilIdle()

        repository.setDetailedPocketAccounts(DataState.Loading)
        viewModel.trySendAction(PocketDashboardAction.Refresh)
        advanceUntilIdle()
        assertTrue(viewModel.stateFlow.value.isRefreshing)

        repository.setDetailedPocketAccounts(DataState.Success(emptyList()))
        advanceUntilIdle()

        assertFalse(viewModel.stateFlow.value.isRefreshing)
        assertTrue(repository.detailedAccountRequests.last().second)
    }

    @Test
    fun whenNavigationActionsAreHandled_thenExpectedEventsAreEmitted() = runTest(testDispatcher) {
        createViewModel()

        viewModel.eventFlow.test {
            viewModel.trySendAction(PocketDashboardAction.NavigateBack)
            advanceUntilIdle()
            assertEquals(PocketDashboardEvent.NavigateBack, awaitItem())

            viewModel.trySendAction(PocketDashboardAction.ManagePocket)
            advanceUntilIdle()
            assertEquals(PocketDashboardEvent.ManagePocket, awaitItem())

            viewModel.trySendAction(PocketDashboardAction.LinkFirstAccount)
            advanceUntilIdle()
            assertEquals(PocketDashboardEvent.ManagePocket, awaitItem())

            viewModel.trySendAction(PocketDashboardAction.NavigateToLoanDetail(10L))
            advanceUntilIdle()
            assertEquals(PocketDashboardEvent.NavigateToLoanDetail(10L), awaitItem())

            viewModel.trySendAction(PocketDashboardAction.NavigateToSavingsDetail(11L))
            advanceUntilIdle()
            assertEquals(PocketDashboardEvent.NavigateToSavingsDetail(11L), awaitItem())

            viewModel.trySendAction(PocketDashboardAction.NavigateToShareDetail(12L))
            advanceUntilIdle()
            assertEquals(PocketDashboardEvent.NavigateToShareDetail(12L), awaitItem())
        }
    }

    private fun createViewModel() {
        viewModel = PocketDashboardViewModel(
            pocketRepository = repository,
            userPreferencesRepository = preferences,
            stringProvider = stringProvider,
        )
    }
}
