package org.mifos.mobile.injection.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.repositories.*

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun providesUserAuthRepository(dataManager: DataManager): UserAuthRepository {
        return UserAuthRepositoryImp(dataManager)
    }

    @Provides
    fun providesHomeRepository(dataManager: DataManager): HomeRepository {
        return HomeRepositoryImp(dataManager)
    }

    @Provides
    fun providesUserDetailRepository(dataManager: DataManager): UserDetailRepository {
        return UserDetailRepositoryImp(dataManager)
    }
}