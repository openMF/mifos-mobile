package org.mifos.selfserviceapp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.ui.adapters.ClientListAdapter;
import org.mifos.selfserviceapp.api.BaseApiManager;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.data.Client;
import org.mifos.selfserviceapp.presenters.ClientListPresenter;
import org.mifos.selfserviceapp.ui.views.ClientListMvpView;
import org.mifos.selfserviceapp.ui.activities.ClientAccountsActivity;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

/**
 * @author Vishwajeet
 * @since 19/06/16
 */

public class ClientListFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener, ClientListMvpView {

    @BindView(R.id.rv_clients)
    RecyclerView rv_clients;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    ClientListAdapter clientListAdapter;
    ClientListPresenter mClientListPresenter;
    private View rootView;
    private List<Client> clientList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private DataManager mDataManager;
    private BaseApiManager mBaseApiManager;
    private ProgressDialog progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_client_list, container, false);
        setHasOptionsMenu(true);
        getActivity().setTitle(getResources().getString(R.string.clients));
        ButterKnife.bind(this, rootView);

        mBaseApiManager = new BaseApiManager(getActivity());
        mDataManager = new DataManager(mBaseApiManager);
        mClientListPresenter = new ClientListPresenter(mDataManager);
        mClientListPresenter.attachView(this);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_clients.setLayoutManager(layoutManager);
        rv_clients.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));
        rv_clients.setHasFixedSize(true);

        swipeRefreshLayout.setColorSchemeResources(R.color.blue_light, R.color.green_light, R
                .color.orange_light, R.color.red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchClientList();
            }
        });

        fetchClientList();

        return rootView;
    }

    private void fetchClientList() {
        mClientListPresenter.loadClients();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mClientListPresenter.detachView();
    }

    @Override
    public void onItemClick(View childView, int position) {
        Intent clientActivityIntent = new Intent(getActivity(), ClientAccountsActivity.class);
        clientActivityIntent.putExtra(Constants.CLIENT_ID, clientList.get(position).getId());
        startActivity(clientActivityIntent);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

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

    @Override
    public void showClients(Response<Client> response) {
        clientList = response.body().getPageItems();
        inflateClientList();
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }

    private void inflateClientList() {
        clientListAdapter = new ClientListAdapter(getContext(), clientList);
        rv_clients.setAdapter(clientListAdapter);
    }

    @Override
    public void showErrorFetchingClients(String message) {

        //TODO: Handle this error properly
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

}
