package org.mifos.selfserviceapp.injection.component;

import org.mifos.selfserviceapp.injection.PerActivity;
import org.mifos.selfserviceapp.injection.module.ActivityModule;
import org.mifos.selfserviceapp.ui.activities.HomeActivity;
import org.mifos.selfserviceapp.ui.fragments.BeneficiaryAddOptionsFragment;
import org.mifos.selfserviceapp.ui.activities.PassCodeActivity;
import org.mifos.selfserviceapp.ui.fragments.BeneficiaryApplicationFragment;
import org.mifos.selfserviceapp.ui.fragments.BeneficiaryDetailFragment;
import org.mifos.selfserviceapp.ui.fragments.BeneficiaryListFragment;
import org.mifos.selfserviceapp.ui.fragments.HelpFragment;
import org.mifos.selfserviceapp.ui.fragments.LoanAccountTransactionFragment;
import org.mifos.selfserviceapp.ui.fragments.LoanAccountWithdrawFragment;
import org.mifos.selfserviceapp.ui.fragments.LoanAccountsDetailFragment;
import org.mifos.selfserviceapp.ui.activities.LoginActivity;
import org.mifos.selfserviceapp.ui.fragments.LoanApplicationFragment;
import org.mifos.selfserviceapp.ui.fragments.RegistrationFragment;
import org.mifos.selfserviceapp.ui.fragments.RegistrationVerificationFragment;
import org.mifos.selfserviceapp.ui.fragments.SavingAccountsDetailFragment;
import org.mifos.selfserviceapp.ui.fragments.AccountsFragment;
import org.mifos.selfserviceapp.ui.fragments.HomeFragment;
import org.mifos.selfserviceapp.ui.fragments.ClientAccountsFragment;
import org.mifos.selfserviceapp.ui.fragments.ClientChargeFragment;
import org.mifos.selfserviceapp.ui.fragments.LoanAccountSummaryFragment;
import org.mifos.selfserviceapp.ui.fragments.LoanRepaymentScheduleFragment;
import org.mifos.selfserviceapp.ui.fragments.RecentTransactionsFragment;
import org.mifos.selfserviceapp.ui.fragments.SavingAccountsTransactionFragment;
import org.mifos.selfserviceapp.ui.fragments.SavingsMakeTransferFragment;
import org.mifos.selfserviceapp.ui.fragments.ThirdPartyTransferFragment;
import org.mifos.selfserviceapp.ui.fragments.TransferProcessFragment;
import org.mifos.selfserviceapp.ui.fragments.UserProfileFragment;

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

}
