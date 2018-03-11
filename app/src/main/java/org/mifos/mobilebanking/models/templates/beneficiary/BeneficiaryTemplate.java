package org.mifos.mobilebanking.models.templates.beneficiary;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dilpreet on 14/6/17.
 */

public class BeneficiaryTemplate implements Parcelable {

    @SerializedName("accountTypeOptions")
    private List<AccountTypeOption> accountTypeOptions = null;

    public List<AccountTypeOption> getAccountTypeOptions() {
        return accountTypeOptions;
    }

    public void setAccountTypeOptions(List<AccountTypeOption> accountTypeOptions) {
        this.accountTypeOptions = accountTypeOptions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.accountTypeOptions);
    }

    public BeneficiaryTemplate() {
    }

    protected BeneficiaryTemplate(Parcel in) {
        this.accountTypeOptions = new ArrayList<>();
        in.readList(this.accountTypeOptions, AccountTypeOption.class.getClassLoader());
    }

    public static final Parcelable.Creator<BeneficiaryTemplate> CREATOR =
            new Parcelable.Creator<BeneficiaryTemplate>() {
        @Override
        public BeneficiaryTemplate createFromParcel(Parcel source) {
            return new BeneficiaryTemplate(source);
        }

        @Override
        public BeneficiaryTemplate[] newArray(int size) {
            return new BeneficiaryTemplate[size];
        }
    };
}
