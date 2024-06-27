package org.mifos.mobile.core.data.di

import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.datastore.PreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mifos.mobile.core.data.repositories.*
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun providesUserAuthRepository(dataManager: DataManager): UserAuthRepository {
        return UserAuthRepositoryImp(dataManager)
    }

    @Provides
    fun providesSavingsAccountRepository(dataManager: DataManager): SavingsAccountRepository {
        return SavingsAccountRepositoryImp(dataManager)
    }

    @Provides
    fun providesLoanRepository(dataManager: DataManager): LoanRepository {
        return LoanRepositoryImp(dataManager)
    }

    @Provides
    fun providesNotificationRepository(dataManager: DataManager): NotificationRepository {
        return NotificationRepositoryImp(dataManager)
    }

    @Provides
    fun providesClientRepository(
        dataManager: DataManager, preferencesHelper: org.mifos.mobile.core.datastore.PreferencesHelper, retrofit: Retrofit
    ): ClientRepository {
        return ClientRepositoryImp(
            dataManager,
            preferencesHelper,
            retrofit
        )
    }

    @Provides
    fun providesRecentTransactionRepository(dataManager: DataManager): RecentTransactionRepository {
        return RecentTransactionRepositoryImp(dataManager)
    }

    @Provides
    fun provideAccountsRepository(dataManager: DataManager): AccountsRepository {
        return AccountsRepositoryImp(dataManager)
    }

    @Provides
    fun providesGuarantorRepository(dataManager: DataManager): GuarantorRepository {
        return GuarantorRepositoryImp(dataManager)
    }

    @Provides
    fun providesBeneficiaryRepository(dataManager: DataManager): BeneficiaryRepository {
        return BeneficiaryRepositoryImp(dataManager)
    }

    @Provides
    fun providesTransferRepository(dataManager: DataManager): TransferRepository {
        return TransferRepositoryImp(dataManager)
    }

    @Provides
    fun providesThirdPartyTransferRepository(dataManager: DataManager): ThirdPartyTransferRepository {
        return ThirdPartyTransferRepositoryImp(dataManager)
    }

    @Provides
    fun providesClientChargeRepository(dataManager: DataManager): ClientChargeRepository {
        return ClientChargeRepositoryImp(dataManager)
    }

    @Provides
    fun providesHomeRepository(dataManager: DataManager): HomeRepository {
        return HomeRepositoryImp(dataManager)
    }

    @Provides
    fun providesUserDetailRepository(dataManager: DataManager): UserDetailRepository {
        return UserDetailRepositoryImp(dataManager)
    }

    @Provides
    fun providesReviewLoanApplicationRepository(dataManager: DataManager): ReviewLoanApplicationRepository {
        return ReviewLoanApplicationRepositoryImpl(dataManager)
    }

}
