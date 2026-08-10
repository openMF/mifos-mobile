/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.pocket.testing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.StringProvider
import org.mifos.mobile.core.data.repository.PocketRepository
import org.mifos.mobile.core.datastore.UserPreferencesRepository
import org.mifos.mobile.core.datastore.model.AppSettings
import org.mifos.mobile.core.datastore.model.TimeBasedTheme
import org.mifos.mobile.core.datastore.model.UserData
import org.mifos.mobile.core.model.LanguageConfig
import org.mifos.mobile.core.model.MifosThemeConfig
import org.mifos.mobile.core.model.entity.payload.PocketLinkPayload
import org.mifos.mobile.core.model.entity.pocket.AccountStatus
import org.mifos.mobile.core.model.entity.pocket.DetailedPocketAccount
import org.mifos.mobile.core.model.entity.pocket.LinkableAccount
import org.mifos.mobile.core.model.entity.pocket.PocketAccount
import org.mifos.mobile.core.model.enums.AccountType

internal class FakePocketRepository : PocketRepository {
    private val detailedAccounts = MutableStateFlow<DataState<List<DetailedPocketAccount>>>(
        DataState.Success(emptyList()),
    )
    private val availableAccounts = MutableStateFlow<DataState<List<LinkableAccount>>>(
        DataState.Success(emptyList()),
    )

    val detailedAccountRequests = mutableListOf<Pair<Long, Boolean>>()
    val availableAccountRequests = mutableListOf<Long>()
    val linkRequests = mutableListOf<PocketLinkPayload>()
    val delinkRequests = mutableListOf<List<Long>>()
    val explicitlyAddedAccounts = mutableListOf<List<DetailedPocketAccount>>()
    var linkAccountsResult: DataState<Unit> = DataState.Success(Unit)
    var delinkAccountsResult: DataState<Unit> = DataState.Success(Unit)
    var detailedAccountsAfterLink: DataState<List<DetailedPocketAccount>>? = null
    var detailedAccountsAfterDelink: DataState<List<DetailedPocketAccount>>? = null
    var resetPocketCacheCalled = false

    fun setDetailedPocketAccounts(state: DataState<List<DetailedPocketAccount>>) {
        detailedAccounts.value = state
    }

    fun setAvailableAccountsToLink(state: DataState<List<LinkableAccount>>) {
        availableAccounts.value = state
    }

    override suspend fun getPocketAccounts(): DataState<List<PocketAccount>> =
        DataState.Success(emptyList())

    override fun getDetailedPocketAccounts(
        clientId: Long,
        forceRefresh: Boolean,
    ): Flow<DataState<List<DetailedPocketAccount>>> {
        detailedAccountRequests += clientId to forceRefresh
        return detailedAccounts
    }

    override fun getAvailableAccountsToLink(clientId: Long): Flow<DataState<List<LinkableAccount>>> {
        availableAccountRequests += clientId
        return availableAccounts
    }

    override suspend fun linkAccounts(
        payload: PocketLinkPayload,
        explicitlyAddedAccounts: List<DetailedPocketAccount>,
        clientId: Long,
    ): DataState<Unit> {
        linkRequests += payload
        this.explicitlyAddedAccounts += explicitlyAddedAccounts
        detailedAccountsAfterLink?.let { detailedAccounts.value = it }
        return linkAccountsResult
    }

    override suspend fun delinkAccounts(
        pocketAccountMappingIds: List<Long>,
        clientId: Long,
    ): DataState<Unit> {
        delinkRequests += pocketAccountMappingIds
        detailedAccountsAfterDelink?.let { detailedAccounts.value = it }
        return delinkAccountsResult
    }

    override suspend fun resetPocketCache() {
        resetPocketCacheCalled = true
    }
}

internal class FakeStringProvider(
    private val value: String = "Unknown Account",
) : StringProvider {
    override suspend fun get(resource: StringResource, vararg formatArgs: Any): String = value
}

