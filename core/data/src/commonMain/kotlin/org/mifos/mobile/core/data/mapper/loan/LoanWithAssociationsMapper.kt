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

package org.mifos.mobile.core.data.mapper.loan

import org.mifos.mobile.core.data.mapper.accounts.toModel
import org.mifos.mobile.core.model.entity.Currency
import org.mifos.mobile.core.model.entity.Transaction
import org.mifos.mobile.core.model.entity.accounts.loan.AmortizationType
import org.mifos.mobile.core.model.entity.accounts.loan.DaysInMonthType
import org.mifos.mobile.core.model.entity.accounts.loan.DaysInYearType
import org.mifos.mobile.core.model.entity.accounts.loan.InterestCalculationPeriodType
import org.mifos.mobile.core.model.entity.accounts.loan.InterestRateFrequencyType
import org.mifos.mobile.core.model.entity.accounts.loan.InterestRecalculationCompoundingType
import org.mifos.mobile.core.model.entity.accounts.loan.InterestRecalculationData
import org.mifos.mobile.core.model.entity.accounts.loan.InterestType
import org.mifos.mobile.core.model.entity.accounts.loan.LoanType
import org.mifos.mobile.core.model.entity.accounts.loan.LoanWithAssociations
import org.mifos.mobile.core.model.entity.accounts.loan.Periods
import org.mifos.mobile.core.model.entity.accounts.loan.RecalculationCompoundingFrequencyType
import org.mifos.mobile.core.model.entity.accounts.loan.RecalculationRestFrequencyType
import org.mifos.mobile.core.model.entity.accounts.loan.RepaymentFrequencyType
import org.mifos.mobile.core.model.entity.accounts.loan.RepaymentSchedule
import org.mifos.mobile.core.model.entity.accounts.loan.RescheduleStrategyType
import org.mifos.mobile.core.model.entity.accounts.loan.Summary
import org.mifos.mobile.core.model.entity.accounts.loan.TermPeriodFrequencyType
import org.mifos.mobile.core.model.entity.accounts.loan.Timeline
import org.mifos.mobile.core.model.entity.accounts.loan.calendardata.CalendarData
import org.mifos.mobile.core.model.entity.accounts.loan.calendardata.EntityType
import org.mifos.mobile.core.model.entity.accounts.loan.calendardata.Frequency
import org.mifos.mobile.core.model.entity.accounts.loan.calendardata.RepeatsOnNthDayOfMonth
import org.mifos.mobile.core.model.entity.accounts.loan.calendardata.Type
import org.mifos.mobile.core.network.dto.common.TypeResponseDto
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.intrest.CalendarDataResponseDto
import org.mifos.mobile.core.network.dto.intrest.InterestRecalculationDataResponseDto
import org.mifos.mobile.core.network.dto.loanAccount.LoanSummaryResponseDto
import org.mifos.mobile.core.network.dto.loanAccount.LoanTimelineResponseDto
import org.mifos.mobile.core.network.dto.loanAccount.LoanWithAssociationsResponseDto
import org.mifos.mobile.core.network.dto.loanAccount.PeriodsResponseDto
import org.mifos.mobile.core.network.dto.loanAccount.RepaymentScheduleResponseDto
import org.mifos.mobile.core.network.dto.transaction.TransactionResponseDto
import org.mifos.mobile.core.model.entity.accounts.loan.Currency as LoanCurrency
import org.mifos.mobile.core.model.entity.client.Type as CoreType

