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
/**
 * Mifos-specific event types
 */
object MifosEventTypes {
    // Client Management Events
    const val CLIENT_CREATED = "client_created"
    const val CLIENT_UPDATED = "client_updated"
    const val CLIENT_ACTIVATED = "client_activated"
    const val CLIENT_CLOSED = "client_closed"
    const val CLIENT_SEARCHED = "client_searched"
    const val CLIENT_PROFILE_VIEWED = "client_profile_viewed"

    // Loan Management Events
    const val LOAN_APPLICATION_STARTED = "loan_application_started"
    const val LOAN_APPLICATION_SUBMITTED = "loan_application_submitted"
    const val LOAN_APPROVED = "loan_approved"
    const val LOAN_REJECTED = "loan_rejected"
    const val LOAN_DISBURSED = "loan_disbursed"
    const val LOAN_REPAYMENT_MADE = "loan_repayment_made"
    const val LOAN_RESCHEDULED = "loan_rescheduled"
    const val LOAN_WRITTEN_OFF = "loan_written_off"
    const val LOAN_CALCULATOR_USED = "loan_calculator_used"

    // Savings Account Events
    const val SAVINGS_ACCOUNT_CREATED = "savings_account_created"
    const val SAVINGS_ACCOUNT_ACTIVATED = "savings_account_activated"
    const val SAVINGS_DEPOSIT_MADE = "savings_deposit_made"
    const val SAVINGS_WITHDRAWAL_MADE = "savings_withdrawal_made"
    const val SAVINGS_ACCOUNT_CLOSED = "savings_account_closed"
    const val SAVINGS_INTEREST_CALCULATED = "savings_interest_calculated"

    // Group Management Events
    const val GROUP_CREATED = "group_created"
    const val GROUP_MEMBER_ADDED = "group_member_added"
    const val GROUP_MEMBER_REMOVED = "group_member_removed"
    const val GROUP_MEETING_CONDUCTED = "group_meeting_conducted"
    const val GROUP_LOAN_DISBURSED = "group_loan_disbursed"
    const val GROUP_COLLECTION_MADE = "group_collection_made"

    // Center Management Events
    const val CENTER_CREATED = "center_created"
    const val CENTER_MEETING_CONDUCTED = "center_meeting_conducted"
    const val CENTER_COLLECTION_SHEET_GENERATED = "center_collection_sheet_generated"
    const val CENTER_ATTENDANCE_RECORDED = "center_attendance_recorded"

    // Survey and Data Collection Events
    const val SURVEY_STARTED = "survey_started"
    const val SURVEY_QUESTION_ANSWERED = "survey_question_answered"
    const val SURVEY_COMPLETED = "survey_completed"
    const val SURVEY_ABANDONED = "survey_abandoned"
    const val DATA_TABLE_ENTRY_CREATED = "data_table_entry_created"

    // Reporting Events
    const val REPORT_GENERATED = "report_generated"
    const val REPORT_EXPORTED = "report_exported"
    const val REPORT_SHARED = "report_shared"
    const val DASHBOARD_VIEWED = "dashboard_viewed"
    const val CHART_VIEWED = "chart_viewed"

    // Sync and Offline Events
    const val OFFLINE_MODE_ENABLED = "offline_mode_enabled"
    const val OFFLINE_MODE_DISABLED = "offline_mode_disabled"
    const val DATA_SYNC_INITIATED = "data_sync_initiated"
    const val DATA_SYNC_COMPLETED = "data_sync_completed"
    const val DATA_SYNC_FAILED = "data_sync_failed"
    const val OFFLINE_TRANSACTION_QUEUED = "offline_transaction_queued"
    const val OFFLINE_TRANSACTION_SYNCED = "offline_transaction_synced"

    // Authentication and Security Events
    const val BIOMETRIC_LOGIN_ATTEMPT = "biometric_login_attempt"
    const val PIN_LOGIN_ATTEMPT = "pin_login_attempt"
    const val PASSWORD_CHANGED = "password_changed"
    const val SESSION_TIMEOUT = "session_timeout"
    const val UNAUTHORIZED_ACCESS_ATTEMPT = "unauthorized_access_attempt"

    // Document and File Events
    const val DOCUMENT_UPLOADED = "document_uploaded"
    const val DOCUMENT_DOWNLOADED = "document_downloaded"
    const val PHOTO_CAPTURED = "photo_captured"
    const val SIGNATURE_CAPTURED = "signature_captured"
    const val FILE_SHARED = "file_shared"

    // Configuration and Settings Events
    const val LANGUAGE_CHANGED = "language_changed"
    const val THEME_CHANGED = "theme_changed"
    const val NOTIFICATION_SETTING_CHANGED = "notification_setting_changed"
    const val BACKUP_CREATED = "backup_created"
    const val BACKUP_RESTORED = "backup_restored"

    // Performance and Error Events
    const val APP_CRASH_OCCURRED = "app_crash_occurred"
    const val API_TIMEOUT = "api_timeout"
    const val NETWORK_CONNECTION_LOST = "network_connection_lost"
    const val NETWORK_CONNECTION_RESTORED = "network_connection_restored"
    const val LOW_STORAGE_WARNING = "low_storage_warning"
    const val BATTERY_LOW_WARNING = "battery_low_warning"
}

/**
 * Mifos-specific parameter keys
 */
