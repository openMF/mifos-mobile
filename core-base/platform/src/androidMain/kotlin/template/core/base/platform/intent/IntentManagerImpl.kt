/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.platform.intent

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import template.core.base.platform.model.MimeType
import template.core.base.platform.utils.isBuildVersionBelow
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Android-specific implementation of the IntentManager interface.
 *
 * This class provides concrete implementations of intent operations using Android's Intent system.
 * It handles activities, URI navigation, content sharing, file operations, and system settings
 * navigation using the standard Android Intent mechanisms.
 *
 * @property context The Android Context used to start activities and access system services
 */
class IntentManagerImpl(
    private val context: Context,
) : IntentManager {
    /**
     * Starts an Android activity with the provided intent.
     *
     * This method attempts to start an activity with the provided Intent object. If the activity
     * cannot be found (ActivityNotFoundException), the exception is caught and ignored to prevent
     * application crashes.
     *
     * @param intent The Android Intent object to be processed, cast from Any
     */
    override fun startActivity(intent: Any) {
        try {
            Log.d("IntentManagerImpl", "Starting activity: $intent")
            context.startActivity(intent as Intent)
        } catch (_: ActivityNotFoundException) {
            // no-op
        }
    }

    /**
     * Opens the specified URI in an appropriate application.
     *
     * This method supports both standard URIs (http, https, etc.) and custom "androidapp://"
     * URIs for launching applications from the Play Store. For standard URIs without a scheme,
     * HTTPS is automatically applied as the default scheme.
     *
     * The method handles:
     * - App store URIs (androidapp://) to open Play Store or launch the app directly (on Android 13+)
     * - Standard web and deep link URIs using ACTION_VIEW intent
     *
     * @param uri The URI string to be opened
     */
    override fun launchUri(uri: String) {
        Log.d("IntentManagerImpl", "Launching URI: $uri")
        val androidUri = uri.toUri()
        if (androidUri.scheme.equals(other = "androidapp", ignoreCase = true)) {
            val packageName = androidUri.toString().removePrefix(prefix = "androidapp://")
            if (isBuildVersionBelow(Build.VERSION_CODES.TIRAMISU)) {
                startActivity(createPlayStoreIntent(packageName))
            } else {
                try {
                    context
                        .packageManager
                        .getLaunchIntentSenderForPackage(packageName)
                        .sendIntent(context, Activity.RESULT_OK, null, null, null)
                } catch (_: IntentSender.SendIntentException) {
                    startActivity(createPlayStoreIntent(packageName))
                }
            }
        } else {
            val newUri = if (androidUri.scheme == null) {
                androidUri.buildUpon().scheme("https").build()
            } else {
                androidUri.normalizeScheme()
            }
            startActivity(Intent(Intent.ACTION_VIEW, newUri))
        }
    }

    /**
     * Shares text content with other applications via Android's share sheet.
     *
     * This method creates an ACTION_SEND intent with the provided text and displays
     * the Android system share chooser to allow the user to select a target application.
     *
     * @param text The text content to be shared
     */
    override fun shareText(text: String) {
        val sendIntent: Intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, null))
    }

    /**
     * Shares a file with other applications via Android's share sheet.
     *
     * This method creates an ACTION_SEND intent with the provided file URI and displays
     * the Android system share chooser. It automatically adds promotional text about the app
     * to the shared content.
     *
     * @param fileUri The URI string pointing to the file to be shared
     * @param mimeType The MIME type of the file to help receiving applications handle it properly
     */
    override fun shareFile(fileUri: String, mimeType: MimeType) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            // Add file URI directly
            putExtra(Intent.EXTRA_STREAM, fileUri.toUri())

            // Add promotional text
            putExtra(
                Intent.EXTRA_TEXT,
                "*Downloaded using our awesome app!* \n\n" +
                    "*Download now from the Play Store!* \n" +
                    "https://play.google.com/store/apps/details?id=${context.packageName}",
            )

            // Grant read permission to receiving app
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            type = mimeType.value
        }

        // Create chooser to let user pick sharing app
        val chooserIntent = Intent.createChooser(
            shareIntent,
            "Share your media",
        )

        startActivity(chooserIntent)
    }

    /**
     * Shares a file with other applications along with custom text content.
     *
     * This method creates an ACTION_SEND intent with the provided file URI and custom text,
     * then displays the Android system share chooser.
     *
     * @param fileUri The URI string pointing to the file to be shared
     * @param mimeType The MIME type of the file to help receiving applications handle it properly
     * @param extraText Additional text to include with the shared file
     */
    override fun shareFile(fileUri: String, mimeType: MimeType, extraText: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            // Add file URI directly
            putExtra(Intent.EXTRA_STREAM, fileUri.toUri())

            // Add promotional text
            putExtra(Intent.EXTRA_TEXT, extraText)

            // Grant read permission to receiving app
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            type = mimeType.value
        }

        // Create chooser to let user pick sharing app
        val chooserIntent = Intent.createChooser(
            shareIntent,
            "Share your media",
        )

        startActivity(chooserIntent)
    }

    /**
     * Shares an ImageBitmap with other applications.
     *
     * This method converts the provided ImageBitmap to an Android Bitmap, saves it to a
     * temporary file in the cache directory, and shares it using an ACTION_SEND intent.
     * FileProvider is used to generate a content:// URI for the saved image.
     *
     * @param title The title to be used in the share dialog
     * @param image The ImageBitmap to be shared
     */
    override suspend fun shareImage(title: String, image: ImageBitmap) {
        val uri = saveImage(image.asAndroidBitmap(), context)

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            setDataAndType(uri, "image/png")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val shareIntent = Intent.createChooser(sendIntent, title)
        startActivity(shareIntent)
    }

    /**
     * Saves a Bitmap to a temporary file in the application's cache directory.
     *
     * This private helper method handles the IO operations to save an image to the
     * cache directory and generate a content:// URI using FileProvider.
     *
     * @param image The Android Bitmap to save
     * @param context The Android Context used to access the cache directory
     * @return A content:// URI for the saved image or null if an error occurred
     */
    private suspend fun saveImage(image: Bitmap, context: Context): Uri? {
        return withContext(Dispatchers.IO) {
            try {
                val imagesFolder = File(context.cacheDir, "images")
                imagesFolder.mkdirs()
                val file = File(imagesFolder, "shared_image.png")

                val stream = FileOutputStream(file)
                image.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.flush()
                stream.close()

                FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
            } catch (e: IOException) {
                Log.d("saving bitmap", "saving bitmap error ${e.message}")
                null
            }
        }
    }

    /**
     * Creates an intent for document creation using Android's document provider system.
     *
     * This method generates an ACTION_CREATE_DOCUMENT intent that will prompt the user
     * to choose a location and filename for a new document. The MIME type is automatically
     * detected from the file extension when possible.
     *
     * @param fileName The suggested name for the document to be created
     * @return An Android Intent configured for document creation
     */
    override fun createDocumentIntent(fileName: String): Any {
        return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            // Attempt to get the MIME type from the file extension
            val extension = MimeTypeMap.getFileExtensionFromUrl(fileName)
            type = extension?.let {
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(it)
            } ?: "*/*"

            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_TITLE, fileName)
        }
    }

    /**
     * Opens the application's details page in the system settings.
     *
     * This method creates and launches an intent to navigate to the current application's
     * details page in the system settings, where users can manage permissions, notifications,
     * and other app-specific settings.
     */
    override fun startApplicationDetailsSettingsActivity() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = ("package:" + context.packageName).toUri()
        startActivity(intent = intent)
    }

    /**
     * Creates an intent to open the Google Play Store for a specific application.
     *
     * This private helper method generates an ACTION_VIEW intent targeting the
     * Google Play Store with the specified package name.
     *
     * @param packageName The package name of the application to view in Play Store
     * @return An Android Intent configured to open the Play Store
     */
    private fun createPlayStoreIntent(packageName: String): Intent {
        val playStoreUri = "https://play.google.com/store/apps/details"
            .toUri()
            .buildUpon()
            .appendQueryParameter("id", packageName)
            .build()
        return Intent(Intent.ACTION_VIEW, playStoreUri)
    }
}