fun LoanWithAssociationsResponseDto.toModel(): LoanWithAssociations =
    LoanWithAssociations(
        id = id,
        accountNo = accountNo,
        externalId = externalId,
        status = status?.toModel(),
        clientId = clientId,
        clientAccountNo = clientAccountNo,
        clientName = clientName,
        clientOfficeId = clientOfficeId,
        loanProductId = loanProductId,
        loanProductName = loanProductName,
        loanProductLinkedToFloatingRate = loanProductLinkedToFloatingRate,
        loanType = loanType?.toLoanType(),
        currency = currency?.toLoanCurrencyModel(),
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
        floatingInterestRate = floatingInterestRate,
        amortizationType = amortizationType?.toAmortizationType(),
        interestType = interestType?.toInterestType(),
        interestCalculationPeriodType = interestCalculationPeriodType?.toInterestCalculationPeriodType(),
        allowPartialPeriodInterestCalculation = allowPartialPeriodInterestCalculation,
        transactionProcessingStrategyId = transactionProcessingStrategyId,
        transactionProcessingStrategyName = transactionProcessingStrategyName,
        syncDisbursementWithMeeting = syncDisbursementWithMeeting,
        timeline = timeline?.toLoanTimelineModel(),
        summary = summary?.toLoanSummaryModel(),
        repaymentSchedule = repaymentSchedule?.toRepaymentScheduleResponseModel(),
        feeChargesAtDisbursementCharged = feeChargesAtDisbursementCharged,
        loanProductCounter = loanProductCounter,
        multiDisburseLoan = multiDisburseLoan,
        canDefineInstallmentAmount = canDefineInstallmentAmount,
        canDisburse = canDisburse,
        canUseForTopup = canUseForTopup,
        topup = topup,
        closureLoanId = closureLoanId,
        inArrears = inArrears,
        npa = npa,
        daysInMonthType = daysInMonthType?.toDaysInMonthType(),
        daysInYearType = daysInYearType?.toDaysInYearType(),
        interestRecalculationEnabled = interestRecalculationEnabled,
        interestRecalculationData = interestRecalculationData?.toInterestReCalculationModel(),
        createStandingInstructionAtDisbursement = createStandingInstructionAtDisbursement,
        valiableInstallmentsAllowed = valiableInstallmentsAllowed,
        minimumGap = minimumGap,
        maximumGap = maximumGap,
        transactions = transactions
            ?.filterNotNull()
            ?.map { it.toModel() },
        loanPurposeName = loanPurposeName,
    )

fun LoanTimelineResponseDto.toLoanTimelineModel(): Timeline =
    Timeline(
        submittedOnDate = submittedOnDate,
        submittedByUsername = submittedByUsername,
        submittedByFirstname = submittedByFirstname,
        submittedByLastname = submittedByLastname,
        approvedOnDate = approvedOnDate,
        approvedByUsername = approvedByUsername,
        approvedByFirstname = approvedByFirstname,
        approvedByLastname = approvedByLastname,
        expectedDisbursementDate = expectedDisbursementDate,
        actualDisbursementDate = actualDisbursementDate,
        disbursedByUsername = disbursedByUsername,
        disbursedByFirstname = disbursedByFirstname,
        disbursedByLastname = disbursedByLastname,
        closedOnDate = closedOnDate,
        expectedMaturityDate = expectedMaturityDate,
        withdrawnOnDate = withdrawnOnDate,
    )

fun LoanSummaryResponseDto.toLoanSummaryModel(): Summary =
    Summary(
        principalDisbursed = principalDisbursed,
        principalPaid = principalPaid,
        interestCharged = interestCharged,
        interestPaid = interestPaid,
        feeChargesCharged = feeChargesCharged,
        penaltyChargesCharged = penaltyChargesCharged,
        penaltyChargesWaived = penaltyChargesWaived,
        totalExpectedRepayment = totalExpectedRepayment,
        interestWaived = interestWaived,
        totalRepayment = totalRepayment,
        feeChargesWaived = feeChargesWaived,
        totalOutstanding = totalOutstanding,
        currency = currency?.toLoanCurrencyModel(),
    )

