/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
@file:Suppress("TooManyFunctions")

package org.mifos.mobile.core.data.mapper.templates

import org.mifos.mobile.core.data.mapper.charge.toChargeCalculationType
import org.mifos.mobile.core.data.mapper.charge.toChargeTimeType
import org.mifos.mobile.core.data.mapper.loan.toAmortizationType
import org.mifos.mobile.core.data.mapper.loan.toDaysInMonthType
import org.mifos.mobile.core.data.mapper.loan.toDaysInYearType
import org.mifos.mobile.core.data.mapper.loan.toInterestCalculationPeriodType
import org.mifos.mobile.core.data.mapper.loan.toInterestRateFrequencyType
import org.mifos.mobile.core.data.mapper.loan.toInterestReCalculationModel
import org.mifos.mobile.core.data.mapper.loan.toInterestType
import org.mifos.mobile.core.data.mapper.loan.toRepaymentFrequencyType
import org.mifos.mobile.core.data.mapper.loan.toTermPeriodFrequencyType
import org.mifos.mobile.core.model.entity.templates.loans.AccountLinkingOptions
import org.mifos.mobile.core.model.entity.templates.loans.AccountingRule
import org.mifos.mobile.core.model.entity.templates.loans.AllowAttributeOverrides
import org.mifos.mobile.core.model.entity.templates.loans.AmortizationTypeOptions
import org.mifos.mobile.core.model.entity.templates.loans.ChargeAppliesTo
import org.mifos.mobile.core.model.entity.templates.loans.ChargeOptions
import org.mifos.mobile.core.model.entity.templates.loans.ChargePaymentMode
import org.mifos.mobile.core.model.entity.templates.loans.Currency
import org.mifos.mobile.core.model.entity.templates.loans.FundOptions
import org.mifos.mobile.core.model.entity.templates.loans.InterestRateFrequencyTypeOptions
import org.mifos.mobile.core.model.entity.templates.loans.InterestTypeOptions
import org.mifos.mobile.core.model.entity.templates.loans.LoanCollateralOptions
import org.mifos.mobile.core.model.entity.templates.loans.LoanOfficerOptions
import org.mifos.mobile.core.model.entity.templates.loans.LoanPurposeOptions
import org.mifos.mobile.core.model.entity.templates.loans.LoanTemplate
import org.mifos.mobile.core.model.entity.templates.loans.Product
import org.mifos.mobile.core.model.entity.templates.loans.ProductOptions
import org.mifos.mobile.core.model.entity.templates.loans.RepaymentFrequencyDaysOfWeekTypeOptions
import org.mifos.mobile.core.model.entity.templates.loans.RepaymentFrequencyNthDayTypeOptions
import org.mifos.mobile.core.model.entity.templates.loans.RepaymentFrequencyTypeOptions
import org.mifos.mobile.core.model.entity.templates.loans.TaxGroup
import org.mifos.mobile.core.model.entity.templates.loans.TermFrequencyTypeOptions
import org.mifos.mobile.core.model.entity.templates.loans.Timeline
import org.mifos.mobile.core.model.entity.templates.loans.TransactionProcessingStrategyOptions
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.products.loan.LoanProductOptionsResponseDto
import org.mifos.mobile.core.network.dto.products.loan.LoanProductResponseDto
import org.mifos.mobile.core.network.dto.templates.loan.AccountLinkingOptionsResponseDto
import org.mifos.mobile.core.network.dto.templates.loan.AllowAttributesOverridesResponseDto
import org.mifos.mobile.core.network.dto.templates.loan.ChargeOptionsResponseDto
import org.mifos.mobile.core.network.dto.templates.loan.FundOptionsResponseDto
import org.mifos.mobile.core.network.dto.templates.loan.LoanCollateralOptionsResponseDto
import org.mifos.mobile.core.network.dto.templates.loan.LoanOfficerOptionsResponseDto
import org.mifos.mobile.core.network.dto.templates.loan.LoanPurposeOptionsResponseDto
import org.mifos.mobile.core.network.dto.templates.loan.LoanTemplateResponseDto
import org.mifos.mobile.core.network.dto.templates.loan.LoanTimelineResponseDto
import org.mifos.mobile.core.network.dto.templates.loan.TaxGroupResponseDto

