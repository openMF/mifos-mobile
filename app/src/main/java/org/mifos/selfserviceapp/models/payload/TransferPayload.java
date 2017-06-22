package org.mifos.selfserviceapp.models.payload;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajan Maurya on 10/03/17.
 */

public class TransferPayload implements Parcelable {

    @SerializedName("fromOfficeId")
    Integer fromOfficeId;

    @SerializedName("fromClientId")
    Long fromClientId;

    @SerializedName("fromAccountType")
    Integer fromAccountType;

    @SerializedName("fromAccountId")
    Integer fromAccountId;

    @SerializedName("toOfficeId")
    Integer toOfficeId;

    @SerializedName("toClientId")
    Long toClientId;

    @SerializedName("toAccountType")
    Integer toAccountType;

    @SerializedName("toAccountId")
    Integer toAccountId;

    @SerializedName("transferDate")
    String transferDate;

    @SerializedName("transferAmount")
    Double transferAmount;

    @SerializedName("transferDescription")
    String transferDescription;

    String dateFormat = "dd MMMM yyyy";
    String locale = "en";

    public Integer getFromOfficeId() {
        return fromOfficeId;
    }

    public void setFromOfficeId(Integer fromOfficeId) {
        this.fromOfficeId = fromOfficeId;
    }

    public Long getFromClientId() {
        return fromClientId;
    }

    public void setFromClientId(Long fromClientId) {
        this.fromClientId = fromClientId;
    }

    public Integer getFromAccountType() {
        return fromAccountType;
    }

    public void setFromAccountType(Integer fromAccountType) {
        this.fromAccountType = fromAccountType;
    }

    public Integer getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Integer fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Integer getToOfficeId() {
        return toOfficeId;
    }

    public void setToOfficeId(Integer toOfficeId) {
        this.toOfficeId = toOfficeId;
    }

    public Long getToClientId() {
        return toClientId;
    }

    public void setToClientId(Long toClientId) {
        this.toClientId = toClientId;
    }

    public Integer getToAccountType() {
        return toAccountType;
    }

    public void setToAccountType(Integer toAccountType) {
        this.toAccountType = toAccountType;
    }

    public Integer getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Integer toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public Double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Double transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransferDescription() {
        return transferDescription;
    }

    public void setTransferDescription(String transferDescription) {
        this.transferDescription = transferDescription;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.fromOfficeId);
        dest.writeValue(this.fromClientId);
        dest.writeValue(this.fromAccountType);
        dest.writeValue(this.fromAccountId);
        dest.writeValue(this.toOfficeId);
        dest.writeValue(this.toClientId);
        dest.writeValue(this.toAccountType);
        dest.writeValue(this.toAccountId);
        dest.writeString(this.transferDate);
        dest.writeValue(this.transferAmount);
        dest.writeString(this.transferDescription);
        dest.writeString(this.dateFormat);
        dest.writeString(this.locale);
    }

    public TransferPayload() {
    }

    protected TransferPayload(Parcel in) {
        this.fromOfficeId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fromClientId = (Long) in.readValue(Long.class.getClassLoader());
        this.fromAccountType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fromAccountId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.toOfficeId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.toClientId = (Long) in.readValue(Long.class.getClassLoader());
        this.toAccountType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.toAccountId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.transferDate = in.readString();
        this.transferAmount = (Double) in.readValue(Double.class.getClassLoader());
        this.transferDescription = in.readString();
        this.dateFormat = in.readString();
        this.locale = in.readString();
    }

    public static final Parcelable.Creator<TransferPayload> CREATOR =
            new Parcelable.Creator<TransferPayload>() {
                @Override
                public TransferPayload createFromParcel(Parcel source) {
                    return new TransferPayload(source);
                }

                @Override
                public TransferPayload[] newArray(int size) {
                    return new TransferPayload[size];
                }
            };
}
