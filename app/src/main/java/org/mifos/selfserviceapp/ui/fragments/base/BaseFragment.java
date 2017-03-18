package org.mifos.selfserviceapp.ui.fragments.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.mifos.selfserviceapp.ui.views.BaseActivityCallback;
import org.mifos.selfserviceapp.utils.ProgressBarHandler;

public class BaseFragment extends Fragment {

    private BaseActivityCallback callback;
    private ProgressBarHandler progressBarHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBarHandler = new ProgressBarHandler(getActivity());
    }

    protected void showMifosProgressDialog(String message) {
        if (callback != null)
            callback.showProgressDialog(message);
    }

    protected void hideMifosProgressDialog() {
        if (callback != null)
            callback.hideProgressDialog();
    }

    protected void setToolbarTitle(String title) {
        callback.setToolbarTitle(title);
    }

    protected void showProgressBar() {
        progressBarHandler.show();
    }

    protected void hideProgressBar() {
        progressBarHandler.hide();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = context instanceof Activity ? (Activity) context : null;
        try {
            callback = (BaseActivityCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement BaseActivityCallback methods");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    public String separator(String inputText) {
        if (inputText.equals("null")) {
            return inputText;
        } else {
            String textToSeparate = inputText;
            String textToLeaveAsItIs = "";
            if (inputText.contains(".")) {
                int indexOfPoint = inputText.indexOf('.');
                textToSeparate = inputText.substring(0, indexOfPoint);
                textToLeaveAsItIs = inputText.substring(indexOfPoint + 1);
            }
            String resultText = "";
            int coefficientOfNeededGroups = textToSeparate.length() / 3;
            if (coefficientOfNeededGroups > 0) {
                for (int i = 0; i < textToSeparate.length(); i++) {
                    resultText += String.valueOf(textToSeparate.charAt(i));
                    for (int k = 1; k < coefficientOfNeededGroups + 1; k++) {
                        if (i == textToSeparate.length() - k * 4 + (k - 1)) {
                            resultText += ",";
                        }
                    }
                }
            } else {
                resultText = textToSeparate;
            }
            if (textToLeaveAsItIs.equals("")) {
                return resultText;
            } else {
                return resultText + "." + textToLeaveAsItIs;
            }
        }
    }
}
