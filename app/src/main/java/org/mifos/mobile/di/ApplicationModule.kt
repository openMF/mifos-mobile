package org.mifos.mobile.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.mifos.mobile.api.BaseApiManager
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.api.local.DatabaseHelper
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.api.services.AuthenticationService
import org.mifos.mobile.api.services.BeneficiaryService
import org.mifos.mobile.api.services.ClientChargeService
import org.mifos.mobile.api.services.ClientService
import org.mifos.mobile.api.services.GuarantorService
import org.mifos.mobile.api.services.LoanAccountsListService
import org.mifos.mobile.api.services.NotificationService
import org.mifos.mobile.api.services.RecentTransactionsService
import org.mifos.mobile.api.services.RegistrationService
import org.mifos.mobile.api.services.SavingAccountsListService
import org.mifos.mobile.api.services.ThirdPartyTransferService
import org.mifos.mobile.api.services.UserDetailsService
import javax.inject.Singleton

/**
 * @author ishan
 * @since 08/07/16
 */
@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun providePrefManager(@ApplicationContext context: Context?): PreferencesHelper {
        return PreferencesHelper(context)
    }

    @Provides
    @Singleton
    fun provideBaseApiManager(
        authenticationService: AuthenticationService,
        clientsService: ClientService,
        savingAccountsListService: SavingAccountsListService,
        loanAccountsListService: LoanAccountsListService,
        recentTransactionsService: RecentTransactionsService,
        clientChargeService: ClientChargeService,
        beneficiaryService: BeneficiaryService,
        thirdPartyTransferService: ThirdPartyTransferService,
        registrationService: RegistrationService,
        notificationService: NotificationService,
        userDetailsService: UserDetailsService,
        guarantorService: GuarantorService
    ): BaseApiManager {
        return BaseApiManager(
            authenticationService,
            clientsService,
            savingAccountsListService,
            loanAccountsListService,
            recentTransactionsService,
            clientChargeService,
            beneficiaryService,
            thirdPartyTransferService,
            registrationService,
            notificationService,
            userDetailsService,
            guarantorService
        )
    }

    @Provides
    @Singleton
    fun providesDataManager(
        preferencesHelper: PreferencesHelper,
        baseApiManager: BaseApiManager,
        databaseHelper: DatabaseHelper
    ): DataManager {
        return DataManager(preferencesHelper, baseApiManager, databaseHelper)
    }
}
