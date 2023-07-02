package org.mifos.mobile.injection.component

import dagger.Component
import org.mifos.mobile.injection.PerActivity
import org.mifos.mobile.injection.module.ActivityModule
import org.mifos.mobile.injection.module.RepositoryModule
import org.mifos.mobile.ui.activities.HomeActivity
import org.mifos.mobile.ui.activities.LoginActivity
import org.mifos.mobile.ui.activities.PassCodeActivity
import org.mifos.mobile.ui.activities.SplashActivity
import org.mifos.mobile.ui.fragments.AccountOverviewFragment
import org.mifos.mobile.ui.fragments.AccountsFragment
import org.mifos.mobile.ui.fragments.AddGuarantorFragment
import org.mifos.mobile.ui.fragments.BeneficiaryAddOptionsFragment
import org.mifos.mobile.ui.fragments.BeneficiaryApplicationFragment
import org.mifos.mobile.ui.fragments.BeneficiaryDetailFragment
import org.mifos.mobile.ui.fragments.BeneficiaryListFragment
import org.mifos.mobile.ui.fragments.ClientAccountsFragment
import org.mifos.mobile.ui.fragments.ClientChargeFragment
import org.mifos.mobile.ui.fragments.GuarantorDetailFragment
import org.mifos.mobile.ui.fragments.GuarantorListFragment
import org.mifos.mobile.ui.fragments.HelpFragment
import org.mifos.mobile.ui.fragments.HomeFragment
import org.mifos.mobile.ui.fragments.HomeOldFragment
import org.mifos.mobile.ui.fragments.LoanAccountSummaryFragment
import org.mifos.mobile.ui.fragments.LoanAccountTransactionFragment
import org.mifos.mobile.ui.fragments.LoanAccountWithdrawFragment
import org.mifos.mobile.ui.fragments.LoanAccountsDetailFragment
import org.mifos.mobile.ui.fragments.LoanApplicationFragment
import org.mifos.mobile.ui.fragments.LoanRepaymentScheduleFragment
import org.mifos.mobile.ui.fragments.NotificationFragment
import org.mifos.mobile.ui.fragments.QrCodeImportFragment
import org.mifos.mobile.ui.fragments.QrCodeReaderFragment
import org.mifos.mobile.ui.fragments.RecentTransactionsFragment
import org.mifos.mobile.ui.fragments.RegistrationFragment
import org.mifos.mobile.ui.fragments.RegistrationVerificationFragment
import org.mifos.mobile.ui.fragments.ReviewLoanApplicationFragment
import org.mifos.mobile.ui.fragments.SavingAccountsDetailFragment
import org.mifos.mobile.ui.fragments.SavingAccountsTransactionFragment
import org.mifos.mobile.ui.fragments.SavingsAccountApplicationFragment
import org.mifos.mobile.ui.fragments.SavingsAccountWithdrawFragment
import org.mifos.mobile.ui.fragments.SavingsMakeTransferFragment
import org.mifos.mobile.ui.fragments.ThirdPartyTransferFragment
import org.mifos.mobile.ui.fragments.TransferProcessFragment
import org.mifos.mobile.ui.fragments.UpdatePasswordFragment
import org.mifos.mobile.ui.fragments.UserProfileFragment

/**
 * @author ishan
 * @since 08/07/16
 */
@PerActivity
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class, RepositoryModule::class])
interface ActivityComponent {
    fun inject(loginActivity: LoginActivity?)
    fun inject(homeActivity: HomeActivity?)
    fun inject(passCodeActivity: PassCodeActivity?)
    fun inject(homeFragment: HomeFragment?)
    fun inject(clientAccountsFragment: ClientAccountsFragment?)
    fun inject(recentTransactionsFragment: RecentTransactionsFragment?)
    fun inject(clientChargeFragment: ClientChargeFragment?)
    fun inject(savingAccountsDetailActivity: SavingAccountsDetailFragment?)
    fun inject(loanAccountsDetailActivity: LoanAccountsDetailFragment?)
    fun inject(accountsFragment: AccountsFragment?)
    fun inject(loanAccountSummaryFragment: LoanAccountSummaryFragment?)
    fun inject(loanAccountTransactionFragment: LoanAccountTransactionFragment?)
    fun inject(loanRepaymentScheduleFragment: LoanRepaymentScheduleFragment?)
    fun inject(loanApplicationFragment: LoanApplicationFragment?)
    fun inject(loanAccountWithdrawFragment: LoanAccountWithdrawFragment?)
    fun inject(savingAccountsTransactionFragment: SavingAccountsTransactionFragment?)
    fun inject(savingsMakeTransferFragment: SavingsMakeTransferFragment?)
    fun inject(beneficiaryAddOptionsFragment: BeneficiaryAddOptionsFragment?)
    fun inject(beneficiaryListFragment: BeneficiaryListFragment?)
    fun inject(beneficiaryApplicationFragment: BeneficiaryApplicationFragment?)
    fun inject(beneficiaryDetailFragment: BeneficiaryDetailFragment?)
    fun inject(thirdPartyTransferFragment: ThirdPartyTransferFragment?)
    fun inject(transferProcessFragment: TransferProcessFragment?)
    fun inject(userProfileFragment: UserProfileFragment?)
    fun inject(helpFragment: HelpFragment?)
    fun inject(registrationFragment: RegistrationFragment?)
    fun inject(registrationVerificationFragment: RegistrationVerificationFragment?)
    fun inject(accountOverviewFragment: AccountOverviewFragment?)
    fun inject(homeOldFragment: HomeOldFragment?)
    fun inject(notificationFragment: NotificationFragment?)
    fun inject(qrCodeImportFragment: QrCodeImportFragment?)
    fun inject(splashActivity: SplashActivity?)
    fun inject(addGuarantorFragment: AddGuarantorFragment?)
    fun inject(guarantorListFragment: GuarantorListFragment?)
    fun inject(guarantorDetailFragment: GuarantorDetailFragment?)
    fun inject(updatePasswordFragment: UpdatePasswordFragment?)
    fun inject(savingsAccountApplicationFragment: SavingsAccountApplicationFragment?)
    fun inject(savingsAccountWithdrawFragment: SavingsAccountWithdrawFragment?)
    fun inject(reviewLoanApplicationFragment: ReviewLoanApplicationFragment?)
    fun inject(qrCodeReaderFragment: QrCodeReaderFragment?)
}
