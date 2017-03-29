package org.mifos.selfserviceapp.models.accounts.loan;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dilpreet on 27/2/17.
 */

public class Currency implements Parcelable {

    @SerializedName("code")
    String code;

    @SerializedName("name")
    String name;

    @SerializedName("decimalPlaces")
    Double decimalPlaces;

    @SerializedName("inMultiplesOf")
    Double inMultiplesOf;

    @SerializedName("displaySymbol")
    String displaySymbol;

    @SerializedName("nameCode")
    String nameCode;

    @SerializedName("displayLabel")
    String displayLabel;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplaySymbol() {
        return displaySymbol;
    }

    public void setDisplaySymbol(String displaySymbol) {
        this.displaySymbol = displaySymbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(Double decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public Double getInMultiplesOf() {
        return inMultiplesOf;
    }

    public void setInMultiplesOf(Double inMultiplesOf) {
        this.inMultiplesOf = inMultiplesOf;
    }

    public String getNameCode() {
        return nameCode;
    }

    public void setNameCode(String nameCode) {
        this.nameCode = nameCode;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }

    public void setDisplayLabel(String displayLabel) {
        this.displayLabel = displayLabel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeValue(this.decimalPlaces);
        dest.writeValue(this.inMultiplesOf);
        dest.writeString(this.displaySymbol);
        dest.writeString(this.nameCode);
        dest.writeString(this.displayLabel);
    }

    public Currency() {
    }

    protected Currency(Parcel in) {
        this.code = in.readString();
        this.name = in.readString();
        this.decimalPlaces = (Double) in.readValue(Double.class.getClassLoader());
        this.inMultiplesOf = (Double) in.readValue(Double.class.getClassLoader());
        this.displaySymbol = in.readString();
        this.nameCode = in.readString();
        this.displayLabel = in.readString();
    }

    public static final Parcelable.Creator<Currency> CREATOR = new Parcelable.Creator<Currency>() {
        @Override
        public Currency createFromParcel(Parcel source) {
            return new Currency(source);
        }

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };
}