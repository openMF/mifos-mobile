package org.mifos.selfserviceapp.presenters;

import org.mifos.selfserviceapp.ui.views.base.MVPView;

/**
 * @author ishan
 * @since 19/05/16
 */
public interface Presenter<V extends MVPView> {

    void attachView(V mvpView);

    void detachView();

}
