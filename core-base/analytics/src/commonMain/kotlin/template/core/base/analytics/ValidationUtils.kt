/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.analytics

/** Data validation utilities for analytics events and parameters */

/** Analytics data validator */
class AnalyticsValidator {

    companion object {
        // Analytics platform constraints
        const val MAX_EVENT_NAME_LENGTH = 40
        const val MAX_PARAM_KEY_LENGTH = 40
        const val MAX_PARAM_VALUE_LENGTH = 100
        const val MAX_USER_PROPERTY_NAME_LENGTH = 24
        const val MAX_USER_PROPERTY_VALUE_LENGTH = 36
        const val MAX_USER_ID_LENGTH = 256
        const val MAX_PARAMS_PER_EVENT = 25

        // Validation patterns
        private val VALID_EVENT_NAME_PATTERN = Regex("^[a-zA-Z][a-zA-Z0-9_]*$")
        private val VALID_PARAM_KEY_PATTERN = Regex("^[a-zA-Z][a-zA-Z0-9_]*$")
        private val RESERVED_PREFIXES = setOf("firebase_", "google_", "ga_")
        private val RESERVED_EVENT_NAMES = setOf(
            "ad_activeview", "ad_click", "ad_exposure", "ad_impression", "ad_query",
            "adunit_exposure", "app_clear_data", "app_exception", "app_remove", "app_update",
            "error", "first_open", "first_visit", "in_app_purchase", "notification_dismiss",
            "notification_foreground", "notification_open", "notification_receive",
            "os_update", "screen_view", "session_start", "user_engagement",
        )
    }

    /** Validation result */
    sealed class ValidationResult {
        object Valid : ValidationResult()
        data class Invalid(val errors: List<String>) : ValidationResult()

        val isValid: Boolean get() = this is Valid
        val errorMessages: List<String> get() = if (this is Invalid) errors else emptyList()
    }

