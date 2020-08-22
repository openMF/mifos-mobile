package org.mifos.mobile.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import org.mifos.mobile.BuildConfig;

import org.mifos.mobile.R;
import org.mifos.mobile.api.local.PreferencesHelper;

import javax.inject.Inject;

public class RateApp {

    private PreferencesHelper preferencesHelper;

    @Inject
    public RateApp(PreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
    }

    public void appLaunched(Context mContext) {

        isAppUpdated();

        if (preferencesHelper.getBoolean(Constants.DONT_SHOW_AGAIN, false)) {
            return;
        }

        // Increment launch counter
        long launchCount = preferencesHelper.getLong(Constants.LAUNCH_COUNT, 0) + 1;
        preferencesHelper.putLong(Constants.LAUNCH_COUNT, launchCount);

        // Get date of first launch
        Long dateFirstLaunch = preferencesHelper.getLong(Constants.DATE_FIRST_LAUNCH, 0);
        if (dateFirstLaunch == 0) {
            dateFirstLaunch = System.currentTimeMillis();
            preferencesHelper.putLong(Constants.DATE_FIRST_LAUNCH, dateFirstLaunch);
        }

        // Wait at least days before opening
        long remindCount = preferencesHelper.getLong(Constants.REMIND_COUNT, 0);
        long remindLaunches = remindCount * Constants.REMIND_LAUNCHES_UNTIL_PROMPT;
        long remindDays = remindCount * Constants.REMIND_DAYS_UNTIL_PROMPT;
        if (launchCount >= Constants.LAUNCHES_UNTIL_PROMPT + remindLaunches &&
                System.currentTimeMillis() >= dateFirstLaunch +
                        ((Constants.DAYS_UNTIL_PROMPT + remindDays) * 24 * 60 * 60 * 1000)) {

            showRateDialog(mContext);
        }
    }

    public void showRateDialog(final Context context) {

        new MaterialDialog.Builder().init(context)
                .setTitle(context.getString(R.string.rate_the_app))
                .setCancelable(false)
                .setMessage(context.getString(R.string.rate_message))
                .setPositiveButton(context.getString(R.string.rate_it_now), new DialogInterface.
                        OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        preferencesHelper.putBoolean(Constants.DONT_SHOW_AGAIN, true);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(
                                "https://play.google.com/store/apps/details?id=" + context.getPackageName() ));
                        intent.setPackage("com.android.vending");
                        context.startActivity(intent);
                    }
                })
                .setNeutralButton(R.string.remind_me_later, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Long remindCount = preferencesHelper.getLong(Constants.REMIND_COUNT, 0);
                        preferencesHelper.putLong(Constants.REMIND_COUNT, remindCount + 1);
                    }
                })
                .setNegativeButton(context.getString(R.string.never),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                preferencesHelper.putBoolean(Constants.DONT_SHOW_AGAIN, true);
                            }
                        })
                .createMaterialDialog()
                .show();
    }

    public void isAppUpdated() {

        Long versionCode = preferencesHelper.getLong(Constants.VERSION_CODE,
                BuildConfig.VERSION_CODE);
        if (versionCode != BuildConfig.VERSION_CODE) {

            preferencesHelper.putBoolean(Constants.DONT_SHOW_AGAIN, false);
        }
        preferencesHelper.putLong(Constants.VERSION_CODE, BuildConfig.VERSION_CODE);
    }
}
