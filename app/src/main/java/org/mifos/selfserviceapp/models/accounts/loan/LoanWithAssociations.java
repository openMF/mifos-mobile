package org.mifos.selfserviceapp.models.accounts.loan;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.mifos.selfserviceapp.models.Transaction;

import java.util.List;

/**
 * Created by Rajan Maurya on 04/03/17.
 */

public class LoanWithAssociations implements Parcelable {

    @SerializedName("id")
    Integer id;

    @SerializedName("accountNo")
    String accountNo;

    @SerializedName("externalId")
    String externalId;

    @SerializedName("status")
    Status status;

    @SerializedName("clientId")
    Integer clientId;

    @SerializedName("clientAccountNo")
    String clientAccountNo;

    @SerializedName("clientName")
    String clientName;

    @SerializedName("clientOfficeId")
    Integer clientOfficeId;

    @SerializedName("loanProductId")
    Integer loanProductId;

    @SerializedName("loanProductName")
    String loanProductName;

    @SerializedName("isLoanProductLinkedToFloatingRate")
    Boolean isLoanProductLinkedToFloatingRate;

    @SerializedName("loanType")
    LoanType loanType;

    @SerializedName("currency")
    Currency currency;

    @SerializedName("principal")
    Double principal;

    @SerializedName("approvedPrincipal")
    Double approvedPrincipal;

    @SerializedName("proposedPrincipal")
    Double proposedPrincipal;

    @SerializedName("termFrequency")
    Integer termFrequency;

    @SerializedName("termPeriodFrequencyType")
    TermPeriodFrequencyType termPeriodFrequencyType;

    @SerializedName("numberOfRepayments")
    Integer numberOfRepayments;

    @SerializedName("repaymentEvery")
    Integer repaymentEvery;

    @SerializedName("repaymentFrequencyType")
    RepaymentFrequencyType repaymentFrequencyType;

    @SerializedName("interestRatePerPeriod")
    Integer interestRatePerPeriod;

    @SerializedName("interestRateFrequencyType")
    InterestRateFrequencyType interestRateFrequencyType;

    @SerializedName("isFloatingInterestRate")
    Boolean isFloatingInterestRate;

    @SerializedName("amortizationType")
    AmortizationType amortizationType;

    @SerializedName("interestType")
    InterestType interestType;

    @SerializedName("interestCalculationPeriodType")
    InterestCalculationPeriodType interestCalculationPeriodType;

    @SerializedName("allowPartialPeriodInterestCalcualtion")
    Boolean allowPartialPeriodInterestCalcualtion;

    @SerializedName("transactionProcessingStrategyId")
    Integer transactionProcessingStrategyId;

    @SerializedName("transactionProcessingStrategyName")
    String transactionProcessingStrategyName;

    @SerializedName("syncDisbursementWithMeeting")
    Boolean syncDisbursementWithMeeting;

    @SerializedName("timeline")
    Timeline timeline;

    @SerializedName("summary")
    Summary summary;

    @SerializedName("repaymentSchedule")
    RepaymentSchedule repaymentSchedule;

    @SerializedName("feeChargesAtDisbursementCharged")
    Double feeChargesAtDisbursementCharged;

    @SerializedName("loanProductCounter")
    Integer loanProductCounter;

    @SerializedName("multiDisburseLoan")
    Boolean multiDisburseLoan;

    @SerializedName("canDefineInstallmentAmount")
    Boolean canDefineInstallmentAmount;

    @SerializedName("canDisburse")
    Boolean canDisburse;

    @SerializedName("canUseForTopup")
    Boolean canUseForTopup;

    @SerializedName("isTopup")
    Boolean isTopup;

    @SerializedName("closureLoanId")
    Integer closureLoanId;

    @SerializedName("inArrears")
    Boolean inArrears;

    @SerializedName("isNPA")
    Boolean isNPA;

    @SerializedName("daysInMonthType")
    DaysInMonthType daysInMonthType;

    @SerializedName("daysInYearType")
    DaysInYearType daysInYearType;

    @SerializedName("isInterestRecalculationEnabled")
    Boolean isInterestRecalculationEnabled;

    @SerializedName("interestRecalculationData")
    InterestRecalculationData interestRecalculationData;