    /** Validate an analytics event */
    fun validateEvent(event: AnalyticsEvent): ValidationResult {
        val errors = mutableListOf<String>()

        // Validate event type
        errors.addAll(validateEventName(event.type))

        // Validate parameter count
        if (event.extras.size > MAX_PARAMS_PER_EVENT) {
            errors.add("Event has ${event.extras.size} parameters, maximum allowed is $MAX_PARAMS_PER_EVENT")
        }

        // Validate each parameter
        event.extras.forEach { param ->
            errors.addAll(validateParameter(param))
        }

        // Check for duplicate parameter keys
        val duplicateKeys = event.extras.groupBy { it.key }
            .filter { it.value.size > 1 }
            .keys
        if (duplicateKeys.isNotEmpty()) {
            errors.add("Event has duplicate parameter keys: ${duplicateKeys.joinToString(", ")}")
        }

        return if (errors.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(errors)
    }

    /** Validate event name */
    fun validateEventName(eventName: String): List<String> {
        val errors = mutableListOf<String>()

        if (eventName.isBlank()) {
            errors.add("Event name cannot be blank")
            return errors
        }

        if (eventName.length > MAX_EVENT_NAME_LENGTH) {
            errors.add("Event name '$eventName' exceeds maximum length of $MAX_EVENT_NAME_LENGTH characters")
        }

        if (!VALID_EVENT_NAME_PATTERN.matches(eventName)) {
            errors.add(
                "Event name '$eventName' contains invalid characters." +
                    " Must start with letter and contain only letters, numbers, and underscores",
            )
        }

        if (RESERVED_PREFIXES.any { eventName.startsWith(it) }) {
            errors.add("Event name '$eventName' uses reserved prefix")
        }

        if (RESERVED_EVENT_NAMES.contains(eventName)) {
            errors.add("Event name '$eventName' is reserved")
        }

        return errors
    }

    /** Validate parameter */
    fun validateParameter(param: Param): List<String> {
        val errors = mutableListOf<String>()

        // Validate key
        if (param.key.isBlank()) {
            errors.add("Parameter key cannot be blank")
        } else {
            if (param.key.length > MAX_PARAM_KEY_LENGTH) {
                errors.add(
                    "Parameter key '${param.key}' " +
                        "exceeds maximum length of $MAX_PARAM_KEY_LENGTH characters",
                )
            }

            if (!VALID_PARAM_KEY_PATTERN.matches(param.key)) {
                errors.add(
                    "Parameter key '${param.key}' contains invalid characters. Must start " +
                        "with letter and contain only letters, numbers, and underscores",
                )
            }

            if (RESERVED_PREFIXES.any { param.key.startsWith(it) }) {
                errors.add("Parameter key '${param.key}' uses reserved prefix")
            }
        }

        // Validate value
        if (param.value.length > MAX_PARAM_VALUE_LENGTH) {
            errors.add(
                "Parameter value for key '${param.key}' exceeds maximum" +
                    " length of $MAX_PARAM_VALUE_LENGTH characters",
            )
        }

        return errors
    }

    /** Validate user property */
    fun validateUserProperty(name: String, value: String): List<String> {
        val errors = mutableListOf<String>()

        if (name.isBlank()) {
            errors.add("User property name cannot be blank")
        } else {
            if (name.length > MAX_USER_PROPERTY_NAME_LENGTH) {
                errors.add(
                    "User property name '$name' exceeds maximum length " +
                        "of $MAX_USER_PROPERTY_NAME_LENGTH characters",
                )
            }

            if (!VALID_PARAM_KEY_PATTERN.matches(name)) {
                errors.add("User property name '$name' contains invalid characters")
            }

            if (RESERVED_PREFIXES.any { name.startsWith(it) }) {
                errors.add("User property name '$name' uses reserved prefix")
            }
        }

        if (value.length > MAX_USER_PROPERTY_VALUE_LENGTH) {
            errors.add(
                "User property value for '$name' exceeds maximum " +
                    "length of $MAX_USER_PROPERTY_VALUE_LENGTH characters",
            )
        }

        return errors
    }

    /** Validate user ID */
    fun validateUserId(userId: String): List<String> {
        val errors = mutableListOf<String>()

        if (userId.isBlank()) {
            errors.add("User ID cannot be blank")
        }

        if (userId.length > MAX_USER_ID_LENGTH) {
            errors.add("User ID exceeds maximum length of $MAX_USER_ID_LENGTH characters")
        }

        return errors
    }

    /** Sanitize event name to make it valid */
    fun sanitizeEventName(eventName: String): String {
        if (eventName.isBlank()) return "unknown_event"

        // Remove invalid characters and ensure it starts with letter
        var sanitized = eventName.replace(Regex("[^a-zA-Z0-9_]"), "_")
            .take(MAX_EVENT_NAME_LENGTH)

        // Ensure it starts with a letter
        if (!sanitized.first().isLetter()) {
            sanitized = "event_$sanitized"
        }

        // Avoid reserved names
        if (RESERVED_EVENT_NAMES.contains(sanitized) || RESERVED_PREFIXES.any {
                sanitized.startsWith(
                    it,
                )
            }
        ) {
            sanitized = "custom_$sanitized"
        }

        return sanitized.take(MAX_EVENT_NAME_LENGTH)
    }

    /** Sanitize parameter key to make it valid */
    fun sanitizeParameterKey(key: String): String {
        if (key.isBlank()) return "unknown_param"

        var sanitized = key.replace(Regex("[^a-zA-Z0-9_]"), "_")
            .take(MAX_PARAM_KEY_LENGTH)

        if (!sanitized.first().isLetter()) {
            sanitized = "param_$sanitized"
        }

        if (RESERVED_PREFIXES.any { sanitized.startsWith(it) }) {
            sanitized = "custom_$sanitized"
        }

        return sanitized.take(MAX_PARAM_KEY_LENGTH)
    }

    /** Sanitize parameter value to make it valid */
    fun sanitizeParameterValue(value: String): String {
        return value.take(MAX_PARAM_VALUE_LENGTH)
    }

    /** Create a safe parameter with validation and sanitization */
    fun createSafeParam(key: String, value: String): Param {
        val sanitizedKey = sanitizeParameterKey(key)
        val sanitizedValue = sanitizeParameterValue(value)
        return Param(sanitizedKey, sanitizedValue)
    }

    /** Create a safe event with validation and sanitization */
    fun createSafeEvent(type: String, params: List<Param> = emptyList()): AnalyticsEvent {
        val sanitizedType = sanitizeEventName(type)
        val sanitizedParams = params.map { createSafeParam(it.key, it.value) }
            .take(MAX_PARAMS_PER_EVENT)

        return AnalyticsEvent(sanitizedType, sanitizedParams)
    }
}

/**
 * Validating analytics helper that wraps another helper and validates
 * events
 */
class ValidatingAnalyticsHelper(
    private val delegate: AnalyticsHelper,
    private val validator: AnalyticsValidator = AnalyticsValidator(),
    // If true, throws on validation errors; if false, sanitizes
    private val strictMode: Boolean = false,
    private val logValidationErrors: Boolean = true,
) : AnalyticsHelper {

    override fun logEvent(event: AnalyticsEvent) {
        val validationResult = validator.validateEvent(event)

        when {
            validationResult.isValid -> {
                delegate.logEvent(event)
            }

            strictMode -> {
                throw IllegalArgumentException(
                    "Invalid analytics event: ${
                        validationResult.errorMessages.joinToString(
                            ", ",
                        )
                    }",
                )
            }

            else -> {
                // Sanitize and log
                val safeEvent = validator.createSafeEvent(event.type, event.extras)
                delegate.logEvent(safeEvent)

                if (logValidationErrors) {
                    delegate.logEvent(
                        AnalyticsEvent(
                            "analytics_validation_error",
                            listOf(
                                Param(key = "original_event_type", value = event.type),
                                Param(
                                    key = "errors",
                                    value = validationResult.errorMessages.joinToString("; "),
                                ),
                            ),
                        ),
                    )
                }
            }
        }
    }

