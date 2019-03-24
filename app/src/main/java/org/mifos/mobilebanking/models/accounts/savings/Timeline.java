package org.mifos.mobilebanking.models.accounts.savings;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Timeline implements Parcelable {

    // Creator
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Timeline createFromParcel(Parcel in) {
            return new Timeline(in);
        }

        public Timeline[] newArray(int size) {
            return new Timeline[size];
        }
    };
    @SerializedName("submittedOnDate")
    private List<Integer> submittedOnDate;
    @SerializedName("submittedByUsername")
    private String submittedByUsername;
    @SerializedName("submittedByFirstname")
    private String submittedByFirstname;
    @SerializedName("submittedByLastname")
    private String submittedByLastname;
    @SerializedName("approvedOnDate")
    private List<Integer> approvedOnDate;
    @SerializedName("approvedByUsername")
    private String approvedByUsername;
    @SerializedName("approvedByFirstname")
    private String approvedByFirstname;
    @SerializedName("approvedByLastname")
    private String approvedByLastname;
    @SerializedName("activatedOnDate")
    private List<Integer> activatedOnDate;
    @SerializedName("activatedByUsername")
    private String activatedByUsername;
    @SerializedName("activatedByFirstname")
    private String activatedByFirstname;
    @SerializedName("activatedByLastname")
    private String activatedByLastname;
    @SerializedName("closedOnDate")
    private List<Integer> closedOnDate;

    // "De-parcel object
    public Timeline(Parcel in) {
        submittedByUsername = in.readString();
        submittedByFirstname = in.readString();
        submittedByLastname = in.readString();
        approvedByUsername = in.readString();
        approvedByFirstname = in.readString();
        approvedByLastname = in.readString();
        activatedByUsername = in.readString();
        activatedByFirstname = in.readString();
        activatedByLastname = in.readString();
        submittedOnDate = new ArrayList<>();
        in.readList(submittedOnDate, Integer.class.getClassLoader());
        activatedOnDate = new ArrayList<>();
        in.readList(activatedOnDate, Integer.class.getClassLoader());
        approvedOnDate = new ArrayList<>();
        in.readList(approvedOnDate, Integer.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(submittedByUsername);
        dest.writeString(submittedByFirstname);
        dest.writeString(submittedByLastname);
        dest.writeString(approvedByUsername);
        dest.writeString(approvedByFirstname);
        dest.writeString(approvedByLastname);
        dest.writeString(activatedByFirstname);
        dest.writeString(activatedByUsername);
        dest.writeString(activatedByLastname);
        dest.writeList(submittedOnDate);
        dest.writeList(activatedOnDate);
        dest.writeList(approvedOnDate);

    }

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

    public List<Integer> getClosedOnDate() {
        return closedOnDate;
    }

    public void setClosedOnDate(List<Integer> closedOnDate) {
        this.closedOnDate = closedOnDate;
    }

}
