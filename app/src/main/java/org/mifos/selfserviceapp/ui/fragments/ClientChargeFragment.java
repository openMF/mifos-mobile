package org.mifos.selfserviceapp.ui.fragments;

import android.app.ProgressDialog;
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
import org.mifos.selfserviceapp.models.Charge;
import org.mifos.selfserviceapp.presenters.ClientChargePresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.adapters.ClientChargeAdapter;
import org.mifos.selfserviceapp.ui.views.ClientChargeView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.DividerItemDecoration;
import org.mifos.selfserviceapp.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 17/8/16.
 */

public class ClientChargeFragment extends Fragment implements
        RecyclerItemClickListener.OnItemClickListener, ClientChargeView {
    @Inject
    ClientChargePresenter mClientChargePresenter;
    ClientChargeAdapter clientChargeAdapter;
    @BindView(R.id.rv_client_charge)
    RecyclerView rvClientCharge;
    @BindView(R.id.swipe_charge_container)
    SwipeRefreshLayout swipeChargeContainer;
    private long clientId;
    private View rootView;
    private LinearLayoutManager layoutManager;
    private ProgressDialog progressDialog;
    private List<Charge> clientChargeList = new ArrayList<>();

    public static ClientChargeFragment newInstance(long clientId) {
        ClientChargeFragment clientChargeFragment = new ClientChargeFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.CLIENT_ID, clientId);
        clientChargeFragment.setArguments(args);
        return clientChargeFragment;
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
        rootView = inflater.inflate(R.layout.fragment_client_charge, container, false);
        ButterKnife.bind(this, rootView);

        mClientChargePresenter.attachView(this);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvClientCharge.setLayoutManager(layoutManager);
        rvClientCharge.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        rvClientCharge.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));

        swipeChargeContainer.setColorSchemeResources(R.color.blue_light, R.color.green_light, R
                .color.orange_light, R.color.red_light);
        swipeChargeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mClientChargePresenter.loadClientCharges(clientId);
            }
        });

        mClientChargePresenter.loadClientCharges(clientId);
        return rootView;
    }

    @Override
    public void showErrorFetchingClientCharges(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showClientCharges(List<Charge> clientChargeList) {
        this.clientChargeList = clientChargeList;
        inflateClientChargeList();
        if (swipeChargeContainer.isRefreshing()) {
            swipeChargeContainer.setRefreshing(false);
        }
    }

    private void inflateClientChargeList() {
        clientChargeAdapter = new ClientChargeAdapter(getContext(), clientChargeList);
        rvClientCharge.setAdapter(clientChargeAdapter);
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
    public void onItemClick(View childView, int position) {

    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    public void onDestroyView() {
        mClientChargePresenter.detachView();
        super.onDestroyView();
    }

}
