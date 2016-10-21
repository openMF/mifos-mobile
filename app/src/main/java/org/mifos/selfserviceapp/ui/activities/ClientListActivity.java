package org.mifos.selfserviceapp.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.models.Client;
import org.mifos.selfserviceapp.presenters.ClientListPresenter;
import org.mifos.selfserviceapp.ui.adapters.ClientListAdapter;
import org.mifos.selfserviceapp.ui.views.ClientListView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Vishwajeet
 * @since 14/07/16
 */
public class ClientListActivity extends BaseActivity
        implements RecyclerItemClickListener.OnItemClickListener, ClientListView {

    @Inject
    ClientListPresenter mClientListPresenter;
    @BindView(R.id.rv_clients)
    RecyclerView rvClients;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    private ClientListAdapter clientListAdapter;
    private List<Client> clientList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        setContentView(R.layout.activity_client_list);

        ButterKnife.bind(this);

        mClientListPresenter.attachView(this);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvClients.setLayoutManager(layoutManager);
        rvClients.addOnItemTouchListener(new RecyclerItemClickListener(this, this));
        rvClients.setHasFixedSize(true);

        swipeRefreshLayout.setColorSchemeResources(R.color.blue_light, R.color.green_light, R
                .color.orange_light, R.color.red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mClientListPresenter.loadClients();
            }
        });

        mClientListPresenter.loadClients();
    }

    @Override
    public void onDestroy() {
        mClientListPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onItemClick(View childView, int position) {
        Intent clientAccountIntent = new Intent(this, HomeActivity.class);
        clientAccountIntent.putExtra(Constants.CLIENT_ID, clientList.get(position).getId());
        startActivity(clientAccountIntent);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }

    @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
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
    public void showClients(List<Client> clientList) {
        this.clientList = clientList;
        inflateClientList();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * setting adapter to display list of clients on the client list screen
     */
    private void inflateClientList() {
        clientListAdapter = new ClientListAdapter(this, clientList);
        rvClients.setAdapter(clientListAdapter);
    }

    @Override
    public void showErrorFetchingClients(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
