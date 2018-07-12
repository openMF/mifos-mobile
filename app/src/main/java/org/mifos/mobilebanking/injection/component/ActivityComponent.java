package org.mifos.mobilebanking.injection.component;

import org.mifos.mobilebanking.injection.PerActivity;
import org.mifos.mobilebanking.injection.module.ActivityModule;
import org.mifos.mobilebanking.ui.activities.HomeActivity;
import org.mifos.mobilebanking.ui.activities.SplashActivity;
import org.mifos.mobilebanking.ui.fragments.AccountOverviewFragment;
import org.mifos.mobilebanking.ui.fragments.AddGuarantorFragment;
import org.mifos.mobilebanking.ui.fragments.BeneficiaryAddOptionsFragment;
import org.mifos.mobilebanking.ui.activities.PassCodeActivity;
import org.mifos.mobilebanking.ui.fragments.BeneficiaryApplicationFragment;
import org.mifos.mobilebanking.ui.fragments.BeneficiaryDetailFragment;
import org.mifos.mobilebanking.ui.fragments.BeneficiaryListFragment;
import org.mifos.mobilebanking.ui.fragments.GuarantorDetailFragment;
import org.mifos.mobilebanking.ui.fragments.GuarantorListFragment;
import org.mifos.mobilebanking.ui.fragments.HelpFragment;
import org.mifos.mobilebanking.ui.fragments.HomeOldFragment;
import org.mifos.mobilebanking.ui.fragments.LoanAccountTransactionFragment;
import org.mifos.mobilebanking.ui.fragments.LoanAccountWithdrawFragment;
import org.mifos.mobilebanking.ui.fragments.LoanAccountsDetailFragment;
import org.mifos.mobilebanking.ui.activities.LoginActivity;
import org.mifos.mobilebanking.ui.fragments.LoanApplicationFragment;
import org.mifos.mobilebanking.ui.fragments.NotificationFragment;
import org.mifos.mobilebanking.ui.fragments.QrCodeImportFragment;
import org.mifos.mobilebanking.ui.fragments.RegistrationFragment;
import org.mifos.mobilebanking.ui.fragments.RegistrationVerificationFragment;
import org.mifos.mobilebanking.ui.fragments.SavingAccountsDetailFragment;
import org.mifos.mobilebanking.ui.fragments.AccountsFragment;
import org.mifos.mobilebanking.ui.fragments.HomeFragment;
import org.mifos.mobilebanking.ui.fragments.ClientAccountsFragment;
import org.mifos.mobilebanking.ui.fragments.ClientChargeFragment;
import org.mifos.mobilebanking.ui.fragments.LoanAccountSummaryFragment;
import org.mifos.mobilebanking.ui.fragments.LoanRepaymentScheduleFragment;
import org.mifos.mobilebanking.ui.fragments.RecentTransactionsFragment;
import org.mifos.mobilebanking.ui.fragments.SavingAccountsTransactionFragment;
import org.mifos.mobilebanking.ui.fragments.SavingsAccountApplicationFragment;
import org.mifos.mobilebanking.ui.fragments.SavingsAccountWithdrawFragment;
import org.mifos.mobilebanking.ui.fragments.SavingsMakeTransferFragment;
import org.mifos.mobilebanking.ui.fragments.ThirdPartyTransferFragment;
import org.mifos.mobilebanking.ui.fragments.TransferProcessFragment;
import org.mifos.mobilebanking.ui.fragments.UpdatePasswordFragment;
import org.mifos.mobilebanking.ui.fragments.UserProfileFragment;

import dagger.Component;

/**
 * @author ishan
 * @since 08/07/16
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(LoginActivity loginActivity);

    void inject(HomeActivity homeActivity);

    void inject(PassCodeActivity passCodeActivity);

    void inject(HomeFragment homeFragment);

    void inject(ClientAccountsFragment clientAccountsFragment);

    void inject(RecentTransactionsFragment recentTransactionsFragment);

    void inject(ClientChargeFragment clientChargeFragment);

    void inject(SavingAccountsDetailFragment savingAccountsDetailActivity);

    void inject(LoanAccountsDetailFragment loanAccountsDetailActivity);

    void inject(AccountsFragment accountsFragment);

    void inject(LoanAccountSummaryFragment loanAccountSummaryFragment);

    void inject(LoanAccountTransactionFragment loanAccountTransactionFragment);

    void inject(LoanRepaymentScheduleFragment loanRepaymentScheduleFragment);

    void inject(LoanApplicationFragment loanApplicationFragment);

    void inject(LoanAccountWithdrawFragment loanAccountWithdrawFragment);

    void inject(SavingAccountsTransactionFragment savingAccountsTransactionFragment);

    void inject(SavingsMakeTransferFragment savingsMakeTransferFragment);

    void inject(BeneficiaryAddOptionsFragment beneficiaryAddOptionsFragment);

    void inject(BeneficiaryListFragment beneficiaryListFragment);

    void inject(BeneficiaryApplicationFragment beneficiaryApplicationFragment);

    void inject(BeneficiaryDetailFragment beneficiaryDetailFragment);

    void inject(ThirdPartyTransferFragment thirdPartyTransferFragment);

    void inject(TransferProcessFragment transferProcessFragment);

    void inject(UserProfileFragment userProfileFragment);

    void inject(HelpFragment helpFragment);

    void inject(RegistrationFragment registrationFragment);

    void inject(RegistrationVerificationFragment registrationVerificationFragment);

    void inject(AccountOverviewFragment accountOverviewFragment);

    void inject(HomeOldFragment homeOldFragment);

    void inject(NotificationFragment notificationFragment);
    
    void inject(QrCodeImportFragment qrCodeImportFragment);

    void inject(SplashActivity splashActivity);

    void inject(AddGuarantorFragment addGuarantorFragment);

    void inject(GuarantorListFragment guarantorListFragment);

    void inject(GuarantorDetailFragment guarantorDetailFragment);

    void inject(UpdatePasswordFragment updatePasswordFragment);

    void inject(SavingsAccountApplicationFragment savingsAccountApplicationFragment);

    void inject(SavingsAccountWithdrawFragment savingsAccountWithdrawFragment);
}
