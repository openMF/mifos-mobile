/*
 * Copyright 2025 Mobile Byte Sensei
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/mobilebytesensei/financiera-bienestar/blob/dev/LICENSE
 */
package org.mifos.mobile.core.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.memory.MemoryCache
import coil3.request.ImageRequest
import coil3.util.DebugLogger
import io.github.vinceglb.filekit.coil.addPlatformFileSupport

internal val LocalAppImageLoader = compositionLocalOf<ImageLoader?> { null }

@Composable
fun rememberImageLoader(context: PlatformContext): ImageLoader {
    return LocalAppImageLoader.current ?: rememberDefaultImageLoader(context)
}

@Composable
internal fun rememberDefaultImageLoader(context: PlatformContext): ImageLoader {
    return remember(context) {
        ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, 0.25)
                    .build()
            }
            .components {
                addPlatformFileSupport()
            }
            .logger(DebugLogger())
            .build()
    }
}
