/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.common

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.create
import platform.Foundation.writeToFile

// iOS implementation
@BetaInteropApi
@OptIn(ExperimentalForeignApi::class)
actual fun createPlatformFileUtils(): FileUtils = object : FileUtils {
    override suspend fun writeInputStreamDataToFile(
        inputStream: ByteArray,
        filePath: String,
    ): Boolean =
        withContext(Dispatchers.Default) {
            try {
                val nsData = inputStream.toNSData()
                nsData.writeToFile(filePath, true)
                true
            } catch (e: Exception) {
                FileUtils.logger.e { "Error writing file: ${e.message}" }
                false
            }
        }

    @BetaInteropApi
    fun ByteArray.toNSData(): NSData = memScoped {
        NSData.create(bytes = allocArrayOf(this@toNSData), length = this@toNSData.size.toULong())
    }
}