fun RepaymentScheduleResponseDto.toRepaymentScheduleResponseModel(): RepaymentSchedule =
    RepaymentSchedule(
        currency = currency?.toLoanCurrencyModel(),
        loanTermInDays = loanTermInDays,
        totalPrincipalDisbursed = totalPrincipalDisbursed,
        totalPrincipalExpected = totalPrincipalExpected,
        totalPrincipalPaid = totalPrincipalPaid,
        totalInterestCharged = totalInterestCharged,
        totalFeeChargesCharged = totalFeeChargesCharged,
        totalPenaltyChargesCharged = totalPenaltyChargesCharged,
        totalWaived = totalWaived,
        totalWrittenOff = totalWrittenOff,
        totalRepaymentExpected = totalRepaymentExpected,
        totalRepayment = totalRepayment,
        totalPaidInAdvance = totalPaidInAdvance,
        totalPaidLate = totalPaidLate,
        totalOutstanding = totalOutstanding,
        periods = periods.map { it.toModel() },
    )

fun PeriodsResponseDto.toModel(): Periods =
    Periods(
        period = period,
        fromDate = fromDate,
        dueDate = dueDate,
        obligationsMetOnDate = obligationsMetOnDate,
        principalDisbursed = principalDisbursed,
        complete = complete,
        daysInPeriod = daysInPeriod,
        principalOriginalDue = principalOriginalDue,
        principalDue = principalDue,
        principalPaid = principalPaid,
        principalWrittenOff = principalWrittenOff,
        principalOutstanding = principalOutstanding,
        principalLoanBalanceOutstanding = principalLoanBalanceOutstanding,
        interestOriginalDue = interestOriginalDue,
        interestDue = interestDue,
        interestPaid = interestPaid,
        interestWaived = interestWaived,
        interestWrittenOff = interestWrittenOff,
        interestOutstanding = interestOutstanding,
        feeChargesDue = feeChargesDue,
        feeChargesPaid = feeChargesPaid,
        feeChargesWaived = feeChargesWaived,
        feeChargesWrittenOff = feeChargesWrittenOff,
        feeChargesOutstanding = feeChargesOutstanding,
        penaltyChargesDue = penaltyChargesDue,
        penaltyChargesPaid = penaltyChargesPaid,
        penaltyChargesWaived = penaltyChargesWaived,
        penaltyChargesWrittenOff = penaltyChargesWrittenOff,
        penaltyChargesOutstanding = penaltyChargesOutstanding,
        totalOriginalDueForPeriod = totalOriginalDueForPeriod,
        totalDueForPeriod = totalDueForPeriod,
        totalPaidForPeriod = totalPaidForPeriod,
        totalPaidInAdvanceForPeriod = totalPaidInAdvanceForPeriod,
        totalPaidLateForPeriod = totalPaidLateForPeriod,
        totalWaivedForPeriod = totalWaivedForPeriod,
        totalWrittenOffForPeriod = totalWrittenOffForPeriod,
        totalOutstandingForPeriod = totalOutstandingForPeriod,
        totalOverdue = totalOverdue,
        totalActualCostOfLoanForPeriod = totalActualCostOfLoanForPeriod,
        totalInstallmentAmountForPeriod = totalInstallmentAmountForPeriod,
    )

fun InterestRecalculationDataResponseDto.toInterestReCalculationModel(): InterestRecalculationData =
    InterestRecalculationData(
        id = id,
        loanId = loanId,
        interestRecalculationCompoundingType =
        interestRecalculationCompoundingType?.toInterestRecalculationCompoundingType(),
        rescheduleStrategyType =
        rescheduleStrategyType?.toRescheduleStrategyType(),
        calendarData = calendarData.toCalendarModel(),
        recalculationRestFrequencyType =
        recalculationRestFrequencyType?.toRecalculationRestFrequencyType(),
        recalculationRestFrequencyInterval = recalculationRestFrequencyInterval,
        recalculationCompoundingFrequencyType =
        recalculationCompoundingFrequencyType?.toRecalculationCompoundingFrequencyType(),
        compoundingToBePostedAsTransaction = compoundingToBePostedAsTransaction,
        allowCompoundingOnEod = allowCompoundingOnEod,
    )

