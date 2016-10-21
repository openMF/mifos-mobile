package org.mifos.selfserviceapp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.accounts.SavingAccount;
import org.mifos.selfserviceapp.presenters.SavingAccountsListPresenter;
import org.mifos.selfserviceapp.ui.activities.BaseActivity;
import org.mifos.selfserviceapp.ui.activities.SavingAccountsDetailActivity;
import org.mifos.selfserviceapp.ui.adapters.SavingAccountsListAdapter;
import org.mifos.selfserviceapp.ui.views.SavingAccountsListView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 02/08/16.
 */

public class SavingAccountsListFragment extends Fragment implements
        RecyclerItemClickListener.OnItemClickListener, SavingAccountsListView {

    @Inject
    SavingAccountsListPresenter mSavingAccountsListPresenter;
    @BindView(R.id.rv_saving_accounts_list)
    RecyclerView rvSavingAccountsList;
    @BindView(R.id.swipe_saving_container)
    SwipeRefreshLayout swipeSavingContainer;
    private long clientId;
    private View view;
    private ProgressDialog progressDialog;
    private List<SavingAccount> savingAccountsList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private SavingAccountsListAdapter savingAccountsListAdapter;

    public static SavingAccountsListFragment newInstance(long clientId) {
        SavingAccountsListFragment fragment = new SavingAccountsListFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.CLIENT_ID, clientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        if (getArguments() != null) {
            clientId = getArguments().getLong(Constants.CLIENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_saving_accounts_list, container, false);
        ButterKnife.bind(this, view);

        mSavingAccountsListPresenter.attachView(this);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvSavingAccountsList.setLayoutManager(layoutManager);
        rvSavingAccountsList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), this));
        // rvSavingAccountsList.setHasFixedSize(true);

        swipeSavingContainer.setColorSchemeResources(R.color.blue_light, R.color.green_light, R
                .color.orange_light, R.color.red_light);
        swipeSavingContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSavingAccountsListPresenter.loadSavingAccountsList(clientId);
            }
        });

        mSavingAccountsListPresenter.loadSavingAccountsList(clientId);
        return view;
    }

    @Override
    public void onItemClick(View childView, int position) {
        Intent intent = new Intent(getActivity(), SavingAccountsDetailActivity.class);
        intent.putExtra(Constants.ACCOUNT_ID, savingAccountsList.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    public void showErrorFetchingSavingAccounts(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSavingAccounts(List<SavingAccount> savingAccountsList) {
        this.savingAccountsList = savingAccountsList;
        inflateSavingAccountsList();
        if (swipeSavingContainer.isRefreshing()) {
            swipeSavingContainer.setRefreshing(false);
        }
    }

    private void inflateSavingAccountsList() {
        savingAccountsListAdapter = new SavingAccountsListAdapter(getContext(), savingAccountsList);
        rvSavingAccountsList.setAdapter(savingAccountsListAdapter);
    }

    @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity(), ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
        }
        progressDialog.setMessage(getResources().getText(R.string.progress_message_loading));
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        mSavingAccountsListPresenter.detachView();
        super.onDestroyView();
    }
}