package org.mifos.mobilebanking.models.templates.loans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajan Maurya on 16/07/16.
 */
public class Timeline implements Parcelable {

    @SerializedName("expectedDisbursementDate")
    List<Integer> expectedDisbursementDate;

    public List<Integer> getExpectedDisbursementDate() {
        return expectedDisbursementDate;
    }

    public void setExpectedDisbursementDate(List<Integer> expectedDisbursementDate) {
        this.expectedDisbursementDate = expectedDisbursementDate;
    }

    @Override
    public String toString() {
        return "Timeline{" +
                "expectedDisbursementDate=" + expectedDisbursementDate +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.expectedDisbursementDate);
    }

    public Timeline() {
    }

    protected Timeline(Parcel in) {
        this.expectedDisbursementDate = new ArrayList<>();
        in.readList(this.expectedDisbursementDate, Integer.class.getClassLoader());
    }

    public static final Creator<Timeline> CREATOR = new Creator<Timeline>() {
        @Override
        public Timeline createFromParcel(Parcel source) {
            return new Timeline(source);
        }

        @Override
        public Timeline[] newArray(int size) {
            return new Timeline[size];
        }
    };
}
