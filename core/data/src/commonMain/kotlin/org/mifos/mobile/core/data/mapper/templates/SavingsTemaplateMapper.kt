/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper.templates

import org.mifos.mobile.core.data.mapper.charge.toChargeCurrencyModel
import org.mifos.mobile.core.model.entity.accounts.savings.Currency
import org.mifos.mobile.core.model.entity.templates.savings.ChargeAppliesTo
import org.mifos.mobile.core.model.entity.templates.savings.ChargeCalculationType
import org.mifos.mobile.core.model.entity.templates.savings.ChargeOptions
import org.mifos.mobile.core.model.entity.templates.savings.ChargePaymentMode
import org.mifos.mobile.core.model.entity.templates.savings.ChargeTimeType
import org.mifos.mobile.core.model.entity.templates.savings.FieldOfficerOptions
import org.mifos.mobile.core.model.entity.templates.savings.ProductOptions
import org.mifos.mobile.core.model.entity.templates.savings.SavingsAccountTemplate
import org.mifos.mobile.core.model.entity.templates.savings.SavingsOptions
import org.mifos.mobile.core.model.entity.templates.savings.SavingsProduct
import org.mifos.mobile.core.model.entity.templates.savings.Timeline
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.products.savings.SavingsProductOptionsResponseDto
import org.mifos.mobile.core.network.dto.products.savings.SavingsProductResponseDto
import org.mifos.mobile.core.network.dto.templates.savings.FieldOfficerOptionsResponseDto
import org.mifos.mobile.core.network.dto.templates.savings.SavingsAccountTemplateResponseDto
import org.mifos.mobile.core.network.dto.templates.savings.SavingsChargeOptionsResponseDto
import org.mifos.mobile.core.network.dto.templates.savings.SavingsOptionsResponseDto
import org.mifos.mobile.core.network.dto.templates.savings.SavingsTimelineResponseDto

fun SavingsAccountTemplateResponseDto.toModel(): SavingsAccountTemplate =
    SavingsAccountTemplate(
        clientId = clientId,
        clientName = clientName,
        savingsProductId = savingsProductId,
        savingsProductName = savingsProductName,
        timeline = timeline?.toModel(),
        currency = currency?.toSavingsTemplate(),
        nominalAnnualInterestRate = nominalAnnualInterestRate,
        interestCompoundingPeriodType = interestCompoundingPeriodType?.toModel(),
        interestPostingPeriodType = interestPostingPeriodType?.toModel(),
        interestCalculationType = interestCalculationType?.toModel(),
        interestCalculationDaysInYearType = interestCalculationDaysInYearType?.toModel(),
        minRequiredOpeningBalance = minRequiredOpeningBalance,
        withdrawalFeeForTransfers = withdrawalFeeForTransfers,
        allowOverdraft = allowOverdraft,
        enforceMinRequiredBalance = enforceMinRequiredBalance,
        withHoldTax = withHoldTax,
        isDormancyTrackingActive = isDormancyTrackingActive,
        charges = charges?.map { it.toModel() },
        productOptions = ArrayList(productOptions.map { it.toModel() }),
        fieldOfficerOptions = fieldOfficerOptions.map { it.toModel() },
        interestCompoundingPeriodTypeOptions =
        interestCompoundingPeriodTypeOptions.map { it.toModel() },
        interestPostingPeriodTypeOptions =
        interestPostingPeriodTypeOptions.map { it.toModel() },
        interestCalculationTypeOptions =
        interestCalculationTypeOptions.map { it.toModel() },
        interestCalculationDaysInYearTypeOptions =
        interestCalculationDaysInYearTypeOptions.map { it.toModel() },
        lockinPeriodFrequencyTypeOptions =
        lockinPeriodFrequencyTypeOptions.map { it.toModel() },
        withdrawalFeeTypeOptions =
        withdrawalFeeTypeOptions.map { it.toModel() },
        chargeOptions = ArrayList(chargeOptions.map { it.toModel() }),
    )

fun SavingsTimelineResponseDto.toModel(): Timeline =
    Timeline(
        expectedDisbursementDate = expectedDisbursementDate,
    )

fun CurrencyResponseDto.toSavingsTemplate(): Currency =
    Currency(
        code = code,
        name = name,
        decimalPlaces = decimalPlaces,
        inMultiplesOf = inMultiplesOf.toInt(),
        displaySymbol = displaySymbol,
        nameCode = nameCode,
        displayLabel = displayLabel,
    )

fun SavingsProductResponseDto.toModel(): SavingsProduct =
    SavingsProduct(
        id = id,
        name = name,
        withdrawalFeeForTransfers = withdrawalFeeForTransfers,
        allowOverdraft = allowOverdraft,
        enforceMinRequiredBalance = enforceMinRequiredBalance,
        lienAllowed = lienAllowed,
        withHoldTax = withHoldTax,
    )

fun SavingsProductOptionsResponseDto.toModel(): ProductOptions =
    ProductOptions(
        id = id,
        name = name,
    )

fun FieldOfficerOptionsResponseDto.toModel(): FieldOfficerOptions =
    FieldOfficerOptions(
        id = id,
        firstname = firstname,
        lastname = lastname,
        displayName = displayName,
        officeId = officeId,
        officeName = officeName,
        isLoanOfficer = isLoanOfficer,
        isActive = isActive,
    )

fun SavingsOptionsResponseDto.toModel(): SavingsOptions =
    SavingsOptions(
        id = id,
        code = code,
        value = value,
    )

fun SavingsChargeOptionsResponseDto.toModel(): ChargeOptions =
    ChargeOptions(
        id = id,
        name = name,
        active = active,
        penalty = penalty,
        currency = currency?.toChargeCurrencyModel(),
        amount = amount,
        chargeTimeType = chargeTimeType.toChargeTimeType(),
        chargeAppliesTo = chargeAppliesTo.toSavingsChargeAppliesTo(),
        chargeCalculationType = chargeCalculationType.toSavingsChargeCalculationType(),
        chargePaymentMode = chargePaymentMode.toSavingsChargePaymentMode(),
    )

fun TypeResponseDto.toChargeTimeType(): ChargeTimeType =
    ChargeTimeType(
        id = id?.toFloat() ?: 0f,
        code = code,
        value = value,
    )

fun TypeResponseDto.toSavingsChargeCalculationType(): ChargeCalculationType =
    ChargeCalculationType(
        id = id?.toFloat() ?: 0f,
        code = code,
        value = value,
    )

fun TypeResponseDto.toSavingsChargeAppliesTo() = ChargeAppliesTo(id?.toFloat() ?: 0f, code, value)

fun TypeResponseDto.toSavingsChargePaymentMode() = ChargePaymentMode(id?.toFloat() ?: 0f, code, value)
