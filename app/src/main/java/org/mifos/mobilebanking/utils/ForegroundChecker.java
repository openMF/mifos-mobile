package org.mifos.mobilebanking.utils;

import android.content.Context;
import android.os.Handler;

import org.mifos.mobilebanking.api.local.PreferencesHelper;

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

    /**
     * Used to initialize {@code instance} of {@link ForegroundChecker}
     * @param context Application Content
     * @return Instance of {@link ForegroundChecker}
     */
    public static ForegroundChecker init(Context context) {
        if (instance == null) {
            instance = new ForegroundChecker(context);
        }
        return instance;
    }

    /**
     * Provides instance of {@link ForegroundChecker}
     * @return Instance of {@link ForegroundChecker}
     */
    public static ForegroundChecker get() {
        return instance;
    }

    /**
     * Initializes {@link ForegroundChecker}
     * @param context Application Context
     */
    private ForegroundChecker(Context context) {
        backgroundTimeStart = -1;
        preferencesHelper = new PreferencesHelper(context);
    }

    /**
     * Returns True if application is in foreground
     * @return State of Application
     */
    public boolean isForeground() {
        return foreground;
    }

    /**
     * Returns True if application is in background
     * @return State of Application
     */
    public boolean isBackground() {
        return !foreground;
    }

    public void addListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * Called in {@link org.mifos.mobilebanking.ui.activities.base.BaseActivity}<br>
     * It calls {@code onBecameForeground()} if {@code secondsInBackground} >=
     * {@code MIN_BACKGROUND_THRESHOLD}
     */
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

    /**
     * Called in {@link org.mifos.mobilebanking.ui.activities.base.BaseActivity}<br>
     * It executes a Handler after {@code CHECK_DELAY} and then sets {@code foreground} to false
     */
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