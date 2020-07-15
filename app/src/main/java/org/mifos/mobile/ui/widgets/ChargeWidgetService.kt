package org.mifos.mobile.ui.widgets

import android.content.Intent
import android.widget.RemoteViewsService

/**
 * ChargeWidgetService is the [RemoteViewsService] that will return our RemoteViewsFactory
 */
class ChargeWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return ChargeWidgetDataProvider(this)
    }
}