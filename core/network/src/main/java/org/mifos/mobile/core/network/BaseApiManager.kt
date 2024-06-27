package org.mifos.mobile.core.network

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
import org.mifos.mobile.core.network.services.*
import javax.inject.Inject

/**
 * @since 13/6/16
 */
class BaseApiManager @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val clientsService: ClientService,
    private val savingAccountsListService: SavingAccountsListService,
    private val loanAccountsListService: LoanAccountsListService,
    private val recentTransactionsService: RecentTransactionsService,
    private val clientChargeService: ClientChargeService,
    private val beneficiaryService: BeneficiaryService,
    private val thirdPartyTransferService: ThirdPartyTransferService,
    private val registrationService: RegistrationService,
    private val notificationService: NotificationService,
    private val userDetailsService: UserDetailsService,
    private val guarantorService: GuarantorService
) {
    val authenticationApi: AuthenticationService
        get() = authenticationService
    val clientsApi: ClientService
        get() = clientsService
    val savingAccountsListApi: SavingAccountsListService
        get() = savingAccountsListService
    val loanAccountsListApi: LoanAccountsListService
        get() = loanAccountsListService
    val recentTransactionsApi: RecentTransactionsService
        get() = recentTransactionsService
    val clientChargeApi: ClientChargeService
        get() = clientChargeService
    val beneficiaryApi: BeneficiaryService
        get() = beneficiaryService
    val thirdPartyTransferApi: ThirdPartyTransferService
        get() = thirdPartyTransferService
    val registrationApi: RegistrationService
        get() = registrationService
    val notificationApi: NotificationService
        get() = notificationService
    val userDetailsApi: UserDetailsService
        get() = userDetailsService
    val guarantorApi: GuarantorService
        get() = guarantorService
}
