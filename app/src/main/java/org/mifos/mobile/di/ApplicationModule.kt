package org.mifos.mobile.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.mifos.mobile.core.datastore.DatabaseHelper
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.core.network.BaseApiManager
import org.mifos.mobile.core.network.DataManager
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
