package org.mifos.selfserviceapp.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.adapters.SavingAccountsListAdapter;
import org.mifos.selfserviceapp.api.BaseApiManager;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.data.accounts.SavingAccount;
import org.mifos.selfserviceapp.presenters.SavingAccountsListPresenter;
import org.mifos.selfserviceapp.ui.views.SavingAccountsListView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

/**
 * @author Vishwajeet
 * @since 21/06/16
 */

public class SavingAccountsListFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener, SavingAccountsListView {

    @BindView(R.id.rv_saving_accounts_list)
    RecyclerView rv_SavingAccounts;
    @BindView(R.id.swipe_savings_container)
    SwipeRefreshLayout swipeSavingsContainer;

    public int clientId;
    private View rootView;
    SavingAccountsListPresenter mSavingAccountsListPresenter;
    private LinearLayoutManager linearLayoutManager;
    private ProgressDialog progress;
    private List<SavingAccount> savingAccountsList = new ArrayList<>();
    private SavingAccountsListAdapter savingAccountsListAdapter;
    private BaseApiManager mBaseApiManager;
    private DataManager mDataManager;

    public static SavingAccountsListFragment newInstance(int clientId) {
        SavingAccountsListFragment fragment = new SavingAccountsListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.CLIENT_ID, clientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            clientId = getArguments().getInt(Constants.CLIENT_ID);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_saving_accounts_list, container, false);
        ButterKnife.bind(this, rootView);
        mBaseApiManager = new BaseApiManager(getActivity());
        mDataManager = new DataManager(mBaseApiManager);
        mSavingAccountsListPresenter = new SavingAccountsListPresenter(mDataManager);
        mSavingAccountsListPresenter.attachView(this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_SavingAccounts.setLayoutManager(linearLayoutManager);
        rv_SavingAccounts.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));
        rv_SavingAccounts.setHasFixedSize(true);

        swipeSavingsContainer.setColorSchemeResources(R.color.blue_light, R.color.green_light, R
                .color.orange_light, R.color.red_light);
        swipeSavingsContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchSavingAccountsList();
            }
        });
        fetchSavingAccountsList();
        return rootView;
    }

    private void fetchSavingAccountsList() {
        mSavingAccountsListPresenter.fetchSavingAccounts(clientId);
    }

    @Override
    public void onItemClick(View childView, int position) {

    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    public void showSavingAccounts(Response<SavingAccount> response) {
        savingAccountsList = response.body().getSavingsAccounts();
        inflateSavingAccountsList();
        if (swipeSavingsContainer.isRefreshing())
            swipeSavingsContainer.setRefreshing(false);
    }

    private void inflateSavingAccountsList() {
        savingAccountsListAdapter = new SavingAccountsListAdapter(getContext(), savingAccountsList);
        rv_SavingAccounts.setAdapter(savingAccountsListAdapter);
    }

    @Override
    public void showErrorFetchingSavingAccounts(String string) {

    }

    @Override
    public void showProgress() {
        if (progress == null) {
            progress = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
        }
        progress.setMessage(getResources().getText(R.string.progress_message_loading));
        progress.show();
    }

    @Override
    public void hideProgress() {
        if (progress != null && progress.isShowing())
            progress.dismiss();
    }

}
