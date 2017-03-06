package org.mifos.selfserviceapp.models.accounts.loan;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajan Maurya on 04/03/17.
 */

public class Periods implements Parcelable {

    @SerializedName("period")
    Integer period;

    @SerializedName("fromDate")
    List<Integer> fromDate = new ArrayList<>();

    @SerializedName("dueDate")
    List<Integer> dueDate = new ArrayList<>();

    @SerializedName("obligationsMetOnDate")
    List<Integer> obligationsMetOnDate = new ArrayList<>();

    @SerializedName("principalDisbursed")
    Double principalDisbursed;

    @SerializedName("complete")
    Boolean complete;

    @SerializedName("daysInPeriod")
    Integer daysInPeriod;

    @SerializedName("principalOriginalDue")
    Double principalOriginalDue;

    @SerializedName("principalDue")
    Double principalDue;

    @SerializedName("principalPaid")
    Double principalPaid;

    @SerializedName("principalWrittenOff")
    Double principalWrittenOff;

    @SerializedName("principalOutstanding")
    Double principalOutstanding;

    @SerializedName("principalLoanBalanceOutstanding")
    Double principalLoanBalanceOutstanding;

    @SerializedName("interestOriginalDue")
    Double interestOriginalDue;

    @SerializedName("interestDue")
    Double interestDue;

    @SerializedName("interestPaid")
    Double interestPaid;

    @SerializedName("interestWaived")
    Double interestWaived;

    @SerializedName("interestWrittenOff")
    Double interestWrittenOff;

    @SerializedName("interestOutstanding")
    Double interestOutstanding;

    @SerializedName("feeChargesDue")
    Double feeChargesDue;

    @SerializedName("feeChargesPaid")
    Double feeChargesPaid;

    @SerializedName("feeChargesWaived")
    Double feeChargesWaived;

    @SerializedName("feeChargesWrittenOff")
    Double feeChargesWrittenOff;

    @SerializedName("feeChargesOutstanding")
    Double feeChargesOutstanding;

    @SerializedName("penaltyChargesDue")
    Double penaltyChargesDue;

    @SerializedName("penaltyChargesPaid")
    Double penaltyChargesPaid;

    @SerializedName("penaltyChargesWaived")
    Double penaltyChargesWaived;

    @SerializedName("penaltyChargesWrittenOff")
    Double penaltyChargesWrittenOff;

    @SerializedName("penaltyChargesOutstanding")
    Double penaltyChargesOutstanding;

    @SerializedName("totalOriginalDueForPeriod")
    Double totalOriginalDueForPeriod;

    @SerializedName("totalDueForPeriod")
    Double totalDueForPeriod;

    @SerializedName("totalPaidForPeriod")
    Double totalPaidForPeriod;

    @SerializedName("totalPaidInAdvanceForPeriod")
    Double totalPaidInAdvanceForPeriod;

    @SerializedName("totalPaidLateForPeriod")
    Double totalPaidLateForPeriod;

    @SerializedName("totalWaivedForPeriod")
    Double totalWaivedForPeriod;

    @SerializedName("totalWrittenOffForPeriod")
    Double totalWrittenOffForPeriod;

    @SerializedName("totalOutstandingForPeriod")
    Double totalOutstandingForPeriod;

    @SerializedName("totalOverdue")
    Double totalOverdue;

    @SerializedName("totalActualCostOfLoanForPeriod")
    Double totalActualCostOfLoanForPeriod;

    @SerializedName("totalInstallmentAmountForPeriod")
    Double totalInstallmentAmountForPeriod;

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public List<Integer> getFromDate() {
        return fromDate;
    }

    public void setFromDate(List<Integer> fromDate) {
        this.fromDate = fromDate;
    }

    public List<Integer> getObligationsMetOnDate() {
        return obligationsMetOnDate;
    }

