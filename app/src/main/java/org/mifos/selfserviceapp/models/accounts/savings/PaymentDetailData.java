package org.mifos.selfserviceapp.models.accounts.savings;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajan Maurya on 05/03/17.
 */

public class PaymentDetailData implements Parcelable {

    @SerializedName("id")
    Integer id;

    @SerializedName("paymentType")
    PaymentType paymentType;

    @SerializedName("accountNumber")
    String accountNumber;

    @SerializedName("checkNumber")
    String checkNumber;

    @SerializedName("routingCode")
    String routingCode;

    @SerializedName("receiptNumber")
    String receiptNumber;

    @SerializedName("bankNumber")
    String bankNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getRoutingCode() {
        return routingCode;
    }

    public void setRoutingCode(String routingCode) {
        this.routingCode = routingCode;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeParcelable(this.paymentType, flags);
        dest.writeString(this.accountNumber);
        dest.writeString(this.checkNumber);
        dest.writeString(this.routingCode);
        dest.writeString(this.receiptNumber);
        dest.writeString(this.bankNumber);
    }

    public PaymentDetailData() {
    }

    protected PaymentDetailData(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.paymentType = in.readParcelable(PaymentType.class.getClassLoader());
        this.accountNumber = in.readString();
        this.checkNumber = in.readString();
        this.routingCode = in.readString();
        this.receiptNumber = in.readString();
        this.bankNumber = in.readString();
    }

    public static final Parcelable.Creator<PaymentDetailData> CREATOR =
            new Parcelable.Creator<PaymentDetailData>() {
                @Override
                public PaymentDetailData createFromParcel(Parcel source) {
                    return new PaymentDetailData(source);
                }

                @Override
                public PaymentDetailData[] newArray(int size) {
                    return new PaymentDetailData[size];
                }
            };
}
