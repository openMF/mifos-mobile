package org.mifos.selfserviceapp.ui.views;

/**
 * @author Rajan Maurya
 */
public interface BaseActivityCallback {

    void showProgressDialog(String message);

    void hideProgressDialog();
}