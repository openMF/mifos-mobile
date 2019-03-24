package org.mifos.mobile.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler;

import org.mifos.mobile.R;
import org.mifos.mobile.models.notification.MifosNotification;
import org.mifos.mobile.presenters.NotificationPresenter;
import org.mifos.mobile.ui.activities.base.BaseActivity;
import org.mifos.mobile.ui.adapters.NotificationAdapter;
import org.mifos.mobile.ui.fragments.base.BaseFragment;
import org.mifos.mobile.ui.views.NotificationView;
import org.mifos.mobile.utils.DividerItemDecoration;
import org.mifos.mobile.utils.Network;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dilpreet on 13/9/17.
 */

public class NotificationFragment extends BaseFragment implements NotificationView,
        SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.rv_notifications)
    RecyclerView rvNotification;

    @BindView(R.id.swipe_notification_container)
    SwipeRefreshLayout swipeNotificationContainer;

    @BindView(R.id.layout_error)
    View layoutError;

    @Inject
    NotificationPresenter presenter;

    @Inject
    NotificationAdapter adapter;

    private View rootView;
    private SweetUIErrorHandler sweetUIErrorHandler;


    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(getString(R.string.notification));
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, rootView);

        sweetUIErrorHandler = new SweetUIErrorHandler(getActivity(), rootView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvNotification.setLayoutManager(layoutManager);
        rvNotification.addItemDecoration(new DividerItemDecoration(getActivity(),
                layoutManager.getOrientation()));
        rvNotification.setAdapter(adapter);

        swipeNotificationContainer.setColorSchemeResources(R.color.blue_light,
                R.color.green_light, R.color.orange_light, R.color.red_light);
        swipeNotificationContainer.setOnRefreshListener(this);

        presenter.attachView(this);
        presenter.loadNotifications();

        return rootView;
    }

    @Override
    public void showNotifications(List<MifosNotification> notifications) {
        if (notifications.size() != 0) {
            adapter.setNotificationList(notifications);
        } else {
            sweetUIErrorHandler.showSweetEmptyUI(getString(R.string.notification),
                    R.drawable.ic_notifications, rvNotification, layoutError);
        }
    }

    @Override
    public void showError(String msg) {
        if (!Network.isConnected(getActivity())) {
            sweetUIErrorHandler.showSweetNoInternetUI(rvNotification, layoutError);
        } else {
            sweetUIErrorHandler.showSweetErrorUI(msg,
                    rvNotification, layoutError);
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_try_again)
    public void retryClicked() {
        if (Network.isConnected(getContext())) {
            sweetUIErrorHandler.hideSweetErrorLayoutUI(rvNotification, layoutError);
            presenter.loadNotifications();
        } else {
            Toast.makeText(getContext(), getString(R.string.internet_not_connected),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showProgress() {
        swipeNotificationContainer.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        swipeNotificationContainer.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        sweetUIErrorHandler.hideSweetErrorLayoutUI(rvNotification, layoutError);
        presenter.loadNotifications();

    }
}
