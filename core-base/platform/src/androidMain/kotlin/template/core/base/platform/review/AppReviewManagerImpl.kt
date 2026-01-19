/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.platform.review

import android.app.Activity
import android.util.Log
import com.google.android.play.core.review.ReviewManagerFactory
import template.core.base.platform.BuildConfig

/**
 * Default implementation of the AppReviewManager interface for Android platforms.
 *
 * This class leverages the Google Play In-App Review API to request user reviews
 * in accordance with platform guidelines. The implementation handles the complete
 * review flow process, including requesting review information and launching the
 * review dialog when appropriate.
 *
 * Note that the actual display of the review dialog is controlled by Google Play
 * Store policies, which may limit the frequency of review prompts to prevent user
 * fatigue. As such, calling the prompt methods does not guarantee that the review
 * dialog will be displayed.
 *
 * @property activity The Android Activity context required to initiate the review flow
 */
class AppReviewManagerImpl(
    private val activity: Activity,
) : AppReviewManager {
    /**
     * Prompts the user to review the application using the standard Google Play
     * In-App Review flow.
     *
     * This implementation follows a two-step process:
     * 1. Request review flow information from the Review Manager
     * 2. Launch the review flow with the obtained information
     *
     * The method handles potential failures in the review flow process and logs
     * errors appropriately. In debug builds, additional logging is provided to
     * facilitate testing and development.
     */
    override fun promptForReview() {
        val manager = ReviewManagerFactory.create(activity)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                manager.launchReviewFlow(activity, reviewInfo)
            } else {
                Log.e("Failed to launch review flow.", task.exception?.message.toString())
            }
        }

        if (BuildConfig.DEBUG) {
            Log.d("ReviewManager", "Prompting for review")
        }
    }

    /**
     * Provides infrastructure for a custom application-defined review experience.
     *
     * This method is intended for scenarios where the standard Google Play review flow
     * is insufficient for application requirements. Custom implementations might include:
     * - Multi-stage feedback collection
     * - Conditional review flows based on user satisfaction
     * - Alternative review destinations
     *
     * Note: This method currently contains a placeholder implementation and requires
     * further development to implement the custom review logic.
     */
    override fun promptForCustomReview() {
        // TODO:: Implement custom review flow
    }
}
