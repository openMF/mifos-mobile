package org.mifos.mobile.injection.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.repositories.SavingsAccountRepository
import org.mifos.mobile.repositories.SavingsAccountRepositoryImp
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.repositories.ClientRepository
import org.mifos.mobile.repositories.ClientRepositoryImp
import org.mifos.mobile.repositories.RecentTransactionRepository
import org.mifos.mobile.repositories.RecentTransactionRepositoryImp
import org.mifos.mobile.repositories.UserAuthRepository
import org.mifos.mobile.repositories.UserAuthRepositoryImp

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun providesUserAuthRepository(dataManager: DataManager): UserAuthRepository {
        return UserAuthRepositoryImp(dataManager)
    }

    @Provides
    fun providesSavingsAccountRepository(dataManager: DataManager) : SavingsAccountRepository {
        return SavingsAccountRepositoryImp(dataManager)
    }

    @Provides
    fun providesClientRepository(preferencesHelper: PreferencesHelper): ClientRepository {
        return ClientRepositoryImp(preferencesHelper)
    }

    @Provides
    fun providesRecentTransactionRepository(dataManager: DataManager): RecentTransactionRepository {
        return  RecentTransactionRepositoryImp(dataManager)
    }
}