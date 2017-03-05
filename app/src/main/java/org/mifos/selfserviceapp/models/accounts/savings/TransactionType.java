package org.mifos.selfserviceapp.models.accounts.savings;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajan Maurya on 05/03/17.
 */

public class TransactionType implements Parcelable {

    @SerializedName("id")
    Integer id;

    @SerializedName("code")
    String code;

    @SerializedName("value")
    String value;

    @SerializedName("deposit")
    Boolean deposit;

    @SerializedName("dividendPayout")
    Boolean dividendPayout;

    @SerializedName("withdrawal")
    Boolean withdrawal;

    @SerializedName("interestPosting")
    Boolean interestPosting;

    @SerializedName("feeDeduction")
    Boolean feeDeduction;

    @SerializedName("initiateTransfer")
    Boolean initiateTransfer;

    @SerializedName("approveTransfer")
    Boolean approveTransfer;

    @SerializedName("withdrawTransfer")
    Boolean withdrawTransfer;

    @SerializedName("rejectTransfer")
    Boolean rejectTransfer;

    @SerializedName("overdraftInterest")
    Boolean overdraftInterest;

    @SerializedName("writtenoff")
    Boolean writtenoff;

    @SerializedName("overdraftFee")
    Boolean overdraftFee;

    @SerializedName("withholdTax")
    Boolean withholdTax;

    @SerializedName("escheat")
    Boolean escheat;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getDeposit() {
        return deposit;
    }

    public void setDeposit(Boolean deposit) {
        this.deposit = deposit;
    }

    public Boolean getDividendPayout() {
        return dividendPayout;
    }

    public void setDividendPayout(Boolean dividendPayout) {
        this.dividendPayout = dividendPayout;
    }

    public Boolean getWithdrawal() {
        return withdrawal;
    }

    public void setWithdrawal(Boolean withdrawal) {
        this.withdrawal = withdrawal;
    }

    public Boolean getInterestPosting() {
        return interestPosting;
    }

    public void setInterestPosting(Boolean interestPosting) {
        this.interestPosting = interestPosting;
    }

    public Boolean getFeeDeduction() {
        return feeDeduction;
    }

    public void setFeeDeduction(Boolean feeDeduction) {
        this.feeDeduction = feeDeduction;
    }

    public Boolean getInitiateTransfer() {
        return initiateTransfer;
    }

    public void setInitiateTransfer(Boolean initiateTransfer) {
        this.initiateTransfer = initiateTransfer;
    }

    public Boolean getApproveTransfer() {
        return approveTransfer;
    }

    public void setApproveTransfer(Boolean approveTransfer) {
        this.approveTransfer = approveTransfer;
    }

    public Boolean getWithdrawTransfer() {
        return withdrawTransfer;
    }

    public void setWithdrawTransfer(Boolean withdrawTransfer) {
        this.withdrawTransfer = withdrawTransfer;
    }

    public Boolean getRejectTransfer() {
        return rejectTransfer;
    }

    public void setRejectTransfer(Boolean rejectTransfer) {
        this.rejectTransfer = rejectTransfer;
    }

    public Boolean getOverdraftInterest() {
        return overdraftInterest;
    }

    public void setOverdraftInterest(Boolean overdraftInterest) {
        this.overdraftInterest = overdraftInterest;
    }

    public Boolean getWrittenoff() {
        return writtenoff;
    }

    public void setWrittenoff(Boolean writtenoff) {
        this.writtenoff = writtenoff;
    }

    public Boolean getOverdraftFee() {
        return overdraftFee;
    }

    public void setOverdraftFee(Boolean overdraftFee) {
        this.overdraftFee = overdraftFee;
    }

    public Boolean getWithholdTax() {
        return withholdTax;
    }

    public void setWithholdTax(Boolean withholdTax) {
        this.withholdTax = withholdTax;
    }

    public Boolean getEscheat() {
        return escheat;
    }

    public void setEscheat(Boolean escheat) {
        this.escheat = escheat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.code);
        dest.writeString(this.value);
        dest.writeValue(this.deposit);
        dest.writeValue(this.dividendPayout);
        dest.writeValue(this.withdrawal);
        dest.writeValue(this.interestPosting);
        dest.writeValue(this.feeDeduction);
        dest.writeValue(this.initiateTransfer);
        dest.writeValue(this.approveTransfer);
        dest.writeValue(this.withdrawTransfer);
        dest.writeValue(this.rejectTransfer);
        dest.writeValue(this.overdraftInterest);
        dest.writeValue(this.writtenoff);
        dest.writeValue(this.overdraftFee);
        dest.writeValue(this.withholdTax);
        dest.writeValue(this.escheat);
    }

    public TransactionType() {
    }

    protected TransactionType(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.code = in.readString();
        this.value = in.readString();
        this.deposit = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.dividendPayout = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.withdrawal = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.interestPosting = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.feeDeduction = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.initiateTransfer = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.approveTransfer = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.withdrawTransfer = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.rejectTransfer = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.overdraftInterest = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.writtenoff = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.overdraftFee = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.withholdTax = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.escheat = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<TransactionType> CREATOR =
            new Parcelable.Creator<TransactionType>() {
                @Override
                public TransactionType createFromParcel(Parcel source) {
                    return new TransactionType(source);
                }

                @Override
                public TransactionType[] newArray(int size) {
                    return new TransactionType[size];
                }
            };
}
