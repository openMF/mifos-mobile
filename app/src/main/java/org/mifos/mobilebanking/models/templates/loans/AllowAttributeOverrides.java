package org.mifos.mobilebanking.models.templates.loans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajan Maurya on 16/07/16.
 */
public class AllowAttributeOverrides implements Parcelable {

    @SerializedName("amortizationType")
    Boolean amortizationType;

    @SerializedName("interestType")
    Boolean interestType;

    @SerializedName("transactionProcessingStrategyId")
    Boolean transactionProcessingStrategyId;

    @SerializedName("interestCalculationPeriodType")
    Boolean interestCalculationPeriodType;

    @SerializedName("inArrearsTolerance")
    Boolean inArrearsTolerance;

    @SerializedName("repaymentEvery")
    Boolean repaymentEvery;

    @SerializedName("graceOnPrincipalAndInterestPayment")
    Boolean graceOnPrincipalAndInterestPayment;

    @SerializedName("graceOnArrearsAgeing")
    Boolean graceOnArrearsAgeing;

    public Boolean getAmortizationType() {
        return amortizationType;
    }

    public void setAmortizationType(Boolean amortizationType) {
        this.amortizationType = amortizationType;
    }

    public Boolean getInterestType() {
        return interestType;
    }

    public void setInterestType(Boolean interestType) {
        this.interestType = interestType;
    }

    public Boolean getTransactionProcessingStrategyId() {
        return transactionProcessingStrategyId;
    }

    public void setTransactionProcessingStrategyId(Boolean transactionProcessingStrategyId) {
        this.transactionProcessingStrategyId = transactionProcessingStrategyId;
    }

    public Boolean getInterestCalculationPeriodType() {
        return interestCalculationPeriodType;
    }

    public void setInterestCalculationPeriodType(Boolean interestCalculationPeriodType) {
        this.interestCalculationPeriodType = interestCalculationPeriodType;
    }

    public Boolean getInArrearsTolerance() {
        return inArrearsTolerance;
    }

    public void setInArrearsTolerance(Boolean inArrearsTolerance) {
        this.inArrearsTolerance = inArrearsTolerance;
    }

    public Boolean getRepaymentEvery() {
        return repaymentEvery;
    }

    public void setRepaymentEvery(Boolean repaymentEvery) {
        this.repaymentEvery = repaymentEvery;
    }

    public Boolean getGraceOnPrincipalAndInterestPayment() {
        return graceOnPrincipalAndInterestPayment;
    }

    public void setGraceOnPrincipalAndInterestPayment(Boolean graceOnPrincipalAndInterestPayment) {
        this.graceOnPrincipalAndInterestPayment = graceOnPrincipalAndInterestPayment;
    }

    public Boolean getGraceOnArrearsAgeing() {
        return graceOnArrearsAgeing;
    }

    public void setGraceOnArrearsAgeing(Boolean graceOnArrearsAgeing) {
        this.graceOnArrearsAgeing = graceOnArrearsAgeing;
    }

    @Override
    public String toString() {
        return "AllowAttributeOverrides{" +
                "amortizationType=" + amortizationType +
                ", interestType=" + interestType +
                ", transactionProcessingStrategyId=" + transactionProcessingStrategyId +
                ", interestCalculationPeriodType=" + interestCalculationPeriodType +
                ", inArrearsTolerance=" + inArrearsTolerance +
                ", repaymentEvery=" + repaymentEvery +
                ", graceOnPrincipalAndInterestPayment=" + graceOnPrincipalAndInterestPayment +
                ", graceOnArrearsAgeing=" + graceOnArrearsAgeing +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.amortizationType);
        dest.writeValue(this.interestType);
        dest.writeValue(this.transactionProcessingStrategyId);
        dest.writeValue(this.interestCalculationPeriodType);
        dest.writeValue(this.inArrearsTolerance);
        dest.writeValue(this.repaymentEvery);
        dest.writeValue(this.graceOnPrincipalAndInterestPayment);
        dest.writeValue(this.graceOnArrearsAgeing);
    }

    public AllowAttributeOverrides() {
    }

    protected AllowAttributeOverrides(Parcel in) {
        this.amortizationType = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.interestType = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.transactionProcessingStrategyId = (Boolean) in.readValue(Boolean.class
                .getClassLoader());
        this.interestCalculationPeriodType = (Boolean) in.readValue(Boolean.class
                .getClassLoader());
        this.inArrearsTolerance = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.repaymentEvery = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.graceOnPrincipalAndInterestPayment = (Boolean) in.readValue(Boolean.class
                .getClassLoader());
        this.graceOnArrearsAgeing = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<AllowAttributeOverrides> CREATOR =
            new Creator<AllowAttributeOverrides>() {
                @Override
                public AllowAttributeOverrides createFromParcel(Parcel source) {
                    return new AllowAttributeOverrides(source);
                }

                @Override
                public AllowAttributeOverrides[] newArray(int size) {
                    return new AllowAttributeOverrides[size];
                }
            };
}
