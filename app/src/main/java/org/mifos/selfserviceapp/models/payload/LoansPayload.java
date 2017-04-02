/*
 * This project is licensed under the open source MPL V2.
 * See https://github.com/openMF/android-client/blob/master/LICENSE.md
 */
package org.mifos.selfserviceapp.models.payload;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nellyk on 2/20/2016.
 */
public class LoansPayload implements Parcelable {

    @SerializedName("clientId")
    Integer clientId;

    @SerializedName("productId")
    Integer productId;

    @SerializedName("principal")
    Double principal;

    @SerializedName("loanTermFrequency")
    Integer loanTermFrequency;

    @SerializedName("loanTermFrequencyType")
    Integer loanTermFrequencyType;

    @SerializedName("loanType")
    String loanType;

    @SerializedName("numberOfRepayments")
    Integer numberOfRepayments;

    @SerializedName("repaymentEvery")
    Integer repaymentEvery;

    @SerializedName("repaymentFrequencyType")
    Integer repaymentFrequencyType;

    @SerializedName("interestRatePerPeriod")
    Double interestRatePerPeriod;

    @SerializedName("amortizationType")
    Integer amortizationType;

    @SerializedName("interestType")
    Integer interestType;

    @SerializedName("interestCalculationPeriodType")
    Integer interestCalculationPeriodType;

    @SerializedName("transactionProcessingStrategyId")
    Integer transactionProcessingStrategyId;

    @SerializedName("expectedDisbursementDate")
    String expectedDisbursementDate;

    @SerializedName("submittedOnDate")
    String submittedOnDate;

    @SerializedName("linkAccountId")
    Integer linkAccountId;

    @SerializedName("loanPurposeId")
    Integer loanPurposeId;

    @SerializedName("maxOutstandingLoanBalance")
    Double maxOutstandingLoanBalance;

    String dateFormat = "dd MMMM yyyy";
    String locale = "en";

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Double getPrincipal() {
        return principal;
    }

    public void setPrincipal(Double principal) {
        this.principal = principal;
    }

    public Integer getLoanTermFrequency() {
        return loanTermFrequency;
    }

    public void setLoanTermFrequency(Integer loanTermFrequency) {
        this.loanTermFrequency = loanTermFrequency;
    }

    public Integer getLoanTermFrequencyType() {
        return loanTermFrequencyType;
    }

    public void setLoanTermFrequencyType(Integer loanTermFrequencyType) {
        this.loanTermFrequencyType = loanTermFrequencyType;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
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

    public Integer getRepaymentFrequencyType() {
        return repaymentFrequencyType;
    }

    public void setRepaymentFrequencyType(Integer repaymentFrequencyType) {
        this.repaymentFrequencyType = repaymentFrequencyType;
    }

    public Double getInterestRatePerPeriod() {
        return interestRatePerPeriod;
    }

    public void setInterestRatePerPeriod(Double interestRatePerPeriod) {
        this.interestRatePerPeriod = interestRatePerPeriod;
    }

    public Integer getAmortizationType() {
        return amortizationType;
    }

    public void setAmortizationType(Integer amortizationType) {
        this.amortizationType = amortizationType;
    }

    public Integer getInterestType() {
        return interestType;
    }

    public void setInterestType(Integer interestType) {
        this.interestType = interestType;
    }

    public Integer getInterestCalculationPeriodType() {
        return interestCalculationPeriodType;
    }

    public void setInterestCalculationPeriodType(Integer interestCalculationPeriodType) {
        this.interestCalculationPeriodType = interestCalculationPeriodType;
    }

    public Integer getTransactionProcessingStrategyId() {
        return transactionProcessingStrategyId;
    }

    public void setTransactionProcessingStrategyId(Integer transactionProcessingStrategyId) {
        this.transactionProcessingStrategyId = transactionProcessingStrategyId;
    }

    public String getExpectedDisbursementDate() {
        return expectedDisbursementDate;
    }

    public void setExpectedDisbursementDate(String expectedDisbursementDate) {
        this.expectedDisbursementDate = expectedDisbursementDate;
    }

    public String getSubmittedOnDate() {
        return submittedOnDate;
    }

    public void setSubmittedOnDate(String submittedOnDate) {
        this.submittedOnDate = submittedOnDate;
    }

    public Integer getLinkAccountId() {
        return linkAccountId;
    }

    public void setLinkAccountId(Integer linkAccountId) {
        this.linkAccountId = linkAccountId;
    }

    public Integer getLoanPurposeId() {
        return loanPurposeId;
    }

    public void setLoanPurposeId(Integer loanPurposeId) {
        this.loanPurposeId = loanPurposeId;
    }

    public Double getMaxOutstandingLoanBalance() {
        return maxOutstandingLoanBalance;
    }

    public void setMaxOutstandingLoanBalance(Double maxOutstandingLoanBalance) {
        this.maxOutstandingLoanBalance = maxOutstandingLoanBalance;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.clientId);
        dest.writeValue(this.productId);
        dest.writeValue(this.principal);
        dest.writeValue(this.loanTermFrequency);
        dest.writeValue(this.loanTermFrequencyType);
        dest.writeString(this.loanType);
        dest.writeValue(this.numberOfRepayments);
        dest.writeValue(this.repaymentEvery);
        dest.writeValue(this.repaymentFrequencyType);
        dest.writeValue(this.interestRatePerPeriod);
        dest.writeValue(this.amortizationType);
        dest.writeValue(this.interestType);
        dest.writeValue(this.interestCalculationPeriodType);
        dest.writeValue(this.transactionProcessingStrategyId);
        dest.writeString(this.expectedDisbursementDate);
        dest.writeString(this.submittedOnDate);
        dest.writeValue(this.linkAccountId);
        dest.writeValue(this.loanPurposeId);
        dest.writeValue(this.maxOutstandingLoanBalance);
        dest.writeString(this.dateFormat);
        dest.writeString(this.locale);
    }

    public LoansPayload() {
    }

    protected LoansPayload(Parcel in) {
        this.clientId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.principal = (Double) in.readValue(Double.class.getClassLoader());
        this.loanTermFrequency = (Integer) in.readValue(Integer.class.getClassLoader());
        this.loanTermFrequencyType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.loanType = in.readString();
        this.numberOfRepayments = (Integer) in.readValue(Integer.class.getClassLoader());
        this.repaymentEvery = (Integer) in.readValue(Integer.class.getClassLoader());
        this.repaymentFrequencyType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.interestRatePerPeriod = (Double) in.readValue(Double.class.getClassLoader());
        this.amortizationType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.interestType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.interestCalculationPeriodType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.transactionProcessingStrategyId = (Integer) in.readValue(
                Integer.class.getClassLoader());
        this.expectedDisbursementDate = in.readString();
        this.submittedOnDate = in.readString();
        this.linkAccountId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.loanPurposeId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.maxOutstandingLoanBalance = (Double) in.readValue(Double.class.getClassLoader());
        this.dateFormat = in.readString();
        this.locale = in.readString();
    }

    public static final Parcelable.Creator<LoansPayload> CREATOR =
            new Parcelable.Creator<LoansPayload>() {
                @Override
                public LoansPayload createFromParcel(Parcel source) {
                    return new LoansPayload(source);
                }

                @Override
                public LoansPayload[] newArray(int size) {
                    return new LoansPayload[size];
                }
            };
}
