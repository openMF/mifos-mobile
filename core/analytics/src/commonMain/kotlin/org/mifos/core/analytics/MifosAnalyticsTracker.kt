/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.core.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import template.core.base.analytics.AnalyticsEvent
import template.core.base.analytics.AnalyticsHelper
import template.core.base.analytics.Param
import template.core.base.analytics.ParamKeys
import template.core.base.analytics.Types
import template.core.base.analytics.rememberAnalyticsHelper

/**
 * Project-specific analytics tracker that provides domain-specific
 * tracking methods for the Mifos application.
 */
class MifosAnalyticsTracker(
    private val analyticsHelper: AnalyticsHelper,
) {

    /** Track user authentication events */
    fun trackLogin(method: String, success: Boolean, errorCode: String? = null) {
        val eventType = if (success) Types.LOGIN_SUCCESS else Types.LOGIN_FAILURE
        val params = mutableListOf(
            Param("login_method", method),
            Param(ParamKeys.SUCCESS, success.toString()),
        )
        errorCode?.let { params.add(Param(ParamKeys.ERROR_CODE, it)) }

        analyticsHelper.logEvent(AnalyticsEvent(eventType, params))
    }

    /** Track client-related operations */
    fun trackClientOperation(
        // "create", "view", "update", "search"
        operation: String,
        clientId: String? = null,
        success: Boolean = true,
        duration: Long? = null,
    ) {
        val params = mutableListOf(
            Param("client_operation", operation),
            Param(ParamKeys.SUCCESS, success.toString()),
        )
        clientId?.let { params.add(Param("client_id", it)) }
        duration?.let { params.add(Param(ParamKeys.DURATION, "${it}ms")) }

        analyticsHelper.logEvent(AnalyticsEvent("client_operation", params))
    }

    /** Track loan-related operations */
    fun trackLoanOperation(
        // "apply", "approve", "disburse", "repay", "view"
        operation: String,
        loanId: String? = null,
        loanType: String? = null,
        amount: String? = null,
        success: Boolean = true,
    ) {
        val params = mutableListOf(
            Param("loan_operation", operation),
            Param(ParamKeys.SUCCESS, success.toString()),
        )
        loanId?.let { params.add(Param("loan_id", it)) }
        loanType?.let { params.add(Param("loan_type", it)) }
        amount?.let { params.add(Param("loan_amount", it)) }

        analyticsHelper.logEvent(AnalyticsEvent("loan_operation", params))
    }

    /** Track savings account operations */
    fun trackSavingsOperation(
        // "create", "deposit", "withdraw", "view"
        operation: String,
        accountId: String? = null,
        amount: String? = null,
        success: Boolean = true,
    ) {
        val params = mutableListOf(
            Param("savings_operation", operation),
            Param(ParamKeys.SUCCESS, success.toString()),
        )
        accountId?.let { params.add(Param("account_id", it)) }
        amount?.let { params.add(Param("transaction_amount", it)) }

        analyticsHelper.logEvent(AnalyticsEvent("savings_operation", params))
    }

    /** Track group operations */
    fun trackGroupOperation(
        // "create", "join", "leave", "view"
        operation: String,
        groupId: String? = null,
        groupType: String? = null,
        memberCount: Int? = null,
        success: Boolean = true,
    ) {
        val params = mutableListOf(
            Param("group_operation", operation),
            Param(ParamKeys.SUCCESS, success.toString()),
        )
        groupId?.let { params.add(Param("group_id", it)) }
        groupType?.let { params.add(Param("group_type", it)) }
        memberCount?.let { params.add(Param("member_count", it.toString())) }

        analyticsHelper.logEvent(AnalyticsEvent("group_operation", params))
    }

    /** Track center operations (Mifos-specific) */
    fun trackCenterOperation(
        // "create", "view", "meeting", "collection"
        operation: String,
        centerId: String? = null,
        meetingDate: String? = null,
        attendance: Int? = null,
        success: Boolean = true,
    ) {
        val params = mutableListOf(
            Param("center_operation", operation),
            Param(ParamKeys.SUCCESS, success.toString()),
        )
        centerId?.let { params.add(Param("center_id", it)) }
        meetingDate?.let { params.add(Param("meeting_date", it)) }
        attendance?.let { params.add(Param("attendance_count", it.toString())) }

        analyticsHelper.logEvent(AnalyticsEvent("center_operation", params))
    }

    /** Track survey operations */
    fun trackSurveyOperation(
        // "start", "complete", "abandon"
        operation: String,
        surveyId: String? = null,
        questionCount: Int? = null,
        completionTime: Long? = null,
    ) {
        val params = mutableListOf(Param("survey_operation", operation))
        surveyId?.let { params.add(Param("survey_id", it)) }
        questionCount?.let { params.add(Param("question_count", it.toString())) }
        completionTime?.let { params.add(Param(ParamKeys.COMPLETION_TIME, "${it}ms")) }

        analyticsHelper.logEvent(AnalyticsEvent("survey_operation", params))
    }

    /** Track report generation */
    fun trackReportGeneration(
        reportType: String,
        filterParams: Map<String, String> = emptyMap(),
        resultCount: Int? = null,
        generationTime: Long? = null,
        success: Boolean = true,
    ) {
        val params = mutableListOf(
            Param("report_type", reportType),
            Param(ParamKeys.SUCCESS, success.toString()),
        )
        resultCount?.let { params.add(Param(ParamKeys.RESULT_COUNT, it.toString())) }
        generationTime?.let { params.add(Param("generation_time_ms", it.toString())) }

        // Add filter parameters with prefix
        filterParams.forEach { (key, value) ->
            params.add(Param("filter_$key", value))
        }

        analyticsHelper.logEvent(AnalyticsEvent("report_generated", params))
    }

    /** Track sync operations */
    fun trackSync(
        // "full", "incremental", "force"
        syncType: String,
        itemCount: Int? = null,
        duration: Long? = null,
        success: Boolean = true,
        errorMessage: String? = null,
    ) {
        val params = mutableListOf(
            Param("sync_type", syncType),
            Param(ParamKeys.SUCCESS, success.toString()),
        )
        itemCount?.let { params.add(Param("sync_item_count", it.toString())) }
        duration?.let { params.add(Param(ParamKeys.DURATION, "${it}ms")) }
        errorMessage?.let { params.add(Param(ParamKeys.ERROR_MESSAGE, it)) }

        analyticsHelper.logEvent(AnalyticsEvent("sync_operation", params))
    }

    /** Track offline operations */
    fun trackOfflineOperation(
        operation: String,
        // "client", "loan", "savings", etc.
        entityType: String,
        queueSize: Int? = null,
        success: Boolean = true,
    ) {
        val params = mutableListOf(
            Param("offline_operation", operation),
            Param("entity_type", entityType),
            Param(ParamKeys.SUCCESS, success.toString()),
        )
        queueSize?.let { params.add(Param("queue_size", it.toString())) }

        analyticsHelper.logEvent(AnalyticsEvent("offline_operation", params))
    }

    /** Track performance metrics */
    fun trackPerformance(
        operation: String,
        duration: Long,
        success: Boolean = true,
        additionalMetrics: Map<String, String> = emptyMap(),
    ) {
        val params = mutableListOf(
            Param("performance_operation", operation),
            Param(ParamKeys.DURATION, "${duration}ms"),
            Param(ParamKeys.SUCCESS, success.toString()),
        )

        additionalMetrics.forEach { (key, value) ->
            params.add(Param("metric_$key", value))
        }

        analyticsHelper.logEvent(AnalyticsEvent("performance_metric", params))
    }
}

@Composable
fun rememberMifosAnalyticsTracker(): MifosAnalyticsTracker {
    val analyticsHelper = rememberAnalyticsHelper()
    return remember(analyticsHelper) { MifosAnalyticsTracker(analyticsHelper) }
}
