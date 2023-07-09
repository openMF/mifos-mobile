package org.mifos.mobile.injection.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.repositories.ClientRepository
import org.mifos.mobile.repositories.ClientRepositoryImp
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
    fun providesClientRepository(preferencesHelper: PreferencesHelper): ClientRepository {
        return ClientRepositoryImp(preferencesHelper)
    }
}