    override fun setUserProperty(name: String, value: String) {
        val errors = validator.validateUserProperty(name, value)

        when {
            errors.isEmpty() -> {
                delegate.setUserProperty(name, value)
            }

            strictMode -> {
                throw IllegalArgumentException(
                    "Invalid user property: ${errors.joinToString(", ")}",
                )
            }

            else -> {
                val sanitizedName = validator.sanitizeParameterKey(name)
                    .take(AnalyticsValidator.MAX_USER_PROPERTY_NAME_LENGTH)
                val sanitizedValue = value.take(AnalyticsValidator.MAX_USER_PROPERTY_VALUE_LENGTH)
                delegate.setUserProperty(sanitizedName, sanitizedValue)

                if (logValidationErrors && errors.isNotEmpty()) {
                    delegate.logEvent(
                        AnalyticsEvent(
                            "user_property_validation_error",
                            listOf(
                                Param("property_name", name),
                                Param("errors", errors.joinToString("; ")),
                            ),
                        ),
                    )
                }
            }
        }
    }

    override fun setUserId(userId: String) {
        val errors = validator.validateUserId(userId)

        when {
            errors.isEmpty() -> {
                delegate.setUserId(userId)
            }

            strictMode -> {
                throw IllegalArgumentException("Invalid user ID: ${errors.joinToString(", ")}")
            }

            else -> {
                val sanitizedUserId = userId.take(AnalyticsValidator.MAX_USER_ID_LENGTH)
                delegate.setUserId(sanitizedUserId)

                if (logValidationErrors && errors.isNotEmpty()) {
                    delegate.logEvent(
                        AnalyticsEvent(
                            "user_id_validation_error",
                            listOf(
                                Param("errors", errors.joinToString("; ")),
                            ),
                        ),
                    )
                }
            }
        }
    }
}

/** Extension to wrap any analytics helper with validation */
fun AnalyticsHelper.withValidation(
    strictMode: Boolean = false,
    logValidationErrors: Boolean = true,
    validator: AnalyticsValidator = AnalyticsValidator(),
): AnalyticsHelper = ValidatingAnalyticsHelper(this, validator, strictMode, logValidationErrors)

/** Extension to validate an event without logging it */
fun AnalyticsEvent.validate(
    validator: AnalyticsValidator = AnalyticsValidator(),
): AnalyticsValidator.ValidationResult {
    return validator.validateEvent(this)
}

/** Extension to check if an event is valid */
fun AnalyticsEvent.isValid(validator: AnalyticsValidator = AnalyticsValidator()): Boolean {
    return validator.validateEvent(this).isValid
}

/** Extension to sanitize an event */
fun AnalyticsEvent.sanitize(
    validator: AnalyticsValidator = AnalyticsValidator(),
): AnalyticsEvent {
    return validator.createSafeEvent(this.type, this.extras)
}
