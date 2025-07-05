/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.utils

import mifos_mobile.core.ui.generated.resources.Res
import mifos_mobile.core.ui.generated.resources.validation_amount_empty
import mifos_mobile.core.ui.generated.resources.validation_amount_invalid_decimal_places
import mifos_mobile.core.ui.generated.resources.validation_amount_invalid_format
import mifos_mobile.core.ui.generated.resources.validation_amount_invalid_number
import mifos_mobile.core.ui.generated.resources.validation_amount_negative
import mifos_mobile.core.ui.generated.resources.validation_amount_too_large
import mifos_mobile.core.ui.generated.resources.validation_amount_too_small
import mifos_mobile.core.ui.generated.resources.validation_email_domain_empty
import mifos_mobile.core.ui.generated.resources.validation_email_domain_insufficient_parts
import mifos_mobile.core.ui.generated.resources.validation_email_domain_max_length
import mifos_mobile.core.ui.generated.resources.validation_email_domain_missing_dot
import mifos_mobile.core.ui.generated.resources.validation_email_domain_starts_ends_with_dot
import mifos_mobile.core.ui.generated.resources.validation_email_domain_starts_ends_with_hyphen
import mifos_mobile.core.ui.generated.resources.validation_email_empty
import mifos_mobile.core.ui.generated.resources.validation_email_ends_with_at
import mifos_mobile.core.ui.generated.resources.validation_email_label_empty
import mifos_mobile.core.ui.generated.resources.validation_email_label_ends_with_hyphen
import mifos_mobile.core.ui.generated.resources.validation_email_label_invalid_characters
import mifos_mobile.core.ui.generated.resources.validation_email_label_max_length
import mifos_mobile.core.ui.generated.resources.validation_email_label_starts_with_hyphen
import mifos_mobile.core.ui.generated.resources.validation_email_local_consecutive_dots
import mifos_mobile.core.ui.generated.resources.validation_email_local_empty
import mifos_mobile.core.ui.generated.resources.validation_email_local_ends_with_dot
import mifos_mobile.core.ui.generated.resources.validation_email_local_invalid_characters
import mifos_mobile.core.ui.generated.resources.validation_email_local_max_length
import mifos_mobile.core.ui.generated.resources.validation_email_local_starts_with_dot
import mifos_mobile.core.ui.generated.resources.validation_email_max_length_exceeded
import mifos_mobile.core.ui.generated.resources.validation_email_missing_at_symbol
import mifos_mobile.core.ui.generated.resources.validation_email_multiple_at_symbols
import mifos_mobile.core.ui.generated.resources.validation_email_starts_with_at
import mifos_mobile.core.ui.generated.resources.validation_email_tld_invalid
import mifos_mobile.core.ui.generated.resources.validation_phone_empty
import mifos_mobile.core.ui.generated.resources.validation_phone_invalid_characters
import mifos_mobile.core.ui.generated.resources.validation_phone_invalid_for_country
import mifos_mobile.core.ui.generated.resources.validation_phone_no_valid_country_found
import mifos_mobile.core.ui.generated.resources.validation_phone_too_long
import mifos_mobile.core.ui.generated.resources.validation_phone_too_short
import org.jetbrains.compose.resources.StringResource
import org.mifos.mobile.core.model.Country
import org.mifos.mobile.core.model.entity.Currency
import org.mifos.mobile.core.model.worldCountries
import kotlin.math.round

@Suppress("ReturnCount", "CyclomaticComplexMethod", "MaxLineLength", "TooManyFunctions")
object ValidationHelper {

    private const val MIN_AMOUNT = 0.01
    private const val MAX_AMOUNT = 999999999.99

    fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false

        val trimmedEmail = email.trim()

        // RFC 5322 maximum email length
        if (trimmedEmail.length > 254) return false

        // Must contain exactly one @ symbol
        val atIndex = trimmedEmail.indexOf('@')
        if (atIndex == -1 || trimmedEmail.lastIndexOf('@') != atIndex) return false

        val localPart = trimmedEmail.substring(0, atIndex)
        val domainPart = trimmedEmail.substring(atIndex + 1)

