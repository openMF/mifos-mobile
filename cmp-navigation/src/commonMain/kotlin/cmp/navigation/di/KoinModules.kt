/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package cmp.navigation.di

import cmp.navigation.ComposeAppViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.mifos.library.passcode.di.PasscodeModule
import org.mifos.mobile.core.common.di.DispatchersModule
import org.mifos.mobile.core.data.di.RepositoryModule
import org.mifos.mobile.core.datastore.di.PreferencesModule
import org.mifos.mobile.core.network.di.NetworkModule
import org.mifos.mobile.feature.accounts.di.accountsModule
import org.mifos.mobile.feature.auth.di.AuthModule
import org.mifos.mobile.feature.beneficiary.di.BeneficiaryModule
import org.mifos.mobile.feature.charge.di.ChargeModule
import org.mifos.mobile.feature.guarantor.di.GuarantorModule
import org.mifos.mobile.feature.help.di.HelpModule
import org.mifos.mobile.feature.home.di.HomeModule
import org.mifos.mobile.feature.loan.di.LoanModule
import org.mifos.mobile.feature.loanaccount.di.loanAccountModule
import org.mifos.mobile.feature.qr.di.QrModule
import org.mifos.mobile.feature.recent.transaction.di.recentTransactionModule
import org.mifos.mobile.feature.savings.di.SavingsModule
import org.mifos.mobile.feature.savingsaccount.di.savingsAccountModule
import org.mifos.mobile.feature.settings.di.SettingsModule
import org.mifos.mobile.feature.shareaccount.di.shareAccountModule
import org.mifos.mobile.feature.third.party.transfer.di.ThirdPartyTransferModule
import org.mifos.mobile.feature.transfer.process.di.TransferProcessModule
import org.mifos.mobile.feature.update.password.di.updatePasswordModule
import org.mifos.mobile.feature.user.profile.di.ProfileModule

object KoinModules {
    private val commonModules = module {
        includes(DispatchersModule)
    }
    private val dataModules = module {
        includes(RepositoryModule)
    }
    private val coreDataStoreModules = module {
        includes(PreferencesModule)
    }
    private val networkModules = module {
        includes(NetworkModule)
    }
    private val sharedModule = module {
        viewModelOf(::ComposeAppViewModel)
    }
    private val featureModules = module {
        includes(
            AuthModule,
            HelpModule,
            HomeModule,
            accountsModule,
            savingsAccountModule,
            loanAccountModule,
            shareAccountModule,
            recentTransactionModule,
            LoanModule,
            SavingsModule,
            ChargeModule,
            TransferProcessModule,
            SettingsModule,
            ThirdPartyTransferModule,
            updatePasswordModule,
            BeneficiaryModule,
            QrModule,
            GuarantorModule,
            ProfileModule,
        )
    }
    private val LibraryModule = module {
        includes(PasscodeModule)
    }

    val allModules = listOf(
        commonModules,
        dataModules,
        coreDataStoreModules,
        networkModules,
        featureModules,
        sharedModule,
        LibraryModule,
    )
}