fun LoanTemplateResponseDto.toModel(): LoanTemplate =
    LoanTemplate(
        clientId = clientId,
        clientAccountNo = clientAccountNo,
        clientName = clientName,
        clientOfficeId = clientOfficeId,
        loanProductName = loanProductName,
        loanProductLinkedToFloatingRate = loanProductLinkedToFloatingRate,
        fundId = fundId,
        fundName = fundName,
        currency = currency?.toLoanTemplateCurrency(),
        principal = principal,
        approvedPrincipal = approvedPrincipal,
        proposedPrincipal = proposedPrincipal,
        termFrequency = termFrequency,
        termPeriodFrequencyType = termPeriodFrequencyType?.toTermPeriodFrequencyType(),
        numberOfRepayments = numberOfRepayments,
        repaymentEvery = repaymentEvery,
        repaymentFrequencyType = repaymentFrequencyType?.toRepaymentFrequencyType(),
        interestRatePerPeriod = interestRatePerPeriod,
        interestRateFrequencyType = interestRateFrequencyType?.toInterestRateFrequencyType(),
        annualInterestRate = annualInterestRate,
        floatingInterestRate = floatingInterestRate,
        amortizationType = amortizationType?.toAmortizationType(),
        interestType = interestType?.toInterestType(),
        interestCalculationPeriodType =
        interestCalculationPeriodType?.toInterestCalculationPeriodType(),
        allowPartialPeriodInterestCalculation = allowPartialPeriodInterestCalculation,
        transactionProcessingStrategyId = transactionProcessingStrategyId,
        transactionProcessingStrategyCode = transactionProcessingStrategyCode,
        graceOnArrearsAgeing = graceOnArrearsAgeing,
        timeline = timeline?.toLoanTemplateTimelineModel(),

        productOptions = productOptions.map { it.toModel() },
        loanOfficerOptions = loanOfficerOptions.map { it.toModel() },
        loanPurposeOptions = loanPurposeOptions.map { it.toModel() },
        fundOptions = fundOptions.map { it.toModel() },

        termFrequencyTypeOptions =
        termFrequencyTypeOptions.map { it.toTermFrequencyTypeOptions() },

        repaymentFrequencyTypeOptions =
        repaymentFrequencyTypeOptions.map { it.toRepaymentFrequencyTypeOptions() },

        repaymentFrequencyNthDayTypeOptions =
        repaymentFrequencyNthDayTypeOptions.map {
            it.toRepaymentFrequencyNthDayTypeOptions()
        },

        repaymentFrequencyDaysOfWeekTypeOptions =
        repaymentFrequencyDaysOfWeekTypeOptions.map {
            it.toRepaymentFrequencyDaysOfWeekTypeOptions()
        },

        interestRateFrequencyTypeOptions =
        interestRateFrequencyTypeOptions.map {
            it.toInterestRateFrequencyTypeOptions()
        },

        amortizationTypeOptions =
        amortizationTypeOptions.map { it.toAmortizationTypeOptions() },

        interestTypeOptions =
        interestTypeOptions.map { it.toInterestTypeOptions() },

        interestCalculationPeriodTypeOptions =
        interestCalculationPeriodTypeOptions.map {
            it.toInterestCalculationPeriodType()
        },

        transactionProcessingStrategyOptions =
        transactionProcessingStrategyOptions.map {
            it.toTransactionProcessingStrategyOptions()
        },

        chargeOptions = chargeOptions.map { it.toModel() },
        loanCollateralOptions = loanCollateralOptions.map { it.toModel() },

        multiDisburseLoan = multiDisburseLoan,
        canDefineInstallmentAmount = canDefineInstallmentAmount,
        canDisburse = canDisburse,
        product = product?.toModel(),

        daysInMonthType = daysInMonthType?.toDaysInMonthType(),
        daysInYearType = daysInYearType?.toDaysInYearType(),

        interestRecalculationEnabled = interestRecalculationEnabled,
        valiableInstallmentsAllowed = valiableInstallmentsAllowed,
        minimumGap = minimumGap,
        maximumGap = maximumGap,

        accountLinkingOptions =
        accountLinkingOptions.map { it.toModel() },
    )

