/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.qr

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.attafitamim.krop.core.images.ImageSrc
import com.attafitamim.krop.core.images.toImageSrc
import kotlinx.coroutines.launch

interface ImagePicker {
    /** Pick an image with [mimetype] */
    fun pick(mimetype: String = "image/*")
}

@Composable
fun rememberImagePicker(
    onImage: (uri: ImageSrc) -> Unit,
): ImagePicker {
    val context = LocalContext.current
    val contract = remember { ActivityResultContracts.GetContent() }
    val coroutineScope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = contract,
        onResult = { uri ->
            coroutineScope.launch {
                val imageSrc = uri?.toImageSrc(context) ?: return@launch
                onImage(imageSrc)
            }
        },
    )

    return remember {
        object : ImagePicker {
            override fun pick(mimetype: String) = launcher.launch(mimetype)
        }
    }
}
