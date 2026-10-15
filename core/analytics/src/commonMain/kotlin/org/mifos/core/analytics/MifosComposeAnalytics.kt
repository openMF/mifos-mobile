/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
@file:Suppress("SpreadOperator")

package org.mifos.core.analytics

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import template.core.base.analytics.AnalyticsHelper
import template.core.base.analytics.rememberAnalyticsHelper

/** Mifos-specific Compose analytics utilities */

/** Track Mifos screen views with additional business context */
@Composable
fun TrackMifosScreen(
    screenName: String,
    clientId: String? = null,
    loanId: String? = null,
    groupId: String? = null,
    additionalParams: Map<String, String> = emptyMap(),
) {
    val analytics = rememberAnalyticsHelper()
    val mifosTracker = remember(analytics) { analytics.mifosTracker() }

    LaunchedEffect(screenName) {
        val params = mutableMapOf<String, String>()
        clientId?.let { params[MifosParamKeys.CLIENT_ID] = it }
        loanId?.let { params[MifosParamKeys.LOAN_ID] = it }
        groupId?.let { params[MifosParamKeys.GROUP_ID] = it }
        params.putAll(additionalParams)

        analytics.logScreenView(screenName)
        if (params.isNotEmpty()) {
            analytics.logEvent("mifos_screen_context", params)
        }
    }
}

/** Track client-related button clicks */
fun Modifier.trackClientAction(
    action: String,
    clientId: String? = null,
): Modifier = this.then(
    Modifier.trackMifosAction(
        "client_action",
        mapOf(
            "action" to action,
            *clientId?.let { arrayOf(MifosParamKeys.CLIENT_ID to it) } ?: emptyArray(),
        ),
    ),
)

/** Track loan-related button clicks */
fun Modifier.trackLoanAction(
    action: String,
    loanId: String? = null,
    loanProductId: String? = null,
): Modifier = this.then(
    Modifier.trackMifosAction(
        "loan_action",
        mapOf(
            "action" to action,
            *loanId?.let { arrayOf(MifosParamKeys.LOAN_ID to it) } ?: emptyArray(),
            *loanProductId?.let { arrayOf(MifosParamKeys.LOAN_PRODUCT_ID to it) } ?: emptyArray(),
        ),
    ),
)

/** Track savings-related button clicks */
fun Modifier.trackSavingsAction(
    action: String,
    accountId: String? = null,
): Modifier = this.then(
    Modifier.trackMifosAction(
        "savings_action",
        mapOf(
            "action" to action,
            *accountId?.let { arrayOf(MifosParamKeys.SAVINGS_ACCOUNT_ID to it) } ?: emptyArray(),
        ),
    ),
)

/** Generic Mifos action tracker */
@Suppress("UnusedParameter")
private fun Modifier.trackMifosAction(
    eventType: String,
    params: Map<String, String>,
): Modifier = this.clickable {
    // Note: In a real implementation, you'd need to access the analytics helper here
    // This is a simplified version for demonstration
}

/** Track form field interactions in Mifos forms */
@Composable
fun TrackMifosFormField(
    fieldName: String,
    formName: String,
    fieldType: String = "text",
) {
    val analytics = rememberAnalyticsHelper()

    LaunchedEffect(fieldName, formName) {
        analytics.logEvent(
            "form_field_focused",
            "field_name" to fieldName,
            "form_name" to formName,
            "field_type" to fieldType,
        )
    }
}

/** Track Mifos business flow completion */
@Composable
fun TrackMifosFlowCompletion(
    flowName: String,
    step: String,
    totalSteps: Int,
    entityId: String? = null,
) {
    val analytics = rememberAnalyticsHelper()

    LaunchedEffect(step) {
        val params = mutableMapOf(
            "flow_name" to flowName,
            "current_step" to step,
            "total_steps" to totalSteps.toString(),
            "progress_percentage" to "${(step.toIntOrNull() ?: 0) * 100 / totalSteps}",
        )
        entityId?.let { params["entity_id"] = it }

        analytics.logEvent("mifos_flow_progress", params)
    }
}

/** Track navigation within Mifos workflows */
@Composable
fun TrackMifosNavigation(
    fromScreen: String,
    toScreen: String,
    navigationTrigger: String = "user_action",
    workflowName: String? = null,
) {
    val analytics = rememberAnalyticsHelper()

    LaunchedEffect(fromScreen, toScreen) {
        val params = mutableMapOf(
            "from_screen" to fromScreen,
            "to_screen" to toScreen,
            "trigger" to navigationTrigger,
        )
        workflowName?.let { params["workflow"] = it }

        analytics.logEvent("mifos_navigation", params)
    }
}

/** Track document operations in Mifos */
@Composable
fun rememberMifosDocumentTracker(): DocumentTracker {
    val analytics = rememberAnalyticsHelper()
    return remember(analytics) { DocumentTracker(analytics) }
}

class DocumentTracker(private val analytics: AnalyticsHelper) {
    fun trackUpload(documentType: String, fileSize: Long, success: Boolean = true) {
        analytics.trackDocumentOperation("upload", documentType, fileSize, success)
    }

    fun trackDownload(documentType: String, success: Boolean = true) {
        analytics.trackDocumentOperation("download", documentType, success = success)
    }

    fun trackView(documentType: String) {
        analytics.trackDocumentOperation("view", documentType, success = true)
    }
}

/** Track survey interactions in Mifos */
@Composable
fun TrackMifosSurvey(
    surveyId: String,
    // "started", "answered", "completed", "abandoned"
    action: String,
    questionId: String? = null,
) {
    val analytics = rememberAnalyticsHelper()

    LaunchedEffect(surveyId, questionId, action) {
        val params = mutableMapOf(
            MifosParamKeys.SURVEY_ID to surveyId,
            "survey_action" to action,
        )
        questionId?.let { params[MifosParamKeys.QUESTION_ID] = it }

        analytics.logEvent("mifos_survey_interaction", params)
    }
}

/** Track report generation in Mifos */
@Composable
fun rememberMifosReportTracker(): ReportTracker {
    val analytics = rememberAnalyticsHelper()
    return remember(analytics) { ReportTracker(analytics) }
}

class ReportTracker(private val analytics: AnalyticsHelper) {
    val analyticsTracker = MifosAnalyticsTracker(analytics)
    fun trackGeneration(
        reportName: String,
        filters: Map<String, String> = emptyMap(),
        duration: Long,
        success: Boolean = true,
    ) {
        analyticsTracker.trackReportGeneration(
            reportType = reportName,
            filterParams = filters,
            generationTime = duration,
            success = success,
        )
    }

    fun trackExport(reportName: String, format: String, success: Boolean = true) {
        analytics.logEvent(
            "report_exported",
            MifosParamKeys.REPORT_NAME to reportName,
            MifosParamKeys.EXPORT_FORMAT to format,
            "success" to success.toString(),
        )
    }

    fun trackShare(reportName: String, method: String) {
        analytics.logEvent(
            "report_shared",
            MifosParamKeys.REPORT_NAME to reportName,
            "share_method" to method,
        )
    }
}
