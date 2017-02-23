package org.mifos.selfserviceapp.models.client;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.mifos.selfserviceapp.models.accounts.loan.LoanAccount;
import org.mifos.selfserviceapp.models.accounts.savings.SavingAccount;
import org.mifos.selfserviceapp.models.accounts.share.ShareAccount;

import java.util.ArrayList;
import java.util.List;

public class ClientAccounts implements Parcelable {

    private List<LoanAccount> loanAccounts = new ArrayList<>();
    private List<SavingAccount> savingsAccounts = new ArrayList<>();

    @SerializedName("shareAccounts")
    private List<ShareAccount> shareAccounts = new ArrayList<>();

    public List<ShareAccount> getShareAccounts() {
        return shareAccounts;
    }

    public void setShareAccounts(List<ShareAccount> shareAccounts) {
        this.shareAccounts = shareAccounts;
    }

    public ClientAccounts() {
    }

    protected ClientAccounts(Parcel in) {
        this.loanAccounts = in.createTypedArrayList(LoanAccount.CREATOR);
        this.savingsAccounts = new ArrayList<SavingAccount>();
        in.readList(this.savingsAccounts, SavingAccount.class.getClassLoader());
    }

    public List<LoanAccount> getLoanAccounts() {
        return loanAccounts;
    }

    public void setLoanAccounts(List<LoanAccount> loanAccounts) {
        this.loanAccounts = loanAccounts;
    }

    public ClientAccounts withLoanAccounts(List<LoanAccount> loanAccounts) {
        this.loanAccounts = loanAccounts;
        return this;
    }

    public List<SavingAccount> getSavingsAccounts() {
        return savingsAccounts;
    }

    public void setSavingsAccounts(List<SavingAccount> savingsAccounts) {
        this.savingsAccounts = savingsAccounts;
    }

    public ClientAccounts withSavingsAccounts(List<SavingAccount> savingsAccounts) {
        this.savingsAccounts = savingsAccounts;
        return this;
    }

    public List<SavingAccount> getRecurringSavingsAccounts() {
        return getSavingsAccounts(true);
    }

    public List<SavingAccount> getNonRecurringSavingsAccounts() {
        return getSavingsAccounts(false);
    }

    private List<SavingAccount> getSavingsAccounts(boolean wantRecurring) {
        List<SavingAccount> result = new ArrayList<SavingAccount>();
        if (this.savingsAccounts != null) {
            for (SavingAccount account : savingsAccounts) {
                if (account.isRecurring() == wantRecurring) {
                    result.add(account);
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "ClientAccounts{" +
                "loanAccounts=" + loanAccounts +
                ", savingsAccounts=" + savingsAccounts +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(loanAccounts);
        dest.writeList(this.savingsAccounts);
    }

    public static final Parcelable.Creator<ClientAccounts> CREATOR = new Parcelable
            .Creator<ClientAccounts>() {
        @Override
        public ClientAccounts createFromParcel(Parcel source) {
            return new ClientAccounts(source);
        }

        @Override
        public ClientAccounts[] newArray(int size) {
            return new ClientAccounts[size];
        }
    };
}
