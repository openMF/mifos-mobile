package org.mifos.mobile.injection.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.repositories.*

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun providesUserAuthRepository(dataManager: DataManager): UserAuthRepository {
        return UserAuthRepositoryImp(dataManager)
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
        dataManager: DataManager, preferencesHelper: PreferencesHelper
    ): ClientRepository {
        return ClientRepositoryImp(dataManager, preferencesHelper)
    }

    @Provides
    fun providesRecentTransactionRepository(dataManager: DataManager): RecentTransactionRepository {
        return RecentTransactionRepositoryImp(dataManager)
    }

    @Provides
    fun providesGuarantorRepository(dataManager: DataManager): GuarantorRepository {
        return GuarantorRepositoryImp(dataManager)
    }
    
    @Provides
    fun providesBeneficiaryRepository(dataManager: DataManager): BeneficiaryRepository {
        return BeneficiaryRepositoryImp(dataManager)
    }
}