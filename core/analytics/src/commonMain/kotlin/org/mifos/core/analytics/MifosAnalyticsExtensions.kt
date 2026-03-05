/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
@file:Suppress("ktlint:standard:discouraged-comment-location")

package org.mifos.core.analytics

import template.core.base.analytics.AnalyticsHelper
import template.core.base.analytics.ParamKeys

/**
 * Extension functions for Mifos-specific analytics operations
 */

/**
 * Extension for tracking client creation flow
 */
fun AnalyticsHelper.trackClientCreationFlow(step: String, success: Boolean = true) {
    logEvent(
        "client_creation_flow",
        "step" to step,
        ParamKeys.SUCCESS to success.toString(),
    )
}

/**
 * Extension for tracking loan application flow
 */
fun AnalyticsHelper.trackLoanApplicationFlow(
    step: String,
    loanProductId: String? = null,
    success: Boolean = true,
) {
    val params = mutableMapOf(
        "step" to step,
        ParamKeys.SUCCESS to success.toString(),
    )
    loanProductId?.let { params["loan_product_id"] = it }
    logEvent("loan_application_flow", params)
}

/**
 * Extension for tracking navigation patterns
 */
fun AnalyticsHelper.trackNavigation(from: String, to: String, trigger: String = "user_action") {
    logEvent(
        "navigation_flow",
        "from_screen" to from,
        "to_screen" to to,
        "trigger" to trigger,
    )
}

/**
 * Extension for tracking API call performance
 */
fun AnalyticsHelper.trackApiCall(
    endpoint: String,
    method: String,
    responseTime: Long,
    statusCode: Int,
    success: Boolean = statusCode in 200..299,
) {
    logEvent(
        "api_call",
        ParamKeys.API_ENDPOINT to endpoint,
        "http_method" to method,
        "response_time_ms" to responseTime.toString(),
        "status_code" to statusCode.toString(),
        ParamKeys.SUCCESS to success.toString(),
    )
}

/**
 * Extension for tracking form validation errors
 */
fun AnalyticsHelper.trackValidationError(
    formName: String,
    fieldName: String,
    errorType: String,
    screenName: String? = null,
) {
    val params = mutableMapOf(
        ParamKeys.FORM_NAME to formName,
        ParamKeys.FIELD_NAME to fieldName,
        "validation_error_type" to errorType,
    )
    screenName?.let { params[ParamKeys.SCREEN_NAME] = it }
    logEvent("validation_error", params)
}

/**
 * Extension for tracking data synchronization
 */
fun AnalyticsHelper.trackDataSync(
    entityType: String,
    operation: String, // "upload", "download", "merge"
    recordCount: Int,
    duration: Long,
    success: Boolean = true,
) {
    logEvent(
        "data_sync",
        "entity_type" to entityType,
        "sync_operation" to operation,
        "record_count" to recordCount.toString(),
        ParamKeys.DURATION to "${duration}ms",
        ParamKeys.SUCCESS to success.toString(),
    )
}

/**
 * Extension for tracking user preferences changes
 */
fun AnalyticsHelper.trackPreferenceChange(
    preferenceName: String,
    oldValue: String?,
    newValue: String,
) {
    val params = mutableMapOf(
        "preference_name" to preferenceName,
        "new_value" to newValue,
    )
    oldValue?.let { params["old_value"] = it }
    logEvent("preference_changed", params)
}

/**
 * Extension for tracking tutorial interactions
 */
fun AnalyticsHelper.trackTutorial(action: String, step: Int, tutorialName: String) {
    logEvent(
        "tutorial_interaction",
        "tutorial_name" to tutorialName,
        "tutorial_action" to action,
        ParamKeys.TUTORIAL_STEP to step.toString(),
    )
}

/**
 * Extension for tracking document operations
 */
fun AnalyticsHelper.trackDocumentOperation(
    // "upload", "download", "view", "delete"
    operation: String,
    documentType: String,
    fileSize: Long? = null,
    success: Boolean = true,
) {
    val params = mutableMapOf(
        "document_operation" to operation,
        "document_type" to documentType,
        ParamKeys.SUCCESS to success.toString(),
    )
    fileSize?.let { params["file_size_bytes"] = it.toString() }
    logEvent("document_operation", params)
}

/**
 * Extension for tracking biometric authentication
 */
fun AnalyticsHelper.trackBiometricAuth(
    // "fingerprint", "face", "voice"
    authType: String,
    success: Boolean,
    fallbackUsed: Boolean = false,
) {
    logEvent(
        "biometric_auth",
        "auth_type" to authType,
        ParamKeys.SUCCESS to success.toString(),
        "fallback_used" to fallbackUsed.toString(),
    )
}

/**
 * Extension for tracking geolocation usage
 */
fun AnalyticsHelper.trackLocationUsage(
    // "client_visit", "center_meeting", "field_collection"
    feature: String,
    accuracy: Float? = null,
    permissionGranted: Boolean = true,
) {
    val params = mutableMapOf(
        "location_feature" to feature,
        "permission_granted" to permissionGranted.toString(),
    )
    accuracy?.let { params["location_accuracy"] = it.toString() }
    logEvent("location_usage", params)
}

/**
 * Extension for tracking notification interactions
 */
fun AnalyticsHelper.trackNotificationInteraction(
    notificationType: String,
    // "opened", "dismissed", "action_clicked"
    action: String,
    notificationId: String? = null,
) {
    val params = mutableMapOf(
        "notification_type" to notificationType,
        "notification_action" to action,
    )
    notificationId?.let { params["notification_id"] = it }
    logEvent("notification_interaction", params)
}

/**
 * Extension for tracking accessibility features usage
 */
fun AnalyticsHelper.trackAccessibilityUsage(
    // "talkback", "large_text", "high_contrast"
    feature: String,
    enabled: Boolean,
) {
    logEvent(
        "accessibility_usage",
        "accessibility_feature" to feature,
        "enabled" to enabled.toString(),
    )
}

/**
 * Extension for tracking backup and restore operations
 */
fun AnalyticsHelper.trackBackupRestore(
    // "backup", "restore"
    operation: String,
    dataSize: Long,
    duration: Long,
    success: Boolean = true,
) {
    logEvent(
        "backup_restore",
        "backup_operation" to operation,
        "data_size_bytes" to dataSize.toString(),
        ParamKeys.DURATION to "${duration}ms",
        ParamKeys.SUCCESS to success.toString(),
    )
}

/**
 * Convenience function to create Mifos analytics tracker
 */
fun AnalyticsHelper.mifosTracker(): MifosAnalyticsTracker = MifosAnalyticsTracker(this)

/**
 * Extension for tracking custom business events specific to microfinance
 */
fun AnalyticsHelper.trackMicrofinanceEvent(
    eventName: String,
    businessParams: Map<String, String>,
) {
    logEvent(
        "microfinance_event",
        mapOf("event_name" to eventName) + businessParams,
    )
}
