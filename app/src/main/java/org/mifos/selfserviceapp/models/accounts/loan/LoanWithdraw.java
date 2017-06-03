package org.mifos.selfserviceapp.models.accounts.loan;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dilpreet on 7/6/17.
 */

public class LoanWithdraw implements Parcelable {

    @SerializedName("withdrawnOnDate")
    String withdrawnOnDate;
    @SerializedName("note")
    String note;

    String dateFormat = "dd MMMM yyyy";
    String locale = "en";

    public String getWithdrawnOnDate() {
        return withdrawnOnDate;
    }

    public void setWithdrawnOnDate(String withdrawnOnDate) {
        this.withdrawnOnDate = withdrawnOnDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.withdrawnOnDate);
        dest.writeString(this.note);
        dest.writeString(this.dateFormat);
        dest.writeString(this.locale);
    }

    public LoanWithdraw() {
    }

    protected LoanWithdraw(Parcel in) {
        this.withdrawnOnDate = in.readString();
        this.note = in.readString();
        this.dateFormat = in.readString();
        this.locale = in.readString();
    }

    public static final Parcelable.Creator<LoanWithdraw> CREATOR =
            new Parcelable.Creator<LoanWithdraw>() {
        @Override
        public LoanWithdraw createFromParcel(Parcel source) {
            return new LoanWithdraw(source);
        }

        @Override
        public LoanWithdraw[] newArray(int size) {
            return new LoanWithdraw[size];
        }
    };
}
