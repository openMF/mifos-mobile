package org.convention.keystore

import org.gradle.api.logging.Logger

/**
 * Utility class for consistent keystore task logging
 */
class KeystoreLogger(private val logger: Logger) {

    companion object {
        private const val PREFIX = "[KEYSTORE]"
        private const val SEPARATOR = "=============================================================================="
    }

    fun info(message: String) {
        logger.lifecycle("$PREFIX $message")
    }

    fun warning(message: String) {
        logger.warn("$PREFIX $message")
    }

    fun error(message: String) {
        logger.error("$PREFIX $message")
    }

    fun success(message: String) {
        logger.lifecycle("$PREFIX ✅ $message")
    }

    fun section(title: String) {
        logger.lifecycle("")
        logger.lifecycle("$PREFIX $SEPARATOR")
        logger.lifecycle("$PREFIX $title")
        logger.lifecycle("$PREFIX $SEPARATOR")
    }

    fun summary(title: String, items: List<Pair<String, Boolean>>) {
        logger.lifecycle("")
        section(title)
        items.forEach { (item, success) ->
            val status = if (success) "✅ SUCCESS" else "❌ FAILED"
            logger.lifecycle("$PREFIX $item: $status")
        }
    }
}