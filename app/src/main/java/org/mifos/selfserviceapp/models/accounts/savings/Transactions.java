package org.mifos.selfserviceapp.models.accounts.savings;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajan Maurya on 05/03/17.
 */

public class Transactions implements Parcelable {

    @SerializedName("id")
    Integer id;

    @SerializedName("transactionType")
    TransactionType transactionType;

    @SerializedName("accountId")
    Integer accountId;

    @SerializedName("accountNo")
    String accountNo;

    @SerializedName("date")
    List<Integer> date = new ArrayList<>();

    @SerializedName("currency")
    Currency currency;

    @SerializedName("paymentDetailData")
    PaymentDetailData paymentDetailData;

    @SerializedName("amount")
    Double amount;

    @SerializedName("runningBalance")
    Double runningBalance;

    @SerializedName("reversed")
    Boolean reversed;

    @SerializedName("submittedOnDate")
    List<Integer> submittedOnDate;

    @SerializedName("interestedPostedAsOn")
    Boolean interestedPostedAsOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(
            TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public List<Integer> getDate() {
        return date;
    }

    public void setDate(List<Integer> date) {
        this.date = date;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public PaymentDetailData getPaymentDetailData() {
        return paymentDetailData;
    }

    public void setPaymentDetailData(
            PaymentDetailData paymentDetailData) {
        this.paymentDetailData = paymentDetailData;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(Double runningBalance) {
        this.runningBalance = runningBalance;
    }

    public Boolean getReversed() {
        return reversed;
    }

    public void setReversed(Boolean reversed) {
        this.reversed = reversed;
    }

    public List<Integer> getSubmittedOnDate() {
        return submittedOnDate;
    }

    public void setSubmittedOnDate(List<Integer> submittedOnDate) {
        this.submittedOnDate = submittedOnDate;
    }

    public Boolean getInterestedPostedAsOn() {
        return interestedPostedAsOn;
    }

    public void setInterestedPostedAsOn(Boolean interestedPostedAsOn) {
        this.interestedPostedAsOn = interestedPostedAsOn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeParcelable(this.transactionType, flags);
        dest.writeValue(this.accountId);
        dest.writeString(this.accountNo);
        dest.writeList(this.date);
        dest.writeParcelable(this.currency, flags);
        dest.writeParcelable(this.paymentDetailData, flags);
        dest.writeValue(this.amount);
        dest.writeValue(this.runningBalance);
        dest.writeValue(this.reversed);
        dest.writeList(this.submittedOnDate);
        dest.writeValue(this.interestedPostedAsOn);
    }

    public Transactions() {
    }

    protected Transactions(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.transactionType = in.readParcelable(TransactionType.class.getClassLoader());
        this.accountId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.accountNo = in.readString();
        this.date = new ArrayList<Integer>();
        in.readList(this.date, Integer.class.getClassLoader());
        this.currency = in.readParcelable(Currency.class.getClassLoader());
        this.paymentDetailData = in.readParcelable(PaymentDetailData.class.getClassLoader());
        this.amount = (Double) in.readValue(Double.class.getClassLoader());
        this.runningBalance = (Double) in.readValue(Double.class.getClassLoader());
        this.reversed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.submittedOnDate = new ArrayList<Integer>();
        in.readList(this.submittedOnDate, Integer.class.getClassLoader());
        this.interestedPostedAsOn = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<Transactions> CREATOR =
            new Parcelable.Creator<Transactions>() {
                @Override
                public Transactions createFromParcel(Parcel source) {
                    return new Transactions(source);
                }

                @Override
                public Transactions[] newArray(int size) {
                    return new Transactions[size];
                }
            };
}
