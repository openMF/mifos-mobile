/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model

enum class LanguageConfig(
    val localName: String?,
    val languageName: String,
) {
    DEFAULT(
        localName = null,
        languageName = "System (Default)",
    ),
    ENGLISH(
        localName = "en",
        languageName = "English (English)",
    ),
    TELUGU(
        localName = "te",
        languageName = "Telugu (తెలుగు)",
    ),
    HINDI(
        localName = "hi",
        languageName = "Hindi (हिंदी)",
    ),
    ARABIC(
        localName = "ar",
        languageName = "Arabic (عربى)",
    ),
    URDU(
        localName = "ur",
        languageName = "Urdu (اُردُو)",
    ),
    BENGALI(
        localName = "bn",
        languageName = "Bengali (বাঙালি)",
    ),
    SPANISH(
        localName = "es",
        languageName = "Spanish (Español)",
    ),
    FRENCH(
        localName = "fr",
        languageName = "French (français)",
    ),
    INDONESIAN(
        localName = "id",
        languageName = "Indonesian (bahasa Indonesia)",
    ),
    KHMER(
        localName = "km",
        languageName = "Khmer (ភាសាខ្មែរ)",
    ),
    KANNADA(
        localName = "kn",
        languageName = "Kannada (ಕನ್ನಡ)",
    ),
    BURMESE(
        localName = "my",
        languageName = "Burmese (မြန်မာ)",
    ),
    POLISH(
        localName = "pl",
        languageName = "Polish (Polski)",
    ),
    PORTUGUESE(
        localName = "pt",
        languageName = "Portuguese (Português)",
    ),
    RUSSIAN(
        localName = "ru",
        languageName = "Russian (русский)",
    ),
    SWAHILI(
        localName = "sw",
        languageName = "Swahili (Kiswahili)",
    ),
    FARSI(
        localName = "fa",
        languageName = "Farsi (فارسی)",
    ),
    ;

    companion object {
        fun fromString(value: String): LanguageConfig {
            return entries.find { it.languageName.equals(value, ignoreCase = true) } ?: DEFAULT
        }
    }
}
