/*
 * Copyright 2025 Mifos Initiative
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
import org.mifos.mobile.core.data.repositoryImpl.ThirdPartyTransferRepositoryImp
import org.mifos.mobile.core.data.repositoryImpl.TransferRepositoryImp
import org.mifos.mobile.core.data.repositoryImpl.UserAuthRepositoryImp
import org.mifos.mobile.core.data.repositoryImpl.UserDetailRepositoryImp
import org.mifos.mobile.core.data.util.NetworkMonitor

private val ioDispatcher = named(MifosDispatchers.IO.name)

val RepositoryModule = module {

    single<Json> { Json { ignoreUnknownKeys = true } }

    single<AccountsRepository> { AccountsRepositoryImp(get(), get(ioDispatcher)) }
    single<UserDataRepository> { AuthenticationUserRepository(get(), get(ioDispatcher)) }
    single<BeneficiaryRepository> { BeneficiaryRepositoryImp(get(), get(ioDispatcher)) }
    single<ClientChargeRepository> { ClientChargeRepositoryImp(get(), get(ioDispatcher)) } // TODO
    single<ClientRepository> { ClientRepositoryImp(get(), get(ioDispatcher)) }
    single<GuarantorRepository> { GuarantorRepositoryImp(get(), get(ioDispatcher)) }
    single<HomeRepository> { HomeRepositoryImp(get(), get(), get(ioDispatcher)) }
    single<LoanRepository> { LoanRepositoryImp(get(), get(ioDispatcher)) }
    single<NotificationRepository> { NotificationRepositoryImp(get(ioDispatcher)) } // TODO
    single<RecentTransactionRepository> { RecentTransactionRepositoryImp(get(), get(ioDispatcher)) }
    single<ReviewLoanApplicationRepository> { ReviewLoanApplicationRepositoryImpl(get(), get(ioDispatcher)) }
    single<SavingsAccountRepository> { SavingsAccountRepositoryImp(get(), get(ioDispatcher)) }
    single<ThirdPartyTransferRepository> { ThirdPartyTransferRepositoryImp(get(), get(ioDispatcher)) }
    single<TransferRepository> { TransferRepositoryImp(get(), get(ioDispatcher)) }
    single<UserAuthRepository> { UserAuthRepositoryImp(get(), get(ioDispatcher)) }
    single<UserDetailRepository> { UserDetailRepositoryImp(get(), get(ioDispatcher)) }
    includes(platformModule)
    single<PlatformDependentDataModule> { getPlatformDataModule }
    single<NetworkMonitor> { getPlatformDataModule.networkMonitor }
}
