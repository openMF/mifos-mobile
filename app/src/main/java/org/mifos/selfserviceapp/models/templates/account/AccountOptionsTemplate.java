package org.mifos.selfserviceapp.models.templates.account;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajan Maurya on 10/03/17.
 */
public class AccountOptionsTemplate implements Parcelable {

    @SerializedName("fromAccountOptions")
    List<AccountOption> fromAccountOptions = new ArrayList<>();

    @SerializedName("toAccountOptions")
    List<AccountOption> toAccountOptions = new ArrayList<>();

    public List<AccountOption> getFromAccountOptions() {
        return fromAccountOptions;
    }

    public void setFromAccountOptions(
            List<AccountOption> fromAccountOptions) {
        this.fromAccountOptions = fromAccountOptions;
    }

    public List<AccountOption> getToAccountOptions() {
        return toAccountOptions;
    }

    public void setToAccountOptions(
            List<AccountOption> toAccountOptions) {
        this.toAccountOptions = toAccountOptions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.fromAccountOptions);
        dest.writeTypedList(this.toAccountOptions);
    }

    public AccountOptionsTemplate() {
    }

    protected AccountOptionsTemplate(Parcel in) {
        this.fromAccountOptions = in.createTypedArrayList(AccountOption.CREATOR);
        this.toAccountOptions = in.createTypedArrayList(AccountOption.CREATOR);
    }

    public static final Parcelable.Creator<AccountOptionsTemplate> CREATOR =
            new Parcelable.Creator<AccountOptionsTemplate>() {
                @Override
                public AccountOptionsTemplate createFromParcel(Parcel source) {
                    return new AccountOptionsTemplate(source);
                }

                @Override
                public AccountOptionsTemplate[] newArray(int size) {
                    return new AccountOptionsTemplate[size];
                }
            };
}