        return isValidEmailLocalPart(localPart) && isValidEmailDomainPart(domainPart)
    }

    private fun isValidEmailLocalPart(localPart: String): Boolean {
        if (localPart.isEmpty() || localPart.length > 64) return false
        if (localPart.startsWith('.') || localPart.endsWith('.')) return false
        if (localPart.contains("..")) return false

        for (char in localPart) {
            if (!isValidLocalPartCharacter(char)) return false
        }
        return true
    }

    private fun isValidLocalPartCharacter(char: Char): Boolean {
        return when {
            char.isLetterOrDigit() -> true
            char in ".!#$%&'*+/=?^_`{|}~-" -> true
            else -> false
        }
    }

    @Suppress("ComplexCondition")
    private fun isValidEmailDomainPart(domainPart: String): Boolean {
        if (domainPart.isEmpty() || domainPart.length > 253) return false
        if (!domainPart.contains('.')) return false
        if (domainPart.startsWith('.') || domainPart.endsWith('.') ||
            domainPart.startsWith('-') || domainPart.endsWith('-')
        ) {
            return false
        }

        val labels = domainPart.split('.')
        if (labels.size < 2) return false

        for (label in labels) {
            if (!isValidDomainLabel(label)) return false
        }

        val tld = labels.last()
        return isValidTopLevelDomain(tld)
    }

    private fun isValidDomainLabel(label: String): Boolean {
        if (label.isEmpty() || label.length > 63) return false
        if (label.startsWith('-') || label.endsWith('-')) return false

        for (char in label) {
            if (!char.isLetterOrDigit() && char != '-') return false
        }
        return true
    }

    private fun isValidTopLevelDomain(tld: String): Boolean {
        if (tld.length < 2) return false
        for (char in tld) {
            if (!char.isLetter()) return false
        }
        return true
    }

    fun validateEmailWithDetails(email: String): EmailValidationResult {
        if (email.isBlank()) {
            return EmailValidationResult.Invalid(Res.string.validation_email_empty)
        }

        val trimmedEmail = email.trim()

        if (trimmedEmail.length > 254) {
            return EmailValidationResult.Invalid(Res.string.validation_email_max_length_exceeded)
        }

        val atIndex = trimmedEmail.indexOf('@')
        when {
            atIndex == -1 -> return EmailValidationResult.Invalid(Res.string.validation_email_missing_at_symbol)
            atIndex == 0 -> return EmailValidationResult.Invalid(Res.string.validation_email_starts_with_at)
            atIndex == trimmedEmail.length - 1 -> return EmailValidationResult.Invalid(Res.string.validation_email_ends_with_at)
            trimmedEmail.lastIndexOf('@') != atIndex -> return EmailValidationResult.Invalid(Res.string.validation_email_multiple_at_symbols)
        }

        val localPart = trimmedEmail.substring(0, atIndex)
        val domainPart = trimmedEmail.substring(atIndex + 1)

        when (val localResult = validateEmailLocalPartWithDetails(localPart)) {
            is EmailPartValidationResult.Invalid -> return EmailValidationResult.Invalid(localResult.errorResource)
            is EmailPartValidationResult.Valid -> {
                /* Continue validation */
            }
        }

        when (val domainResult = validateEmailDomainPartWithDetails(domainPart)) {
            is EmailPartValidationResult.Invalid -> return EmailValidationResult.Invalid(
                domainResult.errorResource,
            )

            is EmailPartValidationResult.Valid -> {
                /* Continue validation */
            }
        }

        return EmailValidationResult.Valid(trimmedEmail.lowercase())
    }

    private fun validateEmailLocalPartWithDetails(localPart: String): EmailPartValidationResult {
        return when {
            localPart.isEmpty() -> {
                EmailPartValidationResult.Invalid(Res.string.validation_email_local_empty)
            }

            localPart.length > 64 -> {
                EmailPartValidationResult.Invalid(Res.string.validation_email_local_max_length)
            }

            localPart.startsWith('.') -> {
                EmailPartValidationResult.Invalid(Res.string.validation_email_local_starts_with_dot)
            }

            localPart.endsWith('.') -> {
                EmailPartValidationResult.Invalid(Res.string.validation_email_local_ends_with_dot)
            }

            localPart.contains("..") -> {
                EmailPartValidationResult.Invalid(Res.string.validation_email_local_consecutive_dots)
            }

            !localPart.all { isValidLocalPartCharacter(it) } -> {
                EmailPartValidationResult.Invalid(
                    Res.string.validation_email_local_invalid_characters,
                )
            }

            else -> {
                EmailPartValidationResult.Valid
            }
        }
    }

    private fun validateEmailDomainPartWithDetails(domainPart: String): EmailPartValidationResult {
        if (domainPart.isEmpty()) {
            return EmailPartValidationResult.Invalid(Res.string.validation_email_domain_empty)
        }

        if (domainPart.length > 253) {
            return EmailPartValidationResult.Invalid(Res.string.validation_email_domain_max_length)
        }

        if (!domainPart.contains('.')) {
            return EmailPartValidationResult.Invalid(Res.string.validation_email_domain_missing_dot)
        }

        if (domainPart.startsWith('.') || domainPart.endsWith('.')) {
            return EmailPartValidationResult.Invalid(Res.string.validation_email_domain_starts_ends_with_dot)
        }

        if (domainPart.startsWith('-') || domainPart.endsWith('-')) {
            return EmailPartValidationResult.Invalid(Res.string.validation_email_domain_starts_ends_with_hyphen)
        }

        val labels = domainPart.split('.')
        if (labels.size < 2) {
            return EmailPartValidationResult.Invalid(Res.string.validation_email_domain_insufficient_parts)
        }

        for (label in labels) {
            when (val labelResult = validateDomainLabelWithDetails(label)) {
                is EmailPartValidationResult.Invalid -> return labelResult
                is EmailPartValidationResult.Valid -> {
                    /* Continue */
                }
            }
        }

        val tld = labels.last()
        if (!isValidTopLevelDomain(tld)) {
            return EmailPartValidationResult.Invalid(Res.string.validation_email_tld_invalid)
        }

        return EmailPartValidationResult.Valid
    }

    private fun validateDomainLabelWithDetails(label: String): EmailPartValidationResult {
        return when {
            label.isEmpty() -> {
                EmailPartValidationResult.Invalid(Res.string.validation_email_label_empty)
            }

            label.length > 63 -> {
                EmailPartValidationResult.Invalid(Res.string.validation_email_label_max_length)
            }

            label.startsWith('-') -> {
                EmailPartValidationResult.Invalid(Res.string.validation_email_label_starts_with_hyphen)
            }

            label.endsWith('-') -> {
                EmailPartValidationResult.Invalid(Res.string.validation_email_label_ends_with_hyphen)
            }

            !label.all { it.isLetterOrDigit() || it == '-' } -> {
                EmailPartValidationResult.Invalid(
                    Res.string.validation_email_label_invalid_characters,
                )
            }

            else -> {
                EmailPartValidationResult.Valid
            }
        }
    }

    fun isValidName(name: String): Boolean {
        if (name.isBlank()) return false
        val trimmedName = name.trim()

        for (char in trimmedName) {
            if (!isValidNameCharacter(char)) return false
        }
        return true
    }

    private fun isValidNameCharacter(char: Char): Boolean {
        return when {
            char.isLetter() -> true
            char.isWhitespace() -> true
            char in "'-" -> true
            char.code in 0x00C0..0x017F -> true
            char.code in 0x0100..0x024F -> true
            else -> false
        }
    }

    fun isValidPhoneNumber(phoneNumber: String, countryCode: String? = null): Boolean {
        if (phoneNumber.isBlank()) return false
        val cleanedNumber = cleanPhoneNumber(phoneNumber)

        return if (countryCode != null) {
            val country = findCountryByCode(countryCode)
            country?.let { isValidPhoneForCountry(cleanedNumber, it) } ?: false
        } else {
            // Try to validate against all countries
            worldCountries.any { isValidPhoneForCountry(cleanedNumber, it) }
        }
    }

    fun validatePhoneNumberWithDetails(phoneNumber: String, countryCode: String? = null): PhoneValidationResult {
        if (phoneNumber.isBlank()) {
            return PhoneValidationResult.Invalid(Res.string.validation_phone_empty)
        }

        val cleanedNumber = cleanPhoneNumber(phoneNumber)

        if (cleanedNumber.length < 7) {
            return PhoneValidationResult.Invalid(Res.string.validation_phone_too_short)
        }

        if (cleanedNumber.length > 15) {
            return PhoneValidationResult.Invalid(Res.string.validation_phone_too_long)
        }

        if (!cleanedNumber.all { it.isDigit() || it == '+' }) {
            return PhoneValidationResult.Invalid(Res.string.validation_phone_invalid_characters)
        }

        val targetCountries = if (countryCode != null) {
            listOfNotNull(findCountryByCode(countryCode))
        } else {
            worldCountries
        }

        for (country in targetCountries) {
            val phoneType = getPhoneTypeForCountry(cleanedNumber, country)
            if (phoneType != PhoneType.INVALID) {
                return PhoneValidationResult.Valid(
                    formattedNumber = formatPhoneNumber(cleanedNumber, country),
                    type = phoneType,
                    country = country,
                )
            }
        }

        return if (countryCode != null) {
            PhoneValidationResult.Invalid(Res.string.validation_phone_invalid_for_country)
        } else {
            PhoneValidationResult.Invalid(Res.string.validation_phone_no_valid_country_found)
        }
    }

    private fun isValidPhoneForCountry(phoneNumber: String, country: Country): Boolean {
        val mobileRegex = Regex(country.mobilePattern)
        val landlineRegex = country.landlinePattern?.let { Regex(it) }
        val specialRegex = country.specialPattern?.let { Regex(it) }

        return mobileRegex.matches(phoneNumber) ||
            landlineRegex?.matches(phoneNumber) == true ||
            specialRegex?.matches(phoneNumber) == true
    }

    private fun getPhoneTypeForCountry(phoneNumber: String, country: Country): PhoneType {
        val mobileRegex = Regex(country.mobilePattern)
        val landlineRegex = country.landlinePattern?.let { Regex(it) }
        val specialRegex = country.specialPattern?.let { Regex(it) }

        return when {
            mobileRegex.matches(phoneNumber) -> PhoneType.MOBILE
            landlineRegex?.matches(phoneNumber) == true -> PhoneType.LANDLINE
            specialRegex?.matches(phoneNumber) == true -> PhoneType.SPECIAL
            else -> PhoneType.INVALID
        }
    }

    private fun formatPhoneNumber(phoneNumber: String, country: Country): String {
        val cleanedNumber = cleanPhoneNumber(phoneNumber)

        if (!isValidPhoneForCountry(cleanedNumber, country)) {
            return phoneNumber
        }

        // Extract the number part after country code
        val numberPart = when {
            cleanedNumber.startsWith(country.phoneCode) ->
                cleanedNumber.substring(country.phoneCode.length)

            cleanedNumber.startsWith("00${country.phoneCode.substring(1)}") ->
                cleanedNumber.substring("00${country.phoneCode.substring(1)}".length)

            cleanedNumber.startsWith(country.phoneCode.substring(1)) ->
                cleanedNumber.substring(country.phoneCode.substring(1).length)

            else -> cleanedNumber
        }

        // Basic formatting - can be enhanced per country
        return "${country.phoneCode} ${formatNumberPart(numberPart, country)}"
    }

    private fun formatNumberPart(numberPart: String, country: Country): String {
        // Basic formatting logic - can be customized per country
        return when (country.code) {
            "ES" -> "${numberPart.substring(0, 3)} ${numberPart.substring(3, 6)} ${numberPart.substring(6)}"
            "US", "CA" -> "${numberPart.substring(0, 3)} ${numberPart.substring(3, 6)} ${numberPart.substring(6)}"
            "GB" -> "${numberPart.substring(0, 4)} ${numberPart.substring(4)}"
            "FR" -> "${numberPart.substring(0, 1)} ${numberPart.substring(1, 3)} ${
                numberPart.substring(
                    3,
                    5,
                )
            } ${numberPart.substring(5, 7)} ${numberPart.substring(7)}"

            else -> numberPart.chunked(3).joinToString(" ")
        }
    }

    private fun cleanPhoneNumber(phoneNumber: String): String {
        return phoneNumber.filter { it.isDigit() || it == '+' }
    }

    private fun findCountryByCode(countryCode: String): Country? {
        return worldCountries.find { it.code.equals(countryCode, ignoreCase = true) }
    }

    fun isValidAmount(amount: String, currency: Currency): Boolean {
        if (amount.isBlank()) return false

        val cleanedAmount = cleanAmountString(amount)
        val parsedAmount = cleanedAmount.toDoubleOrNull() ?: return false

        return isAmountInValidRange(parsedAmount, currency)
    }

    fun validateAmountWithDetails(amount: String, currency: Currency): AmountValidationResult {
        if (amount.isBlank()) {
            return AmountValidationResult.Invalid(Res.string.validation_amount_empty)
        }

        val cleanedAmount = cleanAmountString(amount)

        if (!isValidAmountFormat(cleanedAmount)) {
            return AmountValidationResult.Invalid(Res.string.validation_amount_invalid_format)
        }

        val parsedAmount = cleanedAmount.toDoubleOrNull()
            ?: return AmountValidationResult.Invalid(Res.string.validation_amount_invalid_number)

        if (parsedAmount < 0) {
            return AmountValidationResult.Invalid(Res.string.validation_amount_negative)
        }

        if (parsedAmount < MIN_AMOUNT) {
            return AmountValidationResult.Invalid(Res.string.validation_amount_too_small)
        }

        if (parsedAmount > MAX_AMOUNT) {
            return AmountValidationResult.Invalid(Res.string.validation_amount_too_large)
        }

        if (!isValidDecimalPlaces(parsedAmount, currency)) {
            return AmountValidationResult.Invalid(Res.string.validation_amount_invalid_decimal_places)
        }

//        if (!isMultipleOfMinimumUnit(parsedAmount, currency)) {
//            return AmountValidationResult.Invalid(Res.string.validation_amount_invalid_increment)
//        }

        val normalizedAmount = normalizeAmount(parsedAmount, currency)
        val formattedAmount = formatAmount(normalizedAmount, currency)

        return AmountValidationResult.Valid(
            normalizedAmount = normalizedAmount,
            formattedAmount = formattedAmount,
            currency = currency,
        )
    }

    private fun cleanAmountString(amount: String): String {
        // Remove currency symbols, spaces, and common separators
        return amount.trim()
            .replace(",", "")
            .replace(" ", "")
            .replace("$", "")
            .replace("€", "")
            .replace("£", "")
            .replace("¥", "")
            .replace("₹", "")
            .replace("₽", "")
            .replace("₺", "")
            .replace("₩", "")
            .replace("₪", "")
            .replace("₱", "")
            .replace("₼", "")
            .replace("₸", "")
            .replace("₾", "")
            .replace("֏", "")
    }

    private fun isValidAmountFormat(amount: String): Boolean {
        // Allow numbers with optional decimal point
        val regex = Regex("^\\d+(\\.\\d+)?$")
        return regex.matches(amount)
    }

    @Suppress("UnusedParameter")
    private fun isAmountInValidRange(amount: Double, currency: Currency): Boolean {
        return amount >= MIN_AMOUNT && amount <= MAX_AMOUNT
    }

    private fun isValidDecimalPlaces(amount: Double, currency: Currency): Boolean {
        val amountString = amount.toString()
        val decimalIndex = amountString.indexOf(',')

        if (decimalIndex == -1) {
            // No decimal places
            return true
        }

        val actualDecimalPlaces = amountString.length - decimalIndex - 1
        return actualDecimalPlaces <= currency.decimalPlaces.toInt()
    }

    @Suppress("UnusedPrivateMember")
    private fun isMultipleOfMinimumUnit(amount: Double, currency: Currency): Boolean {
        val remainder = amount % currency.inMultiplesOf
        return kotlin.math.abs(remainder) < 0.0001
    }

    private fun normalizeAmount(amount: Double, currency: Currency): Double {
        val multiplier = pow(10.0, currency.decimalPlaces.toInt())
        return round(amount * multiplier) / multiplier
    }

    private fun formatAmount(amount: Double, currency: Currency): String {
        val decimalPlaces = currency.decimalPlaces.toInt()
        return if (decimalPlaces == 0) {
            "${currency.displaySymbol}${amount.toInt()}"
        } else {
            "${currency.displaySymbol}${formatDecimal(amount, decimalPlaces)}"
        }
    }

    private fun pow(base: Double, exponent: Int): Double {
        if (exponent == 0) return 1.0
        if (exponent == 1) return base

        var result = 1.0
        var exp = exponent
        var currentBase = base

        while (exp > 0) {
            if (exp % 2 == 1) {
                result *= currentBase
            }
            currentBase *= currentBase
            exp /= 2
        }

        return result
    }

    private fun formatDecimal(value: Double, decimalPlaces: Int): String {
        val multiplier = pow(10.0, decimalPlaces)
        val rounded = round(value * multiplier) / multiplier

        val integerPart = rounded.toInt()
        val fractionalPart = rounded - integerPart

        if (decimalPlaces == 0) {
            return integerPart.toString()
        }

        val fractionalString = (fractionalPart * multiplier).toInt().toString()
        val paddedFractional = fractionalString.padStart(decimalPlaces, '0')

        return "$integerPart.$paddedFractional"
    }
}

enum class PhoneType {
    MOBILE,
    LANDLINE,
    SPECIAL,
    INVALID,
}

sealed class EmailValidationResult {
    data class Valid(val normalizedEmail: String) : EmailValidationResult()
    data class Invalid(val errorResource: StringResource) : EmailValidationResult()
}

sealed class EmailPartValidationResult {
    data object Valid : EmailPartValidationResult()
    data class Invalid(val errorResource: StringResource) : EmailPartValidationResult()
}

sealed class PhoneValidationResult {
    data class Valid(
        val formattedNumber: String,
        val type: PhoneType,
        val country: Country,
    ) : PhoneValidationResult()

    data class Invalid(val errorResource: StringResource) : PhoneValidationResult()
}

sealed class AmountValidationResult {
    data class Valid(
        val normalizedAmount: Double,
        val formattedAmount: String,
        val currency: Currency,
    ) : AmountValidationResult()

    data class Invalid(val errorResource: StringResource) : AmountValidationResult()
}
