package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

import java.util.List;

/**
 * @author Vishwajeet
 * @since 14/07/16
 */
public interface ClientListView extends MVPView {

    /**
     * Should be called when list of clients successfully accessed
     * from the server to display list items on the Client List screen.
     *
     * @param clientList List of clients in terms of page items received from server
     */
    void showClients(List<Client> clientList);

    /**
     * Should be called if there is any error from client side in loading the client list from
     * server.
     * Reason for error should be mentioned clearly to the user.
     *
     * @param message Error message to display showing reason of failure in loading client list
     */
    void showErrorFetchingClients(String message);
}
