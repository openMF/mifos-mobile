package org.mifos.selfserviceapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.Charge;
import org.mifos.selfserviceapp.presenters.ClientChargePresenter;
import org.mifos.selfserviceapp.ui.activities.base.BaseActivity;
import org.mifos.selfserviceapp.ui.adapters.ClientChargeAdapter;
import org.mifos.selfserviceapp.ui.fragments.base.BaseFragment;
import org.mifos.selfserviceapp.ui.enums.ChargeType;
import org.mifos.selfserviceapp.ui.views.ClientChargeView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.RecyclerItemClickListener;
import org.mifos.selfserviceapp.utils.Toaster;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 17/8/16.
 */

public class ClientChargeFragment extends BaseFragment implements
        RecyclerItemClickListener.OnItemClickListener, ClientChargeView {

    @Inject
    ClientChargePresenter mClientChargePresenter;

    @Inject
    ClientChargeAdapter clientChargeAdapter;

    @BindView(R.id.rv_client_charge)
    RecyclerView rvClientCharge;

    @BindView(R.id.swipe_charge_container)
    SwipeRefreshLayout swipeChargeContainer;

    @BindView(R.id.ll_error)
    RelativeLayout rlErrorLayout;

    @BindView(R.id.iv_status)
    ImageView ivError;

    @BindView(R.id.tv_status)
    TextView tvError;

    private long id;
    private ChargeType chargeType;
    private View rootView;
    private LinearLayoutManager layoutManager;
    private List<Charge> clientChargeList = new ArrayList<>();

    public static ClientChargeFragment newInstance(long clientId, ChargeType chargeType) {
        ClientChargeFragment clientChargeFragment = new ClientChargeFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.CLIENT_ID, clientId);
        args.putSerializable(Constants.CHARGE_TYPE, chargeType);
        clientChargeFragment.setArguments(args);
        return clientChargeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        if (getArguments() != null) {
            setToolbarTitle(getString(R.string.charges));
            id = getArguments().getLong(Constants.CLIENT_ID);
            chargeType = (ChargeType) getArguments().getSerializable(Constants.CHARGE_TYPE);
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
        rvClientCharge.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));

        swipeChargeContainer.setColorSchemeResources(R.color.blue_light, R.color.green_light, R
                .color.orange_light, R.color.red_light);
        swipeChargeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCharges();
            }
        });
        loadCharges();
        return rootView;
    }

    private void loadCharges() {
        rlErrorLayout.setVisibility(View.GONE);
        swipeChargeContainer.setVisibility(View.VISIBLE);

        if (chargeType == ChargeType.CLIENT) {
            mClientChargePresenter.loadClientCharges(id);
        } else if (chargeType == ChargeType.SAVINGS) {
            mClientChargePresenter.loadSavingsAccountCharges(id);
        } else if (chargeType == ChargeType.LOAN) {
            mClientChargePresenter.loadLoanAccountCharges(id);
        }
    }

    @Override
    public void showErrorFetchingClientCharges(String message) {
        Toaster.show(rootView, message);
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
        if (clientChargeList.size() > 0) {
            clientChargeAdapter.setClientChargeList(clientChargeList);
            rvClientCharge.setAdapter(clientChargeAdapter);
        } else {
            rlErrorLayout.setVisibility(View.VISIBLE);
            swipeChargeContainer.setVisibility(View.GONE);
            tvError.setText(getString(R.string.error_no_charge));
        }
    }

    @Override
    public void showProgress() {
        swipeChargeContainer.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        swipeChargeContainer.setRefreshing(false);
    }

    @Override
    public void onItemClick(View childView, int position) {

    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mClientChargePresenter.detachView();
    }

}
