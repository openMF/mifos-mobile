/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.platform.update

import android.app.Activity
import android.util.Log
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import template.core.base.platform.BuildConfig

/**
 * Android-specific implementation of the AppUpdateManager interface that integrates
 * with Google Play's In-App Update API.
 *
 * This class handles checking for application updates, initiating the update process,
 * and resuming updates that were previously in progress. It is configured to use
 * immediate updates, which interrupt the user experience and require the user to
 * update before continuing to use the application.
 *
 * The implementation includes special handling for debug builds to prevent unwanted
 * update prompts during development and testing.
 *
 * @property activity The Android Activity context required to initiate the update flow
 */
private const val UPDATE_MANAGER_REQUEST_CODE: Int = 9900

class AppUpdateManagerImpl(
    private val activity: Activity,
) : AppUpdateManager {
    /**
     * The Google Play update manager instance that handles the update process.
     */
    private val manager = AppUpdateManagerFactory.create(activity)

    /**
     * Configuration options for the update process.
     *
     * This implementation uses immediate updates (AppUpdateType.IMMEDIATE), which
     * interrupt the user experience and require the update to be completed before
     * the user can continue using the application. Asset pack deletion is disabled
     * to preserve any downloaded content.
     */
    private val updateOptions = AppUpdateOptions
        .newBuilder(AppUpdateType.IMMEDIATE)
        .setAllowAssetPackDeletion(false)
        .build()

    /**
     * Checks for available application updates and initiates the update flow if
     * an update is available and allowed.
     *
     * This method queries the Google Play Store for update information and, if an
     * update is available and meets the configured criteria (immediate update type),
     * launches the update flow. The update process is initiated using the activity's
     * startUpdateFlowForResult method, which will handle the user interface for the
     * update process.
     *
     * In debug builds, update checks are skipped to prevent interrupting the
     * development process with update prompts.
     */
    override fun checkForAppUpdate() {
        if (!BuildConfig.DEBUG) {
            manager
                .appUpdateInfo
                .addOnSuccessListener { info ->
                    val isUpdateAvailable =
                        info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE

                    val isUpdateAllowed = when (updateOptions.appUpdateType()) {
                        AppUpdateType.IMMEDIATE -> info.isImmediateUpdateAllowed
                        else -> false
                    }

                    if (isUpdateAvailable && isUpdateAllowed) {
                        manager.startUpdateFlowForResult(
                            /* p0 = */
                            info,
                            /* p1 = */
                            activity,
                            /* p2 = */
                            updateOptions,
                            /* p3 = */
                            UPDATE_MANAGER_REQUEST_CODE,
                        )
                    }
                }.addOnFailureListener {
                    Log.d("Unable to update app!", "UpdateManager", it)
                }
        } else {
            Log.d("UpdateManager", "Skipping update check in debug mode")
        }
    }

    /**
     * Checks for and resumes any update process that was previously initiated but
     * not completed.
     *
     * This method queries the update status and detects if there is an update in
     * the DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS state, which indicates that an
     * update was started but not completed. If such a state is detected, the update
     * flow is restarted to allow the user to complete the update process.
     *
     * This method is typically called during application startup to ensure that
     * interrupted update processes are properly resumed.
     */
    override fun checkForResumeUpdateState() {
        manager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    // If an in-app update is already running, resume the update.
                    manager.startUpdateFlowForResult(
                        /* p0 = */
                        appUpdateInfo,
                        /* p1 = */
                        activity,
                        /* p2 = */
                        updateOptions,
                        /* p3 = */
                        UPDATE_MANAGER_REQUEST_CODE,
                    )
                }
            }
    }
}
