package org.mifos.mobilebanking.models.beneficary;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dilpreet on 16/6/17.
 */

public class BeneficiaryPayload implements Parcelable {
    String locale = "en_GB";

    @SerializedName("name")
    String name;

    @SerializedName("accountNumber")
    String accountNumber;

    @SerializedName("accountType")
    int accountType;

    @SerializedName("transferLimit")
    double transferLimit;

    @SerializedName("officeName")
    String officeName;

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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public double getTransferLimit() {
        return transferLimit;
    }

    public void setTransferLimit(double transferLimit) {
        this.transferLimit = transferLimit;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.locale);
        dest.writeString(this.name);
        dest.writeString(this.accountNumber);
        dest.writeInt(this.accountType);
        dest.writeDouble(this.transferLimit);
        dest.writeString(this.officeName);
    }

    public BeneficiaryPayload() {
    }

    protected BeneficiaryPayload(Parcel in) {
        this.locale = in.readString();
        this.name = in.readString();
        this.accountNumber = in.readString();
        this.accountType = in.readInt();
        this.transferLimit = in.readDouble();
        this.officeName = in.readString();
    }

    public static final Parcelable.Creator<BeneficiaryPayload> CREATOR =
            new Parcelable.Creator<BeneficiaryPayload>() {
        @Override
        public BeneficiaryPayload createFromParcel(Parcel source) {
            return new BeneficiaryPayload(source);
        }

        @Override
        public BeneficiaryPayload[] newArray(int size) {
            return new BeneficiaryPayload[size];
        }
    };
}
