package org.mifos.mobilebanking.models.templates.loans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.mifos.mobilebanking.models.accounts.loan.AmortizationType;
import org.mifos.mobilebanking.models.accounts.loan.DaysInMonthType;
import org.mifos.mobilebanking.models.accounts.loan.DaysInYearType;
import org.mifos.mobilebanking.models.accounts.loan.InterestCalculationPeriodType;
import org.mifos.mobilebanking.models.accounts.loan.InterestRateFrequencyType;
import org.mifos.mobilebanking.models.accounts.loan.InterestRecalculationData;
import org.mifos.mobilebanking.models.accounts.loan.InterestType;
import org.mifos.mobilebanking.models.accounts.loan.RepaymentFrequencyType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajan Maurya on 16/07/16.
 */
public class Product implements Parcelable {

    @SerializedName("id")
    Integer id;

    @SerializedName("name")
    String name;

    @SerializedName("shortName")
    String shortName;

    @SerializedName("fundId")
    Integer fundId;

    @SerializedName("fundName")
    String fundName;

    @SerializedName("includeInBorrowerCycle")
    Boolean includeInBorrowerCycle;

    @SerializedName("useBorrowerCycle")
    Boolean useBorrowerCycle;

    @SerializedName("startDate")
    List<Integer> startDate;

    @SerializedName("status")
    String status;

    @SerializedName("currency")
    Currency currency;

    @SerializedName("principal")
    Double principal;

    @SerializedName("minPrincipal")
    Double minPrincipal;

    @SerializedName("maxPrincipal")
    Double maxPrincipal;

    @SerializedName("numberOfRepayments")
    Integer numberOfRepayments;

    @SerializedName("minNumberOfRepayments")
    Integer minNumberOfRepayments;

    @SerializedName("maxNumberOfRepayments")
    Integer maxNumberOfRepayments;

    @SerializedName("repaymentEvery")
    Integer repaymentEvery;

    @SerializedName("repaymentFrequencyType")
    RepaymentFrequencyType repaymentFrequencyType;

    @SerializedName("interestRatePerPeriod")
    Double interestRatePerPeriod;

    @SerializedName("minInterestRatePerPeriod")
    Double minInterestRatePerPeriod;

    @SerializedName("maxInterestRatePerPeriod")
    Double maxInterestRatePerPeriod;

    @SerializedName("interestRateFrequencyType")
    InterestRateFrequencyType interestRateFrequencyType;

    @SerializedName("annualInterestRate")
    Double annualInterestRate;

    @SerializedName("isLinkedToFloatingInterestRates")
    Boolean isLinkedToFloatingInterestRates;

    @SerializedName("isFloatingInterestRateCalculationAllowed")
    Boolean isFloatingInterestRateCalculationAllowed;

    @SerializedName("allowVariableInstallments")
    Boolean allowVariableInstallments;

    @SerializedName("minimumGap")
    Double minimumGap;

    @SerializedName("maximumGap")
    Double maximumGap;

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

    @SerializedName("graceOnArrearsAgeing")
    Integer graceOnArrearsAgeing;

    @SerializedName("overdueDaysForNPA")
    Integer overdueDaysForNPA;

    @SerializedName("daysInMonthType")
    DaysInMonthType daysInMonthType;

    @SerializedName("daysInYearType")
    DaysInYearType daysInYearType;

    @SerializedName("isInterestRecalculationEnabled")
    Boolean isInterestRecalculationEnabled;

    @SerializedName("interestRecalculationData")
    InterestRecalculationData interestRecalculationData;

    @SerializedName("canDefineInstallmentAmount")
    Boolean canDefineInstallmentAmount;

    @SerializedName("accountingRule")
    AccountingRule accountingRule;

    @SerializedName("multiDisburseLoan")
    Boolean multiDisburseLoan;

    @SerializedName("maxTrancheCount")
    Integer maxTrancheCount;

    @SerializedName("principalThresholdForLastInstallment")
    Integer principalThresholdForLastInstallment;

    @SerializedName("holdGuaranteeFunds")
    Boolean holdGuaranteeFunds;

    @SerializedName("accountMovesOutOfNPAOnlyOnArrearsCompletion")
    Boolean accountMovesOutOfNPAOnlyOnArrearsCompletion;

    @SerializedName("allowAttributeOverrides")
    AllowAttributeOverrides allowAttributeOverrides;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getFundId() {
        return fundId;
    }

