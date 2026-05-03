/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.LocalPlatformContext
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.util.DebugLogger
import io.github.vinceglb.filekit.coil.addPlatformFileSupport

/**
 * A CompositionLocal instance used to provide a shared [ImageLoader] across the composable hierarchy.
 * This is useful for performing image loading and caching within Jetpack Compose UIs.
 *
 * If no [ImageLoader] is provided within the context, an error will be thrown when accessing this CompositionLocal.
 */
internal val LocalAppImageLoader = compositionLocalOf<ImageLoader> {
    error("No ImageLoader provided. Have you forgotten the LocalImageLoaderProvider?")
}

/**
 * Creates and remembers an instance of the default ImageLoader configured with platform-specific
 * settings for caching and logging. The ImageLoader is initialized using the current platform context.
 *
 * @return A remembered instance of the default ImageLoader configured with common settings.
 */
@Composable
fun rememberImageLoader(): ImageLoader {
    val context = LocalPlatformContext.current
    return remember(context) { getDefaultImageLoader(context) }
}

/**
 * Creates and returns a default instance of an ImageLoader configured with common settings such as
 * logging, caching policies, memory caching, and platform-specific component support.
 *
 * @param context The platform-specific context required to initialize the ImageLoader
 * @return A configured ImageLoader instance ready for use
 */
fun getDefaultImageLoader(context: PlatformContext): ImageLoader = ImageLoader
    .Builder(context)
    .logger(DebugLogger())
    .networkCachePolicy(CachePolicy.ENABLED)
    .memoryCachePolicy(CachePolicy.ENABLED)
    .memoryCache {
        MemoryCache.Builder()
            .maxSizePercent(context, 0.25)
            .build()
    }
    .components {
        addPlatformFileSupport()
    }.build()

/**
 * Provides a composable local context for an `ImageLoader` to be used within a Composable hierarchy.
 *
 * @param imageLoader The `ImageLoader` instance to be provided to the local composition.
 * @param content A composable lambda function representing the UI content that can access the provided `ImageLoader`.
 */
@Composable
fun LocalImageLoaderProvider(imageLoader: ImageLoader, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalAppImageLoader provides imageLoader) {
        content()
    }
}
