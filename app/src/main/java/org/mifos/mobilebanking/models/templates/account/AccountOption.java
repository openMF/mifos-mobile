package org.mifos.mobilebanking.models.templates.account;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajan Maurya on 10/03/17.
 */

public class AccountOption implements Parcelable {

    @SerializedName("accountId")
    Integer accountId;

    @SerializedName("accountNo")
    String accountNo;

    @SerializedName("accountType")
    AccountType accountType;

    @SerializedName("clientId")
    Long clientId;

    @SerializedName("clientName")
    String clientName;

    @SerializedName("officeId")
    Integer officeId;

    @SerializedName("officeName")
    String officeName;

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

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.accountId);
        dest.writeString(this.accountNo);
        dest.writeParcelable(this.accountType, flags);
        dest.writeValue(this.clientId);
        dest.writeString(this.clientName);
        dest.writeValue(this.officeId);
        dest.writeString(this.officeName);
    }

    public AccountOption() {
    }

    protected AccountOption(Parcel in) {
        this.accountId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.accountNo = in.readString();
        this.accountType = in.readParcelable(AccountType.class.getClassLoader());
        this.clientId = (Long) in.readValue(Long.class.getClassLoader());
        this.clientName = in.readString();
        this.officeId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.officeName = in.readString();
    }

    public static final Parcelable.Creator<AccountOption> CREATOR =
            new Parcelable.Creator<AccountOption>() {
                @Override
                public AccountOption createFromParcel(Parcel source) {
                    return new AccountOption(source);
                }

                @Override
                public AccountOption[] newArray(int size) {
                    return new AccountOption[size];
                }
            };
}
