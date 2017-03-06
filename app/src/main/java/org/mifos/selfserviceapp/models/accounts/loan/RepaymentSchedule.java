package org.mifos.selfserviceapp.models.accounts.loan;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajan Maurya on 04/03/17.
 */

public class RepaymentSchedule implements Parcelable {

    @SerializedName("currency")
    Currency currency;

    @SerializedName("loanTermInDays")
    Integer loanTermInDays;

    @SerializedName("totalPrincipalDisbursed")
    Double totalPrincipalDisbursed;

    @SerializedName("totalPrincipalExpected")
    Double totalPrincipalExpected;

    @SerializedName("totalPrincipalPaid")
    Double totalPrincipalPaid;

    @SerializedName("totalInterestCharged")
    Double totalInterestCharged;

    @SerializedName("totalFeeChargesCharged")
    Double totalFeeChargesCharged;

    @SerializedName("totalPenaltyChargesCharged")
    Double totalPenaltyChargesCharged;

    @SerializedName("totalWaived")
    Double totalWaived;

    @SerializedName("totalWrittenOff")
    Double totalWrittenOff;

    @SerializedName("totalRepaymentExpected")
    Double totalRepaymentExpected;

    @SerializedName("totalRepayment")
    Double totalRepayment;

    @SerializedName("totalPaidInAdvance")
    Double totalPaidInAdvance;

    @SerializedName("totalPaidLate")
    Double totalPaidLate;

    @SerializedName("totalOutstanding")
    Double totalOutstanding;

    @SerializedName("periods")
    List<Periods> periods = new ArrayList<>();

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Integer getLoanTermInDays() {
        return loanTermInDays;
    }

    public void setLoanTermInDays(Integer loanTermInDays) {
        this.loanTermInDays = loanTermInDays;
    }

    public Double getTotalPrincipalDisbursed() {
        return totalPrincipalDisbursed;
    }

    public void setTotalPrincipalDisbursed(Double totalPrincipalDisbursed) {
        this.totalPrincipalDisbursed = totalPrincipalDisbursed;
    }

    public Double getTotalPrincipalExpected() {
        return totalPrincipalExpected;
    }

    public void setTotalPrincipalExpected(Double totalPrincipalExpected) {
        this.totalPrincipalExpected = totalPrincipalExpected;
    }

    public Double getTotalPrincipalPaid() {
        return totalPrincipalPaid;
    }

    public void setTotalPrincipalPaid(Double totalPrincipalPaid) {
        this.totalPrincipalPaid = totalPrincipalPaid;
    }

    public Double getTotalInterestCharged() {
        return totalInterestCharged;
    }

    public void setTotalInterestCharged(Double totalInterestCharged) {
        this.totalInterestCharged = totalInterestCharged;
    }

    public Double getTotalFeeChargesCharged() {
        return totalFeeChargesCharged;
    }

    public void setTotalFeeChargesCharged(Double totalFeeChargesCharged) {
        this.totalFeeChargesCharged = totalFeeChargesCharged;
    }

    public Double getTotalPenaltyChargesCharged() {
        return totalPenaltyChargesCharged;
    }

    public void setTotalPenaltyChargesCharged(Double totalPenaltyChargesCharged) {
        this.totalPenaltyChargesCharged = totalPenaltyChargesCharged;
    }

    public Double getTotalWaived() {
        return totalWaived;
    }

    public void setTotalWaived(Double totalWaived) {
        this.totalWaived = totalWaived;
    }

    public Double getTotalWrittenOff() {
        return totalWrittenOff;
    }

    public void setTotalWrittenOff(Double totalWrittenOff) {
        this.totalWrittenOff = totalWrittenOff;
    }

    public Double getTotalRepaymentExpected() {
        return totalRepaymentExpected;
    }

    public void setTotalRepaymentExpected(Double totalRepaymentExpected) {
        this.totalRepaymentExpected = totalRepaymentExpected;
    }

    public Double getTotalRepayment() {
        return totalRepayment;
    }

    public void setTotalRepayment(Double totalRepayment) {
        this.totalRepayment = totalRepayment;
    }

    public Double getTotalPaidInAdvance() {
        return totalPaidInAdvance;
    }

    public void setTotalPaidInAdvance(Double totalPaidInAdvance) {
        this.totalPaidInAdvance = totalPaidInAdvance;
    }

    public Double getTotalPaidLate() {
        return totalPaidLate;
    }

    public void setTotalPaidLate(Double totalPaidLate) {
        this.totalPaidLate = totalPaidLate;
    }

    public Double getTotalOutstanding() {
        return totalOutstanding;
    }

    public void setTotalOutstanding(Double totalOutstanding) {
        this.totalOutstanding = totalOutstanding;
    }

    public List<Periods> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Periods> periods) {
        this.periods = periods;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.currency, flags);
        dest.writeValue(this.loanTermInDays);
        dest.writeValue(this.totalPrincipalDisbursed);
        dest.writeValue(this.totalPrincipalExpected);
        dest.writeValue(this.totalPrincipalPaid);
        dest.writeValue(this.totalInterestCharged);
        dest.writeValue(this.totalFeeChargesCharged);
        dest.writeValue(this.totalPenaltyChargesCharged);
        dest.writeValue(this.totalWaived);
        dest.writeValue(this.totalWrittenOff);
        dest.writeValue(this.totalRepaymentExpected);
        dest.writeValue(this.totalRepayment);
        dest.writeValue(this.totalPaidInAdvance);
        dest.writeValue(this.totalPaidLate);
        dest.writeValue(this.totalOutstanding);
        dest.writeTypedList(this.periods);
    }

    public RepaymentSchedule() {
    }

    protected RepaymentSchedule(Parcel in) {
        this.currency = in.readParcelable(Currency.class.getClassLoader());
        this.loanTermInDays = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalPrincipalDisbursed = (Double) in.readValue(Double.class.getClassLoader());
        this.totalPrincipalExpected = (Double) in.readValue(Double.class.getClassLoader());
        this.totalPrincipalPaid = (Double) in.readValue(Double.class.getClassLoader());
        this.totalInterestCharged = (Double) in.readValue(Double.class.getClassLoader());
        this.totalFeeChargesCharged = (Double) in.readValue(Double.class.getClassLoader());
        this.totalPenaltyChargesCharged = (Double) in.readValue(Double.class.getClassLoader());
        this.totalWaived = (Double) in.readValue(Double.class.getClassLoader());
        this.totalWrittenOff = (Double) in.readValue(Double.class.getClassLoader());
        this.totalRepaymentExpected = (Double) in.readValue(Double.class.getClassLoader());
        this.totalRepayment = (Double) in.readValue(Double.class.getClassLoader());
        this.totalPaidInAdvance = (Double) in.readValue(Double.class.getClassLoader());
        this.totalPaidLate = (Double) in.readValue(Double.class.getClassLoader());
        this.totalOutstanding = (Double) in.readValue(Double.class.getClassLoader());
        this.periods = in.createTypedArrayList(Periods.CREATOR);
    }

    public static final Parcelable.Creator<RepaymentSchedule> CREATOR =
            new Parcelable.Creator<RepaymentSchedule>() {
                @Override
                public RepaymentSchedule createFromParcel(Parcel source) {
                    return new RepaymentSchedule(source);
                }

                @Override
                public RepaymentSchedule[] newArray(int size) {
                    return new RepaymentSchedule[size];
                }
            };
}