    @SerializedName("createStandingInstructionAtDisbursement")
    Boolean createStandingInstructionAtDisbursement;

    @SerializedName("isVariableInstallmentsAllowed")
    Boolean isVariableInstallmentsAllowed;

    @SerializedName("minimumGap")
    Integer minimumGap;

    @SerializedName("maximumGap")
    Integer maximumGap;

    @SerializedName("transactions")
    private List<Transaction> transactions;

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientAccountNo() {
        return clientAccountNo;
    }

    public void setClientAccountNo(String clientAccountNo) {
        this.clientAccountNo = clientAccountNo;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getClientOfficeId() {
        return clientOfficeId;
    }

    public void setClientOfficeId(Integer clientOfficeId) {
        this.clientOfficeId = clientOfficeId;
    }

    public Integer getLoanProductId() {
        return loanProductId;
    }

    public void setLoanProductId(Integer loanProductId) {
        this.loanProductId = loanProductId;
    }

    public String getLoanProductName() {
        return loanProductName;
    }

    public void setLoanProductName(String loanProductName) {
        this.loanProductName = loanProductName;
    }

    public Boolean getLoanProductLinkedToFloatingRate() {
        return isLoanProductLinkedToFloatingRate;
    }

    public void setLoanProductLinkedToFloatingRate(Boolean loanProductLinkedToFloatingRate) {
        isLoanProductLinkedToFloatingRate = loanProductLinkedToFloatingRate;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getPrincipal() {
        return principal;
    }

    public void setPrincipal(Double principal) {
        this.principal = principal;
    }

    public Double getApprovedPrincipal() {
        return approvedPrincipal;
    }

    public void setApprovedPrincipal(Double approvedPrincipal) {
        this.approvedPrincipal = approvedPrincipal;
    }

    public Double getProposedPrincipal() {
        return proposedPrincipal;
    }

    public void setProposedPrincipal(Double proposedPrincipal) {
        this.proposedPrincipal = proposedPrincipal;
    }

    public Integer getTermFrequency() {
        return termFrequency;
    }

    public void setTermFrequency(Integer termFrequency) {
        this.termFrequency = termFrequency;
    }

    public TermPeriodFrequencyType getTermPeriodFrequencyType() {
        return termPeriodFrequencyType;
    }

    public void setTermPeriodFrequencyType(
            TermPeriodFrequencyType termPeriodFrequencyType) {
        this.termPeriodFrequencyType = termPeriodFrequencyType;
    }

    public Integer getNumberOfRepayments() {
        return numberOfRepayments;
    }

    public void setNumberOfRepayments(Integer numberOfRepayments) {
        this.numberOfRepayments = numberOfRepayments;
    }

    public Integer getRepaymentEvery() {
        return repaymentEvery;
    }

    public void setRepaymentEvery(Integer repaymentEvery) {
        this.repaymentEvery = repaymentEvery;
    }

    public RepaymentFrequencyType getRepaymentFrequencyType() {
        return repaymentFrequencyType;
    }

    public void setRepaymentFrequencyType(
            RepaymentFrequencyType repaymentFrequencyType) {
        this.repaymentFrequencyType = repaymentFrequencyType;
    }

    public Integer getInterestRatePerPeriod() {
        return interestRatePerPeriod;
    }

    public void setInterestRatePerPeriod(Integer interestRatePerPeriod) {
        this.interestRatePerPeriod = interestRatePerPeriod;
    }

    public InterestRateFrequencyType getInterestRateFrequencyType() {
        return interestRateFrequencyType;
    }

    public void setInterestRateFrequencyType(
            InterestRateFrequencyType interestRateFrequencyType) {
        this.interestRateFrequencyType = interestRateFrequencyType;
    }

    public Boolean getFloatingInterestRate() {
        return isFloatingInterestRate;
    }

    public void setFloatingInterestRate(Boolean floatingInterestRate) {
        isFloatingInterestRate = floatingInterestRate;
    }

    public AmortizationType getAmortizationType() {
        return amortizationType;
    }

    public void setAmortizationType(
            AmortizationType amortizationType) {
        this.amortizationType = amortizationType;
    }

    public InterestType getInterestType() {
        return interestType;
    }

    public void setInterestType(InterestType interestType) {
        this.interestType = interestType;
    }

    public InterestCalculationPeriodType getInterestCalculationPeriodType() {
        return interestCalculationPeriodType;
    }

    public void setInterestCalculationPeriodType(
            InterestCalculationPeriodType interestCalculationPeriodType) {
        this.interestCalculationPeriodType = interestCalculationPeriodType;
    }

    public Boolean getAllowPartialPeriodInterestCalcualtion() {
        return allowPartialPeriodInterestCalcualtion;
    }

    public void setAllowPartialPeriodInterestCalcualtion(
            Boolean allowPartialPeriodInterestCalcualtion) {
        this.allowPartialPeriodInterestCalcualtion = allowPartialPeriodInterestCalcualtion;
    }

    public Integer getTransactionProcessingStrategyId() {
        return transactionProcessingStrategyId;
    }

    public void setTransactionProcessingStrategyId(Integer transactionProcessingStrategyId) {
        this.transactionProcessingStrategyId = transactionProcessingStrategyId;
    }

    public String getTransactionProcessingStrategyName() {
        return transactionProcessingStrategyName;
    }

    public void setTransactionProcessingStrategyName(String transactionProcessingStrategyName) {
        this.transactionProcessingStrategyName = transactionProcessingStrategyName;
    }

    public Boolean getSyncDisbursementWithMeeting() {
        return syncDisbursementWithMeeting;
    }

    public void setSyncDisbursementWithMeeting(Boolean syncDisbursementWithMeeting) {
        this.syncDisbursementWithMeeting = syncDisbursementWithMeeting;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public RepaymentSchedule getRepaymentSchedule() {
        return repaymentSchedule;
    }

    public void setRepaymentSchedule(
            RepaymentSchedule repaymentSchedule) {
        this.repaymentSchedule = repaymentSchedule;
    }

    public Double getFeeChargesAtDisbursementCharged() {
        return feeChargesAtDisbursementCharged;
    }

    public void setFeeChargesAtDisbursementCharged(Double feeChargesAtDisbursementCharged) {
        this.feeChargesAtDisbursementCharged = feeChargesAtDisbursementCharged;
    }

    public Integer getLoanProductCounter() {
        return loanProductCounter;
    }

    public void setLoanProductCounter(Integer loanProductCounter) {
        this.loanProductCounter = loanProductCounter;
    }

    public Boolean getMultiDisburseLoan() {
        return multiDisburseLoan;
    }

    public void setMultiDisburseLoan(Boolean multiDisburseLoan) {
        this.multiDisburseLoan = multiDisburseLoan;
    }

    public Boolean getCanDefineInstallmentAmount() {
        return canDefineInstallmentAmount;
    }

    public void setCanDefineInstallmentAmount(Boolean canDefineInstallmentAmount) {
        this.canDefineInstallmentAmount = canDefineInstallmentAmount;
    }

    public Boolean getCanDisburse() {
        return canDisburse;
    }

    public void setCanDisburse(Boolean canDisburse) {
        this.canDisburse = canDisburse;
    }

    public Boolean getCanUseForTopup() {
        return canUseForTopup;
    }

    public void setCanUseForTopup(Boolean canUseForTopup) {
        this.canUseForTopup = canUseForTopup;
    }

    public Boolean getTopup() {
        return isTopup;
    }

    public void setTopup(Boolean topup) {
        isTopup = topup;
    }

    public Integer getClosureLoanId() {
        return closureLoanId;
    }

    public void setClosureLoanId(Integer closureLoanId) {
        this.closureLoanId = closureLoanId;
    }

    public Boolean getInArrears() {
        return inArrears;
    }

    public void setInArrears(Boolean inArrears) {
        this.inArrears = inArrears;
    }

    public Boolean getNpa() {
        return isNPA;
    }

    public void setNpa(Boolean npa) {
        isNPA = npa;
    }

    public DaysInMonthType getDaysInMonthType() {
        return daysInMonthType;
    }

    public void setDaysInMonthType(
            DaysInMonthType daysInMonthType) {
        this.daysInMonthType = daysInMonthType;
    }

    public DaysInYearType getDaysInYearType() {
        return daysInYearType;
    }

    public void setDaysInYearType(
            DaysInYearType daysInYearType) {
        this.daysInYearType = daysInYearType;
    }

    public Boolean getInterestRecalculationEnabled() {
        return isInterestRecalculationEnabled;
    }

    public void setInterestRecalculationEnabled(Boolean interestRecalculationEnabled) {
        isInterestRecalculationEnabled = interestRecalculationEnabled;
    }

    public InterestRecalculationData getInterestRecalculationData() {
        return interestRecalculationData;
    }

    public void setInterestRecalculationData(
            InterestRecalculationData interestRecalculationData) {
        this.interestRecalculationData = interestRecalculationData;
    }

    public Boolean getCreateStandingInstructionAtDisbursement() {
        return createStandingInstructionAtDisbursement;
    }

    public void setCreateStandingInstructionAtDisbursement(
            Boolean createStandingInstructionAtDisbursement) {
        this.createStandingInstructionAtDisbursement = createStandingInstructionAtDisbursement;
    }

    public Boolean getVariableInstallmentsAllowed() {
        return isVariableInstallmentsAllowed;
    }

    public void setVariableInstallmentsAllowed(Boolean variableInstallmentsAllowed) {
        isVariableInstallmentsAllowed = variableInstallmentsAllowed;
    }

    public Integer getMinimumGap() {
        return minimumGap;
    }

    public void setMinimumGap(Integer minimumGap) {
        this.minimumGap = minimumGap;
    }

    public Integer getMaximumGap() {
        return maximumGap;
    }

    public void setMaximumGap(Integer maximumGap) {
        this.maximumGap = maximumGap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.accountNo);
        dest.writeString(this.externalId);
        dest.writeParcelable(this.status, flags);
        dest.writeValue(this.clientId);
        dest.writeString(this.clientAccountNo);
        dest.writeString(this.clientName);
        dest.writeValue(this.clientOfficeId);
        dest.writeValue(this.loanProductId);
        dest.writeString(this.loanProductName);
        dest.writeValue(this.isLoanProductLinkedToFloatingRate);
        dest.writeParcelable(this.loanType, flags);
        dest.writeParcelable(this.currency, flags);
        dest.writeValue(this.principal);
        dest.writeValue(this.approvedPrincipal);
        dest.writeValue(this.proposedPrincipal);
        dest.writeValue(this.termFrequency);
        dest.writeParcelable(this.termPeriodFrequencyType, flags);
        dest.writeValue(this.numberOfRepayments);
        dest.writeValue(this.repaymentEvery);
        dest.writeParcelable(this.repaymentFrequencyType, flags);
        dest.writeValue(this.interestRatePerPeriod);
        dest.writeParcelable(this.interestRateFrequencyType, flags);
        dest.writeValue(this.isFloatingInterestRate);
        dest.writeParcelable(this.amortizationType, flags);
        dest.writeParcelable(this.interestType, flags);
        dest.writeParcelable(this.interestCalculationPeriodType, flags);
        dest.writeValue(this.allowPartialPeriodInterestCalcualtion);
        dest.writeValue(this.transactionProcessingStrategyId);
        dest.writeString(this.transactionProcessingStrategyName);
        dest.writeValue(this.syncDisbursementWithMeeting);
        dest.writeParcelable(this.timeline, flags);
        dest.writeParcelable(this.summary, flags);
        dest.writeParcelable(this.repaymentSchedule, flags);
        dest.writeValue(this.feeChargesAtDisbursementCharged);
        dest.writeValue(this.loanProductCounter);
        dest.writeValue(this.multiDisburseLoan);
        dest.writeValue(this.canDefineInstallmentAmount);
        dest.writeValue(this.canDisburse);
        dest.writeValue(this.canUseForTopup);
        dest.writeValue(this.isTopup);
        dest.writeValue(this.closureLoanId);
        dest.writeValue(this.inArrears);
        dest.writeValue(this.isNPA);
        dest.writeParcelable(this.daysInMonthType, flags);
        dest.writeParcelable(this.daysInYearType, flags);
        dest.writeValue(this.isInterestRecalculationEnabled);
        dest.writeParcelable(this.interestRecalculationData, flags);
        dest.writeValue(this.createStandingInstructionAtDisbursement);
        dest.writeValue(this.isVariableInstallmentsAllowed);
        dest.writeValue(this.minimumGap);
        dest.writeValue(this.maximumGap);
    }

    public LoanWithAssociations() {
    }

    protected LoanWithAssociations(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.accountNo = in.readString();
        this.externalId = in.readString();
        this.status = in.readParcelable(Status.class.getClassLoader());
        this.clientId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.clientAccountNo = in.readString();
        this.clientName = in.readString();
        this.clientOfficeId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.loanProductId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.loanProductName = in.readString();
        this.isLoanProductLinkedToFloatingRate = (Boolean) in.readValue(
                Boolean.class.getClassLoader());
        this.loanType = in.readParcelable(LoanType.class.getClassLoader());
        this.currency = in.readParcelable(Currency.class.getClassLoader());
        this.principal = (Double) in.readValue(Double.class.getClassLoader());
        this.approvedPrincipal = (Double) in.readValue(Double.class.getClassLoader());
        this.proposedPrincipal = (Double) in.readValue(Double.class.getClassLoader());
        this.termFrequency = (Integer) in.readValue(Integer.class.getClassLoader());
        this.termPeriodFrequencyType = in.readParcelable(
                TermPeriodFrequencyType.class.getClassLoader());
        this.numberOfRepayments = (Integer) in.readValue(Integer.class.getClassLoader());
        this.repaymentEvery = (Integer) in.readValue(Integer.class.getClassLoader());
        this.repaymentFrequencyType = in.readParcelable(
                RepaymentFrequencyType.class.getClassLoader());
        this.interestRatePerPeriod = (Integer) in.readValue(Integer.class.getClassLoader());
        this.interestRateFrequencyType = in.readParcelable(
                InterestRateFrequencyType.class.getClassLoader());
        this.isFloatingInterestRate = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.amortizationType = in.readParcelable(AmortizationType.class.getClassLoader());
        this.interestType = in.readParcelable(InterestType.class.getClassLoader());
        this.interestCalculationPeriodType = in.readParcelable(
                InterestCalculationPeriodType.class.getClassLoader());
        this.allowPartialPeriodInterestCalcualtion = (Boolean) in.readValue(
                Boolean.class.getClassLoader());
        this.transactionProcessingStrategyId = (Integer) in.readValue(
                Integer.class.getClassLoader());
        this.transactionProcessingStrategyName = in.readString();
        this.syncDisbursementWithMeeting = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.timeline = in.readParcelable(Timeline.class.getClassLoader());
        this.summary = in.readParcelable(Summary.class.getClassLoader());
        this.repaymentSchedule = in.readParcelable(RepaymentSchedule.class.getClassLoader());
        this.feeChargesAtDisbursementCharged = (Double) in.readValue(Double.class.getClassLoader());
        this.loanProductCounter = (Integer) in.readValue(Integer.class.getClassLoader());
        this.multiDisburseLoan = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.canDefineInstallmentAmount = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.canDisburse = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.canUseForTopup = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isTopup = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.closureLoanId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.inArrears = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isNPA = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.daysInMonthType = in.readParcelable(DaysInMonthType.class.getClassLoader());
        this.daysInYearType = in.readParcelable(DaysInYearType.class.getClassLoader());
        this.isInterestRecalculationEnabled = (Boolean) in.readValue(
                Boolean.class.getClassLoader());
        this.interestRecalculationData = in.readParcelable(
                InterestRecalculationData.class.getClassLoader());
        this.createStandingInstructionAtDisbursement = (Boolean) in.readValue(
                Boolean.class.getClassLoader());
        this.isVariableInstallmentsAllowed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.minimumGap = (Integer) in.readValue(Integer.class.getClassLoader());
        this.maximumGap = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<LoanWithAssociations> CREATOR =
            new Parcelable.Creator<LoanWithAssociations>() {
                @Override
                public LoanWithAssociations createFromParcel(Parcel source) {
                    return new LoanWithAssociations(source);
                }

                @Override
                public LoanWithAssociations[] newArray(int size) {
                    return new LoanWithAssociations[size];
                }
            };
}