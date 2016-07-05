package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.data.Client;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

import retrofit2.Response;

/**
 * @author Vishwajeet
 * @since  20/6/16.
 */
public interface ClientListMvpView extends MVPView {

    void showClients(Response<Client> response);

    void showErrorFetchingClients(String message);

    void showProgress();

    void hideProgress();

}
