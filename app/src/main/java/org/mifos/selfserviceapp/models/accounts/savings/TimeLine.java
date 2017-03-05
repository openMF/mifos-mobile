package org.mifos.selfserviceapp.models.accounts.savings;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajan Maurya on 05/03/17.
 */

public class TimeLine implements Parcelable {

    @SerializedName("submittedOnDate")
    List<Integer> submittedOnDate = new ArrayList<>();

    @SerializedName("submittedByUsername")
    String submittedByUsername;

    @SerializedName("submittedByFirstname")
    String submittedByFirstname;

    @SerializedName("submittedByLastname")
    String submittedByLastname;

    @SerializedName("approvedOnDate")
    List<Integer> approvedOnDate = new ArrayList<>();

    @SerializedName("approvedByUsername")
    String approvedByUsername;

    @SerializedName("approvedByFirstname")
    String approvedByFirstname;

    @SerializedName("approvedByLastname")
    String approvedByLastname;

    @SerializedName("activatedOnDate")
    List<Integer> activatedOnDate;

    @SerializedName("activatedByUsername")
    String activatedByUsername;

    @SerializedName("activatedByFirstname")
    String activatedByFirstname;

    @SerializedName("activatedByLastname")
    String activatedByLastname;

    public List<Integer> getSubmittedOnDate() {
        return submittedOnDate;
    }

    public void setSubmittedOnDate(List<Integer> submittedOnDate) {
        this.submittedOnDate = submittedOnDate;
    }

    public String getSubmittedByUsername() {
        return submittedByUsername;
    }

    public void setSubmittedByUsername(String submittedByUsername) {
        this.submittedByUsername = submittedByUsername;
    }

    public String getSubmittedByFirstname() {
        return submittedByFirstname;
    }

    public void setSubmittedByFirstname(String submittedByFirstname) {
        this.submittedByFirstname = submittedByFirstname;
    }

    public String getSubmittedByLastname() {
        return submittedByLastname;
    }

    public void setSubmittedByLastname(String submittedByLastname) {
        this.submittedByLastname = submittedByLastname;
    }

    public List<Integer> getApprovedOnDate() {
        return approvedOnDate;
    }

    public void setApprovedOnDate(List<Integer> approvedOnDate) {
        this.approvedOnDate = approvedOnDate;
    }

    public String getApprovedByUsername() {
        return approvedByUsername;
    }

    public void setApprovedByUsername(String approvedByUsername) {
        this.approvedByUsername = approvedByUsername;
    }

    public String getApprovedByFirstname() {
        return approvedByFirstname;
    }

    public void setApprovedByFirstname(String approvedByFirstname) {
        this.approvedByFirstname = approvedByFirstname;
    }

    public String getApprovedByLastname() {
        return approvedByLastname;
    }

    public void setApprovedByLastname(String approvedByLastname) {
        this.approvedByLastname = approvedByLastname;
    }

    public List<Integer> getActivatedOnDate() {
        return activatedOnDate;
    }

    public void setActivatedOnDate(List<Integer> activatedOnDate) {
        this.activatedOnDate = activatedOnDate;
    }

    public String getActivatedByUsername() {
        return activatedByUsername;
    }

    public void setActivatedByUsername(String activatedByUsername) {
        this.activatedByUsername = activatedByUsername;
    }

    public String getActivatedByFirstname() {
        return activatedByFirstname;
    }

    public void setActivatedByFirstname(String activatedByFirstname) {
        this.activatedByFirstname = activatedByFirstname;
    }

    public String getActivatedByLastname() {
        return activatedByLastname;
    }

    public void setActivatedByLastname(String activatedByLastname) {
        this.activatedByLastname = activatedByLastname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.submittedOnDate);
        dest.writeString(this.submittedByUsername);
        dest.writeString(this.submittedByFirstname);
        dest.writeString(this.submittedByLastname);
        dest.writeList(this.approvedOnDate);
        dest.writeString(this.approvedByUsername);
        dest.writeString(this.approvedByFirstname);
        dest.writeString(this.approvedByLastname);
        dest.writeList(this.activatedOnDate);
        dest.writeString(this.activatedByUsername);
        dest.writeString(this.activatedByFirstname);
        dest.writeString(this.activatedByLastname);
    }

    public TimeLine() {
    }

    protected TimeLine(Parcel in) {
        this.submittedOnDate = new ArrayList<Integer>();
        in.readList(this.submittedOnDate, Integer.class.getClassLoader());
        this.submittedByUsername = in.readString();
        this.submittedByFirstname = in.readString();
        this.submittedByLastname = in.readString();
        this.approvedOnDate = new ArrayList<Integer>();
        in.readList(this.approvedOnDate, Integer.class.getClassLoader());
        this.approvedByUsername = in.readString();
        this.approvedByFirstname = in.readString();
        this.approvedByLastname = in.readString();
        this.activatedOnDate = new ArrayList<Integer>();
        in.readList(this.activatedOnDate, Integer.class.getClassLoader());
        this.activatedByUsername = in.readString();
        this.activatedByFirstname = in.readString();
        this.activatedByLastname = in.readString();
    }

    public static final Parcelable.Creator<TimeLine> CREATOR = new Parcelable.Creator<TimeLine>() {
        @Override
        public TimeLine createFromParcel(Parcel source) {
            return new TimeLine(source);
        }

        @Override
        public TimeLine[] newArray(int size) {
            return new TimeLine[size];
        }
    };
}
