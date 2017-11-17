package org.mifos.mobilebanking.ui.views.base;

import android.view.View;

/**
 * Created by Rajan Maurya on 23/02/17.
 */

public interface OnClickItem {

    void onItemClick(View childView, int position);

    void onItemLongPress(View childView, int position);
}