internal class FakeUserPreferencesRepository(
    initialClientId: Long? = 42L,
) : UserPreferencesRepository {
    override val userInfo = MutableStateFlow(UserData.DEFAULT)
    override val settingsInfo = MutableStateFlow(AppSettings.DEFAULT)
    override val token = MutableStateFlow<String?>(null)
    override val clientId = MutableStateFlow(initialClientId)
    override val appTheme = MutableStateFlow(MifosThemeConfig.FOLLOW_SYSTEM)
    override val profileImage: String? = null
    override val sentTokenToServer = MutableStateFlow(false)
    override val gcmToken = MutableStateFlow<String?>(null)
    override val observeLanguage = flowOf(LanguageConfig.DEFAULT)
    override val observeDarkThemeConfig = flowOf(MifosThemeConfig.FOLLOW_SYSTEM)
    override val observeTimeBasedThemeConfig = flowOf(AppSettings.DEFAULT.timeBasedTheme)
    override val observeDynamicColorPreference = flowOf(false)
    override val passcode = flowOf("")
    override var selectedServices: Set<String>? = null

    override suspend fun updateToken(password: String): DataState<Unit> = DataState.Success(Unit)
    override suspend fun updateTheme(theme: MifosThemeConfig): DataState<Unit> = DataState.Success(Unit)
    override suspend fun updateTimeBasedTheme(theme: TimeBasedTheme): DataState<Unit> = DataState.Success(Unit)
    override suspend fun updateUser(user: UserData): DataState<Unit> = DataState.Success(Unit)
    override suspend fun updateSettings(appSettings: AppSettings): DataState<Unit> = DataState.Success(Unit)
    override suspend fun updateProfileImage(image: String): DataState<Unit> = DataState.Success(Unit)

    override suspend fun updateClientId(clientId: Long?): DataState<Unit> {
        this.clientId.value = clientId
        return DataState.Success(Unit)
    }

    override suspend fun setSentTokenToServer(sent: Boolean): DataState<Unit> = DataState.Success(Unit)
    override suspend fun saveGcmToken(token: String?): DataState<Unit> = DataState.Success(Unit)
    override suspend fun setIsAuthenticated(isAuthenticated: Boolean) = Unit
    override suspend fun setIsUnlocked(isUnlocked: Boolean) = Unit
    override suspend fun setPasscode(passcode: String) = Unit
    override suspend fun setShowOnboarding(showOnboarding: Boolean) = Unit
    override suspend fun setFirstTimeState(firstTimeState: Boolean) = Unit
    override suspend fun setLanguage(language: LanguageConfig) = Unit
    override suspend fun setSelectedServices(selectedServices: Set<String>?) {
        this.selectedServices = selectedServices
    }
    override fun saveSelectedServices(services: Set<String>?) {
        selectedServices = services
    }
    override suspend fun logOut() = Unit
}

internal fun detailedPocketAccount(
    accountId: Long,
    accountType: AccountType,
    accountNumber: String,
    productName: String?,
    balance: Double? = 100.0,
    currencyCode: String? = "USD",
    decimalPlaces: Int? = 2,
    status: AccountStatus? = AccountStatus.ACTIVE,
    mappingId: Long = accountId,
) = DetailedPocketAccount(
    pocket = PocketAccount(
        pocketId = mappingId,
        id = mappingId,
        accountId = accountId,
        accountType = accountType,
        accountNumber = accountNumber,
    ),
    productName = productName,
    balance = balance,
    currencyCode = currencyCode,
    decimalPlaces = decimalPlaces,
    status = status,
)

internal fun linkableAccount(
    accountId: Long,
    accountType: AccountType,
    accountNumber: String,
    productName: String?,
    balance: Double? = 100.0,
    currencyCode: String? = "USD",
    decimalPlaces: Int? = 2,
    status: AccountStatus? = AccountStatus.ACTIVE,
) = LinkableAccount(
    accountId = accountId,
    productName = productName,
    accountNumber = accountNumber,
    accountType = accountType,
    balance = balance,
    currencyCode = currencyCode,
    decimalPlaces = decimalPlaces,
    status = status,
)
