package org.mifos.selfserviceapp.models.templates.loans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.mifos.selfserviceapp.models.accounts.loan.AmortizationType;
import org.mifos.selfserviceapp.models.accounts.loan.DaysInMonthType;
import org.mifos.selfserviceapp.models.accounts.loan.DaysInYearType;
import org.mifos.selfserviceapp.models.accounts.loan.InterestCalculationPeriodType;
import org.mifos.selfserviceapp.models.accounts.loan.InterestRateFrequencyType;
import org.mifos.selfserviceapp.models.accounts.loan.InterestType;
import org.mifos.selfserviceapp.models.accounts.loan.RepaymentFrequencyType;
import org.mifos.selfserviceapp.models.accounts.loan.TermPeriodFrequencyType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajan Maurya on 15/07/16.
 */
public class LoanTemplate implements Parcelable {

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

    @SerializedName("fundId")
    Integer fundId;

    @SerializedName("fundName")
    String fundName;

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
    Double interestRatePerPeriod;

    @SerializedName("interestRateFrequencyType")
    InterestRateFrequencyType interestRateFrequencyType;

    @SerializedName("annualInterestRate")
    Double annualInterestRate;

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

    @SerializedName("graceOnArrearsAgeing")
    Integer graceOnArrearsAgeing;

    @SerializedName("timeline")
    Timeline timeline;

    @SerializedName("productOptions")
    List<ProductOptions> productOptions = new ArrayList<>();

    @SerializedName("loanOfficerOptions")
    List<LoanOfficerOptions> loanOfficerOptions = new ArrayList<>();

    @SerializedName("loanPurposeOptions")
    List<LoanPurposeOptions> loanPurposeOptions = new ArrayList<>();

    @SerializedName("fundOptions")
    List<FundOptions> fundOptions = new ArrayList<>();

    @SerializedName("termFrequencyTypeOptions")
    List<TermFrequencyTypeOptions> termFrequencyTypeOptions = new ArrayList<>();

    @SerializedName("repaymentFrequencyTypeOptions")
    List<RepaymentFrequencyTypeOptions> repaymentFrequencyTypeOptions = new ArrayList<>();

    @SerializedName("repaymentFrequencyNthDayTypeOptions")
    List<RepaymentFrequencyNthDayTypeOptions> repaymentFrequencyNthDayTypeOptions =
            new ArrayList<>();

    @SerializedName("repaymentFrequencyDaysOfWeekTypeOptions")
    List<RepaymentFrequencyDaysOfWeekTypeOptions> repaymentFrequencyDaysOfWeekTypeOptions =
            new ArrayList<>();

    @SerializedName("interestRateFrequencyTypeOptions")
    List<InterestRateFrequencyTypeOptions> interestRateFrequencyTypeOptions = new ArrayList<>();

    @SerializedName("amortizationTypeOptions")
    List<AmortizationTypeOptions> amortizationTypeOptions = new ArrayList<>();

    @SerializedName("interestTypeOptions")
    List<InterestTypeOptions> interestTypeOptions = new ArrayList<>();

    @SerializedName("interestCalculationPeriodTypeOptions")
    List<InterestCalculationPeriodType> interestCalculationPeriodTypeOptions = new ArrayList<>();

    @SerializedName("transactionProcessingStrategyOptions")
    List<TransactionProcessingStrategyOptions> transactionProcessingStrategyOptions =
            new ArrayList<>();

    @SerializedName("chargeOptions")
    List<ChargeOptions> chargeOptions = new ArrayList<>();

    @SerializedName("loanCollateralOptions")
    List<LoanCollateralOptions> loanCollateralOptions = new ArrayList<>();

    @SerializedName("multiDisburseLoan")
    Boolean multiDisburseLoan;

    @SerializedName("canDefineInstallmentAmount")
    Boolean canDefineInstallmentAmount;

    @SerializedName("canDisburse")
    Boolean canDisburse;

    @SerializedName("product")
    Product product;

    @SerializedName("daysInMonthType")
    DaysInMonthType daysInMonthType;

    @SerializedName("daysInYearType")
    DaysInYearType daysInYearType;

    @SerializedName("isInterestRecalculationEnabled")
    Boolean isInterestRecalculationEnabled;

    @SerializedName("isVariableInstallmentsAllowed")
    Boolean isVariableInstallmentsAllowed;

    @SerializedName("minimumGap")
    Integer minimumGap;

    @SerializedName("maximumGap")
    Integer maximumGap;

    @SerializedName("accountLinkingOptions")
    List<AccountLinkingOptions> accountLinkingOptions = new ArrayList<>();

    public List<AccountLinkingOptions> getAccountLinkingOptions() {
        return accountLinkingOptions;
    }

