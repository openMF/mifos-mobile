package org.mifos.mobilebanking.ui.widgets;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.BaseApiManager;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.api.local.DatabaseHelper;
import org.mifos.mobilebanking.api.local.PreferencesHelper;
import org.mifos.mobilebanking.injection.ApplicationContext;
import org.mifos.mobilebanking.models.Charge;
import org.mifos.mobilebanking.presenters.ClientChargePresenter;
import org.mifos.mobilebanking.ui.views.ClientChargeView;

import java.util.ArrayList;
import java.util.List;

/**
 * ChargeWidgetDataProvider acts as the adapter for the collection view widget,
 * providing RemoteViews to the widget in the getViewAt method.
 */
public class ChargeWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory,
        ClientChargeView {

    private static final String LOG_TAG = ChargeWidgetDataProvider.class.getSimpleName();

    ClientChargePresenter chargePresenter;

    private Context context;
    private List<Charge> charges;
    private final Object object;

    public ChargeWidgetDataProvider(@ApplicationContext Context context, Intent intent) {
        this.context = context;
        object = new Object();
        charges = new ArrayList<>();
    }

    @Override
    public void onCreate() {
        PreferencesHelper preferencesHelper = new PreferencesHelper(context);
        BaseApiManager baseApiManager = new BaseApiManager(preferencesHelper);
        DatabaseHelper databaseHelper = new DatabaseHelper();
        DataManager dataManager = new DataManager(preferencesHelper, baseApiManager,
                databaseHelper);
        chargePresenter = new ClientChargePresenter(dataManager, context);
        chargePresenter.attachView(this);
    }

    @Override
    public void onDataSetChanged() {
        //TODO Make ClientId Dynamic
        chargePresenter.loadClientLocalCharges();
        synchronized (object) {
            try {
                // Calling wait() will block this thread until another thread
                // calls notify() on the object.
                object.wait();
            } catch (InterruptedException e) {
                // Happens if someone interrupts your thread.
            }
        }
    }

    @Override
    public int getCount() {
        return charges.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Charge charge = charges.get(position);

        final int itemId = R.layout.item_widget_client_charge;
        RemoteViews view = new RemoteViews(context.getPackageName(), itemId);
        view.setTextViewText(R.id.tv_charge_name, charge.getName());
        view.setTextViewText(R.id.tv_charge_amount, String.valueOf(charge.getAmount()));
        view.setImageViewResource(R.id.circle_status, R.drawable.ic_attach_money_black_24dp);
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void showErrorFetchingClientCharges(String message) {
        Toast.makeText(context, context.getString(R.string.error_client_charge_loading),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showClientCharges(List<Charge> clientChargesList) {
        charges = clientChargesList;
        synchronized (object) {
            object.notify();
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onDestroy() {
        chargePresenter.detachView();
    }
}
