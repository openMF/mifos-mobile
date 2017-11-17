package org.mifos.mobilebanking.ui.views;

/**
 * @author Rajan Maurya
 */
public interface BaseActivityCallback {

    void showProgressDialog(String message);

    void hideProgressDialog();

    void setToolbarTitle(String title);
}