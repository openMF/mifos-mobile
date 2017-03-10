package org.mifos.selfserviceapp.models.templates.loans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajan Maurya on 16/07/16.
 */
public class TransactionProcessingStrategyOptions implements Parcelable {

    @SerializedName("id")
    Integer id;

    @SerializedName("code")
    String code;

    @SerializedName("name")
    String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TransactionProcessingStrategyOptions{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.code);
        dest.writeString(this.name);
    }

    public TransactionProcessingStrategyOptions() {
    }

    protected TransactionProcessingStrategyOptions(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.code = in.readString();
        this.name = in.readString();
    }

    public static final Creator<TransactionProcessingStrategyOptions> CREATOR =
            new Creator<TransactionProcessingStrategyOptions>() {
                @Override
                public TransactionProcessingStrategyOptions createFromParcel(Parcel source) {
                    return new TransactionProcessingStrategyOptions(source);
                }

                @Override
                public TransactionProcessingStrategyOptions[] newArray(int size) {
                    return new TransactionProcessingStrategyOptions[size];
                }
            };
}
