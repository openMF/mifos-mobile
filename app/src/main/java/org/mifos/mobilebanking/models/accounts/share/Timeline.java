package org.mifos.mobilebanking.models.accounts.share;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Timeline implements Parcelable {

    @SerializedName("submittedOnDate")
    @Expose
    private List<Integer> submittedOnDate = null;
    @SerializedName("submittedByUsername")
    @Expose
    private String submittedByUsername;
    @SerializedName("submittedByFirstname")
    @Expose
    private String submittedByFirstname;
    @SerializedName("submittedByLastname")
    @Expose
    private String submittedByLastname;
    @SerializedName("approvedDate")
    @Expose
    private List<Integer> approvedDate = null;
    @SerializedName("approvedByUsername")
    @Expose
    private String approvedByUsername;
    @SerializedName("approvedByFirstname")
    @Expose
    private String approvedByFirstname;
    @SerializedName("approvedByLastname")
    @Expose
    private String approvedByLastname;
    @SerializedName("activatedDate")
    @Expose
    private List<Integer> activatedDate = null;
    @SerializedName("activatedByUsername")
    @Expose
    private String activatedByUsername;
    @SerializedName("activatedByFirstname")
    @Expose
    private String activatedByFirstname;
    @SerializedName("activatedByLastname")
    @Expose
    private String activatedByLastname;

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

    public List<Integer> getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(List<Integer> approvedDate) {
        this.approvedDate = approvedDate;
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

    public List<Integer> getActivatedDate() {
        return activatedDate;
    }

    public void setActivatedDate(List<Integer> activatedDate) {
        this.activatedDate = activatedDate;
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
        dest.writeList(this.approvedDate);
        dest.writeString(this.approvedByUsername);
        dest.writeString(this.approvedByFirstname);
        dest.writeString(this.approvedByLastname);
        dest.writeList(this.activatedDate);
        dest.writeString(this.activatedByUsername);
        dest.writeString(this.activatedByFirstname);
        dest.writeString(this.activatedByLastname);
    }

    public Timeline() {
    }

    protected Timeline(Parcel in) {
        this.submittedOnDate = new ArrayList<>();
        in.readList(this.submittedOnDate, Integer.class.getClassLoader());
        this.submittedByUsername = in.readString();
        this.submittedByFirstname = in.readString();
        this.submittedByLastname = in.readString();
        this.approvedDate = new ArrayList<>();
        in.readList(this.approvedDate, Integer.class.getClassLoader());
        this.approvedByUsername = in.readString();
        this.approvedByFirstname = in.readString();
        this.approvedByLastname = in.readString();
        this.activatedDate = new ArrayList<>();
        in.readList(this.activatedDate, Integer.class.getClassLoader());
        this.activatedByUsername = in.readString();
        this.activatedByFirstname = in.readString();
        this.activatedByLastname = in.readString();
    }

    public static final Parcelable.Creator<Timeline> CREATOR = new Parcelable.Creator<Timeline>() {
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
