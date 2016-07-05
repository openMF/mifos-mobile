package org.mifos.selfserviceapp.ui.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.adapters.ViewPagerAdapter;
import org.mifos.selfserviceapp.ui.fragments.LoanAccountsListFragment;
import org.mifos.selfserviceapp.ui.fragments.SavingAccountsListFragment;
import org.mifos.selfserviceapp.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 21/6/16.
 */
public class ClientAccountsActivity extends AppCompatActivity{

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    private int clientId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_accounts);
        ButterKnife.bind(this);
        clientId = getIntent().getExtras().getInt(Constants.CLIENT_ID);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(SavingAccountsListFragment.newInstance(clientId), getString(R.string.saving_accounts));
        viewPagerAdapter.addFragment(LoanAccountsListFragment.newInstance(clientId), getString(R.string.loan_accounts));
        viewPager.setAdapter(viewPagerAdapter);
    }
}