object MifosParamKeys {
    // Client-specific parameters
    const val CLIENT_ID = "client_id"
    const val CLIENT_TYPE = "client_type"
    const val CLIENT_STATUS = "client_status"
    const val CLIENT_OFFICE = "client_office"
    const val CLIENT_STAFF = "client_staff"

    // Loan-specific parameters
    const val LOAN_ID = "loan_id"
    const val LOAN_PRODUCT_ID = "loan_product_id"
    const val LOAN_PRODUCT_NAME = "loan_product_name"
    const val LOAN_AMOUNT = "loan_amount"
    const val LOAN_TERM = "loan_term"
    const val LOAN_INTEREST_RATE = "loan_interest_rate"
    const val LOAN_STATUS = "loan_status"
    const val REPAYMENT_AMOUNT = "repayment_amount"
    const val REPAYMENT_METHOD = "repayment_method"

    // Savings-specific parameters
    const val SAVINGS_ACCOUNT_ID = "savings_account_id"
    const val SAVINGS_PRODUCT_ID = "savings_product_id"
    const val DEPOSIT_AMOUNT = "deposit_amount"
    const val WITHDRAWAL_AMOUNT = "withdrawal_amount"
    const val ACCOUNT_BALANCE = "account_balance"
    const val TRANSACTION_TYPE = "transaction_type"

    // Group-specific parameters
    const val GROUP_ID = "group_id"
    const val GROUP_NAME = "group_name"
    const val GROUP_TYPE = "group_type"
    const val MEMBER_COUNT = "member_count"
    const val MEETING_DATE = "meeting_date"
    const val ATTENDANCE_COUNT = "attendance_count"

    // Center-specific parameters
    const val CENTER_ID = "center_id"
    const val CENTER_NAME = "center_name"
    const val COLLECTION_AMOUNT = "collection_amount"
    const val COLLECTION_METHOD = "collection_method"

    // Survey-specific parameters
    const val SURVEY_ID = "survey_id"
    const val SURVEY_NAME = "survey_name"
    const val QUESTION_ID = "question_id"
    const val QUESTION_TYPE = "question_type"
    const val ANSWER_VALUE = "answer_value"

    // Report-specific parameters
    const val REPORT_ID = "report_id"
    const val REPORT_NAME = "report_name"
    const val REPORT_TYPE = "report_type"
    const val EXPORT_FORMAT = "export_format"
    const val FILTER_OFFICE = "filter_office"
    const val FILTER_STAFF = "filter_staff"
    const val FILTER_DATE_FROM = "filter_date_from"
    const val FILTER_DATE_TO = "filter_date_to"

    // Sync-specific parameters
    const val SYNC_TYPE = "sync_type"
    const val ENTITY_TYPE = "entity_type"
    const val RECORD_COUNT = "record_count"
    const val SYNC_DIRECTION = "sync_direction"
    const val CONFLICT_COUNT = "conflict_count"

    // Document-specific parameters
    const val DOCUMENT_ID = "document_id"
    const val DOCUMENT_TYPE = "document_type"
    const val FILE_SIZE = "file_size"
    const val FILE_FORMAT = "file_format"
    const val UPLOAD_METHOD = "upload_method"

    // Performance-specific parameters
    const val MEMORY_USAGE = "memory_usage_mb"
    const val STORAGE_AVAILABLE = "storage_available_mb"
    const val BATTERY_LEVEL = "battery_level"
    const val NETWORK_SPEED = "network_speed_kbps"
    const val GPS_ACCURACY = "gps_accuracy_meters"

    // Security-specific parameters
    const val AUTH_METHOD = "auth_method"
    const val SECURITY_LEVEL = "security_level"
    const val PERMISSION_TYPE = "permission_type"
    const val ACCESS_LEVEL = "access_level"

    // Configuration-specific parameters
    const val SETTING_NAME = "setting_name"
    const val OLD_VALUE = "old_value"
    const val NEW_VALUE = "new_value"
    const val LANGUAGE_CODE = "language_code"
    const val THEME_NAME = "theme_name"
}

/**
 * Predefined common parameter values
 */
object MifosParamValues {
    // Client types
    const val CLIENT_TYPE_INDIVIDUAL = "individual"
    const val CLIENT_TYPE_ENTITY = "entity"

    // Loan statuses
    const val LOAN_STATUS_PENDING = "pending"
    const val LOAN_STATUS_APPROVED = "approved"
    const val LOAN_STATUS_ACTIVE = "active"
    const val LOAN_STATUS_CLOSED = "closed"
    const val LOAN_STATUS_REJECTED = "rejected"

    // Transaction types
    const val TRANSACTION_DEPOSIT = "deposit"
    const val TRANSACTION_WITHDRAWAL = "withdrawal"
    const val TRANSACTION_TRANSFER = "transfer"

    // Authentication methods
    const val AUTH_PASSWORD = "password"
    const val AUTH_PIN = "pin"
    const val AUTH_BIOMETRIC = "biometric"
    const val AUTH_PATTERN = "pattern"

    // Sync types
    const val SYNC_FULL = "full"
    const val SYNC_INCREMENTAL = "incremental"
    const val SYNC_FORCED = "forced"

    // Export formats
    const val FORMAT_PDF = "pdf"
    const val FORMAT_EXCEL = "excel"
    const val FORMAT_CSV = "csv"
    const val FORMAT_JSON = "json"
}
