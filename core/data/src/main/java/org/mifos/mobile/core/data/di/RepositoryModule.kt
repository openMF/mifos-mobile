/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
import org.mifos.mobile.core.data.utils.ConnectivityManagerNetworkMonitor
import org.mifos.mobile.core.data.utils.NetworkMonitor

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    internal abstract fun bindsUserDataRepository(
        authenticationUserRepository: AuthenticationUserRepository,
    ): UserDataRepository

    @Binds
    internal abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor

    @Binds
    internal abstract fun providesUserAuthRepository(
        repository: UserAuthRepositoryImp,
    ): UserAuthRepository

    @Binds
    internal abstract fun providesSavingsAccountRepository(
        repository: SavingsAccountRepositoryImp,
    ): SavingsAccountRepository

    @Binds
    internal abstract fun providesLoanRepository(repository: LoanRepositoryImp): LoanRepository

    @Binds
    internal abstract fun providesNotificationRepository(
        repository: NotificationRepositoryImp,
    ): NotificationRepository

    @Binds
    internal abstract fun providesClientRepository(repository: ClientRepositoryImp): ClientRepository

    @Binds
    internal abstract fun providesRecentTransactionRepository(
        repository: RecentTransactionRepositoryImp,
    ): RecentTransactionRepository

    @Binds
    internal abstract fun provideAccountsRepository(
        repository: AccountsRepositoryImp,
    ): AccountsRepository

    @Binds
    internal abstract fun providesGuarantorRepository(
        repository: GuarantorRepositoryImp,
    ): GuarantorRepository

    @Binds
    internal abstract fun providesBeneficiaryRepository(
        repository: BeneficiaryRepositoryImp,
    ): BeneficiaryRepository

    @Binds
    internal abstract fun providesTransferRepository(
        repository: TransferRepositoryImp,
    ): TransferRepository

    @Binds
    internal abstract fun providesThirdPartyTransferRepository(
        repository: ThirdPartyTransferRepositoryImp,
    ): ThirdPartyTransferRepository

    @Binds
    internal abstract fun providesClientChargeRepository(
        repository: ClientChargeRepositoryImp,
    ): ClientChargeRepository

    @Binds
    internal abstract fun providesHomeRepository(repository: HomeRepositoryImp): HomeRepository

    @Binds
    internal abstract fun providesUserDetailRepository(
        repository: UserDetailRepositoryImp,
    ): UserDetailRepository

    @Binds
    internal abstract fun providesReviewLoanApplicationRepository(
        repository: ReviewLoanApplicationRepositoryImpl,
    ): ReviewLoanApplicationRepository
}