    public void setAccountLinkingOptions(List<AccountLinkingOptions> accountLinkingOptions) {
        this.accountLinkingOptions = accountLinkingOptions;
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

    public void setTermPeriodFrequencyType(TermPeriodFrequencyType termPeriodFrequencyType) {
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

    public void setRepaymentFrequencyType(RepaymentFrequencyType repaymentFrequencyType) {
        this.repaymentFrequencyType = repaymentFrequencyType;
    }

    public Double getInterestRatePerPeriod() {
        return interestRatePerPeriod;
    }

    public void setInterestRatePerPeriod(Double interestRatePerPeriod) {
        this.interestRatePerPeriod = interestRatePerPeriod;
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

    public Boolean getFloatingInterestRate() {
        return isFloatingInterestRate;
    }

    public void setFloatingInterestRate(Boolean floatingInterestRate) {
        isFloatingInterestRate = floatingInterestRate;
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

    public Integer getGraceOnArrearsAgeing() {
        return graceOnArrearsAgeing;
    }

    public void setGraceOnArrearsAgeing(Integer graceOnArrearsAgeing) {
        this.graceOnArrearsAgeing = graceOnArrearsAgeing;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public List<ProductOptions> getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(List<ProductOptions> productOptions) {
        this.productOptions = productOptions;
    }

    public List<LoanOfficerOptions> getLoanOfficerOptions() {
        return loanOfficerOptions;
    }

    public void setLoanOfficerOptions(List<LoanOfficerOptions> loanOfficerOptions) {
        this.loanOfficerOptions = loanOfficerOptions;
    }

    public List<LoanPurposeOptions> getLoanPurposeOptions() {
        return loanPurposeOptions;
    }

    public void setLoanPurposeOptions(List<LoanPurposeOptions> loanPurposeOptions) {
        this.loanPurposeOptions = loanPurposeOptions;
    }

    public List<FundOptions> getFundOptions() {
        return fundOptions;
    }

    public void setFundOptions(List<FundOptions> fundOptions) {
        this.fundOptions = fundOptions;
    }

    public List<TermFrequencyTypeOptions> getTermFrequencyTypeOptions() {
        return termFrequencyTypeOptions;
    }

    public void setTermFrequencyTypeOptions(List<TermFrequencyTypeOptions>
                                                    termFrequencyTypeOptions) {
        this.termFrequencyTypeOptions = termFrequencyTypeOptions;
    }

    public List<RepaymentFrequencyTypeOptions> getRepaymentFrequencyTypeOptions() {
        return repaymentFrequencyTypeOptions;
    }

    public void setRepaymentFrequencyTypeOptions(List<RepaymentFrequencyTypeOptions>
                                                         repaymentFrequencyTypeOptions) {
        this.repaymentFrequencyTypeOptions = repaymentFrequencyTypeOptions;
    }

    public List<RepaymentFrequencyNthDayTypeOptions> getRepaymentFrequencyNthDayTypeOptions() {
        return repaymentFrequencyNthDayTypeOptions;
    }

    public void setRepaymentFrequencyNthDayTypeOptions(
            List<RepaymentFrequencyNthDayTypeOptions> repaymentFrequencyNthDayTypeOptions) {
        this.repaymentFrequencyNthDayTypeOptions = repaymentFrequencyNthDayTypeOptions;
    }

    public List<RepaymentFrequencyDaysOfWeekTypeOptions>
            getRepaymentFrequencyDaysOfWeekTypeOptions() {
        return repaymentFrequencyDaysOfWeekTypeOptions;
    }

    public void setRepaymentFrequencyDaysOfWeekTypeOptions(
            List<RepaymentFrequencyDaysOfWeekTypeOptions> repaymentFrequencyDaysOfWeekTypeOptions) {
        this.repaymentFrequencyDaysOfWeekTypeOptions = repaymentFrequencyDaysOfWeekTypeOptions;
    }

    public List<InterestRateFrequencyTypeOptions> getInterestRateFrequencyTypeOptions() {
        return interestRateFrequencyTypeOptions;
    }

    public void setInterestRateFrequencyTypeOptions(List<InterestRateFrequencyTypeOptions>
                                                            interestRateFrequencyTypeOptions) {
        this.interestRateFrequencyTypeOptions = interestRateFrequencyTypeOptions;
    }

    public List<AmortizationTypeOptions> getAmortizationTypeOptions() {
        return amortizationTypeOptions;
    }

    public void setAmortizationTypeOptions(List<AmortizationTypeOptions> amortizationTypeOptions) {
        this.amortizationTypeOptions = amortizationTypeOptions;
    }

    public List<InterestTypeOptions> getInterestTypeOptions() {
        return interestTypeOptions;
    }

    public void setInterestTypeOptions(List<InterestTypeOptions> interestTypeOptions) {
        this.interestTypeOptions = interestTypeOptions;
    }

    public List<InterestCalculationPeriodType> getInterestCalculationPeriodTypeOptions() {
        return interestCalculationPeriodTypeOptions;
    }

    public void setInterestCalculationPeriodTypeOptions(
            List<InterestCalculationPeriodType> interestCalculationPeriodTypeOptions) {
        this.interestCalculationPeriodTypeOptions = interestCalculationPeriodTypeOptions;
    }

    public List<TransactionProcessingStrategyOptions> getTransactionProcessingStrategyOptions() {
        return transactionProcessingStrategyOptions;
    }

    public void setTransactionProcessingStrategyOptions(
            List<TransactionProcessingStrategyOptions> transactionProcessingStrategyOptions) {
        this.transactionProcessingStrategyOptions = transactionProcessingStrategyOptions;
    }

    public List<ChargeOptions> getChargeOptions() {
        return chargeOptions;
    }

    public void setChargeOptions(List<ChargeOptions> chargeOptions) {
        this.chargeOptions = chargeOptions;
    }

    public List<LoanCollateralOptions> getLoanCollateralOptions() {
        return loanCollateralOptions;
    }

    public void setLoanCollateralOptions(List<LoanCollateralOptions> loanCollateralOptions) {
        this.loanCollateralOptions = loanCollateralOptions;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    /**
     * Required to set default value to the Fund spinner
     *
     * @param fundId The value received from the Template for that particular loanProduct
     * @return Returns the index of the fundOption list where the specified fundId is located
     */
    public int getFundNameFromId(int fundId) {
        for (int i = 0; i < fundOptions.size(); i++) {
            if (fundOptions.get(i).getId() == fundId) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.clientId);
        dest.writeString(this.clientAccountNo);
        dest.writeString(this.clientName);
        dest.writeValue(this.clientOfficeId);
        dest.writeValue(this.loanProductId);
        dest.writeString(this.loanProductName);
        dest.writeValue(this.isLoanProductLinkedToFloatingRate);
        dest.writeValue(this.fundId);
        dest.writeString(this.fundName);
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
        dest.writeValue(this.annualInterestRate);
        dest.writeValue(this.isFloatingInterestRate);
        dest.writeParcelable(this.amortizationType, flags);
        dest.writeParcelable(this.interestType, flags);
        dest.writeParcelable(this.interestCalculationPeriodType, flags);
        dest.writeValue(this.allowPartialPeriodInterestCalcualtion);
        dest.writeValue(this.transactionProcessingStrategyId);
        dest.writeValue(this.graceOnArrearsAgeing);
        dest.writeParcelable(this.timeline, flags);
        dest.writeTypedList(this.productOptions);
        dest.writeTypedList(this.loanOfficerOptions);
        dest.writeTypedList(this.loanPurposeOptions);
        dest.writeTypedList(this.fundOptions);
        dest.writeTypedList(this.termFrequencyTypeOptions);
        dest.writeTypedList(this.repaymentFrequencyTypeOptions);
        dest.writeTypedList(this.repaymentFrequencyNthDayTypeOptions);
        dest.writeTypedList(this.repaymentFrequencyDaysOfWeekTypeOptions);
        dest.writeTypedList(this.interestRateFrequencyTypeOptions);
        dest.writeTypedList(this.amortizationTypeOptions);
        dest.writeTypedList(this.interestTypeOptions);
        dest.writeTypedList(this.interestCalculationPeriodTypeOptions);
        dest.writeTypedList(this.transactionProcessingStrategyOptions);
        dest.writeTypedList(this.chargeOptions);
        dest.writeTypedList(this.loanCollateralOptions);
        dest.writeValue(this.multiDisburseLoan);
        dest.writeValue(this.canDefineInstallmentAmount);
        dest.writeValue(this.canDisburse);
        dest.writeParcelable(this.product, flags);
        dest.writeParcelable(this.daysInMonthType, flags);
        dest.writeParcelable(this.daysInYearType, flags);
        dest.writeValue(this.isInterestRecalculationEnabled);
        dest.writeValue(this.isVariableInstallmentsAllowed);
        dest.writeValue(this.minimumGap);
        dest.writeValue(this.maximumGap);
        dest.writeTypedList(this.accountLinkingOptions);
    }

    public LoanTemplate() {
    }

    protected LoanTemplate(Parcel in) {
        this.clientId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.clientAccountNo = in.readString();
        this.clientName = in.readString();
        this.clientOfficeId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.loanProductId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.loanProductName = in.readString();
        this.isLoanProductLinkedToFloatingRate = (Boolean) in.readValue(Boolean.class
                .getClassLoader());
        this.fundId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fundName = in.readString();
        this.currency = in.readParcelable(Currency.class.getClassLoader());
        this.principal = (Double) in.readValue(Double.class.getClassLoader());
        this.approvedPrincipal = (Double) in.readValue(Double.class.getClassLoader());
        this.proposedPrincipal = (Double) in.readValue(Double.class.getClassLoader());
        this.termFrequency = (Integer) in.readValue(Integer.class.getClassLoader());
        this.termPeriodFrequencyType = in.readParcelable(TermPeriodFrequencyType.class
                .getClassLoader());
        this.numberOfRepayments = (Integer) in.readValue(Integer.class.getClassLoader());
        this.repaymentEvery = (Integer) in.readValue(Integer.class.getClassLoader());
        this.repaymentFrequencyType = in.readParcelable(RepaymentFrequencyType.class
                .getClassLoader());
        this.interestRatePerPeriod = (Double) in.readValue(Double.class.getClassLoader());
        this.interestRateFrequencyType = in.readParcelable(InterestRateFrequencyType.class
                .getClassLoader());
        this.annualInterestRate = (Double) in.readValue(Double.class.getClassLoader());
        this.isFloatingInterestRate = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.amortizationType = in.readParcelable(AmortizationType.class.getClassLoader());
        this.interestType = in.readParcelable(InterestType.class.getClassLoader());
        this.interestCalculationPeriodType = in.readParcelable(InterestCalculationPeriodType
                .class.getClassLoader());
        this.allowPartialPeriodInterestCalcualtion = (Boolean) in.readValue(Boolean.class
                .getClassLoader());
        this.transactionProcessingStrategyId = (Integer) in.readValue(Integer.class
                .getClassLoader());
        this.graceOnArrearsAgeing = (Integer) in.readValue(Integer.class.getClassLoader());
        this.timeline = in.readParcelable(Timeline.class.getClassLoader());
        this.productOptions = in.createTypedArrayList(ProductOptions.CREATOR);
        this.loanOfficerOptions = in.createTypedArrayList(LoanOfficerOptions.CREATOR);
        this.loanPurposeOptions = in.createTypedArrayList(LoanPurposeOptions.CREATOR);
        this.fundOptions = in.createTypedArrayList(FundOptions.CREATOR);
        this.termFrequencyTypeOptions = in.createTypedArrayList(TermFrequencyTypeOptions.CREATOR);
        this.repaymentFrequencyTypeOptions = in.createTypedArrayList
                (RepaymentFrequencyTypeOptions.CREATOR);
        this.repaymentFrequencyNthDayTypeOptions = in.createTypedArrayList
                (RepaymentFrequencyNthDayTypeOptions.CREATOR);
        this.repaymentFrequencyDaysOfWeekTypeOptions = in.createTypedArrayList
                (RepaymentFrequencyDaysOfWeekTypeOptions.CREATOR);
        this.interestRateFrequencyTypeOptions = in.createTypedArrayList
                (InterestRateFrequencyTypeOptions.CREATOR);
        this.amortizationTypeOptions = in.createTypedArrayList(AmortizationTypeOptions.CREATOR);
        this.interestTypeOptions = in.createTypedArrayList(InterestTypeOptions.CREATOR);
        this.interestCalculationPeriodTypeOptions = in.createTypedArrayList
                (InterestCalculationPeriodType.CREATOR);
        this.transactionProcessingStrategyOptions = in.createTypedArrayList
                (TransactionProcessingStrategyOptions.CREATOR);
        this.chargeOptions = in.createTypedArrayList(ChargeOptions.CREATOR);
        this.accountLinkingOptions = in.createTypedArrayList(AccountLinkingOptions.CREATOR);
        this.loanCollateralOptions = in.createTypedArrayList(LoanCollateralOptions.CREATOR);
        this.multiDisburseLoan = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.canDefineInstallmentAmount = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.canDisburse = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.product = in.readParcelable(Product.class.getClassLoader());
        this.daysInMonthType = in.readParcelable(DaysInMonthType.class.getClassLoader());
        this.daysInYearType = in.readParcelable(DaysInYearType.class.getClassLoader());
        this.isInterestRecalculationEnabled = (Boolean) in.readValue(Boolean.class
                .getClassLoader());
        this.isVariableInstallmentsAllowed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.minimumGap = (Integer) in.readValue(Integer.class.getClassLoader());
        this.maximumGap = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<LoanTemplate> CREATOR =
            new Creator<LoanTemplate>() {
        @Override
        public LoanTemplate createFromParcel(Parcel source) {
            return new LoanTemplate(source);
        }

        @Override
        public LoanTemplate[] newArray(int size) {
            return new LoanTemplate[size];
        }
    };
}
