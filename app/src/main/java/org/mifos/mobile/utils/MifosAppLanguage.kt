package org.mifos.mobile.utils

enum class MifosAppLanguage(val code: String, val displayName: String) {
    SYSTEM_LANGUAGE("System_Language", "System Language"),
    ENGLISH("en", "English"),
    HINDI("hi", "हिंदी"),
    ARABIC("ar", "عربى"),
    URDU("ur", "اُردُو"),
    BENGALI("bn", "বাঙালি"),
    SPANISH("es", "Español"),
    FRENCH("fr", "français"),
    INDONESIAN("in", "bahasa Indonesia"),
    KHMER("km", "ភាសាខ្មែរ"),
    KANNADA("kn", "ಕನ್ನಡ"),
    TELUGU("te", "తెలుగు"),
    BURMESE("my", "မြန်မာ"),
    POLISH("pl", "Polski"),
    PORTUGUESE("pt", "Português"),
    RUSSIAN("ru", "русский"),
    SWAHILI("sw", "Kiswahili"),
    FARSI("fa", "فارسی");

    companion object {
        fun fromCode(code: String): MifosAppLanguage {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }
    }

    override fun toString(): String {
        return displayName
    }
}
