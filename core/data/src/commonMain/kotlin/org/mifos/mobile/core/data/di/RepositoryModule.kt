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

import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.mifos.mobile.core.common.MifosDispatchers
import org.mifos.mobile.core.data.repository.AccountsRepository
import org.mifos.mobile.core.data.repository.BeneficiaryRepository
import org.mifos.mobile.core.data.repository.ClientChargeRepository
import org.mifos.mobile.core.data.repository.ClientRepository
import org.mifos.mobile.core.data.repository.GuarantorRepository
import org.mifos.mobile.core.data.repository.HomeRepository
import org.mifos.mobile.core.data.repository.LoanRepository
import org.mifos.mobile.core.data.repository.NotificationRepository
import org.mifos.mobile.core.data.repository.RecentTransactionRepository
import org.mifos.mobile.core.data.repository.ReviewLoanApplicationRepository
import org.mifos.mobile.core.data.repository.SavingsAccountRepository
import org.mifos.mobile.core.data.repository.ShareAccountRepository
import org.mifos.mobile.core.data.repository.ThirdPartyTransferRepository
import org.mifos.mobile.core.data.repository.TransferRepository
import org.mifos.mobile.core.data.repository.UserAuthRepository
import org.mifos.mobile.core.data.repository.UserDataRepository
import org.mifos.mobile.core.data.repository.UserDetailRepository
import org.mifos.mobile.core.data.repositoryImpl.AccountsRepositoryImp
import org.mifos.mobile.core.data.repositoryImpl.AuthenticationUserRepository
import org.mifos.mobile.core.data.repositoryImpl.BeneficiaryRepositoryImp
import org.mifos.mobile.core.data.repositoryImpl.ClientChargeRepositoryImp
import org.mifos.mobile.core.data.repositoryImpl.ClientRepositoryImp
import org.mifos.mobile.core.data.repositoryImpl.GuarantorRepositoryImp
import org.mifos.mobile.core.data.repositoryImpl.HomeRepositoryImp
import org.mifos.mobile.core.data.repositoryImpl.LoanRepositoryImp
import org.mifos.mobile.core.data.repositoryImpl.NotificationRepositoryImp
import org.mifos.mobile.core.data.repositoryImpl.RecentTransactionRepositoryImp
import org.mifos.mobile.core.data.repositoryImpl.ReviewLoanApplicationRepositoryImpl
import org.mifos.mobile.core.data.repositoryImpl.SavingsAccountRepositoryImp
import org.mifos.mobile.core.data.repositoryImpl.ShareAccountRepositoryImp
import org.mifos.mobile.core.data.repositoryImpl.ThirdPartyTransferRepositoryImp
import org.mifos.mobile.core.data.repositoryImpl.TransferRepositoryImp
import org.mifos.mobile.core.data.repositoryImpl.UserAuthRepositoryImp
import org.mifos.mobile.core.data.repositoryImpl.UserDetailRepositoryImp
import org.mifos.mobile.core.data.util.NetworkMonitor
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
import org.mobilenativefoundation.store.store5.Store

private val ioDispatcher = named(MifosDispatchers.IO.name)
private val unconfinedDispatcher = named(MifosDispatchers.Unconfined.name)

val RepositoryModule = module {

    single<Json> { Json { ignoreUnknownKeys = true } }

    single<AccountsRepository> {
        AccountsRepositoryImp(get<Store<Long, ClientAccounts>>(StoreRegistry.Accounts), get(ioDispatcher))
    }
    single<UserDataRepository> { AuthenticationUserRepository(get(), get(ioDispatcher), get(unconfinedDispatcher)) }
    single<BeneficiaryRepository> {
        BeneficiaryRepositoryImp(
            get(),
            get<Store<Unit, List<Beneficiary>>>(StoreRegistry.Beneficiary),
            get(ioDispatcher),
        )
    }
    single<ClientChargeRepository> {
        ClientChargeRepositoryImp(get(), get<Store<Long, List<Charge>>>(StoreRegistry.Charge), get(), get(ioDispatcher))
    }
    single<ClientRepository> { ClientRepositoryImp(get(), get(ioDispatcher)) }
    single<GuarantorRepository> { GuarantorRepositoryImp(get(), get(ioDispatcher)) }
    single<HomeRepository> {
        HomeRepositoryImp(
            get(),
            get(),
            get<Store<Long, Client>>(StoreRegistry.Client),
            get<Store<Long, ClientAccounts>>(StoreRegistry.Accounts),
            get(ioDispatcher),
        )
    }
    single<LoanRepository> {
        LoanRepositoryImp(
            get(),
            get<Store<Long, LoanWithAssociations>>(StoreRegistry.LoanDetails),
            get<Store<Long, LoanTemplate>>(StoreRegistry.LoanTemplate),
            get(ioDispatcher),
        )
    }
    single<NotificationRepository> { NotificationRepositoryImp(get(), get(ioDispatcher)) }
    single<RecentTransactionRepository> {
        RecentTransactionRepositoryImp(
            get<Store<Long, List<Transaction>>>(StoreRegistry.Transaction),
            get(ioDispatcher),
        )
    }
    single<ReviewLoanApplicationRepository> { ReviewLoanApplicationRepositoryImpl(get(), get(ioDispatcher)) }
    single<SavingsAccountRepository> {
        SavingsAccountRepositoryImp(
            get(),
            get<Store<Long, SavingsWithAssociations>>(StoreRegistry.SavingsDetails),
            get<Store<Long, SavingsAccountTemplate>>(StoreRegistry.SavingsTemplate),
            get(ioDispatcher),
        )
    }
    single<ThirdPartyTransferRepository> { ThirdPartyTransferRepositoryImp(get(), get(ioDispatcher)) }
    single<TransferRepository> { TransferRepositoryImp(get(), get(ioDispatcher)) }
    single<UserAuthRepository> { UserAuthRepositoryImp(get(), get(ioDispatcher)) }
    single<UserDetailRepository> { UserDetailRepositoryImp(get(), get(ioDispatcher)) }
    single<ShareAccountRepository> {
        ShareAccountRepositoryImp(
            get(),
            get<Store<Long, List<ShareProduct>>>(StoreRegistry.ShareProducts),
            get(ioDispatcher),
        )
    }
    includes(platformModule)
    single<PlatformDependentDataModule> { getPlatformDataModule }
    single<NetworkMonitor> { getPlatformDataModule.networkMonitor }
}
