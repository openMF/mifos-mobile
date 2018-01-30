package org.mifos.mobilebanking.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.notification.MifosNotification;
import org.mifos.mobilebanking.presenters.NotificationPresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.adapters.NotificationAdapter;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.NotificationView;
import org.mifos.mobilebanking.utils.DividerItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dilpreet on 13/9/17.
 */

public class NotificationFragment extends BaseFragment implements NotificationView,
        SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.rv_notifications)
    RecyclerView rvNotification;

    @BindView(R.id.tv_no_notification_msg)
    TextView tvNoNotificationMsg;

    @BindView(R.id.swipe_notification_container)
    SwipeRefreshLayout swipeNotificationContainer;

    @Inject
    NotificationPresenter presenter;

    @Inject
    NotificationAdapter adapter;

    private View rootView;


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

            tvNoNotificationMsg.setVisibility(View.VISIBLE);
            rvNotification.setVisibility(View.GONE);
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
        presenter.loadNotifications();

    }
}
