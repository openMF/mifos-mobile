package org.mifos.selfserviceapp.models.accounts.savings;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajan Maurya on 05/03/17.
 */

public class Summary implements Parcelable {

    @SerializedName("currency")
    Currency currency;

    @SerializedName("totalDeposits")
    Double totalDeposits;

    @SerializedName("totalWithdrawals")
    Double totalWithdrawals;

    @SerializedName("totalInterestEarned")
    Double totalInterestEarned;

    @SerializedName("totalInterestPosted")
    Double totalInterestPosted;

    @SerializedName("accountBalance")
    Double accountBalance;

    @SerializedName("totalOverdraftInterestDerived")
    Double totalOverdraftInterestDerived;

    @SerializedName("interestNotPosted")
    Double interestNotPosted;

    @SerializedName("lastInterestCalculationDate")
    List<Integer> lastInterestCalculationDate;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getTotalDeposits() {
        return totalDeposits;
    }

    public void setTotalDeposits(Double totalDeposits) {
        this.totalDeposits = totalDeposits;
    }

    public Double getTotalWithdrawals() {
        return totalWithdrawals;
    }

    public void setTotalWithdrawals(Double totalWithdrawals) {
        this.totalWithdrawals = totalWithdrawals;
    }

    public Double getTotalInterestEarned() {
        return totalInterestEarned;
    }

    public void setTotalInterestEarned(Double totalInterestEarned) {
        this.totalInterestEarned = totalInterestEarned;
    }

    public Double getTotalInterestPosted() {
        return totalInterestPosted;
    }

    public void setTotalInterestPosted(Double totalInterestPosted) {
        this.totalInterestPosted = totalInterestPosted;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Double getTotalOverdraftInterestDerived() {
        return totalOverdraftInterestDerived;
    }

    public void setTotalOverdraftInterestDerived(Double totalOverdraftInterestDerived) {
        this.totalOverdraftInterestDerived = totalOverdraftInterestDerived;
    }

    public Double getInterestNotPosted() {
        return interestNotPosted;
    }

    public void setInterestNotPosted(Double interestNotPosted) {
        this.interestNotPosted = interestNotPosted;
    }

    public List<Integer> getLastInterestCalculationDate() {
        return lastInterestCalculationDate;
    }

    public void setLastInterestCalculationDate(List<Integer> lastInterestCalculationDate) {
        this.lastInterestCalculationDate = lastInterestCalculationDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.currency, flags);
        dest.writeValue(this.totalDeposits);
        dest.writeValue(this.totalWithdrawals);
        dest.writeValue(this.totalInterestEarned);
        dest.writeValue(this.totalInterestPosted);
        dest.writeValue(this.accountBalance);
        dest.writeValue(this.totalOverdraftInterestDerived);
        dest.writeValue(this.interestNotPosted);
        dest.writeList(this.lastInterestCalculationDate);
    }

    public Summary() {
    }

    protected Summary(Parcel in) {
        this.currency = in.readParcelable(Currency.class.getClassLoader());
        this.totalDeposits = (Double) in.readValue(Double.class.getClassLoader());
        this.totalWithdrawals = (Double) in.readValue(Double.class.getClassLoader());
        this.totalInterestEarned = (Double) in.readValue(Double.class.getClassLoader());
        this.totalInterestPosted = (Double) in.readValue(Double.class.getClassLoader());
        this.accountBalance = (Double) in.readValue(Double.class.getClassLoader());
        this.totalOverdraftInterestDerived = (Double) in.readValue(Double.class.getClassLoader());
        this.interestNotPosted = (Double) in.readValue(Double.class.getClassLoader());
        this.lastInterestCalculationDate = new ArrayList<Integer>();
        in.readList(this.lastInterestCalculationDate, Integer.class.getClassLoader());
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
