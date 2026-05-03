/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.di

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.dsl.module
import org.mifos.mobile.core.data.mapper.accounts.toModel
import org.mifos.mobile.core.data.mapper.beneficiary.toEntity
import org.mifos.mobile.core.data.mapper.beneficiary.toModel
import org.mifos.mobile.core.data.mapper.charge.toEntity
import org.mifos.mobile.core.data.mapper.charge.toModel
import org.mifos.mobile.core.data.mapper.client.toEntity
import org.mifos.mobile.core.data.mapper.client.toModel
import org.mifos.mobile.core.data.mapper.loan.toModel
import org.mifos.mobile.core.data.mapper.savings.toModel
import org.mifos.mobile.core.data.mapper.share.toModel
import org.mifos.mobile.core.data.mapper.templates.toModel
import org.mifos.mobile.core.data.mapper.transactions.toEntity
import org.mifos.mobile.core.data.mapper.transactions.toModel
import org.mifos.mobile.core.data.mapper.transactions.toRecentTransactionModel
import org.mifos.mobile.core.database.dao.BeneficiaryDao
import org.mifos.mobile.core.database.dao.ChargeDao
import org.mifos.mobile.core.database.dao.ClientDao
import org.mifos.mobile.core.database.dao.TransactionDao
import org.mifos.mobile.core.model.entity.Charge
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsWithAssociations
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.model.entity.templates.loans.LoanTemplate
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.core.model.entity.templates.shares.ShareProduct
import org.mifos.mobile.core.network.DataManager
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import template.core.base.store.StoreFactory
import template.core.base.store.di.StoreModule

/**
 * App-level Store 5 module. Creates stores backed by Room DAOs + network fetchers.
 */
val AppStoreModule = module {
    includes(StoreModule)

    // Client Store: fetches client by ID, caches in ClientDao
    single<Store<Long, Client>>(StoreRegistry.Client) {
        val dataManager: DataManager = get()
        val clientDao: ClientDao = get()
        StoreFactory.createStore(
            fetcher = Fetcher.of { clientId: Long ->
                dataManager.clientsApi.getClientForId(clientId).first().let { dto ->
                    dto.toModel()
                }
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { clientId ->
                    clientDao.getClient(clientId).map { entity ->
                        entity?.toModel()
                    }
                },
                writer = { _, client ->
                    clientDao.insert(client.toEntity())
                },
            ),
        )
    }

    // Beneficiary Store: fetches all beneficiaries, caches in BeneficiaryDao
    single<Store<Unit, List<Beneficiary>>>(StoreRegistry.Beneficiary) {
        val dataManager: DataManager = get()
        val beneficiaryDao: BeneficiaryDao = get()
        StoreFactory.createStore(
            fetcher = Fetcher.of {
                dataManager.beneficiaryApi.beneficiaryList().first().map { it.toModel() }
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = {
                    beneficiaryDao.getAllBeneficiaries().map { entities ->
                        entities.map { it.toModel() }
                    }
                },
                writer = { _, beneficiaries ->
                    beneficiaryDao.deleteAll()
                    beneficiaryDao.insertAll(beneficiaries.map { it.toEntity() })
                },
            ),
        )
    }

    // Charge Store: fetches charges by clientId, caches in ChargeDao
    single<Store<Long, List<Charge>>>(StoreRegistry.Charge) {
        val dataManager: DataManager = get()
        val chargeDao: ChargeDao = get()
        StoreFactory.createStore(
            fetcher = Fetcher.of { clientId: Long ->
                dataManager.clientChargeApi.getClientChargeList(clientId).first()
                    .pageItems.map { it.toModel() }
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { clientId ->
                    chargeDao.getChargesByClientId(clientId).map { entities ->
                        entities.map { it.toModel() }
                    }
                },
                writer = { clientId, charges ->
                    chargeDao.deleteByClientId(clientId)
                    chargeDao.syncCharges(charges.map { it.toEntity() })
                },
            ),
        )
    }

    // Transaction Store: fetches recent transactions by clientId
    single<Store<Long, List<Transaction>>>(StoreRegistry.Transaction) {
        val dataManager: DataManager = get()
        val transactionDao: TransactionDao = get()
        StoreFactory.createStore(
            fetcher = Fetcher.of { clientId: Long ->
                dataManager.recentTransactionsApi.getRecentTransactionsList(
                    clientId,
                    offset = 0,
                    limit = 50,
                ).first().pageItems.map { it.toRecentTransactionModel() }
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { clientId ->
                    transactionDao.getTransactionsByClientId(clientId).map { entities ->
                        entities.map { it.toModel() }
                    }
                },
                writer = { clientId, transactions ->
                    transactionDao.deleteByClientId(clientId)
                    transactionDao.insertAll(transactions.map { it.toEntity(clientId) })
                },
            ),
        )
    }

    // Accounts Store: fetches all client accounts (memory-only, no DB persistence)
    single<Store<Long, ClientAccounts>>(StoreRegistry.Accounts) {
        val dataManager: DataManager = get()
        StoreFactory.createMemoryStore(
            fetcher = Fetcher.of { clientId: Long ->
                dataManager.clientsApi.getClientAccounts(clientId).first().toModel()
            },
        )
    }

    // Loan Details Store: fetches loan with associations (memory-only — complex nested object)
    single<Store<Long, LoanWithAssociations>>(StoreRegistry.LoanDetails) {
        val dataManager: DataManager = get()
        StoreFactory.createMemoryStore(
            fetcher = Fetcher.of { loanId: Long ->
                dataManager.loanAccountsListApi
                    .getLoanWithAssociations(loanId, "all").first().toModel()
            },
        )
    }

    // Savings Details Store: fetches savings with associations (memory-only)
    single<Store<Long, SavingsWithAssociations>>(StoreRegistry.SavingsDetails) {
        val dataManager: DataManager = get()
        StoreFactory.createMemoryStore(
            fetcher = Fetcher.of { accountId: Long ->
                dataManager.savingAccountsListApi
                    .getSavingsWithAssociations(accountId, "all").first().toModel()
            },
        )
    }

    // Loan Template Store: memory-only (templates change frequently)
    single<Store<Long, LoanTemplate>>(StoreRegistry.LoanTemplate) {
        val dataManager: DataManager = get()
        StoreFactory.createMemoryStore(
            fetcher = Fetcher.of { clientId: Long ->
                dataManager.loanAccountsListApi.getLoanTemplate(clientId = clientId)
                    .first().toModel()
            },
        )
    }

    // Savings Template Store: memory-only
    single<Store<Long, SavingsAccountTemplate>>(StoreRegistry.SavingsTemplate) {
        val dataManager: DataManager = get()
        StoreFactory.createMemoryStore(
            fetcher = Fetcher.of { clientId: Long ->
                dataManager.savingAccountsListApi
                    .getSavingsAccountApplicationTemplate(clientId).first().toModel()
            },
        )
    }

    // Share Products Store: memory-only
    single<Store<Long, List<ShareProduct>>>(StoreRegistry.ShareProducts) {
        val dataManager: DataManager = get()
        StoreFactory.createMemoryStore(
            fetcher = Fetcher.of { clientId: Long ->
                dataManager.shareAccountApi.getShareProducts(clientId)
                    .first().pageItems.map { it.toModel() }
            },
        )
    }
}
