package org.mifos.mobilebanking.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by michaelsosnick on 12/11/16.
 */

public class ChargeCalculationType implements Parcelable {
    private int id;
    private String code; // example "chargeCalculationType.flat"
    private String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.code);
        dest.writeString(this.value);
    }

    public ChargeCalculationType() {
    }

    protected ChargeCalculationType(Parcel in) {
        this.id = in.readInt();
        this.code = in.readString();
        this.value = in.readString();
    }

    public static final Parcelable.Creator<ChargeCalculationType> CREATOR =
            new Parcelable.Creator<ChargeCalculationType>() {
                @Override
                public ChargeCalculationType createFromParcel(Parcel source) {
                    return new ChargeCalculationType(source);
                }

                @Override
                public ChargeCalculationType[] newArray(int size) {
                    return new ChargeCalculationType[size];
                }
            };
}
