package org.mifos.mobile.injection.module

import dagger.Module
import dagger.Provides
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.repositories.UserAuthRepository
import org.mifos.mobile.repositories.UserAuthRepositoryImp

@Module
class RepositoryModule {

    @Provides
    fun providesUserAuthRepository(dataManager: DataManager): UserAuthRepository {
        return UserAuthRepositoryImp(dataManager)
    }
}