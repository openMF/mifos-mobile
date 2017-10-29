package org.mifos.selfserviceapp.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import org.mifos.selfserviceapp.MifosSelfServiceApp;

public class Toaster {

    public static final int INDEFINITE = Snackbar.LENGTH_INDEFINITE;
    public static final int LONG = Snackbar.LENGTH_LONG;
    public static final int SHORT = Snackbar.LENGTH_SHORT;

    public static void show(View view, String text, int duration) {
        InputMethodManager imm = (InputMethodManager) MifosSelfServiceApp.getContext().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        final Snackbar snackbar = Snackbar.make(view, text, duration);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id
                .snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(12);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public static void cancelTransfer(View view, String text, String buttonText,
                                      View.OnClickListener listener) {
        final Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        snackbar.setAction(buttonText, listener);
        snackbar.show();
    }

    public static void show(View view, int res, int duration) {
        show(view, MifosSelfServiceApp.getContext().getResources().getString(res), duration);
    }

    public static void show(View view, String text) {
        show(view, text, Snackbar.LENGTH_LONG);
    }

    public static void show(View view, int res) {
        show(view, MifosSelfServiceApp.getContext().getResources().getString(res));
    }
}
