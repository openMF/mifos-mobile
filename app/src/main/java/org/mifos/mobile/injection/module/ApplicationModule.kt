package org.mifos.mobile.injection.module

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
import org.mifos.mobile.api.services.*
import javax.inject.Singleton

/**
 * @author ishan
 * @since 08/07/16
 */
@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule() {

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
