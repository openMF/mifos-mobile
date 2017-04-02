package org.mifos.selfserviceapp.models.accounts.savings;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajan Maurya on 05/03/17.
 */

public class PaymentType implements Parcelable {

    @SerializedName("id")
    Integer id;

    @SerializedName("name")
    String name;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
    }

    public PaymentType() {
    }

    protected PaymentType(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
    }

    public static final Parcelable.Creator<PaymentType> CREATOR =
            new Parcelable.Creator<PaymentType>() {
                @Override
                public PaymentType createFromParcel(Parcel source) {
                    return new PaymentType(source);
                }

                @Override
                public PaymentType[] newArray(int size) {
                    return new PaymentType[size];
                }
            };
}
