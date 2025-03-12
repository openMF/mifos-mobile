/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr.qrCodeDisplay

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import org.koin.core.context.GlobalContext
import org.mifos.mobile.core.common.FileUtils.Companion.logger
import java.io.File
import java.io.FileOutputStream

actual fun share(qrBitmap: ImageBitmap, string: String) {
    val context: Context = GlobalContext.get().get()

    val uri = getImageUri(context, qrBitmap)

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(
        Intent.createChooser(intent, string),
    )
}

private fun getImageUri(context: Context?, bitmap: ImageBitmap): Uri {
    try {
        val androidBitmap = bitmap.asAndroidBitmap()
        val cachePath = File(context?.cacheDir, "images")
        cachePath.mkdirs()
        val stream = FileOutputStream("$cachePath/image.png")
        androidBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()
    } catch (e: Exception) {
        logger.d { "${e.message}" }
    }
    val imagePath = File(context?.cacheDir, "images")
    val newFile = File(imagePath, "image.png")
    return FileProvider.getUriForFile(
        context!!,
        "${context.packageName}.fileprovider",
        newFile,
    )
}
