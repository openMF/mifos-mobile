package org.mifos.selfserviceapp.models.accounts.loan;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Summary implements Parcelable {

    @SerializedName("principalDisbursed")
    private double principalDisbursed;

    @SerializedName("principalPaid")
    private double principalPaid;

    @SerializedName("interestCharged")
    private double interestCharged;

    @SerializedName("interestPaid")
    private double interestPaid;

    public double getPrincipalDisbursed() {
        return principalDisbursed;
    }

    public void setPrincipalDisbursed(double principalDisbursed) {
        this.principalDisbursed = principalDisbursed;
    }

    public double getPrincipalPaid() {
        return principalPaid;
    }

    public void setPrincipalPaid(double principalPaid) {
        this.principalPaid = principalPaid;
    }

    public double getInterestCharged() {
        return interestCharged;
    }

    public void setInterestCharged(double interestCharged) {
        this.interestCharged = interestCharged;
    }

    public double getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(double interestPaid) {
        this.interestPaid = interestPaid;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "principalDisbursed=" + principalDisbursed +
                ", principalPaid=" + principalPaid +
                ", interestCharged=" + interestCharged +
                ", interestPaid=" + interestPaid +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.principalDisbursed);
        dest.writeDouble(this.principalPaid);
        dest.writeDouble(this.interestCharged);
        dest.writeDouble(this.interestPaid);
    }

    public Summary() {
    }

    protected Summary(Parcel in) {
        this.principalDisbursed = in.readDouble();
        this.principalPaid = in.readDouble();
        this.interestCharged = in.readDouble();
        this.interestPaid = in.readDouble();
    }

    public static final Parcelable.Creator<Summary> CREATOR = new Parcelable.Creator<Summary>() {
        @Override
        public Summary createFromParcel(Parcel source) {
            return new Summary(source);
        }

        @Override
        public Summary[] newArray(int size) {
            return new Summary[size];
        }
    };
}