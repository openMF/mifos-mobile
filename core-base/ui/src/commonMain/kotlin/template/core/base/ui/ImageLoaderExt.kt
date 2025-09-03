/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.ui

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

/**
 * CompositionLocal that provides access to an [ImageLoader] within the composition hierarchy.
 * Default value is null, requiring an explicit provider or fallback to default implementation.
 */
internal val LocalAppImageLoader = compositionLocalOf<ImageLoader?> { null }

/**
 * Returns an [ImageLoader] from the current composition or creates a default one if none is available.
 *
 * @param context Platform context required for image loading
 * @return An [ImageLoader] instance that can be used for image loading operations
 */
@Composable
fun rememberImageLoader(context: PlatformContext): ImageLoader {
    return LocalAppImageLoader.current ?: rememberDefaultImageLoader(context)
}

/**
 * Creates and remembers a default [ImageLoader] with memory cache and debug logging.
 *
 * @param context Platform context required for image loading and memory calculations
 * @return A default configured [ImageLoader] instance
 */
@Composable
internal fun rememberDefaultImageLoader(context: PlatformContext): ImageLoader {
    return remember(context) {
        ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, 0.25)
                    .build()
            }
            .logger(DebugLogger())
            .components {
                addPlatformFileSupport()
            }
            .build()
    }
}

/**
 * Creates and remembers an [ImageRequest] for loading the specified wallpaper.
 *
 * @param context Platform context required for the image request
 * @param wallpaper String identifier for the wallpaper to be loaded
 * @return An [ImageRequest] configured for the specified wallpaper
 */
@Composable
fun rememberImageRequest(
    context: PlatformContext,
    wallpaper: String,
): ImageRequest {
    return remember(wallpaper) {
        ImageRequest.Builder(context)
            .data(wallpaper)
            .memoryCacheKey(wallpaper)
            .placeholderMemoryCacheKey(wallpaper)
            .build()
    }
}

/**
 * Provides the specified [ImageLoader] to all composables within the [content] lambda
 * via [CompositionLocalProvider].
 *
 * @param imageLoader The [ImageLoader] to provide downstream
 * @param content The composable content that will have access to the provided [ImageLoader]
 */
@Composable
fun LocalImageLoaderProvider(imageLoader: ImageLoader, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalAppImageLoader provides imageLoader) {
        content()
    }
}
