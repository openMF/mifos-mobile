package org.mifos.selfserviceapp.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.ui.activities.BaseActivity;
import org.mifos.selfserviceapp.ui.adapters.ViewPagerAdapter;
import org.mifos.selfserviceapp.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 14/07/2016
 */
public class ClientAccountsFragment extends Fragment {
    private int clientId;

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_client_accounts, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);

        clientId = this.getArguments().getInt(Constants.CLIENT_ID);

        setUpViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPagerAdapter.addFragment(SavingAccountsListFragment.newInstance(clientId), getString(R.string.saving_accounts));
        viewPagerAdapter.addFragment(LoanAccountsListFragment.newInstance(clientId), getString(R.string.loan_accounts));
        viewPager.setAdapter(viewPagerAdapter);
    }

    public static ClientAccountsFragment newInstance(int clientId) {
        ClientAccountsFragment clientAccountsFragment = new ClientAccountsFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.CLIENT_ID, clientId);
        clientAccountsFragment.setArguments(args);
        return clientAccountsFragment;
    }
}