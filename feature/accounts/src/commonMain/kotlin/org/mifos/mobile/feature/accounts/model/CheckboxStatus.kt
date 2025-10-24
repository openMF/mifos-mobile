/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.feature.accounts.model

import org.jetbrains.compose.resources.StringResource

/**
 * Data class representing a checkbox status.
 *
 * @property statusLabel The label of the checkbox.
 * @property isChecked Boolean indicating whether the checkbox is checked.
 * @property type The type of the checkbox (ACCOUNT_TYPE or ACCOUNT_STATUS).
 */
internal data class CheckboxStatus(
    val statusLabel: StringResource,
    val isChecked: Boolean = false,
    val type: FilterType = FilterType.ACCOUNT_STATUS,
)

/**
 * Enum class representing the type of filter.
 *
 * @property ACCOUNT_TYPE The type of filter for account type.
 * @property ACCOUNT_STATUS The type of filter for account status.
 */
enum class FilterType {
    ACCOUNT_TYPE,
    ACCOUNT_STATUS,
}

/**
 * Data class representing a transaction checkbox status.
 *
 * @property statusLabel The label of the transaction checkbox.
 * @property isChecked Boolean indicating whether the transaction checkbox is checked.
 * @property type The type of the transaction checkbox (TRANSACTION_TYPE or DURATION).
 */
internal data class TransactionCheckboxStatus(
    val statusLabel: StringResource,
    val isChecked: Boolean = false,
    val type: TransactionFilterType = TransactionFilterType.TRANSACTION_TYPE,
)

/**
 * Enum class representing the type of transaction filter.
 *
 * @property TRANSACTION_TYPE The type of transaction filter for transaction type.
 * @property DURATION The type of transaction filter for duration.
 */
enum class TransactionFilterType {
    TRANSACTION_TYPE,
    DURATION,
}
