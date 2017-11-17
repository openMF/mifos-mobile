package org.mifos.mobilebanking.ui.widgets;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * ChargeWidgetService is the {@link RemoteViewsService} that will return our RemoteViewsFactory
 */
public class ChargeWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ChargeWidgetDataProvider(this, intent);
    }
}