fun LoanTimelineResponseDto.toLoanTemplateTimelineModel(): Timeline =
    Timeline(
        expectedDisbursementDate = expectedDisbursementDate,
    )

fun CurrencyResponseDto.toLoanTemplateCurrency(): Currency =
    Currency(
        code = code,
        name = name,
        decimalPlaces = decimalPlaces.toDouble(),
        inMultiplesOf = inMultiplesOf.toInt(),
        displaySymbol = displaySymbol,
        nameCode = nameCode,
        displayLabel = displayLabel,
    )

fun LoanProductOptionsResponseDto.toModel(): ProductOptions =
    ProductOptions(
        id = id,
        name = name,
        includeInBorrowerCycle = includeInBorrowerCycle,
        useBorrowerCycle = useBorrowerCycle,
        linkedToFloatingInterestRates = linkedToFloatingInterestRates,
        floatingInterestRateCalculationAllowed = floatingInterestRateCalculationAllowed,
        allowvaliableInstallments = allowvaliableInstallments,
        interestRecalculationEnabled = interestRecalculationEnabled,
        canDefineInstallmentAmount = canDefineInstallmentAmount,
        holdGuaranteeFunds = holdGuaranteeFunds,
        accountMovesOutOfNPAOnlyOnArrearsCompletion =
        accountMovesOutOfNPAOnlyOnArrearsCompletion,
    )

fun LoanOfficerOptionsResponseDto.toModel(): LoanOfficerOptions =
    LoanOfficerOptions(
        id = id,
        firstname = firstname,
        lastname = lastname,
        displayName = displayName,
        mobileNo = mobileNo,
        officeId = officeId,
        officeName = officeName,
        loanOfficer = loanOfficer,
        active = active,
        joiningDate = joiningDate,
    )

fun LoanPurposeOptionsResponseDto.toModel(): LoanPurposeOptions =
    LoanPurposeOptions(
        id = id,
        name = name,
        position = position,
        description = description,
        active = active,
    )

fun FundOptionsResponseDto.toModel(): FundOptions =
    FundOptions(
        id = id,
        name = name,
    )

fun LoanCollateralOptionsResponseDto.toModel(): LoanCollateralOptions =
    LoanCollateralOptions(
        id = id,
        name = name,
        position = position,
        description = description,
        active = active,
    )

fun LoanProductResponseDto.toModel(): Product =
    Product(
        id = id,
        name = name,
        shortName = shortName,
        fundId = fundId,
        fundName = fundName,
        includeInBorrowerCycle = includeInBorrowerCycle,
        useBorrowerCycle = useBorrowerCycle,
        startDate = startDate,
        status = status,
        currency = currency?.toLoanTemplateCurrency(),
        principal = principal,
        minPrincipal = minPrincipal,
        maxPrincipal = maxPrincipal,
        numberOfRepayments = numberOfRepayments,
        minNumberOfRepayments = minNumberOfRepayments,
        maxNumberOfRepayments = maxNumberOfRepayments,
        repaymentEvery = repaymentEvery,
        repaymentFrequencyType =
        repaymentFrequencyType?.toRepaymentFrequencyType(),
        interestRatePerPeriod = interestRatePerPeriod,
        minInterestRatePerPeriod = minInterestRatePerPeriod,
        maxInterestRatePerPeriod = maxInterestRatePerPeriod,
        interestRateFrequencyType =
        interestRateFrequencyType?.toInterestRateFrequencyType(),
        annualInterestRate = annualInterestRate,
        linkedToFloatingInterestRates = linkedToFloatingInterestRates,
        floatingInterestRateCalculationAllowed =
        floatingInterestRateCalculationAllowed,
        allowvaliableInstallments = allowvaliableInstallments,
        minimumGap = minimumGap,
        maximumGap = maximumGap,
        amortizationType = amortizationType.toAmortizationType(),
        interestType = interestType.toInterestType(),
        interestCalculationPeriodType =
        interestCalculationPeriodType?.toInterestCalculationPeriodType(),
        allowPartialPeriodInterestCalculation =
        allowPartialPeriodInterestCalculation,
        transactionProcessingStrategyId =
        transactionProcessingStrategyId,
        transactionProcessingStrategyName =
        transactionProcessingStrategyName,
        graceOnArrearsAgeing = graceOnArrearsAgeing,
        overdueDaysForNPA = overdueDaysForNPA,
        daysInMonthType = daysInMonthType?.toDaysInMonthType(),
        daysInYearType = daysInYearType.toDaysInYearType(),
        interestRecalculationEnabled = interestRecalculationEnabled,
        interestRecalculationData =
        interestRecalculationData?.toInterestReCalculationModel(),
        canDefineInstallmentAmount = canDefineInstallmentAmount,
        accountingRule = accountingRule?.toAccountingRule(),
        multiDisburseLoan = multiDisburseLoan,
        maxTrancheCount = maxTrancheCount,
        principalThresholdForLastInstallment =
        principalThresholdForLastInstallment,
        holdGuaranteeFunds = holdGuaranteeFunds,
        accountMovesOutOfNPAOnlyOnArrearsCompletion =
        accountMovesOutOfNPAOnlyOnArrearsCompletion,
        allowAttributeOverrides =
        allowAttributeOverrides?.toModel(),
    )