    public void setFundId(Integer fundId) {
        this.fundId = fundId;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public Boolean getIncludeInBorrowerCycle() {
        return includeInBorrowerCycle;
    }

    public void setIncludeInBorrowerCycle(Boolean includeInBorrowerCycle) {
        this.includeInBorrowerCycle = includeInBorrowerCycle;
    }

    public Boolean getUseBorrowerCycle() {
        return useBorrowerCycle;
    }

    public void setUseBorrowerCycle(Boolean useBorrowerCycle) {
        this.useBorrowerCycle = useBorrowerCycle;
    }

    public List<Integer> getStartDate() {
        return startDate;
    }

    public void setStartDate(List<Integer> startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Double getMinPrincipal() {
        return minPrincipal;
    }

    public void setMinPrincipal(Double minPrincipal) {
        this.minPrincipal = minPrincipal;
    }

    public Double getMaxPrincipal() {
        return maxPrincipal;
    }

    public void setMaxPrincipal(Double maxPrincipal) {
        this.maxPrincipal = maxPrincipal;
    }

    public Integer getNumberOfRepayments() {
        return numberOfRepayments;
    }

    public void setNumberOfRepayments(Integer numberOfRepayments) {
        this.numberOfRepayments = numberOfRepayments;
    }

    public Integer getMinNumberOfRepayments() {
        return minNumberOfRepayments;
    }

    public void setMinNumberOfRepayments(Integer minNumberOfRepayments) {
        this.minNumberOfRepayments = minNumberOfRepayments;
    }

    public Integer getMaxNumberOfRepayments() {
        return maxNumberOfRepayments;
    }

    public void setMaxNumberOfRepayments(Integer maxNumberOfRepayments) {
        this.maxNumberOfRepayments = maxNumberOfRepayments;
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

    public void setRepaymentFrequencyType(RepaymentFrequencyType repaymentFrequencyType) {
        this.repaymentFrequencyType = repaymentFrequencyType;
    }

    public Double getInterestRatePerPeriod() {
        return interestRatePerPeriod;
    }

    public void setInterestRatePerPeriod(Double interestRatePerPeriod) {
        this.interestRatePerPeriod = interestRatePerPeriod;
    }

    public Double getMinInterestRatePerPeriod() {
        return minInterestRatePerPeriod;
    }

    public void setMinInterestRatePerPeriod(Double minInterestRatePerPeriod) {
        this.minInterestRatePerPeriod = minInterestRatePerPeriod;
    }

    public Double getMaxInterestRatePerPeriod() {
        return maxInterestRatePerPeriod;
    }

    public void setMaxInterestRatePerPeriod(Double maxInterestRatePerPeriod) {
        this.maxInterestRatePerPeriod = maxInterestRatePerPeriod;
    }

    public InterestRateFrequencyType getInterestRateFrequencyType() {
        return interestRateFrequencyType;
    }

    public void setInterestRateFrequencyType(InterestRateFrequencyType interestRateFrequencyType) {
        this.interestRateFrequencyType = interestRateFrequencyType;
    }

    public Double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(Double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public Boolean getLinkedToFloatingInterestRates() {
        return isLinkedToFloatingInterestRates;
    }

    public void setLinkedToFloatingInterestRates(Boolean linkedToFloatingInterestRates) {
        isLinkedToFloatingInterestRates = linkedToFloatingInterestRates;
    }

    public Boolean getFloatingInterestRateCalculationAllowed() {
        return isFloatingInterestRateCalculationAllowed;
    }

    public void setFloatingInterestRateCalculationAllowed(
            Boolean floatingInterestRateCalculationAllowed) {
        isFloatingInterestRateCalculationAllowed = floatingInterestRateCalculationAllowed;
    }

    public Boolean getAllowVariableInstallments() {
        return allowVariableInstallments;
    }

    public void setAllowVariableInstallments(Boolean allowVariableInstallments) {
        this.allowVariableInstallments = allowVariableInstallments;
    }

    public Double getMinimumGap() {
        return minimumGap;
    }

    public void setMinimumGap(Double minimumGap) {
        this.minimumGap = minimumGap;
    }

    public Double getMaximumGap() {
        return maximumGap;
    }

    public void setMaximumGap(Double maximumGap) {
        this.maximumGap = maximumGap;
    }

    public AmortizationType getAmortizationType() {
        return amortizationType;
    }

    public void setAmortizationType(AmortizationType amortizationType) {
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

    public void setInterestCalculationPeriodType(InterestCalculationPeriodType
                                                         interestCalculationPeriodType) {
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

    public Integer getGraceOnArrearsAgeing() {
        return graceOnArrearsAgeing;
    }

    public void setGraceOnArrearsAgeing(Integer graceOnArrearsAgeing) {
        this.graceOnArrearsAgeing = graceOnArrearsAgeing;
    }

    public Integer getOverdueDaysForNPA() {
        return overdueDaysForNPA;
    }

    public void setOverdueDaysForNPA(Integer overdueDaysForNPA) {
        this.overdueDaysForNPA = overdueDaysForNPA;
    }

    public DaysInMonthType getDaysInMonthType() {
        return daysInMonthType;
    }

    public void setDaysInMonthType(DaysInMonthType daysInMonthType) {
        this.daysInMonthType = daysInMonthType;
    }

    public DaysInYearType getDaysInYearType() {
        return daysInYearType;
    }

    public void setDaysInYearType(DaysInYearType daysInYearType) {
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

    public void setInterestRecalculationData(InterestRecalculationData interestRecalculationData) {
        this.interestRecalculationData = interestRecalculationData;
    }

    public Boolean getCanDefineInstallmentAmount() {
        return canDefineInstallmentAmount;
    }

    public void setCanDefineInstallmentAmount(Boolean canDefineInstallmentAmount) {
        this.canDefineInstallmentAmount = canDefineInstallmentAmount;
    }

    public AccountingRule getAccountingRule() {
        return accountingRule;
    }

    public void setAccountingRule(AccountingRule accountingRule) {
        this.accountingRule = accountingRule;
    }

    public Boolean getMultiDisburseLoan() {
        return multiDisburseLoan;
    }

    public void setMultiDisburseLoan(Boolean multiDisburseLoan) {
        this.multiDisburseLoan = multiDisburseLoan;
    }

    public Integer getMaxTrancheCount() {
        return maxTrancheCount;
    }

    public void setMaxTrancheCount(Integer maxTrancheCount) {
        this.maxTrancheCount = maxTrancheCount;
    }

    public Integer getPrincipalThresholdForLastInstallment() {
        return principalThresholdForLastInstallment;
    }

    public void setPrincipalThresholdForLastInstallment(
            Integer principalThresholdForLastInstallment) {
        this.principalThresholdForLastInstallment = principalThresholdForLastInstallment;
    }

    public Boolean getHoldGuaranteeFunds() {
        return holdGuaranteeFunds;
    }

    public void setHoldGuaranteeFunds(Boolean holdGuaranteeFunds) {
        this.holdGuaranteeFunds = holdGuaranteeFunds;
    }

    public Boolean getAccountMovesOutOfNPAOnlyOnArrearsCompletion() {
        return accountMovesOutOfNPAOnlyOnArrearsCompletion;
    }

    public void setAccountMovesOutOfNPAOnlyOnArrearsCompletion(
            Boolean accountMovesOutOfNPAOnlyOnArrearsCompletion) {
        this.accountMovesOutOfNPAOnlyOnArrearsCompletion =
                accountMovesOutOfNPAOnlyOnArrearsCompletion;
    }

    public AllowAttributeOverrides getAllowAttributeOverrides() {
        return allowAttributeOverrides;
    }

    public void setAllowAttributeOverrides(AllowAttributeOverrides allowAttributeOverrides) {
        this.allowAttributeOverrides = allowAttributeOverrides;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.shortName);
        dest.writeValue(this.fundId);
        dest.writeString(this.fundName);
        dest.writeValue(this.includeInBorrowerCycle);
        dest.writeValue(this.useBorrowerCycle);
        dest.writeList(this.startDate);
        dest.writeString(this.status);
        dest.writeParcelable(this.currency, flags);
        dest.writeValue(this.principal);
        dest.writeValue(this.minPrincipal);
        dest.writeValue(this.maxPrincipal);
        dest.writeValue(this.numberOfRepayments);
        dest.writeValue(this.minNumberOfRepayments);
        dest.writeValue(this.maxNumberOfRepayments);
        dest.writeValue(this.repaymentEvery);
        dest.writeParcelable(this.repaymentFrequencyType, flags);
        dest.writeValue(this.interestRatePerPeriod);
        dest.writeValue(this.minInterestRatePerPeriod);
        dest.writeValue(this.maxInterestRatePerPeriod);
        dest.writeParcelable(this.interestRateFrequencyType, flags);
        dest.writeValue(this.annualInterestRate);
        dest.writeValue(this.isLinkedToFloatingInterestRates);
        dest.writeValue(this.isFloatingInterestRateCalculationAllowed);
        dest.writeValue(this.allowVariableInstallments);
        dest.writeValue(this.minimumGap);
        dest.writeValue(this.maximumGap);
        dest.writeParcelable(this.amortizationType, flags);
        dest.writeParcelable(this.interestType, flags);
        dest.writeParcelable(this.interestCalculationPeriodType, flags);
        dest.writeValue(this.allowPartialPeriodInterestCalcualtion);
        dest.writeValue(this.transactionProcessingStrategyId);
        dest.writeString(this.transactionProcessingStrategyName);
        dest.writeValue(this.graceOnArrearsAgeing);
        dest.writeValue(this.overdueDaysForNPA);
        dest.writeParcelable(this.daysInMonthType, flags);
        dest.writeParcelable(this.daysInYearType, flags);
        dest.writeValue(this.isInterestRecalculationEnabled);
        dest.writeParcelable(this.interestRecalculationData, flags);
        dest.writeValue(this.canDefineInstallmentAmount);
        dest.writeParcelable(this.accountingRule, flags);
        dest.writeValue(this.multiDisburseLoan);
        dest.writeValue(this.maxTrancheCount);
        dest.writeValue(this.principalThresholdForLastInstallment);
        dest.writeValue(this.holdGuaranteeFunds);
        dest.writeValue(this.accountMovesOutOfNPAOnlyOnArrearsCompletion);
        dest.writeParcelable(this.allowAttributeOverrides, flags);
    }

    public Product() {
    }

    protected Product(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.shortName = in.readString();
        this.fundId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fundName = in.readString();
        this.includeInBorrowerCycle = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.useBorrowerCycle = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.startDate = new ArrayList<Integer>();
        in.readList(this.startDate, Integer.class.getClassLoader());
        this.status = in.readString();
        this.currency = in.readParcelable(Currency.class.getClassLoader());
        this.principal = (Double) in.readValue(Double.class.getClassLoader());
        this.minPrincipal = (Double) in.readValue(Double.class.getClassLoader());
        this.maxPrincipal = (Double) in.readValue(Double.class.getClassLoader());
        this.numberOfRepayments = (Integer) in.readValue(Integer.class.getClassLoader());
        this.minNumberOfRepayments = (Integer) in.readValue(Integer.class.getClassLoader());
        this.maxNumberOfRepayments = (Integer) in.readValue(Integer.class.getClassLoader());
        this.repaymentEvery = (Integer) in.readValue(Integer.class.getClassLoader());
        this.repaymentFrequencyType = in.readParcelable(RepaymentFrequencyType.class
                .getClassLoader());
        this.interestRatePerPeriod = (Double) in.readValue(Double.class.getClassLoader());
        this.minInterestRatePerPeriod = (Double) in.readValue(Double.class.getClassLoader());
        this.maxInterestRatePerPeriod = (Double) in.readValue(Double.class.getClassLoader());
        this.interestRateFrequencyType = in.readParcelable(InterestRateFrequencyType.class
                .getClassLoader());
        this.annualInterestRate = (Double) in.readValue(Double.class.getClassLoader());
        this.isLinkedToFloatingInterestRates = (Boolean) in.readValue(Boolean.class
                .getClassLoader());
        this.isFloatingInterestRateCalculationAllowed = (Boolean) in.readValue(Boolean.class
                .getClassLoader());
        this.allowVariableInstallments = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.minimumGap = (Double) in.readValue(Double.class.getClassLoader());
        this.maximumGap = (Double) in.readValue(Double.class.getClassLoader());
        this.amortizationType = in.readParcelable(AmortizationType.class.getClassLoader());
        this.interestType = in.readParcelable(InterestType.class.getClassLoader());
        this.interestCalculationPeriodType = in.readParcelable(InterestCalculationPeriodType
                .class.getClassLoader());
        this.allowPartialPeriodInterestCalcualtion = (Boolean) in.readValue(Boolean.class
                .getClassLoader());
        this.transactionProcessingStrategyId = (Integer) in.readValue(Integer.class
                .getClassLoader());
        this.transactionProcessingStrategyName = in.readString();
        this.graceOnArrearsAgeing = (Integer) in.readValue(Integer.class.getClassLoader());
        this.overdueDaysForNPA = (Integer) in.readValue(Integer.class.getClassLoader());
        this.daysInMonthType = in.readParcelable(DaysInMonthType.class.getClassLoader());
        this.daysInYearType = in.readParcelable(DaysInYearType.class.getClassLoader());
        this.isInterestRecalculationEnabled = (Boolean) in.readValue(Boolean.class.getClassLoader
                ());
        this.interestRecalculationData = in.readParcelable(InterestRecalculationData.class
                .getClassLoader());
        this.canDefineInstallmentAmount = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.accountingRule = in.readParcelable(AccountingRule.class.getClassLoader());
        this.multiDisburseLoan = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.maxTrancheCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.principalThresholdForLastInstallment = (Integer) in.readValue(Integer.class
                .getClassLoader());
        this.holdGuaranteeFunds = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.accountMovesOutOfNPAOnlyOnArrearsCompletion = (Boolean) in.readValue(Boolean.class
                .getClassLoader());
        this.allowAttributeOverrides = in.readParcelable(AllowAttributeOverrides.class
                .getClassLoader());
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