fun CalendarDataResponseDto.toCalendarModel(): CalendarData =
    CalendarData(
        id = id,
        calendarInstanceId = calendarInstanceId,
        entityId = entityId,
        entityType = entityType.toEntityType(),
        title = title,
        startDate = startDate,
        endDate = endDate,
        duration = duration,
        type = type.toCalendarType(),
        repeating = repeating,
        recurrence = recurrence,
        frequency = frequency.toFrequency(),
        interval = interval,
        repeatsOnNthDayOfMonth =
        repeatsOnNthDayOfMonth.toRepeatsOnNthDayOfMonth(),
        firstReminder = firstReminder,
        secondReminder = secondReminder,
        humanReadable = humanReadable,
        createdDate = createdDate,
        lastUpdatedDate = lastUpdatedDate,
        createdByUserId = createdByUserId,
        createdByUsername = createdByUsername,
        lastUpdatedByUserId = lastUpdatedByUserId,
        lastUpdatedByUsername = lastUpdatedByUsername,
    )

fun TransactionResponseDto.toModel(): Transaction =
    Transaction(
        id = id,
        officeId = officeId,
        officeName = officeName,
        type = type.toType(),
        date = date,
        currency = currency?.toLoanWithAssociationsModel(),
        amount = amount,
        submittedOnDate = submittedOnDate,
        reversed = reversed,
    )

fun CurrencyResponseDto.toLoanWithAssociationsModel(): Currency =
    Currency(
        code = code,
        name = name,
        decimalPlaces = decimalPlaces,
        inMultiplesOf = inMultiplesOf,
        displaySymbol = displaySymbol,
        nameCode = nameCode,
        displayLabel = displayLabel,
    )

fun CurrencyResponseDto.toLoanCurrencyModel(): LoanCurrency =
    LoanCurrency(
        code = code,
        name = name,
        decimalPlaces = decimalPlaces.toDouble(),
        inMultiplesOf = inMultiplesOf,
        displaySymbol = displaySymbol,
        nameCode = nameCode,
        displayLabel = displayLabel,
    )

fun TypeResponseDto.toType() = CoreType(id, code, value)
fun TypeResponseDto.toLoanType() = LoanType(id, code, value)
fun TypeResponseDto.toTermPeriodFrequencyType() = TermPeriodFrequencyType(id, code, value)
fun TypeResponseDto.toRepaymentFrequencyType() = RepaymentFrequencyType(id, code, value)
fun TypeResponseDto.toInterestRateFrequencyType() = InterestRateFrequencyType(id, code, value)
fun TypeResponseDto.toAmortizationType() = AmortizationType(id, code, value)
fun TypeResponseDto.toInterestType() = InterestType(id, code, value)
fun TypeResponseDto.toInterestCalculationPeriodType() =
    InterestCalculationPeriodType(id, code, value)
fun TypeResponseDto.toDaysInMonthType() = DaysInMonthType(id, code, value)
fun TypeResponseDto.toDaysInYearType() = DaysInYearType(id, code, value)
fun TypeResponseDto.toEntityType() = EntityType(id, code, value)
fun TypeResponseDto.toCalendarType() = Type(id, code, value)
fun TypeResponseDto.toFrequency() = Frequency(id, code, value)
fun TypeResponseDto.toRepeatsOnNthDayOfMonth() = RepeatsOnNthDayOfMonth(id, code, value)
fun TypeResponseDto.toInterestRecalculationCompoundingType() =
    InterestRecalculationCompoundingType(id, code, value)

fun TypeResponseDto.toRescheduleStrategyType() =
    RescheduleStrategyType(id, code, value)

fun TypeResponseDto.toRecalculationRestFrequencyType() =
    RecalculationRestFrequencyType(id, code, value)

fun TypeResponseDto.toRecalculationCompoundingFrequencyType() =
    RecalculationCompoundingFrequencyType(id, code, value)
