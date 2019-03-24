package org.mifos.mobilebanking.models.accounts.loan;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Timeline implements Parcelable {

    // Creator
    public static final Creator CREATOR = new Creator() {
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
    @SerializedName("actualDisbursementDate")
    private List<Integer> actualDisbursementDate;
    @SerializedName("expectedDisbursementDate")
    private List<Integer> expectedDisbursementDate;
    @SerializedName("disbursedByUsername")
    private String disbursedByUsername;
    @SerializedName("disbursedByFirstname")
    private String disbursedByFirstname;
    @SerializedName("disbursedByLastname")
    private String disbursedByLastname;
    @SerializedName("closedOnDate")
    private List<Integer> closedOnDate;
    @SerializedName("expectedMaturityDate")
    private List<Integer> expectedMaturityDate;
    @SerializedName("withdrawnOnDate")
    private List<Integer> withdrawnOnDate;

    // "De-parcel object
    public Timeline(Parcel in) {
        submittedByUsername = in.readString();
        submittedByFirstname = in.readString();
        submittedByLastname = in.readString();
        approvedByUsername = in.readString();
        approvedByFirstname = in.readString();
        approvedByLastname = in.readString();
        disbursedByUsername = in.readString();
        disbursedByFirstname = in.readString();
        disbursedByLastname = in.readString();
        submittedOnDate = new ArrayList<>();
        in.readList(submittedOnDate, Integer.class.getClassLoader());
        expectedDisbursementDate = new ArrayList<>();
        in.readList(expectedDisbursementDate, Integer.class.getClassLoader());
        approvedOnDate = new ArrayList<>();
        in.readList(approvedOnDate, Integer.class.getClassLoader());
        expectedMaturityDate = new ArrayList<>();
        in.readList(expectedMaturityDate, Integer.class.getClassLoader());
        withdrawnOnDate = new ArrayList<>();
        in.readList(withdrawnOnDate, Integer.class.getClassLoader());
        closedOnDate = new ArrayList<>();
        in.readList(closedOnDate, Integer.class.getClassLoader());

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
        dest.writeString(disbursedByFirstname);
        dest.writeString(disbursedByUsername);
        dest.writeString(disbursedByLastname);
        dest.writeList(submittedOnDate);
        dest.writeList(expectedDisbursementDate);
        dest.writeList(actualDisbursementDate);
        dest.writeList(approvedOnDate);
        dest.writeList(withdrawnOnDate);
        dest.writeList(closedOnDate);



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

    public List<Integer> getExpectedDisbursementDate() {
        return expectedDisbursementDate;
    }

    public void setExpectedDisbursementDate(List<Integer> expectedDisbursementDate) {
        this.expectedDisbursementDate = expectedDisbursementDate;
    }

    public String getDisbursedByUsername() {
        return disbursedByUsername;
    }

    public void setDisbursedByUsername(String disbursedByUsername) {
        this.disbursedByUsername = disbursedByUsername;
    }

    public String getDisbursedByFirstname() {
        return disbursedByFirstname;
    }

    public void setDisbursedByFirstname(String disbursedByFirstname) {
        this.disbursedByFirstname = disbursedByFirstname;
    }

    public String getDisbursedByLastname() {
        return disbursedByLastname;
    }

    public void setDisbursedByLastname(String disbursedByLastname) {
        this.disbursedByLastname = disbursedByLastname;
    }

    public List<Integer> getClosedOnDate() {
        return closedOnDate;
    }

    public void setClosedOnDate(List<Integer> closedOnDate) {
        this.closedOnDate = closedOnDate;
    }

    public List<Integer> getActualDisbursementDate() {
        return actualDisbursementDate;
    }

    public void setActualDisbursementDate(List<Integer> actualDisbursementDate) {
        this.actualDisbursementDate = actualDisbursementDate;
    }

    public List<Integer> getExpectedMaturityDate() {
        return expectedMaturityDate;
    }

    public void setExpectedMaturityDate(List<Integer> expectedMaturityDate) {
        this.expectedMaturityDate = expectedMaturityDate;
    }

    public List<Integer> getWithdrawnOnDate() {
        return withdrawnOnDate;
    }

    public void setWithdrawnOnDate(List<Integer> withdrawnOnDate) {
        this.withdrawnOnDate = withdrawnOnDate;
    }

}
