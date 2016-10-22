package org.mifos.selfserviceapp.injection.component;

import org.mifos.selfserviceapp.injection.PerActivity;
import org.mifos.selfserviceapp.injection.module.ActivityModule;
import org.mifos.selfserviceapp.ui.activities.HomeActivity;
import org.mifos.selfserviceapp.ui.activities.LoanAccountsDetailActivity;
import org.mifos.selfserviceapp.ui.activities.LoginActivity;
import org.mifos.selfserviceapp.ui.activities.SavingAccountsDetailActivity;
import org.mifos.selfserviceapp.ui.fragments.ClientAccountsFragment;
import org.mifos.selfserviceapp.ui.fragments.ClientChargeFragment;
import org.mifos.selfserviceapp.ui.fragments.LoanAccountsListFragment;
import org.mifos.selfserviceapp.ui.fragments.RecentTransactionsFragment;
import org.mifos.selfserviceapp.ui.fragments.SavingAccountsListFragment;

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

    void inject(ClientAccountsFragment clientAccountsFragment);

    void inject(LoanAccountsListFragment loanAccountsListFragment);

    void inject(SavingAccountsListFragment savingAccountsListFragment);

    void inject(RecentTransactionsFragment recentTransactionsFragment);

    void inject(ClientChargeFragment clientChargeFragment);

    void inject(SavingAccountsDetailActivity savingAccountsDetailActivity);

    void inject(LoanAccountsDetailActivity loanAccountsDetailActivity);

}
