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

/**
 * Manages application update detection and processing.
 *
 * This interface abstracts platform-specific implementations for checking
 * and managing application updates. It provides methods to initiate update checks
 * and verify the state of previously initiated update processes.
 *
 * Platform-specific implementations of this interface typically integrate with:
 * - Google Play In-App Updates API on Android
 * - AppStoreKit on iOS
 * - Other platform-specific update mechanisms
 *
 * The update management process is essential for ensuring users have access to
 * the latest features, security patches, and performance improvements.
 */
interface AppUpdateManager {

    /**
     * Initiates a check for available application updates.
     *
     * This method communicates with the relevant app distribution platform to
     * determine if a newer version of the application is available for download.
     * The implementation may handle the entire update flow, including presenting
     * update dialogs to the user and facilitating the download and installation
     * process, depending on platform capabilities.
     */
    fun checkForAppUpdate()

    /**
     * Verifies if there is an update process that was previously initiated but not completed.
     *
     * This method is typically called during application startup to determine if
     * an update process needs to be resumed. Update processes may be interrupted
     * by application termination, system restart, or other events, and this method
     * allows the application to recover and continue the update process.
     */
    fun checkForResumeUpdateState()
}