    public void setObligationsMetOnDate(List<Integer> obligationsMetOnDate) {
        this.obligationsMetOnDate = obligationsMetOnDate;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public Integer getDaysInPeriod() {
        return daysInPeriod;
    }

    public void setDaysInPeriod(Integer daysInPeriod) {
        this.daysInPeriod = daysInPeriod;
    }

    public Double getPrincipalOriginalDue() {
        return principalOriginalDue;
    }

    public void setPrincipalOriginalDue(Double principalOriginalDue) {
        this.principalOriginalDue = principalOriginalDue;
    }

    public Double getPrincipalDue() {
        return principalDue;
    }

    public void setPrincipalDue(Double principalDue) {
        this.principalDue = principalDue;
    }

    public Double getPrincipalPaid() {
        return principalPaid;
    }

    public void setPrincipalPaid(Double principalPaid) {
        this.principalPaid = principalPaid;
    }

    public Double getPrincipalWrittenOff() {
        return principalWrittenOff;
    }

    public void setPrincipalWrittenOff(Double principalWrittenOff) {
        this.principalWrittenOff = principalWrittenOff;
    }

    public Double getPrincipalOutstanding() {
        return principalOutstanding;
    }

    public void setPrincipalOutstanding(Double principalOutstanding) {
        this.principalOutstanding = principalOutstanding;
    }

    public Double getInterestOriginalDue() {
        return interestOriginalDue;
    }

    public void setInterestOriginalDue(Double interestOriginalDue) {
        this.interestOriginalDue = interestOriginalDue;
    }

    public Double getInterestDue() {
        return interestDue;
    }

    public void setInterestDue(Double interestDue) {
        this.interestDue = interestDue;
    }

    public Double getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(Double interestPaid) {
        this.interestPaid = interestPaid;
    }

    public Double getInterestWaived() {
        return interestWaived;
    }

    public void setInterestWaived(Double interestWaived) {
        this.interestWaived = interestWaived;
    }

    public Double getInterestWrittenOff() {
        return interestWrittenOff;
    }

    public void setInterestWrittenOff(Double interestWrittenOff) {
        this.interestWrittenOff = interestWrittenOff;
    }

    public Double getInterestOutstanding() {
        return interestOutstanding;
    }

    public void setInterestOutstanding(Double interestOutstanding) {
        this.interestOutstanding = interestOutstanding;
    }

    public Double getFeeChargesWaived() {
        return feeChargesWaived;
    }

    public void setFeeChargesWaived(Double feeChargesWaived) {
        this.feeChargesWaived = feeChargesWaived;
    }

    public Double getFeeChargesWrittenOff() {
        return feeChargesWrittenOff;
    }

    public void setFeeChargesWrittenOff(Double feeChargesWrittenOff) {
        this.feeChargesWrittenOff = feeChargesWrittenOff;
    }

    public Double getFeeChargesOutstanding() {
        return feeChargesOutstanding;
    }

    public void setFeeChargesOutstanding(Double feeChargesOutstanding) {
        this.feeChargesOutstanding = feeChargesOutstanding;
    }

    public Double getPenaltyChargesDue() {
        return penaltyChargesDue;
    }

    public void setPenaltyChargesDue(Double penaltyChargesDue) {
        this.penaltyChargesDue = penaltyChargesDue;
    }

    public Double getPenaltyChargesPaid() {
        return penaltyChargesPaid;
    }

    public void setPenaltyChargesPaid(Double penaltyChargesPaid) {
        this.penaltyChargesPaid = penaltyChargesPaid;
    }

    public Double getPenaltyChargesWaived() {
        return penaltyChargesWaived;
    }

    public void setPenaltyChargesWaived(Double penaltyChargesWaived) {
        this.penaltyChargesWaived = penaltyChargesWaived;
    }

    public Double getPenaltyChargesWrittenOff() {
        return penaltyChargesWrittenOff;
    }

    public void setPenaltyChargesWrittenOff(Double penaltyChargesWrittenOff) {
        this.penaltyChargesWrittenOff = penaltyChargesWrittenOff;
    }

    public Double getPenaltyChargesOutstanding() {
        return penaltyChargesOutstanding;
    }

    public void setPenaltyChargesOutstanding(Double penaltyChargesOutstanding) {
        this.penaltyChargesOutstanding = penaltyChargesOutstanding;
    }

    public Double getTotalPaidInAdvanceForPeriod() {
        return totalPaidInAdvanceForPeriod;
    }

    public void setTotalPaidInAdvanceForPeriod(Double totalPaidInAdvanceForPeriod) {
        this.totalPaidInAdvanceForPeriod = totalPaidInAdvanceForPeriod;
    }

    public Double getTotalPaidLateForPeriod() {
        return totalPaidLateForPeriod;
    }

    public void setTotalPaidLateForPeriod(Double totalPaidLateForPeriod) {
        this.totalPaidLateForPeriod = totalPaidLateForPeriod;
    }

    public Double getTotalWaivedForPeriod() {
        return totalWaivedForPeriod;
    }

    public void setTotalWaivedForPeriod(Double totalWaivedForPeriod) {
        this.totalWaivedForPeriod = totalWaivedForPeriod;
    }

    public Double getTotalWrittenOffForPeriod() {
        return totalWrittenOffForPeriod;
    }

    public void setTotalWrittenOffForPeriod(Double totalWrittenOffForPeriod) {
        this.totalWrittenOffForPeriod = totalWrittenOffForPeriod;
    }

    public Double getTotalOutstandingForPeriod() {
        return totalOutstandingForPeriod;
    }

    public void setTotalOutstandingForPeriod(Double totalOutstandingForPeriod) {
        this.totalOutstandingForPeriod = totalOutstandingForPeriod;
    }

    public Double getTotalOverdue() {
        return totalOverdue;
    }

    public void setTotalOverdue(Double totalOverdue) {
        this.totalOverdue = totalOverdue;
    }

    public Double getTotalInstallmentAmountForPeriod() {
        return totalInstallmentAmountForPeriod;
    }

    public void setTotalInstallmentAmountForPeriod(Double totalInstallmentAmountForPeriod) {
        this.totalInstallmentAmountForPeriod = totalInstallmentAmountForPeriod;
    }

    public static Creator<Periods> getCREATOR() {
        return CREATOR;
    }

    public List<Integer> getDueDate() {
        return dueDate;
    }

    public void setDueDate(List<Integer> dueDate) {
        this.dueDate = dueDate;
    }

    public Double getPrincipalDisbursed() {
        return principalDisbursed;
    }

    public void setPrincipalDisbursed(Double principalDisbursed) {
        this.principalDisbursed = principalDisbursed;
    }

    public Double getPrincipalLoanBalanceOutstanding() {
        return principalLoanBalanceOutstanding;
    }

    public void setPrincipalLoanBalanceOutstanding(Double principalLoanBalanceOutstanding) {
        this.principalLoanBalanceOutstanding = principalLoanBalanceOutstanding;
    }

    public Double getFeeChargesDue() {
        return feeChargesDue;
    }

    public void setFeeChargesDue(Double feeChargesDue) {
        this.feeChargesDue = feeChargesDue;
    }

    public Double getFeeChargesPaid() {
        return feeChargesPaid;
    }

    public void setFeeChargesPaid(Double feeChargesPaid) {
        this.feeChargesPaid = feeChargesPaid;
    }

    public Double getTotalOriginalDueForPeriod() {
        return totalOriginalDueForPeriod;
    }

    public void setTotalOriginalDueForPeriod(Double totalOriginalDueForPeriod) {
        this.totalOriginalDueForPeriod = totalOriginalDueForPeriod;
    }

    public Double getTotalDueForPeriod() {
        return totalDueForPeriod;
    }

    public void setTotalDueForPeriod(Double totalDueForPeriod) {
        this.totalDueForPeriod = totalDueForPeriod;
    }

    public Double getTotalPaidForPeriod() {
        return totalPaidForPeriod;
    }

    public void setTotalPaidForPeriod(Double totalPaidForPeriod) {
        this.totalPaidForPeriod = totalPaidForPeriod;
    }

    public Double getTotalActualCostOfLoanForPeriod() {
        return totalActualCostOfLoanForPeriod;
    }

    public void setTotalActualCostOfLoanForPeriod(Double totalActualCostOfLoanForPeriod) {
        this.totalActualCostOfLoanForPeriod = totalActualCostOfLoanForPeriod;
    }

    public Periods() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.period);
        dest.writeList(this.fromDate);
        dest.writeList(this.dueDate);
        dest.writeList(this.obligationsMetOnDate);
        dest.writeValue(this.principalDisbursed);
        dest.writeValue(this.complete);
        dest.writeValue(this.daysInPeriod);
        dest.writeValue(this.principalOriginalDue);
        dest.writeValue(this.principalDue);
        dest.writeValue(this.principalPaid);
        dest.writeValue(this.principalWrittenOff);
        dest.writeValue(this.principalOutstanding);
        dest.writeValue(this.principalLoanBalanceOutstanding);
        dest.writeValue(this.interestOriginalDue);
        dest.writeValue(this.interestDue);
        dest.writeValue(this.interestPaid);
        dest.writeValue(this.interestWaived);
        dest.writeValue(this.interestWrittenOff);
        dest.writeValue(this.interestOutstanding);
        dest.writeValue(this.feeChargesDue);
        dest.writeValue(this.feeChargesPaid);
        dest.writeValue(this.feeChargesWaived);
        dest.writeValue(this.feeChargesWrittenOff);
        dest.writeValue(this.feeChargesOutstanding);
        dest.writeValue(this.penaltyChargesDue);
        dest.writeValue(this.penaltyChargesPaid);
        dest.writeValue(this.penaltyChargesWaived);
        dest.writeValue(this.penaltyChargesWrittenOff);
        dest.writeValue(this.penaltyChargesOutstanding);
        dest.writeValue(this.totalOriginalDueForPeriod);
        dest.writeValue(this.totalDueForPeriod);
        dest.writeValue(this.totalPaidForPeriod);
        dest.writeValue(this.totalPaidInAdvanceForPeriod);
        dest.writeValue(this.totalPaidLateForPeriod);
        dest.writeValue(this.totalWaivedForPeriod);
        dest.writeValue(this.totalWrittenOffForPeriod);
        dest.writeValue(this.totalOutstandingForPeriod);
        dest.writeValue(this.totalOverdue);
        dest.writeValue(this.totalActualCostOfLoanForPeriod);
        dest.writeValue(this.totalInstallmentAmountForPeriod);
    }

    protected Periods(Parcel in) {
        this.period = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fromDate = new ArrayList<Integer>();
        in.readList(this.fromDate, Integer.class.getClassLoader());
        this.dueDate = new ArrayList<Integer>();
        in.readList(this.dueDate, Integer.class.getClassLoader());
        this.obligationsMetOnDate = new ArrayList<Integer>();
        in.readList(this.obligationsMetOnDate, Integer.class.getClassLoader());
        this.principalDisbursed = (Double) in.readValue(Double.class.getClassLoader());
        this.complete = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.daysInPeriod = (Integer) in.readValue(Integer.class.getClassLoader());
        this.principalOriginalDue = (Double) in.readValue(Double.class.getClassLoader());
        this.principalDue = (Double) in.readValue(Double.class.getClassLoader());
        this.principalPaid = (Double) in.readValue(Double.class.getClassLoader());
        this.principalWrittenOff = (Double) in.readValue(Double.class.getClassLoader());
        this.principalOutstanding = (Double) in.readValue(Double.class.getClassLoader());
        this.principalLoanBalanceOutstanding = (Double) in.readValue(Double.class.getClassLoader());
        this.interestOriginalDue = (Double) in.readValue(Double.class.getClassLoader());
        this.interestDue = (Double) in.readValue(Double.class.getClassLoader());
        this.interestPaid = (Double) in.readValue(Double.class.getClassLoader());
        this.interestWaived = (Double) in.readValue(Double.class.getClassLoader());
        this.interestWrittenOff = (Double) in.readValue(Double.class.getClassLoader());
        this.interestOutstanding = (Double) in.readValue(Double.class.getClassLoader());
        this.feeChargesDue = (Double) in.readValue(Double.class.getClassLoader());
        this.feeChargesPaid = (Double) in.readValue(Double.class.getClassLoader());
        this.feeChargesWaived = (Double) in.readValue(Double.class.getClassLoader());
        this.feeChargesWrittenOff = (Double) in.readValue(Double.class.getClassLoader());
        this.feeChargesOutstanding = (Double) in.readValue(Double.class.getClassLoader());
        this.penaltyChargesDue = (Double) in.readValue(Double.class.getClassLoader());
        this.penaltyChargesPaid = (Double) in.readValue(Double.class.getClassLoader());
        this.penaltyChargesWaived = (Double) in.readValue(Double.class.getClassLoader());
        this.penaltyChargesWrittenOff = (Double) in.readValue(Double.class.getClassLoader());
        this.penaltyChargesOutstanding = (Double) in.readValue(Double.class.getClassLoader());
        this.totalOriginalDueForPeriod = (Double) in.readValue(Double.class.getClassLoader());
        this.totalDueForPeriod = (Double) in.readValue(Double.class.getClassLoader());
        this.totalPaidForPeriod = (Double) in.readValue(Double.class.getClassLoader());
        this.totalPaidInAdvanceForPeriod = (Double) in.readValue(Double.class.getClassLoader());
        this.totalPaidLateForPeriod = (Double) in.readValue(Double.class.getClassLoader());
        this.totalWaivedForPeriod = (Double) in.readValue(Double.class.getClassLoader());
        this.totalWrittenOffForPeriod = (Double) in.readValue(Double.class.getClassLoader());
        this.totalOutstandingForPeriod = (Double) in.readValue(Double.class.getClassLoader());
        this.totalOverdue = (Double) in.readValue(Double.class.getClassLoader());
        this.totalActualCostOfLoanForPeriod = (Double) in.readValue(Double.class.getClassLoader());
        this.totalInstallmentAmountForPeriod = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Creator<Periods> CREATOR = new Creator<Periods>() {
        @Override
        public Periods createFromParcel(Parcel source) {
            return new Periods(source);
        }

        @Override
        public Periods[] newArray(int size) {
            return new Periods[size];
        }
    };
}