fun AccountLinkingOptionsResponseDto.toModel(): AccountLinkingOptions =
    AccountLinkingOptions(
        accountNo = accountNo,
        clientId = clientId,
        clientName = clientName,
        currency = currency?.toLoanTemplateCurrency(),
        fieldOfficerId = fieldOfficerId,
        id = id,
        productId = productId,
        productName = productName,
    )

fun AllowAttributesOverridesResponseDto.toModel(): AllowAttributeOverrides =
    AllowAttributeOverrides(
        amortizationType = amortizationType,
        interestType = interestType,
        transactionProcessingStrategyId = transactionProcessingStrategyId,
        interestCalculationPeriodType = interestCalculationPeriodType,
        inArrearsTolerance = inArrearsTolerance,
        repaymentEvery = repaymentEvery,
        graceOnPrincipalAndInterestPayment = graceOnPrincipalAndInterestPayment,
        graceOnArrearsAgeing = graceOnArrearsAgeing,
    )

fun ChargeOptionsResponseDto.toModel(): ChargeOptions =
    ChargeOptions(
        id = id,
        name = name,
        active = active,
        penalty = penalty,
        currency = currency?.toLoanTemplateCurrency(),
        amount = amount,
        chargeTimeType = chargeTimeType?.toChargeTimeType(),
        chargeAppliesTo = chargeAppliesTo?.toChargeAppliesTo(),
        chargeCalculationType = chargeCalculationType?.toChargeCalculationType(),
        chargePaymentMode = chargePaymentMode?.toChargePaymentMode(),
        taxGroup = taxGroup?.toModel(),
    )

fun TaxGroupResponseDto.toModel(): TaxGroup =
    TaxGroup(
        id = id,
        name = name,
    )

fun TypeResponseDto.toTermFrequencyTypeOptions() =
    TermFrequencyTypeOptions(id, code, value)

fun TypeResponseDto.toRepaymentFrequencyTypeOptions() =
    RepaymentFrequencyTypeOptions(id, code, value)

fun TypeResponseDto.toRepaymentFrequencyNthDayTypeOptions() =
    RepaymentFrequencyNthDayTypeOptions(id, code, value)

fun TypeResponseDto.toRepaymentFrequencyDaysOfWeekTypeOptions() =
    RepaymentFrequencyDaysOfWeekTypeOptions(id, code, value)

fun TypeResponseDto.toInterestRateFrequencyTypeOptions() =
    InterestRateFrequencyTypeOptions(id, code, value)

fun TypeResponseDto.toAmortizationTypeOptions() =
    AmortizationTypeOptions(id, code, value)

fun TypeResponseDto.toInterestTypeOptions() =
    InterestTypeOptions(id, code, value)

fun TypeResponseDto.toTransactionProcessingStrategyOptions() =
    TransactionProcessingStrategyOptions(id, code, value)

fun TypeResponseDto.toChargeAppliesTo() = ChargeAppliesTo(id, code, value)

fun TypeResponseDto.toChargePaymentMode() = ChargePaymentMode(id, code, value)

fun TypeResponseDto.toAccountingRule() = AccountingRule(id, code, value)
