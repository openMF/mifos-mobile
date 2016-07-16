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
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.data.Client;
import org.mifos.selfserviceapp.presenters.ClientListPresenter;
import org.mifos.selfserviceapp.ui.activities.BaseActivity;
import org.mifos.selfserviceapp.ui.adapters.ClientListAdapter;
import org.mifos.selfserviceapp.ui.views.ClientListView;
import org.mifos.selfserviceapp.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

/**
 * @author Vishwajeet
 * @since 14/07/16
 */
public class ClientListFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener, ClientListView {

    @Inject
    ClientListPresenter mClientListPresenter;

    private ClientListAdapter clientListAdapter;
    private View rootView;
    private List<Client> clientList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private ProgressDialog progress;

    @BindView(R.id.rv_clients)
    RecyclerView rv_clients;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_client_list, container, false);
        setHasOptionsMenu(true);
        getActivity().setTitle(getResources().getString(R.string.clients_list));
        ButterKnife.bind(this, rootView);

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
                mClientListPresenter.loadClients();
            }
        });

        mClientListPresenter.loadClients();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        mClientListPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void onItemClick(View childView, int position) {
        //TODO add client accounts screen from here
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
    public void showClients(List<Client> clientList) {
        this.clientList = clientList;
        inflateClientList();
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * setting adapter to display list of clients on the client list screen
     */
    private void inflateClientList() {
        clientListAdapter = new ClientListAdapter(getContext(), clientList);
        rv_clients.setAdapter(clientListAdapter);
    }

    @Override
    public void showErrorFetchingClients(String message) {
        //TODO: Handle this error properly
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showInternalServerError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

}
