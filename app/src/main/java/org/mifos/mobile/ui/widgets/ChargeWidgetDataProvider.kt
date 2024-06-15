package org.mifos.mobile.ui.widgets

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService.RemoteViewsFactory
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import org.mifos.mobile.R
import org.mifos.mobile.models.Charge
import org.mifos.mobile.repositories.ClientChargeRepository
import org.mifos.mobile.ui.client_charge.ClientChargeViewModel
import java.util.concurrent.locks.ReentrantLock
import javax.inject.Inject

/**
 * ChargeWidgetDataProvider acts as the adapter for the collection view widget,
 * providing RemoteViews to the widget in the getViewAt method.
 */
class ChargeWidgetDataProvider(@param:ApplicationContext private val context: Context) :
    RemoteViewsFactory {

    @Inject
    lateinit var clientChargeRepository: ClientChargeRepository

    private lateinit var clientChargeViewModel: ClientChargeViewModel

    private var charges: List<Charge?>?
    private val `object`: ReentrantLock = ReentrantLock()
    private val condition = `object`.newCondition()

    override fun onCreate() {
        clientChargeViewModel = ClientChargeViewModel(clientChargeRepository)
    }

    override fun onDataSetChanged() {
        // TODO Make ClientId Dynamic
        clientChargeViewModel.loadClientLocalCharges()
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
        return if (charges != null) {
            charges!!.size
        } else {
            0
        }
    }

    override fun getViewAt(position: Int): RemoteViews {
        val charge = charges?.get(position)
        val itemId = R.layout.item_widget_client_charge
        val view = RemoteViews(context.packageName, itemId)
        view.setTextViewText(R.id.tv_charge_name, charge?.name)
        view.setTextViewText(R.id.tv_charge_amount, charge?.amount.toString())
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

    fun showErrorFetchingClientCharges(message: String?) {
        Toast.makeText(
            context,
            context.getString(R.string.error_client_charge_loading),
            Toast.LENGTH_SHORT,
        ).show()
    }

    fun showClientCharges(clientChargesList: List<Charge?>?) {
        charges = clientChargesList
        synchronized(`object`) {
            condition.signal()
        }
    }

    fun showProgress() {}

    fun hideProgress() {}

    override fun onDestroy() {
    }

    companion object {
        private val LOG_TAG = ChargeWidgetDataProvider::class.java.simpleName
    }

    init {
        charges = ArrayList()
    }
}
