/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.common

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Extension function to convert ByteArray to Base64 string
 */
@OptIn(ExperimentalEncodingApi::class)
fun ByteArray.toBase64(): String {
    return Base64.encode(this)
}

/**
 * Extension function to convert ByteArray to Base64 string with data URI prefix
 * @param mimeType The MIME type of the data (e.g., "image/png", "image/jpeg")
 */
@OptIn(ExperimentalEncodingApi::class)
fun ByteArray.toBase64DataUri(mimeType: String = "application/octet-stream"): String {
    return "data:$mimeType;base64,${Base64.encode(this)}"
}

/**
 * Extension function to convert Base64 string to ByteArray
 * @throws IllegalArgumentException if the string is not valid Base64
 */
@OptIn(ExperimentalEncodingApi::class)
fun String.fromBase64(): ByteArray {
    return Base64.decode(this)
}

/**
 * Extension function to safely convert Base64 string to ByteArray
 * @return ByteArray if successful, null if the string is not valid Base64
 */
@OptIn(ExperimentalEncodingApi::class)
fun String.fromBase64OrNull(): ByteArray? {
    return try {
        Base64.decode(this)
    } catch (e: IllegalArgumentException) {
        null
    }
}

/**
 * Extension function to convert Base64 data URI to ByteArray
 * Handles data URIs in format: "data:mime/type;base64,actualBase64Data"
 * @return ByteArray of the decoded data
 * @throws IllegalArgumentException if the data URI format is invalid
 */
@OptIn(ExperimentalEncodingApi::class)
fun String.fromBase64DataUri(): ByteArray {
    val dataUriPrefix = "data:"
    val base64Prefix = ";base64,"

    require(this.startsWith(dataUriPrefix)) {
        "Invalid data URI: must start with 'data:'"
    }

    val base64Index = this.indexOf(base64Prefix)
    require(base64Index != -1) {
        "Invalid data URI: missing ';base64,' separator"
    }

    val base64Data = this.substring(base64Index + base64Prefix.length)
    return Base64.decode(base64Data)
}

/**
 * Extension function to safely convert Base64 data URI to ByteArray
 * @return ByteArray if successful, null if the data URI format is invalid
 */
@OptIn(ExperimentalEncodingApi::class)
fun String.fromBase64DataUriOrNull(): ByteArray? {
    return try {
        fromBase64DataUri()
    } catch (e: IllegalArgumentException) {
        null
    }
}

/**
 * Extension function to extract MIME type from Base64 data URI
 * @return MIME type string or null if not a valid data URI
 */
fun String.extractMimeTypeFromDataUri(): String? {
    val dataUriPrefix = "data:"
    val base64Prefix = ";base64,"

    return takeIf { it.startsWith(dataUriPrefix) && it.contains(base64Prefix) }
        ?.substringAfter(dataUriPrefix)
        ?.substringBefore(base64Prefix)
}
