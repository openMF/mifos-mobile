package org.mifos.mobilebanking.models.accounts.loan;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

public class Summary implements Parcelable {

    @SerializedName("principalDisbursed")
    private double principalDisbursed;

    @SerializedName("principalPaid")
    private double principalPaid;

    @SerializedName("interestCharged")
    private double interestCharged;

    @SerializedName("interestPaid")
    private double interestPaid;

    @SerializedName("feeChargesCharged")
    private double feeChargesCharged;

    @SerializedName("penaltyChargesCharged")
    private double penaltyChargesCharged;

    @SerializedName("penaltyChargesWaived")
    private double penaltyChargesWaived;

    @SerializedName("totalExpectedRepayment")
    private double totalExpectedRepayment;

    @SerializedName("interestWaived")
    private double interestWaived;

    @SerializedName("totalRepayment")
    private double totalRepayment;

    @SerializedName("feeChargesWaived")
    private double feeChargesWaived;

    @SerializedName("totalOutstanding")
    private double totalOutstanding;

    @SerializedName("overdueSinceDate")
    private List<Integer> overdueSinceDate = null;

    @SerializedName("currency")
    private Currency currency;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getOverdueSinceDate() {

        return String.format(Locale.ENGLISH , "%02d/%02d/%d" ,
                overdueSinceDate.get(2) , overdueSinceDate.get(1) , overdueSinceDate.get(0));
    }

    public void setOverdueSinceDate(List<Integer> overdueSinceDate) {
        this.overdueSinceDate = overdueSinceDate;
    }
    public double getTotalOutstanding() {
        return totalOutstanding;
    }

    public void setTotalOutstanding(double totalOutstanding) {
        this.totalOutstanding = totalOutstanding;
    }

    public double getFeeChargesWaived() {
        return feeChargesWaived;
    }

    public double getFeeChargesCharged() {
        return feeChargesCharged;
    }

    public void setFeeChargesCharged(double feeChargesCharged) {
        this.feeChargesCharged = feeChargesCharged;
    }

    public void setFeeChargesWaived(double feeChargesWaived) {
        this.feeChargesWaived = feeChargesWaived;
    }

    public double getInterestWaived() {
        return interestWaived;
    }

    public void setInterestWaived(double interestWaived) {
        this.interestWaived = interestWaived;
    }

    public double getTotalRepayment() {
        return totalRepayment;
    }

    public void setTotalRepayment(double totalRepayment) {
        this.totalRepayment = totalRepayment;
    }

    public double getTotalExpectedRepayment() {
        return totalExpectedRepayment;
    }

    public void setTotalExpectedRepayment(double totalExpectedRepayment) {
        this.totalExpectedRepayment = totalExpectedRepayment;
    }

    public double getPenaltyChargesCharged() {
        return penaltyChargesCharged;
    }

    public void setPenaltyChargesCharged(double penaltyChargesCharged) {
        this.penaltyChargesCharged = penaltyChargesCharged;
    }

    public double getPenaltyChargesWaived() {
        return penaltyChargesWaived;
    }

    public void setPenaltyChargesWaived(double penaltyChargesWaived) {
        this.penaltyChargesWaived = penaltyChargesWaived;
    }

    public double getPrincipalDisbursed() {
        return principalDisbursed;
    }

    public void setPrincipalDisbursed(double principalDisbursed) {
        this.principalDisbursed = principalDisbursed;
    }

    public double getPrincipalPaid() {
        return principalPaid;
    }

    public void setPrincipalPaid(double principalPaid) {
        this.principalPaid = principalPaid;
    }

    public double getInterestCharged() {
        return interestCharged;
    }

    public void setInterestCharged(double interestCharged) {
        this.interestCharged = interestCharged;
    }

    public double getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(double interestPaid) {
        this.interestPaid = interestPaid;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "principalDisbursed=" + principalDisbursed +
                ", principalPaid=" + principalPaid +
                ", interestCharged=" + interestCharged +
                ", interestPaid=" + interestPaid +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.principalDisbursed);
        dest.writeDouble(this.principalPaid);
        dest.writeDouble(this.interestCharged);
        dest.writeDouble(this.interestPaid);
    }

    public Summary() {
    }

    protected Summary(Parcel in) {
        this.principalDisbursed = in.readDouble();
        this.principalPaid = in.readDouble();
        this.interestCharged = in.readDouble();
        this.interestPaid = in.readDouble();
    }

    public static final Parcelable.Creator<Summary> CREATOR = new Parcelable.Creator<Summary>() {
        @Override
        public Summary createFromParcel(Parcel source) {
            return new Summary(source);
        }

        @Override
        public Summary[] newArray(int size) {
            return new Summary[size];
        }
    };
}