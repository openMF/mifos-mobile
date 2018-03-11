package org.mifos.mobilebanking.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import org.mifos.mobilebanking.MifosSelfServiceApp;

import java.util.ArrayList;

public class Toaster {

    public static final int INDEFINITE = Snackbar.LENGTH_INDEFINITE;
    public static final int LONG = Snackbar.LENGTH_LONG;
    public static final int SHORT = Snackbar.LENGTH_SHORT;
    private static ArrayList<Snackbar> snackbarsQueue = new ArrayList<>();

    public static void show(View view, String text, int duration) {

        InputMethodManager imm = (InputMethodManager) MifosSelfServiceApp.getContext().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        final Snackbar snackbar = Snackbar.make(view, text, duration);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id
                .snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(12);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onShown(Snackbar sb) {
                super.onShown(sb);
            }

            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {

                super.onDismissed(transientBottomBar, event);

                if (!snackbarsQueue.isEmpty()) {

                    snackbarsQueue.remove(0);

                    if (!snackbarsQueue.isEmpty()) {

                        snackbarsQueue.get(0).show();
                    }
                }
            }
        });

        snackbarsQueue.add(snackbar);

        if (!snackbarsQueue.get(0).isShown()) {

            snackbarsQueue.get(0).show();
        }
    }

    public static void show(View view, int res, int duration) {
        show(view, MifosSelfServiceApp.getContext().getResources().getString(res), duration);
    }

    public static void cancelTransfer(View view, String text, String buttonText,
                                      View.OnClickListener listener) {
        final Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        snackbar.setAction(buttonText, listener);
        snackbar.show();
    }

    public static void show(View view, String text) {
        show(view, text, Snackbar.LENGTH_LONG);
    }

    public static void show(View view, int res) {
        show(view, MifosSelfServiceApp.getContext().getResources().getString(res));
    }
}
