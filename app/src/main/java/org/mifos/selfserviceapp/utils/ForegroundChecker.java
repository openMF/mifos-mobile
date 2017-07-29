package org.mifos.selfserviceapp.utils;

import android.content.Context;
import android.os.Handler;

import org.mifos.selfserviceapp.api.local.PreferencesHelper;

/**
 * Created by dilpreet on 18/7/17.
 */

public class ForegroundChecker {

    public static final long CHECK_DELAY = 500;
    public static final int MIN_BACKGROUND_THRESHOLD = 60;
    public static final String TAG = ForegroundChecker.class.getName();
    private PreferencesHelper preferencesHelper;

    public interface Listener {
        public void onBecameForeground();
    }

    private static ForegroundChecker instance;

    private boolean foreground = false, paused = true;
    private Handler handler = new Handler();
    private Listener listener;
    private Runnable check;
    private long backgroundTimeStart;

    public static ForegroundChecker init(Context context) {
        if (instance == null) {
            instance = new ForegroundChecker(context);
        }
        return instance;
    }

    public static ForegroundChecker get() {
        return instance;
    }

    private ForegroundChecker(Context context) {
        backgroundTimeStart = -1;
        preferencesHelper = new PreferencesHelper(context);
    }

    public boolean isForeground() {
        return foreground;
    }

    public boolean isBackground() {
        return !foreground;
    }

    public void addListener(Listener listener) {
        this.listener = listener;
    }

    public void onActivityResumed() {
        paused = false;
        boolean wasBackground = !foreground;
        foreground = true;

        if (check != null)
            handler.removeCallbacks(check);

        if (wasBackground) {

            int secondsInBackground = (int) ((System.currentTimeMillis() - backgroundTimeStart) /
                    1000);
            if (backgroundTimeStart != -1 && secondsInBackground >= MIN_BACKGROUND_THRESHOLD &&
                    listener != null && !preferencesHelper.getPasscode().isEmpty()) {
                listener.onBecameForeground();
            }

        }
    }

    public void onActivityPaused() {
        paused = true;

        if (check != null)
            handler.removeCallbacks(check);

        handler.postDelayed(check = new Runnable() {
            @Override
            public void run() {
                if (foreground && paused) {
                    foreground = false;
                    backgroundTimeStart = System.currentTimeMillis();

                }
            }
        }, CHECK_DELAY);
    }
}