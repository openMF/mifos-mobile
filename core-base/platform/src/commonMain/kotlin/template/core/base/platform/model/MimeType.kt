/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.platform.model

/**
 * Represents standardized MIME (Multipurpose Internet Mail Extensions) types for various file formats.
 *
 * This enum provides a structured representation of common MIME types organized by categories
 * (images, videos, audio, documents, archives) with their corresponding string values and file extensions.
 * It offers utility methods to determine the appropriate MIME type from file extensions or filenames.
 *
 * @property value The standard MIME type string representation (e.g., "image/jpeg")
 * @property extensions The file extensions associated with this MIME type (e.g., "jpg", "jpeg")
 */
enum class MimeType(val value: String, vararg val extensions: String) {
    /**
     * JPEG image format - "image/jpeg"
     * Extensions: jpg, jpeg
     */
    IMAGE_JPEG("image/jpeg", "jpg", "jpeg"),

    /**
     * PNG image format - "image/png"
     * Extension: png
     */
    IMAGE_PNG("image/png", "png"),

    /**
     * GIF image format - "image/gif"
     * Extension: gif
     */
    IMAGE_GIF("image/gif", "gif"),

    /**
     * WebP image format - "image/webp"
     * Extension: webp
     */
    IMAGE_WEBP("image/webp", "webp"),

    /**
     * BMP image format - "image/bmp"
     * Extension: bmp
     */
    IMAGE_BMP("image/bmp", "bmp"),

    /**
     * SVG image format - "image/svg+xml"
     * Extension: svg
     */
    IMAGE_SVG("image/svg+xml", "svg"),

    /**
     * MP4 video format - "video/mp4"
     * Extension: mp4
     */
    VIDEO_MP4("video/mp4", "mp4"),

    /**
     * WebM video format - "video/webm"
     * Extension: webm
     */
    VIDEO_WEBM("video/webm", "webm"),

    /**
     * Matroska video format - "video/x-matroska"
     * Extension: mkv
     */
    VIDEO_MKV("video/x-matroska", "mkv"),

    /**
     * AVI video format - "video/x-msvideo"
     * Extension: avi
     */
    VIDEO_AVI("video/x-msvideo", "avi"),

    /**
     * QuickTime video format - "video/quicktime"
     * Extension: mov
     */
    VIDEO_MOV("video/quicktime", "mov"),

    /**
     * MP3 audio format - "audio/mpeg"
     * Extension: mp3
     */
    AUDIO_MPEG("audio/mpeg", "mp3"),

    /**
     * WAV audio format - "audio/wav"
     * Extension: wav
     */
    AUDIO_WAV("audio/wav", "wav"),

    /**
     * OGG audio format - "audio/ogg"
     * Extension: ogg
     */
    AUDIO_OGG("audio/ogg", "ogg"),

    /**
     * M4A audio format - "audio/mp4"
     * Extension: m4a
     */
    AUDIO_M4A("audio/mp4", "m4a"),

    /**
     * FLAC audio format - "audio/flac"
     * Extension: flac
     */
    AUDIO_FLAC("audio/flac", "flac"),

    /**
     * PDF document format - "application/pdf"
     * Extension: pdf
     */
    APPLICATION_PDF("application/pdf", "pdf"),

    /**
     * Microsoft Word document format - "application/msword"
     * Extension: doc
     */
    APPLICATION_DOC("application/msword", "doc"),

    /**
     * Microsoft Word Open XML document format
     * Extension: docx
     */
    APPLICATION_DOCX(
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "docx",
    ),

    /**
     * Microsoft Excel spreadsheet format - "application/vnd.ms-excel"
     * Extension: xls
     */
    APPLICATION_XLS("application/vnd.ms-excel", "xls"),

    /**
     * Microsoft Excel Open XML spreadsheet format
     * Extension: xlsx
     */
    APPLICATION_XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx"),

    /**
     * Microsoft PowerPoint presentation format - "application/vnd.ms-powerpoint"
     * Extension: ppt
     */
    APPLICATION_PPT("application/vnd.ms-powerpoint", "ppt"),

    /**
     * Microsoft PowerPoint Open XML presentation format
     * Extension: pptx
     */
    APPLICATION_PPTX(
        "application/vnd.openxmlformats-officedocument.presentationml.presentation",
        "pptx",
    ),

    /**
     * Plain text format - "text/plain"
     * Extension: txt
     */
    TEXT_PLAIN("text/plain", "txt"),

    /**
     * ZIP archive format - "application/zip"
     * Extension: zip
     */
    APPLICATION_ZIP("application/zip", "zip"),

    /**
     * RAR archive format - "application/x-rar-compressed"
     * Extension: rar
     */
    APPLICATION_RAR("application/x-rar-compressed", "rar"),

    /**
     * 7-Zip archive format - "application/x-7z-compressed"
     * Extension: 7z
     */
    APPLICATION_7Z("application/x-7z-compressed", "7z"),

    /**
     * Default MIME type for unknown file formats - "application/octet-stream"
     * Used when the specific MIME type cannot be determined.
     */
    UNKNOWN("application/octet-stream"),
    ;

    /**
     * Contains utility methods for working with MIME types, including
     * lookups by file extension and filename.
     */
    companion object {
        // Map to store file extensions and their corresponding MimeType
        private val extensionToMimeType = mutableMapOf<String, MimeType>()

        init {
            // Populate the map with extensions and their corresponding MimeType
            entries.forEach { mimeType ->
                mimeType.extensions.forEach { extension ->
                    extensionToMimeType[extension] = mimeType
                }
            }
        }

        /**
         * Returns the MimeType corresponding to the given file extension.
         * If the extension is not found, returns UNKNOWN.
         *
         * @param extension The file extension to look up (without the leading dot)
         * @return The corresponding MimeType or UNKNOWN if not found
         */
        fun fromExtension(extension: String): MimeType = extensionToMimeType[extension.lowercase()] ?: UNKNOWN

        /**
         * Returns the MimeType corresponding to the given file name.
         * If the file name has no extension or the extension is not found, returns UNKNOWN.
         *
         * @param fileName The complete filename including its extension
         * @return The corresponding MimeType or UNKNOWN if the extension is not recognized
         */
        fun fromFileName(fileName: String): MimeType {
            val extension = fileName.substringAfterLast(
                delimiter = '.',
                missingDelimiterValue = "",
            ).lowercase()
            return fromExtension(extension)
        }
    }
}
