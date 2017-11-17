package org.mifos.mobilebanking.models.beneficary;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.mifos.mobilebanking.models.templates.account.AccountType;

/**
 * Created by dilpreet on 14/6/17.
 */

public class Beneficiary implements Parcelable {

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("officeName")
    private String officeName;

    @SerializedName("clientName")
    private String clientName;

    @SerializedName("accountType")
    private AccountType accountType;

    @SerializedName("accountNumber")
    private String accountNumber;

    @SerializedName("transferLimit")
    private Integer transferLimit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getTransferLimit() {
        return transferLimit;
    }

    public void setTransferLimit(Integer transferLimit) {
        this.transferLimit = transferLimit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.officeName);
        dest.writeString(this.clientName);
        dest.writeParcelable(this.accountType, flags);
        dest.writeString(this.accountNumber);
        dest.writeValue(this.transferLimit);
    }

    public Beneficiary() {
    }

    protected Beneficiary(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.officeName = in.readString();
        this.clientName = in.readString();
        this.accountType = in.readParcelable(AccountType.class.getClassLoader());
        this.accountNumber = in.readString();
        this.transferLimit = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Beneficiary> CREATOR =
            new Parcelable.Creator<Beneficiary>() {
        @Override
        public Beneficiary createFromParcel(Parcel source) {
            return new Beneficiary(source);
        }

        @Override
        public Beneficiary[] newArray(int size) {
            return new Beneficiary[size];
        }
    };
}
