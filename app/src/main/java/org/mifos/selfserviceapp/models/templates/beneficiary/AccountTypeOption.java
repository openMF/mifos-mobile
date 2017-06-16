package org.mifos.selfserviceapp.models.templates.beneficiary;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dilpreet on 14/6/17.
 */

public class AccountTypeOption implements Parcelable {

    @SerializedName("id")
    private Integer id;

    @SerializedName("code")
    private String code;

    @SerializedName("value")
    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        dest.writeValue(this.id);
        dest.writeString(this.code);
        dest.writeString(this.value);
    }

    public AccountTypeOption() {
    }

    protected AccountTypeOption(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.code = in.readString();
        this.value = in.readString();
    }

    public static final Parcelable.Creator<AccountTypeOption> CREATOR =
            new Parcelable.Creator<AccountTypeOption>() {
        @Override
        public AccountTypeOption createFromParcel(Parcel source) {
            return new AccountTypeOption(source);
        }

        @Override
        public AccountTypeOption[] newArray(int size) {
            return new AccountTypeOption[size];
        }
    };
}
