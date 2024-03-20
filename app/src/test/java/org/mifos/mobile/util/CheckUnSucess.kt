package org.mifos.mobile.util

import kotlinx.coroutines.flow.Flow
import java.io.IOException

suspend fun <T> checkForUnsuccessfulOperation(result: Flow<T>): Boolean {
    val resultList = mutableListOf<T>()
    var errorOccurred = false
    try {
        result.collect { resultList.add(it) }
    } catch (e: RuntimeException) {
        errorOccurred = true
        // Handle the error (optional)
    }
    return errorOccurred
}