package org.mifos.mobilebanking.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by michaelsosnick on 12/11/16.
 */

public class ChargeTimeType implements Parcelable {
    private int id;
    private String code;
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

    public ChargeTimeType() {
    }

    protected ChargeTimeType(Parcel in) {
        this.id = in.readInt();
        this.code = in.readString();
        this.value = in.readString();
    }

    public static final Parcelable.Creator<ChargeTimeType> CREATOR =
            new Parcelable.Creator<ChargeTimeType>() {
                @Override
                public ChargeTimeType createFromParcel(Parcel source) {
                    return new ChargeTimeType(source);
                }

                @Override
                public ChargeTimeType[] newArray(int size) {
                    return new ChargeTimeType[size];
                }
            };
}
