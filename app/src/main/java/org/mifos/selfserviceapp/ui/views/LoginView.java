package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.ui.views.base.MVPView;

/**
 * @author Vishwajeet
 * @since 05/06/16
 */

public interface LoginView extends MVPView {

    /**
     * Should be called when the user credentials are successfully
     * authenticated from the API.
     * The username would be passed to the view so that we can
     * at least say hello!
     *
     * @param userName Username of the user that successfully logged in!
     */
    void onLoginSuccess(String userName);

    /**
     * Should be called when there was an error trying to authenticate
     * the user from the API. The error could be of many types like
     * <ul>
     * <li>Invalid Login Credentials</li>
     * <li>No network connection</li>
     * </ul>
     * and must be communicated with a well described message.
     *
     * @param errorMessage Error message that tells the user why login failed.
     */
    void showError(String errorMessage);

    /**
     * Should be called when there is a problem with the user input that we
     * requested. The problem must be communicated back to the user clearly.
     *
     * @param errorMessage Error message that tells the user about the problem.
     */
    void showMessage(String errorMessage);

    /**
     * Should be called when the client is fetched successfully from API.
     *
     * @param clientId Client Id
     */
    void showClient(long clientId);

}
