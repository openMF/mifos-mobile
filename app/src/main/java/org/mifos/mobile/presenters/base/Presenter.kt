package org.mifos.mobile.presenters.base

import org.mifos.mobile.ui.views.base.MVPView

/**
 * @author ishan
 * @since 19/05/16
 */
interface Presenter<V : MVPView?> {

    fun attachView(mvpView: V)

    fun detachView()

}