package org.mifos.selfserviceapp.injection.component;

import org.mifos.selfserviceapp.injection.PerActivity;
import org.mifos.selfserviceapp.injection.module.ActivityModule;
import org.mifos.selfserviceapp.ui.activities.HomeActivity;
import org.mifos.selfserviceapp.ui.fragments.LoanAccountTransactionFragment;
import org.mifos.selfserviceapp.ui.fragments.LoanAccountsDetailFragment;
import org.mifos.selfserviceapp.ui.activities.LoginActivity;
import org.mifos.selfserviceapp.ui.fragments.LoanApplicationFragment;
import org.mifos.selfserviceapp.ui.fragments.SavingAccountsDetailFragment;
import org.mifos.selfserviceapp.ui.fragments.AccountsFragment;
import org.mifos.selfserviceapp.ui.fragments.HomeFragment;
import org.mifos.selfserviceapp.ui.fragments.HomeScreenFragment;
import org.mifos.selfserviceapp.ui.fragments.ClientAccountsFragment;
import org.mifos.selfserviceapp.ui.fragments.ClientChargeFragment;
import org.mifos.selfserviceapp.ui.fragments.LoanAccountSummaryFragment;
import org.mifos.selfserviceapp.ui.fragments.LoanRepaymentScheduleFragment;
import org.mifos.selfserviceapp.ui.fragments.RecentTransactionsFragment;

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

    void inject(HomeFragment homeFragment);

    void inject(HomeScreenFragment homeScreenFragment);

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

}
