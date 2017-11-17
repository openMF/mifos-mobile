package org.mifos.mobilebanking.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by michaelsosnick on 12/11/16.
 */

public class Currency implements Parcelable {
    private String code;
    private String name;
    private int decimalPlaces;
    private String displaySymbol;
    private String nameCode;
    private String displayLabel;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public String getDisplaySymbol() {
        return displaySymbol;
    }

    public void setDisplaySymbol(String displaySymbol) {
        this.displaySymbol = displaySymbol;
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
        dest.writeInt(this.decimalPlaces);
        dest.writeString(this.displaySymbol);
        dest.writeString(this.nameCode);
        dest.writeString(this.displayLabel);
    }

    public Currency() {
    }

    protected Currency(Parcel in) {
        this.code = in.readString();
        this.name = in.readString();
        this.decimalPlaces = in.readInt();
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
