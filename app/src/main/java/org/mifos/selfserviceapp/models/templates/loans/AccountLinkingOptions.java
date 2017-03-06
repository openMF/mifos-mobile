package org.mifos.selfserviceapp.models.templates.loans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tarun on 12/16/2016.
 */
public class AccountLinkingOptions implements Parcelable {

    @SerializedName("accountNo")
    String accountNo;

    @SerializedName("clientId")
    Integer clientId;

    @SerializedName("clientName")
    String clientName;

    @SerializedName("currency")
    Currency currency;

    @SerializedName("fieldOfficerId")
    Integer fieldOfficerId;

    @SerializedName("id")
    Integer id;

    @SerializedName("productId")
    Integer productId;

    @SerializedName("productName")
    String productName;

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Integer getFieldOfficerId() {
        return fieldOfficerId;
    }

    public void setFieldOfficerId(Integer fieldOfficerId) {
        this.fieldOfficerId = fieldOfficerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountNo);
        dest.writeValue(this.clientId);
        dest.writeString(this.clientName);
        dest.writeParcelable(this.currency, flags);
        dest.writeValue(this.fieldOfficerId);
        dest.writeValue(this.id);
        dest.writeValue(this.productId);
        dest.writeString(this.productName);
    }

    public AccountLinkingOptions() {
    }

    protected AccountLinkingOptions(Parcel in) {
        this.accountNo = in.readString();
        this.clientId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.clientName = in.readString();
        this.currency = in.readParcelable(Currency.class.getClassLoader());
        this.fieldOfficerId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.productName = in.readString();
    }

    public static final Creator<AccountLinkingOptions>
            CREATOR = new Creator<AccountLinkingOptions>() {
                @Override
            public AccountLinkingOptions createFromParcel(Parcel source) {
                    return new AccountLinkingOptions(source);
                }

                @Override
            public AccountLinkingOptions[] newArray(int size) {
                    return new AccountLinkingOptions[size];
                }
            };
}
