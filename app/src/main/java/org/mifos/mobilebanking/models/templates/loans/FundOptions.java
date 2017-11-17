package org.mifos.mobilebanking.models.templates.loans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajan Maurya on 16/07/16.
 */
public class FundOptions implements Parcelable {

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

    public FundOptions() {
    }

    protected FundOptions(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
    }

    public static final Creator<FundOptions> CREATOR = new Creator<FundOptions>() {
        @Override
        public FundOptions createFromParcel(Parcel source) {
            return new FundOptions(source);
        }

        @Override
        public FundOptions[] newArray(int size) {
            return new FundOptions[size];
        }
    };
}
