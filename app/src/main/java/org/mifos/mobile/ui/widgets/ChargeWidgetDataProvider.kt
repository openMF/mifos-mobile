package org.mifos.mobile.ui.widgets

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService.RemoteViewsFactory
import android.widget.Toast

import org.mifos.mobile.R
import org.mifos.mobile.api.BaseApiManager
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.api.local.DatabaseHelper
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.injection.ApplicationContext
import org.mifos.mobile.models.Charge
import org.mifos.mobile.presenters.ClientChargePresenter
import org.mifos.mobile.ui.views.ClientChargeView

import java.util.*
import java.util.concurrent.locks.ReentrantLock

/**
 * ChargeWidgetDataProvider acts as the adapter for the collection view widget,
 * providing RemoteViews to the widget in the getViewAt method.
 */
class ChargeWidgetDataProvider(@param:ApplicationContext private val context: Context) : RemoteViewsFactory,
        ClientChargeView {

    private var chargePresenter: ClientChargePresenter? = null
    private var charges: List<Charge?>?
    private val `object`: ReentrantLock = ReentrantLock()
    private val condition = `object`.newCondition()

    override fun onCreate() {
        val preferencesHelper = PreferencesHelper(context)
        val baseApiManager = BaseApiManager(preferencesHelper)
        val databaseHelper = DatabaseHelper()
        val dataManager = DataManager(preferencesHelper, baseApiManager,
                databaseHelper)
        chargePresenter = ClientChargePresenter(dataManager, context)
        chargePresenter!!.attachView(this)
    }

    override fun onDataSetChanged() {
        //TODO Make ClientId Dynamic
        chargePresenter!!.loadClientLocalCharges()
        synchronized(`object`) {
            try {
                // Calling wait() will block this thread until another thread
                // calls notify() on the object.
                condition.await()
            } catch (e: InterruptedException) {
                // Happens if someone interrupts your thread.
            }
        }
    }

    override fun getCount(): Int {
        return charges!!.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val charge = charges!![position]
        val itemId = R.layout.item_widget_client_charge
        val view = RemoteViews(context.packageName, itemId)
        view.setTextViewText(R.id.tv_charge_name, charge!!.name)
        view.setTextViewText(R.id.tv_charge_amount, charge.amount.toString())
        view.setImageViewResource(R.id.circle_status, R.drawable.ic_attach_money_black_24dp)
        return view
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun showErrorFetchingClientCharges(message: String?) {
        Toast.makeText(context, context.getString(R.string.error_client_charge_loading),
                Toast.LENGTH_SHORT).show()
    }

    override fun showClientCharges(clientChargesList: List<Charge?>?) {
        charges = clientChargesList
        synchronized(`object`) {
            condition.signal()
        }
    }

    override fun showProgress() {}

    override fun hideProgress() {}

    override fun onDestroy() {
        chargePresenter!!.detachView()
    }

    companion object {
        private val LOG_TAG = ChargeWidgetDataProvider::class.java.simpleName
    }

    init {
        charges = ArrayList()
    }
}