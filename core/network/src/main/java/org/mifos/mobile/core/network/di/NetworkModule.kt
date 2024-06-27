package org.mifos.mobile.core.network.di

import org.mifos.mobile.core.datastore.PreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mifos.mobile.core.datastore.BaseURL
import org.mifos.mobile.core.network.SelfServiceOkHttpClient
import org.mifos.mobile.core.network.services.AuthenticationService
import org.mifos.mobile.core.network.services.BeneficiaryService
import org.mifos.mobile.core.network.services.ClientChargeService
import org.mifos.mobile.core.network.services.ClientService
import org.mifos.mobile.core.network.services.GuarantorService
import org.mifos.mobile.core.network.services.LoanAccountsListService
import org.mifos.mobile.core.network.services.NotificationService
import org.mifos.mobile.core.network.services.RecentTransactionsService
import org.mifos.mobile.core.network.services.RegistrationService
import org.mifos.mobile.core.network.services.SavingAccountsListService
import org.mifos.mobile.core.network.services.ThirdPartyTransferService
import org.mifos.mobile.core.network.services.UserDetailsService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @author pratyush
 * @since 21/8/23
 */

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesRetrofitInstance(preferencesHelper: PreferencesHelper): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BaseURL().getUrl(preferencesHelper.baseUrl!!))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(SelfServiceOkHttpClient(preferencesHelper).mifosOkHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesAuthenticationService(retrofit: Retrofit): AuthenticationService {
        return retrofit.create(AuthenticationService::class.java)
    }

    @Provides
    @Singleton
    fun providesClientService(retrofit: Retrofit): ClientService {
        return retrofit.create(ClientService::class.java)
    }

    @Provides
    @Singleton
    fun providesSavingAccountsListService(retrofit: Retrofit): SavingAccountsListService {
        return retrofit.create(SavingAccountsListService::class.java)
    }

    @Provides
    @Singleton
    fun providesLoanAccountListService(retrofit: Retrofit): LoanAccountsListService {
        return retrofit.create(LoanAccountsListService::class.java)
    }

    @Provides
    @Singleton
    fun providesRecentTransactionService(retrofit: Retrofit): RecentTransactionsService {
        return retrofit.create(RecentTransactionsService::class.java)
    }

    @Provides
    @Singleton
    fun providesClientChargeService(retrofit: Retrofit): ClientChargeService {
        return retrofit.create(ClientChargeService::class.java)
    }

    @Provides
    @Singleton
    fun providesBeneficiaryService(retrofit: Retrofit): BeneficiaryService {
        return retrofit.create(BeneficiaryService::class.java)
    }

    @Provides
    @Singleton
    fun provideThirdPartyTransferService(retrofit: Retrofit): ThirdPartyTransferService {
        return retrofit.create(ThirdPartyTransferService::class.java)
    }

    @Provides
    @Singleton
    fun providesRegistrationService(retrofit: Retrofit): RegistrationService {
        return retrofit.create(RegistrationService::class.java)
    }

    @Provides
    @Singleton
    fun providesNotificationService(retrofit: Retrofit): NotificationService {
        return retrofit.create(NotificationService::class.java)
    }

    @Provides
    @Singleton
    fun providesGuarantorService(retrofit: Retrofit): GuarantorService {
        return retrofit.create(GuarantorService::class.java)
    }

    @Provides
    @Singleton
    fun providesUserDetailsService(retrofit: Retrofit): UserDetailsService {
        return retrofit.create(UserDetailsService::class.java)
    }
}