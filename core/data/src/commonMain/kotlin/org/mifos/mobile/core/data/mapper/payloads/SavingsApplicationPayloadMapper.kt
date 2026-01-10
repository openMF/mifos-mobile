package org.mifos.mobile.core.data.mapper.payloads

import org.mifos.mobile.core.model.entity.accounts.savings.SavingsAccountApplicationPayload
import org.mifos.mobile.core.model.entity.accounts.savings.SavingsChargePayload
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsAccountApplicationPayloadDto
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsChargePayloadDto

fun SavingsAccountApplicationPayload.toDto(): SavingsAccountApplicationPayloadDto =
    SavingsAccountApplicationPayloadDto(
        // Mandatory
        clientId = clientId,
        productId = productId,
        submittedOnDate = submittedOnDate,
        locale = locale,
        dateFormat = dateFormat,
        monthDayFormat = monthDayFormat,

        // Optional
        accountNo = accountNo,
        externalId = externalId,
        fieldOfficerId = fieldOfficerId,

        // Override fields
        nominalAnnualInterestRate = nominalAnnualInterestRate,
        interestCompoundingPeriodType = interestCompoundingPeriodType,
        interestPostingPeriodType = interestPostingPeriodType,
        interestCalculationType = interestCalculationType,
        interestCalculationDaysInYearType = interestCalculationDaysInYearType,
        minRequiredOpeningBalance = minRequiredOpeningBalance,
        lockinPeriodFrequency = lockinPeriodFrequency,
        lockinPeriodFrequencyType = lockinPeriodFrequencyType,
        withdrawalFeeForTransfers = withdrawalFeeForTransfers,
        allowOverdraft = allowOverdraft,
        overdraftLimit = overdraftLimit,
        nominalAnnualInterestRateOverdraft = nominalAnnualInterestRateOverdraft,
        minOverdraftForInterestCalculation = minOverdraftForInterestCalculation,
        enforceMinRequiredBalance = enforceMinRequiredBalance,
        withHoldTax = withHoldTax,

        // Charges
        charges = charges?.map { it.toDto() }
    )

fun SavingsChargePayload.toDto(): SavingsChargePayloadDto =
    SavingsChargePayloadDto(
        chargeId = chargeId,
        amount = amount
    )
